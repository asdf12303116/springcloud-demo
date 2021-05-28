package com.chen.userauth.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.chen.userauth.entity.AuthUser;
import com.chen.userauth.entity.dto.LoginUserDto;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;

/**
 * @author chen
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expiration;

    @Resource
    private AuthenticationManager authenticationManager;

    private volatile JWSSigner jwsSigner;
    private volatile JWSVerifier jwsVerifier;


    public JwtService() throws JOSEException {
        setJwsKey();
    }

    private final JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).
            type(JOSEObjectType.JWT)
            .build();


    private void setJwsKey() throws JOSEException {
        if (Objects.isNull(jwsSigner)) {
            jwsSigner = new MACSigner(SecureUtil.sha1(secret));
        }
        if (Objects.isNull(jwsVerifier)) {
            jwsVerifier = new MACVerifier(SecureUtil.sha1(secret));
        }
    }

    public String generateToken(AuthUser user) throws JOSEException {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("default")
                .issueTime(DateUtil.date())
                .expirationTime(DateUtil.date().offset(DateField.SECOND, expiration))
                .claim("username", user.getUsername())
                .claim("userId", user.getId().toString())
                .claim("userType", Objects.nonNull(user.getUserType()) ? user.getUserType() : "user")
                .build();

        JWSObject jwsObject = new JWSObject(jwsHeader, claimsSet.toPayload());
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    public Boolean tokenValid(String tokenStr) throws JOSEException, ParseException {
        JWSObject jwsObject = JWSObject.parse(tokenStr);
        return jwsObject.verify(jwsVerifier);
    }

    public String[] getUserInfo(String tokenStr) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(tokenStr);
        Payload payload = jwsObject.getPayload();
        Map<String, Object> jsonMap = payload.toJSONObject();
        String username = (String) jsonMap.get("username");
        String userId = (String) jsonMap.get("userId");
        String userType = (String) jsonMap.get("userType");
        return new String[]{username, userId, userType, tokenStr};
    }


    public String login(LoginUserDto loginUserDto) throws JOSEException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        AuthUser authUser = (AuthUser) authentication.getPrincipal();


        return generateToken(authUser);
    }

}



