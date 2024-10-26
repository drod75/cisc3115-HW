import java.util.ArrayList;
import java.util.Calendar;

public class Bank {
	private ArrayList<Account> accounts;
	private int numAccts;
	private static double totalAmountInSavingsAccts;
	private static double totalAmountInCheckingAccts;
	private static double totalAmountInCDAccts;
	private static double totalAmountInALLAccts;
	
	//No-Arg Constructor
	public Bank () {
		numAccts = 0;
		accounts = new ArrayList<>();
		totalAmountInSavingsAccts = 0;
		totalAmountInCheckingAccts = 0;
		totalAmountInCDAccts = 0;
		totalAmountInALLAccts = 0;
	}
	
	//getter
	public Account getAccount (int index) {
		String type = accounts.get(index).getType();
		if (type.equals("CD")) {
			CDAccount filler = (CDAccount)accounts.get(index);
			return new CDAccount(filler);
		}else if (type.equals("Checking")) {
			CheckingAccount filler = (CheckingAccount)accounts.get(index);
			return new CheckingAccount (filler);
		}else{
			SavingsAccount filler = (SavingsAccount)accounts.get(index);
			return new SavingsAccount(filler);
		}
	}
	public int getNumberOfAccounts() {
		return numAccts;
	}
	public double getAllCh() {
		return totalAmountInCheckingAccts;
	}
	public double getAllSv() {
		return totalAmountInSavingsAccts;
	}
	public double getAllCD() {
		return totalAmountInCDAccts;
	}
	public double getAllAmount() {
		return totalAmountInALLAccts;
	}
		
	//Total in All Accounts Checker
	public void checkAllInS() {
		for (int x = 0; x < getNumberOfAccounts(); x++) {
			if (accounts.get(x).getType().equals("Savings") ) totalAmountInSavingsAccts += accounts.get(x).getBalance();
		}
	}
	public void checkAllInCh() {
		for (int x = 0; x < getNumberOfAccounts(); x++) {
			if (accounts.get(x).getType().equals("Checking") ) totalAmountInCheckingAccts += accounts.get(x).getBalance();
		}
	}
	public void checkAllInCD() {
		for (int x = 0; x < getNumberOfAccounts(); x++) {
			if (accounts.get(x).getType().equals("CD") ) totalAmountInCDAccts += accounts.get(x).getBalance();
		}
	}
	public void checkAllAccts() {
		checkAllInS();
		checkAllInCh();
		checkAllInCD();	
		totalAmountInALLAccts = totalAmountInSavingsAccts + totalAmountInCheckingAccts + totalAmountInCDAccts;
	}
		
	//new account
	public void addAccount(Account newAccount) {
		accounts.add(newAccount);
		numAccts = accounts.size();
		
		checkAllAccts();
	}
	
