package cs5500.cs5500.model;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayType;
import java.util.Calendar;
import java.util.Date;

import de.jollyday.HolidayManager;

public class Day {

  private Date date;
  private NameOfDay nameOfDay;
  private boolean isWeekday;
  private boolean isInWeekLeadingToHoliday;
  private int isHoliday;

  public Day(Date date) {
    this.date = date;
    this.nameOfDay = getNameOfDay();
    this.isWeekday = checkIsWeekday();
    this.isHoliday = checkIsHoliday();
    this.isInWeekLeadingToHoliday = checkIsWeekLeadingToHoliday();
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
    this.nameOfDay = getNameOfDay();
    this.isWeekday = checkIsWeekday();
    this.isHoliday = checkIsHoliday();
    this.isInWeekLeadingToHoliday = checkIsWeekLeadingToHoliday();
  }

  // TODO(Rui): put magic number into constant class.
  public NameOfDay getNameOfDay() {
    NameOfDay nameOfDay = NameOfDay.SUNDAY;
    Calendar cal = Calendar.getInstance();
    cal.setTime(this.date);
    switch (cal.get(Calendar.DAY_OF_WEEK)) {
      case 1:
        nameOfDay = NameOfDay.SUNDAY;
        break;
      case 2:
        nameOfDay = NameOfDay.MONDAY;
        break;
      case 3:
        nameOfDay = NameOfDay.TUESDAY;
        break;
      case 4:
        nameOfDay = NameOfDay.WEDNESDAY;
        break;
      case 5:
        nameOfDay = NameOfDay.THURSDAY;
        break;
      case 6:
        nameOfDay = NameOfDay.FRIDAY;
        break;
      case 7:
        nameOfDay = NameOfDay.SATURDAY;
        break;
    }
    return nameOfDay;
  }

  public boolean getWeekday() {
    return this.isWeekday;
  }

  public boolean getIsInWeekLeadingToHoliday() {
    return this.isInWeekLeadingToHoliday;
  }

  public int getHoliday() {
    return this.isHoliday;
  }

  private boolean checkIsWeekday() {

    return !(this.nameOfDay == NameOfDay.SATURDAY || this.nameOfDay == NameOfDay.SUNDAY);
  }

  // Check whether this date is a holiday.
  // 0: not a holiday
  // 1: is a holiday
  // 2: the day before a holiday
  // TODO: Move all magic number into constant class.
  private int checkIsHoliday() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this.date);
    HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.UNITED_STATES);
    if (holidayManager.isHoliday(cal, HolidayType.OFFICIAL_HOLIDAY)) {
      return 1;
    } else {
      cal.add(Calendar.DATE, 1);
    }
    return holidayManager.isHoliday(cal, HolidayType.OFFICIAL_HOLIDAY) ? 2 : 0;
  }

  private boolean checkIsWeekLeadingToHoliday() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this.date);
    // find number of days left in current week
    int daysLeftInSameWeek = 7 - cal.get(Calendar.DAY_OF_WEEK);
    // iterate whole week to find a holiday
    HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.UNITED_STATES);
    for (int plusDay = 0; plusDay < daysLeftInSameWeek; plusDay++) {
      if (holidayManager.isHoliday(cal, HolidayType.OFFICIAL_HOLIDAY)) {
        return true;
      }
      cal.add(Calendar.DATE, 1);
    }
    return false;
  }
}
