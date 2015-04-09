package buffermanager;

import java.io.File;
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
    }
}
