/*
 The Name class contains the users first and last name, as well as getters, no argument constructors, and parameterized
 constructors
 */
public class Name {
	private String first;
	private String last;
	
	//No-Arg Constructor
	public Name () {
		first = "";
		last = "";
	}
	//Parameterized Constructor
	public Name (String f, String l) {
		first = f;
		last = l;
	}
	//getters
	public String getLast() {
		return last;
	}		
	public String getFirst() {
		return first;
	}
}
