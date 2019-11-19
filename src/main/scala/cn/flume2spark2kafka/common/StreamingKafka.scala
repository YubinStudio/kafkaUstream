package cn.flume2spark2kafka.common

import java.sql.{Connection, PreparedStatement}

import cn.flume2spark2kafka.utils.OffsetManager
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
//import scala.collection.JavaConversions
/**
  * sparkStreaming整合kafka
  * 直连 direct kafka方式
  */

object StreamingKafka {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local[*]")
    conf.setAppName("StreamingKafka")
    conf.set("spark.streaming.kafka.maxRatePerPartition", "5") // 批次最大数据量 ，3（分区数） * 5 * 2（批次时间）
    conf.set("spark.streaming.stopGracefullyOnShutdown", "true") // 程序优雅的关闭
    conf.set("fetch.message.max.bytes", "52428800")
//    conf.registerKryoClasses(Array(classOf[Info], classOf[scala.collection.mutable.WrappedArray.ofRef[_]]))

    //设置批次时间
//    val session: SparkSession = SparkSession.builder().getOrCreate()
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(2))

    //封装kafka的相关参数
    val groupId = "group02"
    val kafkaParams: Map[String, Object] = Map[String, Object](
      "bootstrap.servers" -> "node-1:9092,node-2:9092,node-3:9092",
      "key.deserializer" -> classOf[StringDeserializer], // 类.class.getName
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "latest", //latest
      "enable.auto.commit" -> (true: java.lang.Boolean) // 不记录消费的偏移量信息
    )

    // 如何告诉程序从我们自己维护的偏移量位置开始拉去数据？
    // 如果程序是第一次启动，则不需要指定偏移量，直接从最早的位置开始消费即可
    // 如果程序非第一启动，则需要指定我们自己维护的偏移量，从指定的位置开始消费数据
    // 如何判断程序是第一次启动？当数据库中没有数据的时候，我们就认为程序是第一次启动
    val offsets: Map[TopicPartition, Long] = OffsetManager(groupId)

    val stream: InputDStream[ConsumerRecord[String, String]] = if (offsets.size == 0) {
      KafkaUtils.createDirectStream[String, String](ssc,
        LocationStrategies.PreferConsistent, // 将拉去到的数据，均匀分散到每台Executor节点上
        ConsumerStrategies.Subscribe[String, String](Array("yu-topic"), kafkaParams)
      )
    } else {
      KafkaUtils.createDirectStream[String, String](ssc,
        LocationStrategies.PreferConsistent, // 将拉去到的数据，均匀分散到每台Executor节点上
        ConsumerStrategies.Subscribe[String, String](Array("yu-topic"), kafkaParams, offsets)
      )
    }

    stream.foreachRDD(rdd => {
      val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      //获取jdbc连接
      val conn: Connection = OffsetManager.getConn
      val prop = new java.util.Properties
      prop.put("user", "root")
      prop.put("password", "root")
      rdd.foreach(line =>{
        line.value().asInstanceOf[DataFrame].write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/kafka","kafka.transaction_data",prop)
        println(line.value())
      })
      println("写入数据到mysql")
      //=======================计算逻辑始===========================

      /*val prep: PreparedStatement = conn.prepareStatement("insert into transaction_data values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)")


      val index= rdd.foreach(data => {
        val result: Array[String] = data.value().split(",")
        result
      }).asInstanceOf[Array[String]]

      //储存数据到mysql
      //        println(s"${index(0)},${index(1)}")

      for (i <- 0 until index.length) {
        println(index(i))
        if (i == 1 || i == 3 || i == 12) {
          prep.setInt(i + 1, index(i).toInt)
          prep.execute()
        } else {
          prep.setString(i + 1, index(i))
          prep.execute()
        }

      }
      prep.setString(1, index(0))
      prep.setInt(2, index(1).toInt)
      prep.setString(3, index(2))
      prep.setInt(4, index(3).toInt)
      prep.setString(5, index(4))
      prep.setString(6, index(5))
      prep.setString(7, index(6))
      prep.setString(8, index(7))
      prep.setString(9, index(8))
      prep.setString(10, index(9))
      prep.setString(11, index(10))
      prep.setString(12, index(11))
      prep.setInt(13, index(12).toInt)
      prep.setString(14, OffsetManager.NowDate())
      /*bridge(index(0), index(1).toInt, index(2), index(3).toInt, index(4), index(5), index(6), index(7), index(8),
        index(9), index(10), index(11), index(12).toInt, OffsetManager.NowDate())*/
      prep.executeUpdate()

      prep.close()*/


      //rdd.filter(x => x==x.value()).foreach(println)


      //=======================计算逻辑终===========================


      //储存偏移量
      val prepareStatement: PreparedStatement = conn.prepareStatement("replace into kafka_offset values(?,?,?,?)")

      for (or <- offsetRanges) {
        println(s"${or.topic}, ${or.partition}, ${or.fromOffset}, ${or.untilOffset}")
        prepareStatement.setString(1, or.topic)
        prepareStatement.setInt(2, or.partition)
        prepareStatement.setLong(3, or.untilOffset)
        prepareStatement.setString(4, groupId)

        //执行
        prepareStatement.execute()
      }

      prepareStatement.close()
      conn.close()
    }

    )


    ssc.start()
    ssc.awaitTermination()

  }

  case class bridge(nodeIp: String, status: Int, weather: String, windDirection: Int, windSpeed: String, temperature: String, waterLevel: String,
                    gravity: String, frequency: String, subsidenceDegree: String, displacementDegree: String,
                    tiltDegree: String, affectResult: Int, dataTime: String)

}
