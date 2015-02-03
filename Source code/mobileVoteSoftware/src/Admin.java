public class Admin {

	private String emailAddress;
	private String password;

	public Admin(){
		emailAddress = "cypher7460377@gmail.com";
		password = "1234";
	}
	
	public Admin(String email, String phrase){
		emailAddress = email;
		password = phrase;
	}
	
	public boolean isAdmin(String email) {
		return emailAddress.toString().equals(email);
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public boolean checkPassword(String phrase){
		return password.equals(phrase);
	}

}