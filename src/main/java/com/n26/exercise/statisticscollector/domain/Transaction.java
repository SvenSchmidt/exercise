package com.n26.exercise.statisticscollector.domain;

public class Transaction
{
  private final double amount;
  private final UnixEpoch timestamp;

  public Transaction(double amount, long timestamp)
  {
    this(amount,new UnixEpoch(timestamp));
  }

  public Transaction(double amount, UnixEpoch unixEpoch)
  {
    this.amount = amount;
    this.timestamp = unixEpoch;
  }

  public double getAmount()
  {
    return amount;
  }

  public UnixEpoch getTimestamp()
  {
    return timestamp;
  }

  public boolean isValid() {
    return !this.timestamp.isBeforeThan(60);
  }

  public boolean isValid(UnixEpoch unixEpoch) {
    return !this.timestamp.isBeforeThan(unixEpoch, 60);
  }

  public static Transaction forAmount(double amount) {
    return new Transaction(amount,UnixEpoch.now());
  }

  public static Transaction of(double amount,long unixEpochAsLong) {
    Transaction transaction = new Transaction(amount, unixEpochAsLong);
    if(!transaction.isValid()) {
      throw new ExpiredTransactionException(transaction.getTimestamp());
    }

    return transaction;
  }


  @Override public String toString()
  {
    return "Transaction{" +
        "amount=" + amount +
        ", timestamp=" + timestamp +
        '}';
  }
}
