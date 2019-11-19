package cn.flume2spark2kafka.utils

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * @program: kafkaUstream
  * @description: ${description}
  * @author: jyb
  * @create: 2019-11-11 17:53
  **/
object WriteToMysqlByScala {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[*]").getOrCreate()
    val arr: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
    val prop = new java.util.Properties
    prop.put("user", "root")
    prop.put("password", "root")
    val url = "jdbc:mysql://localhost:3306/kafka"
    val rddsource: RDD[Int] = spark.sparkContext.parallelize(arr)

    import spark.implicits._
    rddsource.toDF().write.mode(SaveMode.Append).jdbc(url, "transaction_data", prop)
    println("写入完成！")
    spark.stop()
  }

  case class bridge()

}
