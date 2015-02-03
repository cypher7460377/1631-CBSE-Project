

public class Vote{
	private String sender;
	private int candidateID;

	public Vote(){
		sender = "";
		candidateID = 0;
	}
	
	public Vote(String email, int id){
		sender = email;
		candidateID = id;
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

		if(Tally.hasCandidate(cand))
			candidateID = cand;
		return found;
	}
	

}