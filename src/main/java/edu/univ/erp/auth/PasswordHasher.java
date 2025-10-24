package edu.univ.erp.auth;
import org.mindrot.jbcrypt.BCrypt;

public final class PasswordHasher {
    //using static because we don't want accidental modifications
    public static String hash(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(15));
    }
    public static boolean verifyHash(String raw, String hash){
        return BCrypt.checkpw(raw, hash);
    }

    private PasswordHasher() {}
}
