
package jsise20170916;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/*
 * JSiSEに向けてのsimulationプログラム
 */

public class simulation {

	public static void main(String args[]) {

		int j; // 項目の添え字(番号)
		int i; // 受検者の添え字(番号)
		int n; // 真の能力値の添え字(番号)
		int res; // 反応データ
		double estimateTheta; // 受検者の能力値を格納する変数

		int id = 0;

		/*
		 * (θ= -2.0~2.0をそれぞれ)真の能力値として仮定する
		 */
		ArrayList<Double> trueThetaList = new ArrayList<Double>();
		trueThetaList.add(-2.0);
		trueThetaList.add(-1.0);
		trueThetaList.add(0.0);
		trueThetaList.add(1.0);
		trueThetaList.add(2.0);

		/*
		 * 回答データを保持する．添字1が真の能力値の添字，添字2がユーザ，添字3が課題番号
		 */
		int history[][][] = new int[5][501][21];

		/*
		 * 今回は2PLMで能力値推定を行うので、各項目、各カテゴリごとの項目パラメータを設定する
		 * discriminationが識別力、difficultyが困難度
		 */

		double discrimination[] = { // 段階反応モデルでは各カテゴリの識別力は同じとして仮定する
				1.87, 0.92, 1.05, 2.63, 2.52, 1.92, 2.51, 1.02, 1.67, 2.31, 1.79, 1.83, 2.08, 3.04, 2.14, 2.04, 1.58,
				1.95, 1.88, 0.95, 3.03 };

		double difficulty[][] = { // difficultyにはb*を入れるので，困難度レベル0には，-10(どうして？)を入れて欠測値としておく
				// 豊田先生の本P161を参照(b*)

				// 右に行くほど大きい
				{ -10, -1.27230, 0.54577 }, { -10, -3.03491, -1.81493, 0.00968 }, { -10, -1.88432, -0.46066, 1.53130 },
				{ -10, -0.47117, 0.48908 }, { -10, -0.74941, 0.40782 }, { -10, -1.38049, -0.53871, 0.72222 },
				{ -10, -0.85286, -0.67089, 0.48119 },

				{ -10, -2.44305, -0.48762, 0.89402 }, { -10, -1.52999, -0.61862, 0.70426 },
				{ -10, -1.14859, -0.54379, 0.51760 }, { -10, -1.44485, -0.53047, 0.18721 },
				{ -10, -1.24086, -0.53679, 0.55062 }, { -10, -1.25686, -0.44866, 0.60954 },
				{ -10, -0.67136, -0.33282, 0.13970 },

				{ -10, -1.01962, -0.66990, 0.59199 }, { -10, -0.88357, -0.72521, 0.45745 },
				{ -10, -1.64294, -0.64134, 0.69955 }, { -10, -0.87116, 0.60426 }, { -10, -0.91560, 0.80422 },
				{ -10, -2.35397, -1.78813, 0.71795 }, { -10, -0.63087, -0.49943, 0.29477 } };

		int maxDifficultyLevel[] = { // 項目の最高困難度レベル．添字は項目番号j．
				2, 3, 3, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 3, 3 };

		try {

			// ファイル書き出し用
			File csv = new File("C:/Users/J14-8002/pleiades/workspace/IRT_Simulation/result/simulation_0916.csv");
			// 追記モード
			// 文字コードの指定
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csv), "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write("id,trueTheta,user,item,category,estimateTheta");
			bw.newLine();

			/*
			 * 真の能力値(θ= -2.0~2.0をそれぞれ)それぞれ100人分繰り返す
			 */
			for (n = 0; n < trueThetaList.size(); n++) {
				for (i = 0; i < 100; i++) {
					estimateTheta = 0.0; // 能力値の初期値(どうして0.0?)

					for (j = 0; j < 21; j++) {
						res = res(j, trueThetaList.get(n), discrimination, difficulty, maxDifficultyLevel);
						history[n][i][j] = res; // 真の能力値の添字がn，ユーザの添字がi，課題番号がjのとき，カテゴリkで反応

						/*
						 * 能力値を求めるには、反応データ、項目の識別力、困難度、推定したθが必要
						 */
						estimateTheta = estimateTheta(i, n, j, discrimination, difficulty, maxDifficultyLevel, history);
						System.out.println("能力値" + trueThetaList.get(n) + " 推定した能力値 " + estimateTheta + " ユーザ "
								+ (n + 1) + " 課題" + (j + 1) + " カテゴリ " + res + "で反応");
						id++;
						bw.write(id + "," + trueThetaList.get(n) + "," + (n + 1) + "," + (j + 1) + "," + res + ","
								+ estimateTheta);
						bw.newLine();
					}
				}
			}
			bw.close();
		} catch (IOException ex) {
			// 例外時処理
			ex.printStackTrace();
		}
	}

	/*
	 * 反応データを求めるプログラム
	 */
	public static int res(int j, double trueTheta, double discrimination[], double difficulty[][],
			int maxDifficultyLevel[]) {

		// 0〜1未満のランダム値を生成
		double random = (double) (Math.random());
		int res = 0;

		double pj0a = 1;
		double pj1a;
		double pj2a;
		double pj3a;

		// 最新の能力パラメータを用いて，どの項目と反応するか求める
		switch (maxDifficultyLevel[j]) {
		case 3:// 最大困難度レベルが3のとき

			pj1a = 1 / (1 + (Math.exp(-1.7 * discrimination[j] * (trueTheta - difficulty[j][1]))));
			pj2a = 1 / (1 + (Math.exp(-1.7 * discrimination[j] * (trueTheta - difficulty[j][2]))));
			pj3a = 1 / (1 + (Math.exp(-1.7 * discrimination[j] * (trueTheta - difficulty[j][3]))));
			;

			/*
			 * if文のアルゴリズムは加藤先生の本P223を参照する if文の条件分岐間違ってないか？
			 */

			if (random < pj3a) {
				res = 3;
			} else if (pj3a <= random && random < pj2a) {
				res = 2;
			} else if (pj2a <= random && random < pj1a) {
				res = 1;
			} else if (pj1a <= random && random <= pj0a) {
				res = 0;
			}
			return res;

		case 2:// 最大困難度レベルが2のとき

			pj1a = 1 / (1 + (Math.exp(-1.7 * discrimination[j] * (trueTheta - difficulty[j][1]))));
			pj2a = 1 / (1 + (Math.exp(-1.7 * discrimination[j] * (trueTheta - difficulty[j][2]))));

			if (random < pj2a) {
				res = 2;
			} else if (pj2a <= random && random < pj1a) {
				res = 1;
			} else if (pj1a <= random) {
				res = 0;
			}
			return res;
		}
		return res;
	}

	/*
	 * 現在の能力値を推定(ベイズのEAP)
	 */
	public static double estimateTheta(int i, int n, int j, double discrimination[], double difficulty[][],
			int maxDifficultyLevel[], int history[][][]) {

		/*
		 * 求積点
		 */
		double[] Xm = { -4.15989, -3.92869, -3.69862, -3.46959, -3.24151, -3.01432, -2.78794, -2.5623, -2.33732,
				-2.11295, -1.88912, -1.66576, -1.44283, -1.22025, -0.997977, -0.775951, -0.554115, -0.332415, -0.110796,
				0.110796, 0.332415, 0.554115, 0.775951, 0.997977, 1.22025, 1.44283, 1.66576, 1.88912, 2.11295, 2.33732,
				2.5623, 2.78794, 3.01432, 3.24151, 3.46959, 3.69862, 3.92869, 4.15989 };

		/*
		 * 重み 総和が1になるように基準化した
		 */
		double[] Wm = { 7.08E-9, 4.57E-8, 2.63E-7, 1.35E-6, 6.22E-6, 2.57E-5, 9.52E-5, 3.17E-4, 9.53E-4, 0.00257927,
				0.006303, 0.0139157, 0.027779, 0.0501758, 0.082052, 0.121538, 0.16313, 0.19846, 0.21889, 0.21889,
				0.19846, 0.16313, 0.121538, 0.082052, 0.0501758, 0.027779, 0.0139157, 0.006303, 0.00257927, 9.53E-4,
				3.17E-4, 9.52E-5, 2.57E-5, 6.22E-6, 1.35E-6, 2.63E-7, 4.57E-8, 7.08E-9 };

		double result; // 能力値を格納する変数
		double ICC1;
		double ICC2;

		double Numerator[] = new double[Xm.length];// ベイズの分子
		double Denominator[] = new double[Xm.length]; // ベイズの分母

		double Nsum = 0.0;// 分子の和
		double Dsum = 0.0;// 分母の和

		for (int l = 0; l < Xm.length; l++) {
			result = 1;
			for (int m = 0; m < (j + 1); m++) {// 課題数分繰り返す

				switch (maxDifficultyLevel[m]) {

				case 3:// 最大困難度レベルが3のとき
					switch (history[n][i][m]) {
					case 0:
						ICC1 = 1.0;
						ICC2 = 1.0 / (1.0 + Math.exp(-1.7 * discrimination[m] * (Xm[l] - difficulty[m][1])));
						result *= ICC1 - ICC2;
						break;
					case 1:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * discrimination[m] * (Xm[l] - difficulty[m][1])));
						ICC2 = 1.0 / (1.0 + Math.exp( -1.7 * discrimination[m] * (Xm[l] - difficulty[m][2])));
						result *= ICC1 - ICC2;
						break;
					case 2:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * discrimination[m] * (Xm[l] - difficulty[m][2])));
						ICC2 = 1.0 / (1.0 + Math.exp(-1.7 * discrimination[m] * (Xm[l] - difficulty[m][3])));
						result *= ICC1 - ICC2;
						break;
					case 3:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * discrimination[m] * (Xm[l] - difficulty[m][3])));
						result *= ICC1;
						ICC2 = 0.0;
					default:
						break;
					}

				case 2:// 最大困難度レベルが2のとき
					switch (history[n][i][m]) {
					case 0:
						ICC1 = 1.0;
						ICC2 = 1.0 / (1.0 + Math.exp(l-1.7 * discrimination[m] * (Xm[l] - difficulty[m][1])));
						result *= ICC1 - ICC2;
						break;
					case 1:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * discrimination[m] * (Xm[l] - difficulty[m][1])));
						ICC2 = 1.0 / (1.0 + Math.exp(-1.7 * discrimination[m] * (Xm[l] - difficulty[m][2])));
						result *= ICC1 - ICC2;
						break;
					case 2:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * discrimination[m] * (Xm[l] - difficulty[m][2])));
						result *= ICC1;
						ICC2 = 0.0;
					default:
						break;
					}
				}
			}
			Numerator[l] = result * Xm[l] * Wm[l];
			Denominator[l] = result * Wm[l];
		}

		for (int h = 0; h < Xm.length; h++) {

			Nsum += Numerator[h];
			Dsum += Denominator[h];
		}

		double estimateTheta = Nsum / Dsum;

		return estimateTheta;
	}

}