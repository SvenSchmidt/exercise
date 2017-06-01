package com.n26.exercise.statisticscollector.api.controllers;

import com.n26.exercise.statisticscollector.api.dtos.Statistics;
import com.n26.exercise.statisticscollector.domain.SlidingStatisticsSamples;
import com.n26.exercise.statisticscollector.domain.Transaction;
import com.n26.exercise.statisticscollector.spring.StatisticsCollectorApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.n26.exercise.statisticscollector.domain.SchedulableSingleWorkerBaseTest.sleepFor;
import static com.n26.exercise.statisticscollector.domain.StatisticsChecker.assertStatisticsAre;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StatisticsCollectorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsControllerIT
{

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  @Qualifier("slidingStatisticsSamples")
  private SlidingStatisticsSamples slidingStatisticsSamples;

  @Test
  public void initialEmptyStatistics()
  {
    Statistics statistics = restTemplate.getForObject("/statistics", Statistics.class);
    assertThat(statistics, is(notNullValue()));
    assertStatisticsAre(statistics,
                        0,
                        0.0,
                        0.0,
                        0.0,
                        0.0);
  }

  @Test
  public void statisticsAfterSomeTransactions()
  {

    slidingStatisticsSamples.addTransaction(Transaction.forAmount(123.12));
    slidingStatisticsSamples.addTransaction(Transaction.forAmount(23.00));
    slidingStatisticsSamples.addTransaction(Transaction.forAmount(523.12));

    sleepFor(150);

    Statistics statistics = restTemplate.getForObject("/statistics", Statistics.class);
    assertThat(statistics, is(notNullValue()));
    assertStatisticsAre(statistics,
                        3,
                        669.24,
                        223.08,
                        23.00,
                        523.12);
  }

  @Test
  public void resetStatistics()
  {
    restTemplate.delete("/statistics");
  }
}
