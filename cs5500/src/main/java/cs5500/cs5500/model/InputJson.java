package cs5500.cs5500.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InputJson {

  @SerializedName("StartingDate")
  private String startingDate;

  @SerializedName("EndingDate")
  private String endingDate;

  @SerializedName("OpenTime")
  private int openTime;

  @SerializedName("CloseTime")
  private int closeTime;

  @SerializedName("DailyAveNumber")
  private List<Integer> dailyAveNumber;

  @SerializedName("DailyAveDuration")
  private int dailyAveDuration;

  @SerializedName("LunchHour")
  private String lunchHour;

  @SerializedName("DinnerHour")
  private String dinnerHour;

  @SerializedName("SeniorDay")
  private int seniorDay;

  @SerializedName("SeniorHour")
  private String seniorHour;

  private static final HourlyTimeSlot[] TIME_SLOTS = {
      HourlyTimeSlot.ZERO_TO_ONE,
      HourlyTimeSlot.ONE_TO_TWO,
      HourlyTimeSlot.TWO_TO_THREE,
      HourlyTimeSlot.THREE_TO_FOUR,
      HourlyTimeSlot.FOUR_TO_FIVE,
      HourlyTimeSlot.FIVE_TO_SIX,
      HourlyTimeSlot.SIX_TO_SEVEN,
      HourlyTimeSlot.SEVEN_TO_EIGHT,
      HourlyTimeSlot.EIGHT_TO_NINE,
      HourlyTimeSlot.NINE_TO_TEN,
      HourlyTimeSlot.TEN_TO_ELEVEN,
      HourlyTimeSlot.ELEVEN_TO_TWELVE,
      HourlyTimeSlot.TWELVE_TO_THIRTEEN,
      HourlyTimeSlot.THIRTEEN_TO_FOURTEEN,
      HourlyTimeSlot.FOURTEEN_TO_FIFTEEN,
      HourlyTimeSlot.FIFTEEN_TO_SIXTEEN,
      HourlyTimeSlot.SIXTEEN_TO_SEVENTEEN,
      HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN,
      HourlyTimeSlot.EIGHTEEN_TO_NINETEEN,
      HourlyTimeSlot.NINETEEN_TO_TWENTY,
      HourlyTimeSlot.TWENTY_TO_TWENTYONE,
      HourlyTimeSlot.TWENTYONE_TO_TWENTYTWO,
      HourlyTimeSlot.TWENTYTWO_TO_TWENTYTHREE,
      HourlyTimeSlot.TWENTYTHREE_TO_ZERO
  };

  public String getStartingDate() {
    return startingDate;
  }

  public String getEndingDate() {
    return endingDate;
  }

  public int getOpenTime() {
    return openTime;
  }

  public int getCloseTime() {
    return closeTime;
  }

  public int getNumOfTotalHours() {
    return (closeTime - openTime);
  }

  public List<HourlyTimeSlot> getAllTimeSlots() {
    List<HourlyTimeSlot> allTimeSlots = new ArrayList<>();
    while (openTime < closeTime) {
      allTimeSlots.add(TIME_SLOTS[openTime]);
      openTime++;
    }
    return allTimeSlots;
  }

  public List<Integer> getDailyAveNumber() {
    return dailyAveNumber;
  }

  public int getDailyAvgCustomersOfDay(NameOfDay nameOfDay) {
    int dailyAvgCustomers = 0;
    switch (nameOfDay) {
      case MONDAY:
        dailyAvgCustomers = dailyAveNumber.get(0);
        break;
      case TUESDAY:
        dailyAvgCustomers = dailyAveNumber.get(1);
        break;
      case WEDNESDAY:
        dailyAvgCustomers = dailyAveNumber.get(2);
        break;
      case THURSDAY:
        dailyAvgCustomers = dailyAveNumber.get(3);
        break;
      case FRIDAY:
        dailyAvgCustomers = dailyAveNumber.get(4);
        break;
      case SATURDAY:
        dailyAvgCustomers = dailyAveNumber.get(5);
        break;
      case SUNDAY:
        dailyAvgCustomers = dailyAveNumber.get(6);
        break;
    }
    return dailyAvgCustomers;
  }

  public int getDailyAveDuration() {
    return dailyAveDuration;
  }

  public List<HourlyTimeSlot> getLunchHour() {
    String[] strs = lunchHour.split("-");
    int lowerBound = Integer.parseInt(strs[0]);
    int upperBound = Integer.parseInt(strs[strs.length - 1]);
    List<HourlyTimeSlot> lunchHours = new ArrayList<>();
    while (lowerBound < upperBound) {
      lunchHours.add(TIME_SLOTS[lowerBound]);
      lowerBound++;
    }
    return lunchHours;
  }

  public List<HourlyTimeSlot> getDinnerHour() {
    String[] strs = dinnerHour.split("-");
    int lowerBound = Integer.parseInt(strs[0]);
    int upperBound = Integer.parseInt(strs[strs.length - 1]);
    List<HourlyTimeSlot> dinnerHours = new ArrayList<>();
    while (lowerBound < upperBound) {
      dinnerHours.add(TIME_SLOTS[lowerBound]);
      lowerBound++;
    }
    return dinnerHours;
  }

  public int getNumOfPeakHours() {
    List<HourlyTimeSlot> lunchHours = getLunchHour();
    List<HourlyTimeSlot> dinnerHours = getDinnerHour();
    return lunchHours.size() + dinnerHours.size();
  }

  public NameOfDay getSeniorDay() {
    NameOfDay nameOfDay = NameOfDay.MONDAY;
    switch (seniorDay) {
      case 1:
        nameOfDay = NameOfDay.MONDAY;
        break;
      case 2:
        nameOfDay = NameOfDay.TUESDAY;
        break;
      case 3:
        nameOfDay = NameOfDay.WEDNESDAY;
        break;
      case 4:
        nameOfDay = NameOfDay.THURSDAY;
        break;
      case 5:
        nameOfDay = NameOfDay.FRIDAY;
        break;
      case 6:
        nameOfDay = NameOfDay.SATURDAY;
        break;
      case 7:
        nameOfDay = NameOfDay.SUNDAY;
        break;
    }
    return nameOfDay;
  }

  public List<HourlyTimeSlot> getSeniorHour() {
    String[] strs = seniorHour.split("-");
    int lowerBound = Integer.parseInt(strs[0]);
    int upperBound = Integer.parseInt(strs[strs.length - 1]);
    List<HourlyTimeSlot> seniorHours = new ArrayList<>();
    while (lowerBound < upperBound) {
      seniorHours.add(TIME_SLOTS[lowerBound]);
      lowerBound++;
    }
    return seniorHours;
  }
}