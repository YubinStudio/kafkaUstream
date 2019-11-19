package cn.flume2spark2kafka.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CreateDataUtils {

    public static void createData(String path, long len, boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), flag));
            Random random = new Random();
            String arr[] = {"A", "B", "C", "D", "E", "F", "G", "H"};
            for (int i = 1; i <= len; i++) {
                bw.write("192.168.2." + (random.nextInt(6) + 200) + ","
                        + random.nextInt(2) + ","
                        + arr[random.nextInt(8)] + ","
                        + (random.nextInt(8) + 1) + ","
                        + (random.nextInt(12) + 1) + "级" + ","
                        + RandomNegativeNumber.sum(10, 30) + "℃" + ","
                        + String.format(String.format("%.2f", random.nextDouble() * 10)) + "m" + ","
                        + String.format(String.format("%.2f", random.nextDouble() * 1000)) + "N" + ","
                        + random.nextInt(100) + "Hz" + ","
                        + String.format(String.format("%.2f", random.nextDouble() * 10)) + "cm" + ","
                        + String.format(String.format("%.2f", random.nextDouble() * 10)) + "cm" + ","
                        + random.nextInt(10) + "C" + ","
                        + random.nextInt(2) + ","
                        + sdf.format(new Date()) + "\r\n");
                bw.flush();
                Thread.sleep(1000);
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("写入完成！");
        System.out.println("写入路径:" + path + "\n" + "写入行数:" + len + "\n" + "是否追加:" + flag);
    }
}
