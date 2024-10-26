/* TransactionRecipt contains an object from the transaction ticket class, a boolean to indicate success, 
the reason if the transaction fails, the type of account used, the balances before and after, as well as the post 
maturity date if applicable, the method also contains getters, and both no argument and parameterized constructors
*/
import java.util.Calendar;
public class TransactionRecipt {
	private TransactionTicket ticketInfo;
	private boolean successFlag;
	private String failureReason;
	private String accountType;
	private double preBalance;
	private double postBalance; 
	private Calendar postMaturityDate = Calendar.getInstance();

	//No-Arg Constructor
	public TransactionRecipt() {
		ticketInfo = new TransactionTicket();
		successFlag = true;
		failureReason = "";
		accountType = "";
		preBalance = 0.0;
		postBalance = 0.0;
		postMaturityDate = Calendar.getInstance();
		postMaturityDate.clear();
	}
	
	//Parameterized Constructor
	public TransactionRecipt (TransactionTicket info, boolean a, String b, String c, double d, double e, String postDate) {
		ticketInfo = info;
		successFlag = a;
		failureReason = b;
		accountType = c;
		preBalance = d;
		postBalance = e;
		if (postDate.equals("N/A") ) {
			postMaturityDate = Calendar.getInstance();
			postMaturityDate.clear();
		}else {
			postMaturityDate.clear();
			
			String[] mdArray = postDate.split("/");
			int[] b1 = new int[3];
			
			b1[0] = Integer.parseInt(mdArray[0]);
			b1[1] = Integer.parseInt(mdArray[1]);
			b1[2] = Integer.parseInt(mdArray[2]);

			postMaturityDate.set(Calendar.MONTH, b1[0]);
			postMaturityDate.set(Calendar.DAY_OF_MONTH, b1[1]);
			postMaturityDate.set(Calendar.YEAR, b1[2]);	
		}
	}
	
	//getters
	public TransactionTicket getTINfo () {
		return ticketInfo;
	}
	public boolean getFlag() {
		return successFlag;
	}
	public String getType () {
		return accountType;
	}
	public String getReason() {
		return failureReason;
	}
	public double getPreBalance() {
		return preBalance;
	}
	public double getPostBalance() {
		return postBalance;
	}
	public Calendar getPostMDate() {
		return postMaturityDate;
	}
}
