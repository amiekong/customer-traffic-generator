package cs5500.cs5500.tools;

import cs5500.cs5500.model.Age;
import cs5500.cs5500.model.Constants;
import cs5500.cs5500.model.Day;

import cs5500.cs5500.model.Duration;
import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.TimeSlot;
import cs5500.cs5500.model.Weather;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import java.util.Random;
import java.util.stream.IntStream;

public final class GenerateDuration {

  /*
  Generate duration spent by a customer
  */
  public static Duration generateDuration(Age age, Day day, Weather weather, TimeSlot timeSlot, int avgDuration) {
    Duration duration = new Duration(day, timeSlot, age);
    int time;
    HourlyTimeSlot hour = timeSlot.getHour();

    // Seniors shopping pattern
    if (age.getSenior()) {
      time = new Random().nextInt(
          Constants.DURATION_SENIOR_UPPER_BOUND - Constants.DURATION_SENIOR_LOWER_BOUND + 1)
          + Constants.DURATION_SENIOR_LOWER_BOUND;
      duration.setDuration(time);
      return duration;
    }

    // weekends special case
    if (!day.getWeekday()) {
      if (!weather.getGoodWeather()) {
        time = Constants.DURATION_WEEKEND;
      } else {
        // 40% more people stopping by to get grab and go food (so short visits)
        // probability for short visits = 0.4 / (0.4+1)
        int[] durationWeekends = new int[]{Constants.DURATION_WEEKEND,
            Constants.DURATION_WEEKENDS_SHORT};
        double[] probabilitiesWeekends = new double[]{1 / 1.4, 0.4 / 1.4};

        EnumeratedIntegerDistribution distribution =
            new EnumeratedIntegerDistribution(durationWeekends, probabilitiesWeekends);

        time = distribution.sample();
      }
      duration.setDuration(time);
      return duration;
    }

    // weekdays special case
    else {
      // weekday lunchtime
      if (hour == HourlyTimeSlot.TWELVE_TO_THIRTEEN) {
        double probOfLunchVisit =
            (Constants.PEAK_HOUR_CHANGE - 1) / Constants.PEAK_HOUR_CHANGE * 100;
        int num = new Random().nextInt(100 + 1);
        // normal visit
        if (num > probOfLunchVisit) {
          time = generateNormalDuration(avgDuration);
        }
        // lunch visit
        else {
          time = Constants.DURATION_LUNCHTIME;
        }
        duration.setDuration(time);
        return duration;
      }

      // weekday dinnertime (same strategy as used in lunchtime)
      if (hour == HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN || hour == HourlyTimeSlot.SIX_TO_SEVEN) {
        double probOfDinnerVisit =
            (Constants.PEAK_HOUR_CHANGE - 1) / Constants.PEAK_HOUR_CHANGE * 100;
        int num = new Random().nextInt(100 + 1);
        // normal visit
        if (num > probOfDinnerVisit) {
          time = generateNormalDuration(avgDuration);
        }
        // dinner visit
        else {
          time = Constants.DURATION_DINNERTIME;
        }
        duration.setDuration(time);
        return duration;
      }
    }

    // if not the special case, generate a random normal duration number
    time = generateNormalDuration(avgDuration);
    duration.setDuration(time);
    return duration;
  }

  /*
  Generate a random normal duration
   */
  private static int generateNormalDuration(int avgDuration) {
    // possible duration are from LOWER BOUND (6) to UPPER BOUND (75)
    int[] durationToGenerate = IntStream
        .rangeClosed(Constants.DURATION_LOWER_BOUND, Constants.DURATION_UPPER_BOUND).toArray();
    int len = durationToGenerate.length;

    // generate discrete probability
    double[] discretePossibilities = new double[len];

    int midIndex = avgDuration - Constants.DURATION_LOWER_BOUND - 1;
    discretePossibilities[midIndex] = Constants.PROBABILITY_OF_GENERAL_DURATION;
    discretePossibilities[midIndex - 1] = (1 - Constants.PROBABILITY_OF_GENERAL_DURATION) / 4;
    discretePossibilities[midIndex + 1] = (1 - Constants.PROBABILITY_OF_GENERAL_DURATION) / 4;
    // decrease the possibility by half towards two ends
    for (int i = midIndex - 2; i >= 1; i--) {
      discretePossibilities[i] = discretePossibilities[i + 1] / 2;
    }
    for (int i = midIndex + 2; i < len - 1; i++) {
      discretePossibilities[i] = discretePossibilities[i - 1] / 2;
    }
    // in order to add up to 1, make the first two elements have the same prob value, and the last element have the same prob value
    discretePossibilities[0] = discretePossibilities[1];
    discretePossibilities[len - 1] = discretePossibilities[len - 2];

    EnumeratedIntegerDistribution distribution =
        new EnumeratedIntegerDistribution(durationToGenerate, discretePossibilities);

    return distribution.sample();
  }
}
