package buffermanager;

/**
 *
 * @author Tyler
 */
public class Employee extends Record implements Comparable<Employee>{
    private int ID;
    private String name;
    
    public Employee(int startID, String startName){
        setID(startID);
        setName(startName);
    }
    public Employee(int startID){
        this(startID,"");
    }
    public Employee(String startName){
        //This is an error.
        this(-1,startName);
    }
    public Employee(){
        //This is an error?
        this(-1,"");
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        //Maybe check if it's unique?
        this.ID = ID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        if (name.length()<15)
            this.name = name;
        else
            this.name=name.substring(0,15);
    }
    
    /**
     * @return id,name\n
     */
    public String toString(){
        return String.format("%d,%s\n",getID(),getName());
    }
    
    /**
     * @param e Employee object to compare to
     * @return 1 if this.id>e.id, 0 if this.id=e.id, -1 is this.id<e.id
     */
    public int compareTo(Employee e){
        return Integer.compare(this.getID(),e.getID());
    }
}
