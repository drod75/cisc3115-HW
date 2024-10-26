/*
 The depositor class contains the accounts social security number name from the name class,
 it also contains setters and getters. 
 */
public class Depositer {
	private String ssn;
	private Name fullName = new Name();
	
	public void setSSN(String set) {
		ssn = set;
		}
	public void setName(Name set) {
		fullName = set;
	}
	
	public String getSSN() {
		return ssn;
	}		
	public Name getName() {
		return fullName;
	}
}
