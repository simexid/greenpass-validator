package org.simexid.greenpassvalidator.util;

import COSE.CoseException;
import COSE.Encrypt0Message;
import COSE.HeaderKeys;
import COSE.Message;
import com.google.gson.Gson;
import com.google.iot.cbor.CborParseException;
import com.upokecenter.cbor.CBORObject;
import nl.minvws.encoding.Base45;
import org.simexid.greenpassvalidator.model.Greenpass;
import org.simexid.greenpassvalidator.model.data.DataJsonFile;
import org.simexid.greenpassvalidator.model.rest.CheckCertificateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import static java.nio.charset.StandardCharsets.UTF_8;


@Service
public class Util {

    private static final int ECDSA_256 = -7;
    private static final int RSA_PSS_256 = -37;
    private static final int BUFFER_SIZE = 1024;

    @Autowired
    CertUtil certUtil;

    @Value("classpath:data/test.json")
    Resource testFile;

    @Value("classpath:data/vaccine.json")
    Resource vaccineFile;

    @Value("classpath:data/prophylaxis.json")
    Resource prophylaxisFile;

    @Value("classpath:data/manufactorer.json")
    Resource manufactorerFile;

    public ResponseEntity<String> retrieveValidPublicKey(String xResumeToken) throws CertificateException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-resume-token", xResumeToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response;
        response = restTemplate.exchange("https://get.dgc.gov.it/v1/dgc/signercertificate/update", HttpMethod.GET, entity, String.class);
        if (response.getStatusCode()== HttpStatus.OK) {
            HttpHeaders headersRec =  response.getHeaders();
            List<String> kids = certUtil.getKids();
            String xKid = headersRec.getFirst("x-kid");
            if (kids.contains(xKid)) {
                String h = headersRec.getFirst("x-kid");
                Map<String, PublicKey> k = new HashMap<>();
                k.put(h, getPublicKey(response.getBody()));
                certUtil.getKeyMap().put(h, getPublicKey(response.getBody()));

                if (headersRec.getFirst("x-resume-token")!= null
                        && !headersRec.getFirst("x-resume-token").isEmpty()) {
                    retrieveValidPublicKey(headersRec.getFirst("x-resume-token"));
                }
            }
        }
        return response;
    }

    public List<String> retrieveValidKids() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String[]> response;
        response = restTemplate.exchange("https://get.dgc.gov.it/v1/dgc/signercertificate/status", HttpMethod.GET, entity, String[].class);
        certUtil.setKids(List.of(response.getBody()));
        return List.of(response.getBody());
    }

    public Greenpass getCertificate(String code) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, DataFormatException, CoseException, CborParseException, IOException {

        byte[] bytecompressed = Base45.getDecoder().decode(code.substring(4));

        Inflater inflater = new Inflater();
        inflater.setInput(bytecompressed);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytecompressed.length);
        byte[] buffer = new byte[BUFFER_SIZE];
        while (!inflater.finished()) {
            final int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        Message a = Encrypt0Message.DecodeFromBytes(outputStream.toByteArray());
        CBORObject messageObject = CBORObject.DecodeFromBytes(outputStream.toByteArray());
        byte[] coseSignature = messageObject.get(3).GetByteString();
        byte[] protectedHeader = messageObject.get(0).GetByteString();
        byte[] content = messageObject.get(2).GetByteString();
        byte[] dataToBeVerified = getValidationData(protectedHeader, content);
        CBORObject unprotectedHeader = messageObject.get(1);
        byte[] kid = getKid(protectedHeader, unprotectedHeader);
        String kidBase64 = new String(Base64.getEncoder().encode(kid));

        System.out.println("kid=" + kidBase64);
        Map<String, PublicKey> keyMap = certUtil.getKeyMap();
        PublicKey key = keyMap.get(kidBase64);
        CBORObject obj = CBORObject.DecodeFromBytes(messageObject.get(2).GetByteString());

        CBORObject c = obj.get(-260);
        Greenpass greenpass = new Gson().fromJson(c.get(1).toString(), Greenpass.class);

        switch (getAlgoFromHeader(protectedHeader, unprotectedHeader)) {

            case ECDSA_256:
                System.out.println("ECDSA_256");

                Signature signature = Signature.getInstance("SHA256withECDSA");
                signature.initVerify(key);
                signature.update(dataToBeVerified);
                coseSignature = ConvertToDer.convertToDer(coseSignature);
                if (signature.verify(coseSignature)) {
                    return greenpass;
                } else {
                    return null;
                }

            case RSA_PSS_256:
                System.out.println("RSA_PSS_256 - not implemented");
                return null;
        }

        return null;
    }

    private static byte[] getKid(byte[] protectedHeader, CBORObject unprotectedHeader) {
        CBORObject key = HeaderKeys.KID.AsCBOR();
        CBORObject kid;
        if (protectedHeader.length != 0) {
            try {
                kid = CBORObject.DecodeFromBytes(protectedHeader).get(key);
                if (kid == null) {
                    kid = unprotectedHeader.get(key);
                }
            } catch (Exception var8) {
                kid = unprotectedHeader.get(key);
            }
        } else {
            kid = unprotectedHeader.get(key);
        }
        return kid.GetByteString();
    }

    private static int getAlgoFromHeader(byte[] protectedHeader, CBORObject unprotectedHeader) {
        int algoNumber;
        if (protectedHeader.length != 0) {
            try {
                CBORObject algo = CBORObject.DecodeFromBytes(protectedHeader).get(1);
                algoNumber = algo != null ? algo.AsInt32Value() : unprotectedHeader.get(1).AsInt32Value();
            } catch (Exception var7) {
                algoNumber = unprotectedHeader.get(1).AsInt32Value();
            }
        } else {
            algoNumber = unprotectedHeader.get(1).AsInt32Value();
        }
        return algoNumber;
    }

    private static byte[] getValidationData(byte[] protectedHeader, byte[] content) {
        return CBORObject.NewArray().
                Add("Signature1").
                Add(protectedHeader).
                Add(new byte[0]).
                Add(content).EncodeToBytes();
    }

    public static PublicKey getPublicKey(String key) throws CertificateException {
        var decoded = Base64.getDecoder().decode(key);
        InputStream inputStream = new ByteArrayInputStream(decoded);
        return CertificateFactory.getInstance("X.509").generateCertificate(inputStream).getPublicKey();
    }

    public static String getStringFromResource(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return  FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void getData() {
        certUtil.setTestDevices(new Gson().fromJson(getStringFromResource(testFile), DataJsonFile.class));
        certUtil.setProphylaxis(new Gson().fromJson(getStringFromResource(prophylaxisFile), DataJsonFile.class));
        certUtil.setVaccine(new Gson().fromJson(getStringFromResource(vaccineFile), DataJsonFile.class));
        certUtil.setManufactorer(new Gson().fromJson(getStringFromResource(manufactorerFile), DataJsonFile.class));
    }

    public CheckCertificateResponse getCertificateForResponse(Greenpass gp) throws ParseException {
        CheckCertificateResponse certResponse = new CheckCertificateResponse();
        certResponse.setName(gp.getNam().getGn());
        certResponse.setSurname(gp.getNam().getFn());
        certResponse.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(gp.getDob()));
        if (gp.getR()!=null && gp.getR().size()==1) {
            certResponse.setValidFrom(new SimpleDateFormat("yyyy-MM-dd").parse(gp.getR().get(0).getDf()));
            certResponse.setValidUntil(new SimpleDateFormat("yyyy-MM-dd").parse(gp.getR().get(0).getDu()));

            certResponse.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(gp.getR().get(0).getFr()));
            certResponse.setDiseaseAgent(gp.getR().get(0).getTg().equals("840539006") ? "COVID-19" : null);
            certResponse.setIdentifier(gp.getR().get(0).getCi());
            certResponse.setIssuer(gp.getR().get(0).getIs());

            certResponse.setType("RECOVERY");
        } else if (gp.getV()!=null && gp.getV().size()==1) {

            certResponse.setProphylaxis(certUtil.getProphylaxis().getValueSetValues().get(gp.getV().get(0).getVp()).getDisplay());
            certResponse.setDose(gp.getV().get(0).getDn());
            certResponse.setOverallDose(gp.getV().get(0).getSd());

            certResponse.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(gp.getV().get(0).getDt()));
            certResponse.setDiseaseAgent(gp.getV().get(0).getTg().equals("840539006") ? "COVID-19" : null);
            certResponse.setIdentifier(gp.getV().get(0).getCi());
            certResponse.setIssuer(gp.getV().get(0).getIs());
            certResponse.setProductName(certUtil.getVaccine().getValueSetValues().get(gp.getV().get(0).getMp()).getDisplay());
            certResponse.setVendorName(certUtil.getManufactorer().getValueSetValues().get(gp.getV().get(0).getMa()).getDisplay());

            certResponse.setType("VACCINE");
        } else if (gp.getT()!=null && gp.getT().size()==1) {
            certResponse.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(gp.getT().get(0).getSc()));
            certResponse.setDiseaseAgent(gp.getT().get(0).getTg().equals("840539006") ? "COVID-19" : null);
            certResponse.setIdentifier(gp.getT().get(0).getCi());
            certResponse.setIssuer(gp.getT().get(0).getIs());
            certResponse.setProductName(gp.getT().get(0).getTt().equals("LP6464-4") ? "Nucleic acid amplification with probe detection" : "Rapid immunoassay");
            String vendor = gp.getT().get(0).getNm() !=null ? gp.getT().get(0).getNm() : gp.getT().get(0).getMa();
            certResponse.setVendorName(certUtil.getTestDevices().getValueSetValues().get(vendor).getDisplay());

            certResponse.setType("TEST");
        } else {
            return null;
        }
        if (certResponse.getType()!=null) {
            return certResponse;
        } else {
            return null;
        }
    }

}
