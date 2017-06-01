package com.n26.exercise.statisticscollector.domain;

import java.time.Instant;
import java.util.Date;

public class UnixEpoch
{
  private final long epoch;
  private final Date asDate;

  public UnixEpoch(long unixEpoch)
  {
    this.epoch = unixEpoch;
    this.asDate = Date.from(Instant.ofEpochSecond(unixEpoch));
  }

  public UnixEpoch()
  {
    this(nowAsLong());
  }

  public boolean isBeforeThan(int seconds) {
      return isBeforeThan(now(), seconds);
  }

  public boolean isBeforeThan(UnixEpoch other, int seconds) {
    return add(seconds).before(other);
  }

  public boolean after(UnixEpoch other) {
    return this.epoch>other.epoch;
  }

  public boolean before(UnixEpoch other) {
    return this.epoch<other.epoch;
  }

  public UnixEpoch add(int seconds) {
    return new UnixEpoch(this.epoch+seconds);
  }

  public Date getAsDate()
  {

    return asDate;
  }

  public long getEpoch()
  {
    return epoch;
  }

  public static UnixEpoch now() {
    return new UnixEpoch();
  }

  public static long nowAsLong()
  {
    return System.currentTimeMillis()/1000;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    UnixEpoch unixEpoch = (UnixEpoch) o;

    return epoch == unixEpoch.epoch;
  }

  @Override public int hashCode()
  {
    return (int) (epoch ^ (epoch >>> 32));
  }

  @Override public String toString()
  {
    return "UnixEpoch{" +
        "epoch=" + epoch +
        ", asDate=" + asDate +
        '}';
  }
}
