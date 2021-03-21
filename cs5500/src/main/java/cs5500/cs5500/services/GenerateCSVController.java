package cs5500.cs5500.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import cs5500.cs5500.dao.CSVGeneratorDAO;
import cs5500.cs5500.model.Customer;
import java.util.List;
import cs5500.cs5500.model.Constants;
import cs5500.cs5500.model.InputJson;
import cs5500.cs5500.tools.GenerateCSV;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class GenerateCSVController {

  @RequestMapping(value = "/customer-generation", method = RequestMethod.POST)
  public String generateCSV(@RequestBody Map<String, Object> inputJsonMap) throws Exception {
    System.out.println("Received input json: " + inputJsonMap);

    // validate whether input json is in correct format
    try {
      validateInput(inputJsonMap);
    } catch (Exception e) {
      return e.getMessage();
    }

    // passing input payload to InputJson object for next process.
    final Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(inputJsonMap);
    InputJson inputJson = gson.fromJson(jsonElement, InputJson.class);

    try {
      GenerateCSV.generateCSV(inputJson);
    } catch (RuntimeException e) {
      throw new URLNotFoundException("Generate CSV Request Not Found");
    } catch (Exception e) {
      System.out.println(e.fillInStackTrace());
    }

    return "Successfully generated csv into db with following payload: " + inputJsonMap;

  }

  @GetMapping(value = "/busiest-senior-hour")
  public String getBusiestSeniorHour()
      throws Exception {
    return "get-date-and-weather";
  }

  @PostMapping(value = "/busiest-senior-hour")
  public String getBusiestSeniorHour(@RequestParam String date, @RequestParam String weather, HttpSession session)
      throws Exception {

    session.setAttribute("weather", weather);
    session.setAttribute("date", date);

    CSVGeneratorDAO csvGeneratorDAO = new CSVGeneratorDAO();
    Integer timeSlot = 0;

    try {
      timeSlot = csvGeneratorDAO.getBusiestSeniorHourOnADay(date, weather);
      session.setAttribute("timeslot", timeSlot);
    } catch (RuntimeException e) {
      throw new URLNotFoundException("Get Busiest Senior Hour Request Not Found");
    }

    return "busiest-senior-hour";

  }

  @RequestMapping(value = "/comparison/date1/{date1}/weather1/{weather1}/timeSlot1/{timeslot1}/date2/{date2}/weather2/{weather2}/timeSlot2/{timeslot2}", method = RequestMethod.GET)
  public String getComparison(@PathVariable String date1, @PathVariable String weather1,
      @PathVariable int timeslot1, @PathVariable String date2, @PathVariable String weather2,
      @PathVariable int timeslot2)
      throws Exception {
    CSVGeneratorDAO csvGeneratorDAO = new CSVGeneratorDAO();
    int difference = 0;

    System.out.println("Received the first input date: " + date1);
    System.out.println("Received weather for the first input date: " + weather1 + " weather!");
    System.out.println("Received time slot for the first input date: " + timeslot1);
    System.out.println("Received the second input date: " + date2);
    System.out.println("Received weather for the second input date: " + weather2 + " weather!");
    System.out.println("Received time slot for the second input date: " + timeslot2);

    try {
      difference = csvGeneratorDAO
          .compareTwoTimeSlotsOnTwoDates(date1, weather1, timeslot1, date2, weather2, timeslot2);
    } catch (RuntimeException e) {
      throw new URLNotFoundException("Get Comparison Request Not Found");
    } 

    return "The difference in number of customers between the two time slots is: "
        + difference;

  }

  @RequestMapping(value = "/customer/date/{date}/weather/{weather}/id/{customerId}", method = RequestMethod.GET)
  public String getCustomer(@PathVariable String date, @PathVariable String weather,
      @PathVariable Integer customerId)
      throws Exception {
    CSVGeneratorDAO csvGeneratorDAO = new CSVGeneratorDAO();
    String output = null;

    System.out.println("Received input date: " + date);
    System.out.println("Received input weather: " + weather + " weather!");
    System.out.println("Received input customer ID: " + customerId);

    try {
      output = csvGeneratorDAO.getCustomerOnDateById(date, weather, customerId);
    } catch (RuntimeException e) {
      throw new URLNotFoundException("Get Customer Request Not Found");
    } catch (Exception e) {
      System.out.println("Invalid Input!");
      System.out.println(e.fillInStackTrace());
    }
    return output;
  }

  @GetMapping(value = "/customer")
  public String getCustomerFrontEnd()
      throws Exception {
    return "get-customer";
  }

  @PostMapping(value = "/customer")
  public String getCustomerAndReturn(@RequestParam String date, @RequestParam String weather, @RequestParam String customerId, HttpSession session)
      throws Exception {

    session.setAttribute("weather", weather);
    session.setAttribute("date", date);
    session.setAttribute("customerId", customerId);

    CSVGeneratorDAO csvGeneratorDAO = new CSVGeneratorDAO();
    Customer customer = null;

    System.out.println("Received input date: " + date);
    System.out.println("Received input weather: " + weather + " weather!");
    System.out.println("Received input customer ID: " + customerId);

    try {
      customer = csvGeneratorDAO.getCustomerPrettyPrint(date, weather, Integer.parseInt(customerId));
      session.setAttribute("customer", customer);
    } catch (Exception e) {
      System.out.println("Invalid Input!");
      System.out.println(e.fillInStackTrace());
    }

    return "print-customer";

  }

  @GetMapping(value = "/customers")
  public String getCustomersFrontEnd()
      throws Exception {
    return "get-customers";
  }

  @PostMapping(value = "/customers")
  public String getCustomersAndReturn(@RequestParam String date, @RequestParam String weather, HttpSession session)
      throws Exception {

    session.setAttribute("weather", weather);
    session.setAttribute("date", date);

    CSVGeneratorDAO csvGeneratorDAO = new CSVGeneratorDAO();
    List<Customer> customers;
    session.setAttribute("numCustomers", getCustomerCount(date, weather));

    System.out.println("Received input date: " + date);
    System.out.println("Received input weather: " + weather + " weather!");

    try {
      customers = csvGeneratorDAO.getAllCustomers(date, weather);
      session.setAttribute("customers", customers);
    } catch (Exception e) {
      System.out.println("Invalid Input!");
      System.out.println(e.fillInStackTrace());
    }

    return "print-customers";

  }

  @RequestMapping(value = "/customer-count/date/{date}/weather/{weather}/", method = RequestMethod.GET)
  public int getCustomerCount(@PathVariable String date, @PathVariable String weather)
      throws Exception {
    CSVGeneratorDAO csvGeneratorDAO = new CSVGeneratorDAO();

    System.out.println("Received input date: " + date);
    System.out.println("Received input weather: " + weather + " weather!");

    Integer count = 0;

    try {
      count = csvGeneratorDAO.getCustomerCount(date, weather);
    } catch (RuntimeException e) {
      throw new URLNotFoundException("Get Customer Count Request Not Found");
    } catch (Exception e) {
      System.out.println("Invalid Input!");
      System.out.println(e.fillInStackTrace());
    }
    return count;
  }

  @RequestMapping(value = "/busiest-hour/date/{date}/weather/{weather}", method = RequestMethod.GET)
  public String getBusiestHour(@PathVariable String date, @PathVariable String weather)
      throws Exception {
    CSVGeneratorDAO csvGeneratorDAO = new CSVGeneratorDAO();
    Integer timeSlot = 0;

    System.out.println("Received input date: " + date);
    System.out.println("Received input weather: " + weather + " weather!");

    try {
      timeSlot = csvGeneratorDAO.getBusiestHourOnADay(date, weather);
    } catch (RuntimeException e) {
      throw new URLNotFoundException("Get Busiest Hour Request Not Found");
    } catch (Exception e) {
      System.out.println(e.fillInStackTrace());
    }
    return "The timeslot with the largest number of customers on the given date is: "
        + timeSlot;
  }

  /*
  Validate whether the given object is in correct date format and it's a valid date
   */
  private void validateDate(Object obj, String entity) throws IllegalArgumentException {
    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
    try {
      dateFormat.setLenient(false);
      Date date = dateFormat.parse(obj.toString());
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Please enter a valid date in the format YYYY-MM-DD for " + entity);
    }
  }

  /*
  Validate whether the given dates are in correct order (i.e. ending date should not be before starting date)
   */
  private void validateDateOrder(Object obj1, Object obj2, String entity1, String entity2) throws Exception {
    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
    Date date1 = dateFormat.parse(obj1.toString());
    Date date2 = dateFormat.parse(obj2.toString());
    if (date2.before(date1)) {
      throw new IllegalArgumentException("Please enter valid date range: " + entity2 + " should not be before " + entity1);
    }
  }

  /*
  Validate whether the given object is a non negative integer
   */
  private void validateNonNegativeInt(Object obj, String entity) throws IllegalArgumentException {
    if (!obj.toString().matches("\\d+")) {
      throw new IllegalArgumentException("Please enter a non-negative integer for " + entity);
    }
  }

  /*
  Validate whether the given object is a valid hour in correct format (0-23)
   */
  private void validateHour(String str, String entity) throws IllegalArgumentException {
    if (Integer.parseInt(str) < 0 || Integer.parseInt(str) > 23) {
      throw new IllegalArgumentException("Please enter a valid hour (0-23) for " + entity);
    }
  }

  /*
  Validate whether the given two hours are in correct order (i.e. the starting hour should be before the ending hour)
   */
  private void validateHourOrder(String str1, String str2, String entity1, String entity2) throws IllegalArgumentException {
    if (Integer.parseInt(str1) >= Integer.parseInt(str2)) {
      throw new IllegalArgumentException("Please enter a valid hour range: " + entity1 + " should be before " + entity2);
    }
  }

  /*
  Validate whether the given object is an array of desired size
   */
  private void validateArraySize(Object obj, int size, String entity) throws IllegalArgumentException {
    ArrayList arrayList = (ArrayList) obj;
    if (arrayList.size() != size) {
      throw new IllegalArgumentException("Please enter an array of size " + size + " for " + entity);
    }
  }

  /*
  Validate whether the given object is a valid day in correct format (1-7)
   */
  private void validateDay(Object obj, String entity) throws IllegalArgumentException {
    if (Integer.parseInt(obj.toString()) < 1 || Integer.parseInt(obj.toString()) > 7) {
      throw new IllegalArgumentException("Please enter a valid day (1-7) for " + entity);
    }
  }

  /*
  Validate whether the given object is in a correct hour range format (INT1-INT2)
   */
  private void validateHourRange(Object obj, String entity) throws IllegalArgumentException {
    if (!obj.toString().matches("\\d+-\\d+")) {
      throw new IllegalArgumentException("Please enter a valid hour range (INT1-INT2) for " + entity);
    }
  }

  /*
  Validate input json
   */
  private void validateInput(Map<String, Object> inputJsonMap) throws Exception {
    // starting date and ending date should be a valid date in correct format
    // AND starting date and ending date should in correct order (i.e. ending date should not be before starting date)
    validateDate(inputJsonMap.get("StartingDate"), "StartingDate");
    validateDate(inputJsonMap.get("EndingDate"), "EndingDate");
    validateDateOrder(inputJsonMap.get("StartingDate"),inputJsonMap.get("EndingDate"), "StartingDate", "EndingDate");


    // open time, close time should be non negative integer & and should be valid hour
    validateNonNegativeInt(inputJsonMap.get("OpenTime"), "OpenTime");
    validateNonNegativeInt(inputJsonMap.get("CloseTime"), "CloseTime");
    validateHour(inputJsonMap.get("OpenTime").toString(), "OpenTime");
    validateHour(inputJsonMap.get("CloseTime").toString(), "CloseTime");

    // daily average number (of customers) should be an array of size 7, representing daily traffic for each day
    // AND all element in the array should be non-negative integers
    validateArraySize(inputJsonMap.get("DailyAveNumber"), 7, "DailyAveNumber");
    for (Object num: (ArrayList) inputJsonMap.get("DailyAveNumber")) {
      validateNonNegativeInt(num, "each element in DailyAveNumber");
    }

    // daily average duration should be a non negative integer
    validateNonNegativeInt(inputJsonMap.get("DailyAveDuration"), "DailyAveDuration");

    // lunch hour, dinner hour and senior hour should be an hour range in format "HOUR1-HOUR2"
    // AND the starting and ending hours should be valid hours
    // AND the starting hour should be before the ending hour
    Object lunchHourObj = inputJsonMap.get("LunchHour");
    validateHourRange(lunchHourObj, "LunchHour");
    String[] lunchHours = lunchHourObj.toString().split("-");
    for (String hour: lunchHours) {
      validateHour(hour, "LunchHour");
    }
    validateHourOrder(lunchHours[0], lunchHours[1], "lunch hour start time", "lunch hour end time");

    // dinner hour
    Object dinnerHourObj = inputJsonMap.get("DinnerHour");
    validateHourRange(dinnerHourObj, "DinnerHour");
    String[] dinnerHours = dinnerHourObj.toString().split("-");
    for (String hour: dinnerHours) {
      validateHour(hour, "DinnerHour");
    }
    validateHourOrder(dinnerHours[0], dinnerHours[1], "dinner hour start time", "dinner hour end time");

    // senior hour
    Object seniorHourObj = inputJsonMap.get("SeniorHour");
    validateHourRange(seniorHourObj, "SeniorHour");
    String[] seniorHours = seniorHourObj.toString().split("-");
    for (String hour: seniorHours) {
      validateHour(hour, "SeniorHour");
    }
    validateHourOrder(seniorHours[0], seniorHours[1], "senior hour start start", "senior hour end time");

    // senior day should be a non-negative integer
    // AND it should be in range 1-7 representing any day from Monday thru Sunday
    validateNonNegativeInt(inputJsonMap.get("SeniorDay"), "SeniorDay");
    validateDay(inputJsonMap.get("SeniorDay"), "SeniorDay");
  }
}

