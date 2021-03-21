package cs5500.cs5500.tools;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import cs5500.cs5500.dao.CSVGeneratorDAO;
import cs5500.cs5500.model.Constants;
import cs5500.cs5500.model.Customer;
import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.Duration;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.InputJson;
import cs5500.cs5500.model.NameOfDay;
import cs5500.cs5500.model.TimeSlot;
import cs5500.cs5500.model.Weather;

public class GenerateCSV {

  private static final String DATE_FORMAT = "yyyy.MM.dd HH.mm.ss";
  private static final String DB_DATE_FORMAT = "yyyyMMddHHmmss";
  private static final String GOOD_WEATHER = "GoodWeather";
  private static final String BAD_WEATHER = "BadWeather";
  private static final String CSV_SUFFIX = ".csv";

  /**
   * Main function that will generate the CSV. Prompts the user to enter the date for which the CSV
   * is needed, along with the time slot and the weather on the particular day.
   *
   * @param inputJson is the input json object.
   * @throws ParseException catches parsing exception from parsing date, integer and float.
   * @throws IOException    catches exceptions during inputs and making CSV file.
   */
  public static void generateCSV(InputJson inputJson) throws ParseException, IOException, SQLException {

    // Read from JSON file
    String currentTimestamp = new SimpleDateFormat(DATE_FORMAT)
        .format(new Timestamp(System.currentTimeMillis()));
    String currentTimeForTableName = new SimpleDateFormat(DB_DATE_FORMAT)
        .format(new Timestamp(System.currentTimeMillis()));

//    final Gson gson = new Gson();
//    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//
//    System.out.println("Enter the name of the file with input parameters : ");
//    String inputFileName = myObj.nextLine();  // Read user input
//    System.out.println("Name of the file is: " + inputFileName);  // Output user input
//
//    final BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName));
//    InputJson inputJson = gson.fromJson(bufferedReader, InputJson.class);

    // Here is a little hecky. Hard code this inputFileName to prevent changing the metadata schema.
    String inputFileName = "input.json";

    if (inputJson.getDailyAveNumber().size() != 7) {
      System.out.println("DailyAveNumber should be 7 numbers indicating from Sunday to Saturday.");
    }

    // Get start date and end date from input json
    Date startDate;
    Date endDate;
    String startDateS = inputJson.getStartingDate();
    String endDateS = inputJson.getEndingDate();
    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
    startDate = dateFormat.parse(startDateS);
    endDate = dateFormat.parse(endDateS);

    // Get other parameters from input json
    int numTotalHours = inputJson.getNumOfTotalHours();
    int numPeakHours = inputJson.getNumOfPeakHours();
    int avgDuration = inputJson.getDailyAveDuration();
    NameOfDay seniorDay = inputJson.getSeniorDay();
    List<HourlyTimeSlot> seniorHours = inputJson.getSeniorHour();
    List<HourlyTimeSlot> allTimeSlots = inputJson.getAllTimeSlots();
    List<HourlyTimeSlot> lunchHours = inputJson.getLunchHour();
    List<HourlyTimeSlot> dinnerHours = inputJson.getDinnerHour();

    int numCustomersDaily;
    Weather weather;
    String fileName;
    String tableName;
    String dateInFileName;
    Date currentDate = endDate; // start with the endDate

    // generate for GOOD WEATHER
    weather = new Weather(Boolean.TRUE);
    // multiple dates using for loop
    while (currentDate.after(startDate) || currentDate.equals(startDate)) {
      // create a new csv
      dateInFileName = dateFormat.format(currentDate);
      dateInFileName = dateInFileName.replaceAll("-", "");
      fileName = dateInFileName + GOOD_WEATHER + CSV_SUFFIX;
      tableName = GOOD_WEATHER + dateInFileName;

      Day day = new Day(currentDate);
      // get num of average daily customers
      numCustomersDaily = inputJson.getDailyAvgCustomersOfDay(day.getNameOfDay());

      System.out.println(
          "Currently working on CSV File and Database Table for Good Weather conditions on : "
              + dateInFileName);
      generateCSVandAddToDatabase(currentDate, day, numTotalHours, numCustomersDaily, seniorDay,
          seniorHours, avgDuration, weather, allTimeSlots, numPeakHours, lunchHours, dinnerHours,
          fileName, tableName, currentTimestamp, inputFileName);

      // update endDate to the previous date after done with current date
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(currentDate);
      calendar.add(Calendar.DATE, -1);
      currentDate = calendar.getTime();
    }

    // generate for BAD WEATHER
    weather = new Weather(Boolean.FALSE);
    currentDate = endDate; // re-initialize current Date to start with endDate

    while (currentDate.after(startDate) || currentDate.equals(startDate)) {
      // create a new csv
      dateInFileName = dateFormat.format(currentDate);
      dateInFileName = dateInFileName.replaceAll("-", "");
      fileName = dateInFileName + BAD_WEATHER + CSV_SUFFIX;
      tableName = BAD_WEATHER + dateInFileName;

      // start with the end day
      Day day = new Day(currentDate);
      // get num of average daily customers
      numCustomersDaily = inputJson.getDailyAvgCustomersOfDay(day.getNameOfDay());

      System.out.println(
          "Currently working on CSV File and Database Table for Bad Weather conditions on : "
              + dateInFileName);
      generateCSVandAddToDatabase(currentDate, day, numTotalHours, numCustomersDaily, seniorDay,
          seniorHours, avgDuration, weather, allTimeSlots, numPeakHours, lunchHours, dinnerHours,
          fileName, tableName, currentTimestamp, inputFileName);

      // update endDate to the previous date after done with current date
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(currentDate);
      calendar.add(Calendar.DATE, -1);
      currentDate = calendar.getTime();
    }
    System.out.println("Completed!");
  }

