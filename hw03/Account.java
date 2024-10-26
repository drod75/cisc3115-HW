/*
 The BankAccount class contains the users account number, info from the depositor class, balance, account type, 
 and the maturity date if it is a cd, the method also contains constructors, and methods
 */
import java.util.Calendar;
public class Account {
	private Depositor info;
	private int acctNumber;
	private double balance;
	private String acctType;
	private Calendar maturityDate = Calendar.getInstance();
	
	//No-Arg Constructor
	public Account () {
		info = new Depositor();
		acctNumber = 0;
		balance = 0.0;
		acctType = "";
		maturityDate = Calendar.getInstance();
		maturityDate.clear();
	}
	
	//Parameterized Constructor
	public Account (Depositor d, int acctN, String acctT, double b, Calendar MDate) {
		info = d;
		acctNumber = acctN;
		balance = b;
		acctType = acctT;
		maturityDate = MDate;
	}
	
	//Parameterized Constructor
	
	//getters
	public Depositor getDepositer() {
		return info;
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
	public Calendar getMDate() {
		return maturityDate;
	}

	/*The input is a transaction ticket object.
	 The process is that it creates a  a transaction receipt object with the current balance and account type.
	 The output is the transaction receipt object.
	*/
	public TransactionRecipt getBalance(TransactionTicket ticket) {
		Calendar mDate = getMDate();
		int month = mDate.get(Calendar.MONTH) + 1;
		String stringMonth = "" + month;
		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
		
		
		int day = mDate.get(Calendar.DAY_OF_MONTH);
		String stringDay = "" + day;
		if (stringDay.length() != 2) stringDay = "0" + stringDay;

		int year = mDate.get(Calendar.YEAR);
		String date = stringMonth + "/" + stringDay + "/" + year;
		if (date.equals("01/01/1970")) date = "N/A";
		
		TransactionRecipt recipt = new TransactionRecipt(ticket, true, "N/A", acctType, balance, balance, date);
		return recipt;
	}
	
	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object if the withdraw amount is valid, or if the account
	 is a cd and the maturity date has been reached, if it has a int is used to extend the date by that term. If anything is
	 wrong the receipt is given false and the reason why it is false.
	 The output is the transaction receipt object.
	*/
	public TransactionRecipt makeWithdraw(TransactionTicket ticket) {
		double withdrawAmount = ticket.getAmountOfTransaction();
		if  (acctType.equals("CD")) {
			Calendar present = Calendar.getInstance();
			if (present.after(maturityDate) == false) {
				int month = maturityDate.get(Calendar.MONTH);
				int day = maturityDate.get(Calendar.DAY_OF_MONTH);
				int year = maturityDate.get(Calendar.YEAR);
				String date = month + "/" + day + "/" + year;
				TransactionRecipt recipt = new TransactionRecipt(ticket, false, "Maturity Date not reached", 
						"CD", 0.00, 0.00, date);
				return recipt;
			}else {
				if(withdrawAmount <= 0.00 || withdrawAmount > balance) {
					TransactionRecipt recipt = new TransactionRecipt(ticket, false, "Amount can not be accepted", 
							"CD", balance, balance, "N/A");
					return recipt;
				}else {
					int newTerm = ticket.getTermOfCD();
					double postBal = balance - withdrawAmount;
					maturityDate.add(Calendar.MONTH, newTerm);
					int month = maturityDate.get(Calendar.MONTH);
					int day = maturityDate.get(Calendar.DAY_OF_MONTH);
					int year = maturityDate.get(Calendar.YEAR);
					String date = month + "/" + day + "/" + year;
					TransactionRecipt recipt = new TransactionRecipt(ticket, true, "N/A", acctType, balance, postBal, date);
					balance = balance - withdrawAmount;
					return recipt;
				}
			}
		}else {
			if(withdrawAmount <= 0.00 || withdrawAmount > balance) {
				TransactionRecipt recipt = new TransactionRecipt(ticket, false, "Amount can not be accepted", 
						acctType, balance, balance, "N/A");
				return recipt;
			}else {
				double postBal = balance - withdrawAmount;
				TransactionRecipt recipt = new TransactionRecipt(ticket, true, "N/A", acctType, balance, postBal, "N/A");
				balance = balance - withdrawAmount;
				return recipt;
			}

		}
	}
	
	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object that is true if, the cd account's maturity has been reached
	 the amount is valid, if anything is wrong the receipt is given false and the reason why.
	 The output is the transaction receipt object.
	*/
	public TransactionRecipt makeDeposit(TransactionTicket ticket) {
		double depositAmount = ticket.getAmountOfTransaction();
		if  (acctType.equals("CD")) {
			Calendar present = Calendar.getInstance();
			if (present.after(maturityDate) == false) {
				int month = maturityDate.get(Calendar.MONTH);
				int day = maturityDate.get(Calendar.DAY_OF_MONTH);
				int year = maturityDate.get(Calendar.YEAR);
				String date = month + "/" + day + "/" + year;
				TransactionRecipt recipt = new TransactionRecipt(ticket, false, "Maturity Date not reached", 
						"CD", 0.00, 0.00, date);
				return recipt;
			}else {
				if(depositAmount <= 0.00) {
					TransactionRecipt recipt = new TransactionRecipt(ticket, false, "Amount can not be accepted", 
							"CD", balance, balance, "N/A");
					return recipt;
				}else {
					int newTerm = ticket.getTermOfCD();
					double postBal = balance + depositAmount;
					
					maturityDate.add(Calendar.MONTH, newTerm);
					int month = maturityDate.get(Calendar.MONTH);
					int day = maturityDate.get(Calendar.DAY_OF_MONTH);
					int year = maturityDate.get(Calendar.YEAR);
					String date = month + "/" + day + "/" + year;
					TransactionRecipt recipt = new TransactionRecipt(ticket, true, "N/A", acctType, balance, postBal, date);
					balance += depositAmount;
					return recipt;
				}
			}
		}else {
			if(depositAmount <= 0.00) {
				TransactionRecipt recipt = new TransactionRecipt(ticket, false, "Amount can not be accepted", 
						acctType, balance, balance, "N/A");
				return recipt;
			}else {
				double postBal = balance + depositAmount;
				TransactionRecipt recipt = new TransactionRecipt(ticket, true, "N/A", acctType, balance, postBal, "N/A");
				balance += depositAmount;
				return recipt;
			}

		}
	}
	
	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object with the goal of checking if the check date is no more than
	 	6 months ago, if the check amount is within the accounts balance, and if the account is a checking account.
	 	If anything is wrong the receipt gets false and the reason why.
	 The output is the transaction receipt object.
	*/
	public TransactionRecipt clearCheck(Check check, TransactionTicket ticket) {
		if (acctType.equals("Checking")) {
			double amount = check.getCheckAmount();
			if (amount > balance) {
				String reason = "Insufficient funds available";
				double post = balance - 2.50;
				TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, acctType, balance, post, "N/A");
				balance = balance - 2.50;
				return recipt;
			}else {
				Calendar checkDate = check.getCheckDate();
				Calendar present = Calendar.getInstance();
				
				int month = checkDate.get(Calendar.MONTH) + 1;
				int day = checkDate.get(Calendar.DAY_OF_MONTH);
				int year = checkDate.get(Calendar.YEAR);
				String date = month + "/" + day + "/" + year;
				
				if (present.before(checkDate) == true) {
					String reason = "Check date (" + date + ") not reached yet";
					TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, acctType, balance, balance, "N/A");
					return recipt;
				}else {
					Calendar checkBy = check.getCheckDate();
					checkBy.add(Calendar.MONTH, 6);
					
					if (present.after(checkBy) == true) {
						int month1 = checkBy.get(Calendar.MONTH) + 1;
						int day1 = checkBy.get(Calendar.DAY_OF_MONTH);
						int year1 = checkBy.get(Calendar.YEAR);
						String dateBy = month1 + "/" + day1 + "/" + year1;
						
						String reason = "The check is too old and  dated at (" + date + "), "
								+ "for reference, it should have been chased in by " + dateBy;
						TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, acctType, balance, balance, "N/A");
						return recipt;
					}else {
						double postBal = balance - amount;
						TransactionRecipt recipt = new TransactionRecipt(ticket, true, "", acctType, balance, postBal, "N/A");
						balance = balance - amount;
						return recipt;
					}
				}
				
			}
		
		}else  {
			String reason = "Account type " + acctType + " cannot be accpeted to clear a check";
			TransactionRecipt recipt = new TransactionRecipt(ticket, false, reason, acctType, 0.00, balance, "N/A");
			return recipt;
		}
		
		
	}

}
