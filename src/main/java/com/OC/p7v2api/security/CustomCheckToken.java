package com.OC.p7v2api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Log4j2
@Component
public class CustomCheckToken {

    public CustomCheckToken() {
    }

    public String checkTokenAndRetrieveUsernameFromIt(String token) {
        log.info("in CustomCheckToken in checkTokenAndRetrieveUsernameFromIt with token {}", token);
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
        //verification de la validité du token
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        log.info("in CustomCheckToken in checkTokenAndRetrieveUsernameFromIt with username {}", username);
        Date expirationDate = decodedJWT.getExpiresAt();
        log.info("in CustomAuthentication in checkTokenAndRetrieveUsernameFromIt with date {}", expirationDate);

        /* log.error("in CustomAuthorizationFilter in CheckTokenAndRetrieveUserFromIt, an error has occured : the token is not valid");*/
        return username;

    }
}