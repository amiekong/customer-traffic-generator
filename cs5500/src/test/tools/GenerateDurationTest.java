package tools;

import cs5500.cs5500.model.Age;
import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.TimeSlot;
import cs5500.cs5500.model.Weather;
import cs5500.cs5500.tools.GenerateDuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateDurationTest {
  private Day day;
  private Date weekdayDate;
  private Date weekendDate;

  private final String DATE_PATTERN = "yyyy-MM-dd";
  private final String WEEK_DAY_DATE = "2020-05-28";
  private final String WEEKEND_DATE = "2020-05-16";

  private TimeSlot timeSlot;
  private Weather weather;
  private Age age;

  private int avgDuration = 25;

  @Before
  public void setUp() throws Exception {
    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    this.weekdayDate = dateFormat.parse(WEEK_DAY_DATE);
    this.weekendDate = dateFormat.parse(WEEKEND_DATE);
    this.day = new Day(weekdayDate);

    this.weather = new Weather(Boolean.TRUE);
    this.timeSlot = new TimeSlot(day, HourlyTimeSlot.SIXTEEN_TO_SEVENTEEN);
    this.age = new Age();
  }

  @Test
  public void testGenerateDurationGeneral() {
    day.setDate(weekdayDate);
    timeSlot.setHour(HourlyTimeSlot.EIGHT_TO_NINE);
    int sum = 0;
    for (int i = 0; i < 1000; i++) {
      int result = GenerateDuration.generateDuration(age, day, weather, timeSlot, avgDuration).getDuration();
      Assert.assertTrue(result >= 6 && result <= 75);
      sum += result;
    }
    Assert.assertTrue((sum / 1000.0) < 27 && (sum / 1000.0) > 23);
  }

  @Test
  public void testGenerateDurationSeniorShoppingDuration() {
    age.setSenior(true);
    for (int i = 0; i < 100; i++) {
      int result = GenerateDuration.generateDuration(age, day, weather, timeSlot, avgDuration).getDuration();
      Assert.assertTrue(result >= 45 && result <= 60);
    }
  }

  @Test
  public void testGenerateDurationWeekends() {
    day.setDate(weekendDate);
    // good weather
    int result = GenerateDuration.generateDuration(age, day, weather, timeSlot, avgDuration).getDuration();
    Assert.assertTrue(result == 20 || result == 60);
    // bad weather
    weather.setGoodWeather(false);
    Assert.assertTrue(GenerateDuration.generateDuration(age, day, weather, timeSlot, avgDuration).getDuration() == 60);
  }

  @Test
  public void testGenerateDurationWeekdays() {
    // lunch time
    timeSlot.setHour(HourlyTimeSlot.TWELVE_TO_THIRTEEN);
    int sum = 0;
    int countLunchVisits = 0;
    for (int i = 0; i < 10000; i++) {
      int result = GenerateDuration.generateDuration(age, day, weather, timeSlot, avgDuration).getDuration();
      if (result == 10) {
        countLunchVisits++;
      }
      Assert.assertTrue(result >= 6 && result <= 75);
      sum += result;
    }
    Assert.assertTrue((sum / 10000.0) < 23 && (sum / 10000.0) > 20);
    Assert.assertTrue(countLunchVisits > 2100 && countLunchVisits < 2500);

    // dinner time
    timeSlot.setHour(HourlyTimeSlot.EIGHTEEN_TO_NINETEEN);
    sum = 0;
    for (int i = 0; i < 10000; i++) {
      int result = GenerateDuration.generateDuration(age, day, weather, timeSlot, avgDuration).getDuration();
      Assert.assertTrue(result >= 6 && result <= 75);
      sum += result;
    }
    Assert.assertTrue((sum / 10000.0) < 25 && (sum / 10000.0) > 22);
  }
}

