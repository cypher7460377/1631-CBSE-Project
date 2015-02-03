import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TrendEstimator {

	private HashMap<String, Integer> trendTable;
	private HashMap<Integer, String> posterTable;
	private ArrayList<Vote> votes;
	private int totalVotes;
	boolean authorized;
	

	public TrendEstimator() {
		trendTable = new HashMap<String, Integer>();
		posterTable = getPosterIDReference();
		initTrend();
		setVotes(new ArrayList<Vote>());
		totalVotes = 0;
		authorized = false;
	}
	
	private void initTrend() {
		// TODO Auto-generated method stub
		for(Map.Entry<Integer, String> entry : posterTable.entrySet()){
			if (!trendTable.containsKey(entry.getValue())) {
				trendTable.put(entry.getValue(), 0);
			}
		}
	}

	private HashMap<Integer, String> getPosterIDReference() {
		HashMap<Integer, String> temp = new HashMap<Integer, String>();
		File file = null;
		BufferedReader reader = null;
		try {
			file = new File("categories.txt");
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    String[] split;

		    while ((text = reader.readLine()) != null) {
		    	split = text.split(",");
		    	temp.put(Integer.valueOf(split[0]), split[1]);
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
		
		return temp;
	}

	/**
	 * @return the trendTable
	 */
	public HashMap<String, Integer> getTrendTable() {
		return trendTable;
	}

	/**
	 * @param trendTable the trendTable to set
	 */
	public void setTrendTable(HashMap<String, Integer> trendTable) {
		this.trendTable = trendTable;
	}

	/**
	 * @return the totalVotes
	 */
	public int getTotalVotes() {
		return totalVotes;
	}

	/**
	 * @param totalVotes the totalVotes to set
	 */
	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}
	
	public boolean startTrend(){
		if(trendTable==null){
			System.out.println("Trend table is null");
			return false;
		}
		if(!trendTable.isEmpty()){
			trendTable.clear();
//			trendTable = 
			initTrend();
		}
		return authorized = true;
		
	}

	public boolean stopTrend(){
		return authorized = false;
	}
	
	public boolean countVote(String email, int posterID, String date){
		if(posterTable==null){
			System.out.println("Postertable = null");
			posterTable = getPosterIDReference();
		}
		
		if(!posterTable.containsKey(posterID) || !authorized) {
			System.out.println("poster id not found");			
			return false;
		}
		else{
			votes.add(new Vote(email,posterID,date));
			String category = posterTable.get(posterID);
			int vote = trendTable.get(category) + 1; 
			trendTable.put(category, vote);
			totalVotes++;
		}
		return true;
		
	}

	public HashMap<String, Double> getReport(){
		ArrayList<YearRecord> years = new ArrayList<YearRecord>();
		int yearIndex = -1;
		HashMap<String, Double> report = new HashMap<String, Double>();
		File file = null;
		BufferedReader reader = null;
		ArrayList<String> category = getCategoryList();
		double[] catAvg = new double[10];
		
		/*
		 * Getting our data structure filled out.
		 */
		try {
			file = new File("history.txt");
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    String[] split;

		    while ((text = reader.readLine()) != null) {
		    	split = text.split(",");
		    	if(split.length==2){
		    		(years.get(yearIndex)).setRecord(split[0], Integer.valueOf(split[1]));
//		    		if (!category.contains(split[0])) {
//						category.add(split[0]);
//					}
		    	}else{
		    		yearIndex++;
		    		years.add(new YearRecord());
		    		(years.get(yearIndex)).setYear(Integer.parseInt(split[0]));
		    	}
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
		
		/*
		 * Loop to find the averages for years and vote percentages
		 */
		for (int i = 0; i < years.size(); i++) {
			for(int j = 0; j < category.size(); j++){
				catAvg[j] += years.get(i).getRecord(category.get(j));
			}
		}
		
		/*
		 * Now to add in our current averages
		 */
		for(int i = 0; i < category.size(); i++){
			catAvg[i] += getPercentage(category.get(i));
			catAvg[i] /= years.size() +1;
			report.put(category.get(i), catAvg[i]);
		}
		
//		for (Map.Entry<String, Integer> entry : trendTable.entrySet()){
//		    System.out.println(entry.getKey() + "/" + entry.getValue());
//		}

		return report;
		
	}

	private ArrayList<String> getCategoryList() {
		
		ArrayList<String> temp = new ArrayList<String>();
		for(Map.Entry<Integer, String> entry : posterTable.entrySet()){
			if (!temp.contains(entry.getValue())) {
				temp.add(entry.getValue());
			}
			//System.out.println(entry.getKey() + "/" + entry.getValue());
		}
		// TODO Auto-generated method stub
		return temp;
	}

	private int getPercentage(String string) {
		// TODO Auto-generated method stub
//		System.out.println("printing table:");
//		for(Map.Entry<String, Integer> entry : trendTable.entrySet()){
//			System.out.println(entry.getKey() + "/" + entry.getValue());
//		}
		return (trendTable.get(string)/totalVotes)*100;
	}

	/**
	 * @return the votes
	 */
	public ArrayList<Vote> getVotes() {
		return votes;
	}

	/**
	 * @param votes the votes to set
	 */
	public void setVotes(ArrayList<Vote> votes) {
		this.votes = votes;
	}
	
	private class YearRecord{
		private int year;
		private HashMap<String, Integer> history;
		
		YearRecord(){
			history = new HashMap<String, Integer>();
			year = 0;
		}
		
		public int getYear(){
			return year;
		}
		
		public boolean setYear(int year){
			this.year = year;
			return true;
		}
		
		public boolean setRecord(String cat, Integer val){
			history.put(cat, val);
			return true;
		}
		
		public Integer getRecord(String cat){
			return history.get(cat);
		}
	}
}
