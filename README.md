# StormTwitterStreamAnalysis

A  Storm Topology to generate a list of popular words used in twitter. Data is ingested from either a storm spout or a kafka spout and processed downstream using Storm Bolts.

## Requirements
- IDE 
- Apache Maven 3.x
- JVM 6 or 7

## General Info
The source folder is organized into 2 packages i.e. Kafka and Storm. Storm package has two topologies. The KafkaTwitterTopology using a Kafka spout and TwitterWordCountTopology using a Twitter Sample spout. Below is the list of classes:
* com/stdatalabs/Kafka
     * KafkaTwitterProducer.java --   A Kafka Producer that publishes twitter data to a kafka broker
* com/stdatalabs/Storm
    * TwitterWordCountTopology.java -- A topology which uses the TwitterSampleSpout to get the list of top words in twitter 
    * KafkaTwitterTopology.java -- A topology which uses the KafkaSpout to get the list of top words in twitter
    * TwitterSampleSpout.java -- A spout which uses the twittet4j library to receive twitter data
    * StringWordSplitterBolt.java -- A bolt which receives tweets and emits its words which are over a certain length
    * IgnoreWordsBolt.java -- A bolt which filters out a predefined set of words
    * WordCounterBolt.java -- A bolt which calculates and prints list of popular words over a time interval
    * JsonWordSplitterBolt.java -- A bolt which receives tweets and emits its words which are over a certain length

## Description
* ##### A Storm topology that receives tweets on certain keywords from TwitterSampleSpout and finds the list of popular words over a time interval.
  Discussed in blog -- 
     [Realtime stream processing using Apache Storm - Part 1](http://stdatalabs.blogspot.in/2016/09/realtime-stream-processing-using-apache.html)

* ##### A Storm topology that receives tweets on certain keywords from KafkaSpout and finds the list of popular words over a time interval.
    Discussed in blog -- 
     [Realtime stream processing using Apache Storm and Kafka - Part 2](http://stdatalabs.blogspot.in/2016/10/real-time-stream-processing-using.html)

### More articles on hadoop technology stack at [stdatalabs](http://stdatalabs.blogspot.com)
