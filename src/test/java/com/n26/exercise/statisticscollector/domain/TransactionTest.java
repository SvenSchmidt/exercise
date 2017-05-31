package com.n26.exercise.statisticscollector.domain;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class TransactionTest
{

  @Test
  public void transactionOfNotExpired() {
    long unixEpoch = (System.currentTimeMillis() / 1000) - 10;
    Transaction transaction = Transaction.of(12.56, unixEpoch);
    assertThat(transaction.getAmount(),is(equalTo(12.56)));
    Date timestamp = transaction.getTimestamp();
    assertThat(timestamp,is(notNullValue()));
    assertThat((System.currentTimeMillis()-timestamp.getTime())>=10000,is(true));

  }

  @Test(expected = ExpiredTransactionException.class)
  public void transactionOfExpired() {
    long unixEpoch = (System.currentTimeMillis() / 1000) - 61;
    Transaction transaction = Transaction.of(12.56, unixEpoch);
  }

}
