package control;

import java.util.ArrayList;
import java.util.List;

import beans.AnswerLog;
import dao.AnswerLogDAO;
import dao.ResultDAO;

public class insertResultManager {

	public insertResultManager() {
	}

	// 情報量最大の問題を選択する
	public List<AnswerLog> insertFinishingTesting(double ability, int userId, int section) {
		ResultDAO resultDAO = new ResultDAO();
		resultDAO.insertFinishingTesting(ability, userId, section);

		AnswerLogDAO answerLogDAO = new AnswerLogDAO();

		List<AnswerLog> answerLogs = new ArrayList<AnswerLog>();
		answerLogs = answerLogDAO.selectAnswerLogByUser(userId, section);

		return answerLogs;
	}
}