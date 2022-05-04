package com.OC.p7v2api.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Log4j2
@Component
public class TokenUtil {


    public String createToken(User user) {
        log.info("in CustomAuthenticationFilter in createToken");
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 13*24*60*60*1000)) // 13 jours
                .sign(algorithm);
        log.info("in CustomAuthentication in createToken with token {}", access_token);
        return access_token;
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