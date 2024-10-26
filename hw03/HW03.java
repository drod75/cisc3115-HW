import java.util.Scanner;
import java.util.Calendar;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
public class HW03 {

	public static void main(String[] args) throws FileNotFoundException {
		String a = "testCases.txt";
		File tc = new File(a);
		Scanner input = new Scanner(tc);
		
		String b = "Output.txt";
		File po = new File(b);
		PrintWriter recipt = new PrintWriter(po);
		
		Bank bank = new Bank();
		
		int numAccts = readAccts(bank);
		printAccounts(bank, recipt, numAccts);
	
		boolean loop = true;		
		do {
			menu();
			numAccts = bank.getNumberOfAccounts();
			
			char option = input.next().charAt(0);
			switch(option){
				case 'q':
				case 'Q':
					printAccounts(bank, recipt, numAccts);
					System.out.println("\tQuitting Bank Program: ");
					loop = false;
					break;
				case 'w':
				case 'W':
					withdrawal(bank, recipt, input);
					break;
				case 'd':
				case 'D':
					deposit(bank, recipt, input);
					break;
				case 'c':
				case 'C':
					clearCheck(bank, recipt, input);
					break;
				case 'n':
				case 'N':
					newAcct(bank, recipt, input, numAccts);
					break;
				case 'i':
				case 'I':
					accountInfo(bank, numAccts, recipt, input);
					break;
				case 'b':
				case 'B':
					balance(bank, recipt, input);
					break;
				case 'x':
				case 'X':
					deleteAcct(bank, recipt, input);
					break;
				default:
	                recipt.println("Error: " + option + " is an invalid selection -  try again");
	                recipt.println();
	                break;
			}		}while (loop);
		
		input.close();
		recipt.close();
	}
	
	/*
	 The Input is an object from Bank class which contains the array of accounts, 
	 	this also includes the number of accounts
	 The process is that the a scanner is used to take the account information from
	 	text file accounts.txt and put it into the Bank objects account array.
	 The output is the actual amount accounts in the banks account array, decided by 
	 	a incremented variable in a loop that goes up until there is no more text file input
	 */
	public static int readAccts(Bank bank) throws FileNotFoundException {
		File file = new File("InititalAccounts.txt");
		Scanner readFrom = new Scanner(file);
		
		while (readFrom.hasNext()) {
			Account fillUp = new Account();
			
			String last = readFrom.next();
			String first = readFrom.next();
			String ssn = readFrom.next();
			int accountNumber = readFrom.nextInt();
			String type = readFrom.next();
			double balance = readFrom.nextDouble();
			
			String maturityDate = readFrom.next();
			Calendar date = Calendar.getInstance();
			if (maturityDate.equals("N/A")) {
				date = Calendar.getInstance();
				date.clear();
			}else {
				String[] a = maturityDate.split("/");
				int[] b = new int[3];
				
				b[0] = Integer.parseInt(a[0]);
				b[1] = Integer.parseInt(a[1]);
				b[2] = Integer.parseInt(a[2]);

				date.set(Calendar.MONTH, b[0] - 1);
				date.set(Calendar.DAY_OF_MONTH, b[1]);
				date.set(Calendar.YEAR, b[2]);
						
			}
			
			Name name = new Name(first, last);
			Depositor depositer = new Depositor(ssn, name);
			fillUp = new Account(depositer, accountNumber, type, balance, date);			
		
			bank.addAccount(fillUp);
		}
		
		readFrom.close();
		return bank.getNumberOfAccounts();
	}
	
