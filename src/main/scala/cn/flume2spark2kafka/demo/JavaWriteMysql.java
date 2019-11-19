package cn.flume2spark2kafka.demo;

import cn.flume2spark2kafka.bean.BridgeInfo;
import cn.flume2spark2kafka.utils.Druid2MySQL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: Scala
 * @description:
 * @author: jyb
 * @create: 2019-11-07 09:45
 **/
public class JavaWriteMysql {
    //    ConfigFactory
    final static String path = "";
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = Druid2MySQL.getConnection();
            prep = conn.prepareStatement("insert into source_data values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            BufferedReader br = new BufferedReader(new FileReader(new File("H:\\flume2kafka.txt")));
            String str = "";
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
//                Thread.sleep(1000);
//                System.out.println(str);
                String[] split = str.split(",");
                System.out.println(split.length);
                prep.setString(1, split[0]);
                prep.setString(2, split[1]);
                prep.setString(3, split[2]);
                prep.setString(4, split[3]);
                prep.setString(5, split[4]);
                prep.setString(6, split[5]);
                prep.setString(7, split[6]);
                prep.setString(8, split[7]);
                prep.setString(9, split[8]);
                prep.setString(10, split[9]);
                prep.setString(11, split[10]);
                prep.setString(12, split[11]);
                prep.setString(13, split[12]);
                prep.setString(14, split[13]);

                prep.executeUpdate();

                BridgeInfo bridgeInfo = new BridgeInfo();

                bridgeInfo.setNodeIp(split[0]);
                bridgeInfo.setStatus(Integer.valueOf(split[1]));
                bridgeInfo.setWeather(split[2]);
                bridgeInfo.setWindDirection(Integer.valueOf(split[3]));
                bridgeInfo.setWindSpeed(split[4]);
                bridgeInfo.setTemperature(split[5]);
                bridgeInfo.setWaterLevel(split[6]);
                bridgeInfo.setGravity(split[7]);
                bridgeInfo.setFrequency(split[8]);
                bridgeInfo.setSubsidenceDegree(split[9]);
                bridgeInfo.setDisplacementDegree(split[10]);
                bridgeInfo.setTiltDegree(split[11]);
                bridgeInfo.setAffectResult(Integer.valueOf(split[12]));
                bridgeInfo.setDataTime(split[13]);
                System.out.println(bridgeInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static BridgeInfo transToBridgeInfo() {
        return new BridgeInfo();
    }
}
