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

    fixedSizeSlidingStatisticsSamples
        = new FixedSizeSlidingStatisticsSamples(5);
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

  @Test
  public void transactionsNotAppliableToAllTheSamples()
  {
    UnixEpoch now = UnixEpoch.now();

    fixedSizeSlidingStatisticsSamples.addTransaction(
        new Transaction(
            3.5,
            now.add(-40)
        )
    );
    fixedSizeSlidingStatisticsSamples.addTransaction(
        new Transaction(
            123.15,
            now.add(-58)
        )
    );
    fixedSizeSlidingStatisticsSamples.addTransaction(
        new Transaction(
            88.5,
            now.add(-57)
        )
    );
    fixedSizeSlidingStatisticsSamples.addTransaction(
        new Transaction(
            23.5,
            now.add(-60)
        )
    );

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          3,
                                          215.15,
                                          71.716666666666667,
                                          3.5,
                                          123.15);


    fixedSizeSlidingStatisticsSamples.slide();

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          3,
                                          215.15,
                                          71.716666666666667,
                                          3.5,
                                          123.15);

    fixedSizeSlidingStatisticsSamples.slide();

    StatisticsChecker.assertStatisticsAre(fixedSizeSlidingStatisticsSamples.getStatistics(),
                                          2,
                                          92,
                                          46,
                                          3.5,
                                          88.5);
  }

}
