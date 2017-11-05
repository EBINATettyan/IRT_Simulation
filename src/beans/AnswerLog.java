package beans;

public class AnswerLog {
	private int id = 0;
	private int userId = 0; // ユーザID
	private int questionId = 0; // 問題番号
	private double discrimination = 0.0;
	private double difficulty = 0.0;
	private int section = 0; // 第○回
	private int trueOrFalse = 0; // 正誤
	private double ability = 0;
	private int answer1 = 0; // 解答1
	private int answer2 = 0; // 解答2
	private int answer3 = 0; // 解答3
	private int answer4 = 0; // 解答4

	/**
	 * @param id
	 * @param userId
	 * @param questionId
	 * @param discrimination
	 * @param difficulty
	 * @param section
	 * @param trueOrFalse
	 * @param ability
	 * @param answer1
	 * @param answer2
	 * @param answer3
	 * @param answer4
	 */
	public AnswerLog(int id, int userId, int questionId, double discrimination, double difficulty, int section,
			int trueOrFalse, double ability, int answer1, int answer2, int answer3, int answer4) {
		this.id = id;
		this.userId = userId;
		this.questionId = questionId;
		this.discrimination = discrimination;
		this.difficulty = difficulty;
		this.section = section;
		this.trueOrFalse = trueOrFalse;
		this.ability = ability;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
	}

	/**
	 * 
	 */
	public AnswerLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public double getDiscrimination() {
		return discrimination;
	}

	public void setDiscrimination(double discrimination) {
		this.discrimination = discrimination;
	}

	public double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public int getTrueOrFalse() {
		return trueOrFalse;
	}

	public void setTrueOrFalse(int trueOrFalse) {
		this.trueOrFalse = trueOrFalse;
	}

	public double getAbility() {
		return ability;
	}

	public void setAbility(double ability) {
		this.ability = ability;
	}

	public int getAnswer1() {
		return answer1;
	}

	public void setAnswer1(int answer1) {
		this.answer1 = answer1;
	}

	public int getAnswer2() {
		return answer2;
	}

	public void setAnswer2(int answer2) {
		this.answer2 = answer2;
	}

	public int getAnswer3() {
		return answer3;
	}

	public void setAnswer3(int answer3) {
		this.answer3 = answer3;
	}

	public int getAnswer4() {
		return answer4;
	}

	public void setAnswer4(int answer4) {
		this.answer4 = answer4;
	}

}
