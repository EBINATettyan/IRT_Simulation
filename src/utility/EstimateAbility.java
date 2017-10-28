package utility;

/*
 * 2値反応能力値推定プログラム
 * */
public class EstimateAbility {

	public Double estimateTheta(int[] u, double[] a, double[] b) {

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