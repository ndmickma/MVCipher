// imports go here
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *	MVCipher - This program will take user input for a key and also what is to be encrpted and/or decrypted as well as the choice of encrpt/decrypt.
 *	Requires Prompt and FileUtils classes.

 *	
 *	@author	Sanvitti Shah
 *	@since	09/27/26
 */
public class MVCipher {
	
	// fields go here
	private String key; //what the user entered as the key
	private int index; //counter that tells us which letter of the key to use
		
	/** Constructor */
	public MVCipher() { 
		key = "";
		index = 0;

	}
	
	public static void main(String[] args) {
		MVCipher mvc = new MVCipher();
		mvc.run();
	}
	
	/**
	 *	Method header goes here
	 */
	public void run() {
		System.out.println("\n Welcome to the MV Cipher machine!\n");
		
		/* Prompt for a key and change to uppercase
		   Do not let the key contain anything but alpha
		   Use the Prompt class to get user input */
		
		key = getKey();

		/* Prompt for encrypt or decrypt */
		int choice = Prompt.getInt("Encrypt or decrypt (1 or 2)? ", 1,2);
			
		/* Prompt for an input file name */
		String inputFile = Prompt.getString("Enter the name of the file you want to read from: ");
		
		/* Prompt for an output file name */
		String outputFile = Prompt.getString("Enter the name of the file you want to write to: ");
		
		
		/* Read input file, encrypt or decrypt, and print to output file */
		Scanner input = null; //to read files
		PrintWriter output = null; //to write to files

		//use try catch to try opening file to read

		try{
			input = new Scanner(new File(inputFile));
		}
		catch(FileNotFoundException e){
			System.err.println("ERROR: Cannot open " + inputFile + " to read.");
			System.exit(1);

		}

		//use try catch to try opening file to write to
		try{
			output = new PrintWriter(new File(outputFile));
		}
		catch(FileNotFoundException e){
			System.err.println("ERROR: Cannot open " + outputFile + " to write.");
			System.exit(2);
		}

		while(input.hasNextLine()){
			String line = input.nextLine(); //get the line

			//loop through each character(index) in the line and call encrypt on it or decrypt depending on what the user picked
			for(int i = 0; i < line.length(); i++){
				char ch = line.charAt(i); //get character at index i
				if(choice == 1){ //if encrypt picked then call encrypt on character and print it to output file
					output.print(encrypt(ch));
				}
				else{ //if decrypt picked then call decrypt on ch and print it to output file
					output.print(decrypt(ch));
				}
			}
			output.println(); //move to next line after each sentence
		}
		
		/* Don't forget to close your output file */
		output.close();
		input.close();
	}
	
	// other methods go here
	public String getKey() {
		String keyIn = Prompt.getString("Please enter a key (letters only): ");
		while(!keyIsValid(keyIn)){
			keyIn = Prompt.getString("Please enter a key (letters only): ");
		}
		
		keyIn = keyIn.toUpperCase();

		return keyIn;
	}

	public boolean keyIsValid(String keyIn) {
		if(keyIn.length() < 3) //key must be at least 3 letters long
			return false;

		for(int i = 0; i < keyIn.length(); i++){
			if((keyIn.charAt(i)  < 'A' || keyIn.charAt(i) > 'Z') && (keyIn.charAt(i) < 'a' || keyIn.charAt(i) > 'z')){
				return false;
			}
		}
		return true;
	}

	public char encrypt(char ch){
		//if the character is not a letter leave it as is
		if((ch < 'A' || ch > 'Z') && (ch < 'a' || ch > 'z')){			
			return ch;
		}
		//this is all if ch is a letter

		int shift = key.charAt(index) - 'A' + 1; //use the character at the "index" position of the key and 
			// subtract "A" from it then add one to get its position in alphabet (e.g A A-A = 0 0 + 1 = 1, A is letter # 1)

			//encrypt an uppercase letter
			if(ch >= 'A' && ch <= 'Z'){ //if character from input file is an uppercase letter
				ch = (char)(ch + shift); //shift the ch from input file by the shfit and cast to char

				//check if we need to wrap around (ch after shifting goes past 'Z')
				if(ch > 'Z'){
					ch = (char)(ch - 26); //subtract 26 from already shifted number brings us back to properly shifted letter
					//think of it as going back one whole alphabet NOT wrapping around. 		
				}
			} 

			//encrypt a lowercase letter
			else{ //if it isn't an uppercase letter, it needs to be a lowercase letter
				//same process
				ch = (char)(ch + shift);
				if(ch > 'z'){ //if it goes past 'z' "wrap around"
					ch = (char)(ch - 26);
				}

			}

			index = (index + 1) % key.length(); //shifts the index of the key by one each letter of input file 
			//repeats when index + 1 % length of key == 0, meaning they are equal so index should restart (bc index starts at 0)

			return ch; //return the encrypted character
	}

	public char decrypt(char ch){
		//if character not a letter leave it as it is
		if((ch < 'A' || ch > 'Z') && (ch < 'a' || ch > 'z')){
			return ch; //return it as is
		}

		//this is all if it is a letter
		int shift = key.charAt(index) - 'A' + 1; //use the character at the "index" position of the key and 
			// subtract "A" from it then add one to get its position in alphabet (e.g A A-A = 0 0 + 1 = 1, A is letter # 1
		
		//decrypt an uppercase letter
		if(ch >= 'A' && ch <= 'Z'){
			//shift the letter (subtract because going "backwards")
			ch = (char)(ch - shift);

			//if goes past 'A'
			if(ch < 'A'){
				ch = (char)(ch + 26); //shift forward one WHOLE alphabet 
			}
		}
		
		//decrypt a lowercase letter
		else{
			ch = (char)(ch - shift);

			if(ch < 'a'){
				ch = (char)(ch + 26);
			}
		}

		index = (index + 1) % key.length();
		return ch;
	}

	
	
}