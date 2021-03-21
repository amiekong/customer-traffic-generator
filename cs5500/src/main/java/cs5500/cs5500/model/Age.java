package cs5500.cs5500.model;
import java.util.List;
import java.util.Random;

/**
 * We want to generate the age of a customer, so there will be a function that generates the
 * probability of a customer being senior or not, based on the day, time. Default isSenior is
 * randomly generated.
 */
public class Age {

  private Day day;
  private TimeSlot timeSlot;
  private boolean isSenior;

  public Age() {
    this.isSenior = false;
  }

  public Age(Day day, TimeSlot timeSlot, NameOfDay seniorDay, List<HourlyTimeSlot> seniorHours) {
    this.day = day;
    this.timeSlot = timeSlot;
    this.isSenior = generateAgeOfCustomer(day, timeSlot, seniorDay, seniorHours);
  }

  /**
   * gets the day of an age object.
   *
   * @return the day in an age object.
   */
  public Day getDay() {
    return this.day;
  }

  /**
   * gets the time slot in an age object.
   *
   * @return the time slot in an age object.
   */
  public TimeSlot getTimeSlot() {
    return this.timeSlot;
  }

  /**
   * return whether the customer is a senior member or not.
   *
   * @return True if the customer is a senior member.
   */
  public boolean getSenior() {
    return this.isSenior;
  }

  /**
   * Set the senior to a default value needed.
   *
   * @param isSenior default value to set for the senior value of a customer.
   */
  public void setSenior(boolean isSenior) {
    this.isSenior = isSenior;
  }

  /**
   * Determines if the given day and time slot is senior discount hours of 10-12pm on Tuesdays.
   *
   * @param day      day of the week
   * @param timeSlot hourly time slot
   * @return true if it is senior discount hour, false otherwise
   */
  public static boolean isSeniorHour(Day day, TimeSlot timeSlot, NameOfDay seniorDay, List<HourlyTimeSlot> seniorHours) {
    if (timeSlot.getDay().getNameOfDay().equals(seniorDay)) {
      return seniorHours.contains(timeSlot.getHour());
    }
    return false;
  }

  /**
   * Generates the age of the customers by determining whether the customer is senior or not. It
   * uses probability based on if it is senior discount hour. It uses random generator to increase
   * probability of senior customers during senior hours of 10-12pm on Tuesday.
   *
   * @param day      day of the week
   * @param timeSlot hourly time slot
   * @return true if customer is senior, false otherwise
   */
  public boolean generateAgeOfCustomer(Day day, TimeSlot timeSlot, NameOfDay seniorDay, List<HourlyTimeSlot> seniorHours) {
    Random rand = new Random();
    int probability = 40;

    if (isSeniorHour(day, timeSlot, seniorDay, seniorHours)) {
      probability = 85;
    }

    if (rand.nextInt(100) < probability) {
      this.isSenior = true;
      return true;
    } else {
      return false;
    }
  }
}
