package buffermanager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;

/**
 *
 * @author Tyler
 */
public class BufferManager {
    
    /**
     * @param args the command line arguments
     */
    
    private static SplayBST<Integer,Record> buffer; 
    private static int bufferCount=0;
    private static int bufferSize=0;
    
    /*
    private static Employee[] empList = new Employee[11];
    private static Department[] deptList = new Department[12];
    private static EmployeeDepartment[] edList = new EmployeeDepartment[13];
    */
    
    public static void main(String[] args) {
    	Scanner in=init(args);
    	
    }
    
    /**
     * Processes command line arguments
     * @param args command line arguments
     * @return A file scanner or a System.in scanner
     */
    private static Scanner init(String[] args){
    	Scanner in = new Scanner(System.in);
    	try{
	    	if (args.length<1){ //No arguments
	    		throw new Exception(); //Tell user usage and exit
	    	}
	    	else if (args.length<3){ //1 or 2 arguments
	    		bufferSize=Integer.parseInt(args[0]); //Set buffer size. If args[0] is not an int, exit
	    		if (args.length==2)
	    			in = new Scanner( new File(args[1]) ); //Create Scanner for file
	    	}
	    	
    	}
    	//Exit on errors
    	catch (FileNotFoundException e){
    		System.err.printf("Could not find file %s.\n",args[1]);
    		System.exit(1);
    	}
    	catch (Exception e){ //Tell usage and exit
    		System.err.println("Usage: java BufferManager <buffer size> [query filename]");
    		System.exit(1);
    	}
    	
    	return in;
    }
}
