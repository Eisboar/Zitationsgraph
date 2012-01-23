package de.uni.leipzig.asv.zitationsgraph.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import sun.dc.DuctusRenderingEngine;

import de.uni.leipzig.asv.zitationsgraph.data.*;

public class DBLoader {

	static final String JDBC_DRIVER = "org.gjt.mm.mysql.Driver";
	
	static final String DB_CREATE = "CREATE DATABASE GRAPH";
	static final String DB_DROP = "DROP DATABASE GRAPH";
	static final String DB_USE = "USE GRAPH";
	static final String VENUE_TABLE_CREATE = "CREATE TABLE Venue (name VARCHAR(200), PRIMARY KEY(name), year INT)";
	static final String AUTHOR_TABLE_CREATE = "CREATE TABLE Author (name VARCHAR(500), PRIMARY KEY(name), department VARCHAR(200))";
	static final String PUBLICATION_TABLE_CREATE = "CREATE TABLE Publication (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id),title VARCHAR(200)," +
			" venue VARCHAR(500), Foreign Key (venue) references Venue(name))";
	static final String PUBLISHED_TABLE_CREATE = "CREATE TABLE Published (pub_id INT references Publication(id), author INT references Author(name), PRIMARY KEY(pub_id, author))";
	static final String CITED_TABLE_CREATE = "CREATE TABLE Cited (source_id INT references Publication(id), target_id INT references Publication(id), textphrase VARCHAR(1000), PRIMARY KEY(source_id, target_id))";
	static final String VENUE_TABLE_DROP = "DROP TABLE Venue";
	static final String AUTHOR_TABLE_DROP = "DROP TABLE Author";
	static final String PUBLICATION_TABLE_DROP = "DROP TABLE Publication";
	static final String PUBLISHED_TABLE_DROP = "DROP TABLE Published";
	static final String CITED_TABLE_DROP = "DROP TABLE Cited";
	
	static final String INSERT_AUTHOR = "INSERT INTO Author(name, department) VALUES(?, ?)";
	static final String INSERT_VENUE = "INSERT INTO Venue(name, year) VALUES(?, ?)";
	static final String INSERT_PUBLICATION ="INSERT INTO Publication(title, venue) VALUES(?, ?)";
	static final String INSERT_PUBLISHED = "INSERT INTO Published(pub_id, author) VALUES(?, ?)";
	static final String INSERT_CITED = "INSERT INTO Author(source_id, target_id, textphrase) VALUES(?, ?, ?)";
	

	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	
	
	public DBLoader() {
		
		loadDriver();
		dbConnect();
		db_use();
		dropTables();
		createTables();
		closeConnection();
		
	}
	
	private void loadDriver(){
		
		try {
			Class.forName(JDBC_DRIVER).newInstance();
		}
		catch(Exception e){
			System.out.println("Unable to load the driver class!");
			System.out.println(e);
		}
	}
	
	private void dbConnect(){
		
		ConfigReader configReader = new ConfigReader();
		String url = "jdbc:mysql://"+configReader.getServer()+":"+configReader.getPort();
		establishConnection(url, configReader.getUser(), configReader.getPassword());	
	}
	
	private void establishConnection(String url, String user, String password){
		
		try{
			 connection=DriverManager.getConnection(url,user,password);
		}catch( SQLException e ){
				System.out.println( "Couldn’t establish DB-connection!" );
				System.out.println(e);
		}
	}
	
	private void closeConnection(){
		
		try{
			 connection.close();
		}catch( SQLException e ){
				System.out.println( "Couldn't close DB-Connection!" );
				System.out.println(e);
		}
	}
	
	
	/**
	 * Method to create the DB named 'GRAPH'
	 */
	public void dbCreate(){
		System.out.println("try to created DB...");
		String errorMessage = "Couldn't create DB";
		executeStatement(DB_CREATE, errorMessage);
	}
	
	/**
	 * Method to drop the DB 'GRAPH'
	 */
	public void dbDrop(){
		System.out.println("try to drop DB...");
		String errorMessage = "Couldn't drop DB";
		executeStatement(DB_DROP, errorMessage);
	}
	
	/**
	 * Method to activate the MySQL-DB
	 */
	public void db_use(){
		System.out.println( "try to select Graph-DB..." );
		String errorMessage = "Couldn't select Graph-DB";
		executeStatement(DB_USE, errorMessage);
	}
	
	
	//create Tables
	
	/**
	 * Method to Drop all Tables
	 */
	public void createTables(){
		createVenueTable();
		createAuthorTable();
		createPublicationTable();
		createPublishedTable();
		createCitedTable();
	}
	
	private void createVenueTable(){
		System.out.println("try to created Venue-Table...");
		String errorMessage = "Couldn't create Venue-Table";
		executeStatement(VENUE_TABLE_CREATE, errorMessage);
	}
	
