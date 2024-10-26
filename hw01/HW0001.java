import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
public class HW0001 {

	public static void main(String[] args) throws FileNotFoundException{
		int maxNum = 100;
		int[] acctNum = new int[maxNum];
		double[] balance = new double[maxNum];
		
		String a = "TestCases.txt";
		File tc = new File(a);
		Scanner input = new Scanner(tc);
		
		String b = "pgmOutput.txt";
		File po = new File(b);
		PrintWriter recipt = new PrintWriter(po);
		
		int numAccts = readAccts(acctNum, balance);
		
		printAccts(acctNum,balance, numAccts,recipt);
		
		boolean repeat = true;
		do {
			menu();
			char option = input.next().charAt(0);
			switch(option){
				case 'q':
				case 'Q':
					printAccts(acctNum,balance, numAccts,recipt);
					repeat = false;
					break;
				case 'w':
				case 'W':
					withdrawal(acctNum, balance, numAccts, recipt, input);
					break;
				case 'd':
				case 'D':
					deposit(acctNum, balance, numAccts, recipt, input);
					break;
				case 'n':
				case 'N':
					numAccts =  newAcct(acctNum, balance, numAccts, recipt, input);
					break;
				case 'b':
				case 'B':
					balance(acctNum, balance, numAccts, recipt, input);
					break;
				case 'x':
				case 'X':
					numAccts = deleteAcct(acctNum, balance, numAccts, recipt, input);
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
	
	//Input is nothing
	//The method prints messages out to the console
	//The output is a series of prompts that tells the user all the choices they can makes in the program
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
	    System.out.println("\t     X(x) -- Delete Account");
	    System.out.println("\t     Q(q) -- Quit");
	    System.out.println();
	    System.out.println("\tEnter your selection: ");
	}

	//Input: The method takes in the array that will hold all the account numbers and the one that holds the corresponding balances
	//Process: The method opens a file and scanner to read the initAccounts.txt and add the account number and balance to their respective array, it uses a for loop to stop when there is no more text
	//Output: The method returns the amount of accounts in the arrays there actually are since the array is set a high number for usage purposes
	public static int readAccts(int[] acctNum, double[] balance) throws FileNotFoundException{
		String a = "initAccounts.txt";
		File file = new File(a);
		Scanner read = new Scanner(file);
		int x = 0;
		
		while (read.hasNext() && x < acctNum.length){
			acctNum[x] = read.nextInt();
			balance[x] = read.nextDouble();
			x++;
		}
		read.close();
		
		return x;
	}
	
	//Input: The method takes in the array that holds the current account numbers, the number of accounts actually in the array, and the account to search
	//Process: It uses the account number provided to find it in the array
	//Output: If the method cannot find the account in the array of current accounts, then it returns a negative 1, if finds it returns the value of the array where it is present
	public static int findAcct(int[] acctNum, int numAccts, int account) {
		for (int x = 0; x < numAccts; x++) {
			if (acctNum[x] == account) return x;
		}
		return -1;
	}
	
	/*Input: The method takes in the arrays for account numbers and there balances, a Printwriter that leads to pgmOutput.txt
	and a scanner for test cases */
	/*Process: The method uses the arrays to activate findAcct, then it uses the index to locate the account and its balances in their arrays,
		after that it uses if and else's to check whether the account exists, and has a accepted deposit amount*/
	/*Output: Although there is no output to the method itself it outputs the transactions made to the pgmOutput*/
	public static void deposit(int[] acctNum, double[] balance, int numAccts,PrintWriter recipt, Scanner input){
		int account = input.nextInt();
		int index = findAcct(acctNum, numAccts, account);		
		if (index == -1) {
			recipt.println("Transaction Requested: Deposit");
	        recipt.println("Error: Account number " + account + " does not exist");
		}else {
			System.out.println("\tEnter amount to deposit: ");
		    double depositAmount = input.nextDouble();
			if (depositAmount <= 0.00) {
				recipt.println("Transaction Requested: Deposit");
		        recipt.println("Account Number: " + account);
	            recipt.println("Amount to Deposit: $" + depositAmount);
		        recipt.printf("Error: $%.2f is an invalid amount", depositAmount);
			    recipt.println();
			}else {
				recipt.println("Transaction Requested: Deposit");
			    recipt.println("Account Number: " + account);
		        recipt.printf("Old Balance: $%.2f", balance[index]);
				recipt.println();
		        recipt.println("Amount to Deposit: $" + depositAmount);
		        balance[index] += depositAmount;			
		        recipt.printf("New Balance: $%.2f", balance[index]);
				recipt.println(); 
			}
		}
		recipt.println();
	}
	
	/*Input: The method takes in the arrays of accounts and their balances, as well as the Printwriter for the pgmoutput.txt*/
	/*Process: The method uses printlns to print out the accounts that are contained in the array as well as their 
		balances when you quit the program*/
	/*Output: The program outputs the database of all current accounts and balances to pgmoutput.txt*/
	public static void printAccts(int[] acctNum, double[] balance, int numAccts,PrintWriter recipt){
		recipt.println("\t\tCurrent Bank Accounts");
		recipt.println();
		recipt.println("Account Balance");
		for (int index = 0; index < numAccts; index++) {
			recipt.printf("%7d  $%7.2f", acctNum[index], balance[index]);
			recipt.println();
		}
		recipt.println();
	}
	
	/*Input: The account takes in the arrays of account and their balances, the actual amount of accounts, 
	 a printwriter to the pgmoutput.txt, and the scanner to test cases*/
	/*Process: The method uses the scanner to find the account requests for a balances, it then uses findAcct to find the 
	 account and if and else's to determine if it exists to display the proper message to pgmoutput.txt*/
	/*Output: pgmoutput.txt is printed the transaction, the words depending on it if succeeded or if there was an error*/	
	public static void balance(int[] acctNum, double[] balance, int numAccts, PrintWriter recipt, Scanner input){
		System.out.println("\tEnter the account number: ");
		int account = input.nextInt();
		int index = findAcct(acctNum, numAccts, account);
		if (index == -1){
			recipt.println("Transaction Requested: Balance Inquiry");
		    recipt.println("Error: Account number " + account + " does not exist");
	        recipt.println();

		}else {
			recipt.println("Transaction Requested: Balance Inquiry");
		    recipt.println("Account Number: " + account);
		    recipt.printf("Current Balance: $%.2f", balance[index]);
			recipt.println();
			recipt.println();
		}
	}
	
	/*Input: the method takes in the arrays of account numbers and balances, printwriter to pgmoutput.txt, and the scanner to test cases*/
	/*Process: the method reads test cases to find the account in the array of accounts, if not found in the if then a error message is displayed to 
	 the printwriter, next it determines if the withdraw amount is above 0 and or less than of equal to the amount in the account*/
	/*Output: It prints to pgmoutput.txt the  or the error message*/	
	public static void withdrawal(int[] acctNum, double[] balance, int numAccts, PrintWriter recipt, Scanner input){
		int account = input.nextInt();
		int index = findAcct(acctNum, numAccts, account);
		if (index == -1) {
			recipt.println("Transaction Requested: Withdraw");
	        recipt.println("Error: Account number " + account + " does not exist");
	        recipt.println();

		}else {
			System.out.println("\tEnter amount to Withdraw: ");
			double withdrawAmount = input.nextDouble();

			if(withdrawAmount <= 0. || withdrawAmount > balance[index]) {
				recipt.println("Transaction Requested: Withdraw");
		        recipt.println("Account Number: " + account);
		        recipt.printf("Old Balance: $%.2f", balance[index]);
		        recipt.println();
	            recipt.println("Amount to Withdraw: $" + withdrawAmount);
		        recipt.printf("Error: $%.2f is an invalid amount", withdrawAmount);
			    recipt.println();
			    recipt.println();
			}else {
				recipt.println("Transaction Requested: Withdraw");
			    recipt.println("Account Number: " + account);
		        recipt.printf("Old Balance: $%.2f", balance[index]);
				recipt.println();
		        recipt.println("Amount to Withdraw: $" + withdrawAmount);
		        balance[index] = balance[index] - withdrawAmount;			
		        recipt.printf("New Balance: $%.2f", balance[index]);
				recipt.println();
				recipt.println();
			}
		}
		
	}
	
	/*Input: This method takes in the arrays, the number of actual accounts(numAccts), 
		the printwriter for pgmoutput.txt, and the scanner for test cases*/
	/*Process: The method uses findAcct to determine whether if the account provided exists already, 
	 	if not then a new account is added to the int array and numAccts goes up by one*/
	/*Output: The output sends the value of numAccts, if there were no errors then it should be 1 higher, otherwise the same*/
	public static int newAcct(int[] acctNum, double[] balance, int numAccts, PrintWriter recipt, Scanner input){
		System.out.println("\tEnter your proposed account number: ");
		int check = input.nextInt();
		String checkTwo = "" + check;
		if (checkTwo.length() != 6){
			recipt.println("Transaction Requested: New Account");
	        recipt.println("Proposed Account Number: " + check);
	        recipt.println("Error: Proposed Account " + check + " cannot be accepted");
	        recipt.println();
		}
		else {
			int index = findAcct(acctNum, numAccts,check);
			if (index != -1){
				recipt.println("Transaction Requested: New Account");
		        recipt.println("Proposed Account Number: " + check);
		        recipt.println("Error: Account " + check + " already exists");
		        recipt.println();
			}else {
				recipt.println("Transaction Requested: New Account");
		        recipt.println("Proposed Account Number: " + check);
		        recipt.println("Congratulations! Your account " + check + " has been made, welcome to our Bank!");
		        recipt.println();
		        acctNum[numAccts] = check;
		        numAccts++;
			}
		}
		return numAccts;
	}
	
	/*Input: the method takes in the account arrays, the number of actual accounts, the printwriter for pgmoutput.txt
	 	and the scanner for testcases.txt*/
	/*Process: The method first locates the account and if it it does cheks if the balance is 0, if it is then it deletes the account
	 	after the account is deleted the arrays are adjusted for the accounts deletion and are moved back a space depending
	 	on the account deleted*/
	/*Output: The method outputs the adjusted numAccts, it changes to minus 1 if it worked, if there was an error it's the same*/
	public static int deleteAcct(int[] acctNum, double[] balance, int numAccts, PrintWriter recipt, Scanner input){
		System.out.println("\tEnter an account to delete: ");
		int deleteAcct = input.nextInt();
		int index = findAcct(acctNum, numAccts, deleteAcct);
		if (index == -1) {
			recipt.println("Transaction Requested: Delete Account");
			recipt.println("Account Number: " + deleteAcct);
		    recipt.println("Error: " + deleteAcct + " does not exist");
		    recipt.println();
		}
		else {
			if (balance[index] != 0) {
				recipt.println("Transaction Requested: Delete Account");
				recipt.println("Account Number: " + deleteAcct);
				recipt.println("Error: Account " + deleteAcct + " does not have a balance of zero");
				recipt.println();
			}else {
				recipt.println("Transaction Requested: Delete Account");
				recipt.println("Account Number: " + deleteAcct);
				recipt.println("Your account has been deleted, sorry to see you go!");
				recipt.println();
				for (int x = index; x < numAccts; x++) {
					acctNum[x] = acctNum[x + 1];
					balance[x] = balance[x + 1];
				}
				numAccts--;
			}
		}
		return numAccts;
	}
}	