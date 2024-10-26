import java.util.Scanner;
import java.util.Calendar;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
public class HW04 {

	public static void main(String[] args) throws FileNotFoundException {
		String a = "testCases.txt";
		File tc = new File(a);
		Scanner input = new Scanner(tc);
		
		String b = "Output.txt";
		File po = new File(b);
		PrintWriter receipt = new PrintWriter(po);
		
		Bank bank = new Bank();
		
		int numAccts = readAccts(bank);
		printAccounts(bank, receipt, numAccts);
	
		boolean loop = true;		
		do {
			menu();
			numAccts = bank.getNumberOfAccounts();
			
			char option = input.next().charAt(0);
			switch(option){
				case 'q':
				case 'Q':
					printAccounts(bank, receipt, numAccts);
					System.out.println("\tQuitting Bank Program: ");
					loop = false;
					break;
				case 'w':
				case 'W':
					withdrawal(bank, receipt, input);
					break;
				case 'd':
				case 'D':
					deposit(bank, receipt, input);
					break;
				case 'c':
				case 'C':
					clearCheck(bank, receipt, input);
					break;
				case 'n':
				case 'N':
					newAcct(bank, receipt, input, numAccts);
					break;
				case 'i':
				case 'I':
					accountInfo(bank, numAccts, receipt, input);
					break;
				case 'h':
				case 'H':
					accountInfoWithHistory(bank, numAccts, receipt, input);
					break;
				case 'S':
				case 's':
					closeAccount(bank, receipt, input);
					break;
				case 'R':
				case 'r':
					reOpenAccount(bank, receipt, input);
					break;
				case 'b':
				case 'B':
					balance(bank, receipt, input);
					break;
				case 'x':
				case 'X':
					deleteAcct(bank, receipt, input);
					break;
				default:
	                receipt.println("Error: " + option + " is an invalid selection -  try again");
	                receipt.println();
	                break;
			}		}while (loop);
		
		input.close();
		receipt.close();
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
		Account fillUp = new Account();
		
		while (readFrom.hasNext()) {
			
			String last = readFrom.next();
			String first = readFrom.next();
			String ssn = readFrom.next();
			int accountNumber = readFrom.nextInt();
			String type = readFrom.next();
			double balance = readFrom.nextDouble();
			
			String maturityDate = readFrom.next();
			Calendar date;
			if (maturityDate.equals("N/A")) {
				date = Calendar.getInstance();
				date.clear();
			}else {
				date = Calendar.getInstance();
				date.clear();
				
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
			fillUp = new Account(depositer, accountNumber, type, balance, date, true);			
		
			
			Calendar present = Calendar.getInstance();
			TransactionTicket ticket = new TransactionTicket(accountNumber, present, "New Account", balance, 0);
			TransactionReceipt getBack = new TransactionReceipt(ticket, true, "N/A", type, balance, balance, maturityDate);
			fillUp.addTransaction(getBack);
			
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
	public static void printAccounts(Bank bank, PrintWriter receipt, int numAccts) {
		String header = String.format("\t\tCurrent Bank Accounts");
		String rowTitle = String.format("Last Name:\tFirst Name:\tSSN:\t\tAccount Number:\tAccount Type:\tBalance:\tMaturity Date:");
		receipt.println(header);
		receipt.println(rowTitle);
		
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
			
			receipt.printf("%-12s", last);
			receipt.printf("%-12s", first);
			receipt.printf("%-9s", ssn);
			receipt.printf(" %8d", accountNum);
			receipt.printf("\t\t\t%-9s", type);
			receipt.printf("\t\t$%8.2f", balance);
			receipt.print("\t" + date);		
			receipt.println();
		}
		receipt.println();

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
	    System.out.println("\t     H(h) -- Account Info with Transaction History");
	    System.out.println("\t     S(s) -- Close an account");
	    System.out.println("\t     R(r) -- Reopen Account");
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
	public static void balance(Bank bank, PrintWriter receipt, Scanner input) {
		System.out.println("\tEnter the account number: ");
		int account = input.nextInt();
		Calendar present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, present, "balance", 0.00, 0);
		TransactionReceipt getBack = bank.getBalance(ticket);
		
		boolean success = getBack.getFlag();
		if (success == false) {
			receipt.println(present.getTime());
			receipt.println("Transaction Requested: Balance");
			receipt.println("Error: " + getBack.getReason());
		}else {
			if (getBack.getType().equals("CD")) {
				receipt.println(present.getTime());
				receipt.println("Transaction Requested: Balance");
				receipt.println("Account Number: " + account);;
	        	receipt.println("Account type: " + getBack.getType());
	        	receipt.printf("Current Balance: $%.2f",getBack.getPostBalance() );
	        	
	        	Calendar mDate = getBack.getPostMDate();
	    		int month = mDate.get(Calendar.MONTH) + 1;
	    		String stringMonth = "" + month;
	    		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
	    		
	    		
	    		int day = mDate.get(Calendar.DAY_OF_MONTH);
	    		String stringDay = "" + day;
	    		if (stringDay.length() != 2) stringDay = "0" + stringDay;

	    		int year = mDate.get(Calendar.YEAR);
	    		String date = stringMonth + "/" + stringDay + "/" + year;
	    		receipt.println();
	    		receipt.println("Maturity Date: " + date);
			}else {
				receipt.println(present.getTime());
				receipt.println("Transaction Requested: Balance");
				receipt.println("Account Number: " + account);;
	        	receipt.println("Account type: " + getBack.getType());
	        	receipt.printf("Current Balance: $%.2f",getBack.getPostBalance() );
	        	receipt.println();
			}
		}
		receipt.println();
	}
	
	/*
	 The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the account requested for the withdraw, then a transcation tikcet
	 	is made to send to the bank class to it's withdraw method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void withdrawal(Bank bank, PrintWriter receipt, Scanner input) {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		int term = input.nextInt();
		
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "Withdrawal", amount, term);
		TransactionReceipt gotBack = bank.makeWithdraw(ticket);
		boolean check = gotBack.getFlag();
		String type = gotBack.getType();
		String reason = gotBack.getReason();
		
		receipt.println(Present.getTime());
		if (reason.contains("does not exist")) {
			receipt.println("Transaction Requested: Withdraw");
	        receipt.println("Error: " + reason);
		    receipt.println();
		}else if(reason.contains("is Closed")){
			receipt.println("Transaction Requested: Withdraw");
	        receipt.println("Account Number: " + account);
		    receipt.println("Error: " + reason);
		    receipt.println();
		}else {
			System.out.println("\tEnter amount to Withdraw: ");
			if (check == false) {
				if (type.equals("CD")) {
					if (reason.contains("not reached") ) {
						receipt.println("Transaction Requested: Withdraw");
				        receipt.println("Account Number: " + account);
				        receipt.println("Account type: " + type);
				        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
				        receipt.println();	            
			            receipt.printf("Amount to Withdraw: $%.2f", amount);
			            receipt.println();
			            
			            Calendar post = gotBack.getPostMDate();
						int month = post.get(Calendar.MONTH);
						int day = post.get(Calendar.DAY_OF_MONTH);
						int year = post.get(Calendar.YEAR);
						String date = month + "/" + day + "/" + year;	
			            
				        receipt.printf("Error: Maturity Date " + date + " not reached yet: ");
					    receipt.println();
					    receipt.println();
					}else {
						receipt.println("Transaction Requested: Withdraw");
				        receipt.println("Account Number: " + account);
				        receipt.println("Account type: " + type);
				        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
				        receipt.println();	            
			            receipt.printf("Amount to Withdraw: $%.2f", amount);
			            receipt.println();
				        receipt.printf("Error: $%.2f is an invalid amount", amount);
					    receipt.println();
					    receipt.println();
					}
				}else {
					receipt.println("Transaction Requested: Withdraw");
			        receipt.println("Account Number: " + account);
			        receipt.println("Account type: " + type);
			        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
			        receipt.println();	            
		            receipt.printf("Amount to Withdraw: $%.2f", amount);
		            receipt.println();
			        receipt.printf("Error: $%.2f is an invalid amount", amount);
				    receipt.println();
				    receipt.println();
				}
			}else {
				if (type.equals("CD")) {
					System.out.println("\tEnter new Maturity term: ");

					receipt.println("Transaction Requested: Withdraw");
				    receipt.println("Account Number: " + account);
			        receipt.println("Account type: " + type);
			        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
					receipt.println();
					receipt.printf("Amount to Withdraw: $%.2f", amount);
		            receipt.println();
			        receipt.printf("New Balance: $%.2f", gotBack.getPostBalance());
					receipt.println();
					
					Calendar post = gotBack.getPostMDate();
					int month = post.get(Calendar.MONTH) + 1;
					int day = post.get(Calendar.DAY_OF_MONTH);
					int year = post.get(Calendar.YEAR);
					String date = month + "/" + day + "/" + year;					
					receipt.print("New Maturity Date: " + date);
					receipt.println();
					receipt.println();
				}else {
					receipt.println("Transaction Requested: Withdraw");
				    receipt.println("Account Number: " + account);
			        receipt.println("Account type: " + type);
			        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
					receipt.println();
					receipt.printf("Amount to Withdraw: $%.2f", amount);
		            receipt.println();
			        receipt.printf("New Balance: $%.2f", gotBack.getPostBalance());
					receipt.println();
					receipt.println();
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
	public static void deposit(Bank bank, PrintWriter receipt, Scanner input) {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		int term = input.nextInt();
		
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "Deposit", amount, term);
		TransactionReceipt gotBack = bank.makeDeposit(ticket);
		boolean check = gotBack.getFlag();
		String type = gotBack.getType();
		String reason = gotBack.getReason();
		
		receipt.println(Present.getTime());
		if (reason.contains("does not exist")) {
			receipt.println("Transaction Requested: Deposit");
	        receipt.println("Error: " + reason);
		    receipt.println();
		}else if(reason.contains("is Closed")){
			receipt.println("Transaction Requested: Deposit");
	        receipt.println("Account Number: " + account);
		    receipt.println("Error: " + reason);
		    receipt.println();
		}else {
			System.out.println("\tEnter amount to deposit: ");
			if (check == false) {
				if (type.equals("CD")) {
					if (reason.contains("not reached") ) {
						receipt.println("Transaction Requested: Deposit");
				        receipt.println("Account Number: " + account);
				        receipt.println("Account type: " + type);
				        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
				        receipt.println();	            
			            receipt.printf("Amount to Deposit: $%.2f", amount);
			            receipt.println();
			            
			            Calendar post = gotBack.getPostMDate();
						int month = post.get(Calendar.MONTH);
						int day = post.get(Calendar.DAY_OF_MONTH);
						int year = post.get(Calendar.YEAR);
						String date = month + "/" + day + "/" + year;	
			            
				        receipt.printf("Error: Maturity Date " + date + " not reached yet: ");
					    receipt.println();
					    receipt.println();
					}else {
						receipt.println("Transaction Requested: Deposit");
				        receipt.println("Account Number: " + account);
				        receipt.println("Account type: " + type);
				        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
				        receipt.println();	            
			            receipt.printf("Amount to Deposit: $%.2f", amount);
			            receipt.println();
				        receipt.printf("Error: $%.2f is an invalid amount", amount);
					    receipt.println();
					    receipt.println();
					}
				}else {
					receipt.println("Transaction Requested: Deposit");
			        receipt.println("Account Number: " + account);
			        receipt.println("Account type: " + type);
			        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
			        receipt.println();	            
		            receipt.printf("Amount to Deposit: $%.2f", amount);
		            receipt.println();
			        receipt.printf("Error: $%.2f is an invalid amount", amount);
				    receipt.println();
				    receipt.println();
				}
			}else {
				if (type.equals("CD")) {
					System.out.println("\tEnter  Maturity term: ");
					
					receipt.println("Transaction Requested: Deposit");
				    receipt.println("Account Number: " + account);
			        receipt.println("Account type: " + type);
			        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
					receipt.println();
					receipt.printf("Amount to Deposit: $%.2f", amount);
		            receipt.println();
			        receipt.printf("New Balance: $%.2f", gotBack.getPostBalance());
					receipt.println();
					
					Calendar post = gotBack.getPostMDate();
					int month = post.get(Calendar.MONTH) + 1;
					int day = post.get(Calendar.DAY_OF_MONTH);
					int year = post.get(Calendar.YEAR);
					String date = month + "/" + day + "/" + year;					
					receipt.print("New Maturity Date: " + date);
					receipt.println();
					receipt.println();
				}else {
					receipt.println("Transaction Requested: Deposit");
				    receipt.println("Account Number: " + account);
			        receipt.println("Account type: " + type);
			        receipt.printf("Old Balance: $%.2f", gotBack.getPreBalance());
					receipt.println();
					receipt.printf("Amount to Deposit: $%.2f", amount);
		            receipt.println();
			        receipt.printf("New Balance: $%.2f", gotBack.getPostBalance());
					receipt.println();
					receipt.println();
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
	public static void deleteAcct(Bank bank, PrintWriter receipt, Scanner input){
		System.out.println("\tEnter an account to delete: ");
		int deleteAcct = input.nextInt();
		Calendar present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(deleteAcct, present, "Delete Account", 0.00, 0);
		TransactionReceipt getBack = bank.deleteAccount(ticket);
		
		boolean success = getBack.getFlag();
		String reason = getBack.getReason();
		
		receipt.println(present.getTime());
		if (success == false && reason.contains("does not exist") ) {
			receipt.println("Transaction Requested: Delete Account");
			receipt.println("Account Number: " + deleteAcct);
		    receipt.println("Error: " + deleteAcct + " does not exist");
		    receipt.println();
		}
		else {
			if (success == false && reason.contains("Balance does not equal zero") ) {
				receipt.println("Transaction Requested: Delete Account");
				receipt.println("Account Number: " + deleteAcct);
				receipt.println("Account type: " + getBack.getType());
				receipt.println("Account Balance: " + getBack.getPreBalance());
				receipt.println("Error: Account " + deleteAcct + " does not have a balance of zero");
				receipt.println();
			}else {
				receipt.println("Transaction Requested: Delete Account");
				receipt.println("Account Number: " + deleteAcct);
				receipt.println("Account type: " + getBack.getType() );
				receipt.println("Your account has been deleted, sorry to see you go!");
				receipt.println();
			
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
	public static void accountInfo(Bank bank, int numAccts,PrintWriter receipt, Scanner input) {
		System.out.println("\tEnter your Social Security Number: ");
		String ssn = input.next();
		int yes = 0;
		
		Calendar present = Calendar.getInstance();
		receipt.println(present.getTime());
		
		for (int x = 0; x < numAccts; x++) {
			String check = bank.getAccount(x).getDepositer().getSSN();
			if (check.equals(ssn))yes++;
		}
		if (yes == 0) {
			receipt.println("Transaction Requested: Account Info");
			receipt.println("Error: SSN " + ssn +" does not exist in our database: ");
			receipt.println();
		}else {
			receipt.println("Transaction Requested: Account Info");
			String header = String.format("\t\tCurrent Accounts With Your Matching SSN: ");
			String rowTitle = String.format("Last Name:\tFirst Name:\tSSN:\t\tAccount Number:\tAccount Type:\tBalance:\tMaturity Date:");
			receipt.println(header);
			receipt.println(rowTitle);
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
					
					receipt.printf("%-12s", last);
					receipt.printf("%-12s", first);
					receipt.printf("%-9s", ssn);
					receipt.printf(" %8d", accountNum);
					receipt.printf("\t\t\t%-9s", type);
					receipt.printf("\t\t$%8.2f", balance);
					receipt.print("\t" + date);		
					receipt.println();		
				}
			}
			receipt.println();
		}
	}
		
	/*
	 The input is a bank class object, a printwriter, and a scanner object.
	 The process is that the bank object to extract the information under the ssns accounts.
	 The output is to the output file and the accounts under that ssn and its accounts plus those accounts transactions. 
   		if the ssn does not exist in the database than that is accounted for.
	 */
	public static void accountInfoWithHistory(Bank bank, int numAccts,PrintWriter receipt, Scanner input) {
		System.out.println("\tEnter your Social Security Number: ");
		String ssn = input.next();
		int yes = 0;
		
		Calendar present = Calendar.getInstance();
		receipt.println();
		receipt.println(present.getTime());
		
		for (int x = 0; x < numAccts; x++) {
			String check = bank.getAccount(x).getDepositer().getSSN();
			if (check.equals(ssn))yes++;
		}
		if (yes == 0) {
			receipt.println("Transaction Requested: Account Info With Transaction History");
			receipt.println("Error: SSN " + ssn +" does not exist in our database: ");
			receipt.println();
		}else {
			receipt.println("Transaction Requested: Account Info with Transaction History");
			String header = String.format("\t\tCurrent Accounts With Your Matching SSN and their History: ");
			String rowTitle = String.format("Last Name:\tFirst Name:\tSSN:\t\tAccount Number:\tAccount Type:\tBalance:\tMaturity Date:");
			receipt.println(header);
			
			for (int x = 0; x < numAccts; x++) {
				
				Account account  = bank.getAccount(x);
				String check = account.getDepositer().getSSN();
				if (check.equals(ssn)) {
					receipt.println(rowTitle);
					
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
					
					receipt.printf("%-12s", last);
					receipt.printf("%-12s", first);
					receipt.printf("%-9s", ssn);
					receipt.printf(" %8d", accountNum);
					receipt.printf("\t\t\t%-9s", type);
					receipt.printf("\t\t$%8.2f", balance);
					receipt.print("\t" + date);
					receipt.println();
					
					receipt.println("-----Account Transactions: ");
					
					String transactionHeader = String.format("Date:\t\t\tTransaction:\tAmount:\t\tStatus:\tBalance:\tReason For Failure:");
					receipt.println(transactionHeader);
					
					int numTransactions = account.getAmountOfTransactions();
					
					TransactionTicket ticket = new TransactionTicket(account.getAcctNumber(), present, "Transaction History", 0.00, 0);
					ArrayList<TransactionReceipt> history = account.getTransactionHistory(ticket);
					for (int z = 0; z < numTransactions; z++) {
						
						TransactionReceipt soloHistory = history.get(z);
						
						Calendar transDate = soloHistory.getTINfo().getDateOfTransaction();
						int transMonth = transDate.get(Calendar.MONTH) + 1;
						String transMonthString = "" + transMonth;
						if (transMonthString.length() != 2) transMonthString = "0" + transMonthString;
						
						
						int transDay = transDate.get(Calendar.DAY_OF_MONTH);
						String transDayString = "" + transDay;
						if (transDayString.length() != 2) transDayString = "0" + transDayString;

						int transYear = transDate.get(Calendar.YEAR);
						String transYearString = transMonthString + "/" + transDayString + "/" + transYear;
						
						
						
						receipt.printf("%-6s\t\t", transYearString);
						receipt.printf("%-12s\t", soloHistory.getTINfo().getTypeOfTransaction());
						receipt.printf("$%-8.2f", soloHistory.getTINfo().getAmountOfTransaction());
						receipt.printf("\t%4b", soloHistory.getFlag());
						receipt.printf("\t%-7.2f ", soloHistory.getPostBalance());
						
						String reasonTransaction = soloHistory.getReason();
						receipt.printf("\t%-7s", reasonTransaction);
						receipt.println();
				
					}
					receipt.println();
					
				}
			}
			receipt.println();
		}
	}
	
	/*
	 The input is a bank class object, a printwriter, and a scanner object.
	 The process is that the a transactionticket ticket is made and sent to the bank object to continue the transaction,
	 	and get a transaction receipt object.
	 The output is to the output file and depends on the transaction receipt object.
	 */
	public static void closeAccount(Bank bank, PrintWriter receipt, Scanner input) {
		int account = input.nextInt();
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "Close Account", 0.0, 0);
		TransactionReceipt closeReceipt = bank.closeAccount(ticket);
		
		boolean results = closeReceipt.getFlag();
		String reason = closeReceipt.getReason();
		
		if(results == false) {
			 if (reason.contains("does not exist")) { 
				 receipt.println("Transaction Requested: Close Account");
			     receipt.println("Error: " + reason);
			 }
			 else {
				 receipt.println("Transaction Requested: Close Account");
				 receipt.println("Account Number: " + account);
				 receipt.println("Error: " + reason);
			 }
		}else {
			receipt.println("Transaction Requested: Close Account");
			receipt.println("Account Number: " + account);
			receipt.println("Your account has been closed, transactions will not go through unless it is reopened");
		} 
		receipt.println();
	}
	
	/*
	 The input is a bank class object, a printwriter, and a scanner object.
	 The process is that the a transactionticket ticket is made and sent to the bank object to continue the transaction,
	 	and get a transaction receipt object.
	 The output is to the output file and depends on the transaction receipt object.
	  */
	public static void reOpenAccount(Bank bank, PrintWriter receipt, Scanner input) {
		int account = input.nextInt();
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "ReOpen Account", 0.0, 0);
		TransactionReceipt reOpenReceipt = bank.reOpenAccount(ticket);
		
		boolean results = reOpenReceipt.getFlag();
		String reason = reOpenReceipt.getReason();
		
		if(results == false) {
			 if (reason.contains("does not exist")) { 
				 receipt.println("Transaction Requested: ReOpen Account");
			     receipt.println("Error: " + reason);
			 }else {
				 receipt.println("Transaction Requested: ReOpen Account");
				 receipt.println("Account: " + account);
			     receipt.println("Error: " + reason);
			 }
			 
		}else {
			receipt.println("Transaction Requested: ReOpen Account");
			receipt.println("Account Number: ");
			receipt.println("Your account has been Reopened, transactions will now go through again");
		} 
		receipt.println();
	}
	
