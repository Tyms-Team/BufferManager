package buffermanager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileWriter;

import com.sun.xml.internal.ws.util.StringUtils;

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
    	String query = in.nextLine();
    	
    	String[] sList = queryFrom(query);
    	
    	Scanner objectIn=openFile(sList);
    	
    	sList = queryWhere(query);
    	
    	while (objectIn.hasNext()){
    		String tuple=objectIn.nextLine();
    		if (passesCheck(tuple,sList))
    			//System.out.println(tuple);
    			;
    	}
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
    
    // returns select statement
    public static String[] querySelect(String theQuery){
    	
    	theQuery = theQuery.toLowerCase();
    	
    	// Get string from beginning up until From or ;
    	int i = theQuery.indexOf("from");
    	if (i == -1 || theQuery.indexOf(';') < i){
    		i = theQuery.indexOf(';');
    	}
    	theQuery = theQuery.substring(6,i);
    	
    	//System.out.println("theQuery = " + theQuery);
    	
    	String delims = "[ ,;]\\s*";
        String[] tokens = theQuery.split(delims);
        
    	return tokens;
    }
    
    // returns from
    public static String[] queryFrom(String theQuery){
    	theQuery = theQuery.toLowerCase();
    	
    	// Get string from 'from' up until where or ;
    	int i = theQuery.indexOf("where");
    	if (i == -1 || theQuery.indexOf(';') < i){
    		i = theQuery.indexOf(';');
    	}
    	theQuery = theQuery.substring(theQuery.indexOf("from")+4,i);
    	
    	//System.out.println("theQuery = " + theQuery);
    	
    	String delims = "[ ;]\\s*";
        String[] tokens = theQuery.split(delims);
        
    	return tokens;
    }
    
    // returns where
    public static String[] queryWhere(String theQuery){
    	theQuery = theQuery.toLowerCase();
    	
    	// Get string from 'from' up until where or ;
    	int i = theQuery.indexOf(';');
    	theQuery = theQuery.substring(theQuery.indexOf("where")+5,i);
    	
    	//System.out.println("theQuery = " + theQuery);
    	
    	String delims = "[ ;]\\s*|\\b";
        String[] tokens = theQuery.split(delims);
        
    	return tokens;
    }
    
    public static Scanner openFile(String[] fromString){
    	Scanner r = null;
    	String s="";
    	int state=0;
    	
    	for (String file: fromString){
    		switch (file){
    		case "":
    			break;
    		case "employee":
    			try{
    				r = new Scanner(new File(Employee.fname));
    			} catch (Exception e){
    				System.err.printf("Don't delete %s.\n",Employee.fname);
    			}
    			break;
    		case "department":
    			try {
					r = new Scanner(new File(Department.fname));
				}  catch (Exception e){
					System.err.printf("Don't delete %s.\n",Department.fname);
    			}
    			break;
    		case "employeedepartmnet":
    			try {
					r = new Scanner(new File(EmployeeDepartment.fname));
				} catch (FileNotFoundException e) {
					System.err.printf("Don't delete %s.\n",EmployeeDepartment.fname);
				}
    			break;
    		case "natural":
    			state=1;
    			break;
    		case "outer":
    			state=2;
    			break;
    		case "inner":
    			state=3;
    			break;
    		case "join":
    			break;
			default:
				System.err.printf("Syntax error in FROM statement\n");
				System.exit(2);
    		}
    	}
    	
    	if (r==null){
    		System.err.printf("Syntax error in FROM statement\n");
    		System.exit(2);
    	}
    	
    	return r;
    }
    
    public static boolean passesCheck(String tuple,String[] checkRules){
    	compileCheck(checkRules); 
    	return true;
    }
    
    public static String[] compileCheck(String[] checkRules){
    	String[] r = new String[checkRules.length];
    	int last=0;
    	for (String s:checkRules){
    		if ( s.matches("where") ){
    			//Do nothing
    		}
    		else if ( s.matches("id|name|employee_id|dept_id") ){
    			r[last]=s;
    		}
    		else if ( s.matches("[><]?=|[><]") ){
    			r[last]+=s;
    		}
    		else if ( s.matches("\".*\"|[1-9]\\d*") ){
    			r[last]+=s;
    			System.out.println(r[last]);
    			last++;
    		}
    	}
    	return r;
    }
}
