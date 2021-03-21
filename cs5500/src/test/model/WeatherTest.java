package model;

import cs5500.cs5500.model.Weather;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WeatherTest {

  private Weather weatherTrue;
  private Weather weatherFalse;

  @Before
  public void setUp() throws Exception {
    weatherTrue = new Weather(Boolean.TRUE);
    weatherFalse = new Weather(Boolean.FALSE);
  }

  @Test
  public void testGetGoodWeather() {
    Assert.assertTrue(weatherTrue.getGoodWeather());
    Assert.assertFalse(weatherFalse.getGoodWeather());
  }

  @Test
  public void testSetGoodWeather() {
    weatherTrue.setGoodWeather(Boolean.FALSE);
    Assert.assertFalse(weatherTrue.getGoodWeather());
    weatherFalse.setGoodWeather(Boolean.TRUE);
    Assert.assertTrue(weatherFalse.getGoodWeather());
  }
}