import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
public class HW02 {

	public static void main(String[] args) throws FileNotFoundException{
		String a = "testCases.txt";
		File tc = new File(a);
		Scanner input = new Scanner(tc);
		
		String b = "output.txt";
		File po = new File(b);
		PrintWriter recipt = new PrintWriter(po);
		
		final int maxNumAccts = 100;
		BankAccount[] accounts = new BankAccount[maxNumAccts];
		int numAccts = readAccts(accounts);
		
		printAccts(accounts, numAccts, recipt);
		boolean repeat = true;
		do {
			menu();
			char option = input.next().charAt(0);
			switch(option){
				case 'q':
				case 'Q':
					printAccts(accounts, numAccts, recipt);
					System.out.println("\tQuitting Bank Program: ");
					repeat = false;
					break;
				case 'w':
				case 'W':
					withdrawal(accounts, numAccts, recipt, input);
					break;
				case 'd':
				case 'D':
					deposit(accounts, numAccts, recipt, input);
					break;
				case 'n':
				case 'N':
					numAccts = newAcct(accounts, numAccts, recipt, input);
					break;
				case 'i':
				case 'I':
					accountInfo(accounts, numAccts, recipt, input);
					break;
				case 'b':
				case 'B':
					balance(accounts, numAccts, recipt, input);
					break;
				case 'x':
				case 'X':
					numAccts = deleteAcct(accounts, numAccts, recipt, input);
					break;
				default:
	                recipt.println("Error: " + option + " is an invalid selection -  try again");
	                recipt.println();
	                break;
			}
		} 
		while (repeat);
		
		input.close();
		recipt.close();
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
	    System.out.println("\t     N(n) -- New Account");
	    System.out.println("\t     B(b) -- Balance Inquiry");
	    System.out.println("\t     I(i) -- Account Info");
	    System.out.println("\t     X(x) -- Delete Account");
	    System.out.println("\t     Q(q) -- Quit");
	    System.out.println();
	    System.out.println("\tEnter your selection: ");
	}
	
	/* 
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	this also includes the Depositor class which contains ssn, and the name class.
	 The process is that the a scanner is used to take the account information from
	 	text file accounts.txt and put it into the BankAccount class array.
	 The output is the actual amount accounts in the BankAccount array, decided by 
	 	a incremented variable in a loop that goes up untiil there is no more text file input
	  */
	public static int readAccts(BankAccount[] accounts) throws FileNotFoundException{
		String a = "accounts.txt";
		File file = new File(a);
		Scanner read = new Scanner(file);
		int x = 0;
		
		while (x < accounts.length && read.hasNext()) {
			BankAccount mba = new BankAccount();
			Depositer md = new Depositer();
			Name mn = new Name();
			
			mn.setLast(read.next());
			mn.setFirst(read.next());
			md.setSSN(read.next());
			mba.setAcctNumber(read.nextInt());
			mba.setType(read.next());
			mba.setBalance(read.nextDouble());
			
			md.setName(mn);
			mba.setDepositer(md);
			accounts[x] = mba;
			x++;
		}
		
		read.close();
		return x;
	}
	/*
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	the actual number of accounts, and the requested account to locate.
	 The method searches through the BankAccount array to find the account number.
	 The output is -1 if no such account number is located, but if it is located,
	 	the index where the account# lies in the BankAccount array is returned.
	 */
	public static int findAcct(BankAccount[] accounts, int numAccts, int requestedAccount) {
		for (int x  = 0; x < numAccts; x++) if (accounts[x].getAcctNumber() == requestedAccount) return x;
		
		return -1;
	}
	/*
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	the actual number of accounts, and the PrintWriter that leads to the output file.
	 The process is that the information from each account in the BankAccount array is printed to the output file by using the getters.
	 There is no other output.
	 */
	public static void printAccts(BankAccount[] account , int numAccts, PrintWriter recipt){
		recipt.print("\t\tCurrent Bank Accounts");
		recipt.println();
		recipt.print("Last Name:");
		recipt.print("\tFirst Name:");
		recipt.printf("\tSSN:");
		recipt.printf("\t   Account Number:");
		recipt.printf("\t\tAccount Type:");
		recipt.printf("\tBalance:");
		recipt.println();		
		for (int index = 0; index < numAccts; index++) {
			String first = account[index].getDepositer().getName().getFirst();
			String last = account[index].getDepositer().getName().getLast();
			String ssn = account[index].getDepositer().getSSN();
			int accountNum = account[index].getAcctNumber();
			String type = account[index].getType();
			double balance = account[index].getBalance();
			
			recipt.printf("%-12s", last);
			recipt.printf("%-12s", first);
			recipt.printf("%-9s", ssn);
			recipt.printf("%8d ", accountNum);
			recipt.printf("\t\t\t\t%-9s", type);
			recipt.printf("\t\t$%8.2f", balance);
			recipt.println();
			
		}
		recipt.println();
	}
	
