package jsise20190325;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/*
 * JSET全国大会に向けてのシミュレーションプログラム
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
		int history[][][] = new int[5][51][18];

		/*
		 * 今回は1PLMで能力値推定を行うので、各カテゴリごとの困難度パラメータを設定する difficultyが困難度
		 */
		double difficulty[][] = {
				// 今回は決め打ちでやってます。本当はちゃんとしたルーブリックでやりたい
				{ -10, -3.34, -2.55, -1.33, 0.0, 0.51 }, { -10, -3.01, -2.23, -1.08, -0.65, 0.13 },
				{ -10, -0.47, 0.00, 0.98, 1.96, 3.82 }, { -10, 1.00, 2.21, 2.43, 3.50, 4.00 },
				{ -10, -2.76, -2.42, -1.48, 0.74, 1.42 }, { -10, -0.91, 0.85, 1.74, 2.52, 3.64 },
				{ -10, -1.41, 0.0, 0.98, 1.93, 3.84 }, { -10, 0.83, 1.45, 1.99, 2.61, 3.48 },
				{ -10, 0.81, 1.65, 2.74, 3.52, 3.64 }, { -10, -1.63, -0.43, 0.08, 1.65, 2.51 },
				{ -10, 0.01, 0.76, 1.88, 2.73, 3.62 }, { -10, -2.74, -1.75, -1.21, 0.50, 1.78 },
				{ -10, -2.41, -1.31, 0.98, 1.62, 2.64 }, { -10, 1.31, 2.45, 2.95, 3.31, 3.75 },
				{ -10, 1.14, 2.35, 2.94, 3.32, 3.74 }, { -10, 1.00, 2.21, 2.43, 3.11, 3.47 },
				{ -10, -0.74, 0.45, 1.53, 2.50, 3.78 }, { -10, -3.14, -1.45, 0.53, 1.50, 3.11 }
		};

		// 項目の最高困難度レベル．添字は項目番号j
		int maxDifficultyLevel[] = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };

		try {

			// ファイル書き出し用
			File csv = new File(
					"/Applications/Eclipse_4.8.0.app/Contents/workspace/IRT_Simulation/result/simulation_jsise_20190325.csv");
			// 追記モード
			// 文字コードの指定
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csv), "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write("id,trueTheta,user,category,estimateTheta");
			bw.newLine();

			/*
			 * 真の能力値(θ= -2.0~2.0をそれぞれ)それぞれ100人分繰り返す
			 */
			for (n = 0; n < trueThetaList.size(); n++) {
				for (i = 0; i < 10; i++) {
					estimateTheta = 0.0; // 能力値の初期値

					// resをエクセルで行に出力したいので、配列に格納する ここでは、宣言
					ArrayList<Integer> resList = new ArrayList<Integer>();
					double postTrueTheta = trueThetaList.get(n);

					for (j = 0; j < 18; j++) {

						if (j == 0) {
							res = res(j, trueThetaList.get(n), difficulty, maxDifficultyLevel);
						} else {
							res = res(j, postTrueTheta, difficulty, maxDifficultyLevel);
						}
						resList.add(res);
						history[n][i][j] = res; // 真の能力値の添字がn，ユーザの添字がi，課題番号がjのとき，カテゴリkで反応

						/*
						 * 能力値を求めるには、反応データ、項目の識別力、困難度、推定したθが必要
						 */
						estimateTheta = estimateTheta(i, n, j, difficulty, maxDifficultyLevel, history);
					}
					bw.write(id + "," + trueThetaList.get(n) + "," + (n + 1) + "," + resList + "," + estimateTheta);
					bw.newLine();
					id++;
				}
			}
			bw.close();
		} catch (IOException ex) {
			// 例外時処理
			ex.printStackTrace();
		}
	}

	/*
	 * 反応データ"res"を求めるプログラム
	 */
	public static int res(int j, double trueTheta, double difficulty[][], int maxDifficultyLevel[]) {

		// 0〜1未満のランダム値を生成
		double random = (double) (Math.random());
		int res = 0;

		double pj0 = 1;
		double pj1;
		double pj2;
		double pj3;
		double pj4;
		double pj5;

		// 最新の能力パラメータを用いて，どの項目と反応するか求める
		// 今回は、1PLMで考えるので、Da == 1.7として考える
		switch (maxDifficultyLevel[j]) {

		case 5:// 最大困難度レベルが5のとき

			pj1 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][1]))));
			pj2 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][2]))));
			pj3 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][3]))));
			pj4 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][4]))));
			pj5 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][5]))));

			if (random < pj5) {
				res = 5;
			} else if (pj5 <= random && random < pj4) {
				res = 4;
			} else if (pj4 <= random && random < pj3) {
				res = 3;
			} else if (pj3 <= random && random < pj2) {
				res = 2;
			} else if (pj2 <= random && random < pj1) {
				res = 1;
			} else if (pj1 <= random && random <= pj0) {
				res = 1;
			}
			return res;

		case 4:// 最大困難度レベルが4のとき

			pj1 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][1]))));
			pj2 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][2]))));
			pj3 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][3]))));
			pj4 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][4]))));

			if (random < pj4) {
				res = 4;
			} else if (pj4 <= random && random < pj3) {
				res = 3;
			} else if (pj3 <= random && random < pj2) {
				res = 2;
			} else if (pj2 <= random && random <= pj1) {
				res = 1;
			} else if (pj1 <= random && random <= pj0) {
				res = 0;
			}
			return res;

		case 3:// 最大困難度レベルが3のとき

			pj1 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][1]))));
			pj2 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][2]))));
			pj3 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][3]))));

			if (random < pj3) {
				res = 3;
			} else if (pj3 <= random && random < pj2) {
				res = 2;
			} else if (pj2 <= random && random < pj1) {
				res = 1;
			} else if (pj1 <= random && random <= pj0) {
				res = 0;
			}
			return res;

		case 2:// 最大困難度レベルが2のとき

			pj1 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][1]))));
			pj2 = 1 / (1 + (Math.exp(-1.7 * (trueTheta - difficulty[j][2]))));

			if (random < pj2) {
				res = 2;
			} else if (pj2 <= random && random < pj1) {
				res = 1;
			} else if (pj1 <= random) {
				res = 0;
			}
			return res;
		}
		return res;
	}

	/*
	 * 現在の能力値を推定(ベイズのEAP)
	 */
	public static double estimateTheta(int i, int n, int j, double difficulty[][], int maxDifficultyLevel[],
			int history[][][]) {

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

				case 5:// 最大困難度レベルが3のとき
					switch (history[n][i][m]) {
					case 0:
						ICC1 = 1.0;
						ICC2 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][1])));
						result *= ICC1 - ICC2;
						break;
					case 1:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][1])));
						ICC2 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][2])));
						result *= ICC1 - ICC2;
						break;
					case 2:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][2])));
						ICC2 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][3])));
						result *= ICC1 - ICC2;
						break;
					case 3:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][3])));
						ICC2 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][4])));
						result *= ICC1 - ICC2;
						break;
					case 4:
						ICC1 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][4])));
						ICC2 = 1.0 / (1.0 + Math.exp(-1.7 * (Xm[l] - difficulty[m][5])));
						result *= ICC1 - ICC2;
						break;
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