package tools;

import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.TimeSlot;
import cs5500.cs5500.model.Weather;
import cs5500.cs5500.tools.GenerateNumberOfCustomers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GenerateNumberOfCustomersTest {

  private Day day;
  private Date weekdayDate;
  private Date weekendDate;
  private Date holidayDate;
  private Date beforeHolidayDate;
  private Date dateLeadingToHoliday;

  private final String DATE_PATTERN = "yyyy-MM-dd";
  private final String WEEK_DAY_DATE = "2020-05-28";
  private final String WEEKEND_DATE = "2020-05-16";
  private final String HOLIDAY_DATE = "2019-7-4";
  private final String BEFORE_HOLIDAY_DATE = "2019-7-3";
  private final String DATE_LEADING_TO_HOLIDAY = "2019-7-2";

  private TimeSlot timeSlot;
  private Weather weather;

  private int numCustomersDaily;
  private int numTotalHours = 15;
  private int numPeakHours = 3;
  private List<HourlyTimeSlot> lunchHours = new ArrayList<>(
          Arrays.asList(HourlyTimeSlot.TWELVE_TO_THIRTEEN)
  );
  private List<HourlyTimeSlot> dinnerHours = new ArrayList<>(
          Arrays.asList(HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN, HourlyTimeSlot.EIGHTEEN_TO_NINETEEN)
  );

  @Before
  public void setUp() throws Exception {
    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    this.weekdayDate = dateFormat.parse(WEEK_DAY_DATE);
    this.weekendDate = dateFormat.parse(WEEKEND_DATE);
    this.holidayDate = dateFormat.parse(HOLIDAY_DATE);
    this.beforeHolidayDate = dateFormat.parse(BEFORE_HOLIDAY_DATE);
    this.dateLeadingToHoliday = dateFormat.parse(DATE_LEADING_TO_HOLIDAY);

    this.day = new Day(weekdayDate);
    weather = new Weather(Boolean.TRUE);
    timeSlot = new TimeSlot(day, HourlyTimeSlot.SIXTEEN_TO_SEVENTEEN);
  }

  @Test
  public void testGenerateNumberOfCustomers() {
    // 2020-05-28: not a holiday, Thursday, normal hour & peak hour
    numCustomersDaily = 900;
    Assert.assertEquals(57, GenerateNumberOfCustomers.generateNumberOfCustomers(day, timeSlot, weather, numCustomersDaily, numTotalHours, numPeakHours, lunchHours, dinnerHours));
    timeSlot.setHour(HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN);
    Assert.assertEquals(74, GenerateNumberOfCustomers.generateNumberOfCustomers(day, timeSlot, weather, numCustomersDaily, numTotalHours, numPeakHours, lunchHours, dinnerHours));

    // 2020-05-16: not a holiday, Saturday, good weather & bad weather
    timeSlot.setHour(HourlyTimeSlot.SIXTEEN_TO_SEVENTEEN);
    day.setDate(weekendDate);
    numCustomersDaily = 4000;
    Assert.assertEquals(373,GenerateNumberOfCustomers.generateNumberOfCustomers(day, timeSlot, weather, numCustomersDaily, numTotalHours, numPeakHours, lunchHours, dinnerHours));
    weather.setGoodWeather(Boolean.FALSE);
    Assert.assertEquals(267, GenerateNumberOfCustomers.generateNumberOfCustomers(day, timeSlot, weather, numCustomersDaily, numTotalHours, numPeakHours, lunchHours, dinnerHours));

    // 2019-7-4: holiday, Thursday, normal hour
    numCustomersDaily = 900;
    day.setDate(holidayDate);
    Assert.assertEquals(12, GenerateNumberOfCustomers.generateNumberOfCustomers(day, timeSlot, weather, numCustomersDaily, numTotalHours, numPeakHours, lunchHours, dinnerHours));

    // 2019-7-3: day before holiday, Wednesday
    numCustomersDaily = 1200;
    day.setDate(beforeHolidayDate);
    Assert.assertEquals(106, GenerateNumberOfCustomers.generateNumberOfCustomers(day, timeSlot, weather, numCustomersDaily, numTotalHours, numPeakHours, lunchHours, dinnerHours));

    // 2019-7-2: week leading up to a holiday, Tuesday
    numCustomersDaily = 1000;
    day.setDate(dateLeadingToHoliday);
    Assert.assertEquals(72, GenerateNumberOfCustomers.generateNumberOfCustomers(day, timeSlot, weather, numCustomersDaily, numTotalHours, numPeakHours, lunchHours, dinnerHours));
  }
}
