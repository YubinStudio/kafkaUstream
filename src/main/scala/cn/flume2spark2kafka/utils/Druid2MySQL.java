package cn.flume2spark2kafka.utils;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @program: javautils
 * @description: 阿里的druid连接MySQL
 * @author: jyb
 * @create: 2019-09-06 16:39
 **/
public class Druid2MySQL {
    private static DruidDataSource dataSource = null;
    private static final String inPath = "H:\\myIdea\\kafka\\kafkaUstream\\src\\main\\resources\\mysqlCon.properties";
    private static Properties properties = new Properties();

    /**
     * 构造函数完成数据库的连接和连接对象的生成
     */
    public Druid2MySQL() {

    }

    private static void getDruidConnect() {
        try {
            FileInputStream stream = new FileInputStream(new File(inPath));
            properties.load(stream);
            if (null == dataSource) {
                dataSource = new DruidDataSource();
                //设置连接参数mysql
                dataSource.setUrl(properties.getProperty("url"));
                dataSource.setDriverClassName(properties.getProperty("driver"));
                dataSource.setUsername(properties.getProperty("username"));
                dataSource.setPassword(properties.getProperty("password"));
                //配置初始化大小、最小、最大
                dataSource.setInitialSize(1);
                dataSource.setMinIdle(1);
                dataSource.setMaxActive(20);
                //连接泄漏监测
                dataSource.setRemoveAbandoned(true);
                dataSource.setRemoveAbandonedTimeout(30);
                //配置获取连接等待超时的时间
                dataSource.setMaxWait(20000);
                //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
                dataSource.setTimeBetweenEvictionRunsMillis(20000);
                //防止过期
                dataSource.setValidationQuery("SELECT 'x'");
                dataSource.setTestWhileIdle(true);
                dataSource.setTestOnBorrow(true);
                System.out.println("数据库配置成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getClass() + " ," + e.getMessage());
        }
    }

    public static Connection getConnection() {
        Connection con = null;
        getDruidConnect();
        try {
            con = dataSource.getConnection();
            System.out.println("获取数据库连接成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

}
