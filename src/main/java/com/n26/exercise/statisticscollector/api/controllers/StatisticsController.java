package com.n26.exercise.statisticscollector.api.controllers;

import com.n26.exercise.statisticscollector.api.dtos.Statistics;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController
{

  @RequestMapping("/statistics")
  public Statistics getStatistics() {

    return new Statistics(12.3,12.3,11.0,9.0,12);
  }

}
