package br.social.impacthub.service;

import br.social.impacthub.exception.InvalidAccessToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTService implements TokenService{
    @Value("${token.issuer}")
    private String issuer;

    @Value("${token.secret}")
    private String secret;

    @Override
    public String generateToken(String username){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(username)
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error generating JWT Token");
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC); // -03:00 ?
    }

    @Override
    public String validateTokenAndRetrieveUsername(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();

            String tokenLogin = verifier.verify(token).getSubject();
            return tokenLogin;

        } catch (JWTVerificationException exception){
//            return null;
            throw new InvalidAccessToken("Invalid token");
        }
    }


}
