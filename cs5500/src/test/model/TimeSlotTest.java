package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.TimeSlot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimeSlotTest {

  private Day day;
  private Day weekend;
  private TimeSlot timeSlot;
  private TimeSlot timeSlotHour;

  private Date weekdayDate;
  private Date weekendDate;
  private final String DATE_PATTERN = "yyyy-MM-dd";
  private final String WEEK_DAY_DATE = "2020-05-28";
  private final String SET_DATE = "2020-05-29";
  private final String WEEKEND_DAY_DATE = "2020-05-30";

  private List<HourlyTimeSlot> lunchHours = new ArrayList<>(
          Arrays.asList(HourlyTimeSlot.TWELVE_TO_THIRTEEN)
  );
  private List<HourlyTimeSlot> dinnerHours = new ArrayList<>(
          Arrays.asList(HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN, HourlyTimeSlot.EIGHTEEN_TO_NINETEEN)
  );


  @Before
  public void setUp() throws Exception {
    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    weekdayDate = dateFormat.parse(WEEK_DAY_DATE);
    weekendDate = dateFormat.parse(WEEKEND_DAY_DATE);

    day = new Day(weekdayDate);
    weekend = new Day(weekendDate);
    timeSlot = new TimeSlot(day);
    timeSlotHour = new TimeSlot(day, HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN);
  }

  @Test
  public void getDay() {
    Assert.assertEquals(day, timeSlot.getDay());
  }

  @Test
  public void setDay() throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    Date newDate = dateFormat.parse(SET_DATE);
    Day newDay = new Day(newDate);

    timeSlot.setDay(newDay);
    Assert.assertEquals(newDay, timeSlot.getDay());

  }

  @Test
  public void getHour() {
    Assert.assertEquals(HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN, timeSlotHour.getHour());
  }

  @Test
  public void setHour() {
    timeSlotHour.setHour(HourlyTimeSlot.TWELVE_TO_THIRTEEN);
    Assert.assertEquals(HourlyTimeSlot.TWELVE_TO_THIRTEEN, timeSlotHour.getHour());
  }

  @Test
  public void isPeakTimeWeekday() {
    Assert.assertTrue(timeSlotHour.isPeakTime(lunchHours, dinnerHours));
  }

  @Test
  public void isPeakTimeWeekend() {
    timeSlotHour.setDay(weekend);
    Assert.assertFalse(timeSlotHour.isPeakTime(lunchHours, dinnerHours));
  }
}