import java.util.Calendar;

public class CheckingAccount extends Account{
	
	//Constructors
	public CheckingAccount() {
		super();
	}
	public CheckingAccount(Depositor d, int acctN, String acctT, double b, boolean status) {
		super(d, acctN, acctT, b, status);
	}
	public CheckingAccount(CheckingAccount account) {
		super(account);
	}
	
	/*The input is a transaction ticket object.
	 The process is that it creates a  a transaction receipt object with the current balance and account type.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt getCurrentBalance(TransactionTicket ticket) {
		if (getStatus() == false) {
			String reason = "Account " + ticket.getAcctNum() + " is Closed";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}else {
			TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", getType(), balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}
	}

	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object that is true if the amount is valid, 
	 	if anything is wrong the receipt is given false and the reason why.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt makeDeposit(TransactionTicket ticket) {
		if (getStatus() == false) {
			String reason = "Account " + ticket.getAcctNum() + " is Closed";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}else {
			Double depositAmount = ticket.getAmountOfTransaction();
			if(depositAmount <= 0.00) {
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, "Amount can not be accepted", 
						getType(), balance, balance, "N/A");
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			}else {
				double postBal = balance + depositAmount;
				TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", getType(), balance, postBal, "N/A");
				balance += depositAmount;
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			}
		}
		
	}

	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object if the withdraw amount is valid.If anything is
	 	wrong the receipt is given false and the reason why it is false.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt makeWithdrawal(TransactionTicket ticket) {
		if (getStatus() == false) {
			String reason = "Account " + ticket.getAcctNum() + " is Closed";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}else {
			double withdrawAmount = ticket.getAmountOfTransaction();
			if(withdrawAmount <= 0.00 || withdrawAmount > balance) {
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, "Amount can not be accepted", 
						getType(), balance, balance, "N/A");
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			}else {
				if (balance < 2500) {
					double postBal = balance - withdrawAmount - 1.50;
					String reason = "Successful Transaction, but a fee of $1.50 was charged for a initial balance of under $2500 ";
					TransactionReceipt receipt = new TransactionReceipt(ticket, true, reason, getType(), balance, postBal, "N/A");
					balance = balance - withdrawAmount - 1.50;
					addTransaction(receipt);
					return new TransactionReceipt(receipt);
				}else {
					double postBal = balance - withdrawAmount;
					TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", getType(), balance, postBal, "N/A");
					balance = balance - withdrawAmount;
					addTransaction(receipt);
					return new TransactionReceipt(receipt);
				}
			}
		}
		
	}
	
	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object with the goal of checking if the check date is no more than
	 	6 months ago, if the check amount is within the accounts balance, and if the account is a checking account.
	 	If anything is wrong the receipt gets false and the reason why.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt clearCheck(Check check, TransactionTicket ticket) {
		if (getStatus() == false) {
			String reason = "Account " + ticket.getAcctNum() + " is Closed";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}else {
			double amount = check.getCheckAmount();
			if (amount > balance) {
				String reason = "Insufficient funds available";
				double post = balance - 2.50;
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, post, "N/A");
				balance = balance - 2.50;
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			}else {
				Calendar checkDate = check.getCheckDate();
				Calendar present = Calendar.getInstance();
				
				int month = checkDate.get(Calendar.MONTH) + 1;
				int day = checkDate.get(Calendar.DAY_OF_MONTH);
				int year = checkDate.get(Calendar.YEAR);
				String date = month + "/" + day + "/" + year;
				
				if (present.before(checkDate) == true) {
					String reason = "Check date (" + date + ") not reached yet";
					TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
					addTransaction(receipt);
					return new TransactionReceipt(receipt);
				}else {
					Calendar checkBy = check.getCheckDate();
					checkBy.add(Calendar.MONTH, 6);
					
					if (present.after(checkBy) == true) {
						int month1 = checkBy.get(Calendar.MONTH) + 1;
						int day1 = checkBy.get(Calendar.DAY_OF_MONTH);
						int year1 = checkBy.get(Calendar.YEAR);
						String dateBy = month1 + "/" + day1 + "/" + year1;
						
						String reason = "The check is too old and dated at (" + date + "), "
								+ "for reference, it should have been cashed in by " + dateBy;
						TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
						addTransaction(receipt);
						return new TransactionReceipt(receipt);
					}else {
						if (balance < 2500) {
							double postBal = balance - amount - 1.50;
							String reason = "Successful Transaction, but a fee of $1.50 was charged for a initial balance of under $2500";
							TransactionReceipt receipt = new TransactionReceipt(ticket, true, reason, getType(), balance, postBal, "N/A");
							balance = balance - amount - 1.50;
							addTransaction(receipt);
							return new TransactionReceipt(receipt);
						}else {
							double postBal = balance - amount;
							TransactionReceipt receipt = new TransactionReceipt(ticket, true, "", getType(), balance, postBal, "N/A");
							balance = balance - amount;
							addTransaction(receipt);
							return new TransactionReceipt(receipt);
						}
					}
				}
			}
			
		}
		
	}
	
	// to String
	public String toString() {
		String toStr = String.format("%-12s%-12s%-9s %8d\t\t\t%-9s\t\t$%8.2f\tN/A", getDepositer().getName().getLast(), getDepositer().getName().getFirst(),
				getDepositer().getSSN(), getAcctNumber(), getType(), balance);
		return toStr;
	}
		
}
