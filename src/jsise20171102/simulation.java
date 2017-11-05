package jsise20171102;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import beans.AnswerLog;
import beans.Question;
import control.EstimateAbilityManager;
import control.SelectQestionManager;
import dao.AnswerLogDAO;

/*
 * JSiSEに向けてのシミュレーションプログラム(20171102 適応型テスト)
 */

public class simulation {

	public static void main(String args[]) {

		long start = System.currentTimeMillis();

		int trueThetaListUser; // 真の能力値を保持する変数
		// int item; // 課題番号を保持する変数
		// int count; // 課題番号を保持する変数
		double estimateTheta; // 能力の推定値を保持する変数
		int user; // ユーザ変数
		boolean tf; // True or Falseで正誤を保持する

		/*
		 * 真の能力値を仮定
		 */
		ArrayList<Double> trueThetaList = new ArrayList<Double>();
		trueThetaList.add(-2.0);
		trueThetaList.add(-1.0);
		trueThetaList.add(0.0);
		trueThetaList.add(1.0);
		trueThetaList.add(2.0);

		int section = 0;

		try {

			// ファイル書き出し用
			File csv = new File("C:/Users/J14-8002/pleiades/workspace/IRT_Simulation/result/simulation_1102.csv");
			// 追記モード
			// 文字コードの指定
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csv), "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write("estimateTheta");
			bw.newLine();

			/*
			 * for分で真の能力値ごとに100人分繰り返す
			 */
			for (trueThetaListUser = 0; trueThetaListUser < trueThetaList.size(); trueThetaListUser++) {
				bw.newLine();
				bw.newLine();
				for (user = 0; user < 100; user++) {
					estimateTheta = 0.0;
					for (int count = 1; count <= 30; count++) {

						/*
						 * 項目をDBから引っ張り、discriminationとdifficultyを求める
						 * 現在の推定能力値を用いて情報量最大の問題を選択
						 */
						SelectQestionManager selectQestionManager = new SelectQestionManager();
						Question question = selectQestionManager.selectQuestion(estimateTheta, user, section);

						// 反応データを乱数により求め、正誤判定をする
						tf = tf(trueThetaList.get(trueThetaListUser), question.getDiscrimination(),
								question.getDifficulty());
						int TF;
						if (tf == true) {
							TF = 1;
						} else {
							TF = 0;
						}

						// AnswerLogを生成
						AnswerLog answerLog = new AnswerLog(0, user, question.getId(), question.getDiscrimination(),
								question.getDifficulty(), section, TF, estimateTheta, 0, 0, 0, 0);

						// AnswerLogを蓄積
						EstimateAbilityManager estimateAbilityManager = new EstimateAbilityManager();
						estimateAbilityManager.insertAnswerLog(answerLog);

						// 解答を取り出し
						List<AnswerLog> answerLogs = new ArrayList<AnswerLog>();
						AnswerLogDAO answerLogDAO = new AnswerLogDAO();
						answerLogs = answerLogDAO.selectAnswerLogByUser(user, section);

						// 解答データ、そのときのaとbの配列を作成する
						int u[] = new int[answerLogs.size() + 1];
						double a[] = new double[answerLogs.size() + 1];
						double b[] = new double[answerLogs.size() + 1];

						// 能力値を再推定する
						for (int j = 0; j < answerLogs.size(); j++) {
							u[j] = answerLogs.get(j).getTrueOrFalse();
							a[j] = answerLogs.get(j).getDiscrimination();
							b[j] = answerLogs.get(j).getDifficulty();
						}
						estimateTheta = estimateTheta(u, a, b);
						bw.write(estimateTheta + ",");
					}
					bw.newLine();
					section++;
				}
			}
			bw.close();
		} catch (IOException ex) {
			// 例外時処理
			ex.printStackTrace();
		}
		System.out.println("END");
		long end = System.currentTimeMillis();
		System.out.println("ProcessTime：" + (end - start) + "ms");

	}

	// 真の能力と今から解く穴あきワークシートの困難度と識別力から正誤を判定
	public static boolean tf(double trueTheta, double discrimination, double difficulty) {
		boolean tf = true;

		// 真の能力値での，その課題に対する正答確率を推定する
		double e = Math.exp(-1.7 * discrimination * (trueTheta - difficulty));
		double probability = 1 / (1 + e);

		// 0〜100未満のランダム値を生成
		double random = (double) (Math.random());

		// 正答か誤答か判断
		if (probability > random) {
			tf = true;
		} else {
			tf = false;
		}

		return tf;
	}

	/*
	 * 2値反応現在の能力値を推定
	 */
	public static double estimateTheta(int[] u, double[] a, double[] b) {
		// ループカウンター
		int i, j;

		// 求積点
		double[] Xm = { -4.15989, -3.92869, -3.69862, -3.46959, -3.24151, -3.01432, -2.78794, -2.5623, -2.33732,
				-2.11295, -1.88912, -1.66576, -1.44283, -1.22025, -0.997977, -0.775951, -0.554115, -0.332415, -0.110796,
				0.110796, 0.332415, 0.554115, 0.775951, 0.997977, 1.22025, 1.44283, 1.66576, 1.88912, 2.11295, 2.33732,
				2.5623, 2.78794, 3.01432, 3.24151, 3.46959, 3.69862, 3.92869, 4.15989 };

		// 重み（基準化した）
		double[] Wm = { 7.08E-9, 4.57E-8, 2.63E-7, 1.35E-6, 6.22E-6, 2.57E-5, 9.52E-5, 3.17E-4, 9.53E-4, 0.00257927,
				0.006303, 0.0139157, 0.027779, 0.0501758, 0.082052, 0.121538, 0.16313, 0.19846, 0.21889, 0.21889,
				0.19846, 0.16313, 0.121538, 0.082052, 0.0501758, 0.027779, 0.0139157, 0.006303, 0.00257927, 9.53E-4,
				3.17E-4, 9.52E-5, 2.57E-5, 6.22E-6, 1.35E-6, 2.63E-7, 4.57E-8, 7.08E-9 };

		// 尤度関数を求めるために用いる変数
		double result; // 結果を求める用
		double logit1; // ロジスティックモデル
		double ICC1; // 段階反応のひとつ目の式
		@SuppressWarnings("unused")
		double ICC2; // 段階反応のふたつ目の式

		double Numerator[] = new double[Xm.length];// ベイズの分子
		double Denominator[] = new double[Xm.length]; // ベイズの分母

		double Nsum = 0.0;// 分子の和
		double Dsum = 0.0;// 分母の和

		// 尤度関数を求める
		for (i = 0; i < Xm.length; i++) {
			result = 1;
			for (j = 0; j < u.length; j++) {
				switch (u[j]) {
				case 1:
					logit1 = -1.7 * a[j] * (Xm[i] - b[j]);
					ICC1 = 1.0 / (1.0 + Math.exp(logit1));
					result *= ICC1;
					ICC2 = 0.0;
					break;
				case 0:
					logit1 = -1.7 * a[j] * (Xm[i] - b[j]);
					ICC1 = 1.0 / (1.0 + Math.exp(logit1));
					result *= (1 - ICC1);
					ICC2 = 0.0;
					break;
				}

			}
			Numerator[i] = result * Xm[i] * Wm[i];
			Denominator[i] = result * Wm[i];
		}
		for (int h = 0; h < Xm.length; h++) {
			Nsum += Numerator[h];
			Dsum += Denominator[h];
		}

		double theta = Nsum / Dsum;
		return theta;
	}
}