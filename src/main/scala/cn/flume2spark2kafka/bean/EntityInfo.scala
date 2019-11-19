package cn.flume2spark2kafka.bean

object EntityInfo {
  case class bridge(nodeIp: String, status: Int, weather: String, windDirection: Int, windSpeed: String, temperature: String, waterLevel: String,
                    gravity: String, frequency: String, subsidenceDegree: String, displacementDegree: String,
                    tiltDegree: String, affectResult: Int, dataTime: String)

}
