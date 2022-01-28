package org.simexid.greenpassvalidator.util;

import org.simexid.greenpassvalidator.model.data.DataJsonFile;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertUtil {

    public Map<String, PublicKey> keyMap = new HashMap<>();
    public List<String> kids = null;
    public DataJsonFile testDevices;
    public DataJsonFile vaccine;
    public DataJsonFile prophylaxis;
    public DataJsonFile manufactorer;

    public Map<String, PublicKey> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, PublicKey> keyMap) {
        this.keyMap = keyMap;
    }

    public List<String> getKids() {
        return kids;
    }

    public void setKids(List<String> kids) {
        this.kids = kids;
    }

    public DataJsonFile getTestDevices() {
        return testDevices;
    }

    public void setTestDevices(DataJsonFile testDevices) {
        this.testDevices = testDevices;
    }

    public DataJsonFile getVaccine() {
        return vaccine;
    }

    public void setVaccine(DataJsonFile vaccine) {
        this.vaccine = vaccine;
    }

    public DataJsonFile getProphylaxis() {
        return prophylaxis;
    }

    public void setProphylaxis(DataJsonFile prophylaxis) {
        this.prophylaxis = prophylaxis;
    }

    public DataJsonFile getManufactorer() {
        return manufactorer;
    }

    public void setManufactorer(DataJsonFile manufactorer) {
        this.manufactorer = manufactorer;
    }
}
