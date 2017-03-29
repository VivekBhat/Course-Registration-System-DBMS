
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Vivek Bhat
 */
public class JDBC_Connection {
	
	Creds obj = new Creds();
	
	static final String jdbcURL = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";
	private String user_name = obj.userName;
	private String password = obj.password;

	private JDBC_Connection() {

	}

	public Connection makeConnection() {
		Connection conn = null;
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			try {

				conn = DriverManager.getConnection(jdbcURL, this.user_name, this.password);

			} finally {

				close(conn);
			}
		} catch (Throwable oops) {
			oops.printStackTrace();
		}

		return conn;
	}

	static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Throwable whatever) {
			}
		}
	}

}