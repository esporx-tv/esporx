package tv.esporx.framework.security;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.slf4j.LoggerFactory.getLogger;

public class HashGenerator {

    private static final Logger LOGGER = getLogger(HashGenerator.class);

    public String generateHash() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(new DateTime().toString().getBytes("UTF-8"));
            StringBuilder builder = new StringBuilder();
            for(byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String msg = "Hash generation error - should not arrive here" ;
        LOGGER.error(msg);
        throw new RuntimeException(msg);
    }

}
