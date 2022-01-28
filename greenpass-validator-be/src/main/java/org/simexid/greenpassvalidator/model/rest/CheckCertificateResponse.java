package org.simexid.greenpassvalidator.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckCertificateResponse implements Serializable {

    String name;
    String surname;
    Date birthDate;
    String Type;
    String vendorName;
    String productName;
    String diseaseAgent;
    String prophylaxis;
    int dose;
    int overallDose;
    Date date;
    String issuer;
    String identifier;
    String testDeviceIdentifier;
    Date validFrom;
    Date validUntil;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDiseaseAgent() {
        return diseaseAgent;
    }

    public void setDiseaseAgent(String diseaseAgent) {
        this.diseaseAgent = diseaseAgent;
    }

    public String getProphylaxis() {
        return prophylaxis;
    }

    public void setProphylaxis(String prophylaxis) {
        this.prophylaxis = prophylaxis;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getOverallDose() {
        return overallDose;
    }

    public void setOverallDose(int overallDose) {
        this.overallDose = overallDose;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTestDeviceIdentifier() {
        return testDeviceIdentifier;
    }

    public void setTestDeviceIdentifier(String testDeviceIdentifier) {
        this.testDeviceIdentifier = testDeviceIdentifier;
    }


    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }
}