  private static int getTimeSlotNumber(HourlyTimeSlot timeSlot, List<HourlyTimeSlot> allTimeSlots) {
    int timeSlotNumber = 0;
    for (int i = 0; i < allTimeSlots.size(); i++) {
      if (allTimeSlots.get(i) == timeSlot) {
        timeSlotNumber = i + 1;
        break;
      }
    }
    return timeSlotNumber;
  }

  private static void generateCSVandAddToDatabase(Date currentDate, Day day, int numTotalHours,
      int numCustomersDaily, NameOfDay seniorDay, List<HourlyTimeSlot> seniorHours, int avgDuration,
      Weather weather, List<HourlyTimeSlot> allTimeSlots, int numPeakHours,
      List<HourlyTimeSlot> lunchHours, List<HourlyTimeSlot> dinnerHours, String fileName,
      String tableName, String currentTimestamp, String inputFileName)
      throws IOException, SQLException {

    FileWriter csvWriter;
    Integer customerID = 1;
    CSVGeneratorDAO csvGeneratorDAO = new CSVGeneratorDAO();
    csvGeneratorDAO.createCustomerTable(tableName);

    csvGeneratorDAO.insertTableInfo(currentTimestamp, inputFileName, tableName);

    csvWriter = new FileWriter(fileName);
    csvWriter.append("Random CustomerID");
    csvWriter.append(",");
    csvWriter.append("isSenior");
    csvWriter.append(",");
    csvWriter.append("timeSlot");
    csvWriter.append(",");
    csvWriter.append("Duration (in minutes)");
    csvWriter.append("\n");

    // generate for all every timeslot
    for (HourlyTimeSlot ts : allTimeSlots) {
      TimeSlot timeSlot = new TimeSlot(day, ts);
      Integer numCustomersHourly = GenerateNumberOfCustomers
          .generateNumberOfCustomers(day, timeSlot, weather, numCustomersDaily, numTotalHours,
              numPeakHours, lunchHours, dinnerHours);

      Integer timeSlotNumber = getTimeSlotNumber(timeSlot.getHour(), allTimeSlots);
      for (int i = 0; i < numCustomersHourly; i++) {
        Customer customer = new Customer(day, weather, timeSlot, seniorDay, seniorHours,
            avgDuration);
        Duration duration = customer.getDuration();
        Integer time = duration.getDuration();
        Boolean isSenior = customer.getSenior();

        csvWriter.append(Integer.toString(customerID));
        csvWriter.append(",");
        csvWriter.append(Boolean.toString(isSenior));
        csvWriter.append(",");
        csvWriter.append(Integer.toString(timeSlotNumber));
        csvWriter.append(",");
        csvWriter.append(Integer.toString(time));
        csvWriter.append("\n");

        csvGeneratorDAO.insertCustomer(customerID, Boolean.toString(isSenior),
            Integer.toString(timeSlotNumber), time, tableName);

        customerID++;
      }
    }
    // close the csv writer and re-initialize customer id
    csvWriter.flush();
    csvWriter.close();
  }
}

//    ------------  BELOW IS THE CODE FOR SPRINT 1 - USER INPUT FROM COMMAND LINE ---------------

