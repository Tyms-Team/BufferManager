package buffermanager;

import java.io.File;
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
    
    //private static SplayBST buffer; //Will use this later to control the buffer.
    
    private static Employee[] empList = new Employee[11];
    private static Department[] deptList = new Department[12];
    private static EmployeeDepartment[] edList = new EmployeeDepartment[13];
    
    public static void main(String[] args) {
        Scanner in;
        /*try{
            FileWriter out = new FileWriter("employee.txt",false);
            out.write("");
            System.exit(0);
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(4);
        }
        */
        try{
            
            in=new Scanner(new File("employee.txt"));
            int i=0;
            while (in.hasNext() && i<empList.length){
                String s = in.nextLine();
                int j=s.indexOf(',');
                empList[i] = new Employee(Integer.parseInt( s.substring(0,j) ),s.substring(j+1) );
                i++;
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        try{
            in=new Scanner(new File("department.txt"));
            int i=0;
            while (in.hasNext() && i<deptList.length){
                String s = in.nextLine();
                int j=s.indexOf(',');
                deptList[i] = new Department(Integer.parseInt( s.substring(0,j) ),s.substring(j+1) );
                i++;
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        try{
            in=new Scanner(new File("employee-department.txt"));
            int i=0;
            while (in.hasNext() && i<edList.length){
                String s = in.nextLine();
                int j=s.indexOf(',');
                edList[i] = new EmployeeDepartment(Integer.parseInt( s.substring(0,j) ),Integer.parseInt( s.substring(j+1) ) );
                i++;
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Employees:");
        for (Record item : empList){
            System.out.print(item);
        }
        
        System.out.println("\nDepartments:");
        for (Record item : deptList){
            System.out.print(item);
        }
        
        System.out.println("\nEmployee-Departments:");
        for (Record item : edList){
            System.out.print(item);
        }
        
        String query1 = "select 1select, 2 from 3 NATURAL JOIN 4 where 5 = 6 or 7 = 8;";
        String[] selectStatement = querySelect(query1);
        String[] fromStatement = queryFrom(query1);
        String[] whereStatement = queryWhere(query1);
        System.out.print("Array = ");
        for (int i = 0; i < whereStatement.length; i++){
        	System.out.println(i + " = " + whereStatement[i]);
        }
        System.out.println();
        //System.out.println("Size of statement is: " + selectStatement.length);
        System.out.println("query = " + query1);
    }
    
    // returns select statement
    public static String[] querySelect(String theQuery){
    	
    	theQuery = theQuery.toLowerCase();
    	
    	// Get string from beginning up until From or ;
    	int i = theQuery.indexOf("from");
    	if (i == -1 || theQuery.indexOf(';') < i){
    		i = theQuery.indexOf(';');
    	}
    	theQuery = theQuery.substring(0,i);
    	
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
    	theQuery = theQuery.substring(theQuery.indexOf("from"),i);
    	
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
    	theQuery = theQuery.substring(theQuery.indexOf("where"),i);
    	
    	//System.out.println("theQuery = " + theQuery);
    	
    	String delims = "[ ;]\\s*";
        String[] tokens = theQuery.split(delims);
        
    	return tokens;
    }
    
    /*
    public static String[] removeEmpty(String[] queryArray){
    	String query = "";
    	query += queryArray;
    	
    }
    */
}
