package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import utility.DriverAccessor;

public class ResultDAO extends DriverAccessor {

	// 今まで何回テストにとりくんだかを検索する
	public int selectSection(int userId) {
		Connection con = null;
		con = createConnection();

		try {
			String sql = "SELECT * FROM results WHERE user_id = '" + userId
					+ "' AND section=(SELECT MAX(section) FROM results WHERE user_id=" + userId + ");";

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			int section = 0;
			if (rs.first()) {
				section = rs.getInt("section");
			}

			stmt.close();
			rs.close();
			closeConnection(con);

			return section;
		} catch (

		SQLException e)

		{
			e.printStackTrace();
			return 0;
		} finally

		{
		}
	}

	// 解答し始めたことを蓄積
	public void insertStartTesting(int userId, int section) {

		Connection con = null;
		con = createConnection();

		try {

			String sql = "INSERT INTO results VALUES(?,?,?,?,?)";

			PreparedStatement stmt = con.prepareStatement(sql);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			stmt.setInt(1, 0);
			stmt.setInt(2, userId);
			stmt.setInt(3, section);
			stmt.setDouble(4, -100); // 開始時は，能力値をぶっとんだ値にしとく
			stmt.setTimestamp(5, timestamp);

			stmt.executeUpdate();
			stmt.close();
			closeConnection(con);

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {

		}
	}

	// 解答が終了したことを蓄積
	public void insertFinishingTesting(double ability, int userId, int section) {

		Connection con = null;
		con = createConnection();

		try {

			String sql = "UPDATE results SET ability=" + ability + ", datetime=cast(now() as datetime) WHERE user_id="
					+ userId + " AND section=" + section + ";";

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			stmt.close();
			closeConnection(con);

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {

		}
	}

}