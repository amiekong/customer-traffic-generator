package model;

import cs5500.cs5500.model.Age;
import cs5500.cs5500.model.Customer;
import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.Duration;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.NameOfDay;
import cs5500.cs5500.model.TimeSlot;
import cs5500.cs5500.model.Weather;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomerTest {

  private Age age;
  private Day day;
  private TimeSlot timeSlot;

  private Date weekdayDate;
  private Date newDate;
  private final String DATE_PATTERN = "yyyy-MM-dd";
  private final String WEEK_DAY_DATE = "2020-05-28";
  private final String NEW_DATE = "2020-05-29";

  private Duration duration;
  private Weather weather;
  private Customer customer;

  private NameOfDay seniorDay = NameOfDay.TUESDAY;
  private List<HourlyTimeSlot> seniorHours = new ArrayList<>(
          Arrays.asList(HourlyTimeSlot.TEN_TO_ELEVEN, HourlyTimeSlot.ELEVEN_TO_TWELVE)
  );
  private int avgDuration = 25;

  @Before
  public void setUp() throws Exception {
    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    weekdayDate = dateFormat.parse(WEEK_DAY_DATE);

    newDate = dateFormat.parse(NEW_DATE);

    day = new Day(weekdayDate);
    timeSlot = new TimeSlot(day);
    age = new Age(day, timeSlot, seniorDay, seniorHours);

    duration = new Duration(day, timeSlot, age);
    weather = new Weather(Boolean.TRUE);
    customer = new Customer(day, weather, timeSlot, seniorDay, seniorHours, avgDuration);
    customer.setAge(age);
    customer.setDuration(duration);
    customer.setTimeSlot(timeSlot);
    customer.setDay(day);
  }

  @Test
  public void testGetWeather() {
    Assert.assertEquals(weather, customer.getWeather());
  }

  @Test
  public void testGetDay() {
    Assert.assertEquals(day, customer.getDay());
  }

  @Test
  public void testSetDay() throws ParseException {
    Day newDay = new Day(newDate);
    customer.setDay(newDay);
    Assert.assertEquals(newDay, customer.getDay());
  }

  @Test
  public void getAge() {
    Assert.assertEquals(age, customer.getAge());
  }

  @Test
  public void setAge() {
    Day newDay = new Day(newDate);
    Age newAge = new Age(newDay, timeSlot, seniorDay, seniorHours);
    customer.setAge(newAge);
    Assert.assertEquals(newAge, customer.getAge());
  }

  @Test
  public void getTimeSlot() {
    Assert.assertEquals(timeSlot, customer.getTimeSlot());
  }

  @Test
  public void setTimeSlot() {
    Day newDay = new Day(newDate);
    TimeSlot newTimeSlot = new TimeSlot(newDay);
    customer.setTimeSlot(newTimeSlot);
    Assert.assertEquals(newTimeSlot, customer.getTimeSlot());
  }

  @Test
  public void getDuration() {
    Assert.assertEquals(duration, customer.getDuration());
  }

  @Test
  public void setDuration() {
    Day newDay = new Day(newDate);
    Duration newDuration = new Duration(newDay, timeSlot, age);
    customer.setDuration(newDuration);
    Assert.assertEquals(newDuration, customer.getDuration());
  }
}