	/*
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	the actual number of accounts, the PrintWriter that leads to the output file, and 
	 	the scanner for the test cases file.
	 The process is that the method reads test cases to get the account provided then find it using findAcct, 
	 	if not found in the if then a error message is displayed to output text file, 
	 	next it determines if the withdraw amount is above 0 and or less than of equal to the amount in the account,
	 	if everything is good from there the action is accepted and the confirmation is printed to the output file
	 The output is the information printed to the output file.
	 */
	public static void withdrawal(BankAccount[] accounts, int numAccts, PrintWriter recipt, Scanner input){
		int account = input.nextInt();
		int index = findAcct(accounts, numAccts, account);
		if (index == -1) {
			recipt.println("Transaction Requested: Withdraw");
	        recipt.println("Error: Account number " + account + " does not exist");
	        recipt.println();

		}else {
			System.out.println("\tEnter amount to Withdraw: ");
			double withdrawAmount = input.nextDouble();

			if(withdrawAmount <= 0. || withdrawAmount > accounts[index].getBalance() ) {
				recipt.println("Transaction Requested: Withdraw");
		        recipt.println("Account Number: " + account);
		        String a = accounts[index].getType();
		        recipt.println("Account type: " + a);
		        recipt.printf("Old Balance: $%.2f", accounts[index].getBalance());
		        recipt.println();	            
	            recipt.printf("Amount to Withdraw: $%.2f", withdrawAmount);
	            recipt.println();
		        recipt.printf("Error: $%.2f is an invalid amount", withdrawAmount);
			    recipt.println();
			    recipt.println();
			}else {
				recipt.println("Transaction Requested: Withdraw");
			    recipt.println("Account Number: " + account);
			    String a = accounts[index].getType();
		        recipt.println("Account type: " + a);
		        recipt.printf("Old Balance: $%.2f", accounts[index].getBalance());
				recipt.println();
				recipt.printf("Amount to Withdraw: $%.2f", withdrawAmount);
	            recipt.println();
		        double withdraw = accounts[index].getBalance() - withdrawAmount;
		        accounts[index].setBalance(withdraw);
		        recipt.printf("New Balance: $%.2f", accounts[index].getBalance());
				recipt.println();
				recipt.println();
			}
		}
		
	}
	/*
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	the actual number of accounts, the PrintWriter that leads to the output file, and 
	 	the scanner for the test cases file.
	 The proccess is that method uses the arrays to activate findAcct, then it uses the index to locate the account 
	 	and its balances in their array, after that it uses if and else's to check whether the account exists, 
	 	and has a accepted deposit amount.
	 The output is the information printed to the output file.
	 */
	public static void deposit(BankAccount[] accounts, int numAccts, PrintWriter recipt, Scanner input){
		int account = input.nextInt();
		int index = findAcct(accounts, numAccts, account);		
		if (index == -1) {
			recipt.println("Transaction Requested: Deposit");
	        recipt.println("Error: Account number " + account + " does not exist");
		}else {
			System.out.println("\tEnter amount to deposit: ");
		    double depositAmount = input.nextDouble();
			if (depositAmount <= 0.00) {
				recipt.println("Transaction Requested: Deposit");
		        recipt.println("Account Number: " + account);
		        String a = accounts[index].getType();
		        recipt.println("Account type: " + a);
		        recipt.printf("Amount to Withdraw: $%.2f", depositAmount);
	            recipt.println();	            
		        recipt.printf("Error: $%.2f is an invalid amount", depositAmount);
			    recipt.println();
			}else {
				recipt.println("Transaction Requested: Deposit");
			    recipt.println("Account Number: " + account);
			    String a = accounts[index].getType();
		        recipt.println("Account type: " + a);
		        recipt.printf("Old Balance: $%.2f", accounts[index].getBalance());
				recipt.println();
				recipt.printf("Amount to Withdraw: $%.2f", depositAmount);
	            recipt.println();		        
		        double deposit = accounts[index].getBalance() + depositAmount;
		        accounts[index].setBalance(deposit);			
		        recipt.printf("New Balance: $%.2f", accounts[index].getBalance());
				recipt.println(); 
			}
		}
		recipt.println();
	}
	/*
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	the actual number of accounts, the PrintWriter that leads to the output file, and 
	 	the scanner for the test cases file.
	 The process if that the method uses findAcct to determine whether if the account provided exists already, 
	 	if not then a new account is added to the array and is filled with the corresponding information in test cases.
	 The output is the information to the output text file and the amount of accounts is increased by 1.
	 */
	public static int newAcct(BankAccount[] accounts, int numAccts, PrintWriter recipt, Scanner input){
		System.out.println("\tEnter your Last Name: ");
		String last = input.next();
		System.out.println("\tEnter your First Name: ");
		String first = input.next();
		System.out.println("\tEnter your SSN: ");
		String ssn = input.next();
		String check = ssn + "";
		if (check.length() != 9) {
			recipt.println("Transaction Requested: New Account");
			recipt.println("Your Last Name: " + last);
			recipt.println("Your First Name: " + first);
	        recipt.println("Your SSN: " + ssn);
	        recipt.println("Error: " + ssn + " cannot be accepted as it is not 9 digits long, please enter a valid SSN");
	        recipt.println();
		}else {
			System.out.println("\tEnter your proposed account number: ");
			int acct = input.nextInt();
			String check1 = "" + acct;
			if (check1.length() != 6){
				recipt.println("Transaction Requested: New Account");
				recipt.println("Your Last Name: " + last);
				recipt.println("Your First Name: " + first);
		        recipt.println("Your SSN: " + ssn);
		        recipt.println("Proposed Account Number: " + acct);
		        recipt.println("Error: Proposed Account " + acct + " cannot be accepted");
		        recipt.println();
			}
			else {
				int index = findAcct(accounts, numAccts, acct);
				if (index != -1){
					recipt.println("Transaction Requested: New Account");
					recipt.println("Your Last Name: " + last);
					recipt.println("Your First Name: " + first);
			        recipt.println("Your SSN: " + ssn);
			        recipt.println("Proposed Account Number: " + acct);
			        recipt.println("Error: Account " + acct + " already exists");
			        recipt.println();
				}else {
					System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
					String type = input.next();
					String a = "Checking";
					String b = "Savings";
					String c = "CD";
					if ( a.equals(type) || b.equals(type) || c.equals(type) ) {
						if (c.equals(type)) {
							System.out.println("\tEnter an amount for your opening balance: ");
							double cdOpening = input.nextDouble();
							if ( !(cdOpening >= 0) ) recipt.println("Error: " + cdOpening + " cannot be accpeted");
							else {
								recipt.println("Transaction Requested: New Account");
								recipt.println("Your Last Name: " + last);
								recipt.println("Your First Name: " + first);
								recipt.println("Your SSN: " + ssn);
								recipt.println("Proposed Account Number: " + acct);
								recipt.println("Account type: " + type);
								recipt.printf("Opnening Balance: $%.2f", cdOpening);
								recipt.println();
								recipt.println("Congratulations! Your account " + acct + " has been made, welcome to our Bank!");
								recipt.println();
					        
								BankAccount mba = new BankAccount();
								Depositer md = new Depositer();
					        	Name mn = new Name();
							
					        	mn.setLast(last);
					        	mn.setFirst(first);
					        	md.setSSN(ssn);
					        	mba.setAcctNumber(acct);
					        	mba.setType(type);
					        	mba.setBalance(cdOpening);
							
					        	md.setName(mn);
								mba.setDepositer(md);
								accounts[numAccts] = mba;
								numAccts++;
							return numAccts;
							}
						}
						else {
							recipt.println("Transaction Requested: New Account");
							recipt.println("Your Last Name: " + last);
							recipt.println("Your First Name: " + first);
							recipt.println("Your SSN: " + ssn);
							recipt.println("Proposed Account Number: " + acct);
							recipt.println("Account type: " + type);
							recipt.println("Congratulations! Your account " + acct + " has been made, welcome to our Bank!");
							recipt.println();
				        
							BankAccount mba = new BankAccount();
							Depositer md = new Depositer();
				        	Name mn = new Name();
						
				        	mn.setLast(last);
				        	mn.setFirst(first);
				        	md.setSSN(ssn);
				        	mba.setAcctNumber(acct);
				        	mba.setType(type);
				        	mba.setBalance(0.00);
						
				        	md.setName(mn);
							mba.setDepositer(md);
							accounts[numAccts] = mba;
							numAccts++;
						return numAccts;
						}
					}else {
						recipt.println("Transaction Requested: New Account");
						recipt.println("Your Last Name: " + last);
						recipt.println("Your First Name: " + first);
				        recipt.println("Your SSN: " + ssn);
				        recipt.println("Proposed Account Number: " + acct);
				        recipt.println("Account type: " + type);
				        recipt.println("Error: " + type + " is not a valid account type, "
				        		+ "please select from the valid options: (Checking, Savings, or CD)");
				        recipt.println();
					}
				}
					
			}

		}
		return numAccts;
	}
	/*
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	the actual number of accounts, the PrintWriter that leads to the output file, and 
	 	the scanner for the test cases file.
	 The proccess is, the method uses the scanner to find the account requested, it then uses findAcct to find the 
	 	account and if and else's to determine if it exists to display the proper message to pgmoutput.txt
	 The output is the info to the output file.
	 */
	public static void balance(BankAccount[] accounts, int numAccts, PrintWriter recipt, Scanner input){
		System.out.println("\tEnter the account number: ");
		int account = input.nextInt();
		int index = findAcct(accounts, numAccts, account);
		if (index == -1){
			recipt.println("Transaction Requested: Balance Inquiry");
		    recipt.println("Error: Account number " + account + " does not exist");
	        recipt.println();

		}else {
			recipt.println("Transaction Requested: Balance Inquiry");
		    recipt.println("Account Number: " + account);
		    String a = accounts[index].getType();
	        recipt.println("Account type: " + a);
		    recipt.printf("Current Balance: $%.2f", accounts[index].getBalance());
			recipt.println();
			recipt.println();
		}
	}
	/*
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	the actual number of accounts, the PrintWriter that leads to the output file, and 
	 	the scanner for the test cases file.
	 The process is that the test cases file is scanned for the ssn of a user, then it used to locate
	 	all accounts with the ssn and if and elses are used to display the proper messages
	 	to the output file.
	 The output is all info in the output file.
	 */
	public static void accountInfo(BankAccount[] account, int numAccts, PrintWriter recipt, Scanner input) {
		System.out.println("\tEnter your Social Security Number: ");
		String ssn = input.next();
		int yes = 0;
		for (int x = 0; x < numAccts; x++) {
			String check = account[x].getDepositer().getSSN();
			if (check.equals(ssn))yes++;
		}
		if (yes == 0) {
			recipt.println("Transaction Requested: Account Info");
			recipt.println("Error: SSN " + ssn +" does not exist in our database: ");
			recipt.println();
		}else {
			recipt.println("Transaction Requested: Account Info");
			recipt.println("SSN: " + ssn);
			recipt.print("Last Name:");
			recipt.print("\tFirst Name:");
			recipt.printf("\tSSN:");
			recipt.printf("\t   Account Number:");
			recipt.printf("\t\tAccount Type:");
			recipt.printf("\tBalance:");
			recipt.println();	
			for (int x = 0; x < numAccts; x++) {
				String check = account[x].getDepositer().getSSN();
				if (check.equals(ssn)) {
					String first = account[x].getDepositer().getName().getFirst();
					String last = account[x].getDepositer().getName().getLast();
					int accountNum = account[x].getAcctNumber();
					String type = account[x].getType();
					double balance = account[x].getBalance();
					
					recipt.printf("%-12s", last);
					recipt.printf("%-12s", first);
					recipt.printf("%-9s", ssn);
					recipt.printf("%8d ", accountNum);
					recipt.printf("\t\t\t\t%-9s", type);
					recipt.printf("\t\t$%8.2f", balance);
					recipt.println();		
				}
			}
			recipt.println();
		}
	}
	/*
	 The Input is an array from BankAccount class which contains the information of a user, 
	 	the actual number of accounts, the PrintWriter that leads to the output file, and 
	 	the scanner for the test cases file.
	 The process is the method first locates the account and checks if the balance is 0, if it is then it deletes the account
	 	after the account is deleted the BankAccount array is adjusted for the accounts deletion
	 The output is the information to the output text file and the amount of accounts is decreased by 1.	 
	 */
	public static int deleteAcct(BankAccount[] accounts, int numAccts, PrintWriter recipt, Scanner input){
		System.out.println("\tEnter an account to delete: ");
		int deleteAcct = input.nextInt();
		int index = findAcct(accounts, numAccts, deleteAcct);
		if (index == -1) {
			recipt.println("Transaction Requested: Delete Account");
			recipt.println("Account Number: " + deleteAcct);
		    recipt.println("Error: " + deleteAcct + " does not exist");
		    recipt.println();
		}
		else {
			if (accounts[index].getBalance() != 0.00) {
				recipt.println("Transaction Requested: Delete Account");
				recipt.println("Account Number: " + deleteAcct);
				recipt.println("Account type: " + accounts[index].getType());
				recipt.println("Error: Account " + deleteAcct + " does not have a balance of zero");
				recipt.println();
			}else {
				recipt.println("Transaction Requested: Delete Account");
				recipt.println("Account Number: " + deleteAcct);
				recipt.println("Account type: " + accounts[index].getType());
				recipt.println("Your account has been deleted, sorry to see you go!");
				recipt.println();
				for (int x = index; x < numAccts; x++) {
					accounts[x] = accounts[x + 1];
				}
				numAccts--;
			}
		}
		return numAccts;
	}

}

