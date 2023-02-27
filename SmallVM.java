//Title: SmallVm Project | Name: Spencer Sesser | Class: CSCI 4200 - A1 | Professor: Dr. Abi Salimi

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class SmallVM {
	public static String[] mainMem = new String[500];
	public static int progCount = 0;
	public static int lastOpen = 1;
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
		try {
			PrintStream outStream = new PrintStream(new File("mySmallVm_Output.txt"));
	        System.setOut(outStream);
		} catch (FileNotFoundException e) {e.printStackTrace();}
    }
	
	public static void printMainMem() { //Prints the mainMem array used to understand memory storage
		System.out.println("**********************************************" + "\n" + "Main Memory Array:");
		for(int i=0; mainMem[i] != null; i++) 
			System.out.println("Line " + i + ": " + mainMem[i]);
	}
	
	public static void loadFile() { //Loads the file into mainMem array and increases the last open spot reference in memory
		try {
	        Scanner prog = new Scanner(new File("mySmallVm_Prog.txt"));
	        for(int i = 0; prog.hasNextLine() == true; i++) {
	        	mainMem[i] = prog.nextLine();
	        	lastOpen = i + 1;
	            System.out.println(mainMem[i]); //Prints mainMem Array
	        }
	        prog.close();
	    } 
	    catch (FileNotFoundException e) {e.printStackTrace();}
	}

	public static void decode() { //Determines which methods the prog lines need to call
		while (mainMem[progCount] != null) {
            Scanner line = new Scanner(mainMem[progCount]);
            String testCase = line.next();
            line.close();
            switch(testCase) {
            	case "IN": inComm(); break;
            	case "OUT": outComm(); break;
            	case "STO": stoComm(); break;
            	case "HALT": printMainMem(); System.exit(0); break;
            	case "ADD", "SUB", "MUL", "DIV": doMath(); break;
            }
            progCount++;
       }
	}
	
	public static void inComm() { //Recieves user input
		String value = input.nextLine();
		String variable = mainMem[progCount].replaceFirst("IN ", "") + " ";
		String result = variable + value;
		System.out.println(value); //Prints user input below the in statement for output file
		mainMem[lastOpen] = result;
		lastOpen++;
	}
	
	public static void outComm() { //Determines if needed to print statement or variables
		if(mainMem[progCount].contains("\"")) { //Prints out's with " in them
			for(int i = mainMem[progCount].indexOf("\"") + 1; mainMem[progCount].charAt(i) != '\"'; i++) 
				System.out.print(mainMem[progCount].charAt(i));
		}
		else System.out.print(searchMem(findVarInLine(4)));
		System.out.println();
	}
	
	public static void doMath() { //Does the math operations requested
		Scanner findVals = new Scanner(mainMem[progCount]);
		String operation = findVals.next();
		String destination = findVals.next();
		int val1 = toValue(findVals.next());
		int val2 = toValue(findVals.next());
		findVals.close();
		int math = 0;
		switch(operation) {
			case "ADD": math = val1 + val2; break;
			case "SUB": math = val1 - val2; break;
			case "MUL": math = val1 * val2; break;
			case "DIV": math = val1 / val2; break;
		}
		String result = destination + " " + math;
		mainMem[lastOpen] = result;
		lastOpen++;
	}

	public static void stoComm() { //Stores requested variable or int
		int tempIndex = 4;
		int tempProCount = 0;
		String destination = findVarInLine(tempIndex);
		tempIndex += destination.length() + 1;
		int variable = toValue(findVarInLine(tempIndex));
		while(mainMem[tempProCount] != null && mainMem[tempProCount].startsWith(destination) == false) { //Searches mainMem for the requested destination index
			tempProCount++;
		}
		mainMem[tempProCount] = destination + " " + variable;
	}
	
	public static String findVarInLine(int index){ //index = char in line to start | Returns variable names
		String result = "";
		while(index < mainMem[progCount].length() && mainMem[progCount].charAt(index) != ' ') {
			result += mainMem[progCount].charAt(index);
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
		int tempProCount = 0;
		while(mainMem[tempProCount].startsWith(variable) == false) { //Searches mainMem for the requested variable name
			tempProCount++; 
		}
		Scanner temp = new Scanner(mainMem[tempProCount]); //Finds variable in the prog line
		temp.next();
		String outVar = temp.next();
		temp.close();
		return outVar;
	}
}