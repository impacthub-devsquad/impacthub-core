package br.social.impacthub.service.security;

import br.social.impacthub.exception.InvalidAccessTokenException;
import br.social.impacthub.exception.InvalidRefreshTokenException;
import br.social.impacthub.exception.RefreshTokenNotFoundException;
import br.social.impacthub.model.TokenType;
import br.social.impacthub.model.dto.LoginResponse;
import br.social.impacthub.model.dto.security.DecodedRefreshToken;
import br.social.impacthub.model.dto.security.Token;
import br.social.impacthub.model.entity.RefreshToken;
import br.social.impacthub.infrastructure.persistence.RefreshTokenRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class JWTService implements TokenService{
    @Value("${token.issuer}")
    private String issuer;

    @Value("${token.secret}")
    private String secret;

    private RefreshTokenRepository refreshTokenRepository;

    public JWTService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public LoginResponse login(UUID userId){
        UUID newRefreshTokenId = UUID.randomUUID();

        Token accessToken = generateNewAccessToken(userId);
        Token refreshToken = generateNewRefreshToken(newRefreshTokenId, userId);

        /*RefreshToken newToken = */refreshTokenRepository.save(
                new RefreshToken(
                        newRefreshTokenId,
                        userId,
                        refreshToken.expiration(),
                        false
                )
        );

        return new LoginResponse(accessToken.rawToken(), refreshToken.rawToken());
    }

    @Override
    public LoginResponse refresh(String rawRefreshToken) {
        validateRefreshToken(rawRefreshToken);

        DecodedRefreshToken decodedRefreshToken = decodeRefreshToken(rawRefreshToken);
        UUID userId = decodedRefreshToken.userId();

        RefreshToken refreshToken = refreshTokenRepository.findById(decodedRefreshToken.tokenId())
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

        if (refreshToken.getRevoked())
            throw new InvalidRefreshTokenException("Refresh token is revoked");

        refreshToken.setRevoked(true);

        UUID newRefreshTokenId = UUID.randomUUID();

        Token newAccessToken = generateNewAccessToken(userId);
        Token newRefreshToken = generateNewRefreshToken(newRefreshTokenId, userId);

        refreshTokenRepository.save(
                new RefreshToken(
                        newRefreshTokenId,
                        userId,
                        newRefreshToken.expiration(),
                        false
                )
        );

        return new LoginResponse(
                newAccessToken.rawToken(),
                newRefreshToken.rawToken()
        );
    }

    private Token generateNewAccessToken(UUID userId){
        Instant expiration = getAccessTokenExpirationDate();
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String rawToken = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(userId.toString())
                    .withClaim("type", TokenType.ACCESS.getValue())
                    .withExpiresAt(expiration)
                    .sign(algorithm);
            return new Token(rawToken, expiration, userId.toString());
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error generating JWT Token");
        }
    }

    private Token generateNewRefreshToken(UUID newRefreshTokenId, UUID userId){
        Instant expiration = getRefreshTokenExpirationDate();

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String rawToken = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(userId.toString())
                    .withJWTId(newRefreshTokenId.toString())
                    .withClaim("type", TokenType.REFRESH.getValue())
                    .withClaim("userId", userId.toString())
                    .withExpiresAt(expiration)
                    .sign(algorithm);
            return new Token(rawToken, expiration, userId.toString());
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error generating JWT Token");
        }
    }

    private DecodedRefreshToken decodeRefreshToken(String refreshToken){
        try {
            DecodedJWT decodedJWT = JWT.decode(refreshToken);
            return new DecodedRefreshToken(
                    UUID.fromString(
                            decodedJWT.getId()
                    ),
                    UUID.fromString(
                            decodedJWT.getClaim("userId").asString()
                    ),
                    decodedJWT.getExpiresAtAsInstant()
            );
        } catch (Exception e){
            throw new InvalidRefreshTokenException("Invalid Refresh Token");
        }
    }

    private Instant getAccessTokenExpirationDate(){
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC);
    }

    private Instant getRefreshTokenExpirationDate(){
        return LocalDateTime.now().plusWeeks(1).toInstant(ZoneOffset.UTC);
    }

    @Override
    public void validateAccessToken(String accessToken){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withClaim("type", TokenType.ACCESS.getValue())
                    .build();
            verifier.verify(accessToken);
        }
        catch (TokenExpiredException exception){
            throw new InvalidAccessTokenException("Access Token expired");
        }
        catch (Exception exception){
            throw new InvalidAccessTokenException("Invalid access token");
        }
    }

    @Override
    public void validateRefreshToken(String refreshToken){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withClaim("type", TokenType.REFRESH.getValue())
                    .withClaimPresence("userId")
                    .build();
            verifier.verify(refreshToken);
        }
        catch (TokenExpiredException exception){
            throw new InvalidRefreshTokenException("Refresh token expired");
        }
        catch (Exception exception){
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
    }

    @Override
    public String getSubjectFromAccessToken(String accessToken){
        try {
            DecodedJWT decodedJWT = JWT.decode(accessToken);
            return decodedJWT.getSubject();
        } catch (Exception e){
            throw new InvalidRefreshTokenException("Invalid Refresh Token");
        }
    }
}
