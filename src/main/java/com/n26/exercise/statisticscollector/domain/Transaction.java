package com.n26.exercise.statisticscollector.domain;

import java.util.Date;

public class Transaction
{
  private final double amount;
  private final Date timestamp;

  public Transaction(double amount, Date timestamp)
  {
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public double getAmount()
  {
    return amount;
  }

  public Date getTimestamp()
  {
    return timestamp;
  }
}
