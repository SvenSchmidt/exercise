package com.n26.exercise.statisticscollector.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StatisticsCalculatorTest
{

  StatisticsCalculator calculator;

  @Before
  public void setup()
  {
    calculator = new StatisticsCalculator();
  }

  @Test
  public void noTransaction()
  {
    assertStatisticsAre(0,
                        0.0,
                        0.0,
                        0.0,
                        0.0);

  }

  @Test
  public void oneTransaction()
  {

    updateWithTransaction(15.3);

    assertStatisticsAre(1,
                        15.3,
                        15.3,
                        15.3,
                        15.3);

  }

  @Test
  public void withManyTransaction()
  {

    updateWithTransaction(
        15.6,
                  22.8,
                  45.9,
                  765.4,
                  234.53);

    assertStatisticsAre(5,
                        1084.23,
                        216.846,
                        15.6,
                        765.4);

  }

  private void updateWithTransaction(double... amounts)
  {
    for (double amount : amounts)
    {
      calculator.update(new Transaction(amount, null));
    }
  }

  private void assertStatisticsAre(int count,
                                   double sum,
                                   double avg,
                                   double min,
                                   double max)
  {
    Statistics statistics = calculator.getStatistics();
    assertThat(statistics.getCount(), is(equalTo(count)));
    assertThat(statistics.getSum(), is(equalTo(sum)));

    assertThat(statistics.getAvg(), is(equalTo(avg)));

    assertThat(statistics.getMin(), is(equalTo(min)));
    assertThat(statistics.getMax(), is(equalTo(max)));
  }

}
