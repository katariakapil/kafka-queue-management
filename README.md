# kafka-queue-management
# kafka-queue-managment


install kafka 

1) brew install kafka 

2) Download kafka for all config files.

https://www.apache.org/dyn/closer.cgi?path=/kafka/2.1.0/kafka_2.12-2.1.0.tgz

3) start zookeeper 

zookeeper-server-start kafka_2.12-2.1.0/config/zookeeper.properties 

it start on following port

0.0.0.0/0.0.0.0:2181

4) start server

 kafka-server-start kafka_2.12-2.1.0/config/server.properties 
 
 it will start on following port
 
 0.0.0.0:9092

SERVERS ARE UP NOW

1) Create topic 

kafka-topics --zookeeper localhost:2181 --topic first_topic --create --partitions 3 --replication-factor 1


2) List topic

kafka-topics --zookeeper localhost:2181 --list

MACLTUS44965:~ kkataria$ kafka-topics --zookeeper localhost:2181 --list
first_topic

3) To describe Topic 

MACLTUS44965:~ kkataria$ kafka-topics --zookeeper localhost:2181 --topic first_topic --describe
Topic:first_topic	PartitionCount:3	ReplicationFactor:1	Configs:
	Topic: first_topic	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: first_topic	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: first_topic	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
  
4) Send message 

 kafka-console-producer --broker-list localhost:9092 --topic first_topic 

MACLTUS44965:~ kkataria$ kafka-console-producer --broker-list localhost:9092 --topic first_topic 
>test

control + c to exit

5) Reading messages 

kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic




