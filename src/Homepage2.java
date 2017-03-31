import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class Homepage {
    public static final Connection conn = JDBC_Connection.makeConnection();
    public static final Scanner in = new Scanner(System.in);
    public static ResultSet rs;
    public static PreparedStatement preparedStatement;
    public static String query;
    public static void printWelcome()
    {
        System.out.println("***************************************");
	System.out.println("Welcome to CSC 540 Project Spring 2017");
	System.out.println("***************************************");
    }
    
     public static void loginUser()
    {
        System.out.println("Enter Username:");
	String username = in.nextLine();
	System.out.println("Enter password:");
	String password = in.nextLine();
        String name = "";
        boolean admin = false;
        query = "Select * from ADMINISTRATOR WHERE USERNAME=? and PASSWORD=?";
        try 
        {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                admin = true;
                name = rs.getString("F_NAME")+" "+rs.getString("L_NAME");
            }
            else
            {
                query = "Select * from STUDENT WHERE USERNAME=? and PASSWORD=?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                rs = preparedStatement.executeQuery();
                
                if(rs.next())
                {
                    admin = false;
                    name = rs.getString("F_NAME")+" "+rs.getString("L_NAME");
                }
                else
                {
                    System.out.println("Invalid Username or Password Try Again");
                    loginUser();
                }
            }
            if(admin)
                welcomeAdmin(name);
            else
                System.out.println("Welcome Student");
           // else
                //student home page
                
        } 
        catch (Exception e) 
        {
           e.printStackTrace();
            System.out.println("Hello Exception");
        }
        
    }
    
    public static void enterNewStudent() throws Exception
    {
        System.out.println("*********************");
        System.out.println("Enter a new Student");
        System.out.println("**********************");
        System.out.print("Student ID: ");
        String student_id = in.nextLine();
        System.out.print("Username: ");
        String username = in.nextLine();
        System.out.print("First Name: ");
        String f_name = in.nextLine();
        System.out.print("Last Name: ");
        String l_name = in.nextLine();
        System.out.print("GPA: ");
        float gpa = in.nextFloat();
        in.nextLine();
        System.out.print("Email ID: ");
        String email = in.nextLine();
        System.out.print("Password: ");
        String password = in.nextLine();
        System.out.print("Residency: ");
        String residency = in.nextLine();
        System.out.print("Classification Level: ");
        String class_level = in.nextLine();
        System.out.print("Department: ");
        String dept = in.nextLine();
        System.out.print("Date of Birth(yyyy-dd-mm): ");
        String date = in.nextLine();
        Date dob = Date.valueOf(date);
        System.out.println(dob);
        query = "insert into STUDENT(STUDENT_ID,F_NAME, l_name, gpa, email, password, "
                + "current_credits,residency, class_level, department, "
                + "pending_bill) values (?,?,?,?,?,?,?,?,?,?,?)";
        preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, student_id);
        preparedStatement.setString(2, f_name);
        preparedStatement.setString(3, l_name);
        preparedStatement.setFloat(4, gpa);
        preparedStatement.setString(5, email);
        preparedStatement.setString(6, password);
        preparedStatement.setInt(7, 0);
        preparedStatement.setString(8, residency);
        preparedStatement.setString(9, class_level);
        preparedStatement.setString(10, dept);
        preparedStatement.setInt(11, 0);
        if(preparedStatement.executeUpdate()==1)
        {
            System.out.println("Student Entered Successfully");
        }
        else
        {
            System.out.println("Error Encountered");
        }
        
    }
    
    public static void viewStudent()
    {
        
    }
    
    public static void welcomeAdmin(String adminName)
    {
        System.out.println("***************************************");
	System.out.println("Welcome "+adminName);
	System.out.println("***************************************");
        System.out.println("Please choose an option:");
	System.out.println("1 to enter new student data");
	System.out.println("2 to get Student Info");
	System.out.println("3 to View or Add Course");
	System.out.println("4 to View or Add Course Offering");
        int option = in.nextInt();
        in.nextLine();
        switch (option) 
        {
            case 1:
                try 
                {
                    enterNewStudent();
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }
    
    public static void main(String[] args) 
    {
        printWelcome();
        loginUser();
    }

    static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Throwable whatever) {
			}
		}

	}

    static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (Throwable whatever) {
			}
		}
	}

    static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Throwable whatever) {
			}
		}
	}

}
