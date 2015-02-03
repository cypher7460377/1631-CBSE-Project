import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Tally {
	private static ArrayList<String> voters;
	private HashMap<Integer, Integer> tallyTable;
	private static ArrayList<Integer> candidates;
	public Admin myAdmin;
	
	public Tally(){
		voters = new ArrayList<String>();
		tallyTable = new HashMap<Integer, Integer>();
		candidates = new ArrayList<Integer>();
		myAdmin = new Admin();
	}
	
	public Tally(int tally){
		voters = new ArrayList<String>();
		tallyTable = new HashMap<Integer, Integer>();
		candidates = new ArrayList<Integer>();
		myAdmin = new Admin();
		
		for(int i = 0; i < tally; i++){
			candidates.add(i+1);
		}
		
		for(Integer id: candidates)
			tallyTable.put( id, 0);
	}

	boolean initTally(String[] candidateList){
		boolean complete = false;
		if(tallyTable.isEmpty()){
			if(candidateList==null) return false;
			else
				complete = setCandidates(candidateList);
			for(Integer id: candidates)
				tallyTable.put( id, 0);
		} // end if
		System.out.println("Tally table initialized.");
		return complete;
	}

	private boolean setCandidates(String[] candidateList) {
		for(String str: candidateList){
			candidates.add(Integer.parseInt(str.trim()));
		}
		return true;
	}

	public boolean terminateTally() {
		tallyTable.clear();
		System.out.println("Table cleared.");
		return true;
	}

	void getReport(int n) {
		ArrayList<Entry<Integer, Integer>> mapSet = new ArrayList<Entry<Integer, Integer>>(tallyTable.entrySet());
		Collections.sort(mapSet, new MyComparator());
	
		Iterator<Entry<Integer, Integer>> itr= mapSet.iterator();
		Integer key;
		int value=0;
		while(itr.hasNext()){

			Map.Entry<Integer, Integer> e=(Map.Entry<Integer, Integer>)itr.next();

			key = (Integer)e.getKey();
			value = ((Integer)e.getValue()).intValue();

			System.out.println(key+", "+value);
		}
	}

	public static boolean hasCandidate(int cand) {
		return candidates.contains(cand);
	}


	boolean validateVote(String voter){
		return voters.contains(voter);
	}

	
	boolean castVote(Vote vote) {
		if(!validateVote(vote.getEmail())){
			tallyTable.put(vote.getCandidate(), tallyTable.get(vote.getCandidate())+1);
			System.out.println("Vote successfully cast!");
		} else{
			System.out.println("Duplicate vote attempted.");
			return false; //End if for validating vote
		}
		return voters.add(vote.getEmail());
	}
}