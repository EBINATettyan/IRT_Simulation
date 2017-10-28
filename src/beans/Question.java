package beans;

public class Question {
	private int id = 0; // 問題の番号
	private int typeId = 0; // 問題形式
	private String content = null; // 問題文
	private String choice1 = null; // 選択肢1
	private String choice2 = null; // 選択肢2
	private String choice3 = null; // 選択肢3
	private String choice4 = null; // 選択肢4
	private int correct_answer1 = 0; // 正答1
	private int correct_answer2 = 0; // 正答2
	private int correct_answer3 = 0; // 正答3
	private int correct_answer4 = 0; // 正答4
	private double discrimination = 0; // 識別力
	private double difficulty = 0; // 困難度

	/**
	 * @param id
	 * @param typeId
	 * @param content
	 * @param choice1
	 * @param choice2
	 * @param choice3
	 * @param choice4
	 * @param correct_answer1
	 * @param correct_answer2
	 * @param correct_answer3
	 * @param correct_answer4
	 * @param discrimination
	 * @param difficulty
	 */
	public Question(int id, int typeId, String content, String choice1, String choice2, String choice3, String choice4,
			int correct_answer1, int correct_answer2, int correct_answer3, int correct_answer4, double discrimination,
			double difficulty) {
		this.id = id;
		this.typeId = typeId;
		this.content = content;
		this.choice1 = choice1;
		this.choice2 = choice2;
		this.choice3 = choice3;
		this.choice4 = choice4;
		this.correct_answer1 = correct_answer1;
		this.correct_answer2 = correct_answer2;
		this.correct_answer3 = correct_answer3;
		this.correct_answer4 = correct_answer4;
		this.discrimination = discrimination;
		this.difficulty = difficulty;
	}

	/**
	 * 
	 */
	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getChoice1() {
		return choice1;
	}

	public void setChoice1(String choice1) {
		this.choice1 = choice1;
	}

	public String getChoice2() {
		return choice2;
	}

	public void setChoice2(String choice2) {
		this.choice2 = choice2;
	}

	public String getChoice3() {
		return choice3;
	}

	public void setChoice3(String choice3) {
		this.choice3 = choice3;
	}

	public String getChoice4() {
		return choice4;
	}

	public void setChoice4(String choice4) {
		this.choice4 = choice4;
	}

	public int getCorrect_answer1() {
		return correct_answer1;
	}

	public void setCorrect_answer1(int correct_answer1) {
		this.correct_answer1 = correct_answer1;
	}

	public int getCorrect_answer2() {
		return correct_answer2;
	}

	public void setCorrect_answer2(int correct_answer2) {
		this.correct_answer2 = correct_answer2;
	}

	public int getCorrect_answer3() {
		return correct_answer3;
	}

	public void setCorrect_answer3(int correct_answer3) {
		this.correct_answer3 = correct_answer3;
	}

	public int getCorrect_answer4() {
		return correct_answer4;
	}

	public void setCorrect_answer4(int correct_answer4) {
		this.correct_answer4 = correct_answer4;
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

}
