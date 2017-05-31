package com.n26.exercise.statisticscollector.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AutoSlidingStatisticsSamplesTest
{

  @Mock
  private SlidingStatisticsSamples delegate;
  @Mock
  private ScheduledExecutorService scheduledExecutorService;

  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;
  @Captor
  private ArgumentCaptor<Long> initialDelayCaptor;
  @Captor
  private ArgumentCaptor<Long> periodCaptor;
  @Captor
  private ArgumentCaptor<TimeUnit> timeUnitCaptor;

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
  public void getStatistics() {

    when(delegate.getStatistics()).thenReturn(statistics);

    Statistics statisticsToTest = autoSlidingStatisticsSamples.getStatistics();

    assertThat(statisticsToTest,is(equalTo(statistics)));

    verify(delegate).getStatistics();
  }

  @Test
  public void init() {
    autoSlidingStatisticsSamples.init();

    verify(scheduledExecutorService).scheduleAtFixedRate(runnableCaptor.capture(),
                                                         initialDelayCaptor.capture(),
                                                         periodCaptor.capture(),
                                                         timeUnitCaptor.capture());

    Runnable slider = runnableCaptor.getValue();
    assertThat(slider, is(notNullValue()));
    assertThat(slider instanceof AutoSlidingStatisticsSamples.Slider,is(true));

    assertThat(initialDelayCaptor.getValue(),is(equalTo(3l)));
    assertThat(periodCaptor.getValue(),is(equalTo(3l)));
    assertThat(timeUnitCaptor.getValue(),is(equalTo(TimeUnit.SECONDS)));

  }

  @Test
  public void destroy() {
    autoSlidingStatisticsSamples.destroy();

    verify(scheduledExecutorService).shutdownNow();

  }

  @Test
  public void happyPath() {
    autoSlidingStatisticsSamples = new AutoSlidingStatisticsSamples(delegate,
                                                                    Executors.newScheduledThreadPool(1),
                                                                    10,
                                                                    TimeUnit.MILLISECONDS);

    autoSlidingStatisticsSamples.init();

    try
    {
      Thread.sleep(60l);
    } catch (Exception ex) {

    }

    verify(delegate,atLeast(5)).slide();

    autoSlidingStatisticsSamples.destroy();
  }



}
