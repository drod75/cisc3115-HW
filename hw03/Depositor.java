/*
 The Name class contains the users ssn and full name from the name class, as well as getters, no argument constructors, and parameterized
 constructors
 */
public class Depositor {
	private String ssn;
	private Name fullName;
	
	//No-Arg Constructor
	public Depositor (){
		ssn = "";
		fullName = new Name();
	}
	
	//Parameterized Constructor
	public Depositor (String SSN, Name name) {
		ssn = SSN;
		fullName = name;
	}
	
	//Getters
	public String getSSN() {
		return ssn;
	}		
	public Name getName() {
		return fullName;
	}
}
