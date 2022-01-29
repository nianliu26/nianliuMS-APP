package com.dh.nianliums.Module;


/**
 * 保存服务器发送的工具数据
 */
public class Instrument {
    private String instrumentNumber;//工具编号：
    private String instrument;//工具名称：
    private String instrumentStatus;//工具状况：
    private String informationI;//备注信息：

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getInstrumentStatus() {
        return instrumentStatus;
    }

    public void setInstrumentStatus(String instrumentStatus) {
        this.instrumentStatus = instrumentStatus;
    }

    public String getInformationI() {
        return informationI;
    }

    public void setInformationI(String informationI) {
        this.informationI = informationI;
    }
}
