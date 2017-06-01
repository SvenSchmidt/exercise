****Solution
This repository contains a solution to the problem listed below.
The idea behind the solution is to have 1 item holding statistics for each second.
So we will have a sliding window of 60 StatisticsCalculator that will be shifted by 
an asynchronous thread.

StatisticsCalculator istances has a threshold and could only accept transaction not older
than 60 seconds comparing to the threshold.
Synchronized operation on this class are the update of the statistics and the read operation
that creates an immutable Statistics object.

Above this object you have the collection of samples FixedSizeSlidingStatisticsSamples (60 StatisticsCalculator).
Also in this case read and write operation are separated. You have an atomic reference to 
the object holding the statistics for now and you have a list on which are performed two different
operation (both synchronized):

- window sliding
- update of the transaction

Then you have:

- AutoSlidingStatisticsSamples: it's a decorator for the FixedSizeSlidingStatisticsSamples that
schedule a thread for sliding the window of samples each second
- AsyncTransactionUpdaterStatisticsSamples: it's a decorator for the FixedSizeSlidingStatisticsSamples.
Valid transaction are queued in a queue and a thread is scheduled each time to pick up a transaction
and update the statistics. In this way, when a transaction arrives it is parked in a queue (linked list).

TODO:
- improve AsyncTransactionUpdaterStatisticsSamples: the thread shouls awake periodically and consume
a batch of transactions, not just one
- adding a concurrent integration test
- I integrated everything in travis for CI and I discoverd UnixEpoch test were dipendent on the
Timezone, so I had to ignore it
- Controllers use directly domain classes, maybe a usescase object would be great in both case
RetrieveStatisticsUsecase -> FixedSizeSlidingStatisticsSamples
UpdateStatisticsUsecase -> FixedSizeSlidingStatisticsSamples
- I have some integration tests, they are not always launched



****Code Challenge:

We would like to have a restful API for our statistics. 
The main use case for our API is to calculate realtime statistic from the last 60 seconds. 
There will be two APIs, one of them is called every time a transaction is made. 
It is also the sole input of this rest API. The other one returns the statistic based of the transactions of the last 60 seconds.

****Specs
POST /transactions

Every Time a new transaction happened, this endpoint will be called.
Body: {
   "amount": 12.3,
   "timestamp": 1478192204000
}

Where:
1) amount  - transaction amount
2) timestamp  - transaction time in epoch in millis in UTC time zone (this is not
current timestamp)
Returns: Empty body with either 201 or 204.
1) 201 - in case of success
2) 204 - if transaction is older than 60 seconds
Where:
1) amount  is a double specifying the amount
2) time  is a long specifying unix time format in milliseconds GET /statistics
This is the main endpoint of this task, this endpoint have to execute in constant time and memory (O(1)). It returns the statistic based on the transactions which happened in the last 60 seconds.
 
 
Returns: {
"sum": 1000, 
"avg": 100, 
"max": 200, 
"min": 50, 
"count": 10
}

Where:
1) sum  is a double specifying the total sum of transaction value in the last 60 seconds
2) avg  is a double specifying the average amount of transaction value in the last 60 seconds
3) max  is a double specifying single highest transaction value in the last 60 seconds
4) min  is a double specifying single lowest transaction value in the last 60 seconds
5) count  is a long specifying the total number of transactions happened in the last 60 seconds

****Requirements
For the rest api, the biggest and maybe hardest requirement is to make the  GET /statistics  execute in constant time and space. The best solution would be O(1). It is very recommended to tackle the O(1) requirement as the last thing to do as it is not the only thing which will be rated in the code challenge.
Other requirements, which are obvious, but also listed here explicitly:
1) The API have to be threadsafe with concurrent requests
2) The API have to function properly, with proper result
3) The project should be buildable, and tests should also complete successfully.
e.g. If maven is used, then  mvn clean install  should complete successfully.
4) The API should be able to deal with time discrepancy, which means, at any point of time, 
we could receive a transaction which have a timestamp of the past
5) Make sure to send the case in memory solution without database (including
in-memory database)
6) Endpoints have to execute in constant time and memory (O(1)) 
