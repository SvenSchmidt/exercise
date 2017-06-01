package com.n26.exercise.statisticscollector.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class StatisticsCalculator
{
  private final Logger logger = LoggerFactory.getLogger(StatisticsCalculator.class);

  double sum =0.0;
  double max =0.0;
  double min =0.0;
  int count =0;

  private final UnixEpoch unixEpoch;

  public StatisticsCalculator(UnixEpoch unixEpoch)
  {
    this.unixEpoch = unixEpoch;
  }

  public synchronized Statistics getStatistics() {
    return new Statistics(sum,
                          calculateAvg(),
                          max,
                          min,
                          count);
  }

  public boolean isAppliableToStatistics(Transaction transaction) {
    return transaction.isValid(this.unixEpoch);
  }

  public synchronized void update(Collection<Transaction> transactions)
  {
    for(Transaction transaction:transactions) {
      update(transaction);
    }
  }

  public synchronized void update(Transaction transaction)
  {
    if(isAppliableToStatistics(transaction))
    {
      double amount = transaction.getAmount();
      transaction.getTimestamp();
      updateSumAndAvg(amount);
      updateMinValue(amount);
      updateMaxValue(amount);
    } else {
      logger.debug("Transaction {} not appliable {}.",transaction,unixEpoch);
    }
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

  public synchronized void clear()
  {
    double sum =0.0;
    double max =0.0;
    double min =0.0;
    int count =0;
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

  public UnixEpoch getUnixEpoch()
  {
    return unixEpoch;
  }
}
