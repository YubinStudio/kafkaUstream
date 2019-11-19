package cn.flume2spark2kafka.bean;

import java.io.Serializable;

/**
 * @program: javautils
 * @description: 桥梁信息
 * @author: jyb
 * @create: 2019-10-31 19:46
 **/
public class BridgeInfo implements Serializable {
    private String nodeIp;              //传感器编号
    private int status;                 //传感器状态
    private String weather;             //天气
    private int windDirection;          //风向
    private String windSpeed;           //风速，1-10级
    private String temperature;         //温度，-20—40
    private String waterLevel;          //水位,-20—40
    private String gravity;             //重力
    private String frequency;           //自振频率
    private String subsidenceDegree;    //桥墩沉降度,(0-3)cm
    private String displacementDegree;  //桥墩位移度,(0-3)cm
    private String tiltDegree;          //桥墩倾斜度
    private int affectResult;           //影响结果
    private String dataTime;           //日期

    public BridgeInfo() {
    }

    public BridgeInfo(String nodeIp, int status, String weather, int windDirection, String windSpeed, String temperature, String waterLevel, String gravity, String frequency, String subsidenceDegree, String displacementDegree, String tiltDegree, int affectResult, String dataTime) {
        this.nodeIp = nodeIp;
        this.status = status;
        this.weather = weather;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.waterLevel = waterLevel;
        this.gravity = gravity;
        this.frequency = frequency;
        this.subsidenceDegree = subsidenceDegree;
        this.displacementDegree = displacementDegree;
        this.tiltDegree = tiltDegree;
        this.affectResult = affectResult;
        this.dataTime = dataTime;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(String waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSubsidenceDegree() {
        return subsidenceDegree;
    }

    public void setSubsidenceDegree(String subsidenceDegree) {
        this.subsidenceDegree = subsidenceDegree;
    }

    public String getDisplacementDegree() {
        return displacementDegree;
    }

    public void setDisplacementDegree(String displacementDegree) {
        this.displacementDegree = displacementDegree;
    }

    public String getTiltDegree() {
        return tiltDegree;
    }

    public void setTiltDegree(String tiltDegree) {
        this.tiltDegree = tiltDegree;
    }

    public int getAffectResult() {
        return affectResult;
    }

    public void setAffectResult(int affectResult) {
        this.affectResult = affectResult;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    @Override
    public String toString() {
        return "BridgeInfo{" +
                "nodeIp='" + nodeIp + '\'' +
                ", status=" + status +
                ", weather='" + weather + '\'' +
                ", windDirection=" + windDirection +
                ", windSpeed='" + windSpeed + '\'' +
                ", temperature='" + temperature + '\'' +
                ", waterLevel='" + waterLevel + '\'' +
                ", gravity='" + gravity + '\'' +
                ", frequency='" + frequency + '\'' +
                ", subsidenceDegree='" + subsidenceDegree + '\'' +
                ", displacementDegree='" + displacementDegree + '\'' +
                ", tiltDegree='" + tiltDegree + '\'' +
                ", affectResult=" + affectResult +
                ", dataTime='" + dataTime + '\'' +
                '}';
    }
}
