package org.simexid.greenpassvalidator.model.data;

import java.io.Serializable;
import java.util.HashMap;

public class DataJsonFile implements Serializable {

    HashMap<String, DataValues> valueSetValues;

    public HashMap<String, DataValues> getValueSetValues() {
        return valueSetValues;
    }

    public void setValueSetValues(HashMap<String, DataValues> valueSetValues) {
        this.valueSetValues = valueSetValues;
    }
}
