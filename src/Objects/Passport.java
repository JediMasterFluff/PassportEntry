package Objects;

public class Passport {

	/*
	 * Required
	 */
	private String foodie;

	/*
	 * Optional
	 */
	private String age;
	private String postal;
	private String gender;
	private Comment<String, String> comments;

	public Passport() {
	}

	public String getFoodie() {
		return foodie;
	}

	public String getAge() {
		return age;
	}

	public String getPostal() {
		return postal;
	}

	public String getGender() {
		return gender;
	}

	public Comment<String, String> getComments() {
		return comments;
	}

	public void setFoodie(String foodie) {
		this.foodie = foodie;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setComments(Comment<String, String> comments) {
		this.comments = comments;
	}

	public static class Comment<A, B> {
		private A text;
		private B recipent;

		public Comment(A text, B recipent) {
			super();
			this.setText(text);
			this.setRecipent(recipent);
		}

		public B getRecipent() {
			return recipent;
		}

		public void setRecipent(B recipent) {
			this.recipent = recipent;
		}

		public A getText() {
			return text;
		}

		public void setText(A text) {
			this.text = text;
		}
	}

	@Override
	public String toString() {
		return "Passport [foodie=" + foodie + ", age=" + age + ", postal=" + postal + ", gender=" + gender
				+ ", comments=" + comments.getText() + "]";
	}

}
