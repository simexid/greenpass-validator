package org.simexid.greenpassvalidator.rest;

import COSE.CoseException;
import com.google.iot.cbor.CborParseException;
import org.simexid.greenpassvalidator.model.CheckCertificateRequest;
import org.simexid.greenpassvalidator.model.Greenpass;
import org.simexid.greenpassvalidator.model.rest.CheckCertificateResponse;
import org.simexid.greenpassvalidator.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RestApplication {

    @Autowired
    Util util;

    @PostMapping("checkCertificate")
    public CheckCertificateResponse checkCertificate(@RequestBody CheckCertificateRequest request) throws DataFormatException, CoseException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, CborParseException, IOException, ParseException {
        Greenpass gp = util.getCertificate(request.getCode());
        return util.getCertificateForResponse(gp);
    }

}
