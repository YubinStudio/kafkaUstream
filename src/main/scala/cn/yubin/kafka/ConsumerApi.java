package cn.yubin.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 消费者api
 */
public class ConsumerApi {
    public static void main(String[] args) throws InterruptedException {
        HashMap<String, Object> config = new HashMap<String, Object>();
        config.put("bootstrap.servers", "node-1:9092,node-2:9092,node-3:9092");
        config.put("key.deserializer", StringDeserializer.class.getName());
        config.put("value.deserializer", StringDeserializer.class.getName());
        config.put("auto.offset.reset", "earliest");  //[latest, earliest, none]从哪里开始获取数据
        config.put("group.id", "k001");//消费者组id
        config.put("enable.auto.commit", false);  //是否自动提交偏移量

        //创建消费者实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
        //订阅主题
        consumer.subscribe(Arrays.asList("mi-topic"));
        //拉取数据
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(3000);
            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord<String, String> record = iterator.next();
                String testValue = record.value();
                Thread.sleep(1000);
                System.out.println(testValue);
            }
        }
        // 1、准备配置文件
     /*   Properties props = new Properties();
        props.put("bootstrap.servers", "node-01:9092,node-02:9092,node-03:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
       //props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        // [latest, earliest, none]
        props.put("auto.offset.reset", "earliest"); // 从哪个位置开始获取数据
       // props.put("enable.auto.commit", false);// 是否要自动提交偏移量（offset）

// 2、创建 KafkaConsumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String,
                String>(props);
// 3、订阅数据，这里的 topic 可以是多个
        kafkaConsumer.subscribe(Arrays.asList("testTopic03"));
// 4、获取数据
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf(record.topic(), record.offset(), record.key(), record.value());

            }
        }*/


        //consumer.close();


    }
}
