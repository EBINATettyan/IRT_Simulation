package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.Question;
import utility.DriverAccessor;

public class QuestionDAO extends DriverAccessor {

	// 問題一覧を検索する
	public List<Question> selectQuestionAll() {
		Connection con = null;
		con = createConnection();

		try {
			String sql = "SELECT * FROM questions;";

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			List<Question> questionsList = new ArrayList<Question>();
			while (rs.next()) {
				Question question = new Question(rs.getInt("id"), rs.getInt("type_id"), rs.getString("content"),
						rs.getString("choice1"), rs.getString("choice2"), rs.getString("choice3"),
						rs.getString("choice4"), rs.getInt("correct_answer1"), rs.getInt("correct_answer2"),
						rs.getInt("correct_answer3"), rs.getInt("correct_answer4"), rs.getDouble("discrimination"),
						rs.getDouble("difficulty"));
				questionsList.add(question);
			}

			stmt.close();
			rs.close();
			closeConnection(con);

			return questionsList;
		} catch (

		SQLException e)

		{
			e.printStackTrace();
			return null;
		} finally

		{
		}
	}

}