package com.sprint.mission.discodeit.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.exception.token.IllegalTokenException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private JWSSigner jwsSigner;
    private JWSVerifier jwsVerifier;

    @PostConstruct
    private void init() {

        try {
            byte[] secretBytes = jwtProperties.getSecret().getBytes();
            jwsSigner = new MACSigner(secretBytes);
            jwsVerifier = new MACVerifier(secretBytes);
        } catch (KeyLengthException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String generateAccessToken(String userName, String role) {
        return generateToken(userName, role, jwtProperties.getAccessKeyExpiration());
    }

    public String generateRefreshToken(String userName, String role) {
        return generateToken(userName, role, jwtProperties.getRefreshKeyExpiration());
    }

    public JWTClaimsSet parseToken(String token) {
        try {
            if (validateToken(token) == false) {
                return null;
            }

            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.verify(jwsVerifier) == false)
                return false;

            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            if (jwtClaimsSet == null || jwtClaimsSet.getExpirationTime().before(new Date()))
                return false;

            return true;
        } catch (ParseException e) {
            log.error(e.getMessage());
            return false;
        } catch (JOSEException e) {
            log.error(e.getMessage());
            return false;
        }
    }

//    public String refreshToken(String refreshToken) {
//        try
//        {
//            JWTClaimsSet set = parseToken(refreshToken);
//            if (set == null)
//                throw new IllegalTokenException();
//
//            return generateAccessToken(set.getSubject(), set.getClaimAsString("role"));
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private String generateToken(String userName, String role, Long expiration) {
        try {
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + expiration);

            JWTClaimsSet set = new JWTClaimsSet.Builder()
                    .issuer(jwtProperties.getIssuer())
                    .subject(userName)
                    .issueTime(now)
                    .expirationTime(expirationDate)
                    .claim("role", "ROLE_" + role)
                    .build();

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT signedJWT = new SignedJWT(header, set);
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