	//This method has an input of an account number and locates it the array of accounts, if found it outputs the account
 	private int findAcct(int requestedAccount) {
 		for (int x  = 0; x < numAccts; x++) {
 			Account filler = accounts.get(x);
 			int acctN = filler.getAcctNumber();
 			if (acctN == requestedAccount) return x;
 		}
		return -1;
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionReceipt getBalance(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}else {
			String type = accounts.get(index).getType();
			if (type.equals("CD")) {
				CDAccount filler = (CDAccount)accounts.get(index);
				TransactionReceipt receipt = filler.getCurrentBalance(ticket);
				return receipt;
			}else if (type.equals("Checking")) {
				CheckingAccount filler = (CheckingAccount)accounts.get(index);
				TransactionReceipt receipt = filler.getCurrentBalance(ticket);
				return receipt;
			}else{
				SavingsAccount filler = (SavingsAccount)accounts.get(index);
				TransactionReceipt receipt = filler.getCurrentBalance(ticket);
				return receipt;
			}
		}
		
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionReceipt makeWithdraw(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}else {
			String type = accounts.get(index).getType();
			if (type.equals("CD")) {
				CDAccount filler = (CDAccount)accounts.get(index);
				TransactionReceipt receipt = filler.makeWithdrawal(ticket);
				return receipt;
			}else if (type.equals("Checking")) {
				CheckingAccount filler = (CheckingAccount)accounts.get(index);
				TransactionReceipt receipt = filler.makeWithdrawal(ticket);
				return receipt;
			}else{
				SavingsAccount filler = (SavingsAccount)accounts.get(index);
				TransactionReceipt receipt = filler.makeWithdrawal(ticket);
				return receipt;
			}
		}
		
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */	
	public TransactionReceipt makeDeposit(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}else {
			String type = accounts.get(index).getType();
			if (type.equals("CD")) {
				CDAccount filler = (CDAccount)accounts.get(index);
				TransactionReceipt receipt = filler.makeDeposit(ticket);
				return receipt;
			}else if (type.equals("Checking")) {
				CheckingAccount filler = (CheckingAccount)accounts.get(index);
				TransactionReceipt receipt = filler.makeDeposit(ticket);
				return receipt;
			}else{
				SavingsAccount filler = (SavingsAccount)accounts.get(index);
				TransactionReceipt receipt = filler.makeDeposit(ticket);
				return receipt;
			}
		}
	}

	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionReceipt clearCheck(Check check, TransactionTicket ticket) {
		int account = check.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}else {
			CheckingAccount filler = (CheckingAccount)accounts.get(index);
			TransactionReceipt receipt = filler.clearCheck(check, ticket);
			checkAllAccts();
			return receipt;
		}
	}
		
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does deletes the account if it has a balance of zero,
		if not the receipt is put false.
	The output is a receipt which is made depending on if the transaction went through.
	 */
	public TransactionReceipt deleteAccount(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}else {
			Account filler = accounts.get(index);
			String type = filler.getType();
			double balance = filler.getBalance();
			if (balance > 0) {
				String reason = "Balance does not equal zero";
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, type, balance, balance, "N/A");
				return receipt;
			}else {
				accounts.remove(index);
				numAccts = numAccts - 1;
				TransactionReceipt receipt = new TransactionReceipt(ticket, true, "", type, balance, balance, "N/A");
				checkAllAccts();
				return receipt;
			}
		
		}
	
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if does not, the information is sent through and all of it is checked.
		If anything is wrong then the receipt if put false and the reason why.
	The output is a receipt which is made depending on if the transaction went through.
	 */
	public TransactionReceipt newAccount(TransactionTicket ticket, Depositor info, String type) {
		int account = ticket.getAcctNum();
		String ssn = info.getSSN();
		int termNext = ticket.getTermOfCD();
		double cdBalance = ticket.getAmountOfTransaction();
		int index = findAcct(account);
		
		if (ssn.length() == 9) {
			if (index != -1) {
				String reason = "Account number " + account + " already exists";
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
				return receipt;
			}else {
				if (type.equals("Savings")) {
					TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", type, 0.00, 0.00, "N/A");
					Account newAccount = new SavingsAccount(info, account, type, 0, true);
					addAccount(newAccount);
					checkAllAccts();
					newAccount.addTransaction(receipt);
					return receipt;
				}else if(type.equals("Checking")){
					TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", type, 0.00, 0.00, "N/A");
					Account newAccount = new CheckingAccount(info, account, type, 0, true);
					addAccount(newAccount);
					checkAllAccts();
					newAccount.addTransaction(receipt);
					return receipt;
				}else if (type.equals("CD")) {
					if ( !(cdBalance >= 0) ) {
						String reason = "CD accounts must have an opening balance of at least 0";
						TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, type, 0.00, 0.00, "N/A");
						return receipt;
					}else {
						if ( termNext == 6 || termNext == 12 || termNext == 18 || termNext == 24 ) {
							Calendar date = Calendar.getInstance();
							Calendar present = Calendar.getInstance();
							date.clear();
							date.set(Calendar.MONTH, present.MONTH);
							date.set(Calendar.DAY_OF_MONTH, present.DAY_OF_MONTH);
							date.set(Calendar.YEAR, 2023);
							date.add(Calendar.MONTH, termNext);
							
							int month = date.get(Calendar.MONTH) + 1;
							int day = date.get(Calendar.DAY_OF_MONTH);
							int year = date.get(Calendar.YEAR);
							String writtenDate = month + "/" + day + "/" + year;
						
							TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", type, cdBalance, cdBalance, writtenDate);
							Account newAccount = new CDAccount(info, account, type, cdBalance, true, date);
							addAccount(newAccount);
							checkAllAccts();
							newAccount.addTransaction(receipt);
							
							return receipt;
							
							
						}else {
							String reason = "You chose a term of " + termNext + ", CD accounts must a maturity date that is either,"
									+ " 6, 12, 18, or 24 months away";
							TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, type, cdBalance, cdBalance, "N/A");
							return receipt;
						}
					}
				}else {
					String reason = "Account type " + type + " cannot be accepted, choose from the choices available "
							+ "(Checking, Savings, CD)";
					TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
					return receipt;
				}
			}
		}else {
			String reason = "SSN " + ssn + " cannot be accpeted as it is not 9 digits long";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
			return receipt;
		}
		
	}
	
	/*
	The input is an object from the transaction ticket class.
	The process is that if locates the account and if does not, the information is sent through and all of it is checked.
		If anything is wrong then the receipt if put false and the reason why.
	The output is a receipt which is made depending on if the transaction went through.
	 */
	public TransactionReceipt closeAccount(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}else {
			Account filler = accounts.get(index);
			TransactionReceipt receipt = filler.closeAcct(ticket);
			return receipt;
		}
	}
	
	/* 
	The input is an object from the transaction ticket class.
	The process is that if locates the account and if does not, the information is sent through and all of it is checked.
		If anything is wrong then the receipt if put false and the reason why.
	The output is a receipt which is made depending on if the transaction went through.
	  */
	public TransactionReceipt reOpenAccount(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}else {
			Account filler = accounts.get(index);
			TransactionReceipt receipt = filler.reOpenAcct(ticket);
			return receipt;
		}
	}

}
