package com.n26.exercise.statisticscollector.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransactionTest
{

  @Test
  public void transactionOfNotExpired() {
    long unixEpoch = (System.currentTimeMillis() / 1000) - 10;
    Transaction transaction = Transaction.of(12.56, unixEpoch);
    assertThat(transaction.getAmount(),is(equalTo(12.56)));
    long transactionTime = transaction.getTimestamp().getEpoch();
    assertThat((System.currentTimeMillis()-transactionTime)>=10000,is(true));
  }

  @Test
  public void isValid() {
    Transaction transaction = new Transaction(12.56, UnixEpoch.nowAsLong() - 10);

    assertThat(transaction.isValid(),is(true));

    transaction = new Transaction(12.56, UnixEpoch.nowAsLong() - 60);
    assertThat(transaction.isValid(),is(true));

    transaction = new Transaction(12.56, UnixEpoch.nowAsLong() - 61);
    assertThat(transaction.isValid(),is(false));
  }

  @Test
  public void isValidOtherTimestamp() {
    Transaction transaction = new Transaction(12.56, UnixEpoch.nowAsLong() - 10);

    UnixEpoch other = UnixEpoch.now().add(40);
    assertThat(transaction.isValid(other),is(true));

    other = UnixEpoch.now().add(50);
    assertThat(transaction.isValid(other),is(true));

    other = UnixEpoch.now().add(51);
    assertThat(transaction.isValid(other),is(false));
  }

  @Test(expected = ExpiredTransactionException.class)
  public void transactionOfExpired() {
    Transaction.of(12.56, UnixEpoch.now().add(-61).getEpoch());
  }

}
