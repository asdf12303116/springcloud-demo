package com.chen.userauth.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import com.chen.common.service.RedisService;
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

    @Resource
    private RedisService redisService;

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
                .jwtID(UUID.fastUUID().toString())
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

    public JWTClaimsSet getTokenInfo(String token) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        return JWTClaimsSet.parse(jwsObject.getParsedString());
    }

    public String[] getUserInfo(String tokenStr) throws ParseException {
        JWTClaimsSet jwtClaimsSet = getTokenInfo(tokenStr);
        String username = jwtClaimsSet.getStringClaim("username");
        String userId = jwtClaimsSet.getStringClaim("userId");
        String userType = jwtClaimsSet.getStringClaim("userType");
        return new String[]{username, userId, userType, tokenStr};
    }


    public String login(LoginUserDto loginUserDto) throws JOSEException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        AuthUser authUser = (AuthUser) authentication.getPrincipal();


        return generateToken(authUser);
    }

    public void logout(String token) throws ParseException {
        JWTClaimsSet jwtClaimsSet = getTokenInfo(token);
        redisService.put("BLACK_LIST", jwtClaimsSet.getJWTID(), true);
    }

    public Boolean verifyToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getParsedString());
            boolean status = redisService.exists("BLACK_LIST", claimsSet.getJWTID());
            if (status) {
                status = jwsObject.verify(jwsVerifier);
            }
            if (status) {
                DateTime issueTime = DateTime.of(claimsSet.getIssueTime());
                DateTime expirationTime = DateTime.of(claimsSet.getExpirationTime());
                status = issueTime.after(expirationTime);
            }
            return status;
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return false;
        }

    }

}



