package cs5500.cs5500.model;

/**
 * The amount of time a customer spends in the store depends on the age, time of day, day and
 * returns a duration.
 */
public class Duration {

  private Day day;
  private TimeSlot timeSlot;
  private Age age;
  private Integer duration;

  public Duration(Day day, TimeSlot timeSlot, Age age) {
    this.day = day;
    this.timeSlot = timeSlot;
    this.age = age;
  }

  public Day getDay() {
    return day;
  }

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }

  public Age getAge() {
    return age;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public Integer getDuration() {
    return duration;
  }

}
