package com.n26.exercise.statisticscollector.domain;

import java.util.Date;

public class ExpiredTransactionException extends RuntimeException
{

  public ExpiredTransactionException(long unixEpoch, Date transactionDate)
  {
    super("Transaction timestamp ["+unixEpoch+","+transactionDate+"] is expired.");
  }
}
