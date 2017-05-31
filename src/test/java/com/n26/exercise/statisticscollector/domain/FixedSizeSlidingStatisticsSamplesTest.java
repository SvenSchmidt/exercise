package com.n26.exercise.statisticscollector.domain;

import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;

public class FixedSizeSlidingStatisticsSamplesTest
{

  FixedSizeSlidingStatisticsSamples fixedSizeSlidingStatisticsSamples;

  @Before
  public void setup()
  {
    fixedSizeSlidingStatisticsSamples = new FixedSizeSlidingStatisticsSamples(5);
  }

  @Test
  public void onlySomeEventsAtTheBeginning()
  {
    fixedSizeSlidingStatisticsSamples.addTransaction(Transaction.forAmount(3.5));
    fixedSizeSlidingStatisticsSamples.addTransaction(Transaction.forAmount(8.22));
    fixedSizeSlidingStatisticsSamples.addTransactions(
        asList(
            Transaction.forAmount(18.22),
            Transaction.forAmount(86.72),
            Transaction.forAmount(180.12),
            Transaction.forAmount(48),
            Transaction.forAmount(57)
        )
    );

    for (int i = 0; i < 5; i++)
    {
      StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                            7,
                                            401.78,
                                            57.39714285714285,
                                            3.5,
                                            180.12);

      fixedSizeSlidingStatisticsSamples.slide();
    }

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          0,
                                          0.0,
                                          0.0,
                                          0.0,
                                          0.0);
  }

  @Test
  public void eventsArrivingInDifferentMoments()
  {
    fixedSizeSlidingStatisticsSamples.addTransaction(Transaction.forAmount(3.5));
    fixedSizeSlidingStatisticsSamples.addTransaction(Transaction.forAmount(8.22));

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          2,
                                          11.72,
                                          5.86,
                                          3.5,
                                          8.22);

    fixedSizeSlidingStatisticsSamples.slide();

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          2,
                                          11.72,
                                          5.86,
                                          3.5,
                                          8.22);

    fixedSizeSlidingStatisticsSamples.addTransactions(
        asList(
            Transaction.forAmount(47.562),
            Transaction.forAmount(86.72)
        )
    );

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          4,
                                          146.002,
                                          36.5005,
                                          3.5,
                                          86.72);

    fixedSizeSlidingStatisticsSamples.slide();
    fixedSizeSlidingStatisticsSamples.slide();

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          4,
                                          146.002,
                                          36.5005,
                                          3.5,
                                          86.72);

    fixedSizeSlidingStatisticsSamples.slide();
    fixedSizeSlidingStatisticsSamples.slide();

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          2,
                                          134.28199999999998,
                                          67.14099999999999,
                                          47.562,
                                          86.72);

    fixedSizeSlidingStatisticsSamples.slide();

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          0,
                                          0.0,
                                          0.0,
                                          0.0,
                                          0.0);
  }

}
