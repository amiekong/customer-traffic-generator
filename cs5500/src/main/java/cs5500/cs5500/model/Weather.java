package cs5500.cs5500.model;

/**
 * Weather is going to come as an input from the customer who will tell us whether the weather is
 * good or bad.
 */

public class Weather {

  private Boolean isGoodWeather;

  public Weather(Boolean isGoodWeather) {
    this.isGoodWeather = isGoodWeather;
  }

  public Boolean getGoodWeather() {
    return isGoodWeather;
  }

  public void setGoodWeather(Boolean goodWeather) {
    isGoodWeather = goodWeather;
  }
}
