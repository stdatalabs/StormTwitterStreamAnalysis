package com.stdatalabs.Storm;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

import java.util.Arrays;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.StringScheme;

/**
 * A Storm topology to consume messages from kafka topic and count
 * and display the list of top words used in tweets on a keyword
 * 
 * More discussion at stdatalabs.blogspot.com
 * 
 * @author Sachin Thirumala
 */

public class KafkaTwitterTopology {

	public static void main(String[] args) {

		String zkIp = "localhost";

		String nimbusHost = "localhost";

		String zookeeperHost = zkIp + ":2181";

		ZkHosts zkHosts = new ZkHosts(zookeeperHost);

		SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, "tweets", "", "storm");

		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme(){
				@Override
				public Fields getOutputFields() {
					return new Fields("tweet");
				}
			});

		KafkaSpout kafkaSpout = new KafkaSpout(kafkaConfig);

		TopologyBuilder builder = new TopologyBuilder();
		
		builder.setSpout("twitter-spout", kafkaSpout, 8);

		builder.setBolt("WordSplitterBolt", new JsonWordSplitterBolt(5)).shuffleGrouping("twitter-spout");
		builder.setBolt("IgnoreWordsBolt", new IgnoreWordsBolt()).shuffleGrouping("WordSplitterBolt");
		builder.setBolt("WordCounterBolt", new WordCounterBolt(5, 5 * 60, 50)).shuffleGrouping("IgnoreWordsBolt");

		Config config = new Config();
		config.setDebug(false);

		config.setMaxTaskParallelism(5);
		//config.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 2);
		config.put(Config.NIMBUS_HOST, nimbusHost);
		config.put(Config.NIMBUS_THRIFT_PORT, 6627);
		config.put(Config.STORM_ZOOKEEPER_PORT, 2181);
		config.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(zkIp));

		try {
			LocalCluster cluster = new LocalCluster();
	      cluster.submitTopology("TwitterWordCountStorm", config, builder.createTopology());
		} catch (Exception e) {
			throw new IllegalStateException("Couldn't initialize the topology", e);
		}
		
	      
	}

}
