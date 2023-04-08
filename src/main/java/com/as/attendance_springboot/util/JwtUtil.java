package com.as.attendance_springboot.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.as.attendance_springboot.model.PayloadDto;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * @author xulili
 */
public class JwtUtil {
    private static final String DEFAULT_SECRET = "moblieAttendace";

    public static PayloadDto generatePayloadDto(String userid, String username, String right) {
        Date now = new Date();
        Date exp = DateUtil.offsetSecond(now, 5*60 * 60);
        PayloadDto payloadDto = PayloadDto.builder()
                .iat(now.getTime())
                .exp(exp.getTime())
                .jti(UUID.randomUUID().toString())
                .userId(userid)
                .username(username)
                .right(right)
                .build();
        return payloadDto;
    }

    public static PayloadDto getByToken(String token) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        String payload = jwsObject.getPayload().toString();
        return JSONUtil.toBean(payload, PayloadDto.class);
    }

    public static String generateTokenByHmac(PayloadDto payloadDto) throws JOSEException {
        return generateTokenByHmac(payloadDto, DEFAULT_SECRET);
    }

    public static PayloadDto verifyTokenByHmac(String token) throws ParseException, JOSEException {
        return verifyTokenByHmac(token, DEFAULT_SECRET);
    }

    /**
     * 使用 HMAC SHA-256
     * @param payloadDto 有效载荷
     * @param secret     密钥
     * @return JWS字符串
     * @throws JOSEException
     */
    public static String generateTokenByHmac(PayloadDto payloadDto, String secret) throws JOSEException {
        String payloadStr = JSONUtil.toJsonStr(payloadDto);
        secret = SecureUtil.md5(secret);
        JWSHeader jwsheader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
        Payload payload = new Payload(payloadStr);
        JWSObject jwsObject = new JWSObject(jwsheader, payload);
        JWSSigner jwsSigner = new MACSigner(secret);
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    /**
     * 验证token,提取有效载荷
     * @param token  JWT
     * @param secret 未经加密密钥
     * @return PayloadDto对象
     * @throws ParseException
     * @throws JOSEException
     */
    public static PayloadDto verifyTokenByHmac(String token, String secret) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(token);
        secret = SecureUtil.md5(secret);
        JWSVerifier jwsVerifier = new MACVerifier(secret);
        if (!jwsObject.verify(jwsVerifier)) {
            throw new JOSEException("token签名不合法");
        }
        String payload = jwsObject.getPayload().toString();
        PayloadDto payloadDto = JSONUtil.toBean(payload, PayloadDto.class);
        if (payloadDto.getExp() < System.currentTimeMillis()) {
            throw new JOSEException("token已过期");
        }
        return payloadDto;
    }

}
