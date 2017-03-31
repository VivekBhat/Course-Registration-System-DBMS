import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Homepage {

	public static PreparedStatement stmt;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.println("***********");
		System.out.println("Admin Login");
		System.out.println("***********");
		System.out.println("Enter Username:");
		String username = in.nextLine();
		System.out.println("Enter password:");
		String password = in.nextLine();

		Connection conn = JDBC_Connection.makeConnection();
		// java.sql.Statement stmt = null;
		ResultSet rs = null;

		String sid;

		Date dob;

		System.out.println("Hello Admin");

		boolean switchOption = true;

		while (switchOption) {
			System.out.println("Please choose an option:");
			System.out.println("1 to enter new student data");
			System.out.println("2 to get Student Info");
			System.out.println("3 to View or Add Course");
			System.out.println("4 to View or Add Course Offering");
			int option = in.nextInt();
			in.nextLine();
			String opt = "y";

			switch (option) {
			case 1:
				opt = "y";

				while (opt.toLowerCase().equals("y")) {
					opt = newStud(in, opt);
				}

				switchOption = goBackToMenuOption(in);

				break;

			case 2:
				/*
				 * Task: After Showing details. Provide 2 options Check another
				 * student details Enter grades for this student
				 */

				System.out.println("Enter Student ID: ");
				sid = in.nextLine();
				opt = "y";

				while (opt.toLowerCase().equals("y"))
					getStud(in, sid);
				switchOption = goBackToMenuOption(in);

				break;

			case 3:
				System.out.println("View/Add Courses");
				System.out.println("****************\n");
				System.out.println("Press 1 to view all courses");
				System.out.println("Press 2 to view course by Course ID");
				System.out.println("Press 3 to Add Course:");
				int i = in.nextInt();
				in.nextLine();

				switch (i) {

				case 1:
					System.out.println("Displaying all courses:\n");
					try {
						String selectAllCourses = "Select * from Courses";
						stmt = conn.prepareStatement(selectAllCourses);
						rs = stmt.executeQuery();
						while (rs.next()) {
							String course_id = rs.getString("COURSE_ID");
							String title = rs.getString("TITLE");
							String class_level = rs.getString("CLASS_LEVEL");
							String department = rs.getString("DEPARTMENT");
							System.out.println(
									String.format("| %-5s\t | \t %-35s\t | \t %-5s |", course_id, title, department));
							// System.out.println(course_id +
							// "\t\t"+title+"\t\t"+class_level+"\t\t"+department);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						close(conn);
						close(stmt);
						close(rs);

					}

					break;

				case 2:
					System.out.println("Enter the COurses");
					System.out.println("Displaying course by Course ID");

					System.out.println("Enter Course ID: ");
					String selectCID = "select * from courses where COURSE_ID = ?";
					String courseID = in.nextLine(); // TO DO: Input from user
					try {
						stmt = conn.prepareStatement(selectCID);
						rs = stmt.executeQuery();
						while (rs.next()) {
							String course_id = rs.getString("COURSE_ID");
							String title = rs.getString("TITLE");
							String class_level = rs.getString("CLASS_LEVEL");
							String department = rs.getString("DEPARTMENT");
							System.out.println(
									String.format("| %-5s\t | \t %-35s\t | \t %-5s |", course_id, title, department));
							// System.out.println(course_id +
							// "\t\t"+title+"\t\t"+class_level+"\t\t"+department);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				case 3:

					break;

				default:
					break;
				}
				switchOption = goBackToMenuOption(in);

				break;

			case 4:
				System.out.println("View/Add Course Offering");

				System.out.println("Enter Student ID: ");
				sid = in.nextLine();

				opt = "y";

				break;

			case 5:
				System.out.println("View Pending Requests");

				// get pending requests from the table

				System.out.println("ADMIN: Please select request number:");
				String reqNo = in.nextLine();
				System.out.println("Do you want to approve: ");

				break;

			default:
				System.out.println("Invalid Option");
				switchOption = goBackToMenuOption(in);
				for (int j = 0; j < 50; j++) {
					System.out.println();
				}

			}

		}

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

	private static boolean goBackToMenuOption(Scanner in) {
		boolean switchOption;
		String switchOptionInput;
		System.out.println("To go back to the main menu press 'y'\nOr else press any key to exit");
		switchOptionInput = in.nextLine();

		if (switchOptionInput.toLowerCase().equals("y")) {
			switchOption = true;
		} else {
			switchOption = false;
			System.out.println("\nExiting...\n");
		}
		return switchOption;
	}

	private static void getStud(Scanner in, String sid) {

	}

	private static String newStud(Scanner in, String opt) {
		String sid;
		String email;
		String f_name;
		String l_name;
		String address;
		String date;
		Date dob;
		System.out.println("Enter First Name of the student: ");
		f_name = in.nextLine();
		System.out.println("Enter Last Name of the student: ");
		l_name = in.nextLine();
		System.out.println("Enter id of the student: ");
		sid = in.nextLine();
		System.out.println("Enter Email of the student: ");
		email = in.nextLine();
		System.out.println("Enter Address of the student: ");
		address = in.nextLine();

		while (true) {
			try {
				System.out.println("Enter date of birth of the student(yyyy-dd-mm): ");
				date = in.nextLine();
				dob = Date.valueOf(date);
				System.out.println("Date is: " + dob);

				break;
			} catch (java.lang.IllegalArgumentException e) {
				System.out.println("Please enter a valid date in the format yyyy-dd-mm");
			}
		}
		// catch (java.lang.IllegalArgumentException e) {

		System.out.println("Press 'y' to enter more");
		opt = in.nextLine();

		return opt;

	}

}
