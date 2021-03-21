package cs5500.cs5500.model;

public class Constants {

  public static final double HOLIDAY_CUSTOMERS_CHANGE = 0.2; // holidays are about 20% of the normal traffic,
  public static final double DAY_BEFORE_HOLIDAY_CUSTOMER_CHANGE = 1.4; // the day before a holiday is 40% higher than normal traffic
  public static final double WEEK_LEADING_TO_HOLIDAY_CHANGE = 1.15; // the week leading up to a holiday is about 15% higher than normal traffic
  public static final double WEEKENDS_NICE_WEATHER_CHANGE = 1.4; // if the weather is really nice, store has about 40% more people

  public static final double PEAK_HOUR_CHANGE = 1.3; // ASSUMED PEAK HOUR HAS 30% MORE CUSTOMERS
  public static final double TOTAL_HOURS = 15; // store opens at 6am and closes at 9pm -> 15 hours
  public static final double WEEKDAY_PEAK_HOURS = 3; // peak hours: LUNCH 12-1pm, DINNER 5-7pm
  public static final double WEEKDAY_NORMAL_HOURS = TOTAL_HOURS - WEEKDAY_PEAK_HOURS;

  public static final int DURATION_LOWER_BOUND = 6; // The duration distribution ranges from about 6 minutes on the super-fast end, to about 75 minutes on the long end.
  public static final int DURATION_UPPER_BOUND = 75;
  public static final int DURATION_GENERAL = 25; // generally shoppers spend about 25 minutes in the store
  public static final int DURATION_LUNCHTIME = 10;
  public static final int DURATION_DINNERTIME = 20;
  public static final int DURATION_SENIOR_LOWER_BOUND = 45; // Seniors tend to take about 45-60 minutes shopping
  public static final int DURATION_SENIOR_UPPER_BOUND = 60;
  public static final int DURATION_WEEKEND = 60;
  public static final int DURATION_WEEKENDS_SHORT = 20; // ASSUMED THE SAME TIME AS DINNER TIME DURATION (SHORT VISIT)

  public static final double PROBABILITY_OF_GENERAL_DURATION = 0.2; // the probability of a customer spending general duration (25min) (ASSUMED)

  public static final String DATE_PATTERN = "yyyy-MM-dd";

  public static final int NUM_CUSTOMERS_PER_STAFF_MEMBER = 8;
}
