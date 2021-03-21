package cs5500.cs5500.model;

import java.util.List;

/**
 * For every customer, we need to generate a random time slot. However, some time slots are more
 * likely than others.
 */

public class TimeSlot {

  private Day day;
  private HourlyTimeSlot hour;

  public TimeSlot(Day day) {
    this.day = day;
    this.hour = null;
  }

  public TimeSlot(Day day, HourlyTimeSlot hour) {
    this.day = day;
    this.hour = hour;
  }

  public Day getDay() {
    return this.day;
  }

  public HourlyTimeSlot getHour() {
    return this.hour;
  }

  public void setDay(Day day) {
    this.day = day;
  }

  public void setHour(HourlyTimeSlot hour) {
    this.hour = hour;
  }

  /**
   * Determines whether the HourlyTimeSlot hour is a peak time hour of 12-1pm for the lunch rush or
   * 5-7pm for the dinner rush during the weekday.
   *
   * @return true if HourlyTimeSlot are those specific lunch or dinner rush times
   */
  public Boolean isPeakTime(List<HourlyTimeSlot> lunchHours, List<HourlyTimeSlot> dinnerHours) {
    if (this.day.getWeekday()) {
      if (lunchHours.contains(this.hour) || dinnerHours.contains(this.hour)) {
        return true;
      }
    }
    return false;
  }
}
