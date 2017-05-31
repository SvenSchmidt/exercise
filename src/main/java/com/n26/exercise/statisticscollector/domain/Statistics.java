package com.n26.exercise.statisticscollector.domain;

public class Statistics
{
  final double sum;
  final double avg;
  final double max;
  final double min;
  final int count;

  public Statistics(double sum, double avg, double max, double min, int count)
  {
    this.sum = sum;
    this.avg = avg;
    this.max = max;
    this.min = min;
    this.count = count;
  }

  public double getSum()
  {
    return sum;
  }

  public double getAvg()
  {
    return avg;
  }

  public double getMax()
  {
    return max;
  }

  public double getMin()
  {
    return min;
  }

  public int getCount()
  {
    return count;
  }

  @Override public String toString()
  {
    return "Statistics{" +
        "sum=" + sum +
        ", avg=" + avg +
        ", max=" + max +
        ", min=" + min +
        ", count=" + count +
        '}';
  }
}
