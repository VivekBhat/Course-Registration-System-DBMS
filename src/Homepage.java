import java.io.IOException;
import java.sql.Date;
import java.util.Scanner;

public class Homepage {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.println("***********");
		System.out.println("Admin Login");
		System.out.println("***********");
		System.out.println("Enter Username:");
		String username = in.nextLine();
		System.out.println("Enter password:");
		String password = in.nextLine();

		String sid;
		Date dob;
		System.out.println("Hello Admin");
		//
		// System.out.println("Username is: " + username);
		// System.out.println("Password is: " + password);
		// System.out.println();

		System.out.println("Please choose an option:");
		System.out.println("1 to enter new student data");
		System.out.println("2 to get Student Info");
		System.out.println("3 to View or Add Course");
		int option = in.nextInt();
		in.nextLine();
		String opt = "y";

		switch (option) {
		case 1:
			opt = "y";

			while (opt.toLowerCase().equals("y")) {
				opt = newStud(in, opt);
			}
			try {
				Runtime.getRuntime().exec("clear");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
			break;

		case 3:

			System.out.println("Enter Student ID: ");
			sid = in.nextLine();

			opt = "y";
			while (opt.toLowerCase().equals("y"))
				viewOrAdd(in, sid);

			break;

		default:
			System.out.println("Invalid Option");
			break;
		}

	}

	private static void viewOrAdd(Scanner in, String sid) {
		// TODO Auto-generated method stub

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
