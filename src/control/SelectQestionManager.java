package control;

import beans.Question;
import dao.ResultDAO;
import utility.SelectMaxInformationQuestion;

public class SelectQestionManager {

	public SelectQestionManager() {
	}

	// 情報量最大の問題を選択する
	public Question selectQuestion(double ability, int userId, int section) {
		Question question = new Question();

		// 情報量最大の問題を選択する
		SelectMaxInformationQuestion selectMaxInformationQuestion = new SelectMaxInformationQuestion();
		question = selectMaxInformationQuestion.selectMaxInformationQuestion(ability, userId, section);

		return question;
	}

	// 今まで何回試験を行ったかチェックし，解答を開始したことをDBに挿入
	public int selectSectionByUser(int userId) {

		ResultDAO resultDAO = new ResultDAO();

		// 何回目の試験かをチェック
		int section = resultDAO.selectSection(userId) + 1;

		// 解答を開始したことをDBに挿入する
		resultDAO.insertStartTesting(userId, section);

		return section;
	}
}