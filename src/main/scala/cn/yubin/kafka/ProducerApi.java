package cn.yubin.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.UUID;


/**
 * kafka api
 * 生产数据
 */
public class ProducerApi {
    static Logger logger = Logger.getLogger(String.valueOf(ProducerApi.class));
    public static void main(String[] args) throws InterruptedException {
        //初始化 log4j 需要的变量
        String rootPath = System.getProperty("user.dir");
        System.setProperty("log.base",rootPath);

        try {
            Properties props = new Properties();
            props.setProperty("bootstrap.servers", "node-01:9092,node-02:9092,node-03:9092");
            props.setProperty("key.serializer", StringSerializer.class.getName());
            props.setProperty("value.serializer", StringSerializer.class.getName());

            //创建生产者对象实例
            KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);

            int i = 1;
            while (i <= 1000) {
                int part = i % 3;
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("mi-topic", part, UUID.randomUUID().toString(), "dami" + i);
                //发送数据
                kafkaProducer.send(record);
                i++;
                Thread.sleep(1000);
                System.out.println("record=" + record);
            }


            kafkaProducer.close();
            System.out.println("生产结束！");
        } catch (Exception e) {
            logger.info("--------info--------"+e.getMessage()+e.getCause());
            logger.debug("--------debug--------"+e.getClass().getName()+e.getCause().getLocalizedMessage());
            logger.error("--------error--------"+e.getClass().getName()+e.getCause().getLocalizedMessage());
        }
    }
}
