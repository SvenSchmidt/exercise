package com.n26.exercise.statisticscollector.api.controllers;

import com.n26.exercise.statisticscollector.api.dtos.Event;
import com.n26.exercise.statisticscollector.api.dtos.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionsController
{

  Logger logger = LoggerFactory.getLogger(TransactionsController.class);


  @RequestMapping(path = "/transactions",method = RequestMethod.POST)
  public void transactions(@RequestBody Event event) {
    logger.info("Received event {}",event);
  }

}
