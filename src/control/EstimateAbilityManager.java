package control;

import java.util.ArrayList;
import java.util.List;

import beans.AnswerLog;
import dao.AnswerLogDAO;
import utility.EstimateAbility;

public class EstimateAbilityManager {

	public EstimateAbilityManager() {
	}

	// AnswerLogを蓄積する
	public void insertAnswerLog(AnswerLog answerLog) {

		// 解答を蓄積
		AnswerLogDAO answerLogDAO = new AnswerLogDAO();
		answerLogDAO.insertAnswerLog(answerLog);

	}

	// 能力値を再推定する
	public Double estimateAbility(int userId, int section) {
		double ability;

		// 解答を取り出し
		List<AnswerLog> answerLogs = new ArrayList<AnswerLog>();
		AnswerLogDAO answerLogDAO = new AnswerLogDAO();
		answerLogs = answerLogDAO.selectAnswerLogByUser(userId, section);

		// 解答データ、そのときのaとbの配列を作成する
		int u[] = new int[answerLogs.size() + 1];
		double a[] = new double[answerLogs.size() + 1];
		double b[] = new double[answerLogs.size() + 1];

		// 能力値を再推定する
		EstimateAbility estimateAbility = new EstimateAbility();
		for (int i = 0; i < answerLogs.size(); i++) {

			u[i] = answerLogs.get(i).getTrueOrFalse();
			a[i] = answerLogs.get(i).getDiscrimination();
			b[i] = answerLogs.get(i).getDifficulty();

		}

		ability = estimateAbility.estimateTheta(u, a, b);

		return ability;
	}

}