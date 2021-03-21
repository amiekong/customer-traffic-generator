package model;

import cs5500.cs5500.model.Age;
import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.Duration;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.NameOfDay;
import cs5500.cs5500.model.TimeSlot;
import cs5500.cs5500.model.Weather;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DurationTest {

  private Age age;
  private Day day;
  private TimeSlot timeSlot;

  private Date weekdayDate;
  private final String DATE_PATTERN = "yyyy-MM-dd";
  private final String WEEK_DAY_DATE = "2020-05-28";

  private Weather weather;
  private Duration duration;

  private NameOfDay seniorDay = NameOfDay.TUESDAY;
  private List<HourlyTimeSlot> seniorHours = new ArrayList<>(
          Arrays.asList(HourlyTimeSlot.TEN_TO_ELEVEN, HourlyTimeSlot.ELEVEN_TO_TWELVE)
  );

  @Before
  public void setUp() throws Exception {
    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    weekdayDate = dateFormat.parse(WEEK_DAY_DATE);

    day = new Day(weekdayDate);
    timeSlot = new TimeSlot(day);
    age = new Age(day, timeSlot, seniorDay, seniorHours);
    duration = new Duration(day, timeSlot, age);
  }

  @Test
  public void getDay() {
    Assert.assertEquals(day, duration.getDay());
  }

  @Test
  public void getTimeSlot() {
    Assert.assertEquals(timeSlot, duration.getTimeSlot());
  }

  @Test
  public void getAge() {
    Assert.assertEquals(age, duration.getAge());
  }
}