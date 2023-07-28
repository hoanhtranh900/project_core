package com.osp.core.security;

import com.osp.core.dto.request.LoginRequest;
import com.osp.core.dto.response.JwtAuthDto;
import com.osp.core.entity.AdmUser;
import com.osp.core.utils.UtilsDate;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.List;

@Component
public class TokenHelper {

    private static final String USER_ID = "ui";
    private static final String USER_NAME = "uname";
    private static final String US_AGENT = "us-agent";
    private static final String IP = "ip";
    private static final String EXPIRATION = "exp";
    private static final String EMAIL_KEY = "mail";
    private static final String FULLNAME_KEY = "fullname";
    private static final String SESSION = "ss";

    @Value("${spring.application.name}")
    private String APP_NAME;
    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;
    @Value("${jwt.privateKeyFile}")
    private String privateKeyFile;
    @Value("${jwt.publicKeyFile}")
    private String publicKeyFile;
    private byte[] publicKey;
    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS256;

    @PostConstruct
    private void postConstruct() {
        this.publicKey = readPublicKey(publicKeyFile);
    }

    public String getUsernameFromToken(String token) {
        String username = "";
        try {
            final Claims claims = this.getClaimsFromToken(token);

            Date validTo = claims.getExpiration();
            Date requestTime = new Date();

            if (validTo.after(requestTime)) {
                username = claims.getSubject();
            }

        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String generateToken(LoginRequest loginRequest, AdmUser user, String session) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        String jws = Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(loginRequest.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(getPrivateSigningKey(), SIGNATURE_ALGORITHM)
                .claim(USER_ID, user.getId())
                .claim(USER_NAME, user.getUsername())
                .claim(EMAIL_KEY, user.getEmail())
                .claim(FULLNAME_KEY, user.getFullName())
                .claim(SESSION, session)
                .claim(EMAIL_KEY, user.getEmail())
                .claim(IP, loginRequest.getIp())
                .claim(US_AGENT, loginRequest.getUserAgent())
                .compact();
        return jws;
    }

    @SneakyThrows
    public JwtAuthDto getJWTInfor(String authToken) {
        Claims body = Jwts.parser().setSigningKey(getPublicSigningKey()).parseClaimsJws(authToken).getBody();
        return JwtAuthDto.builder()
                .issuer(body.getIssuer())
                .ui(body.get(USER_ID, Long.class))
                .expi(body.get(EXPIRATION, Date.class))
                .uname(body.get(USER_NAME, String.class))
                .ipAddress(body.get(IP, String.class))
                .usAgent(body.get(US_AGENT, String.class))
                .email(body.get(EMAIL_KEY, String.class))
                .fullname(body.get(FULLNAME_KEY, String.class))
                .ss(body.get(SESSION, String.class))
                .build();
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(getPublicSigningKey()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getPublicSigningKey()).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Key getPublicSigningKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey key = kf.generatePublic(new X509EncodedKeySpec(publicKey));
        return key;
    }

    public Key getPrivateSigningKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        InputStream is = new ClassPathResource(this.privateKeyFile, this.getClass().getClassLoader()).getInputStream();
        byte[] privateKey = IOUtils.toByteArray(is);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey key = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        return key;
    }

    public byte[] readPublicKey(String publicKeyFile) {
        try {
            InputStream is = new ClassPathResource(publicKeyFile, this.getClass().getClassLoader()).getInputStream();
            return publicKey = IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Date generateExpirationDate() {
        Date date = UtilsDate.sencondDate(new Date(), EXPIRES_IN);
        return date;
    }

}
