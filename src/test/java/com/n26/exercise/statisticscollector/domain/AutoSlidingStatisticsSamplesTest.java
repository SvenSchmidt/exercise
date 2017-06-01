package com.n26.exercise.statisticscollector.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AutoSlidingStatisticsSamplesTest extends SchedulableSingleWorkerBaseTest
{

  @Mock
  private SlidingStatisticsSamples delegate;


  AutoSlidingStatisticsSamples autoSlidingStatisticsSamples;

  Transaction transaction = new Transaction(1d, null);
  Collection<Transaction> transactions = Collections.singleton(transaction);
  private com.n26.exercise.statisticscollector.domain.Statistics statistics = new Statistics(12,6,6,6,2);


  @Before
  public void setup() {
    autoSlidingStatisticsSamples = new AutoSlidingStatisticsSamples(delegate,
                                                                    scheduledExecutorService,
                                                                    3,
                                                                    TimeUnit.SECONDS);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void slide() {
    autoSlidingStatisticsSamples.slide();
  }

  @Test
  public void addTransaction() {
    autoSlidingStatisticsSamples.addTransaction(transaction);

    verify(delegate).addTransaction(transaction);
  }

  @Test
  public void addTransactions() {
    autoSlidingStatisticsSamples.addTransactions(transactions);

    verify(delegate).addTransactions(transactions);
  }

  @Test
  public void resetStatistics()
  {
    autoSlidingStatisticsSamples.resetStatistics();

    verify(delegate).resetStatistics();
  }


  @Test
  public void getStatistics() {

    when(delegate.getStatistics()).thenReturn(statistics);

    Statistics statisticsToTest = autoSlidingStatisticsSamples.getStatistics();

    assertThat(statisticsToTest,is(equalTo(statistics)));

    verify(delegate).getStatistics();
  }


  @Test
  public void happyPath() {
    autoSlidingStatisticsSamples = new AutoSlidingStatisticsSamples(delegate,
                                                                    Executors.newScheduledThreadPool(1),
                                                                    10,
                                                                    TimeUnit.MILLISECONDS);

    autoSlidingStatisticsSamples.init();

    sleepFor(60l);

    verify(delegate,atLeast(5)).slide();

    autoSlidingStatisticsSamples.destroy();
  }


  @Override protected SchedulableSingleWorker getInstance()
  {
    return autoSlidingStatisticsSamples;
  }

  @Override protected Class<? extends Runnable> getRunnableClass()
  {
    return AutoSlidingStatisticsSamples.Slider.class;
  }

  @Override protected long getInitialDelay()
  {
    return 3;
  }

  @Override protected long getPeriod()
  {
    return 3;
  }

  @Override protected TimeUnit getTimeUnit()
  {
    return TimeUnit.SECONDS;
  }
}
