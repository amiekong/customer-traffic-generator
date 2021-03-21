package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.NameOfDay;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DayTest {

  private Day day;
  private Date weekdayDate;
  private Date holidayDate;
  private Date dateLeadingToHoliday;
  private final String DATE_PATTERN = "yyyy-MM-dd";
  private final String WEEK_DAY_DATE = "2020-05-28";
  private final String HOLIDAY_DATE = "2020-05-25";
  private final String DATE_LEADING_TO_HOLIDAY = "2020-05-24";

  @Before
  public void setUp() throws Exception {
    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    this.weekdayDate = dateFormat.parse(WEEK_DAY_DATE);
    this.holidayDate = dateFormat.parse(HOLIDAY_DATE);
    this.dateLeadingToHoliday = dateFormat.parse(DATE_LEADING_TO_HOLIDAY);
    this.day = new Day(weekdayDate);
  }

  @Test
  public void testGetterAndSetter() throws Exception {
    Assert.assertEquals(day.getNameOfDay(), NameOfDay.THURSDAY);
    Assert.assertEquals(day.getHoliday(), 0);
    Assert.assertTrue(day.getWeekday());
    Assert.assertFalse(day.getIsInWeekLeadingToHoliday());
    day.setDate(this.holidayDate);
    Assert.assertEquals(day.getDate(), this.holidayDate);
    Assert.assertEquals(day.getNameOfDay(), NameOfDay.MONDAY);
    Assert.assertEquals(day.getHoliday(), 1);
    day.setDate(this.dateLeadingToHoliday);
    Assert.assertTrue(day.getIsInWeekLeadingToHoliday());
    Assert.assertEquals(day.getHoliday(), 2);
  }

  @Test
  public void testGetDate() {
    Assert.assertEquals(day.getDate(), this.weekdayDate);
  }

  @Test
  public void testSetDate() {
    day.setDate(this.holidayDate);
    Assert.assertEquals(day.getDate(), this.holidayDate);
  }

  @Test
  public void testGetNameOfDay() {
    Assert.assertEquals(day.getNameOfDay(), NameOfDay.THURSDAY);
  }

  @Test
  public void testGetWeekday() {
    Assert.assertTrue(day.getWeekday());
  }

  @Test
  public void testGetIsInWeekLeadingToHoliday() {
    day.setDate(this.dateLeadingToHoliday);
    Assert.assertTrue(day.getIsInWeekLeadingToHoliday());
  }

  @Test
  public void testGetHoliday() {
    day.setDate(this.dateLeadingToHoliday);
    Assert.assertEquals(day.getHoliday(), 2);
  }

}
