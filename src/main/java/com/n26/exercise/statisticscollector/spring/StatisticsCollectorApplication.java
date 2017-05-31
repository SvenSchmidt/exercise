package com.n26.exercise.statisticscollector.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.n26.exercise.statisticscollector.spring","com.n26.exercise.statisticscollector.api.controllers"})
public class StatisticsCollectorApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(StatisticsCollectorApplication.class, args);
  }

}
