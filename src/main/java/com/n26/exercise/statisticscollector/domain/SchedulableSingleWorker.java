package com.n26.exercise.statisticscollector.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SchedulableSingleWorker
{
  private final static Logger logger = LoggerFactory.getLogger(SchedulableSingleWorker.class);

  private final ScheduledExecutorService scheduledExecutorService;
  private final long interval;
  private final TimeUnit period;
  private final AtomicBoolean running = new AtomicBoolean(false);

  private Runnable worker;

  public SchedulableSingleWorker(ScheduledExecutorService scheduledExecutorService,
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
    running.set(true);
    this.worker = getWorker();
    logger.info("Starting worker {} with delay {} period {} and time unit {}",
                workerName(),
                interval,
                interval,
                period);
    scheduledExecutorService.scheduleAtFixedRate(worker,
                                                 interval,
                                                 interval,
                                                 period);
  }

  @PreDestroy
  public void destroy()
  {
    running.set(false);
    logger.info("Stopping worker {}.", workerName());
    scheduledExecutorService.shutdownNow();
  }

  private String workerName()
  {
    if (worker != null)
    {
      return worker.getClass().getName();
    }
    return "NONE";
  }

  protected abstract Runnable getWorker();

  public boolean isRunning()
  {
    return running.get();
  }
}
