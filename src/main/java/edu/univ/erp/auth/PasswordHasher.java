package edu.univ.erp.auth;
import org.mindrot.jbcrypt.BCrypt;

public final class PasswordHasher {
    //using static because we don't want accidental modifications
    public static String hash(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(10));
    }
    public static boolean verifyHash(String raw, String hash){
        if (hash == null || hash.isBlank()) {
            return false; //doing a safety check
        }
        try {
            return BCrypt.checkpw(raw, hash);
        } catch (Exception e) {
            return false;
        }
    }

    private PasswordHasher() {}
}
