package cn.flume2spark2kafka.utils;

import java.util.Random;

public class RandomNegativeNumber {
    /**
     * 随机
     * 正负数
     * 使用方法: RandomNegativeNumber.sum(最小数,最大数);
     */
    public static int sum(int min, int max) {
        int result;
        Random random = new Random();
        int sumNum = random.nextInt(max - min + 1) + min;
        int temp = random.nextInt(10) + 1;
        Int2String i2s = new Int2String(0);
        i2s.setInt(sumNum);
        String randomArg = i2s.returnString();
        switch (temp) {
            case 1:
                if ((randomArg.contains("0")) || (randomArg.contains("9"))) {
                    randomArg = "-" + randomArg;
                    break;
                }
            case 2:
                if ((randomArg.contains("1")) || (randomArg.contains("8"))) {
                    randomArg = "-" + randomArg;
                    break;
                }
            case 3:
                if ((randomArg.contains("2")) || (randomArg.contains("7"))) {
                    randomArg = "-" + randomArg;
                    break;
                }
            case 4:
                if ((randomArg.contains("3")) || (randomArg.contains("6"))) {
                    randomArg = "-" + randomArg;
                    break;
                }
            case 5:
                if ((randomArg.contains("4")) || (randomArg.contains("5"))) {
                    randomArg = "-" + randomArg;
                    break;
                }
        }
        sumNum = Integer.parseInt(randomArg);
        result = sumNum;
        return result;
    }

    static class Int2String {
        String ArgFloor = "";

        public Int2String(int FloorInt) {
            ArgFloor = FloorInt + "";
        }

        String returnString() {
            return ArgFloor;
        }

        void setInt(int FloorInt) {
            ArgFloor = FloorInt + "";
        }
    }

    public static void main(String[] args) {
        System.out.println(sum(10, 40));
    }

}
