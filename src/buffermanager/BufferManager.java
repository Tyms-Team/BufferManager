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
    
    public static void main(String[] args) throws FileNotFoundException {
    	Scanner in=init(args);
    	String query = in.nextLine();
    	
    	String[] sList = queryFrom(query);
        
        Scanner objectIn=openFile(sList);
    	
    	sList = queryWhere(query);
                
        String[] projectionList = querySelect(query);
        
        while (objectIn.hasNext()){
        	System.out.println(objectIn.nextLine());
        }
        
        printBoxHeader(projectionList);
    	while (objectIn.hasNext()){
            String tuple=objectIn.nextLine();
            if (passesCheck(tuple,sList)){
                printBoxLine(projectionList,tuple);
            }
    	}
        printBoxFooter(projectionList);
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
    	
    	//String delims = "[ ;]\\s*|\\b";
        String delims = "\\s+|;\\s*|(?<=[a-z])(?=[<>=])|(?<=[<>=])(?=(?:\"|\\d))|(?=&&)|(?=\\|\\|)"; //This got a lot more complicated, whoops
        String[] tokens = theQuery.split(delims);
        
    	return tokens;
    }
    
    /**
     * Opens a scanner from the indicated table, whether it's from a file or a join.
     * @param fromString queryFrom(query);
     * @return Scanner over the tuples in the correct table. That may be on a file or a string.
     * @throws FileNotFoundException 
     */
    //@SuppressWarnings("resource")
	public static Scanner openFile(String[] fromString) throws FileNotFoundException{
    	Scanner r = null; //Return Scanner
    	int state = 0;
    	String saveString = "";
    	Scanner s = null;
    	for (String file: fromString){
            switch (file){
            case "": 
                //Empty string, do nothing.
                break;
            case "employee": //Open a scanner on the employee file
                try{
                	if(r == null){
                		r = new Scanner(new File(Employee.fname)); //Open the scanner on employee.txt
                	}else{
                		String w = r.nextLine();
                		s = new Scanner(new File(Employee.fname));
                		w += ',' + s.nextLine() + '\n';
                		while (r.hasNext()){
                			s = new Scanner(new File(Employee.fname));
                			s.nextLine();
                			String rline = r.nextLine();
                			while(s.hasNext()){
                				String sline = s.nextLine();
                				w += join(rline,sline,state);
                			}
                			//r = new Scanner(w);
                		}
                		r = new Scanner(w);
                	}
                } catch (Exception e){
                    System.err.printf("Don't delete %s.\n",Employee.fname); //Someone deleted employee.txt
                }
                break;
            case "department": //Very similar to above.
                try {
                	System.out.println("We are in department");
                	if(r == null){
                		r = new Scanner(new File(Department.fname)); //Open the scanner on employee.txt
                	}else{
                		String w = "";
                		while (r.hasNext()){
                			s = new Scanner(new File(Department.fname));
                			String rline = r.nextLine();
                			while(s.hasNext()){
                				String sline = s.nextLine();
                				w += join(rline,sline,state);
                			}
                			//r = new Scanner(w);
                		}
                		r = new Scanner(w);
                	}
                }  catch (Exception e){
                    System.err.printf("Don't delete %s.\n",Department.fname);
                }
                break;
            case "employee_department": //Again, very similar
                try {
                	System.out.println("We are in employee_department");
                	if(r == null){
                		r = new Scanner(new File(EmployeeDepartment.fname)); //Open the scanner on employee.txt
                	}else{
                		String w = "";
                		while (r.hasNext()){
                			s = new Scanner(new File(EmployeeDepartment.fname));
                			String rline = r.nextLine();
                			while(s.hasNext()){
                				String sline = s.nextLine();
                				w += join(rline,sline,state);
                			}
                			//r = new Scanner(w);
                		}
                		r = new Scanner(w);
                	}
                } catch (FileNotFoundException e) { //For some reason I decided to not use the generic exception. Weird.
                    System.err.printf("Don't delete %s.\n",EmployeeDepartment.fname);
                }
                break;
            
            //TO DO: Join statement stuff.
            case "natural":
                //I'm thinking use these three to figure out what kind of join we're doing. 
            	//Save it to a state variable outside the for loop
                state = 1;
            	break;
            case "outer":
            	state = 2;
                break;
            case "inner":
            	state = 3;
                break;
            case "join":
            	state = 0;
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
    
    private static String join(String rline, String sline, int state) {
    	switch(state){
    	case 0:
    		// JOIN
    			rline += ',' + sline + '\n';
    		break;
    	case 1:
    		// NATURAL
    		
    		break;
    	case 2:
    		// OUTER
    		
    		break;
    	case 3:
    		// INNER
    		
    		break;
    	default:
    		System.err.println("There's a problem in from statement.");
    		System.exit(2);
    	}
    	//System.out.println(rline);
		return rline;
	}

	public static boolean passesCheck(String tuple,String[] checkRules){
        boolean r=false;
        String lvalue=null;
        String op=null;
        String boolOp=null;
        
        for (String s: checkRules){
            if (s.equals("")){
                continue;
            }
            else if (s.matches("^(?:\\|\\||&&)$")){
                boolOp=s;
            }
            else if (s.matches("^(?:[><]?=|[><])$")){
                op=s;
            }
            //else if (s.matches("^(?:employee|department)\\.id$|^employeedepartment\\.employee_id$")){
            else if (s.matches("^id$|^employee_id$")){
                lvalue=tuple.substring(0,tuple.indexOf(',')).toLowerCase();
            }
            else if (s.matches("^name$|^dept_id$")){
                lvalue=tuple.substring(tuple.indexOf(',')+1).toLowerCase();
            }
            else {
                int compvalue;
                //Figure out the comparison value
                if (s.matches("\".*\"")){
                    s=s.substring(1,s.length()-1);
                    compvalue = lvalue.compareTo(s);
                }
                else if ( s.matches("\\d+")){
                    Integer i=Integer.parseInt(lvalue);
                    Integer j=Integer.parseInt(s);
                    
                    compvalue=i.compareTo(j);
                }
                else{
                    return false;
                }
                
                //Figure out if the comparison value is part of the operator
                boolean sr;
                if (compvalue<0 && op.indexOf('<')!=-1){
                    sr=true;
                }
                else if (compvalue==0 && op.indexOf('=')!=-1){
                    sr=true;
                }
                else if (compvalue>0 && op.indexOf('>')!=-1){
                    sr=true;
                }
                else{
                    sr=false;
                }
                
                
                
                //Apply any boolean operators
                if (boolOp==null){
                    r=sr;
                }
                else if (boolOp.equals("&&")){
                    r=r && sr;
                    boolOp=null;
                }
                else if (boolOp.equals("||")){
                    r=r || sr;
                    boolOp=null;
                }
            }
        }
        if (boolOp!=null)
            return false;
    	return r;
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

    private static void printBoxHeader(String[] projectionList) {
        boolean printfinal=true;
        System.out.print('+');
        
        for (String s: projectionList){
            if (!s.equals("")){
                int end=(15-s.length())>>1;
                for (int i=0;i<end;i++){
                    System.out.print('-');
                }
                System.out.print(s);
                
                end=end-1*((s.length()%2==1)?1:0);
                for (int i=0;i<end;i++){
                    System.out.print('-');
                }
                printfinal=false;
                System.out.print('+');
            }
        }
        if (printfinal)
            System.out.print('+');
        System.out.println();
    }

    private static void printBoxFooter(String[] projectionList) {
        boolean printfinal=true;
        System.out.print('+');
        
        for (String s: projectionList){
            if (!s.equals("")){
                for (int i=0;i<14;i++){
                    System.out.print('-');
                }
                System.out.print('+');
                printfinal=false;
            }
        }
        if (printfinal)
            System.out.print('+');
        System.out.println();
    }

    private static void printBoxLine(String[] projectionList, String tuple) {
        boolean printfinal=true;
        System.out.print('|');
        
        for (String s: projectionList){
            int end;
            switch (s){
                case "":
                    break;
                case "id":
                case "employee_id":
                    s=tuple.substring(0,tuple.indexOf(','));
                    end=(15-s.length())>>1;
                    for (int i=0;i<end;i++){
                        System.out.print(' ');
                    }
                    System.out.print(s);
                    
                    end=end-1*((s.length()%2==1)?1:0);
                    for (int i=0;i<end;i++){
                        System.out.print(' ');
                    }
                    System.out.print('|');
                    printfinal=false;
                    break;
                case "name":
                case "dept_id":
                    s=tuple.substring(tuple.indexOf(',')+1);
                    end = (15-s.length())>>1;
                    for (int i=0;i<end;i++){
                        System.out.print(' ');
                    }
                    System.out.print(s);
                    
                    end=end-1*((s.length()%2==1)?1:0);
                    for (int i=0;i<end;i++){
                        System.out.print(' ');
                    }
                    System.out.print('|');
                    printfinal=false;
                    break;
            }
        }
        
        
        if (printfinal)
            System.out.print('|');
        System.out.println();
    }
}