	private void createAuthorTable(){
		System.out.println("try to created Author-Table...");
		String errorMessage = "Couldn't create Author-Table";
		executeStatement(AUTHOR_TABLE_CREATE, errorMessage);
	}
	
	private void createPublicationTable(){
		System.out.println("try to created Publication-Table...");
		String errorMessage = "Couldn't create Publication-Table";
		executeStatement(PUBLICATION_TABLE_CREATE, errorMessage);
	}
	
	private void createPublishedTable(){
		System.out.println("try to created Published-Table...");
		String errorMessage = "Couldn't create Published-Table";
		executeStatement(PUBLISHED_TABLE_CREATE, errorMessage);
	}
	
	private void createCitedTable(){
		System.out.println("try to created Cited-Table...");
		String errorMessage = "Couldn't create Cited-Table";
		executeStatement(CITED_TABLE_CREATE, errorMessage);
	}
	
	
	public void dropTables(){
		dropCitedTable();
		dropPublishedTable();
		dropPublicationTable();
		dropAuthorTable();
		dropVenueTable();
	}
	
	//drop Tables
	private void dropCitedTable(){
		System.out.println("try to drop Cited-Table...");
		String errorMessage = "Couldn't drop Cited-Table";
		executeStatement(CITED_TABLE_DROP, errorMessage);
	}
	
	private void dropVenueTable(){
		System.out.println("try to drop Venue-Table...");
		String errorMessage = "Couldn't drop Venue-Table";
		executeStatement(VENUE_TABLE_DROP, errorMessage);
	}
	
	private void dropAuthorTable(){
		System.out.println("try to drop Author-Table...");
		String errorMessage = "Couldn't drop Author-Table";
		executeStatement(AUTHOR_TABLE_DROP, errorMessage);
	}
	
	private void dropPublicationTable(){
		System.out.println("try to drop Publication-Table...");
		String errorMessage = "Couldn't drop Publication-Table";
		executeStatement(PUBLICATION_TABLE_DROP, errorMessage);
	}
	
	private void dropPublishedTable(){
		System.out.println("try to drop Published-Table...");
		String errorMessage = "Couldn't drop Published-Table";
		executeStatement(PUBLISHED_TABLE_DROP, errorMessage);
	}
	


	/**
	 * Standard method to execute an SQL-statement 
	 * @param String Query, String errorMessage
	 */
	private void executeStatement(String query, String errorMessage){
		try{
			statement = connection.createStatement();
			statement.execute(query);
			System.out.println( "done..." );
		}catch( SQLException e ){
			System.out.println( errorMessage );
			System.out.println(e);
		}finally{
			closeStatement();
		}
	}
	
	
	private void closeStatement(){
		try{
			if(statement!=null)
	        	 statement.close();
	    }catch(SQLException e){
	    	System.out.println( "Couldn't close Statement!" );
	    	System.out.println(e);
	    }
	}
	
	private void closePreparedStatement(){
		try{
			if(preparedStatement!=null)
	        	 preparedStatement.close();
	    }catch(SQLException e){
	    	System.out.println( "Couldn't close PreparedStatement!" );
	    	System.out.println(e);
	    }
	}
	
	
	private void saveAuthor(Author author){
		try {
			preparedStatement = connection.prepareStatement(INSERT_AUTHOR);
			preparedStatement.setString(1, author.getName());
			preparedStatement.setString(2, author.getAffiliation());
			preparedStatement.executeUpdate();
		}catch( SQLException e ){
			System.out.println( "Couldn't insert Author" );
	    	System.out.println(e);
		} finally {
		      closePreparedStatement();
		}
	}
	
	private void saveVenue(Date year, String name){
		//int venueID=-1;
		try {
			preparedStatement = connection.prepareStatement(INSERT_VENUE);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, year.getYear()); //TODO: change Date
			preparedStatement.executeUpdate();
		}catch( SQLException e ){
			System.out.println( "Couldn't insert Author" );
	    	System.out.println(e);
		} finally {
		      closePreparedStatement();
		}
		//return venueID;
	}
	
	
	private void savePublication(Publication publication){
		try {
			preparedStatement = connection.prepareStatement(INSERT_PUBLICATION);
			preparedStatement.setString(1, publication.getTitle());
			preparedStatement.setString(2, publication.getVenue());
			preparedStatement.executeUpdate();
		}catch( SQLException e ){
			System.out.println( "Couldn't insert Author" );
	    	System.out.println(e);
		} finally {
		      closePreparedStatement();
		}
	}
	
	/**
	 * Method for writing a Document into the DB
	 * @param publication
	 */
	public void saveDocument(Document document){
		Publication publication = document.getPublication();
		saveVenue(publication.getYear(), publication.getVenue());
		savePublication(publication);
		for (Author author : publication.getAuthors())
		{
		    saveAuthor(author);
		}
		for (Citation citation: document.getCitations())
		{
		    savePublication(citation.getPublication());
		}
	}	
}
