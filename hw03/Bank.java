/* The bank class contains the array of accounts, the number of accounts,constructors, as well as getters.
*/
import java.util.Calendar;
public class Bank {
	final int MAX_ACCTS = 100;
	private Account[] accounts;
	private int numAccts = 0;
	
	//No-Arg Constructor
	public Bank () {
		numAccts = 0;
		accounts = new Account[50];
	}
	
	//getter
	public int getNumberOfAccounts() {
		return numAccts;
	}
	
	//new account
	public void addAccount(Account newAccount) {
		accounts[numAccts] = newAccount;
		numAccts++;
	}
	
	//This method has an input of an account number and locates it the array of accounts, if found it outputs the account
 	private int findAcct(int requestedAccount) {
 		for (int x  = 0; x < numAccts; x++) {
 			Account filler = getAccount(x);
 			int acctN = filler.getAcctNumber();
 			if (acctN == requestedAccount) return x;
 		}
		return -1;
	}
	public Account getAccount (int index) {
		return accounts[index];
	}

	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionRecipt getBalance(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return recipt;
		}else {
			Account filler = getAccount(index);
			TransactionRecipt recipt = filler.getBalance(ticket);
			return recipt;
		}
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionRecipt makeWithdraw(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return recipt;
		}else {
			Account filler = getAccount(index);
			TransactionRecipt recipt = filler.makeWithdraw(ticket);
			return recipt;
		}
		
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */	
	public TransactionRecipt makeDeposit(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return recipt;
		}else {
			Account filler = getAccount(index);
			TransactionRecipt recipt = filler.makeDeposit(ticket);
			return recipt;
		}
	}

	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionRecipt clearCheck(Check check, TransactionTicket ticket) {
		int account = check.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return recipt;
		}else {
			Account filler = getAccount(index);
			TransactionRecipt recipt = filler.clearCheck(check, ticket);
			return recipt;
		}
	}
		
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does deletes the account if it has a balance of zero,
		if not the receipt is put false.
	The output is a receipt which is made depending on if the transaction went through.
	 */
	public TransactionRecipt deleteAccount(TransactionTicket ticket) {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		if (index == -1) {
			String reason = "Account " + account + " does not exist: ";
			TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return recipt;
		}else {
			Account filler = getAccount(index);
			String type = filler.getType();
			double balance = filler.getBalance();
			if (balance > 0) {
				String reason = "Balance does not equal zero";
				TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, type, balance, balance, "N/A");
				return recipt;
			}else {
				for (int x = index; x < numAccts; x++) {
					accounts[x] = accounts[x + 1];
				}
				numAccts = numAccts - 1;
				TransactionRecipt recipt = new TransactionRecipt(ticket, true, "", type, balance, balance, "N/A");
				return recipt;
			}
		}
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if does not, the information is sent through and all of it is checked.
		If anything is wrong then the recipt if put false and the reason why.
	The output is a receipt which is made depending on if the transaction went through.
	 */
	public TransactionRecipt newAcct(TransactionTicket ticket, Depositor info, String type) {
		int account = ticket.getAcctNum();
		String ssn = info.getSSN();
		int termNext = ticket.getTermOfCD();
		String term = termNext + "";
		double cdBalance = ticket.getAmountOfTransaction();
		int index = findAcct(account);
		
		if (ssn.length() == 9) {
			if (index != -1) {
				String reason = "Account number " + account + " already exists";
				TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
				return recipt;
			}else {
				if (type.equals("Savings") || type.equals("Checking")) {
					Calendar present = Calendar.getInstance();
					present.clear();
					
					TransactionRecipt recipt = new TransactionRecipt(ticket, true, "N/A", type, 0.00, 0.00, "N/A");
					accounts[numAccts] = new Account(info, account, type, 0, present);
					numAccts = numAccts + 1;
					
					return recipt;
				}else if (type.equals("CD")) {
					if ( !(cdBalance >= 0) ) {
						String reason = "CD accounts must have an opening balance of at least 0";
						TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, type, 0.00, 0.00, "N/A");
						return recipt;
					}else {
						if ( !term.equals("6") || !term.equals("6") || !term.equals("6") || !term.equals("6")) {
							String reason = "You chose a term of " + termNext + ", CD accounts must a maturity date that is either,"
									+ " 6, 12, 18, or 24 months away";
							TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, type, cdBalance, cdBalance, "N/A");
							return recipt;
						}else {
							Calendar date = Calendar.getInstance();
							date.clear();
							date.set(Calendar.MONTH, 1);
							date.set(Calendar.DAY_OF_MONTH, 24);
							date.set(Calendar.YEAR, 2023);
							date.add(Calendar.MONTH, termNext);
							
							int month = date.get(Calendar.MONTH) + 1;
							int day = date.get(Calendar.DAY_OF_MONTH);
							int year = date.get(Calendar.YEAR);
							String writtenDate = month + "/" + day + "/" + year;
						
							TransactionRecipt recipt = new TransactionRecipt(ticket, true, "N/A", type, cdBalance, cdBalance, writtenDate);
							accounts[numAccts] = new Account(info, account, type, cdBalance, date);
							numAccts = numAccts + 1;
							
							return recipt;
						}
					}
				}else {
					String reason = "Account type " + type + " cannot be accepted, choose from the choices available "
							+ "(Checking, Savings, CD)";
					TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
					return recipt;
				}
			}
		}else {
			String reason = "SSN " + ssn + " cannot be accpeted as it is not 9 digits long";
			TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
			return recipt;
		}
		
	}

}
