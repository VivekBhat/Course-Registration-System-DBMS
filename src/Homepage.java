import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class Homepage {

	public static final Connection conn = JDBC_Connection.makeConnection();
	public static final Scanner in = new Scanner(System.in);
	public static ResultSet rs;
	public static PreparedStatement preparedStatement;
	public static CallableStatement callableStatement;
	public static String query;
	public static final String CURR_SESSION = "Spring 2017";
	public static String studentName, adminName, adminUsername, studentId, studentClassLevel;
	private static final HashMap<String, Float> grades = new HashMap<>();

	public static void printWelcome() {
		System.out.println();
		System.out.println("***************************************");
		System.out.println("Welcome to CSC 540 Project Spring 2017");
		System.out.println("***************************************");
		System.out.println();
	}

	/*
	 * 03/30/2017 dsuri - Danish Suri This method provides common login
	 * interface for both Admin and Student
	 */
	public static void loginUser() {
		System.out.println("Enter Username:");
		String username = in.next();
		System.out.println("Enter password:");
		String password = in.next();
		String name = "";
		String stu_id = "";
		boolean admin = false;
		query = "Select * from ADMINISTRATOR WHERE USERNAME=? and PASSWORD=?";
		try {
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				admin = true;
				name = rs.getString("F_NAME") + " " + rs.getString("L_NAME");
			} else {
				query = "Select * from STUDENT WHERE USERNAME=? and PASSWORD=?";
				preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				rs = preparedStatement.executeQuery();

				if (rs.next()) {
					admin = false;
					name = rs.getString("F_NAME") + " " + rs.getString("L_NAME");
					stu_id = rs.getString("student_id");
					studentClassLevel = rs.getString("class_level");
				} else {
					System.out.println("Invalid Username or Password Try Again");
					loginUser();
				}
			}
			if (admin) {
				adminUsername = username;
				adminName = name;
				welcomeAdmin(name, username);
			} else {
				studentId = stu_id;
				studentName = name;
				welcomeStudent(name, stu_id);
			}
			// else
			// student home page

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Hello Exception");
		}

	}

	/*
	 * 03/30/2017 dsuri - Danish Suri This method is used by Admin to add New
	 * Student in the Database
	 */
	public static void enterNewStudent() throws Exception {
		System.out.println("*********************");
		System.out.println("Enter a new Student");
		System.out.println("**********************");
		System.out.print("Student ID: ");
		String student_id = in.next();
		System.out.print("Username: ");
		String username = in.next();
		System.out.print("First Name: ");
		String f_name = in.next();
		System.out.print("Last Name: ");
		String l_name = in.next();
		System.out.print("GPA: ");
		float gpa = in.nextFloat();
		in.nextLine();
		System.out.print("Email ID: ");
		String email = in.next();
		System.out.print("Password: ");
		String password = in.next();

		System.out.print("Residency: ");
		String residency = getResidency();

		System.out.print("Classification Level: ");
		String class_level = getClassificationLevel();

		System.out.print("Department: ");
		String dept = in.next();

		System.out.print("Date of Birth(yyyy-mm-dd): ");
		String date = in.next();

		Date dob = Date.valueOf(date);
		// System.out.println("Please enter a valid input in yyyy-mm-dd
		// format");
		// System.out.println(dob);

		query = "insert into STUDENT(STUDENT_ID,F_NAME, l_name, gpa, email, password, "
				+ "current_credits,residency, class_level, department, "
				+ "pending_bill, username, dob) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		preparedStatement.setString(12, username);
		preparedStatement.setDate(13, dob);
		if (preparedStatement.executeUpdate() == 1) {
			System.out.println("Student Entered Successfully");
			welcomeAdmin(adminName, adminUsername);
		} else {
			System.out.println("Error Encountered");
		}

	}

	/*
	 * 03/30/2017 dsuri - Danish Suri This method is used by Admin to view
	 * details of a Student using their Student ID
	 */
	public static void viewStudent() throws Exception {
		System.out.println("*********************");
		System.out.println("Search Student");
		System.out.println("*********************");
		System.out.print("Enter Student ID: ");
		String stu_id = in.next();
		// query = "SELECT s.f_name, s.l_name, s.DOB,
		// s.EMAIL,e.course_id,e.grade " + "FROM enrollment e, student s "
		// + "WHERE s.STUDENT_ID=? and s.STUDENT_ID=e.STUDENT_ID";
		query = "SELECT f_name, l_name, DOB, EMAIL, residency, class_level, gpa, pending_bill " + "FROM student "
				+ "WHERE STUDENT_ID=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, stu_id);
		rs = preparedStatement.executeQuery();
		if (rs.next()) {
			String fname = rs.getString("f_name");
			String lname = rs.getString("l_name");
			String email = rs.getString("email");
			String dob = rs.getDate("dob").toString();
			String resi = rs.getString("residency");
			String class_level = rs.getString("class_level");
			String gpa = rs.getFloat("gpa") + "";
			int pendingBill = rs.getInt("pending_bill");
			System.out.println("First Name: " + fname);
			System.out.println("Last Name: " + lname);
			System.out.println("D.O.B (mm-dd-yyyy): " + dob);
			System.out.println("Student's Level: " + class_level);
			System.out.println("Residency: " + resi);
			if (pendingBill > 0)
				System.out.println("Pending Bill: " + pendingBill);
			System.out.println("GPA: " + gpa);
			// String course_id = rs.getString("course_id");
			// String grade = rs.getString("grade");
			// System.out.println("Press 1 to Enter Grdaes for " + fname+"
			// "+lname + "\nPress 0 to exit");
			System.out.println("\n\nPress 0 to Exit");
			System.out.println("Press 1 to View/Add Grades");
			int input = in.nextInt();
			if (input == 0) {
				welcomeAdmin(adminName, adminUsername);
			} else if (input == 1)
				viewGrades(stu_id, fname + " " + lname);
			else {
				System.out.println("Invalid option");
				welcomeStudent(studentName, studentId);
			}

		} else {
			System.out.println("\n\nNo Such Student Exists");
			viewStudent();
		}
	}

	/*
	 * 03/30/2017 dsuri - Danish Suri This method is used by Admin to view a
	 * Student's grade
	 */

	public static void viewGrades(String studentId, String name) throws Exception {
		query = "SELECT e.course_id,e.grade " + "FROM enrollment e, student s "
				+ "WHERE s.STUDENT_ID=? and s.STUDENT_ID=e.STUDENT_ID";
		boolean flag = false;
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, studentId);
		rs = preparedStatement.executeQuery();
		System.out.println("Courses Enrolled by " + name);
		System.out.println(String.format("\n\n%-10s\t%-15s\t", "Course ID", "Letter Grade"));
		while (rs.next()) {
			flag = true;
			String course_id = rs.getString("course_id");
			String grade = rs.getString("grade");
			System.out.println(String.format("%-10s\t%-15s\t", course_id, grade));
		}
		if (!flag) {
			System.out.println("\n\nNo Courses Available");
			System.out.println("Press 0 to Go to previous menu");
		} else {
			System.out.println("\n\nPress 0 to Go to previous menu");
			System.out.println("Press 1 to enter grades");
		}
		int input = 0;
		try {
			input = in.nextInt();
		} catch (Exception e) {
			System.out.println("Invalid Option");
			viewGrades(studentId, name);
		}

		if (input == 0)
			viewStudent();
		else if (input == 1)
			enterGrades(studentId);
		else {
			System.out.println("Invalid Option");
			viewGrades(studentId, name);
		}
	}

	/*
	 * 03/30/2017 dsuri - Danish Suri This method is used by Admin to add Grades
	 * of a Student in the courses they have enrolled
	 */
	public static void enterGrades(String studentId) throws Exception {
		query = "SELECT e.course_id,e.grade " + "FROM enrollment e " + "WHERE e.STUDENT_ID = " + studentId;
		preparedStatement = conn.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		int num = 1;
		while (rs.next()) {
			String course_id = rs.getString("course_id");
			String grade = rs.getString("grade");
			System.out.println(String.format("%-2s\t%-6s\t%-30s\t", num++ + "", course_id, grade));
		}
		String grades[] = { "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-" };
		System.out.println("Enter Course ID for which you want to add Grades");
		String c_id = in.next();
		System.out.println("Select Grade for this Course");
		System.out.println(String.format("%-5s\t%-5s\t%-5s\n%-5s\t%-5s\t%-5s\n%-5s\t%-5s\t%-5s\n", "1.A+", "2.A",
				"3.A-", "4.B+", "5.B", "6.B-", "7.C+", "8.C", "9.C-"));
		int grade = in.nextInt();
		query = "update enrollment set grade=? where STUDENT_ID=? and COURSE_ID=?";
		preparedStatement = conn.prepareCall(query);
		preparedStatement.setString(1, grades[grade - 1]);
		preparedStatement.setString(2, studentId);
		preparedStatement.setString(3, c_id);
		if (preparedStatement.executeUpdate() == 1) {
			System.out.println("Grades Entered");
			updateGPA(studentId);
		} else {
			System.out.println("Error Entering Grades. Please Try Again");
		}
		viewStudent();

	}

	/*
	 * 03/30/2017 dsuri - Danish Suri This is helper method to update GPA
	 */

	public static void updateGPA(String studentID) throws Exception {
		float gpa, num = 0;
		int credits = 0;
		query = "SELECT GRADE, CREDITS FROM ENROLLMENT WHERE STUDENT_ID = ? AND GRADE <> 'NA'";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, studentID);
		rs = preparedStatement.executeQuery();
		while (rs.next()) {

			int c = rs.getInt("credits");
			float g = grades.get(rs.getString("grade"));
			num = num + (c * g);
			credits += c;
		}
		query = "UPDATE STUDENT SET GPA = ? WHERE STUDENT_ID = ?";
		preparedStatement = conn.prepareStatement(query);
		System.out.println(num + " " + credits);
		gpa = num / credits;
		preparedStatement.setFloat(1, gpa);
		preparedStatement.setString(2, studentID);
		if (preparedStatement.executeUpdate() == 1) {
			System.out.println("Update Successfull");
		} else {
			System.out.println("Error Updating GPA");
		}
	}

	/*
	 * 03/30/2017 dsuri - Danish Suri This method is used by Admin to view
	 * Pending Requests and Approve them
	 */
	public static void viewPendingRequests(String adminName, String userName) throws Exception {
		query = "SELECT * FROM PENDING_PERMISSIONS WHERE PERMISSION = 'Pending'";
		preparedStatement = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = preparedStatement.executeQuery();
		int num = 1;
		System.out.println(String.format("%-5s\t%-20s\t%-20s\t", "Row", "Student ID", "Course ID"));
		while (rs.next()) {
			String student_id = rs.getString("student_id");
			String course_id = rs.getString("course_id");
			System.out.println("*****************\n\n");
			System.out.println("Pending Requests");
			System.out.println("*****************\n");
			System.out.println(String.format("%-5s\t%-20s\t%-20s\t", num++ + "", student_id, course_id));

		}
		System.out.println("Enter Row number to Approve/Reject request");
		System.out.println("Or Press 0 to exit");
		int inp = in.nextInt();
		if (inp == 0) {
			welcomeAdmin(adminName, adminUsername);
		} else {
			rs.first();
			for (int i = 0; i < inp; i++) {
				if (rs.next()) {

				}
			}
			rs.previous();
			String student_id = rs.getString("student_id");
			String course_id = rs.getString("course_id");
			String result = "Pending";
			query = "UPDATE PENDING_PERMISSIONS SET PERMISSION = ? WHERE STUDENT_ID = ? AND COURSE_ID = ?";
			System.out.println("Press 1. To Approve Request");
			System.out.println("Press 2. To Reject Request");
			System.out.println("Press 0. To Go Back");
			inp = in.nextInt();
			switch (inp) {
			case 1:
				result = "Approved";
				break;

			case 2:
				result = "Rejected";
				break;

			case 0:
				// go to previous menu
				break;
			}
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, result);
			preparedStatement.setString(2, student_id);
			preparedStatement.setString(3, course_id);
			if (preparedStatement.executeUpdate() == 1) {
				System.out.println("Success");
			} else {
				System.out.println("Fail");
			}
		}
	}

	/*
	 * 03/30/2017 dsuri - Danish Suri Helper Method to fetch Classification
	 * Level
	 */
	private static String getClassificationLevel() {
		System.out.println("Enter\n 1 for Undergraduate \n 2 for Graduate");
		int opt = in.nextInt();
		String class_level = "Undergraduate";
		switch (opt) {
		case 1:
			class_level = "Undergraduate";
			break;
		case 2:
			class_level = "Graduate";
			break;
		default:
			System.out.println("Invalid input.\nPlease enter a valid input");
			getResidency();

		}

		return class_level;
	}

	/*
	 * 03/30/2017 dsuri - Danish Suri Helper Method to fetch Residency
	 */
	private static String getResidency() {
		System.out.println("Enter\n 1 for in-state\n 2 for out-state\n 3 for international");
		int opt = in.nextInt();
		String residency = "in-state";
		switch (opt) {
		case 1:
			residency = "in-state";
			break;
		case 2:
			residency = "out-state";
			break;
		case 3:
			residency = "international";
			break;
		default:
			System.out.println("Invalid input.\nPlease enter a valid input");
			getResidency();

		}

		return residency;
	}

	/*
	 * 03/30/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to view all courses
	 */
	private static void viewCourses(String adminName, String userName) {
		System.out.println("Displaying all courses:\n");
		try {
			String selectAllCourses = "Select * from Courses";
			preparedStatement = conn.prepareStatement(selectAllCourses);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String course_id = rs.getString("COURSE_ID");
				String title = rs.getString("TITLE");
				String class_level = rs.getString("CLASS_LEVEL");
				String department = rs.getString("DEPARTMENT");
				System.out.println(String.format("| %-10s\t | \t %-35s\t | \t %-5s |", course_id, title, department));
				// System.out.println(course_id +
				// "\t\t"+title+"\t\t"+class_level+"\t\t"+department);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Press 0 to Go Back to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			viewOrAddCourse(adminName, userName);
		}
	}

	/*
	 * 03/30/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to view a course by id
	 */
	private static void viewCoursesById(String adminName, String userName) {
		// System.out.println("Enter the Course ID");
		System.out.println("Displaying course by Course ID");
		System.out.println("Enter Course ID: ");
		String selectCID = "select * from courses where COURSE_ID = ?";
		String courseID = in.next().toUpperCase(); // TO DO: Input from user
		try {
			preparedStatement = conn.prepareStatement(selectCID, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, courseID);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String course_id = rs.getString("COURSE_ID");
				String title = rs.getString("TITLE");
				String class_level = rs.getString("CLASS_LEVEL");
				String department = rs.getString("DEPARTMENT");
				System.out.println(String.format("| %-5s\t | \t %-35s\t | \t %-5s |", course_id, title, department));
				// System.out.println(course_id +
				// "\t\t"+title+"\t\t"+class_level+"\t\t"+department);
			}
			rs.last();
			int curr = rs.getRow();
			if (curr < 1) {
				System.out.println("No course matching this Course_ID. Please try again");
				viewCoursesById(adminName, userName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Press 0 to Go Back to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			viewOrAddCourse(adminName, userName);
		}
	}

	/*
	 * 03/30/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to add a new course
	 */
	private static void addCourse(String adminName, String userName) {
		System.out.println("Adding new Course");
		System.out.println("Enter Course ID: ");
		String courseID = in.next(); // TO DO: Input from user
		System.out.println("Enter Course Title: ");
		String courseTitle = in.next(); // TO DO: Input from user
		System.out.println("Enter minimum credits: ");
		int minCredits = in.nextInt(); // TO DO: Input from user
		System.out.println("Enter maximum credits: ");
		int maxCredits = in.nextInt(); // TO DO: Input from user
		System.out.println("Enter minimum GPA required: ");
		float minGPA = in.nextFloat(); // TO DO: Input from user
		/* user input for class level */
		int classLevel = 0;
		while (true) {
			System.out.println("Enter Class Level: 1 for Graduate, 2 for Undergrad ");
			try {
				classLevel = in.nextInt(); // TO DO: Input from user
				if (classLevel != 1 && classLevel != 2) {
					throw new Exception();
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("Please enter valid Class Level");
			}
		}
		System.out.println("Enter Department: ");
		String dept = in.next(); // TO DO: Input from user
		/* user input special permission */

		String splPermStr = "";
		String preCID = "";
		float preCourseGrade = 0.0f;
		while (true) {
			System.out.println(
					"Enter Special Permission: 0 for None, 1 for Prerequisite course, 2 for Admin Permission, 3 for both");
			preCID = "";
			preCourseGrade = 0.0f;

			try {
				int splPerm = in.nextInt(); // TO DO: Input from user
				if (splPerm != 1 && splPerm != 2 && splPerm != 3 && splPerm != 0) {
					throw new Exception();
				} else {
					if (splPerm == 0) {
						splPermStr = "None";
					} else if (splPerm == 1) {
						System.out.println("Enter Prerequisite Course ID: ");
						preCID = in.next(); // TO DO: Input from user
						System.out.println("Enter prerequisite course grade requirement: ");
						preCourseGrade = in.nextFloat();
						splPermStr = "Prereq";

					} else if (splPerm == 2) {
						splPermStr = "SPP";
					} else {
						System.out.println("Enter Prerequisite Course ID: ");
						preCID = in.next(); // TO DO: Input from user
						System.out.println("Enter prerequisite course grade requirement: ");
						preCourseGrade = in.nextFloat();
						splPermStr = "SPPERM";
					}
					break;
				}
			} catch (Exception e) {
				System.out.println("Please enter valid Special Permission");
				System.out.println(
						"Enter Special Permission: 0 for None, 1 for Prerequisite course, 2 for Admin Permission, 3 for both");
			}
		}

		String insertCourseSQL = "insert into COURSES(COURSE_ID,TITLE,MIN_Credits,max_credits,min_gpa, class_level, department, special_perm) values (?,?,?,?,?,?,?,?) ";
		try {
			preparedStatement = conn.prepareStatement(insertCourseSQL);
			preparedStatement.setString(1, courseID);
			preparedStatement.setString(2, courseTitle);
			preparedStatement.setInt(3, minCredits);
			preparedStatement.setInt(4, maxCredits);
			preparedStatement.setFloat(5, minGPA);
			if (classLevel == 1) {
				preparedStatement.setString(6, "Graduate");
			} else if (classLevel == 1) {
				preparedStatement.setString(6, "Undergraduate");
			}
			preparedStatement.setString(7, dept);
			preparedStatement.setString(8, splPermStr);
			int result = preparedStatement.executeUpdate();
			System.out.println("Course added successfully");
			System.out.println();
			/* adding prerequisite course */
			if (preCID != null && preCID.length() > 0) {
				String insertPrerCourseSQL = "insert into PREREQ(PREREQ_CID,GRADE_REQ,COURSE_ID) values(?,?,?)";
				preparedStatement = conn.prepareStatement(insertPrerCourseSQL);
				preparedStatement.setString(1, preCID);
				preparedStatement.setFloat(2, preCourseGrade);
				preparedStatement.setString(3, courseID);
				int resultPreReq = preparedStatement.executeUpdate();
				if (resultPreReq == 1) {
					System.out.println("Prerequisite course added successfully");
				} else {
					System.out.println("Prerequisite course not able to add");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Press 0 to Go Back to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			viewOrAddCourse(adminName, userName);
		}
	}

	/*
	 * 04/03/2017 dsuri - Danish Suri This method is used Student to drop
	 * courses
	 */
	private static void dropCourse(String studentID, String courseID) throws Exception {

		query = "SELECT DROP_DEAD FROM SESSIONS WHERE SESSION_ID=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, CURR_SESSION);
		rs = preparedStatement.executeQuery();
		Date dead_date = null;
		if (rs.next())
			dead_date = rs.getDate("drop_dead");
		Date curr_date = new Date(System.currentTimeMillis());
		if (curr_date.after(dead_date)) {
			query = "DELETE FROM ENROLLMENT WHERE STUDENT_ID =? AND COURSE_ID = ?";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, studentID);
			preparedStatement.setString(2, courseID);
			if (preparedStatement.executeUpdate() == 1) {
				updateCredits(studentID);
				if (updateBill(studentID) == 1)
					System.out.println("Course Dropped Successfully");
				else
					System.out.println("Error Dropping Course");
			}
		} else {
			System.out.println("Drop Deadline has been passed");
			viewMyCourses(studentId);
		}
		System.out.println("Press 0 to go back to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			viewMyCourses(studentId);
		} else {
			System.out.println("Invalid Option");
			viewMyCourses(studentId);
		}

	}

	/*
	 * 04/03/2017 dsuri - Danish Suri This is a helper method to update current
	 * credits of a student
	 */

	private static void updateCredits(String studentId) throws Exception {
		query = "SELECT CREDITS FROM ENROLLMENT WHERE STUDENT_ID =? AND STATUS='Enrolled' AND SESSION_ID =?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, studentId);
		preparedStatement.setString(2, CURR_SESSION);
		rs = preparedStatement.executeQuery();
		int credits = 0;
		while (rs.next()) {
			credits += rs.getInt("CREDITS");
		}

		query = "UPDATE STUDENT SET CURRENT_CREDITS = ? WHERE STUDENT_ID=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(2, studentId);
		preparedStatement.setInt(1, credits);
		if (preparedStatement.executeUpdate() == 1) {
			return;
		}
		System.out.println("Error Updating Credits");

	}

	/*
	 * 03/31/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to view a course offering
	 */
	private static void viewCourseOffering(String adminName, String userName) {
		System.out.println("Displaying all course offerings:\n");
		try {
			String selectAllCourses = "Select * from COURSE_OFFERED";
			preparedStatement = conn.prepareStatement(selectAllCourses);
			rs = preparedStatement.executeQuery();
			System.out.println(String.format("| %-10s\t | \t %-10s\t | \t %-15s | \t %-10s | \t %-10s | \t %-10s |",
					"COURSE_ID", "INSTRUCTOR", "TERM", "SCHEDULE", "CAPACITY", "WAITLIST"));

			while (rs.next()) {
				String courseId = rs.getString("COURSE_ID");
				String instrId = rs.getString("INSTR_ID");
				String sessionId = rs.getString("SESSION_ID");
				int scheduleId = rs.getInt("SCHEDULE_ID");
				int maxNumOfStudents = rs.getInt("MAX_STUDENT");
				int waitlistCount = rs.getInt("WAITLIST");
				System.out.println(String.format("| %-10s\t | \t %-10s\t | \t %-15s | \t %-10s | \t %-10s | \t %-10s |",
						courseId, instrId, sessionId, scheduleId, maxNumOfStudents, waitlistCount));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Press 0 to Go Back to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			viewAddCoursesOffering(adminName, userName);
		}
	}

	/*
	 * 03/31/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to add a new course offering
	 */
	private static void addCourseOffering(String adminName, String userName) {
		System.out.println("Adding new Course Offering");
		// creating schedule
		System.out.println("Please add schedule:");
		System.out.println("Enter Day1: 1 for Mon, 2 for Tues, 3 for Wed, 4 for Thu, 5 for Fri");
		String day1 = getDayInput("Day1", false);
		System.out.println("Enter Day2: 0 for null, 1 for Mon, 2 for Tues, 3 for Wed, 4 for Thu, 5 for Fri");
		String day2 = getDayInput("Day2", true);

		System.out.println("Enter Start Time in 24 hour (hh:mm) format:");
		String startTime = getTimeInput("Start");
		System.out.println("Enter End Time in 24 hour (hh:mm) format:");
		String endTime = getTimeInput("End");
		System.out.println("Enter Location:");
		String location = in.next();
		System.out.println("Enter Course ID: ");
		String courseId = in.next(); // TO DO: Input from user
		String createScheduleSQL = null;
		if (day2 == "null") {
			createScheduleSQL = "insert into schedule(DAY,start_time,end_time,location,course_id,session_id) values(?,?,?,?,?,?)";
		} else {
			createScheduleSQL = "insert into schedule(DAY,start_time,end_time,location,course_id,session_id,DAY2) values(?,?,?,?,?,?,?)";
		}
		try {
			preparedStatement = conn.prepareStatement(createScheduleSQL);
			preparedStatement.setString(1, day1);
			preparedStatement.setString(2, startTime);
			preparedStatement.setString(3, endTime);
			preparedStatement.setString(4, location);
			preparedStatement.setString(5, courseId);
			preparedStatement.setString(6, CURR_SESSION);
			if (day2 != "null") {
				preparedStatement.setString(7, day2);
			}
			int result = preparedStatement.executeUpdate();
			// System.out.println("insert course result = " + result);
			if (result == 1) {
				System.out.println("Schedule added successfully");
			} else {
				System.out.println("Error in adding schedule");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// creating schedule -end
		System.out.println("Enter Instructor ID: ");
		String instrId = in.next(); // TO DO: Input from user
		// System.out.println("Enter Session ID: ");
		// String sessionId = in.next(); // TO DO: Input from user
		// System.out.println("Enter Schedule ID: ");
		// String scheduleId = in.next(); // TO DO: Input from user
		System.out.println("Enter Max Capacity: ");
		int capacity = in.nextInt(); // TO DO: Input from user
		System.out.println("Enter Current Waitlist Count: ");
		int waitlistCnt = in.nextInt(); // TO DO: Input from user
		String insertCourseOfferingSQL = "insert into COURSE_OFFERED(COURSE_ID,INSTR_ID,session_id,schedule_id,max_student,waitlist) values(?,?,?,(select max(SCHEDULE_ID) from SCHEDULE),?,?)";
		try {
			preparedStatement = conn.prepareStatement(insertCourseOfferingSQL);
			preparedStatement.setString(1, courseId);
			preparedStatement.setString(2, instrId);
			preparedStatement.setString(3, CURR_SESSION);
			preparedStatement.setInt(4, capacity);
			preparedStatement.setInt(5, waitlistCnt);
			int result = preparedStatement.executeUpdate();
			if (result == 1) {
				System.out.println("Course Offering added successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Press 0 to Go Back to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			viewAddCoursesOffering(adminName, userName);
		}
	}

	/*
	 * 03/31/2017 - Jitin Kumar This method shall be used to input time in 24
	 * hour format
	 */
	private static String getTimeInput(String displayStr) {
		String time = null;
		while (true) {
			try {
				time = in.next();
				if (!time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
					throw new Exception();
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("Enter " + displayStr + " Time in 24 hour (hh:mm) format:");
			}
		}
		return time;
	}

	/*
	 * 03/31/2017 - Jitin Kumar This method shall be used to input day
	 */
	private static String getDayInput(String displayStr, boolean isNullAllowed) {
		int day = 0;
		while (true) {
			try {
				// System.out.println("in try again");
				// in.next();
				day = Integer.parseInt(in.next());
				if (isNullAllowed) {
					if (day < 0 || day > 5) {
						throw new Exception();
					} else {
						break;
					}
				} else {
					if (day < 1 || day > 5) {
						throw new Exception();
					} else {
						break;
					}
				}

			} catch (Exception e) {
				System.out.println("Please input valid Day");
				if (isNullAllowed) {
					System.out.println("Enter " + displayStr
							+ ": 0 for null, 1 for Mon, 2 for Tues, 3 for Wed, 4 for Thu, 5 for Fri");
				} else {
					System.out.println(
							"Enter " + displayStr + ": 1 for Mon, 2 for Tues, 3 for Wed, 4 for Thu, 5 for Fri");
				}
			}
		}
		if (day == 0) {
			return "null";
		} else if (day == 1) {
			return "Mon";
		} else if (day == 2) {
			return "Tue";
		} else if (day == 3) {
			return "Wed";
		} else if (day == 4) {
			return "Thu";
		} else if (day == 5) {
			return "Fri";
		} else {
			return null;
		}
	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Admin to view their
	 * Profile
	 */
	public static void viewAdminProfile(String adminName, String adminUsername) throws Exception {
		query = "select f_name,L_NAME,EMAIL,ADDRESS,DOB from ADMINISTRATOR where username=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, adminName);
		rs = preparedStatement.executeQuery();
		if (rs.next()) {
			String name = rs.getString("f_name") + " " + rs.getString("l_name");
			System.out.println("Name: " + name);
			System.out.println("Email: " + rs.getString("email"));
			System.out.println("Address: " + rs.getString("address"));
			System.out.println("D.O.B: " + rs.getDate("dob"));
			System.out.println("\nPress 0 to Go Back");
			System.out.println("\nPress 1 to Edit Profile");
			int i = in.nextInt();
			if (i == 1) {
				editAdminProfile(adminName);
			} else if (i == 0) {
				welcomeAdmin(name, adminName);
			} else {
				System.out.println("Invalid Input");
				viewAdminProfile(adminName, adminUsername);
			}
		} else {
			System.out.println("No Records Found");
			welcomeAdmin("", adminName);
		}
	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Admin to edit their
	 * Profile
	 */
	public static void editAdminProfile(String adminUsername) throws Exception {
		System.out.print("Enter First Name: ");
		in.nextLine();
		String fname = in.nextLine();

		System.out.print("Enter Last Name: ");
		String lname = in.nextLine();

		System.out.print("Enter Email Address: ");
		String email = in.nextLine();

		System.out.print("Enter Address: ");
		String addr = in.nextLine();

		query = "UPDATE ADMINISTRATOR SET F_NAME=?, L_NAME=?, EMAIL =?, ADDRESS=? WHERE username = ?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, fname);
		preparedStatement.setString(2, lname);
		preparedStatement.setString(3, email);
		preparedStatement.setString(4, addr);
		preparedStatement.setString(5, adminUsername);
		if (preparedStatement.executeUpdate() == 1) {
			System.out.println("Details Updated Successfully\n\n");
			viewAdminProfile(adminUsername, null);
		} else {
			System.out.println("Error Updating Table. Please Try Again Later\n\n");
			viewAdminProfile(adminUsername, null);
		}

	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Student to view
	 * their Profile
	 */
	public static void viewStudentProfile(String student_id) throws Exception {
		query = "select f_name,L_NAME,GPA,EMAIL,DEPARTMENT from student where student_id=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, student_id);
		rs = preparedStatement.executeQuery();
		System.out.println("***************************************");
		if (rs.next()) {
			System.out.println("Name: " + rs.getString("f_name") + " " + rs.getString("l_name"));
			System.out.println("GPA: " + rs.getFloat("gpa") + "");
			System.out.println("Email: " + rs.getString("email"));
			// System.out.println("Address: "+rs.getString("Address"));
			System.out.println("Department: " + rs.getString("department"));
		}
		System.out.println("***************************************\n\n");
		System.out.println("Press 1 to Edit Profile");
		System.out.println("Press 0 to Go Back");
		int ip = in.nextInt();
		if (ip == 1) {
			updateStudentProfile(student_id);
		} else if (ip == 0) {
			welcomeStudent(studentName, student_id);
		} else {
			System.out.println("Invalid Option");
			viewStudentProfile(student_id);
		}

	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Student to edit
	 * their Profile
	 */
	public static void updateStudentProfile(String student_id) throws Exception {
		System.out.print("Enter First Name: ");
		String fname = in.next();

		System.out.print("Enter Last Name: ");
		String lname = in.next();

		System.out.print("Enter Email Address: ");
		String email = in.next();

		query = "UPDATE STUDENT SET F_NAME=?, L_NAME=?, EMAIL =? WHERE STUDENT_ID = ?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, fname);
		preparedStatement.setString(2, lname);
		preparedStatement.setString(3, email);
		preparedStatement.setString(4, student_id);
		if (preparedStatement.executeUpdate() == 1) {
			System.out.println("Details Updated Successfully\n\n");
			viewStudentProfile(student_id);
		} else {
			System.out.println("Error Updating Table. Please Try Again Later\n\n");
			viewStudentProfile(student_id);
		}
	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Student to view
	 * their Enrolled Courses
	 */
	public static void viewMyCourses(String student_id) throws Exception {

		query = "SELECT c.course_id,c.title,e.grade,i.instr_name, e.session_id "
				+ "FROM enrollment e,student s, courses c, instructor i " + "WHERE s.STUDENT_ID=? "
				+ "and s.STUDENT_ID=e.STUDENT_ID " + "and e.status = 'Enrolled' " + "and c.course_id = e.course_id "
				+ "and i.instr_id = e.instr_id";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, student_id);
		rs = preparedStatement.executeQuery();

		// Formatting Output
		for (int i = 0; i < 150; i++) {
			System.out.print("_");
		}
		System.out.println("");
		System.out.println(String.format("%-10s\t%-40s\t%-10s\t%-30s\t%-15s", "Course ID", "Course Name", "Grade",
				"Instructor Name", "Session"));
		for (int i = 0; i < 99; i++) {
			System.out.print("_");
		}
		System.out.println("");
		// Formatting Ends here

		while (rs.next()) {
			String course_name = rs.getString("title");
			String grade = rs.getString("grade");
			String instr_name = rs.getString("instr_name");
			String session = rs.getString("session_id");
			String cid = rs.getString("course_id");
			System.out.println(
					String.format("%-10s\t%-40s\t%-10s\t%-30s\t%-15s\n", cid, course_name, grade, instr_name, session));
		}
		System.out.println("\n\n");
		System.out.println("Press 1. Drop a course");
		System.out.println("Press 0  to go back");
		int inps = in.nextInt();
		if (inps == 1) {
			in.nextLine();
			System.out.println("Enter Course ID: ");
			String cid = in.next();
			dropCourse(student_id, cid);
		} else
			welcomeStudent(studentName, student_id);
	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Student to view all
	 * current courses which are being offered
	 */
	public static void viewAllCurrentCourses() throws Exception {
		query = "select c.course_id,c.title, i.instr_name,i.instr_id, co.max_student, "
				+ "sc.day, sc.day2, sc.start_time, sc.end_time, " + "sc.location "
				+ "from COURSE_OFFERED co, instructor i, courses c, schedule sc " + "where co.session_id = ? and "
				+ "co.course_id = c.course_id and " + "i.instr_id = co.instr_id and "
				+ "co.schedule_id = sc.schedule_id " + "and c.class_level = ?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, CURR_SESSION);
		preparedStatement.setString(2, studentClassLevel);
		rs = preparedStatement.executeQuery();
		for (int i = 0; i < 170; i++) {
			System.out.print("_");
		}
		System.out.println("");
		System.out.println(String.format("%-15s\t%-40s\t%-30s\t%-15s\t%-10s\t%-10s\t%-20s\t%-20s\t%-10s", "Course ID",
				"Title", "Instructor(ID)", "Max-Students", "Day 1", "Day 2", "Start Time", "End Time", "Location"));
		for (int i = 0; i < 170; i++) {
			System.out.print("_");
		}
		System.out.println("");
		while (rs.next()) {
			String title = rs.getString("title");
			String instr_name = rs.getString("instr_name") + "(" + rs.getString("instr_id") + ")";
			String max_student = rs.getString("max_student");
			String day = rs.getString("day");
			String day2 = rs.getString("day2");
			String start_time = rs.getString("start_time");
			String end_time = rs.getString("end_time");
			String loc = rs.getString("location");
			String course_id = rs.getString("course_id");
			System.out.println(String.format("%-15s\t%-40s\t%-30s\t%-15s\t%-10s\t%-10s\t%-20s\t%-20s\t%-10s\n",
					course_id, title, instr_name, max_student, day, day2, start_time, end_time, loc));
		}

		System.out.println("Press 0 to go to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			welcomeStudent(studentName, studentId);
		} else {
			viewAllCurrentCourses();
		}

	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Student to view
	 * their Grades and GPA
	 */
	public static void displayGrades(int option, String student_id) throws Exception {
		if (option == 1) {
			query = "select e.course_id,e.grade " + "from enrollment e, student s "
					+ "where s.STUDENT_ID=? and s.STUDENT_ID=e.STUDENT_ID";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, student_id);
			rs = preparedStatement.executeQuery();
			System.out.println(String.format("\n%-10s\t%-7s", "Course ID", "Grade"));
			while (rs.next()) {
				String course_id = rs.getString("course_id");
				String grade = rs.getString("grade");
				System.out.println(String.format("%-10s\t%-7s\n", course_id, grade));
			}
		} else if (option == 2) {
			query = "select GPA from student where student_id=?";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, student_id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				System.out.println("Current GPA: " + rs.getFloat("gpa"));
			}
		} else {
			System.out.println("Invalid Option");
		}
		System.out.println("Press 0 to go back to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			welcomeStudent(studentName, student_id);
		} else {
			System.out.println("Invalid Option");
			displayGrades(option, student_id);
		}
	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Student to view
	 * their Pending Bill and to Pay Bill
	 */
	public static void viewBill(String student_id) throws Exception {
		query = "select pending_bill from student where student_id =?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, student_id);
		rs = preparedStatement.executeQuery();
		rs.next();
		int amt = rs.getInt("pending_bill");
		System.out.println("Pending Bill: " + amt);
		if (amt > 0) {
			System.out.println("Press 1 to pay bill");
			System.out.println("Press 0 to go back to previous menu");
			int inp = in.nextInt();
			if (inp == 1) {
				System.out.println("Enter Amount");
				int pay_amt = in.nextInt();
				if (pay_amt <= amt) {
					amt = amt - pay_amt;
					query = "update student set pending_bill=? where STUDENT_ID=?";
					preparedStatement = conn.prepareStatement(query);
					preparedStatement.setInt(1, amt);
					preparedStatement.setString(2, student_id);
					if (preparedStatement.executeUpdate() == 1) {
						System.out.println("Payment Successfull");
					} else {
						System.out.println("Payment Error");
						viewBill(student_id);
					}
				} else {
					System.out.println("Invalid Amount Entered");
					viewBill(student_id);
				}
			} else if (inp == 0)
				welcomeStudent(studentName, student_id);
		}

	}

	/*
	 * 04/1/2017 dsuri - Danish Suri This is a helper method used to check if
	 * Student should be in waitlist, enroll or rejected
	 */

	public static void checkEnrollmentStatus(String courseId, String studentId, String instrId) throws Exception {
		int count = 0, total = 0, waitlist = 0;
		query = "SELECT COUNT(*) FROM ENROLLMENT WHERE COURSE_ID = ? AND SESSION_ID = ?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, courseId);
		preparedStatement.setString(2, CURR_SESSION);
		rs = preparedStatement.executeQuery();
		if (rs.next()) {
			count = rs.getInt("count(*)");
		}
		query = "SELECT MAX_STUDENT, WAITLIST FROM COURSE_OFFERED WHERE COURSE_ID =?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, courseId);
		rs = preparedStatement.executeQuery();
		if (rs.next()) {
			total = rs.getInt("max_student");
			waitlist = rs.getInt("waitlist");
		}

		if (waitlist != 0 && total != 0) {
			if (count == waitlist + total) {
				System.out.println("No More Seats Available in this Course");
			} else if (count < waitlist + total) {
				query = "insert into ENROLLMENT(STUDENT_ID,COURSE_ID,instr_id,grade,session_id,status,credits) values(?,?,?,?,?,?,?)";
				preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, studentId);
				preparedStatement.setString(2, courseId);
				preparedStatement.setString(3, instrId);
				preparedStatement.setString(4, "NA");
				preparedStatement.setInt(7, 3);
				preparedStatement.setString(5, CURR_SESSION);
				if (count >= total) {
					System.out.println("No More Seats Available.\n");
					System.out.println("Press 1. to enroll into Waitlist");
					System.out.println("Press 0 to go back to previous menu");
					int i = in.nextInt();
					if (i == 1) {
						preparedStatement.setString(6, "Waitlist");
						if (preparedStatement.executeQuery() != null) {
							System.out.println("Added to Course Waitlist");
						} else {
							System.out.println("Error Adding Course");
						}
					} else if (i == 0) {
						welcomeStudent("Student", studentId);
					} else {
						System.out.println("Inavlid Input");
						welcomeStudent("Student", studentId);
					}

				} else {
					preparedStatement.setString(6, "Enrolled");
					if (preparedStatement.executeQuery() != null) {
						if (updateBill(studentId) == 1)
							System.out.println("Enrollment Successfull");
					} else
						System.out.println("Error Adding Course");
				}
			}
		}
	}

	/*
	 * 04/03/2017 dsuri - Danish Suri This is a helper method to update bill
	 */

	public static int updateBill(String studentId) throws Exception {
		query = " UPDATE STUDENT s " + "Set s.PENDING_BILL = " + "(SELECT COST_CREDIT FROM BILLING "
				+ "WHERE CLASS_LEVEL= s.CLASS_LEVEL and RESIDENCY = s.RESIDENCY)*s.CURRENT_CREDITS "
				+ "WHERE s.STUDENT_ID = ?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, studentId);
		return preparedStatement.executeUpdate();
	}

	/*
	 * 03/31/2017 dsuri - Danish Suri This method is used by Student to view
	 * their Pending Course Permission Requests Status
	 */
	public static void viewPendingCourses(String student_id) throws Exception {
		query = "SELECT * FROM PENDING_PERMISSIONS WHERE STUDENT_ID = ? and PERMISSION = 'Pending' OR PERMISSION = 'Rejected'";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, student_id);
		rs = preparedStatement.executeQuery();
		System.out.println(String.format("\n%-10s\t%-15s", "Course ID", "Status"));
		while (rs.next()) {
			String course_id = rs.getString("course_id");
			String status = rs.getString("permission");
			System.out.println(String.format("%-10s\t%-7s\n", course_id, status));
		}

		System.out.println("Press 0 to go back to previous menu");
		int i = in.nextInt();
		if (i == 0) {
			welcomeStudent(studentName, student_id);
		} else {
			System.out.println("Invalid Option Selected");
			viewPendingCourses(student_id);
		}
	}

	/*
	 * 04/3/2017 dsuri - Danish Suri Used by admin to enforce drop deadline
	 */
	public static void enforceDropDeadline() throws Exception {
		System.out.println("\n\nAre you sure you want to enforce drop deadline? y/n");
		String s = in.next();
		if (s.equalsIgnoreCase("y")) {
			query = "DELETE FROM ENROLLMENT WHERE STATUS = 'Waitlist'";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.executeUpdate();

			query = "DELETE " + "FROM ENROLLMENT " + "WHERE STUDENT_ID "
					+ "IN (SELECT STUDENT_ID FROM STUDENT WHERE PENDING_BILL > 0)";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.executeQuery();
			System.out.println("Enforcement Successfull");
			welcomeAdmin(adminName, adminUsername);

		} else {
			welcomeAdmin(adminName, adminUsername);
		}
	}

	/*
	 * 03/30/2017 dsuri - Danish Suri Helper Method called after Admin Logins
	 */
	public static void welcomeAdmin(String adminName, String userName) {
		System.out.println();
		System.out.println("***************************************");
		System.out.println("Welcome " + adminName);
		System.out.println("***************************************");
		System.out.println();
		System.out.println("Please choose an option:");
		System.out.println("1 to enter new student data");
		System.out.println("2 to get Student Info");
		System.out.println("3 to View or Add Course");
		System.out.println("4 to View or Add Course Offering");
		System.out.println("5 to View Pending Requests");
		System.out.println("6 to View/Edit Profile ");
		System.out.println("7 to Enforce Drop Deadline ");
		System.out.println("0 to Exit");
		int option = in.nextInt();
		switch (option) {
		case 1:
			try {
				enterNewStudent();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				viewStudent();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 3:
			viewOrAddCourse(adminName, userName);

			break;
		case 4:
			viewAddCoursesOffering(adminName, userName);

			break;
		case 5:
			try {
				viewPendingRequests(adminName, userName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 6:
			try {
				viewAdminProfile(userName, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		case 7:
			try {
				enforceDropDeadline();
			} catch (Exception e) {
				e.printStackTrace();
			}
		case 0:
			printWelcome();
			close(preparedStatement);
			close(rs);
			close(conn);

		default:
			System.out.println("Enter a valid option");
			welcomeAdmin(adminName, adminUsername);
		}
	}

	private static void viewAddCoursesOffering(String adminName, String userName) {
		try {
			System.out.println("Press 0 to Go Back to previous menu");
			System.out.println("Press 1 to View Course offering");
			System.out.println("Press 2 to Add Course Offering");
			int choice = in.nextInt();
			switch (choice) {
			case 0:
				welcomeAdmin(adminName, userName);

			case 1:
				viewCourseOffering(adminName, userName);
				break;
			case 2:
				addCourseOffering(adminName, userName);
				break;
			default:
				System.out.println("Please Enter a valid choice");
				welcomeAdmin(adminName, userName);
			}
		} catch (Exception e) {
			System.out.println("Error - Please Enter a valid choice!");
			welcomeAdmin(adminName, userName);
		}
	}

	private static void viewOrAddCourse(String adminName, String userName) {
		try {
			System.out.println("Press 0 to Go Back to previous menu");
			System.out.println("Press 1 to View All Courses");
			System.out.println("Press 2 to Search a Course by Course ID");
			System.out.println("Press 3 to Add a new Course");
			int choice = in.nextInt();
			switch (choice) {

			case 0:
				welcomeAdmin(adminName, userName);
			case 1:
				viewCourses(adminName, userName);
				break;
			case 2:
				viewCoursesById(adminName, userName);
				break;
			case 3:
				addCourse(adminName, userName);
				break;
			default:
				System.out.println("Please Enter a valid choice");
				welcomeAdmin(adminName, userName);
			}
		} catch (Exception e) {

		}

	}

	/*
	 * 03/31/2017 dsuri - Danish Suri Helper Method called after Student Logins
	 */
	public static void welcomeStudent(String studentName, String student_id) throws Exception {
		System.out.println();
		System.out.println("***************************************");
		System.out.println("Welcome " + studentName);
		System.out.println("***************************************");
		System.out.println();
		System.out.println("Please choose an option:");
		System.out.println("1 View Profile");
		System.out.println("2 View All Courses");
		System.out.println("3 Enroll Courses");
		System.out.println("4 View My Courses");
		System.out.println("5 View Pending Courses");
		System.out.println("6 View Grades");
		System.out.println("7 View/Pay Bill");
		System.out.println("0 To Exit\n\n");
		int input = in.nextInt();
		switch (input) {
		case 1:
			viewStudentProfile(student_id);
			break;

		case 2:
			viewAllCurrentCourses();
			break;

		case 3:
			enrollCourse(student_id);
			break;

		case 4:
			viewMyCourses(student_id);
			break;

		case 5:
			viewPendingCourses(student_id);
			break;

		case 6:
			System.out.println("Press 1 to View Letter Grades");
			System.out.println("Press 2 to View GPA");
			System.out.println("Press 0 to go back to previous menu");
			int opt = in.nextInt();
			if (opt == 0)
				welcomeStudent(studentName, student_id);

			displayGrades(opt, student_id);
			break;

		case 7:
			viewBill(student_id);
			break;

		case 0:
			printWelcome();
			close(preparedStatement);
			close(rs);
			close(conn);
			break;
		default:
			System.out.println("Enter a valid option");
			welcomeStudent(studentName, student_id);

		}
	}

	/*
	 * 02/04/2017 - Jitin Kumar This method shall be used to enroll the courses
	 */
	private static void enrollCourse(String studentId) {
		System.out.println("Enroll for new Course\n\n");
		System.out.println("Enter Course ID: ");
		String courseId = in.next(); // TO DO: Input from user
		System.out.println("Enter Instructor ID:");
		String instrID = in.next(); // TO DO: Input from user
		// checking conflicting schedule
		String query = "select * from schedule where course_id like ? and session_id like ?";
		try {
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, courseId);
			preparedStatement.setString(2, CURR_SESSION);
			rs = preparedStatement.executeQuery();
			double startTime_new = 0.0;
			double endTime_new = 0.0;
			String day1_new = null;
			String day2_new = null;

			while (rs.next()) {
				startTime_new = Double.parseDouble(rs.getString("start_time").replace(':', '.'));
				endTime_new = Double.parseDouble(rs.getString("end_time").replace(':', '.'));
				day1_new = rs.getString("day");
				day2_new = rs.getString("day2");
			}

			query = "select * from schedule where course_id in (select course_id from enrollment where student_id like ?) and session_id like ?";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, studentId);
			preparedStatement.setString(2, CURR_SESSION);

			rs = preparedStatement.executeQuery();
			double startTime = 0.0;
			double endTime = 0.0;
			String day1 = null;
			String day2 = null;
			String crsId;
			while (rs.next()) {
				startTime = Double.parseDouble(rs.getString("start_time").replace(':', '.'));
				endTime = Double.parseDouble(rs.getString("end_time").replace(':', '.'));
				crsId = rs.getString("course_id");
				day1 = rs.getString("day");
				day2 = rs.getString("day2");
				if (day1_new.equals(day1) || day2_new.equals(day1) || day1_new.equals(day2) || day2_new.equals(day2)) {
					if ((startTime_new < startTime && endTime_new > startTime)
							|| (startTime_new < endTime && endTime_new > endTime)) {
						System.out
								.println("Error: Not able to enroll as this course is conflicting with current course "
										+ courseId);
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		query = "{call isEnrollable(?,?,?,?,?,?,?,?,?)}";
		try {
			callableStatement = conn.prepareCall(query);
			callableStatement.setString(1, studentId);
			callableStatement.setString(2, courseId);
			callableStatement.setString(3, instrID);
			callableStatement.setString(4, " ");
			callableStatement.setString(5, CURR_SESSION);
			callableStatement.setString(6, " ");
			callableStatement.setInt(7, 3);
			callableStatement.registerOutParameter(8, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			int status = callableStatement.getInt(8);
			String outputStr = callableStatement.getString(9);
			if (status == 0) {
				checkEnrollmentStatus(courseId, studentId, instrID);
				updateCredits(studentId);
				updateBill(studentId);
			} else if (status == 1) {
				System.out.println("Error: could not register for the course");
				System.out.println(outputStr);
			} else if (status == 2) {
				System.out.println(outputStr);
			}
			System.out.println("Press 0 to Go Back to previous menu");
			int i = in.nextInt();
			if (i == 0) {
				welcomeStudent(studentName, studentId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initGrades() {
		try {
			query = "SELECT * FROM GRADING_SYSTEM";
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				grades.put(rs.getString("grade"), rs.getFloat("gpa"));
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		printWelcome();
		initGrades();
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
