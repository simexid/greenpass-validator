package org.simexid.greenpassvalidator.command;

import COSE.CoseException;
import com.google.iot.cbor.CborParseException;
import org.simexid.greenpassvalidator.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.zip.DataFormatException;

@Component
public class AfterStartup {

    @Autowired
    Util util;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws CertificateException, DataFormatException, CoseException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, CborParseException {
        util.retrieveValidKids();
        util.retrieveValidPublicKey("0");
        util.getData();
    }

}
