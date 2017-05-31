package com.n26.exercise.statisticscollector.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public abstract class SchedulableSingleWorkerBaseTest
{

  @Mock
  protected ScheduledExecutorService scheduledExecutorService;

  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;
  @Captor
  private ArgumentCaptor<Long> initialDelayCaptor;
  @Captor
  private ArgumentCaptor<Long> periodCaptor;
  @Captor
  private ArgumentCaptor<TimeUnit> timeUnitCaptor;

  @Test
  public void init() {
    getInstance().init();

    verify(scheduledExecutorService).scheduleAtFixedRate(runnableCaptor.capture(),
                                                         initialDelayCaptor.capture(),
                                                         periodCaptor.capture(),
                                                         timeUnitCaptor.capture());

    Runnable runnable = runnableCaptor.getValue();
    assertThat(runnable, is(notNullValue()));
    assertThat(getRunnableClass().isAssignableFrom(runnable.getClass()),is(true));

    assertThat(initialDelayCaptor.getValue(),is(equalTo(getInitialDelay())));
    assertThat(periodCaptor.getValue(),is(equalTo(getPeriod())));
    assertThat(timeUnitCaptor.getValue(),is(getTimeUnit()));

  }

  @Test
  public void destroy() {
    getInstance().destroy();

    verify(scheduledExecutorService).shutdownNow();

  }

  protected abstract SchedulableSingleWorker getInstance();

  protected abstract Class<? extends Runnable> getRunnableClass();

  protected abstract long getInitialDelay();

  protected abstract long getPeriod();

  protected abstract TimeUnit getTimeUnit();

  public static void sleepFor(long howMuch)
  {
    try
    {
      Thread.sleep(howMuch);
    } catch (Exception ex) {

    }
  }


}
