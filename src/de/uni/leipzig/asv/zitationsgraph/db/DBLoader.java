package de.uni.leipzig.asv.zitationsgraph.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBLoader {

	Connection connection = null;
	
	
	
	public DBLoader() {
		
		loadDriver();
	}

	private void loadDriver(){
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); //Or any other driver
		}
		catch(Exception e){
			System.out.println("Unable to load the driver class!");
			System.out.println(e);
		}
	}
	
	private getConfigProperties()
	
	private void establishConnection(String url, String user, String password){
		try{
			 connection=DriverManager.getConnection(url,user,password);
			 connection.close();
		}
			catch( SQLException e ){
				System.out.println( "Couldn’t establish DB-connection!" );
				System.out.println(e);
				
			}
	}
	
	private void closeConnection(){
		connection.close();
	}
	
	
}
