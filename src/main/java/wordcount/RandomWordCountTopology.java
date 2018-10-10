package wordcount;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class RandomWordCountTopology {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("generator",
                new RandomSentenceSpout(), 1);

        builder.setBolt("splitter",
                new SplitSentenceBolt(), 1)
                .shuffleGrouping("generator");

        builder.setBolt("counter",
                new WordCountBolt(), 2)
                .fieldsGrouping("splitter", new Fields("word"));


        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);

        StormSubmitter.submitTopologyWithProgressBar(
                "word-count-soln", conf,
                builder.createTopology());

    }
}