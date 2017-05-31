package com.n26.exercise.statisticscollector.domain;

import java.util.LinkedList;

public class SlidingStatisticsCollector implements SlidingStatisticsSamples
{
  LinkedList<StatisticsCalculator> statistics = new LinkedList<>();

  public SlidingStatisticsCollector(int numberOfSamples)
  {
    fillStatistics(numberOfSamples);
  }

  public void slide() {
    statistics.pop();
    statistics.add(new StatisticsCalculator());
  }

  @Override public void addTransaction(Transaction transaction)
  {
    for(StatisticsCalculator statistics:statistics) {
      statistics.update(transaction);
    }
  }

  @Override public Statistics getStatistics()
  {
    return statistics.peek().getStatistics();
  }

  private void fillStatistics(int numberOfSamples)
  {
    for(int i=0;i<numberOfSamples;i++) {
      statistics.add(new StatisticsCalculator());
    }
  }

}
