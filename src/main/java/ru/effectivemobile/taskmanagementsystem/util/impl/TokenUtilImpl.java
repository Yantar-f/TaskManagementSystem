package ru.effectivemobile.taskmanagementsystem.util.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;
import ru.effectivemobile.taskmanagementsystem.config.AccessTokenConfig;
import ru.effectivemobile.taskmanagementsystem.config.TokenConfig;
import ru.effectivemobile.taskmanagementsystem.exception.InvalidTokenException;
import ru.effectivemobile.taskmanagementsystem.model.TokenBody;
import ru.effectivemobile.taskmanagementsystem.util.TokenUtil;

import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.io.Decoders.BASE64;

@Component
public class TokenUtilImpl implements TokenUtil {
    private final AccessTokenConfig accessTokenConfig;
    private final JwtParser accessTokenParser;

    public TokenUtilImpl(AccessTokenConfig accessTokenConfig) {

        this.accessTokenConfig = accessTokenConfig;
        accessTokenParser = buildParser(accessTokenConfig);
    }

    @Override
    public String generateAccessToken(TokenBody tokenBody) {
        return generateToken(tokenBody, accessTokenConfig);
    }

    @Override
    public TokenBody parseAccessToken(String token) {
        return parseTokenWith(accessTokenParser, token);
    }

    private TokenBody parseTokenWith(JwtParser parser, String token) {
        try {
            Claims claimsMap = extractClaimsMapWith(parser, token);
            String subjectID = extractSubjectIDFrom(claimsMap);

            return new TokenBody(Long.valueOf(subjectID));
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 UnsupportedJwtException |
                 IllegalArgumentException |
                 ClassCastException exception) {
            throw new InvalidTokenException(token);
        }
    }

    private String generateToken(TokenBody body, TokenConfig config) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(BASE64.decode(config.getSecretKey())), HS256)
                .setIssuer(config.getIssuer())
                .setExpiration(Date.from(Instant.now().plusMillis(config.getExpirationMs())))
                .setSubject(body.getSubjectId().toString())
                .compact();
    }

    private String extractSubjectIDFrom(Claims claimsMap) {
        return claimsMap.getSubject();
    }

    private Claims extractClaimsMapWith(JwtParser parser, String token) {
        return parser.parseClaimsJws(token).getBody();
    }

    private JwtParser buildParser(TokenConfig config) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(BASE64.decode(config.getSecretKey())))
                .requireIssuer(config.getIssuer())
                .setAllowedClockSkewSeconds(config.getAllowedClockSkewS())
                .build();
    }
}