	/*
	 The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the account requested for the check, then a transcation tikcet
	 	is made to send to the bank class to it's check method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void clearCheck(Bank bank, PrintWriter receipt, Scanner input) {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		String date = input.next();
		
		Calendar present = Calendar.getInstance();
		Check check = new Check(account, amount, date);
		TransactionTicket ticket = new TransactionTicket(account, present, "check", amount, 0);
		TransactionReceipt returnedCheck = bank.clearCheck(check, ticket);
		
		boolean review = returnedCheck.getFlag();
		receipt.println(present.getTime());
		if (review == false) {
			String reason = returnedCheck.getReason();
			if (reason.contains("does not exist:")) {
				receipt.println("Transaction Requested: Withdraw");
		        receipt.println("Error: " + reason);
			}else if(reason.contains("cannot be accpeted to clear a check")) {
				receipt.println("Transaction Requested: Clear Check");
				receipt.println("Account number: " + check.getAcctNum());
				receipt.println("Account type: " + returnedCheck.getType());
				receipt.println("Error: Account type cannot be accpeted to clear a check");
			}else if(reason.contains("Insufficient funds available")) {
				receipt.println("Transaction Requested: Clear Check");
				receipt.println("Account number: " + check.getAcctNum());
				receipt.println("Account type: " + returnedCheck.getType());
				
				receipt.printf("Old Balance: $%.2f", returnedCheck.getPreBalance());
				receipt.println();
				
				receipt.printf("Amount of Check: $%.2f", check.getCheckAmount());
				receipt.println();
				
				receipt.printf("New Balance: $%.2f", returnedCheck.getPostBalance());
				receipt.println();
				
				receipt.println("Error: Insufficient Funds Available - Bounce Fee ($2.50) Charged");
			}else if(reason.contains("not reached yet")) {
				receipt.println("Transaction Requested: Clear Check");
				receipt.println("Account number: " + check.getAcctNum());
				receipt.println("Account type: " + returnedCheck.getType());
				
				receipt.printf("Old Balance: $%.2f", returnedCheck.getPreBalance());
				receipt.println();
				
				receipt.printf("Amount of Check: $%.2f", check.getCheckAmount());
				receipt.println();
				
				receipt.printf("New Balance: $%.2f", returnedCheck.getPostBalance());
				receipt.println();
				
				receipt.println("Error: " + reason);
			}else if(reason.contains("is Closed")){
				receipt.println("Transaction Requested: Clear Check");
		        receipt.println("Account Number: " + account);
			    receipt.println("Error: " + reason);
			    receipt.println();
			}else {
				receipt.println("Transaction Requested: Clear Check");
				receipt.println("Account number: " + check.getAcctNum());
				receipt.println("Account type: " + returnedCheck.getType());
				
				receipt.printf("Old Balance: $%.2f", returnedCheck.getPreBalance());
				receipt.println();
				
				receipt.printf("Amount of Check: $%.2f", check.getCheckAmount());
				receipt.println();
				
				receipt.printf("New Balance: $%.2f", returnedCheck.getPostBalance());
				receipt.println();
				
				receipt.println("Error: " + reason);
			}
		}else {
			receipt.println("Transaction Requested: Clear Check");
			receipt.println("Account number: " + check.getAcctNum());
			receipt.println("Account type: " + returnedCheck.getType());
			
			receipt.printf("Old Balance: $%.2f", returnedCheck.getPreBalance());
			receipt.println();
			
			receipt.printf("Amount of Check: $%.2f", check.getCheckAmount());
			receipt.println();
			
			receipt.printf("New Balance: $%.2f", returnedCheck.getPostBalance());
			receipt.println();
			

		}
		receipt.println();
	}
	
	/*
	The input is a bank object, and printwriter and scanner objects
	 The process is that the test cases file is read for the information for the new account, then a transcation tikcet
	 	is made to send to the bank class to it's new account method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void newAcct(Bank bank, PrintWriter receipt, Scanner input, int numAccts) {
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
		int term;
		
		if (type.equals("CD")) {
			System.out.println("\tEnter your intial Deposit: ");
			balance = input.nextDouble();	
			System.out.println("\tenter your wanted term length: ");
			term = input.nextInt();
		}else {
			term = 0;
		}
		
		Calendar present = Calendar.getInstance();
		Name name = new Name(first, last);
		Depositor depositor = new Depositor(ssn, name);
		TransactionTicket ticket = new TransactionTicket(account, present, "New Account", balance, term);
		TransactionReceipt getBack = bank.newAcct(ticket, depositor, type);
		
		boolean success = getBack.getFlag();
		String reason = getBack.getReason();	
		receipt.println(present.getTime());
		
		if (success == false) {
			if (reason.contains("already exists")) {
				receipt.println("Transaction Requested: New Account");
				receipt.println("Your Last Name: " + last);
				receipt.println("Your First Name: " + first);
		        receipt.println("Your SSN: " + ssn);
		        receipt.println("Proposed Account Number: " + account);
		        receipt.println("Error: " + reason );
			}else if(reason.contains("opening balance")) {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				System.out.println("\tEnter your opening CD balance");
				
				receipt.println("Transaction Requested: New Account");
				receipt.println("Your Last Name: " + last);
				receipt.println("Your First Name: " + first);
				receipt.println("Your SSN: " + ssn);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Opening balance: " + balance);
				receipt.println("Error: " + reason);
			}else if(reason.contains("CD accounts must a maturity date that is either")) {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				System.out.println("\tEnter your opening CD balance");
				System.out.println("\tEnter your opening CD term(6, 12, 8, or 24)");

				receipt.println("Transaction Requested: New Account");
				receipt.println("Your Last Name: " + last);
				receipt.println("Your First Name: " + first);
				receipt.println("Your SSN: " + ssn);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Opening balance: " + balance);
				receipt.println("Error: " + reason);
			}else if(reason.contains("not 9 digits long")) {
				receipt.println("Transaction Requested: New Account");
				receipt.println("Your Last Name: " + last);
				receipt.println("Your First Name: " + first);
				receipt.println("Your SSN: " + ssn);
				receipt.println("Error: " + reason);
			}else {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				
				receipt.println("Transaction Requested: New Account");
				receipt.println("Your Last Name: " + last);
				receipt.println("Your First Name: " + first);
				receipt.println("Your SSN: " + ssn);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Error: " + reason);
			}
		}else {
			System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
			if (type.equals("CD")) {
				System.out.println("\tEnter your opening CD balance");
				System.out.println("\tEnter your opening CD term(6, 12, 8, or 24)");

				receipt.println("Transaction Requested: New Account");
				receipt.println("Your Last Name: " + last);
				receipt.println("Your First Name: " + first);
				receipt.println("Your SSN: " + ssn);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Opening balance: " + balance);
				
				Calendar date = getBack.getPostMDate();
				int month = date.get(Calendar.MONTH) + 1;
				int day = date.get(Calendar.DAY_OF_MONTH);
				int year = date.get(Calendar.YEAR);
				String writtenDate = month + "/" + day + "/" + year;
				
				receipt.println("Maturity Date: " + writtenDate);
				receipt.println("Congratulations! Your account " + account + " has been made, welcome to our Bank!");
			
				
			}else {
				receipt.println("Transaction Requested: New Account");
				receipt.println("Your Last Name: " + last);
				receipt.println("Your First Name: " + first);
				receipt.println("Your SSN: " + ssn);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Congratulations! Your account " + account + " has been made, welcome to our Bank!");
			}
			
		}
	
		receipt.println();
	}
	
}