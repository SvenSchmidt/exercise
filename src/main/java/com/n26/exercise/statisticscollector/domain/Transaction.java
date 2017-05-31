package com.n26.exercise.statisticscollector.domain;

import java.time.Instant;
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

  public static Transaction forAmount(double amount) {
    return new Transaction(amount,new Date());
  }

  public static Transaction of(double amount,long unixEpoch) {
    Date transactionDate = Date.from(Instant.ofEpochSecond(unixEpoch));
    if(System.currentTimeMillis()-transactionDate.getTime()>60000) {
      throw new ExpiredTransactionException(unixEpoch,transactionDate);
    }
    return new Transaction(amount, transactionDate);
  }


  @Override public String toString()
  {
    return "Transaction{" +
        "amount=" + amount +
        ", timestamp=" + timestamp +
        '}';
  }
}
