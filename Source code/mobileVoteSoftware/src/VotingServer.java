import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import javax.mail.*;


public class VotingServer {

	private static Tally tally = new Tally();
	private static ArrayList<String> senders = new ArrayList<String>();
	private static String subject = "";
	private static String message;
	private static Session session;
	private static Store store;
	private static Properties props;
	private static String timestamp = "";

	public static void main(String[] args) {
		//Scanner in = new Scanner(System.in);
		boolean initiated = false, terminated = false;
		//String stop;
		props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		session = Session.getInstance(props, null);
		
		try {
			store = session.getStore();
			store.connect("imap.gmail.com", "dummy5225@gmail.com", "dummy12345");
		} catch (NoSuchProviderException e1) {
			e1.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		
		while(!initiated){
			initiated = getMail(0);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		while(!terminated){
			terminated = getMail(1);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*stop = in.nextLine();
			if(stop.equals("stop"))
				terminated = true;*/
		}
	}

	public static boolean getMail(int option){
		boolean success = false, valid = false;

		try {

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			Message msg = inbox.getMessage(inbox.getMessageCount());
			if (!timestamp.equals(msg.getSentDate().toString())) {
				timestamp = msg.getSentDate().toString();
				subject = msg.getSubject();
				Address[] in = msg.getFrom();
				
				for (Address address : in) {
					senders.add(address.toString());
					//System.out.println("FROM:" + address.toString());
				}
				
				if(option == 0){
					valid = senders.get(0).contains(tally.myAdmin.getEmailAddress());
					if(valid && subject.equals("Initiate tally")){
						message = getMessage(msg);
						tally.initTally(message.split(";"));
						success = true;
					}
				}else if(option ==1){
					valid = senders.get(0).contains(tally.myAdmin.getEmailAddress());
					if(valid){
						if(subject.equals("Get report")){
							tally.getReport(3);
						}else if(subject.equals("Terminate tally")){
							success = tally.terminateTally();
						}
					}else if(subject.equals("Cast vote")){
						message = getMessage(msg);
						tally.castVote(new Vote(senders.get(0), Integer.parseInt(message.trim())));
					}else{
						System.out.println("Not a valid email. Returning.");
					}
				}
			}
			inbox.close(true);
			//store.close();

		} catch (Exception mex) {
			mex.printStackTrace();
		}
		

		senders.clear();
		return success;

	}

	private static String getMessage(Message msg) throws MessagingException, IOException {
		String content = "";
		if (msg.isMimeType("multipart/*")){
			Multipart mp = (Multipart) msg.getContent();
			BodyPart bp = mp.getBodyPart(0);

			BufferedReader reader=new BufferedReader(new     InputStreamReader(bp.getInputStream()));

			String line;
			while((line=reader.readLine())!= null){

				content += line;
				System.out.flush();
			}
		}
		else {
			content = msg.getContent().toString();
		}

		return content;
	}

	static String convertStreamToString(InputStreamReader is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}

