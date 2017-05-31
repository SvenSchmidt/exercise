package com.n26.exercise.statisticscollector.api.controllers;

import com.n26.exercise.statisticscollector.api.dtos.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController
{
  Logger logger = LoggerFactory.getLogger(TransactionsController.class);

  @RequestMapping(path = "/statistics",method = RequestMethod.GET)
  public Statistics getStatistics() {

    Statistics statistics = new Statistics(12.3, 12.3, 11.0, 9.0, 12);
    logger.info("Returning statistics {}",statistics);
    return statistics;
  }

}
