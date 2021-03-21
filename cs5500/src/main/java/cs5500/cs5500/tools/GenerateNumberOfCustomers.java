package cs5500.cs5500.tools;

import cs5500.cs5500.model.Constants;
import cs5500.cs5500.model.Day;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.TimeSlot;
import cs5500.cs5500.model.Weather;
import java.util.List;


public final class GenerateNumberOfCustomers {

  private static final int HOLIDAY = 1;
  private static final int THE_DAY_BEFORE_HOLIDAY = 2;

  /*
  Generate number of customer for a given time slot
   */
  public static int generateNumberOfCustomers(Day day, TimeSlot timeSlot, Weather weather, int numCustomersDaily, int numTotalHours, int numPeakHours, List<HourlyTimeSlot> lunchHours, List<HourlyTimeSlot> dinnerHours) {
//    // get daily average number of customers
//    NameOfDay nameOfDay = day.getNameOfDay();
//    int numCustomersDaily = inputJson.getDailyAvgCustomersOfDay(nameOfDay);

    // update daily average number of customers based on holiday effects
    int numCustomersHourly;
    int numCustomersPeekHour;

    // case 1: holiday.
    if (day.getHoliday() == HOLIDAY) {
      numCustomersDaily = (int) Math.round(numCustomersDaily * Constants.HOLIDAY_CUSTOMERS_CHANGE);
      // for holiday there is no peak hours for holiday -> every time slot has the same number of customers
      numCustomersHourly = (int) Math.round(numCustomersDaily / (double) numTotalHours);
      return numCustomersHourly;
    }
    // case 2: the day before holiday
    else if (day.getHoliday() == THE_DAY_BEFORE_HOLIDAY) {
      numCustomersDaily = (int) Math
          .round(numCustomersDaily * Constants.DAY_BEFORE_HOLIDAY_CUSTOMER_CHANGE);
    }
    // case 3: the week leading to holiday
    else if (day.getIsInWeekLeadingToHoliday()) {
      numCustomersDaily = (int) Math
          .round(numCustomersDaily * Constants.WEEK_LEADING_TO_HOLIDAY_CHANGE);
    }

    // calculate hourly number of customers for the given time slot
    // weekends
    if (!day.getWeekday()) {
      // good weather will increase the number of customers
      if (weather.getGoodWeather()) {
        numCustomersDaily = (int) Math
            .round(numCustomersDaily * Constants.WEEKENDS_NICE_WEATHER_CHANGE);
      }
      numCustomersHourly = (int) Math.round(numCustomersDaily / (double) numTotalHours);
      return numCustomersHourly;
    }
    // weekdays
    else {
      numCustomersHourly = (int) Math.round(numCustomersDaily / ( (numTotalHours - numPeakHours)
          + numPeakHours * Constants.PEAK_HOUR_CHANGE));
      numCustomersPeekHour = (int) Math.round(numCustomersHourly * Constants.PEAK_HOUR_CHANGE);
      // peak hour
      if (timeSlot.isPeakTime(lunchHours, dinnerHours)) {
        return numCustomersPeekHour;
      }
      // not peak hour
      else {
        return numCustomersHourly;
      }
    }
  }
}
