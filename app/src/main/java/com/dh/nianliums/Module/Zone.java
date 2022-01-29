package com.dh.nianliums.Module;


/**
 * 获取服务器查询到的区域信息
 */
public class Zone {
    private String zoneNumber;//区域编号：
    private String zone;//区域名称：
    private String informationZ;//备注信息：

    public String getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(String zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getInformationZ() {
        return informationZ;
    }

    public void setInformationZ(String informationZ) {
        this.informationZ = informationZ;
    }
}
