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

		String id, email, f_name, l_name, address, date;
		Date dob;
		System.out.println("Hello Admin");
		//
		// System.out.println("Username is: " + username);
		// System.out.println("Password is: " + password);
		// System.out.println();

		System.out.println("Please choose an option:");
		System.out.println("1 to enter new student data");
		System.out.println("2 to get Student Info");
		int option = in.nextInt();
		String opt = "y";
		switch (option) {
		case 1:
			in.nextLine();
			opt = "y";

			while (opt.toLowerCase().equals("y"))
				newStud(in, opt);

			break;

		default:
			System.out.println("Invalid Option");
			break;
		}

	}

	private static void newStud(Scanner in, String opt) {
		String id;
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
		id = in.nextLine();
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
		for (

				int i = 0; i < 100; ++i)
			System.out.println();
	}

}
