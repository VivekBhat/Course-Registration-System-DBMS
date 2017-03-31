import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Homepage {
	public static final Connection conn = JDBC_Connection.makeConnection();
	public static final Scanner in = new Scanner(System.in);
	public static ResultSet rs;
	public static PreparedStatement preparedStatement;
	public static String query;
	public static final String CURR_SESSION = "Spring 2017";

	public static void printWelcome() {
		System.out.println("***************************************");
		System.out.println("Welcome to CSC 540 Project Spring 2017");
		System.out.println("***************************************");
	}

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
					name = rs.getString("F_NAME") + " "
							+ rs.getString("L_NAME");
					stu_id = rs.getString("student_id");
				} else {
					System.out
							.println("Invalid Username or Password Try Again");
					loginUser();
				}
			}
			if (admin)
				welcomeAdmin(name);
			else
				welcomeStudent(name, stu_id);
			// else
			// student home page

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Hello Exception");
		}

	}

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
		in.next();
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
		System.out.print("Date of Birth(yyyy-dd-mm): ");
		String date = in.next();
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
		if (preparedStatement.executeUpdate() == 1) {
			System.out.println("Student Entered Successfully");
		} else {
			System.out.println("Error Encountered");
		}

	}

	public static void viewStudent() throws Exception {
		System.out.println("*********************");
		System.out.println("Search Student");
		System.out.println("*********************");
		System.out.print("Enter Student ID: ");
		String stu_id = in.next();
		query = "SELECT s.f_name, s.l_name, s.DOB, s.EMAIL,e.course_id,e.grade "
				+ "FROM enrollment e, student s "
				+ "WHERE s.STUDENT_ID=? and s.STUDENT_ID=e.STUDENT_ID";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, stu_id);
		rs = preparedStatement.executeQuery();
		if (rs.next()) {
			String name = rs.getString("f_name") + " " + rs.getString("l_name");
			String email = rs.getString("email");
			String dob = rs.getDate("dob").toString();
			String course_id = rs.getString("course_id");
			String grade = rs.getString("grade");
			System.out.println(String.format("%-20s\t%-20s\t%-15s\t%-6s\t%-5s",
					name, email, dob, course_id, grade));
			System.out.println("Press 1 to Enter Grdaes for " + name
					+ "\nPress 0 to exit");
			int input = in.nextInt();
			if (input == 0)
				viewStudent();
			else if (input == 1)
				enterGrades(stu_id);
			else {
				System.out.println("Invalid option");
				viewStudent();
			}

		} else {
			System.out.println("No Such Student Exists");
		}
	}

	public static void enterGrades(String studentId) throws Exception {
		query = "SELECT e.course_id,e.grade " + "FROM enrollment e "
				+ "WHERE e.STUDENT_ID = " + studentId;
		preparedStatement = conn.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		int num = 1;
		while (rs.next()) {
			String course_id = rs.getString("course_id");
			String grade = rs.getString("grade");
			System.out.println(String.format("%-2s\t%-6s\t%-30s\t", num++ + "",
					course_id, grade));
		}
		String grades[] = { "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-" };
		System.out.println("Enter Course ID for which you want to add Grades");
		String c_id = in.next();
		System.out.println("Select Grade for this Course");
		System.out.println(String.format(
				"%-5s\t%-5s\t%-5s\n%-5s\t%-5s\t%-5s\n%-5s\t%-5s\t%-5s\n",
				"1.A+", "2.A", "3.A-", "4.B+", "5.B", "6.B-", "7.C+", "8.C",
				"9.C-"));
		int grade = in.nextInt();
		query = "update enrollment set grade=? where STUDENT_ID=? and COURSE_ID=?";
		preparedStatement = conn.prepareCall(query);
		preparedStatement.setString(1, grades[grade - 1]);
		preparedStatement.setString(2, studentId);
		preparedStatement.setString(3, c_id);
		if (preparedStatement.executeUpdate() == 1)
			System.out.println("Grades Entered");
		else
			System.out.println("Error Entering Grades. Please Try Again");
		viewStudent();

	}

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

		default:
			System.out.println("Invalid input.\nPlease enter a valid input");
			getResidency();

		}

		return class_level;
	}

	private static String getResidency() {
		System.out
				.println("Enter\n 1 for in-state\n 2 for out-state\n 3 for international");
		int opt = in.nextInt();
		String residency = "in-state";
		switch (opt) {
		case 1:
			residency = "in-state";
			break;
		case 2:
			residency = "out-state";
		case 3:
			residency = "international";
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
	private static void viewCourses() {
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
				System.out.println(String.format(
						"| %-10s\t | \t %-35s\t | \t %-5s |", course_id, title,
						department));
				// System.out.println(course_id +
				// "\t\t"+title+"\t\t"+class_level+"\t\t"+department);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn);
			close(preparedStatement);
			close(rs);
		}
	}

	/*
	 * 03/30/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to view a course by id
	 */

	private static void viewCoursesById() {
		// System.out.println("Enter the Course ID");
		System.out.println("Displaying course by Course ID");
		System.out.println("Enter Course ID: ");
		String selectCID = "select * from courses where COURSE_ID = ?";
		String courseID = in.next().toUpperCase(); // TO DO: Input from user
		try {
			preparedStatement = conn.prepareStatement(selectCID,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, courseID);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String course_id = rs.getString("COURSE_ID");
				String title = rs.getString("TITLE");
				String class_level = rs.getString("CLASS_LEVEL");
				String department = rs.getString("DEPARTMENT");
				System.out.println(String.format(
						"| %-5s\t | \t %-35s\t | \t %-5s |", course_id, title,
						department));
				// System.out.println(course_id +
				// "\t\t"+title+"\t\t"+class_level+"\t\t"+department);
			}
			rs.last();
			int curr = rs.getRow();
			if (curr < 1) {
				System.out
						.println("No course matching this Course_ID. Please try again");
				viewCoursesById();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 03/30/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to add a new course
	 */
	private static void addCourse() {
		System.out.println("Adding new Course");
		System.out.println("Enter Course ID: ");
		String courseID = in.next(); // TO DO: Input from user
		System.out.println("Enter Course Title: ");
		String courseTitle = in.next(); // TO DO: Input from user
		System.out.println("Enter minimum credits: ");
		float minCredits = in.nextInt(); // TO DO: Input from user
		System.out.println("Enter maximum credits: ");
		float maxCredits = in.nextInt(); // TO DO: Input from user
		System.out.println("Enter minimum GPA required: ");
		float minGPA = in.nextFloat(); // TO DO: Input from user
		/* user input for class level */
		int classLevel = 0;
		while (true) {
			System.out
					.println("Enter Class Level: 1 for Graduate, 2 for Undergrad ");
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
			System.out
					.println("Enter Special Permission: 0 for None, 1 for Prerequisite course, 2 for Admin Permission, 3 for both");
			preCID = "";
			preCourseGrade = 0.0f;

			try {
				int splPerm = in.nextInt(); // TO DO: Input from user
				if (splPerm != 1 && splPerm != 2 && splPerm != 3
						&& splPerm != 0) {
					throw new Exception();
				} else {
					if (splPerm == 0) {
						splPermStr = "None";
					} else if (splPerm == 1) {
						System.out.println("Enter Prerequisite Course ID: ");
						preCID = in.next(); // TO DO: Input from user
						System.out
								.println("Enter prerequisite course grade requirement: ");
						preCourseGrade = in.nextFloat();
						splPermStr = "Prereq";

					} else if (splPerm == 2) {
						splPermStr = "SPP";
					} else {
						System.out.println("Enter Prerequisite Course ID: ");
						preCID = in.next(); // TO DO: Input from user
						System.out
								.println("Enter prerequisite course grade requirement: ");
						preCourseGrade = in.nextFloat();
						splPermStr = "SPPERM";
					}
					break;
				}
			} catch (Exception e) {
				System.out.println("Please enter valid Special Permission");
				System.out
						.println("Enter Special Permission: 0 for None, 1 for Prerequisite course, 2 for Admin Permission, 3 for both");
			}
		}

		String insertCourseSQL = "insert into COURSES(COURSE_ID,TITLE,MIN_Credits,max_credits,min_gpa, class_level, department, special_perm) values (?,?,?,?,?,?,?,?) ";
		try {
			preparedStatement = conn.prepareStatement(insertCourseSQL);
			preparedStatement.setString(1, courseID);
			preparedStatement.setString(2, courseTitle);
			preparedStatement.setFloat(3, minCredits);
			preparedStatement.setFloat(4, maxCredits);
			preparedStatement.setFloat(5, minGPA);
			if (classLevel == 1) {
				preparedStatement.setString(6, "Graduate");
			} else if (classLevel == 1) {
				preparedStatement.setString(6, "Undergraduate");
			}
			preparedStatement.setString(7, dept);
			preparedStatement.setString(8, splPermStr);
			int result = preparedStatement.executeUpdate();
			System.out.println("insert course result = " + result);
			/* adding prerequisite course */
			if (preCID != null && preCID.length() > 0) {
				String insertPrerCourseSQL = "insert into PREREQ(PREREQ_CID,GRADE_REQ,COURSE_ID) values(?,?,?)";
				preparedStatement = conn.prepareStatement(insertPrerCourseSQL);
				preparedStatement.setString(1, preCID);
				preparedStatement.setFloat(2, preCourseGrade);
				preparedStatement.setString(3, courseID);
				int resultPreReq = preparedStatement.executeUpdate();
				if (resultPreReq == 1) {
					System.out
							.println("Prerequisite course added successfully");
				} else {
					System.out.println("Prerequisite course not able to add");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 03/31/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to view a course offering
	 */
	private static void viewCourseOffering() {
		System.out.println("Displaying all course offerings:\n");
		try {
			String selectAllCourses = "Select * from COURSE_OFFERED";
			preparedStatement = conn.prepareStatement(selectAllCourses);
			rs = preparedStatement.executeQuery();
			System.out
					.println(String
							.format("| %-10s\t | \t %-10s\t | \t %-15s | \t %-10s | \t %-10s | \t %-10s |",
									"COURSE_ID", "INSTRUCTOR", "TERM",
									"SCHEDULE", "CAPACITY", "WAITLIST"));

			while (rs.next()) {
				String courseId = rs.getString("COURSE_ID");
				String instrId = rs.getString("INSTR_ID");
				String sessionId = rs.getString("SESSION_ID");
				int scheduleId = rs.getInt("SCHEDULE_ID");
				int maxNumOfStudents = rs.getInt("MAX_STUDENT");
				int waitlistCount = rs.getInt("WAITLIST");
				System.out
						.println(String
								.format("| %-10s\t | \t %-10s\t | \t %-15s | \t %-10s | \t %-10s | \t %-10s |",
										courseId, instrId, sessionId,
										scheduleId, maxNumOfStudents,
										waitlistCount));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn);
			close(preparedStatement);
			close(rs);
		}
	}

	/*
	 * 03/31/2017 jkumar3 - Jitin Kumar This method shall be used by
	 * administrator to add a new course offering
	 */
	private static void addCourseOffering() {
		System.out.println("Adding new Course Offering");
		// creating schedule
		System.out.println("Please add schedule:");
		System.out
				.println("Enter Day1: 1 for Mon, 2 for Tues, 3 for Wed, 4 for Thu, 5 for Fri");
		String day1 = getDayInput("Day1", false);
		System.out
				.println("Enter Day2: 0 for null, 1 for Mon, 2 for Tues, 3 for Wed, 4 for Thu, 5 for Fri");
		String day2 = getDayInput("Day2", true);

		System.out.println("Enter Start Time in 24 hour format:");
		String startTime = getTimeInput("Start");
		System.out.println("Enter End Time in 24 hour format:");
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
		//System.out.println("Enter Session ID: ");
		// String sessionId = in.next(); // TO DO: Input from user
//		System.out.println("Enter Schedule ID: ");
//		String scheduleId = in.next(); // TO DO: Input from user
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
				System.out.println("Enter "+displayStr+" Time in 24 hour format:");
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
					System.out
							.println("Enter "
									+ displayStr
									+ ": 0 for null, 1 for Mon, 2 for Tues, 3 for Wed, 4 for Thu, 5 for Fri");
				} else {
					System.out
							.println("Enter "
									+ displayStr
									+ ": 1 for Mon, 2 for Tues, 3 for Wed, 4 for Thu, 5 for Fri");
				}
			}
		}
		if (day == 0)
			return "null";
		else if (day == 1)
			return "Mon";
		else if (day == 2)
			return "Tue";
		else if (day == 3)
			return "Wed";
		else if (day == 4)
			return "Thu";
		else if (day == 5)
			return "Fri";
		else
			return null;
	}

	public static void viewStudentProfile(String student_id) throws Exception {
		query = "select f_name,L_NAME,GPA,EMAIL,DEPARTMENT from student where student_id=?";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, student_id);
		rs = preparedStatement.executeQuery();
		System.out.println("***************************************");
		if (rs.next()) {
			System.out.println("Name: " + rs.getString("f_name") + " "
					+ rs.getString("l_name"));
			System.out.println("GPA: " + rs.getFloat("gpa") + "");
			System.out.println("Email: " + rs.getString("email"));
			// System.out.println("Address: "+rs.getString("Address"));
			System.out.println("Department: " + rs.getString("department"));
		}
		System.out.println("***************************************");

	}

	public static void viewMyCourses(String student_id) throws Exception {

		query = "SELECT c.title,e.grade,i.instr_name, e.session_id "
				+ "FROM enrollment e,student s, courses c, instructor i "
				+ "WHERE s.STUDENT_ID=? " + "and s.STUDENT_ID=e.STUDENT_ID "
				+ "and e.status = 'Enrolled' "
				+ "and c.course_id = e.course_id "
				+ "and i.instr_id = e.instr_id";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, student_id);
		rs = preparedStatement.executeQuery();

		// Formatting Output
		for (int i = 0; i < 99; i++)
			System.out.print("_");
		System.out.println("");
		System.out.println(String.format("%-20s\t%-10s\t%-30s\t%-15s",
				"Course Name", "Grade", "Instructor Name", "Session"));
		for (int i = 0; i < 99; i++)
			System.out.print("_");
		System.out.println("");
		// Formatting Ends here

		while (rs.next()) {
			String course_name = rs.getString("title");
			String grade = rs.getString("grade");
			String instr_name = rs.getString("instr_name");
			String session = rs.getString("session_id");
			System.out.println(String.format("%-20s\t%-10s\t%-30s\t%-15s\n",
					course_name, grade, instr_name, session));
		}
	}

	public static void viewAllCurrentCourses() throws Exception {
		query = "select c.title, i.instr_name, co.max_student, "
				+ "sc.day, sc.day2, sc.start_time, sc.end_time, "
				+ "sc.location "
				+ "from COURSE_OFFERED co, instructor i, courses c, schedule sc "
				+ "where co.session_id = ? and "
				+ "co.course_id = c.course_id and "
				+ "i.instr_id = co.instr_id and "
				+ "co.schedule_id = sc.schedule_id";
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, CURR_SESSION);
		rs = preparedStatement.executeQuery();
		for (int i = 0; i < 170; i++)
			System.out.print("_");
		System.out.println("");
		System.out.println(String.format(
				"%-20s\t%-20s\t%-15s\t%-10s\t%-10s\t%-20s\t%-20s\t%-10s",
				"Title", "Instructor", "Max-Students", "Day 1", "Day 2",
				"Start Time", "End Time", "Location"));
		for (int i = 0; i < 170; i++)
			System.out.print("_");
		System.out.println("");
		while (rs.next()) {
			String title = rs.getString("title");
			String instr_name = rs.getString("instr_name");
			String max_student = rs.getString("max_student");
			String day = rs.getString("day");
			String day2 = rs.getString("day2");
			String start_time = rs.getString("start_time");
			String end_time = rs.getString("end_time");
			String loc = rs.getString("location");
			System.out.println(String.format(
					"%-20s\t%-20s\t%-15s\t%-10s\t%-10s\t%-20s\t%-20s\t%-10s\n",
					title, instr_name, max_student, day, day2, start_time,
					end_time, loc));
		}
	}

	public static void displayGrades(int option, String student_id)
			throws Exception {
		if (option == 1) {
			query = "select e.course_id,e.grade "
					+ "from enrollment e, student s "
					+ "where s.STUDENT_ID=? and s.STUDENT_ID=e.STUDENT_ID";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, student_id);
			rs = preparedStatement.executeQuery();
			System.out.println(String.format("%-10s\t%-7s\n", "Course ID",
					"Grade"));
			while (rs.next()) {
				String course_id = rs.getString("course_id");
				String grade = rs.getString("grade");
				System.out.println(String.format("%-10s\t%-7s\n", course_id,
						grade));
			}
		} else if (option == 2) {
			query = "select GPA from student where student_id=?";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, student_id);
			rs = preparedStatement.executeQuery();
			System.out.println("Current GPA: " + rs.getFloat("gpa"));
		} else {
			System.out.println("Invalid Option");
		}
	}

	public static void welcomeAdmin(String adminName) {
		System.out.println("***************************************");
		System.out.println("Welcome " + adminName);
		System.out.println("***************************************");
		System.out.println("Please choose an option:");
		System.out.println("1 to enter new student data");
		System.out.println("2 to get Student Info");
		System.out.println("3 to View or Add Course");
		System.out.println("4 to View or Add Course Offering");
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
			try {
				System.out.println("Press 1 to View All Courses");
				System.out.println("Press 2 to Search a Course by Course ID");
				System.out.println("Press 3 to Add a new Course");
				int choice = in.nextInt();
				switch (choice) {
				case 1:
					viewCourses();
					break;
				case 2:
					viewCoursesById();
					break;
				case 3:
					addCourse();
					break;
				default:
					System.out.println("Please Enter a valid choice");
					welcomeAdmin(adminName);
				}
			} catch (Exception e) {
				System.out.println("Error - Please Enter a valid choice!");
				welcomeAdmin(adminName);
			}
			break;

		case 4:
			try {
				System.out.println("Press 1 to View Course offering");
				System.out.println("Press 2 to Add Course Offering");
				int choice = in.nextInt();
				switch (choice) {
				case 1:
					viewCourseOffering();
					break;
				case 2:
					addCourseOffering();
					break;
				default:
					System.out.println("Please Enter a valid choice");
					welcomeAdmin(adminName);
				}
			} catch (Exception e) {
				System.out.println("Error - Please Enter a valid choice!");
				welcomeAdmin(adminName);
			}
			break;

		default:
		}
	}

	public static void welcomeStudent(String studentName, String student_id)
			throws Exception {
		System.out.println("***************************************");
		System.out.println("Welcome " + studentName);
		System.out.println("***************************************");
		System.out.println("Please choose an option:");
		System.out.println("1 View Profile");
		System.out.println("2 View All Courses");
		System.out.println("3 Enroll Courses");
		System.out.println("4 View My Courses");
		System.out.println("5 View Pending Courses");
		System.out.println("6 View Grades");
		System.out.println("7 View/Pay Bill\n\n");
		int input = in.nextInt();
		switch (input) {
		case 1:
			viewStudentProfile(student_id);
			break;

		case 2:
			viewAllCurrentCourses();
			break;

		case 3:
			break;

		case 4:
			viewMyCourses(student_id);
			break;

		case 5:
			break;

		case 6:
			break;

		case 7:
			break;

		case 8:
			break;
		default:

		}
	}

	public static void main(String[] args) {
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
