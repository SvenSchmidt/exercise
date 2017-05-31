package com.n26.exercise.statisticscollector.api.controllers;

import com.n26.exercise.statisticscollector.api.dtos.Event;
import com.n26.exercise.statisticscollector.domain.ExpiredTransactionException;
import com.n26.exercise.statisticscollector.domain.SlidingStatisticsSamples;
import com.n26.exercise.statisticscollector.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionsController
{

  Logger logger = LoggerFactory.getLogger(TransactionsController.class);

  private final SlidingStatisticsSamples slidingStatisticsSamples;

  @Autowired
  public TransactionsController(@Qualifier("slidingStatisticsSamples") SlidingStatisticsSamples slidingStatisticsSamples)
  {
    this.slidingStatisticsSamples = slidingStatisticsSamples;
  }

  @RequestMapping(path = "/transactions", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public void transactions(@RequestBody Event event)
  {

    logger.info("Received event {}", event);

    slidingStatisticsSamples.addTransaction(
        Transaction.of(
            event.getAmount(),
            event.getTimestamp()
        )
    );
  }

  @ExceptionHandler(value = ExpiredTransactionException.class)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void expiredTransaction(ExpiredTransactionException exception)
  {
    logger.error(exception.getMessage());
  }
}
