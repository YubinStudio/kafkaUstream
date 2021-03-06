/*
package cn.yubin.kafka2mysql

import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import scalikejdbc.config.DBs
import scalikejdbc.{DB, SQL}

/**
  * kafka数据读取，偏移量自己管理，偏移量数据传入mysql。
  * log数据可以使用其他的进行保存。
  */
object kafka2mysql {

  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("xx")
      //每秒钟每个分区kafka拉取消息的速率
      .set("spark.streaming.kafka.maxRatePerPartition", "100")
      // 序列化
      .set("spark.serilizer", "org.apache.spark.serializer.KryoSerializer")
      // 建议开启rdd的压缩
      .set("spark.rdd.compress", "true")
    val ssc = new StreamingContext(conf, Seconds(2))

    //一参数设置
    val groupId = "1"
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hdp01:9092,hdp02:9092,hdp03:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean) //自己维护偏移量。连接kafka的集群。
    )
    val topics = Array("test")

    //二参数设置
    DBs.setup()
    val fromdbOffset: Map[TopicPartition, Long] =
      DB.readOnly { implicit session =>
        SQL(s"select * from `offset` where groupId = '${groupId}'")
          .map(rs => (new TopicPartition(rs.string("topic"), rs.int("partition")), rs.long("untilOffset")))
          .list().apply()

      }.toMap

    //程序启动，拉取kafka的消息。
    val stream = if (fromdbOffset.size == 0) {
      KafkaUtils.createDirectStream[String, String](
        ssc,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
      )
    } else {
      KafkaUtils.createDirectStream(
        ssc,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Assign[String, String](fromdbOffset.keys, kafkaParams, fromdbOffset)
      )
    }


    stream.foreachRDD({
      rdd =>
        val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

        //数据处理
        val resout: RDD[(String, Int)] = rdd.flatMap(_.value().split(" ")).map((_, 1)).reduceByKey(_ + _)
        resout.foreach(println)
        resout.foreachPartition({
          it =>
            val jedis = RedisUtils.getJedis
            it.foreach({
              va =>
                jedis.hincrBy("wc", va._1, va._2)
            })
            jedis.close()
        })
        //偏移量存入mysql，使用scalikejdbc框架事务
        DB.localTx { implicit session =>
          for (or <- offsetRanges) {
            SQL("replace into `offset`(groupId,topic,partition,untilOffset) values(?,?,?,?)")
              .bind(groupId, or.topic, or.partition, or.untilOffset).update().apply()
          }
        }
    })

    ssc.start()
    ssc.awaitTermination()

  }
}*/
