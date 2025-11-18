package edu.univ.erp.auth;

import edu.univ.erp.domain.AuthClass;

public interface AuthenticationService {
    AuthClass login(String username, String password) throws Exception;
}