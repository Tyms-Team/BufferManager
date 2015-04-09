package buffermanager;

/**
 *
 * @author Tyler
 */
public class EmployeeDepartment extends Record implements Comparable<EmployeeDepartment>{
    private Integer employee_id;
    private Integer dept_id;
    
    public static final String fname = "employee-department.txt";
    
    public EmployeeDepartment(int startEID,int startDID){
        setEmployee_id(startEID);
        setDept_id(startDID);
    }
    
    public EmployeeDepartment(){
        //This is an error?
        this(-1,-1);
    }

    /**
     * @return the employee_id
     */
    public int getEmployee_id() {
        return employee_id;
    }

    /**
     * @param employee_id the employee_id to set
     */
    public void setEmployee_id(int employee_id) {
        //Check if this is in the employee table?
        this.employee_id = employee_id;
    }

    /**
     * @return the dept_id
     */
    public int getDept_id() {
        return dept_id;
    }

    /**
     * @param dept_id the dept_id to set
     */
    public void setDept_id(int dept_id) {
        //Check if this is in the department table?
        this.dept_id = dept_id;
    }
    
    /**
     * @return employee_id,dept_id\n
     */
    public String toString(){
        return String.format("%d,%d\n",getEmployee_id(),getDept_id());
    }
    
    /**
     * @param e Employee object to compare to
     * @return 1 if this.id>e.id, 0 if this.id=e.id, -1 is this.id<e.id
     */
    public int compareTo(EmployeeDepartment e){
        return Integer.compare(this.getDept_id(),e.getDept_id());
    }
}
