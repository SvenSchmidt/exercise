package com.n26.exercise.statisticscollector.domain;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class FixedSizeSlidingStatisticsSamples implements SlidingStatisticsSamples
{
  LinkedList<StatisticsCalculator> statistics = new LinkedList<>();
  AtomicReference<StatisticsCalculator> firstItem = new AtomicReference<>();

  public FixedSizeSlidingStatisticsSamples(int numberOfSamples)
  {
    fillStatistics(numberOfSamples);
  }

  @Override public synchronized void addTransaction(Transaction transaction)
  {
    for (StatisticsCalculator statistics : statistics)
    {
      statistics.update(transaction);
    }
  }

  @Override public void addTransactions(Collection<Transaction> transactions)
  {
    for (Transaction transaction : transactions)
    {
      addTransaction(transaction);
    }
  }

  @Override public synchronized void slide()
  {
    statistics.pop();
    statistics.add(new StatisticsCalculator());
    refreshFirstItem();
  }

  @Override public Statistics getStatistics()
  {
    return firstItem.get().getStatistics();
  }

  private void refreshFirstItem()
  {
    firstItem.set(statistics.peek());
  }

  private void fillStatistics(int numberOfSamples)
  {
    for (int i = 0; i < numberOfSamples; i++)
    {
      statistics.add(new StatisticsCalculator());
    }
    refreshFirstItem();
  }

}
