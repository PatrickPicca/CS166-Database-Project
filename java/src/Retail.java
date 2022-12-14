/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Retail {

   // reference to physical database connection.
   private Connection _connection = null;
   private static String userID = "";
   private static String userName = "";
   private static double userLat = 0;
   private static double userLong = 0;
   private static String userType = "";
   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Retail shop
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Retail(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Retail

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public static double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Retail.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Retail esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Retail object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Retail (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Stores within 30 miles");
                System.out.println("2. View Product List");
                System.out.println("3. Place a Order");
                System.out.println("4. View 5 recent orders");
              // if (userType.contains("manager"))
                //the following functionalities basically used by managers
                System.out.println("5. Update Product");
                System.out.println("6. View 5 recent Product Updates Info");
                System.out.println("7. View 5 Popular Items");
                System.out.println("8. View 5 Popular Customers");
                System.out.println("9. Place Product Supply Request to Warehouse");
                System.out.println("10. View all Orders");
                System.out.println("11. View User Info");
                System.out.println("12. Update User Info");
                System.out.println("13. View all Products");

               //The following functionalities basically used by admins
                //System.out.println("15. Update Product Info ");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewStores(esql); break;
                   case 2: viewProducts(esql); break;
                   case 3: placeOrder(esql); break;
                   case 4: viewRecentOrders(esql); break;
                   case 5: updateProduct(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewPopularProducts(esql); break;
                   case 8: viewPopularCustomers(esql); break;
                   case 9: placeProductSupplyRequests(esql); break;
                   case 10: viewAllOrders(esql); break;
                   case 11: viewUserInfo(esql); break;
                   case 12: updateUserInfo(esql); break;
                   case 13: viewAllProducts(esql); break;

                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
         System.out.print("\tEnter latitude: ");   
         String latitude = in.readLine();       //enter lat value between [0.0, 100.0]
         System.out.print("\tEnter longitude: ");  //enter long value between [0.0, 100.0]
         String longitude = in.readLine();
         
         String type="Customer";

			String query = String.format("INSERT INTO USERS (name, password, latitude, longitude, type) VALUES ('%s','%s', %s, %s,'%s')", name, password, latitude, longitude, type);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         userID = result.get(0).get(0);
         userLat = Double.valueOf(result.get(0).get(3));
         userLong = Double.valueOf(result.get(0).get(4));
         userType = result.get(0).get(5);
	      if (userID != "")
		      return name;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   //View Stores within 30 miles
   public static void viewStores(Retail esql) {
      String query = "SELECT * FROM STORE";
      try{
         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         System.out.println("\nStores within 30 miles of you: \n");
         System.out.println("Store ID\tStore Name\t\t\tStore Lat\tStore Long\tManager ID\tDate Established");
         for(int i = 0; i < result.size(); i++) {
            if(calculateDistance(userLat, userLong, Double.valueOf(result.get(i).get(2)), Double.valueOf(result.get(i).get(3))) <= 30) {
                  System.out.println(result.get(i).get(0) + "\t\t" + result.get(i).get(1) + "\t" + result.get(i).get(2) + "\t" + result.get(i).get(3) + "\t" + result.get(i).get(4) + "\t\t" + result.get(i).get(5));
            }
         }
         System.out.println("\n");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }

   public static void viewProducts(Retail esql) {
      try{
         System.out.print("\tEnter store ID: ");
         String storeID = in.readLine();
         String query = String.format("SELECT * FROM PRODUCT WHERE storeID = '%s'", storeID);
         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         System.out.println("\nProducts in store " + storeID + ": \n");
         System.out.println("Product Name\t\t\tNumber of Units\t\tPrice Per Unit");
         for(int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).get(1) + "\t" + result.get(i).get(2) + "\t\t\t" + result.get(i).get(3));
         }
         System.out.println("\n");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }
   //NOTE: A trigger can be used here to update the order number each time an order is inserted
   public static void placeOrder(Retail esql) {
      String storeID = "";
      String productName = "";
      int numUnits = 0;
      int result = 0;;
      String query = "";
      System.out.print("\tEnter store ID: ");
      do{
         try{
            storeID = in.readLine();
            query = String.format("SELECT * FROM STORE WHERE storeID = '%s'", storeID);
            result = esql.executeQuery(query);
            if(result <= 0){
               System.out.print("\tStore ID does not exist. Please enter a valid store ID: ");
            }else{
               break;
            }
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
      }while(true);
      System.out.print("\tEnter product name: ");
      int unitsAvailiable = 0;
      do{
         try{
            productName = in.readLine();
            query = String.format("SELECT * FROM PRODUCT WHERE storeID = '%s' AND productName = '%s'", storeID, productName);
            result = esql.executeQuery(query);
            if(result <= 0) {
               System.out.print("\tProduct not found or not available. Please enter a valid product name: ");
            }
            else {
               query = String.format("SELECT numberOfUnits FROM PRODUCT WHERE storeID = '%s' AND productName = '%s'", storeID, productName);
               unitsAvailiable = Integer.valueOf(esql.executeQueryAndReturnResult(query).get(0).get(0));
               break;
            }
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
      }while(true);
      System.out.print("\tEnter number of units: ");
      do{
         try{
            numUnits = Integer.valueOf(in.readLine());
            if(numUnits < 1 || numUnits > unitsAvailiable) {
               System.out.printf("\tInvalid number of units. Please enter a valid number. (There are currently %s units available.): ", unitsAvailiable);
            }
            else {
               break;
            }
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
      }while(true);
      //NOTE: if trigget is used, orderNum can be removed from the query
      query = String.format("INSERT INTO Orders(customerID, storeID, productName, unitsOrdered) Values (%s, %s, '%s', %d)", userID, storeID, productName, numUnits);
      try{
         esql.executeUpdate(query);
         System.out.println("\n\tOrder successfully placed!\n");
         query = String.format("UPDATE PRODUCT SET numberOfUnits = numberOfUnits - %d WHERE storeID = '%s' AND productName = '%s'", numUnits, storeID, productName);
         esql.executeUpdate(query);
         query = String.format("SELECT * FROM PRODUCT WHERE storeID = '%s' AND productName = '%s'", storeID, productName);
         List<List<String>> result2 = esql.executeQueryAndReturnResult(query);
         System.out.println("Store ID\tProduct Name\t\t\tNumber of Units\t\tPrice Per Unit");
         for(int i = 0; i < result2.size(); i++) {
            System.out.println(result2.get(i).get(0) + "\t\t" + result2.get(i).get(1) + '\t' +result2.get(i).get(2) + "\t\t\t" + result2.get(i).get(3) + "\n");
         }
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }
   public static void viewRecentOrders(Retail esql) {
      String query = String.format("SELECT * FROM Orders WHERE customerID = %s ORDER BY orderTime DESC", userID);
      try{
         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         System.out.println("\nFive of your most recent orders: ");
         System.out.println("Store ID\tStore Name\t\t\tProduct Name\t\t\tNumber of Units\t\tOrder Time");
         int numOrders = (result.size() < 5) ? result.size() : 5;
         for(int i = 0; i < numOrders; i++) {
            String storeName = esql.executeQueryAndReturnResult(String.format("SELECT name FROM STORE WHERE storeID = '%s'", result.get(i).get(2))).get(0).get(0);
            System.out.println(result.get(i).get(2) + "\t\t" + storeName + "\t" + result.get(i).get(3) + "\t" + result.get(i).get(4) + "\t\t\t" + result.get(i).get(5));
         }
         System.out.println("\n");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }
   public static void updateProduct(Retail esql) {
      if(!userType.contains("manager") && !userType.contains("admin")) {
         System.out.println("\n\tERROR: You do not have permission to update products!\n");
         return;
      }
      String query = "";
       //Display to user menu of stores they manage
            try{
               query = String.format("SELECT * FROM STORE WHERE managerID = '%s'", userID);
               List<List<String>> theResult = esql.executeQueryAndReturnResult(query);
               System.out.println("Store ID\tStore Name");
               for(int i = 0; i < theResult.size(); i++) {
                  System.out.println(theResult.get(i).get(0) + "\t\t" + theResult.get(i).get(1));
               }
            }
            catch(Exception e){
                  System.err.println (e.getMessage ());
            }

      System.out.print("\tEnter store ID: ");
      String storeID = "";
      int result = 0;
      do{
         try{
            storeID = in.readLine();
            query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeID, userID);
            result = esql.executeQuery(query);
            if(result <= 0 && userType.contains("manager")) { //Only managers need to check if they are the manager of the store
               System.out.print("\tYou are not the manager of this store. Please enter a valid store ID: ");
            }else {
               break;
            }
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
      }while(true);
      System.out.print("\tEnter product name: ");
      String productName = "";
      do{
         try{
            productName = in.readLine();
            query = String.format("SELECT * FROM PRODUCT WHERE storeID = '%s' AND productName = '%s'", storeID, productName);
            result = esql.executeQuery(query);
            if(result <= 0) {
               System.out.print("\tProduct not found. Please enter a valid product name: ");
            }
            else {
               break;
            }
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
      }while(true);
      int input = 0;
      do{
         System.out.print("\tWhat would you like to update? (1) Number of units (2) Price per unit: ");
         try{
            input = Integer.valueOf(in.readLine());
            if(input != 1 && input != 2) {
               System.out.println("\tInvalid input. Please enter 1 or 2.");
            }
            else {
               break;
            }
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
      }while(true);
      if(input == 1) {
         System.out.print("\tEnter new number of units: ");
         do{
            try{
               input = Integer.valueOf(in.readLine());
               if(input < 0) {
                  System.out.print("\tInvalid number of units. Please enter a positive number: ");
               }
               else {
                  break;
               }
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
         }while(true);
         query = String.format("UPDATE PRODUCT SET numberOfUnits = %d WHERE storeID = '%s' AND productName = '%s'", input, storeID, productName);
      }
      else {
         System.out.print("\tEnter new price per unit: ");
         double price = 0.0;
         do{
            try{
               price = Double.valueOf(in.readLine());
               if(price < 0) {
                  System.out.print("\tInvalid price. Please enter a positive number: ");
               }
               else {
                  break;
               }
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
         }while(true);
         query = String.format("UPDATE PRODUCT SET pricePerUnit = %f WHERE storeID = '%s' AND productName = '%s'", price, storeID, productName);
      }
      try{
         esql.executeUpdate(query);
         System.out.println("\n\tProduct successfully updated!\n");
         query = String.format("SELECT * FROM PRODUCT WHERE storeID = '%s' AND productName = '%s'", storeID, productName);
         List<List<String>> result2 = esql.executeQueryAndReturnResult(query);
         System.out.println("Store ID\tProduct Name\t\t\tNumber of Units\t\tPrice Per Unit");
         System.out.println(result2.get(0).get(0) + "\t\t" + result2.get(0).get(1) + '\t' +result2.get(0).get(2) + "\t\t\t" + result2.get(0).get(3) + "\n");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
      if (userType.contains("manager")){
         query = String.format("INSERT INTO ProductUpdates(managerID, storeID, productName) Values ('%s', '%s', '%s')", userID, storeID, productName);
         try{
            esql.executeUpdate(query);
            System.out.println("\n\tProduct Update entry made!\n");
            query = String.format("SELECT * FROM ProductUpdates ORDER BY updateNumber DESC LIMIT 1");
            List<List<String>> result2 = esql.executeQueryAndReturnResult(query);
            System.out.println("Update Number\tManager ID\tStore ID\t\tProduct Name\t\t\tUpdated On");
            System.out.println(result2.get(0).get(0) + "\t\t" + result2.get(0).get(1) + "\t\t" +result2.get(0).get(2) + "\t\t\t" + result2.get(0).get(3) + "\t" + result2.get(0).get(4) );
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
      }
   }

   /*
   Manager can also view the information of last 5 recent updates of his/her store(s).
   */
   public static void viewRecentUpdates(Retail esql) 
   {
      //Find most recent updates local to the store of the manager
      if (userType.contains("manager"))
      {
         try{
            String query = String.format("SELECT P.productName, P.storeID, P.managerID, P.updateNumber, P.updatedOn FROM ProductUpdates P  WHERE P.storeID IN ( SELECT S.storeID FROM Store S WHERE S.managerID = %s) ORDER BY P.updatedOn DESC LIMIT 5", userID);
            List<List<String>> result = esql.executeQueryAndReturnResult(query);
            System.out.println("Product Name\t\tStore ID\t\tManager ID\tUpdate Number\tUpdated ON");
            for(int i = 0; i < result.size(); i++) {

               System.out.println(result.get(i).get(0) + "\t" + result.get(i).get(1) + "\t\t" + result.get(i).get(2)  + "\t\t" + result.get(i).get(3)  + "\t\t" + result.get(i).get(4));
            }
            System.out.println("\n");
         }catch(Exception e){
            
            System.err.println (e.getMessage ());
         }
      }
      else if (userType.contains("admin"))
         System.out.println("\tYou do not have a store");
      else
         System.out.println("\tYou do not have Manager priveldges");
 
   }
   /*
   Popular product and customer: Manager will be able to see top 5 most popular products (product name) 
      in his/her store(s) (Based on the order count of Product).
   */
   public static void viewPopularProducts(Retail esql) 
   {
      if (userType.contains("manager"))
      {
         try{
            String query = String.format("SELECT O.productName, SUM(O.unitsOrdered) AS totalUnitsOrdered FROM ORDERS O WHERE O.storeID IN (SELECT S.storeID FROM Store S WHERE S.managerID = %s) GROUP BY O.productName ORDER BY SUM(O.unitsOrdered) DESC LIMIT 5", userID);
            List<List<String>> result = esql.executeQueryAndReturnResult(query);
            System.out.println("Product Name");
            for(int i = 0; i < result.size(); i++) {

               System.out.println(result.get(i).get(0));
            }
            System.out.println("\n");
         }catch(Exception e){
            
            System.err.println (e.getMessage ());
         }
      }
      else if (userType.contains("admin"))
         System.out.println("\tYou do not have a store");
      else
         System.out.println("\tYou do not have Manager priveldges");
   } 
   /*
   Manager can also view the top 5 customer???s information who placed the most orders in his/her store(s).
   */
   public static void viewPopularCustomers(Retail esql) 
   {
      if(userType.contains("manager"))
      {
         //   System.out.println("This is a manager!");
         //Based on the store local to the manager, return the 5 customers who has the most orders for that store.
         try{
            String query = String.format("SELECT U.userID, U.name, U.password, U.longitude, U.latitude FROM ORDERS O, USERS U WHERE U.userID = O.customerID AND O.storeID  IN (	SELECT S.storeID FROM Store S WHERE S.managerID = %s)GROUP BY U.userID ORDER BY COUNT(*) DESC LIMIT 5", userID);
            List<List<String>> result = esql.executeQueryAndReturnResult(query);
            System.out.println("User ID\t\tUser Name\t\t\t\tUser Password\t\t\tUser Longitude\t\tUser Latitude");
            for(int i = 0; i < result.size(); i++) {
               //System.out.println(result.get(i).get(0));
               System.out.println(result.get(i).get(0) + "\t\t" + result.get(i).get(1)  + result.get(i).get(2)  + "\t\t" + result.get(i).get(3)  + "\t\t" + result.get(i).get(4));
            }
            System.out.println("\n");
         }catch(Exception e){ 
            
            System.err.println (e.getMessage ());
         }
      }
      else if (userType.contains("admin"))
         System.out.println("\tYou do not have a store");
      else
         System.out.println("\tYou do not have Manager priveldges");
   }

   /* 
   ??? Put Supply Request: Manager can put product supply request for any product of his/her store(s).For that, 
      they will need to input storeID, productName, number of units needed, and warehouseID of the warehouse 
      which will supply the supply request. After placing the request we can assume that warehouse has enough
      products to process the request. So Product table and ProductRequests table should be updated accordingly 
      after placing the supply request.
   */
   public static void placeProductSupplyRequests(Retail esql) 
   {
      if (userType.contains("manager"))
      {
         try{
            String query;
            String storeID;
            int result = 0;

            //Display to user menu of stores they manage
            try{
               query = String.format("SELECT * FROM STORE WHERE managerID = '%s'", userID);
               List<List<String>> theResult = esql.executeQueryAndReturnResult(query);
               System.out.println("Store ID\tStore Name");
               for(int i = 0; i < theResult.size(); i++) {
                  System.out.println(theResult.get(i).get(0) + "\t\t" + theResult.get(i).get(1));
               }
            }
            catch(Exception e){
                  System.err.println (e.getMessage ());
            }

            System.out.print("Enter store ID from the above list of stores: ");
            do{
               try{
                  storeID = in.readLine();
                  query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeID, userID);
                  result = esql.executeQuery(query);
                  if(result <= 0){
                     System.out.print("Store ID does not exist. Please enter a valid store ID: ");
                  }else{
                     break;
                  }
               }
               catch(Exception e){
                  System.err.println (e.getMessage ());
               }
            }while(true);
            System.out.print("\tEnter Product Name: ");
            String productName = in.readLine();
            System.out.print("\tEnter Number of Units: ");
            String theunits = in.readLine();
            int numUnits = Integer.parseInt(theunits);
            System.out.print("\tEnter Warehouse ID: ");
            String warehouseID = in.readLine();

            //Need query to update Entry in Product Table with additional units
            query = String.format("UPDATE PRODUCT SET numberOfUnits = numberOfUnits + %d WHERE storeID = '%s' AND productName = '%s'", numUnits, storeID, productName);
            try{
               System.out.println("\tAbout to add additional units!\n");
               esql.executeUpdate(query);
               System.out.println("\tAdditional units added!\n");
               query = String.format("SELECT * FROM PRODUCT WHERE storeID = '%s' AND productName = '%s'", storeID, productName);
               List<List<String>> result2 = esql.executeQueryAndReturnResult(query);
               System.out.println("Store ID\tProduct Name\t\t\tNumber of Units\t\tPrice Per Unit");
               System.out.println(result2.get(0).get(0) + "\t\t" + result2.get(0).get(1) + '\t' +result2.get(0).get(2) + "\t\t\t" + result2.get(0).get(3) + "\n");
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
            //Need query to add Entry into ProductSupplyRequests table
            query = String.format("INSERT INTO ProductSupplyRequests(managerID, warehouseID, storeID, productName, unitsRequested) Values ('%s', '%s', '%s', '%s', %d)", userID, warehouseID, storeID, productName, numUnits);
            try{
               System.out.println("\tAbout to place supply request!\n");
               esql.executeUpdate(query);
               System.out.println("\tRequest history updated!\n");
               query = String.format("SELECT * FROM ProductSupplyRequests ORDER BY requestNumber DESC LIMIT 1");
               List<List<String>> result3 = esql.executeQueryAndReturnResult(query);
               System.out.println("Request ID\tManager ID\tWarehouse ID\tStore ID\tProduct Name\t\t\tUnits Requested");
               System.out.println(result3.get(0).get(0) + "\t\t" + result3.get(0).get(1) + "\t\t" + result3.get(0).get(2) + "\t\t" + result3.get(0).get(3) + "\t\t" + result3.get(0).get(4) + "\t" + result3.get(0).get(5) + "\n");
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }      


            //String query = String.format("SELECT P.productName, P.storeID, P.managerID, P.updateNumber, P.updatedOn FROM ProductUpdates P  WHERE P.storeID IN ( SELECT S.storeID FROM Store S WHERE S.managerID = %s) ORDER BY P.updatedOn DESC LIMIT 5", userID);
            //List<List<String>> result = esql.executeQueryAndReturnResult(query);
            //System.out.println("Product Name\t\tStore ID\t\tManager ID\tUpdate Number\tUpdated ON");
            //for(int i = 0; i < result.size(); i++) {

            //   System.out.println(result.get(i).get(0) + "\t" + result.get(i).get(1) + "\t\t" + result.get(i).get(2)  + "\t\t" + result.get(i).get(3)  + "\t\t" + result.get(i).get(4));
           // }
           // System.out.println("\n");
         }catch(Exception e){
            
            System.err.println (e.getMessage ());
         }
      }
      else if (userType.contains("admin"))
         System.out.println("\tYou do not have a store");
      else
         System.out.println("\tYou do not have Manager priveldges");
      
   }

 /*
   Admin: Admins will be able view and update the information of all users and 
   products information of the database
    */
   public static void viewUserInfo(Retail esql) {
      if(!userType.contains("admin")){
         System.out.println("You do not have permission to view user information.\n");
         return;
      }
      String choice = "";
      do{
         System.out.print("View all users or view a specific user? (all or specific): ");
         try{
            choice = in.readLine();
            if(!choice.equals("all") && !choice.equals("specific")){
               System.out.print("Invalid choice. Please enter 'all' or 'specific': ");
               continue;
            }
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
         break;
      }while(true);
      do{
         if(choice.equals("all")){
            try{
               String query = "SELECT * FROM Users";
               List<List<String>> result = esql.executeQueryAndReturnResult(query);
               System.out.println("ID\tName\t\t\t\t\t\t\tPassword\t\tLatitude\t\tLongitude\t\tUser Type");
               for(int i = 0; i < result.size(); i++) {
                  System.out.println(result.get(i).get(0) + "\t" + result.get(i).get(1) + '\t' + result.get(i).get(2)  + "\t\t" + result.get(i).get(3) + "\t\t" + result.get(i).get(4) + "\t\t" + result.get(i).get(5));
               }
               System.out.println("\n");
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
            break;
         }
         else if(choice.equals("specific")){
            System.out.print("Enter user ID: ");
            try{
               String userID = in.readLine();
               String query = String.format("SELECT * FROM Users WHERE userID = '%s'", userID);
               List<List<String>> result = esql.executeQueryAndReturnResult(query);
               System.out.println("\nID\tName\t\t\t\t\t\t\tPassword\t\tLatitude\t\tLongitude\t\tUser Type");
               System.out.println(result.get(0).get(0) + "\t" + result.get(0).get(1) + '\t' + result.get(0).get(2)  + "\t\t" + result.get(0).get(3) + "\t\t" + result.get(0).get(4) + "\t\t" + result.get(0).get(5) + "\n");
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
            break;
         }
         System.out.print("Would you like to view another user? (y/n): ");
         do{
            try{
               choice = in.readLine();
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
            if(choice.equals("n")){
               break;
            }
            else if(choice.equals("y")){
               continue;
            }else{
               System.out.println("Invalid choice. Please enter 'y' or 'n': ");
            }
         }while(true);
      }while(true);
   }

   public static void updateUserInfo(Retail esql){
      if(!userType.contains("admin")){
         System.out.println("You do not have permission to update user information.");
         return;
      }
      String ID = "";
      String  attribute = "";
      String update = "";
      do{
         System.out.print("\tEnter user ID of the user you wish to update: ");
         do{
            try{
               ID = in.readLine();
               String query = String.format("SELECT * FROM Users WHERE userID = '%s'", userID);
               int result = esql.executeQuery(query);
               if(result <= 0){
                  System.out.print("\tUser ID does not exist. Please enter a valid user ID:");
                  continue;
               }
               break;
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
         }while(true);
         System.out.print("\tEnter the attribute you wish to update \n\t(name, password, latitude, longitude, type): ");
         do{
            try{
               attribute = in.readLine();
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
            if(!attribute.equals("name") && !attribute.equals("password") && !attribute.equals("latitude") && !attribute.equals("longitude") && !attribute.equals("type")){
               System.out.print("\tInvalid attribute. Please enter a valid attribute:");
               continue;
            }
            break;
         }while(true);
         System.out.printf("\tEnter the new value for the attribute %s: ", attribute);
         do{
            try{
               update = in.readLine();
            }catch(Exception e){
               System.err.println (e.getMessage ());
            }
            if(attribute.equals("latitude") || attribute.equals("longitude")){
               try{
                  Double.parseDouble(update);
               }catch(Exception e){
                  System.out.print("\tInvalid value. Please enter a valid decimal value:");
                  continue;
               }
            }
            if(attribute.equals("type")){
               if(!update.equals("admin") && !update.equals("manager") && !update.equals("customer")){
                  System.out.print("\tInvalid type. Please enter a valid type:");
                  continue;
               }
            }
            if(attribute.equals("name")){
               if(update.length() > 50 || update.length() < 1){
                  System.out.print("\tThe length of the name must be between 1 and 50 characters. Please enter a valid value:");
                  continue;
               }
            }
            break;
         }while(true);
         String query = String.format("UPDATE Users SET %s = '%s' WHERE userID = '%s'", attribute, update, ID);
         try{
            esql.executeUpdate(query);
            System.out.println("\tUser information updated!");
            query = String.format("SELECT * FROM Users WHERE userID = '%s'", ID);
            List<List<String>> result = esql.executeQueryAndReturnResult(query);
            System.out.println("\nID\tName\t\t\t\t\t\t\tPassword\t\tLatitude\t\tLongitude\t\tUser Type");
            System.out.println(result.get(0).get(0) + "\t" + result.get(0).get(1) + '\t' + result.get(0).get(2)  + "\t\t" + result.get(0).get(3) + "\t\t" + result.get(0).get(4) + "\t\t" + result.get(0).get(5) + "\n");
            System.out.print("\tWould you like to update another user? (y/n): ");
            String answer = in.readLine();
            if(answer.equals("n")){
               break;
            }
         }catch(Exception e){
            System.err.println (e.getMessage ());
         }
      }while(true);
   }


   public static void viewAllProducts(Retail esql) {
      if(!userType.contains("admin")) {
         System.out.println("\n\tERROR: You do not have permission to view all products!\n");
         return;
      }
      String query = "SELECT * FROM PRODUCT";
      try{
         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         System.out.println("\nAll products: ");
         System.out.println("Store ID\tProduct Name\t\t\tNumber of Units\t\tPrice per Unit");
         for(int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).get(0) + "\t\t" + result.get(i).get(1) + "\t" + result.get(i).get(2) + "\t\t\t" + result.get(i).get(3));
         }
         System.out.println("\n");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }

   /*
   Manager can see all the orders information of the store(s) he/she
   manages. They will be able to see orderID, customer name, storeID, productName, 
   and date of order for each order.
    */
    public static void viewAllOrders(Retail esql)
    {
         if (userType.contains("manager"))
         {
            try{
               String query = String.format("SELECT O.orderNumber, O.customerID, O.storeID, O.productName, O.unitsOrdered, O.orderTime FROM ORDERS O WHERE O.storeID IN (SELECT S.storeID FROM Store S WHERE S.managerID = %s) ", userID);
               List<List<String>> result = esql.executeQueryAndReturnResult(query);
               System.out.println("Order Number\tCustomer ID\tStore ID\tProduct Name\t\t\tUnits Ordered\tOrder Time");
               for(int i = 0; i < result.size(); i++) {

                  System.out.println(result.get(i).get(0) + "\t\t" + result.get(i).get(1) + "\t\t" + result.get(i).get(2)  + "\t\t" + result.get(i).get(3)  + "\t" + result.get(i).get(4)  + "\t\t" + result.get(i).get(5));
               }
               System.out.println("\n");
            }catch(Exception e){
               
               System.err.println (e.getMessage ());
            }
         }
         else if (userType.contains("admin"))
            System.out.println("\tYou do not have a store");
         else
            System.out.println("\tYou do not have Manager priveldges");
    }
    


}//end Retail
