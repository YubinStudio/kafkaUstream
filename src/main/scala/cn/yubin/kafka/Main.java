package cn.yubin.kafka;

import org.apache.log4j.Logger;

/**
 * @program: spark
 * @description:
 * @author: jyb
 * @create: 2019-11-04 10:52
 **/
public class Main {
    static Logger logger = Logger.getLogger(Main.class);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(1/0);
        logger.info("----------info");
        logger.debug("----------debug");
        logger.error("----------error");
        logger.trace("----------trace");
        // TODO code application logic here
    }

}
