package com.n26.exercise.statisticscollector.spring;

import com.n26.exercise.statisticscollector.domain.AsyncTransactionUpdaterStatisticsSamples;
import com.n26.exercise.statisticscollector.domain.AutoSlidingStatisticsSamples;
import com.n26.exercise.statisticscollector.domain.FixedSizeSlidingStatisticsSamples;
import com.n26.exercise.statisticscollector.domain.SlidingStatisticsSamples;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Configuration
public class SpringConfiguration
{
  @Value("${statistics-collector.sliding.period}")
  long slidingInterval;
  @Value("${statistics-collector.sliding.timeUnit}")
  TimeUnit slidingPeriod;

  @Value("${statistics-collector.transactionUpdater.period}")
  long transactionUpdaterInterval;
  @Value("${statistics-collector.transactionUpdater.timeUnit}")
  TimeUnit transactionUpdaterPeriod;
  @Value("${statistics-collector.transactionUpdater.bufferSize}")
  int transactionUpdaterSize;

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
