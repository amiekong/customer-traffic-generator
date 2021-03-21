package cs5500.cs5500.model;

import java.util.List;

import cs5500.cs5500.tools.GenerateDuration;

/**
 * This is the final object that will help us generate the CSV - each customer will be a single row.
 * Customer inputs will ve date, weather. To generate : day, age, timeslot, duration
 */
public class Customer {

  private Weather weather;
  private Day day;
  private Age age;
  private TimeSlot timeSlot;
  private Duration duration;
  private Boolean isSenior;
  private Integer printTimeSlot;
  private Boolean printIsSenior;
  private Integer printVisitDuration;

  /**
   * Generates a customer given a day, weather on that day and a particular time slot.
   *
   * @param day      day for which the CSV is needed.
   * @param weather  weather on the day, either good or bad.
   * @param timeSlot timeslot for whcih the CSV is needed.
   */
  public Customer(Day day, Weather weather, TimeSlot timeSlot, NameOfDay seniorDay, List<HourlyTimeSlot> seniorHours, int avgDuration) {
    this.day = day;
    this.weather = weather;
    this.timeSlot = timeSlot;
    this.age = new Age(day, timeSlot, seniorDay, seniorHours);
    this.duration = GenerateDuration.generateDuration(age, day, weather, timeSlot, avgDuration);
    this.isSenior = age.getSenior();
  }

  public Customer(Integer timeSlot, Boolean isSenior, Integer visitDuration) {
    this.printTimeSlot = timeSlot;
    this.printIsSenior = isSenior;
    this.printVisitDuration = visitDuration;
  }

  public Integer getPrintVisitDuration() {
    return printVisitDuration;
  }

  public Integer getPrintTimeSlot() {
    return printTimeSlot;
  }

  public Boolean getPrintIsSenior() {
    return printIsSenior;
  }

  /**
   * Set the weather on the day.
   *
   * @param weather good or bad weather for the customer to be generated.
   */
  public void setWeather(Weather weather) {
    this.weather = weather;
  }

  /**
   * Set the day for a customer.
   *
   * @param day day to be set for a customer.
   */
  public void setDay(Day day) {
    this.day = day;
  }

  /**
   * Set the age of a customer.
   *
   * @param age age to be set for the customer.
   */
  public void setAge(Age age) {
    this.age = age;
  }

  /**
   * Set the time slot when the customer visits the store.
   *
   * @param timeSlot timeslot that needs to be set for the customer.
   */
  public void setTimeSlot(TimeSlot timeSlot) {
    this.timeSlot = timeSlot;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public Weather getWeather() {
    return weather;
  }

  public Day getDay() {
    return day;
  }

  public Age getAge() {
    return age;
  }

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }

  public Duration getDuration() {
    return duration;
  }

  public Boolean getSenior() {
    return isSenior;
  }
}