//    // I N P U T S
//    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//
//    // D A T E
//    System.out.println("Enter date in the format yyyy-MM-dd");
//    String dateS = myObj.nextLine();  // Read user input
//    System.out.println("Date is: " + dateS);  // Output user input
//    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
//    date = dateFormat.parse(dateS);
//    day = new Day(date);
//    Boolean isWeekday = day.getWeekday();
//    Integer isHoliday = day.getHoliday();
//    if (isWeekday) {
//      System.out.println("Date is a weekday!");
//    } else {
//      System.out.println("Date is a weekend!");
//    }
//
//    if (isHoliday == 0) {
//      System.out.println("Date is not a holiday!");
//    } else if (isHoliday == 1) {
//      System.out.println("Date is a holiday!");
//    } else {
//      System.out.println("Date is in a week leading to a holiday!");
//    }
//
//    // W E A T H E R
//    System.out.println("Enter Weather : 1 for good weather and 2 for bad weather");
//    String weatherS = myObj.nextLine();  // Read user input
//    if (weatherS.equals("1")) {
//      weather = new Weather(Boolean.TRUE);
//      System.out.println("Weather is good!");
//    } else {
//      weather = new Weather(Boolean.FALSE);
//      System.out.println("Weather is bad!");
//    }
//
//    // T I M E  S L O T
//    System.out.println(
//        "Enter time-slot for which the customers are needed (1-12) : 1 for 6am to 7am, 2 for 7am to 8am ...");
//    Integer timeS = Integer.parseInt(myObj.nextLine());  // Read user input
//    HourlyTimeSlot hourlyTimeSlot = intToHourlyTimeSlot(timeS);
//    System.out.println("TimeSlot is: " + hourlyTimeSlot.toString());  // Output user input
//    timeSlot = new TimeSlot(day, hourlyTimeSlot);
//
//    // N U M  C U S T O M E R S
//    Integer numCustomers = GenerateNumberOfCustomers
//        .generateNumberOfCustomers(day, timeSlot, weather);
//
//    // generate CSV
//    FileWriter csvWriter = new FileWriter("new.csv");
//    csvWriter.append("Random CustomerID");
//    csvWriter.append(",");
//    csvWriter.append("isSenior");
//    csvWriter.append(",");
//    csvWriter.append("Duration (in minutes)");
//    csvWriter.append("\n");
//
//    // Keep track of number of senior customers
//    Integer seniorNum = 0;
//
//    for (int i = 0; i < numCustomers; i++) {
//
//      Customer customer = new Customer(day, weather, timeSlot);
//      Duration duration = customer.getDuration();
//      Boolean isSenior = customer.getSenior();
//      if (isSenior) {
//        seniorNum += 1;
//      }
//      Integer time = duration.getDuration();
//      csvWriter.append(Integer.toString(i + 1));
//      csvWriter.append(",");
//      csvWriter.append(Boolean.toString(isSenior));
//      csvWriter.append(",");
//      csvWriter.append(Integer.toString(time));
//      csvWriter.append("\n");
//    }
//
//    csvWriter.flush();
//    csvWriter.close();
//
//    System.out.println("============================================================");
//    System.out.println("Total number of customers that visited during this time : " + numCustomers);
//    System.out.println(
//        "Total number of staff members needed during this time : " + numOfStaffMembersNeeded(
//            numCustomers));
//    System.out.println("Total number of Senior customers : " + seniorNum);
//    System.out.println("CSV Generated!");
//    System.out.println("============================================================");

//  /**
//   * Generate the number of staff members needed for the store in a time slot.
//   *
//   * @param numCustomers number of the predicted customers in a time slot.
//   * @return number of staff members needed.
//   */
//  private static Integer numOfStaffMembersNeeded(Integer numCustomers) {
//    return Math.round(numCustomers / Constants.NUM_CUSTOMERS_PER_STAFF_MEMBER);
//  }

//  /**
//   * Converts the interger time slot into an HourlyTimeSlot object that can be used to generate the
//   * CSV.
//   *
//   * @param integerInput integer input by the user.
//   * @return HourlyTimeSlot object for use in the other functions.
//   */
//  private static HourlyTimeSlot intToHourlyTimeSlot(Integer integerInput) {
//
//    HourlyTimeSlot hourlyTimeSlot = null;
//    switch (integerInput) {
//      case 1:
//        hourlyTimeSlot = HourlyTimeSlot.SIX_TO_SEVEN;
//        break;
//      case 2:
//        hourlyTimeSlot = HourlyTimeSlot.SEVEN_TO_EIGHT;
//        break;
//      case 3:
//        hourlyTimeSlot = HourlyTimeSlot.EIGHT_TO_NINE;
//        break;
//      case 4:
//        hourlyTimeSlot = HourlyTimeSlot.NINE_TO_TEN;
//        break;
//      case 5:
//        hourlyTimeSlot = HourlyTimeSlot.TEN_TO_ELEVEN;
//        break;
//      case 6:
//        hourlyTimeSlot = HourlyTimeSlot.ELEVEN_TO_TWELVE;
//        break;
//      case 7:
//        hourlyTimeSlot = HourlyTimeSlot.TWELVE_TO_THIRTEEN;
//        break;
//      case 8:
//        hourlyTimeSlot = HourlyTimeSlot.THIRTEEN_TO_FOURTEEN;
//        break;
//      case 9:
//        hourlyTimeSlot = HourlyTimeSlot.FOURTEEN_TO_FIFTEEN;
//        break;
//      case 10:
//        hourlyTimeSlot = HourlyTimeSlot.FIFTEEN_TO_SIXTEEN;
//        break;
//      case 11:
//        hourlyTimeSlot = HourlyTimeSlot.SIXTEEN_TO_SEVENTEEN;
//        break;
//      case 12:
//        hourlyTimeSlot = HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN;
//        break;
//      case 13:
//        hourlyTimeSlot = HourlyTimeSlot.EIGHTEEN_TO_NINETEEN;
//        break;
//      case 14:
//        hourlyTimeSlot = HourlyTimeSlot.NINETEEN_TO_TWENTY;
//        break;
//      case 15:
//        hourlyTimeSlot = HourlyTimeSlot.TWENTY_TO_TWENTYONE;
//        break;
//    }
//    return hourlyTimeSlot;
//  }
