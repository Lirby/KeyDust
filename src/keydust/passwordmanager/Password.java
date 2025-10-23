package keydust.passwordmanager;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Password {

    private char[] password;
    private byte[] salt;
    private byte[] hash;

    public Password(String password) {
        this.password = password.toCharArray();

        SecureRandom random = new SecureRandom();
        salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(this.password, salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public String getHash() {
        return Base64.getEncoder().encodeToString(hash);
    }

    public String getSalt() {
        return Base64.getEncoder().encodeToString(salt);
    }


}
