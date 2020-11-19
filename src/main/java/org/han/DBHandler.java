package org.han;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.han.debug.Log;
import org.han.files.FileObj;

/**
 * Needs to be re-implemented. The design for this is simple...me starting to learn database stuff. Please ignore.
 * @author hanro
 *
 */
@Deprecated
public class DBHandler implements AutoCloseable {
	private Connection Con;
	private Statement statement;
	

	public DBHandler(FileObj file) {
		this(file, "SA", "");
	}

	public DBHandler(FileObj file, String UserName, String Password) {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			Con = DriverManager.getConnection("jdbc:hsqldb:file:" + file.getAbsolutePath(), UserName, Password);
			statement = Con.createStatement();
		} catch (ClassNotFoundException e) {
			Log.err("Cannot find DataBase Driver");
		} catch (SQLException e) {
			Log.err("Connection error");
			Log.trace(e);
		}
	}

	public Set<String> listTables() throws SQLException {
		DatabaseMetaData md = Con.getMetaData();
		String[] types = { "TABLE" };

		ResultSet rs = md.getTables(null, null, "%", types);
		Set<String> List = new HashSet<String>();
	
		while (rs.next()) {
			List.add(rs.getString("TABLE_NAME"));
			Log.rep("\t-->"+rs.getString("TABLE_NAME"));
		}
		
		return List;
	}

	public boolean delTable(String Name) {
		try {
			return Execute("DROP TABLE "+Name.toUpperCase()+";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.err("Could not delete table");
		}
		return false;
	}
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean Execute(String SQL) throws SQLException {
		Log.rep("Executing => " +SQL);
		return statement.execute(SQL);
	}
	
}
