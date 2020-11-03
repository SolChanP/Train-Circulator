import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBManager {
	public DBManager() {
		
	}
	public Connection makeDB() {
		String url = "jdbc:mysql://www.travelit.me:3306";					
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("DB 연결중 ...");
			con = DriverManager.getConnection(url, "mjstation", "mjstation");
			System.out.println("DB 연결 성공^^ ...");
		}catch(ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}catch(SQLException ex) {
			System.out.println("SQLException" + ex.getMessage());
		}
		return con;
	}
}
