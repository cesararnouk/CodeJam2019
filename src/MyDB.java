import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyDB {
	//this class creates the connection to the database
	static String jdbcUrl = "jdbc:mysql://localhost:3306/school";
	static String USERNAME = "root";
	static String PASSWORD = "";
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD);
	}
	
	public static void addReview(String prof_ID, String review, String grade) {
		String sql = "INSERT INTO reviews (prof_ID, review, grade) VALUES (?, ?, ?)";
		try ( Connection conn = MyDB.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setString(1, prof_ID);
			stmt.setString(2, review);
			stmt.setString(3, grade);
			stmt.execute();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getProfs() throws SQLException{
		JsonArray array = new JsonArray();
		try ( Connection conn = MyDB.getConnection();
				Statement stmt = conn.createStatement();
				){
			String sql = "SELECT * FROM teachers";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {			
//				arr.add("{'last_name': '" + rs.getString("last_name") + "', " +
//						"'first_name': '" + rs.getString("first_name") + "', " +
//						"'ID': '" + rs.getInt("ID") + "', }");
//				arr.add("{ \"last_name\": \"" + rs.getString("last_name") + "\", " +
//						"\"first_name\": \"" + rs.getString("first_name") + "\", " +
//						"\"ID\": \"" + rs.getInt("ID") + "\", }");
				
				JsonObject json = new JsonObject();
				json.addProperty("last_name", rs.getString("last_name"));
				json.addProperty("first_name", rs.getString("first_name"));
				json.addProperty("id", rs.getString("ID"));
				array.add(json);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return array.toString();
	}
}