	/*
	 The Input is an object from the Bank class which contains the array of accounts, 
	 	the number of accounts, and the PrintWriter that leads to the output file.
	 The process is that the information from each account in the account array is printed to the output file by 
	 	using the getters.
	 There is no other output.
	 */
	public static void printAccounts(Bank bank, PrintWriter recipt, int numAccts) {
		String header = String.format("\t\tCurrent Bank Accounts");
		String rowTitle = String.format("Last Name:\tFirst Name:\tSSN:\t\tAccount Number:\tAccount Type:\tBalance:\tMaturity Date:");
		recipt.println(header);
		recipt.println(rowTitle);
		
		for (int x = 0; x < numAccts; x++) {
			Account account = bank.getAccount(x);

			String first = account.getDepositer().getName().getFirst();
			String last = account.getDepositer().getName().getLast();
			String ssn = account.getDepositer().getSSN();
			int accountNum = account.getAcctNumber();
			String type = account.getType();
			double balance = account.getBalance();
			
			Calendar mDate = account.getMDate();
			int month = mDate.get(Calendar.MONTH) + 1;
			String stringMonth = "" + month;
			if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
			
			
			int day = mDate.get(Calendar.DAY_OF_MONTH);
			String stringDay = "" + day;
			if (stringDay.length() != 2) stringDay = "0" + stringDay;

			int year = mDate.get(Calendar.YEAR);
			String date = stringMonth + "/" + stringDay + "/" + year;
			if (date.equals("01/01/1970")) date = "N/A";
			
			recipt.printf("%-12s", last);
			recipt.printf("%-12s", first);
			recipt.printf("%-9s", ssn);
			recipt.printf(" %8d", accountNum);
			recipt.printf("\t\t\t%-9s", type);
			recipt.printf("\t\t$%8.2f", balance);
			recipt.print("\t" + date);		
			recipt.println();
		}
		recipt.println();

	}
	
	/*
	Input is nothing
	The method prints messages out to the console
	The output is a series of prompts that tells the user all the choices they can makes in the program
	*/
	public static void menu(){
		System.out.println();
	    System.out.println("Select one of the following transactions: ");
	    System.out.println("\t----------------------------");
	    System.out.println("\t    List of Choices         ");
	    System.out.println("\t----------------------------");
	    System.out.println("\t     W(w) -- Withdrawal");
	    System.out.println("\t     D(d) -- Deposit");
	    System.out.println("\t     C(c) -- Clear Check");
	    System.out.println("\t     N(n) -- New Account");
	    System.out.println("\t     B(b) -- Balance Inquiry");
	    System.out.println("\t     I(i) -- Account Info");
	    System.out.println("\t     X(x) -- Delete Account");
	    System.out.println("\t     Q(q) -- Quit");
	    System.out.println();
	    System.out.println("\tEnter your selection: ");
	}

