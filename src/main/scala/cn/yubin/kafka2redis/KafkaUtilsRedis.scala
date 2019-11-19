/*
package cn.itcast.kafka2redis

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by chouyarn of BI on 2018/8/21
  */
object KafkaUtilsRedis {
  /**
    * 根据groupId保存offset
    * @param ranges
    * @param groupId
    */
  def storeOffset(ranges: Array[OffsetRange], groupId: String): Unit = {
    for (o <- ranges) {
      val key = s"bi_kafka_offset_${groupId}_${o.topic}_${o.partition}"
      val value = o.untilOffset
      JedisUtil.set(key, value.toString)
    }
  }

  /**
    * 根据topic，groupid获取offset
    * @param topics
    * @param groupId
    * @return
    */
  def getOffset(topics: Array[String], groupId: String): (Map[TopicPartition, Long], Int) = {
    val fromOffSets = scala.collection.mutable.Map[TopicPartition, Long]()

    topics.foreach(topic => {
      val keys = JedisUtil.getKeys(s"bi_kafka_offset_${groupId}_${topic}*")
      if (!keys.isEmpty) {
        keys.asScala.foreach(key => {
          val offset = JedisUtil.get(key)
          val partition = Try(key.split(s"bi_kafka_offset_${groupId}_${topic}_").apply(1)).getOrElse("0")
          fromOffSets.put(new TopicPartition(topic, partition.toInt), offset.toLong)
        })
      }
    })
    if (fromOffSets.isEmpty) {
      (fromOffSets.toMap, 0)
    } else {
      (fromOffSets.toMap, 1)
    }
  }

  /**
    * 创建InputDStream，如果auto.offset.reset为latest则从redis读取
    * @param ssc
    * @param topic
    * @param kafkaParams
    * @return
    */
  def createStreamingContextRedis(ssc: StreamingContext, topic: Array[String],
                                  kafkaParams: Map[String, Object]): InputDStream[ConsumerRecord[String, String]] = {
    var kafkaStreams: InputDStream[ConsumerRecord[String, String]] = null
    val groupId = kafkaParams.get("group.id").get
    val (fromOffSet, flag) = getOffset(topic, groupId.toString)
    val offsetReset = kafkaParams.get("auto.offset.reset").get
    if (flag == 1 && offsetReset.equals("latest")) {
      kafkaStreams = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe(topic, kafkaParams, fromOffSet))
    } else {
      kafkaStreams = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe(topic, kafkaParams))
    }
    kafkaStreams
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("offSet Redis").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(60))
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "group.id" -> "binlog.test.rpt_test_1min",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean),
      "session.timeout.ms" -> "20000",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer]
    )
    val topic = Array("binlog.test.rpt_test", "binlog.test.hbase_test", "binlog.test.offset_test")
    val groupId = "binlog.test.rpt_test_1min"
    val lines = createStreamingContextRedis(ssc, topic, kafkaParams)
    lines.foreachRDD(rdds => {
      if (!rdds.isEmpty()) {
        println("##################:" + rdds.count())
      }
      storeOffset(rdds.asInstanceOf[HasOffsetRanges].offsetRanges, groupId)
    })

    ssc.start()
    ssc.awaitTermination()
  }
}*/
