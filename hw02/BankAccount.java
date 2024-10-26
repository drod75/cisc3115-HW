/*
 The BankAccount class contains the users account number, info from the depositor class, balance, and account type, 
 as well as setters and getters.
 */
public class BankAccount {
	private Depositer info = new Depositer();
	private int acctNumber;
	private double balance;
	private String acctType;
	
	public void setDepositer(Depositer set) {
		info = set;
	}
	public void setAcctNumber(int set) {
		acctNumber = set;
	}
	public void setBalance(double amount) {
		balance = amount;
		
	}
	public void setType(String set) {
		acctType = set;
	}
	
	public Depositer getDepositer() {
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
}
