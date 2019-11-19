package cn.flume2spark2kafka.utils

import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.Date

import com.typesafe.config.ConfigFactory
import org.apache.kafka.common.TopicPartition

/**
  * coder mi
  * Created 2019/11/04
  */
object OffsetManager {


  // 加载配置信息

  val config = ConfigFactory.load("application.properties")

  def getConn = {
    DriverManager.getConnection(config.getString("db.url"),
      config.getString("db.user"),
      config.getString("db.password")
    )
  }

  /**
    * 从数据库中把偏移量拿出来
    */
  def apply(groupId: String) = {

    var map = Map[TopicPartition, Long]()
    val conn = getConn
    val stmt = conn.prepareStatement(s"select * from kafka_offset where groupId='$groupId'")
    val rs = stmt.executeQuery()
    while (rs.next()) {
      map += new TopicPartition(rs.getString("topicName"), rs.getInt("partitionId")) -> rs.getLong("offset")
    }

    rs.close()
    stmt.close()
    conn.close()
    map


    /* DB.readOnly { implicit session =>
         SQL("select * from kafka_offset where groupId=?").bind(groupId)
           .map(rs => {
               new TopicPartition(rs.string("topicName"), rs.int("partitionId")) -> rs.long("offset")
           })
           .list().apply()
     }.toMap*/


  }

  //获取当前系统时间
  def NowDate(): String = {
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss")
    val date = dateFormat.format(now)
    return date
  }


}
