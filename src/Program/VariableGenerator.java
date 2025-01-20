package Program;

public class VariableGenerator {
	
	private static int counter = 0;
	public String generateUniqueNumber() {
		
        char letter = (char) ('a' + counter % 26);
        int number = counter / 26;
        counter++; 
        return counter < 27?letter+"" : letter + String.valueOf(number);
    }

}
