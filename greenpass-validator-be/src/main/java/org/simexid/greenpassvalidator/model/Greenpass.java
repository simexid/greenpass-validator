package org.simexid.greenpassvalidator.model;

import org.simexid.greenpassvalidator.model.greenpass.NAM;
import org.simexid.greenpassvalidator.model.greenpass.R;
import org.simexid.greenpassvalidator.model.greenpass.T;
import org.simexid.greenpassvalidator.model.greenpass.V;

import java.io.Serializable;
import java.util.List;

public class Greenpass implements Serializable {

    List<V> v;
    List<R> r;
    List<T> t;
    NAM nam;
    String ver;
    String dob;

    public List<V> getV() {
        return v;
    }

    public void setV(List<V> v) {
        this.v = v;
    }

    public List<R> getR() {
        return r;
    }

    public void setR(List<R> r) {
        this.r = r;
    }

    public List<T> getT() {
        return t;
    }

    public void setT(List<T> t) {
        this.t = t;
    }

    public NAM getNam() {
        return nam;
    }

    public void setNam(NAM nam) {
        this.nam = nam;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

}
