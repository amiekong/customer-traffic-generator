package model;

import cs5500.cs5500.model.Age;
import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.NameOfDay;
import cs5500.cs5500.model.TimeSlot;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AgeTest {

  private Age age;
  private Age ageTuesday;
  private Day day;
  private Day tuesday;
  private TimeSlot timeSlot;

  private Date weekdayDate;
  private Date tuesdayDate;
  private final String DATE_PATTERN = "yyyy-MM-dd";
  private final String WEEK_DAY_DATE = "2020-05-28";
  private final String TUESDAY_DATE = "2020-05-26";

  private NameOfDay seniorDay = NameOfDay.TUESDAY;
  private List<HourlyTimeSlot> seniorHours = new ArrayList<>(
          Arrays.asList(HourlyTimeSlot.TEN_TO_ELEVEN, HourlyTimeSlot.ELEVEN_TO_TWELVE)
  );

  @Before
  public void setUp() throws Exception {

    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    weekdayDate = dateFormat.parse(WEEK_DAY_DATE);
    tuesdayDate = dateFormat.parse(TUESDAY_DATE);

    day = new Day(weekdayDate);
    tuesday = new Day(tuesdayDate);
    timeSlot = new TimeSlot(day);
    age = new Age(day, timeSlot, seniorDay, seniorHours);
    ageTuesday = new Age(tuesday, timeSlot, seniorDay, seniorHours);
  }

  @Test
  public void testGetDay() {
    Assert.assertEquals(day, age.getDay());
    Assert.assertEquals(tuesday, ageTuesday.getDay());
  }

  @Test
  public void testGetTimeSlot() {
    Assert.assertEquals(timeSlot, age.getTimeSlot());
  }

  @Test
  public void testIsSeniorHour() {
    Assert.assertFalse(age.isSeniorHour(day, timeSlot, seniorDay, seniorHours));
  }

  @Test
  public void testIsSeniorHourTues() {
    TimeSlot timeSlot = new TimeSlot(tuesday, HourlyTimeSlot.TEN_TO_ELEVEN);
    Assert.assertTrue(ageTuesday.isSeniorHour(tuesday, timeSlot, seniorDay, seniorHours));
  }

  @Test
  public void testGenerateAgeOfCustomer() {
    timeSlot = new TimeSlot(day, HourlyTimeSlot.NINE_TO_TEN);
    int seniorCount = 0;
    for (int i = 0; i < 100; i++) {
      if (Boolean.TRUE.equals(age.generateAgeOfCustomer(day, timeSlot, seniorDay, seniorHours))) {
        seniorCount += 1;
      }
    }
    System.out.println("Number of senior customers are:" + seniorCount);
  }

  @Test
  public void testGenerateAgeOfCustomerSeniorHour() {
    timeSlot = new TimeSlot(tuesday, HourlyTimeSlot.ELEVEN_TO_TWELVE);
    int seniorCount = 0;
    for (int i = 0; i < 100; i++) {
      if (Boolean.TRUE.equals(age.generateAgeOfCustomer(tuesday, timeSlot, seniorDay, seniorHours))) {
        seniorCount += 1;
      }
    }
    System.out.println("Number of senior customers are:" + seniorCount);
  }
}


