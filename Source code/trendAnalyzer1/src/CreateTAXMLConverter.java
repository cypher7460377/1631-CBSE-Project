import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map.Entry;

public class CreateTAXMLConverter
{
	static OutputStream outstream;
	static MsgEncoder mEncoder;
	static Socket universal;

	public static void main(String[] args) throws Exception
	{
		TrendEstimator ta = new TrendEstimator();
		universal = new Socket("127.0.0.1", 7999);

		mEncoder = new MsgEncoder();
		final MsgDecoder mDecoder = new MsgDecoder(universal.getInputStream());
		
		KeyValueList msg23 = new KeyValueList();
		msg23.addPair("MsgID","23");
		msg23.addPair("Description", "Connect to SISServer");
		msg23.addPair("Name","TAXMLConverter");
		mEncoder.sendMsg(msg23, universal.getOutputStream());
		
		KeyValueList kvList;
		outstream = universal.getOutputStream();
		
		while(true)
		{	
			kvList = mDecoder.getMsg();
			ProcessMsg(kvList, ta);
		}	
	}
	
	static void ProcessMsg(KeyValueList kvList, TrendEstimator trendAnalyzer) throws Exception
	{
		int MsgID = Integer.parseInt(kvList.getValue("MsgID"));
		
		switch(MsgID)
		{
		/****************************************************
		Below are the main part of the component program. All received msgs are encoded as a KeyValueList kvList.
		kvList is a vector of <String key, String value> pairs. The 5 main methods of KeyValueList are 
			int size()                                    to get the size of KeyValueList
			String getValue(String key)                   to get value given key
			void addPair(String key, String value)        to add <Key, Value> pair to KeyValueList
			void setValue(String key, String value)       to set value to specific key
			String toString()                             System.out.print(KeyValueList) could work
		The following code can be used to new and send a KeyValueList msg to SISServer
			KeyValueList msg = new KeyValueList();
			msg.addPair("MsgID","23");
			msg.addPair("Description","Connect to SISServer");
			msg.addPair("Attribute","Value");
			... ...
			mEncoder.sendMsg(msg, universal.getOutputStream()); //This line sends the msg
		NOTE: Always check whether all the attributes of a msg are in the KVList before sending it.
		Don't forget to send a msg after processing an incoming msg if necessary.
		All msgs must have the following 2 attributes: MsgID and Description.
		Below are the sending messages' attributes list:
			MsgName: trendReport	MsgID: 803	Attrs:
		For more information about KeyValueList, read comments in Util.java.
		****************************************************/
        
		case 800:
			System.out.println("Message MsgName:startTrend MsgID:800 received, authorizing....");
            String password;
            password = kvList.getValue("admin");
			if (password.equals("1234")){
				System.out.println("Authorized!");	
				System.out.println();
            	trendAnalyzer.startTrend();
			}
			else {
				System.out.println("Authorization failed");
				System.out.println();
			}            


			break;
		case 801:
			System.out.println("Message MsgName:stopTrend MsgID:801 received, terminating Trend Analyzer.");
			trendAnalyzer.stopTrend();
			System.out.println("Succeed!");
			System.out.println();


			break;
		case 802:
			System.out.println("Message MsgName:getTrendReport MsgID:802 received, retrieving Trend Report.");
			KeyValueList msg = new KeyValueList();
			msg.addPair("MsgID","803");
			msg.addPair("Description","Trend Prediction");
			msg.addPair("Year","2015");
			HashMap<String, Double> report = new HashMap<String, Double>();
			report = trendAnalyzer.getReport();
			for (Map.Entry<String, Double> entry : report.entrySet())
			{
			    msg.addPair(entry.getKey(), Double.toString(entry.getValue()));
			}
                
            mEncoder.sendMsg(msg, universal.getOutputStream());
            System.out.println("Trend Report Generated!");
            System.out.println();



			break;
		case 901:
			System.out.println("Message MsgID:901 received, casting vote.");
			/*************************************************
			Add code below to process Message MsgName:countVote1 MsgID:901
			This message has following attributes: , use KeyValueList.getValue(String key) to get the values.
			If needed, don't forget to send a msg after processing. See previous comments on how to send a message.
			*************************************************/
                String VoterEmail, PosterID, Date;
                VoterEmail = kvList.getValue("VoterEmail");
      //          System.out.println("VoterEmail: " + VoterEmail);
                PosterID = kvList.getValue("PosterID");
       //         System.out.println("PosterID: " + PosterID);
                Date = kvList.getValue("Date");
        //        System.out.println("Date: " + Date);
                
                trendAnalyzer.countVote(VoterEmail, Integer.parseInt(PosterID), Date);
                System.out.println("Succeed");
                System.out.println();



			break;
				/*************************************************
		Below are system messages. No modification required.
		*************************************************/
		case 26:
			System.out.println("Connect to SISServer successful.");
			break;
		case 22:
			System.exit(0);
			break;
		case 24:
			System.out.println("Algorithm Activated");
			break;
		case 25:
			System.out.println("Algorithm Deactivated");
			break;
		default:
			break;
		}
	}
}
