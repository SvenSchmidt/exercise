package com.n26.exercise.statisticscollector;

import com.n26.exercise.statisticscollector.domain.AsyncTransactionUpdaterStatisticsSamples;
import com.n26.exercise.statisticscollector.domain.AutoSlidingStatisticsSamples;
import com.n26.exercise.statisticscollector.domain.FixedSizeSlidingStatisticsSamples;
import com.n26.exercise.statisticscollector.domain.SlidingStatisticsSamples;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class StatisticsCollectorApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(StatisticsCollectorApplication.class, args);
  }

  long slidingInterval = 1;
  TimeUnit slidingPeriod = TimeUnit.SECONDS;

  long transactionUpdaterInterval = 50;
  TimeUnit transactionUpdaterPeriod = TimeUnit.MILLISECONDS;
  int transactionUpdaterSize = 50;

  @Bean(name = "realSlidingStatisticsSample")
  public FixedSizeSlidingStatisticsSamples fixedSizeSlidingStatisticsSamples()
  {
    return new FixedSizeSlidingStatisticsSamples(60);
  }

  @Bean(name = "transactionUpdaterScheduledExecutorService")
  public ScheduledExecutorService transactionUpdaterScheduledExecutorService()
  {
    return Executors.newScheduledThreadPool(1, new ThreadFactory()
    {
      @Override public Thread newThread(Runnable r)
      {
        return new Thread(r, "TransactionUpdater");
      }
    });
  }

  @Bean(name = "transactionUpdaterStatisticsSamples")
  @Autowired
  public AsyncTransactionUpdaterStatisticsSamples asyncTransactionUpdaterStatisticsSamples(
      @Qualifier("realSlidingStatisticsSample") SlidingStatisticsSamples delegate,
      @Qualifier("transactionUpdaterScheduledExecutorService") ScheduledExecutorService executorService)
  {
    return new AsyncTransactionUpdaterStatisticsSamples(delegate,
                                                        executorService,
                                                        transactionUpdaterInterval,
                                                        transactionUpdaterPeriod,
                                                        transactionUpdaterSize);
  }

  @Bean(name = "autoSlidingScheduledExecutorService")
  public ScheduledExecutorService autoSlidingScheduledExecutorService()
  {
    return Executors.newScheduledThreadPool(1, new ThreadFactory()
    {
      @Override public Thread newThread(Runnable r)
      {
        return new Thread(r, "SamplesSlider");
      }
    });
  }

  @Bean(name = "slidingStatisticsSamples")
  @Autowired
  public AutoSlidingStatisticsSamples autoSlidingStatisticsSamples(
      @Qualifier("transactionUpdaterStatisticsSamples") SlidingStatisticsSamples delegate,
      @Qualifier("autoSlidingScheduledExecutorService") ScheduledExecutorService executorService)
  {
    return new AutoSlidingStatisticsSamples(delegate,
                                            executorService,
                                            slidingInterval,
                                            slidingPeriod);
  }

}
