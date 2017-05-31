package com.n26.exercise.statisticscollector.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.n26.exercise.statisticscollector.domain.Transaction.forAmount;
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

  @Test
  public void oneTransactionWithCollection()
  {

    updateWithTransactionWithCollection(15.3);

    assertStatisticsAre(1,
                        15.3,
                        15.3,
                        15.3,
                        15.3);

  }

  @Test
  public void withManyTransactionWithCollection()
  {

    updateWithTransactionWithCollection(
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
      calculator.update(forAmount(amount));
    }
  }

  private void updateWithTransactionWithCollection(double... amounts)
  {
    List<Transaction> transactionList = new LinkedList<>();
    for (double amount : amounts)
    {
      transactionList.add(forAmount(amount));
    }
    calculator.update(transactionList);
  }

  private void assertStatisticsAre(int count,
                                   double sum,
                                   double avg,
                                   double min,
                                   double max)
  {
    Statistics statistics = calculator.getStatistics();
    StatisticsChecker.assertStatisticsAre(statistics,
                                          count,
                                          sum,
                                          avg,
                                          min,
                                          max);
  }

}
