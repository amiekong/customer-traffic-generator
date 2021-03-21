package cs5500.cs5500.model;

public enum DailyAverageNumCustomers {
  MONDAY(800),
  TUESDAY(1000),
  WEDNESDAY(1200),
  THURSDAY(900),
  FRIDAY(2500),
  SATURDAY(4000),
  SUNDAY(5000);

  private final int value;

  private DailyAverageNumCustomers(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
