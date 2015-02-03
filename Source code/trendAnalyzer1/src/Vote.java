

public class Vote{
	private String sender;
	private int candidateID;
	private String date;

	public Vote(){
		sender = "";
		candidateID = 0;
	}
	
	public Vote(String email, int id){
		sender = email;
		candidateID = id;
	}
	
	public Vote(String email, int id, String date){
		this.sender = email;
		this.candidateID = id;
		this.date = date;
	}

	String getEmail(){
		return sender;
	}

	int getCandidate(){
		return candidateID;
	}

	boolean setEmail(String email){
		sender = email;
		return true;
	}

	boolean setCandidate(int cand){
		boolean found = false;

/*		if(Tally.hasCandidate(cand))
			candidateID = cand;*/
		return found;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	

}