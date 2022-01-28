package org.simexid.greenpassvalidator.model.greenpass;

import java.io.Serializable;

public class NAM implements Serializable {
    
    String fnt;
    String fn;
    String gnt;
    String gn;

    public String getFnt() {
        return fnt;
    }

    public void setFnt(String fnt) {
        this.fnt = fnt;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getGnt() {
        return gnt;
    }

    public void setGnt(String gnt) {
        this.gnt = gnt;
    }

    public String getGn() {
        return gn;
    }

    public void setGn(String gn) {
        this.gn = gn;
    }
}
