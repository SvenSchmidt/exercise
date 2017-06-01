package com.n26.exercise.statisticscollector.domain;

import java.util.Date;

public class ExpiredTransactionException extends RuntimeException
{

  public ExpiredTransactionException(UnixEpoch unixEpoch)
  {
    super("Transaction timestamp ["+unixEpoch+"] is expired.");
  }
}
