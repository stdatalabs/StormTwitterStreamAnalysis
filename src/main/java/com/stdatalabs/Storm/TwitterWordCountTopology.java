package com.stdatalabs.Storm;

import java.util.*;

import com.stdatalabs.Storm.IgnoreWordsBolt;
import com.stdatalabs.Storm.TwitterSampleSpout;
import com.stdatalabs.Storm.WordCounterBolt;
import com.stdatalabs.Storm.JsonWordSplitterBolt;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class TwitterWordCountTopology {
	public static void main(String[] args) throws Exception {
		String consumerKey = args[0];
		String consumerSecret = args[1];

		String accessToken = args[2];
		String accessTokenSecret = args[3];

		String[] arguments = args.clone();
		String[] keyWords = Arrays.copyOfRange(arguments, 4, arguments.length);

		Config config = new Config();
		config.setDebug(false);

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitter-spout",
				new TwitterSampleSpout(consumerKey, consumerSecret, accessToken, accessTokenSecret, keyWords));

		builder.setBolt("WordSplitterBolt", new StringWordSplitterBolt(5)).shuffleGrouping("twitter-spout");
		builder.setBolt("IgnoreWordsBolt", new IgnoreWordsBolt()).shuffleGrouping("WordSplitterBolt");
		builder.setBolt("WordCounterBolt", new WordCounterBolt(5, 5 * 60, 50)).shuffleGrouping("IgnoreWordsBolt");

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("TwitterWordCountStorm", config, builder.createTopology());
		// Thread.sleep(10000);
		// cluster.shutdown();
	}

}
