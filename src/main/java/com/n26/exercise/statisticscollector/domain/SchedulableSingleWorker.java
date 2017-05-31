package com.n26.exercise.statisticscollector.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class SchedulableSingleThread
{
  private final static Logger logger = LoggerFactory.getLogger(SchedulableSingleThread.class);

  private final ScheduledExecutorService scheduledExecutorService;
  private final long interval;
  private final TimeUnit period;
  private Runnable worker;

  public SchedulableSingleThread(ScheduledExecutorService scheduledExecutorService,
                                 long interval,
                                 TimeUnit period)
  {
    this.scheduledExecutorService = scheduledExecutorService;
    this.interval = interval;
    this.period = period;
  }

  @PostConstruct
  public void init()
  {
    this.worker = getWorker();
    logger.info("Starting worker {}.",worker.getClass());
    scheduledExecutorService.scheduleAtFixedRate(worker,
                                                 interval,
                                                 interval,
                                                 period);
  }

  protected abstract Runnable getWorker();

  @PreDestroy
  public void destroy()
  {
    logger.info("Stopping worker {}.",worker.getClass());
    scheduledExecutorService.shutdownNow();
  }
}
