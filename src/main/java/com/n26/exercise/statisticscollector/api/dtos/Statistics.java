package com.n26.exercise.statisticscollector.api.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Statistics
{
  final double sum;
  final double avg;
  final double max;
  final double min;
  final int count;

  @JsonCreator
  public Statistics(@JsonProperty("sum") double sum,
                    @JsonProperty("avg") double avg,
                    @JsonProperty("max") double max,
                    @JsonProperty("min") double min,
                    @JsonProperty("count") int count)
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
