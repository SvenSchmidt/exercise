package com.n26.exercise.statisticscollector.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class FixedSizeSlidingStatisticsSamples implements SlidingStatisticsSamples
{
  private final static Logger logger = LoggerFactory.getLogger(FixedSizeSlidingStatisticsSamples.class);

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
      if(statistics.isAppliableToStatistics(transaction))
      {
        statistics.update(transaction);
      } else {
        break;
      }
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

    StatisticsCalculator lastItem = statistics.peekLast();
    statistics.add(new StatisticsCalculator(lastItem.getUnixEpoch().add(1)));

    refreshFirstItem();
  }

  @Override public synchronized void resetStatistics()
  {
    logger.info("Reset statistics.");
    for(StatisticsCalculator statistics:statistics) {
      statistics.clear();
    }
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
    UnixEpoch unixEpoch = UnixEpoch.now();
    for (int i = 0; i < numberOfSamples; i++)
    {
      UnixEpoch threshold = unixEpoch.add(1);
      statistics.add(new StatisticsCalculator(threshold));
    }
    refreshFirstItem();
  }

}
