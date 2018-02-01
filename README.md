# SparkStreamingOpenTSDBDataIngestion

Follow below steps .
A).Download  Above project and open it using IntelliJ.
B).Compile and export it as Jar.
C).Copy above exported jar to Cluster nodes.
For example:
```
bkumar-1041:Downloads basapuramkumar$ scp /Users/basapuramkumar/Documents/Learning-Docs/SparkStreamingOpenTSDB60/target/SparkStreaming-OpenTSDB-6.0-1.0-SNAPSHOT.jar mapr@10.10.71.23:/home/mapr/ps
```
D).Producer launch:
Start sample Kafka Producer to dump JSON data 
From Terminal-1:
```
[mapr@sat-node5 ~]$ /opt/mapr/kafka/kafka-0.9.0/bin/kafka-console-producer.sh  --broker-list 10.20.30.180:9092 --topic /user/mapr/msstream:mstopic
```
E).Once Producer started, Dump below sample data to Producer console.

```
{ "mcs_nodename": "10.20.30.200", "ubuntu-ps1.mapr.com": { "NODE": "ubuntu-ps1.mapr.com", "TIMESTAMPSTR": "Fri Jan 19 10:38:47 PST 2018", "TIMESTAMP": 1516387127000, "NETWORK": { "em1": { "BYTESIN": 1154671364992, "BYTESOUT": 2710545472195, "PKTSIN": 2364628202, "PKTSOUT": 3079678325 } }, "MEMORYUSED": 114121, "SERVUSEDSIZEMB": 0, "CPUSYSTEM": 417324170, "CPUUSER": 1293365936 }, "ubuntu-ps2.mapr.com": { "NODE": "ubuntu-ps2.mapr.com", "TIMESTAMPSTR": "Fri Jan 19 10:38:48 PST 2018", "TIMESTAMP": 1516387128000, "NETWORK": { "em1": { "BYTESIN": 1770812546285, "BYTESOUT": 1176688961794, "PKTSIN": 2378405444, "PKTSOUT": 1916259425 } }, "MEMORYUSED": 118266, "SERVUSEDSIZEMB": 218340, "CPUSYSTEM": 310395358, "CPUUSER": 1069811247 }, "ubuntu-ps3.mapr.com": { "NODE": "ubuntu-ps3.mapr.com", "TIMESTAMPSTR": "Fri Jan 19 10:38:44 PST 2018", "TIMESTAMP": 1516387124000, "NETWORK": { "em1": { "BYTESIN": 1336985038692, "BYTESOUT": 4176589586363, "PKTSIN": 2430899455, "PKTSOUT": 4200010531 } }, "MEMORYUSED": 126477, "SERVUSEDSIZEMB": 208325, "CPUSYSTEM": 422744000, "CPUUSER": 1414947723 }, "ubuntu-ps4.mapr.com": { "NODE": "ubuntu-ps4.mapr.com", "TIMESTAMPSTR": "Fri Jan 19 10:38:48 PST 2018", "TIMESTAMP": 1516387128000, "NETWORK": { "em1": { "BYTESIN": 4735397819656, "BYTESOUT": 1734812214406, "PKTSIN": 4146849086, "PKTSOUT": 2279520339 } }, "MEMORYUSED": 117810, "SERVUSEDSIZEMB": 215160, "CPUSYSTEM": 280477456, "CPUUSER": 615620453 }, "ubuntu-ps5.mapr.com": { "NODE": "ubuntu-ps5.mapr.com", "TIMESTAMPSTR": "Fri Jan 19 10:38:34 PST 2018", "TIMESTAMP": 1516387114000, "NETWORK": { "em1": { "BYTESIN": 2961184572636, "BYTESOUT": 2218000004776, "PKTSIN": 2866161431, "PKTSOUT": 2394413790 } }, "MEMORYUSED": 41498, "SERVUSEDSIZEMB": 224769, "CPUSYSTEM": 234213232, "CPUUSER": 61861923 } }
```

F). Go to the dorecotry where jar is available
```
[mapr@sat-node5 ps]$ pwd
/home/mapr/ps
```
G).Start Spark Streaming Application using below command.
```
[mapr@sat-node5 ps]$ /opt/mapr/spark/spark-2.1.0/bin/spark-submit --master yarn --deploy-mode client --class SparkStreamingApp SparkStreaming-OpenTSDB-6.0-1.0-SNAPSHOT.jar 10.10.71.23:9092 /user/mapr/msstream:mstopic earliest testgroup
```

Here job is successfully processing Streaming data.

Launch OpenTSDB webui to monitor the graph by choosing from and to ranges dates with metric names  like BYTESIN, BYTESOUT, SERVUSEDSIZEMB , MEMORYUSED, CPUSYSTEM , CPUUSER , PKTSIN , PKTSOUT .
```
http://10.10.75.35:4242/
```

