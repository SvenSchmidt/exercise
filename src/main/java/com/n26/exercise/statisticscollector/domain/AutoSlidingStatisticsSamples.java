package com.n26.exercise.statisticscollector.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoSlidingStatisticsSamples extends SchedulableSingleWorker implements SlidingStatisticsSamples
{
  private final static Logger logger = LoggerFactory.getLogger(AutoSlidingStatisticsSamples.class);

  private final SlidingStatisticsSamples delegate;

  public AutoSlidingStatisticsSamples(SlidingStatisticsSamples delegate,
                                      ScheduledExecutorService scheduledExecutorService,
                                      long interval,
                                      TimeUnit period)
  {
    super(scheduledExecutorService,
          interval,
          period);

    this.delegate = delegate;
  }

  @Override public void addTransaction(Transaction transaction)
  {
    delegate.addTransaction(transaction);
  }

  @Override public void addTransactions(Collection<Transaction> transactions)
  {
    delegate.addTransactions(transactions);
  }

  @Override public Statistics getStatistics()
  {
    return delegate.getStatistics();
  }

  @Override public void slide()
  {
    throw new UnsupportedOperationException("I am supposed to slide samples on my own :)");
  }

  @Override protected Runnable getWorker()
  {
    return new Slider();
  }

  class Slider implements Runnable
  {
    @Override public void run()
    {
      try
      {
        delegate.slide();
      }
      catch (Exception ex)
      {
        logger.error("Error sliding samples.", ex);
      }
    }
  }

}
