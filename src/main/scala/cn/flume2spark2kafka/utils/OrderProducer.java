package cn.flume2spark2kafka.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * 订单的生产者代码
 */

public class OrderProducer {
    public static void main(String[] args) throws InterruptedException {
         /* 1、连接集群，通过配置文件的方式
         * 2、发送数据-topic:order，value
         */
        Properties props = new Properties();
        props.put("bootstrap.servers", "node-1:9092,node-2:9092,node-3:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("group.id", "groupId2");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);
        /*for (int i = 0; i < 200; i++) {
            // 发送数据 ,需要一个producerRecord对象,最少参数 String topic, V value
            kafkaProducer.send(new ProducerRecord<String, String>("yu-topic", "路人-" + i));
            Thread.sleep(100);
        }*/
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("H:\\flume2kafka.txt")));
            String line = "";
            while ((line = br.readLine()) != null) {
                kafkaProducer.send(new ProducerRecord<String, String>("yu-topic", line));
                Thread.sleep(100);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("写入完成！");
    }
}
