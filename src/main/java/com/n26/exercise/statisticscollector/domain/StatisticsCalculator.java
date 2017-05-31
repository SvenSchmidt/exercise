package com.n26.exercise.statisticscollector.domain;

public class StatisticsCalculator
{
  double sum =0.0;
  double max =0.0;
  double min =0.0;
  int count =0;

  public synchronized Statistics getStatistics() {
    return new Statistics(sum,
                          calculateAvg(),
                          max,
                          min,
                          count);
  }

  public synchronized void update(Transaction transaction)
  {
    double amount = transaction.getAmount();
    updateSumAndAvg(amount);
    updateMinValue(amount);
    updateMaxValue(amount);
  }

  private double calculateAvg()
  {
    if(count==0)
    {
      return 0.0;
    }
    return sum/count;
  }

  private void updateMaxValue(double amount)
  {
    if(amount>max) {
      max = amount;
    }
  }

  private void updateMinValue(double amount)
  {
    if(min==0 || amount<min) {
      this.min = amount;
    }
  }

  private void updateSumAndAvg(double amount)
  {
    this.count++;
    this.sum+=amount;
  }

  @Override public String toString()
  {
    return "StatisticsCalculator{" +
        "sum=" + sum +
        ", max=" + max +
        ", min=" + min +
        ", count=" + count +
        '}';
  }
}
