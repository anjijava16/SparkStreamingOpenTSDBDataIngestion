

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function0;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import org.apache.spark.streaming.kafka09.*;



public class SparkStreamingApp implements Serializable {



    JsonDataParser jsonDataParser = new JsonDataParser();


    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: JavaDStreaming <brokers> <topics><offset reset>\n" +
                    "  <brokers> is a list of one or more Kafka brokers\n" +
                    "  <topics> is a list of one or more kafka topics to consume from\n\n");
            System.exit(1);
        }
// $ /opt/mapr/spark/spark-2.1.0/bin/spark-submit --master yarn --deploy-mode client --class JavaDStreaming maprstreams-spark-opentsdb-1.0-SNAPSHOT.jar 10.10.71.23:9092 /user/mapr/psstream:pstopic earliest testgroup



        // String brokers = args[0];
        String brokers = ConfigParams.brokerList;

        //String topics = args[1];
        String topics = ConfigParams.topicList;
        //String group = args[3];
        String group = ConfigParams.groupName;
        // Create context with a 2 seconds batch interval

        SparkConf sparkConf = new SparkConf().setAppName(ConfigParams.appName);
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", brokers);
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, args[2]);
        kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        List topicss = Arrays.asList(topics.split(","));
        ConsumerStrategy consumerStrategy = ConsumerStrategies.Subscribe(topicss, kafkaParams);


        SparkStreamingApp ssapp = new SparkStreamingApp();
        ssapp.readAndWriteData(jssc,topics,consumerStrategy);

        jssc.start();
        jssc.awaitTermination();

    }



    public  void  readAndWriteData(JavaStreamingContext jssc,String topics,ConsumerStrategy consumerStrategy){

        // Create direct kafka stream with brokers and topics
        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream( jssc, LocationStrategies.PreferConsistent(), consumerStrategy);

        JavaDStream<String> lines = stream.map((Function<ConsumerRecord<String, String>, String>) ConsumerRecord::value);


        lines.print();


        lines.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            @Override
            public void call(JavaRDD<String> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<String>>() {
                    @Override
                    public void call(Iterator<String> tenants) throws Exception {

                        while (tenants != null && tenants.hasNext()) {
                            String line = tenants.next();
                            System.out.println("######## input  messages ##### :  "+line);
                           // JsonDataParser jsonDataParser = new JsonDataParser();
                            jsonDataParser.putJsonDataParser(line);
                           /* OpenTSDBDataingestApp openTSDBput = new OpenTSDBDataingestApp();
                            int statuscode = openTSDBput.putOpenTSDBdata(line);
                           System.out.println("returned status code is "+statuscode);
                            */
                        }

                    }

                });

            }
        });

    }
}
