package com.n26.exercise.statisticscollector.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.n26.exercise.statisticscollector.domain.Transaction.forAmount;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StatisticsCalculatorTest
{

  StatisticsCalculator calculator;

  @Before
  public void setup()
  {
    calculator = new StatisticsCalculator(UnixEpoch.now());
  }

  @Test
  public void noTransaction()
  {
    checkEmptyStatistics();

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
  public void oneTransactionExpired()
  {

    calculator.update(new Transaction(13.5, UnixEpoch.now().add(-61)));

    checkEmptyStatistics();

    calculator.update(new Transaction(13.5, UnixEpoch.now().add(-60)));

    assertStatisticsAre(1,
                        13.5,
                        13.5,
                        13.5,
                        13.5);

  }

  @Test
  public void someTransactionExpired()
  {

    calculator.update(
        asList(
            new Transaction(13.5, UnixEpoch.now().add(-45)),
            new Transaction(48.63, UnixEpoch.now().add(-61)),
            new Transaction(113.25, UnixEpoch.now().add(-63)),
            new Transaction(79.25, UnixEpoch.now().add(-20))
        )
    );

    assertStatisticsAre(2,
                        92.75,
                        46.375,
                        13.5,
                        79.25);

  }

  @Test
  public void isAppliableToStatistics()
  {

    assertThat(calculator.isAppliableToStatistics(new Transaction(13.5, UnixEpoch.now().add(-61))),
               is(false));

    assertThat(calculator.isAppliableToStatistics(new Transaction(13.5, UnixEpoch.now().add(-60))), is(true));

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

  private void checkEmptyStatistics()
  {
    assertStatisticsAre(0,
                        0.0,
                        0.0,
                        0.0,
                        0.0);
  }

}