	/*
	 The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the account requested for the balance, then a transcation tikcet
	 	is made to send to the bank class to it's balance method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void balance(Bank bank, PrintWriter recipt, Scanner input) {
		System.out.println("\tEnter the account number: ");
		int account = input.nextInt();
		Calendar present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, present, "balance", 0.00, 0);
		TransactionRecipt getBack = bank.getBalance(ticket);
		
		boolean success = getBack.getFlag();
		if (success == false) {
			recipt.println(present.getTime());
			recipt.println("Transaction Requested: Balance");
			recipt.println("Error: " + getBack.getReason());
		}else {
			if (getBack.getType().equals("CD")) {
				recipt.println(present.getTime());
				recipt.println("Transaction Requested: Balance");
				recipt.println("Account Number: " + account);;
	        	recipt.println("Account type: " + getBack.getType());
	        	recipt.printf("Current Balance: $%.2f",getBack.getPostBalance() );
	        	Calendar mDate = getBack.getPostMDate();
	    		int month = mDate.get(Calendar.MONTH) + 1;
	    		String stringMonth = "" + month;
	    		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
	    		
	    		
	    		int day = mDate.get(Calendar.DAY_OF_MONTH);
	    		String stringDay = "" + day;
	    		if (stringDay.length() != 2) stringDay = "0" + stringDay;

	    		int year = mDate.get(Calendar.YEAR);
	    		String date = stringMonth + "/" + stringDay + "/" + year;
	    		recipt.println();
	    		recipt.println("Maturity Date: " + date);
			}else {
				recipt.println(present.getTime());
				recipt.println("Transaction Requested: Balance");
				recipt.println("Account Number: " + account);;
	        	recipt.println("Account type: " + getBack.getType());
	        	recipt.printf("Current Balance: $%.2f",getBack.getPostBalance() );
	        	recipt.println();
			}
		}
		recipt.println();
	}
	
	/*
	 The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the account requested for the withdraw, then a transcation tikcet
	 	is made to send to the bank class to it's withdraw method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void withdrawal(Bank bank, PrintWriter recipt, Scanner input) {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		int term = input.nextInt();
		
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "Withdrawal", amount, term);
		TransactionRecipt gotBack = bank.makeWithdraw(ticket);
		boolean check = gotBack.getFlag();
		String type = gotBack.getType();
		String reason = gotBack.getReason();
		
		recipt.println(Present.getTime());
		if (reason.contains("does not exist")) {
			recipt.println("Transaction Requested: Withdraw");
	        recipt.println("Error: " + reason);
		    recipt.println();
		}else {
			System.out.println("\tEnter amount to Withdraw: ");
			if (check == false) {
				if (type.equals("CD")) {
					if (reason.contains("not reached") ) {
						recipt.println("Transaction Requested: Withdraw");
				        recipt.println("Account Number: " + account);
				        recipt.println("Account type: " + type);
				        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
				        recipt.println();	            
			            recipt.printf("Amount to Withdraw: $%.2f", amount);
			            recipt.println();
			            
			            Calendar post = gotBack.getPostMDate();
						int month = post.get(Calendar.MONTH);
						int day = post.get(Calendar.DAY_OF_MONTH);
						int year = post.get(Calendar.YEAR);
						String date = month + "/" + day + "/" + year;	
			            
				        recipt.printf("Error: Maturity Date " + date + " not reached yet: ");
					    recipt.println();
					    recipt.println();
					}else {
						recipt.println("Transaction Requested: Withdraw");
				        recipt.println("Account Number: " + account);
				        recipt.println("Account type: " + type);
				        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
				        recipt.println();	            
			            recipt.printf("Amount to Withdraw: $%.2f", amount);
			            recipt.println();
				        recipt.printf("Error: $%.2f is an invalid amount", amount);
					    recipt.println();
					    recipt.println();
					}
				}else {
					recipt.println("Transaction Requested: Withdraw");
			        recipt.println("Account Number: " + account);
			        recipt.println("Account type: " + type);
			        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
			        recipt.println();	            
		            recipt.printf("Amount to Withdraw: $%.2f", amount);
		            recipt.println();
			        recipt.printf("Error: $%.2f is an invalid amount", amount);
				    recipt.println();
				    recipt.println();
				}
			}else {
				if (type.equals("CD")) {
					System.out.println("\tEnter new Maturity term: ");

					recipt.println("Transaction Requested: Withdraw");
				    recipt.println("Account Number: " + account);
			        recipt.println("Account type: " + type);
			        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
					recipt.println();
					recipt.printf("Amount to Withdraw: $%.2f", amount);
		            recipt.println();
			        recipt.printf("New Balance: $%.2f", gotBack.getPostBalance());
					recipt.println();
					
					Calendar post = gotBack.getPostMDate();
					int month = post.get(Calendar.MONTH) + 1;
					int day = post.get(Calendar.DAY_OF_MONTH);
					int year = post.get(Calendar.YEAR);
					String date = month + "/" + day + "/" + year;					
					recipt.print("New Maturity Date: " + date);
					recipt.println();
					recipt.println();
				}else {
					recipt.println("Transaction Requested: Withdraw");
				    recipt.println("Account Number: " + account);
			        recipt.println("Account type: " + type);
			        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
					recipt.println();
					recipt.printf("Amount to Withdraw: $%.2f", amount);
		            recipt.println();
			        recipt.printf("New Balance: $%.2f", gotBack.getPostBalance());
					recipt.println();
					recipt.println();
				}
			}
		}
	}
	

	/*
	 The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the account requested for the deposit, then a transcation tikcet
	 	is made to send to the bank class to it's deposit method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void deposit(Bank bank, PrintWriter recipt, Scanner input) {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		int term = input.nextInt();
		
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "Deposit", amount, term);
		TransactionRecipt gotBack = bank.makeDeposit(ticket);
		boolean check = gotBack.getFlag();
		String type = gotBack.getType();
		String reason = gotBack.getReason();
		
		recipt.println(Present.getTime());
		if (reason.contains("does not exist")) {
			recipt.println("Transaction Requested: Deposit");
	        recipt.println("Error: " + reason);
		    recipt.println();
		}else {
			System.out.println("\tEnter amount to deposit: ");
			if (check == false) {
				if (type.equals("CD")) {
					if (reason.contains("not reached") ) {
						recipt.println("Transaction Requested: Deposit");
				        recipt.println("Account Number: " + account);
				        recipt.println("Account type: " + type);
				        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
				        recipt.println();	            
			            recipt.printf("Amount to Deposit: $%.2f", amount);
			            recipt.println();
			            
			            Calendar post = gotBack.getPostMDate();
						int month = post.get(Calendar.MONTH);
						int day = post.get(Calendar.DAY_OF_MONTH);
						int year = post.get(Calendar.YEAR);
						String date = month + "/" + day + "/" + year;	
			            
				        recipt.printf("Error: Maturity Date " + date + " not reached yet: ");
					    recipt.println();
					    recipt.println();
					}else {
						recipt.println("Transaction Requested: Deposit");
				        recipt.println("Account Number: " + account);
				        recipt.println("Account type: " + type);
				        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
				        recipt.println();	            
			            recipt.printf("Amount to Deposit: $%.2f", amount);
			            recipt.println();
				        recipt.printf("Error: $%.2f is an invalid amount", amount);
					    recipt.println();
					    recipt.println();
					}
				}else {
					recipt.println("Transaction Requested: Deposit");
			        recipt.println("Account Number: " + account);
			        recipt.println("Account type: " + type);
			        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
			        recipt.println();	            
		            recipt.printf("Amount to Deposit: $%.2f", amount);
		            recipt.println();
			        recipt.printf("Error: $%.2f is an invalid amount", amount);
				    recipt.println();
				    recipt.println();
				}
			}else {
				if (type.equals("CD")) {
					System.out.println("\tEnter  Maturity term: ");
					
					recipt.println("Transaction Requested: Deposit");
				    recipt.println("Account Number: " + account);
			        recipt.println("Account type: " + type);
			        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
					recipt.println();
					recipt.printf("Amount to Deposit: $%.2f", amount);
		            recipt.println();
			        recipt.printf("New Balance: $%.2f", gotBack.getPostBalance());
					recipt.println();
					
					Calendar post = gotBack.getPostMDate();
					int month = post.get(Calendar.MONTH) + 1;
					int day = post.get(Calendar.DAY_OF_MONTH);
					int year = post.get(Calendar.YEAR);
					String date = month + "/" + day + "/" + year;					
					recipt.print("New Maturity Date: " + date);
					recipt.println();
					recipt.println();
				}else {
					recipt.println("Transaction Requested: Deposit");
				    recipt.println("Account Number: " + account);
			        recipt.println("Account type: " + type);
			        recipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
					recipt.println();
					recipt.printf("Amount to Deposit: $%.2f", amount);
		            recipt.println();
			        recipt.printf("New Balance: $%.2f", gotBack.getPostBalance());
					recipt.println();
					recipt.println();
				}
			}
		}
	}
	
	
	/*
	 The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the account requested for the deletion, then a transcation tikcet
	 	is made to send to the bank class to it's deletion method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void deleteAcct(Bank bank, PrintWriter recipt, Scanner input){
		System.out.println("\tEnter an account to delete: ");
		int deleteAcct = input.nextInt();
		Calendar present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(deleteAcct, present, "Delete Account", 0.00, 0);
		TransactionRecipt getBack = bank.deleteAccount(ticket);
		
		boolean success = getBack.getFlag();
		String reason = getBack.getReason();
		
		recipt.println(present.getTime());
		if (success == false && reason.contains("does not exist") ) {
			recipt.println("Transaction Requested: Delete Account");
			recipt.println("Account Number: " + deleteAcct);
		    recipt.println("Error: " + deleteAcct + " does not exist");
		    recipt.println();
		}
		else {
			if (success == false && reason.contains("Balance does not equal zero") ) {
				recipt.println("Transaction Requested: Delete Account");
				recipt.println("Account Number: " + deleteAcct);
				recipt.println("Account type: " + getBack.getType());
				recipt.println("Account Balance: " + getBack.getPreBalance());
				recipt.println("Error: Account " + deleteAcct + " does not have a balance of zero");
				recipt.println();
			}else {
				recipt.println("Transaction Requested: Delete Account");
				recipt.println("Account Number: " + deleteAcct);
				recipt.println("Account type: " + getBack.getType() );
				recipt.println("Your account has been deleted, sorry to see you go!");
				recipt.println();
			
			}
		}
	
	}
	
	
	/*
	 The Input is an object from the bank class which contains the array of accounts and number of accounts, it also
	 	receives a printwriter and scanner object.
	 The process is that the test cases file is scanned for the ssn of a user, then it used to locate
	 	all accounts with the ssn and if and else's are used to display the proper messages
	 	to the output file.
	 The output is all info in the output file.
	 */
	public static void accountInfo(Bank bank, int numAccts,PrintWriter recipt, Scanner input) {
		System.out.println("\tEnter your Social Security Number: ");
		String ssn = input.next();
		int yes = 0;
		
		Calendar present = Calendar.getInstance();
		recipt.println(present.getTime());
		
		for (int x = 0; x < numAccts; x++) {
			String check = bank.getAccount(x).getDepositer().getSSN();
			if (check.equals(ssn))yes++;
		}
		if (yes == 0) {
			recipt.println("Transaction Requested: Account Info");
			recipt.println("Error: SSN " + ssn +" does not exist in our database: ");
			recipt.println();
		}else {
			recipt.println("Transaction Requested: Account Info");
			String header = String.format("\t\tCurrent Accounts With Your Matching SSN: ");
			String rowTitle = String.format("Last Name:\tFirst Name:\tSSN:\t\tAccount Number:\tAccount Type:\tBalance:\tMaturity Date:");
			recipt.println(header);
			recipt.println(rowTitle);
			for (int x = 0; x < numAccts; x++) {
				Account account  = bank.getAccount(x);
				String check = account.getDepositer().getSSN();
				if (check.equals(ssn)) {
					String first = account.getDepositer().getName().getFirst();
					String last = account.getDepositer().getName().getLast();
					int accountNum = account.getAcctNumber();
					String type = account.getType();
					double balance = account.getBalance();
					
					Calendar mDate = account.getMDate();
					int month = mDate.get(Calendar.MONTH) + 1;
					String stringMonth = "" + month;
					if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
					
					
					int day = mDate.get(Calendar.DAY_OF_MONTH);
					String stringDay = "" + day;
					if (stringDay.length() != 2) stringDay = "0" + stringDay;

					int year = mDate.get(Calendar.YEAR);
					String date = stringMonth + "/" + stringDay + "/" + year;
					if (date.equals("01/01/1970")) date = "N/A";
					
					recipt.printf("%-12s", last);
					recipt.printf("%-12s", first);
					recipt.printf("%-9s", ssn);
					recipt.printf(" %8d", accountNum);
					recipt.printf("\t\t\t%-9s", type);
					recipt.printf("\t\t$%8.2f", balance);
					recipt.print("\t" + date);		
					recipt.println();		
				}
			}
			recipt.println();
		}
	}
	
	
	/*
	 The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the account requested for the check, then a transcation tikcet
	 	is made to send to the bank class to it's check method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void clearCheck(Bank bank, PrintWriter recipt, Scanner input) {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		String date = input.next();
		
		Calendar present = Calendar.getInstance();
		Check check = new Check(account, amount, date);
		TransactionTicket ticket = new TransactionTicket(account, present, "check", amount, 0);
		TransactionRecipt returnedCheck = bank.clearCheck(check, ticket);
		
		boolean review = returnedCheck.getFlag();
		recipt.println(present.getTime());
		if (review == false) {
			String reason = returnedCheck.getReason();
			if (reason.contains("does not exist:")) {
				recipt.println("Transaction Requested: Withdraw");
		        recipt.println("Error: " + reason);
			}else if(reason.contains("cannot be accpeted to clear a check")) {
				recipt.println("Transaction Requested: Clear Check");
				recipt.println("Account number: " + check.getAcctNum());
				recipt.println("Account type: " + returnedCheck.getType());
				recipt.println("Error: Account type cannot be accpeted to clear a check");
			}else if(reason.contains("Insufficient funds available")) {
				recipt.println("Transaction Requested: Clear Check");
				recipt.println("Account number: " + check.getAcctNum());
				recipt.println("Account type: " + returnedCheck.getType());
				
				recipt.printf("Old Balance: $%.2f", returnedCheck.getPreBalance());
				recipt.println();
				
				recipt.printf("Amount of Check: $%.2f", check.getCheckAmount());
				recipt.println();
				
				recipt.printf("New Balance: $%.2f", returnedCheck.getPostBalance());
				recipt.println();
				
				recipt.println("Error: Insufficient Funds Available - Bounce Fee ($2.50) Charged");
			}else if(reason.contains("not reached yet")) {
				recipt.println("Transaction Requested: Clear Check");
				recipt.println("Account number: " + check.getAcctNum());
				recipt.println("Account type: " + returnedCheck.getType());
				
				recipt.printf("Old Balance: $%.2f", returnedCheck.getPreBalance());
				recipt.println();
				
				recipt.printf("Amount of Check: $%.2f", check.getCheckAmount());
				recipt.println();
				
				recipt.printf("New Balance: $%.2f", returnedCheck.getPostBalance());
				recipt.println();
				
				recipt.println("Error: " + reason);
			}else {
				recipt.println("Transaction Requested: Clear Check");
				recipt.println("Account number: " + check.getAcctNum());
				recipt.println("Account type: " + returnedCheck.getType());
				
				recipt.printf("Old Balance: $%.2f", returnedCheck.getPreBalance());
				recipt.println();
				
				recipt.printf("Amount of Check: $%.2f", check.getCheckAmount());
				recipt.println();
				
				recipt.printf("New Balance: $%.2f", returnedCheck.getPostBalance());
				recipt.println();
				
				recipt.println("Error: " + reason);
			}
		}else {
			recipt.println("Transaction Requested: Clear Check");
			recipt.println("Account number: " + check.getAcctNum());
			recipt.println("Account type: " + returnedCheck.getType());
			
			recipt.printf("Old Balance: $%.2f", returnedCheck.getPreBalance());
			recipt.println();
			
			recipt.printf("Amount of Check: $%.2f", check.getCheckAmount());
			recipt.println();
			
			recipt.printf("New Balance: $%.2f", returnedCheck.getPostBalance());
			recipt.println();
			

		}
		recipt.println();
	}
	
	
	/*
	The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the information for the new account, then a transcation tikcet
	 	is made to send to the bank class to it's new account method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void newAcct(Bank bank, PrintWriter recipt, Scanner input, int numAccts) {
		System.out.println("\tEnter your Last Name: ");
		String last = input.next();
		System.out.println("\tEnter your First Name: ");
		String first = input.next();
		System.out.println("\tEnter your SSN: ");
		String ssn = input.next();
		System.out.println("\tEnter your proposed account number: ");
		int account = input.nextInt();
		String type = input.next();
		double balance = 0.00;
		int term = 0;
		
		if (type.equals("CD")) {
			 balance = input.nextDouble();	
			 term = input.nextInt();
		}
		
		Calendar present = Calendar.getInstance();
		Name name = new Name(first, last);
		Depositor depositor = new Depositor(ssn, name);
		TransactionTicket ticket = new TransactionTicket(account, present, "New Account", balance, term);
		TransactionRecipt getBack = bank.newAcct(ticket, depositor, type);
		
		boolean success = getBack.getFlag();
		String reason = getBack.getReason();	
		recipt.println(present.getTime());
		
		if (success == false) {
			if (reason.contains("already exists")) {
				recipt.println("Transaction Requested: New Account");
				recipt.println("Your Last Name: " + last);
				recipt.println("Your First Name: " + first);
		        recipt.println("Your SSN: " + ssn);
		        recipt.println("Proposed Account Number: " + account);
		        recipt.println("Error: " + reason );
			}else if(reason.contains("opening balance")) {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				System.out.println("\tEnter your opening CD balance");
				
				recipt.println("Transaction Requested: New Account");
				recipt.println("Your Last Name: " + last);
				recipt.println("Your First Name: " + first);
				recipt.println("Your SSN: " + ssn);
				recipt.println("Proposed Account Number: " + account);
				recipt.println("Account type: " + type);
				recipt.println("Opening balance: " + balance);
				recipt.println("Error: " + reason);
			}else if(reason.contains("CD accounts must a maturity date that is either")) {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				System.out.println("\tEnter your opening CD balance");
				System.out.println("\tEnter your opening CD term(6, 12, 8, or 24)");

				recipt.println("Transaction Requested: New Account");
				recipt.println("Your Last Name: " + last);
				recipt.println("Your First Name: " + first);
				recipt.println("Your SSN: " + ssn);
				recipt.println("Proposed Account Number: " + account);
				recipt.println("Account type: " + type);
				recipt.println("Opening balance: " + balance);
				recipt.println("Error: " + reason);
			}else if(reason.contains("not 9 digits long")) {
				recipt.println("Transaction Requested: New Account");
				recipt.println("Your Last Name: " + last);
				recipt.println("Your First Name: " + first);
				recipt.println("Your SSN: " + ssn);
				recipt.println("Error: " + reason);
			}else {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				
				recipt.println("Transaction Requested: New Account");
				recipt.println("Your Last Name: " + last);
				recipt.println("Your First Name: " + first);
				recipt.println("Your SSN: " + ssn);
				recipt.println("Proposed Account Number: " + account);
				recipt.println("Account type: " + type);
				recipt.println("Error: " + reason);
			}
		}else {
			System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
			if (type.equals("CD")) {
				System.out.println("\tEnter your opening CD balance");
				System.out.println("\tEnter your opening CD term(6, 12, 8, or 24)");

				recipt.println("Transaction Requested: New Account");
				recipt.println("Your Last Name: " + last);
				recipt.println("Your First Name: " + first);
				recipt.println("Your SSN: " + ssn);
				recipt.println("Proposed Account Number: " + account);
				recipt.println("Account type: " + type);
				recipt.println("Opening balance: " + balance);
				
				Calendar date = getBack.getPostMDate();
				int month = date.get(Calendar.MONTH) + 1;
				int day = date.get(Calendar.DAY_OF_MONTH);
				int year = date.get(Calendar.YEAR);
				String writtenDate = month + "/" + day + "/" + year;
				
				recipt.println("Maturity Date: " + writtenDate);
				recipt.println("Congratulations! Your account " + account + " has been made, welcome to our Bank!");
			}else {
				recipt.println("Transaction Requested: New Account");
				recipt.println("Your Last Name: " + last);
				recipt.println("Your First Name: " + first);
				recipt.println("Your SSN: " + ssn);
				recipt.println("Proposed Account Number: " + account);
				recipt.println("Account type: " + type);
				recipt.println("Congratulations! Your account " + account + " has been made, welcome to our Bank!");
			}
			
		}
	
		recipt.println();
	}
	
}