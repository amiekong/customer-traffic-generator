package cs5500.cs5500.dao;

import cs5500.cs5500.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CSVGeneratorDAO {

  protected ConnectionManager connectionManager;

  // Single pattern: instantiation is limited to one object.
  private static CSVGeneratorDAO instance = null;

  public CSVGeneratorDAO() {
    connectionManager = new ConnectionManager();
  }

  public static CSVGeneratorDAO getInstance() {
    if (instance == null) {
      instance = new CSVGeneratorDAO();
    }
    return instance;
  }

  /**
   * Save the Persons instance by storing it in your MySQL instance. This runs a INSERT statement.
   */
  public void createCustomerTable(String tableName) throws SQLException {

    dropTable(tableName);
    String createTable = "CREATE TABLE " + tableName
            + "  (CustomerId INT NOT NULL,"
            + "  isCustomerSenior VARCHAR(5),"
            + "  timeSlot VARCHAR(255),"
            + "  visitDuration INT(4),"
            + "  PRIMARY KEY (CustomerId));";

    Connection connection = null;
    Statement createStmt = null;
    try {
      connection = connectionManager.getConnection();
      createStmt = connection.createStatement();
      createStmt.executeUpdate(createTable);
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
  }

  public void dropTable(String tableName) throws SQLException {

    String dropTable = "DROP TABLE IF EXISTS " + tableName + ";";
    Connection connection = null;
    Statement dropStmt = null;
    try {
      connection = connectionManager.getConnection();
      dropStmt = connection.createStatement();
      dropStmt.executeUpdate(dropTable);
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
  }

  public void insertCustomer(Integer customerID, String isSenior, String timeSlot, Integer duration,
                             String tableName) throws SQLException {
    String insertCustomer = "INSERT INTO " + tableName
            + " (CustomerId,isCustomerSenior,timeSlot,visitDuration) VALUES(?,?,?,?);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    try {
      connection = connectionManager.getConnection();
      insertStmt = connection.prepareStatement(insertCustomer);

      insertStmt.setInt(1, customerID);
      insertStmt.setString(2, isSenior);
      insertStmt.setString(3, timeSlot);
      insertStmt.setInt(4, duration);

      insertStmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }
  }

  public void insertTableInfo(String timestamp, String inputFileName, String generatedCSVName)
          throws SQLException {
    String insertTableInfo = "INSERT INTO SimulationMetadata (GenerationDateTime, InputParamatersFileName, GeneratedTableName) VALUES(?,?,?);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    try {
      connection = connectionManager.getConnection();
      insertStmt = connection.prepareStatement(insertTableInfo);

      insertStmt.setString(1, timestamp);
      insertStmt.setString(2, inputFileName);
      insertStmt.setString(3, generatedCSVName);
      insertStmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }
  }

  public Integer getBusiestHourOnADay(String date, String weather) throws SQLException {

    Connection connection = null;
    PreparedStatement insertStmt = null;
    PreparedStatement selectStmt;
    ResultSet results;

    String nameOfTable = weather + "Weather" + date;
    Integer timeSlot = 0;

    String selectTimeSlot = "SELECT timeSlot, COUNT(*) FROM " + nameOfTable
            + " GROUP BY timeSlot ORDER BY COUNT(*) DESC LIMIT 1;";

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectTimeSlot);
      results = selectStmt.executeQuery();

      if (results.next()) {
        timeSlot = results.getInt(1);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }

    return timeSlot;

  }

  public Integer getBusiestSeniorHourOnADay(String date, String weather) throws SQLException {

    Connection connection = null;
    PreparedStatement insertStmt = null;
    PreparedStatement selectStmt;
    ResultSet results;

    String nameOfTable = weather + "Weather" + date;
    Integer timeSlot = 0;

    String selectTimeSlot = "SELECT timeSlot, COUNT(*) FROM " + nameOfTable
            + " WHERE isCustomerSenior = 'true' GROUP BY timeSlot ORDER BY COUNT(*) DESC LIMIT 1;";

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectTimeSlot);
      results = selectStmt.executeQuery();

      if (results.next()) {
        timeSlot = results.getInt(1);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }

    return timeSlot;

  }

  public Integer compareTwoTimeSlotsOnTwoDates(String date1, String weather1, int timeSlot1,
                                               String date2, String weather2, int timeSlot2) throws SQLException {

    Connection connection = null;
    PreparedStatement insertStmt = null;
    PreparedStatement selectStmt;
    ResultSet results;

    String nameOfTable1 = weather1 + "Weather" + date1;
    String nameOfTable2 = weather2 + "Weather" + date2;

    Integer count1 = 0;
    Integer count2 = 0;

    String selectTimeSlot =
            " SELECT x.count1, y.count2 FROM(SELECT COUNT(*) as count1 FROM " + nameOfTable1
                    + " WHERE timeSlot = " + timeSlot1
                    + ") as x, (SELECT COUNT(*) as count2 FROM " + nameOfTable2 + " WHERE timeSlot = "
                    + timeSlot2
                    + ") as y;";

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectTimeSlot);
      results = selectStmt.executeQuery();

      if (results.next()) {
        count1 = results.getInt("count1");
        count2 = results.getInt("count2");
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }

    return count1 - count2;

  }

  public String getCustomerOnDateById(String date, String weather, Integer customerId) throws SQLException {

    Connection connection = null;
    PreparedStatement insertStmt = null;
    PreparedStatement selectStmt;
    ResultSet results;

    String nameOfTable = weather + "Weather" + date;
    Integer timeSlot = 0;
    Boolean isSenior = null;
    Integer visitDuration = 0;

    String selectTimeSlot = "SELECT isCustomerSenior, timeSlot, visitDuration FROM " + nameOfTable
            + " WHERE CustomerID = " + customerId + ";";

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectTimeSlot);
      results = selectStmt.executeQuery();

      if (results.next()) {
        isSenior = results.getBoolean(1);
        timeSlot = results.getInt(2);
        visitDuration = results.getInt(3);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }

    return "The customer with customer ID : " + customerId + " on : " + date + " has the following attributes:\n isSenior: " + isSenior + "\ntimeSlot: " + timeSlot + "\nDuration: " + visitDuration + " minutes";

  }

  public int getCustomerCount(String date, String weather) throws SQLException {

    Connection connection = null;
    PreparedStatement insertStmt = null;
    PreparedStatement selectStmt;
    ResultSet results;

    String nameOfTable = weather + "Weather" + date;
    Integer count = 0;

    String selectTimeSlot = "SELECT COUNT(*) FROM " + nameOfTable + ";";

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectTimeSlot);
      results = selectStmt.executeQuery();

      if (results.next()) {
        count = results.getInt(1);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }

    return count;
  }

  public List<Customer> getAllCustomers(String date, String weather) throws SQLException {

    Connection connection = null;
    PreparedStatement insertStmt = null;
    PreparedStatement selectStmt;
    ResultSet results;

    String nameOfTable = weather + "Weather" + date;
    Boolean isCustomerSenior;
    Integer timeSlot;
    Integer visitDuration;
    Customer customer;
    List<Customer> customers = new ArrayList<>();

    String selectCustomers = "SELECT isCustomerSenior, timeSlot, visitDuration FROM " + nameOfTable + ";";

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectCustomers);
      results = selectStmt.executeQuery();

      while (results.next()) {
        isCustomerSenior = results.getBoolean(1);
        timeSlot = results.getInt(2);
        visitDuration = results.getInt(3);
        customer = new Customer(timeSlot, isCustomerSenior, visitDuration);
        customers.add(customer);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }

    return customers;
  }

  public Customer getCustomerPrettyPrint(String date, String weather, Integer customerId) throws SQLException {

    Connection connection = null;
    PreparedStatement insertStmt = null;
    PreparedStatement selectStmt;
    ResultSet results;

    String nameOfTable = weather + "Weather" + date;
    Integer timeSlot = 0;
    Boolean isSenior = null;
    Integer visitDuration = 0;

    String selectTimeSlot = "SELECT isCustomerSenior, timeSlot, visitDuration FROM " + nameOfTable
        + " WHERE CustomerID = " + customerId + ";";

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectTimeSlot);
      results = selectStmt.executeQuery();

      if (results.next()) {
        isSenior = results.getBoolean(1);
        timeSlot = results.getInt(2);
        visitDuration = results.getInt(3);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }

    Customer customer = new Customer(timeSlot, isSenior, visitDuration);
    return customer;

  }

}