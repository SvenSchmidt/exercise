package com.n26.exercise.statisticscollector.domain;

public interface SlidingStatisticsSamples
{
  public void addTransaction(Transaction transaction);

  public Statistics getStatistics();

  public void slide();
}
