//Title: SmallVm Project | Name: Spencer Sesser | Class: CSCI 4200 - A1 | Professor: Dr. Abi Salimi

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class SmallVM {
	public static String[] mainMem = new String[500];
	public static int progCount = 0;
	public static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		//setOutFile(); //Uncomment to see output go to file in workspace.
		System.out.println("Spencer Sesser, CSCI 4200, Spring 2023" + "\n" + "**********************************************");
		loadFile();
		System.out.println("**********************************************");
		decode();
		input.close();
	}
	
	public static void setOutFile() { //Sets the printstream to the requested file output in workspace folder.
   	 File output = new File("mySmallVm_Output.txt"); 
		try {
			PrintStream outStream = new PrintStream(output);
	        System.setOut(outStream);
		} catch (FileNotFoundException e) {e.printStackTrace();}
    }
	
	public static void printMainMem() { //Prints the mainMem array used to understand memory storage
		System.out.println("**********************************************" + "\n" + "Main Memory Array:");
		for(int i=0; mainMem[i] != null; i++) {
			System.out.println("Line " + i + ": " + mainMem[i]);
		}
	}
	
	public static void loadFile() { //Loads the file into mainMem array
		File progFile = new File("mySmallVm_Prog.txt");
		try {
	        Scanner prog = new Scanner(progFile);
	        for(int i = 0; prog.hasNextLine() == true; i++) {
	        	mainMem[i] = prog.nextLine();
	            System.out.println(mainMem[i]); //Prints mainMem Array
	        }
	        prog.close();
	    } 
	    catch (FileNotFoundException e) {e.printStackTrace();}
	}

	public static void decode() { //Determines which methods the prog lines need to call
		while (mainMem[progCount] != null) {
            String testCase = mainMem[progCount];
            if (testCase.startsWith("IN")) inComm(); 
            else if (testCase.startsWith("OUT")) outComm();
            else if (testCase.matches("(ADD|SUB|MUL|DIV).*")) doMath();
            else if (testCase.startsWith("STO")) stoComm(); 
            else if (testCase.startsWith("HALT")) { printMainMem(); System.exit(0); }
            progCount++;
       }
	}
	
	public static void inComm() { //Recieves user input
		String value = input.nextLine();
		String variable = mainMem[progCount].replaceFirst("IN ", "") + " ";
		String result = variable + value;
		int index = 0;
		while(mainMem[index] != null) {
			index++;
		}
		System.out.println(value); //Prints user input below the in statement for output file
		mainMem[index] = result;
	}
	
	public static void outComm() { //Determines if needed to print statement or variables
		if(mainMem[progCount].contains("\"")) { //Prints out's with " in them
			for(int i = mainMem[progCount].indexOf("\"") + 1; mainMem[progCount].charAt(i) != '\"'; i++) 
				System.out.print(mainMem[progCount].charAt(i));
		}
		else System.out.print(searchMem(findVarInLine(progCount, 4)));
		System.out.println();
	}
	
	public static void doMath() { //Does the math operations requested
		int tempIndex = 4;
		String destination = findVarInLine(progCount, tempIndex);
		tempIndex += destination.length() + 1;
		String variable1 = findVarInLine(progCount, tempIndex);
		tempIndex += variable1.length() + 1;
		String variable2 = findVarInLine(progCount, tempIndex);
		int valV1 = toValue(variable1);
		int valV2 = toValue(variable2);
		int mathOperation = 0;
		if(mainMem[progCount].startsWith("ADD")) mathOperation = valV1 + valV2;
		else if(mainMem[progCount].startsWith("SUB")) mathOperation = valV1 - valV2;
		else if(mainMem[progCount].startsWith("MUL")) mathOperation = valV1 * valV2;
		else if(mainMem[progCount].startsWith("DIV")) mathOperation = valV1 / valV2;
		String result = destination + " " + mathOperation;
		int resCount = 0;
		while(mainMem[resCount] != null) { //Finds open slot in mainMem
			resCount++;
		}
		mainMem[resCount] = result;
	}

	public static void stoComm() { //Stores requested variable or int
		int tempIndex = 4;
		int tempProCount = 0;
		String destination = findVarInLine(progCount, tempIndex);
		tempIndex += destination.length() + 1;
		String variable = String.valueOf(toValue(findVarInLine(progCount, tempIndex)));
		while(mainMem[tempProCount]!= null && mainMem[tempProCount].startsWith(destination) == false) { //Searches mainMem for the requested destination index
			tempProCount++;
		}
		mainMem[tempProCount] = destination + " " + variable;
	}
	
	public static String findVarInLine(int pCount, int index){ //pCount = progCounter | index = char in line to start | Returns variable names
		String result = "";
		while(index < mainMem[pCount].length() && mainMem[pCount].charAt(index) != ' ') {
			result += mainMem[pCount].charAt(index);
			index++;
		}
		return result;
	}
		
	public static int toValue(String input) { //Determines if the inputted string is a value or variable and returns int value
		int x = 0;
		try { x = Integer.parseInt(input); }
		catch (NumberFormatException e) { return toValue(searchMem(input)); }
		return x;
	}
	
	public static String searchMem(String variable) { //Searches mainMem for requested variable and returns value
		int tProgIndex = 0;
		int tempProCount = 0;
		String outVar = "";
		while(mainMem[tempProCount].startsWith(variable) == false) { //Searches mainMem for the requested variable name
			tempProCount++; 
		}
		while(tProgIndex < mainMem[tempProCount].length() && mainMem[tempProCount].charAt(tProgIndex) != ' ') { //Searches prog Line for variable assigned
			tProgIndex++; 
		}
		tProgIndex++;
		while(tProgIndex < mainMem[tempProCount].length()) { //Sets variable value to outVar
			outVar += mainMem[tempProCount].charAt(tProgIndex);
			tProgIndex++; 
		}
		return outVar;
	}
}