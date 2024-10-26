import java.util.ArrayList;

public class Account {
	private Depositor info;
	private int acctNumber;
	protected double balance;
	private String acctType;
	private boolean openStatus;
	private ArrayList<TransactionReceipt> transactionHistory;
	
	//No Argument Constructor
	public Account () {
		info = new Depositor();
		acctNumber = 0;
		balance = 0.0;
		acctType = "";
		transactionHistory = new ArrayList<>();
		openStatus = false;
	}
	//Parameterized Constructor
	public Account (Depositor d, int acctN, String acctT, double b, boolean status) {
		info = d;
		acctNumber = acctN;
		balance = b;
		acctType = acctT;
		openStatus = status;
		transactionHistory = new ArrayList<>();
	}
	//copy Constructor
	public Account (Account account) {
		info = new Depositor(account.info);
		acctNumber = account.acctNumber;
		balance = account.balance;
		acctType = account.acctType;
		openStatus = account.openStatus;
		transactionHistory = new ArrayList<>(account.transactionHistory);
	}

	//Transaction Methods
	public void addTransaction(TransactionReceipt receipt) {
		transactionHistory.add(receipt);
	}
	public ArrayList<TransactionReceipt> getTransactionHistory(TransactionTicket ticket){
		return transactionHistory;
	}
	
	//getters
	public Depositor getDepositer() {
			return new Depositor(info);		
	}
	public int getAcctNumber() {
			return acctNumber;
	}
	public double getBalance() {
		return balance;
	}
	public String getType() {
			return acctType;
	}
	public boolean getStatus() {
			return openStatus;
	}
	public int getAmountOfTransactions() {
			return transactionHistory.size();
	}

	/* The input is a Transaction Ticket object
	 The process is that the method makes the status boolean false and makes a transaction recsipt which is added
	 	to the Array List of receipts.
	 The output is the TransactionReceipt;
	*/
	public TransactionReceipt closeAcct(TransactionTicket ticket) {
		if (openStatus == false) {
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, "Account is Closed", acctType, balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}else {
			openStatus = false;
			TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", acctType, balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}
	}
		
	/* The input is a Transaction Ticket object
	 The process is that the method makes the status boolean true and makes a transaction recsipt which is added
	 	to the Array List of receipts.
	 The output is the TransactionReceipt;
	 */
	public TransactionReceipt reOpenAcct(TransactionTicket ticket) {
		openStatus = true;
		TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", acctType, balance, balance, "N/A");
		addTransaction(receipt);
		return new TransactionReceipt(receipt);
	}
	
}