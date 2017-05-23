package com.kelpiegang.tacoresume.ApplicationLayer.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.kelpiegang.tacoresume.ApplicationLayer.Error.AuthenticationError;
import com.kelpiegang.tacoresume.ModelLayer.User;
import java.io.UnsupportedEncodingException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtAuthentication {

    private String secretKey;

    public JwtAuthentication(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getJwtToken(User user) throws AuthenticationError {

        try {
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("_id", user.getObjectId().toString())
                    .sign(Algorithm.HMAC256(this.secretKey));
            return token;
        } catch (JWTCreationException | IllegalArgumentException | UnsupportedEncodingException exception) {
            throw new AuthenticationError(exception.getMessage());
        }
    }

    public String verifyJwtToken(String token) throws AuthenticationError {

        if (token != null) {
            token = token.replace("Bearer ", "");
            try {
                JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.secretKey))
                        .withIssuer("auth0")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);
                return this.getUserId(token);

            } catch (JWTVerificationException exception) {
                throw new AuthenticationError("Invalid signature/claims");
            } catch (IllegalArgumentException | UnsupportedEncodingException exception) {
                throw new AuthenticationError(exception.getMessage());
            }
        } else {
            throw new AuthenticationError("Token is null");
        }
    }

    private String getUserId(String jwtToken) throws AuthenticationError {

        try {
            JWT jwt = JWT.decode(jwtToken);
            return jwt.getClaim("_id").asString();
        } catch (JWTDecodeException exception) {
            throw new AuthenticationError(exception.getMessage());
        }
    }

}
