package model;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs5500.cs5500.model.HourlyTimeSlot;
import cs5500.cs5500.model.InputJson;
import cs5500.cs5500.model.NameOfDay;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InputJsonTest {

  private InputJson inputJson;
  private final String json = "{\"StartingDate\": \"2020-06-07\",\"EndingDate\": \"2020-06-07\",\"OpenTime\":\"6\",\"CloseTime\": \"21\",\"DailyAveNumber\": [800, 1000, 1200, 900, 2500, 4000, 5000],\"DailyAveDuration\": 25,\"LunchHour\": \"12-13\",\"DinnerHour\": \"17-19\",\"SeniorDay\": 2,\"SeniorHour\": \"10-12\"}";


  @Before
  public void setUp() throws Exception {
    final Gson gson = new Gson();
    inputJson = gson.fromJson(json, InputJson.class);
  }

  @Test
  public void testGetter() throws Exception {
    Assert.assertEquals("2020-06-07", inputJson.getStartingDate());
    Assert.assertEquals("2020-06-07", inputJson.getEndingDate());
    Assert.assertEquals(6, inputJson.getOpenTime());
    Assert.assertEquals(21, inputJson.getCloseTime());
    List<Integer> dailyAveNum = new ArrayList<>(
        Arrays.asList(800, 1000, 1200, 900, 2500, 4000, 5000)
    );
    Assert.assertEquals(dailyAveNum, inputJson.getDailyAveNumber());
    Assert.assertEquals(25, inputJson.getDailyAveDuration());
  }

  @Test
  public void testGetDailyAvgCustomersOfDay() {
    NameOfDay nameOfDay = NameOfDay.MONDAY;
    Assert.assertEquals(800, inputJson.getDailyAvgCustomersOfDay(nameOfDay));
    nameOfDay = NameOfDay.TUESDAY;
    Assert.assertEquals(1000, inputJson.getDailyAvgCustomersOfDay(nameOfDay));
    nameOfDay = NameOfDay.WEDNESDAY;
    Assert.assertEquals(1200, inputJson.getDailyAvgCustomersOfDay(nameOfDay));
    nameOfDay = NameOfDay.THURSDAY;
    Assert.assertEquals(900, inputJson.getDailyAvgCustomersOfDay(nameOfDay));
    nameOfDay = NameOfDay.FRIDAY;
    Assert.assertEquals(2500, inputJson.getDailyAvgCustomersOfDay(nameOfDay));
    nameOfDay = NameOfDay.SATURDAY;
    Assert.assertEquals(4000, inputJson.getDailyAvgCustomersOfDay(nameOfDay));
    nameOfDay = NameOfDay.SUNDAY;
    Assert.assertEquals(5000, inputJson.getDailyAvgCustomersOfDay(nameOfDay));
  }

  @Test
  public void testgetAllTimeSlots() {
    List<HourlyTimeSlot> allTimeSlots = new ArrayList<>(
            Arrays.asList(HourlyTimeSlot.SIX_TO_SEVEN, HourlyTimeSlot.SEVEN_TO_EIGHT, HourlyTimeSlot.EIGHT_TO_NINE, HourlyTimeSlot.NINE_TO_TEN, HourlyTimeSlot.TEN_TO_ELEVEN, HourlyTimeSlot.ELEVEN_TO_TWELVE,
                    HourlyTimeSlot.TWELVE_TO_THIRTEEN, HourlyTimeSlot.THIRTEEN_TO_FOURTEEN, HourlyTimeSlot.FOURTEEN_TO_FIFTEEN, HourlyTimeSlot.FIFTEEN_TO_SIXTEEN, HourlyTimeSlot.SIXTEEN_TO_SEVENTEEN,
                    HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN, HourlyTimeSlot.EIGHTEEN_TO_NINETEEN, HourlyTimeSlot.NINETEEN_TO_TWENTY, HourlyTimeSlot.TWENTY_TO_TWENTYONE)
    );
    Assert.assertEquals(allTimeSlots, inputJson.getAllTimeSlots());
  }

  @Test
  public void testGetLunchHour() {
    List<HourlyTimeSlot> lunchHours = new ArrayList<>(
            Arrays.asList(HourlyTimeSlot.TWELVE_TO_THIRTEEN)
    );
    Assert.assertEquals(lunchHours, inputJson.getLunchHour());
  }

  @Test
  public void testGetDinnerHour() {
    List<HourlyTimeSlot> dinnerHour = new ArrayList<>(
            Arrays.asList(HourlyTimeSlot.SEVENTEEN_TO_EIGHTEEN, HourlyTimeSlot.EIGHTEEN_TO_NINETEEN)
    );
    Assert.assertEquals(dinnerHour, inputJson.getDinnerHour());
  }

  @Test
  public void testGetSeniorDay() {
    Assert.assertEquals(NameOfDay.TUESDAY, inputJson.getSeniorDay());
  }

  @Test
  public void testGetSeniorHour() {
    List<HourlyTimeSlot> seniorHours = new ArrayList<>(
            Arrays.asList(HourlyTimeSlot.TEN_TO_ELEVEN, HourlyTimeSlot.ELEVEN_TO_TWELVE)
    );
    Assert.assertEquals(seniorHours, inputJson.getSeniorHour());
  }

  @Test
  public void testGetNumOfTotalHours() {
    Assert.assertEquals(15, inputJson.getNumOfTotalHours());
  }

  @Test
  public void testGetNumOfPeakHours() {
    Assert.assertEquals(3, inputJson.getNumOfPeakHours());
  }
}
