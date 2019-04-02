package com.pixshare.utils;

import java.io.UnsupportedEncodingException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtToken {
	
	private static String tokenSecret="iwskjieifjwior";
	
	public static String generateToken(long ID)
	{
		try {
		Algorithm algorithm= Algorithm.HMAC256(tokenSecret);
				return JWT.create().withClaim("id", ID).sign(algorithm);
		}
		catch(UnsupportedEncodingException e)
		{
			log.error("Algorithm is Not Supported",e);
		}
		return null;
	}
	
	public static Long verifyToken(String token)
	{
		try {
		Verification verification=JWT.require(Algorithm.HMAC256(tokenSecret));
		JWTVerifier jwtverifier=verification.build();
		DecodedJWT decodedjwt=jwtverifier.verify(token);
		Claim claim=decodedjwt.getClaim("id");
		return claim.asLong();
		}
		catch(UnsupportedEncodingException e)
		{
			log.error("Unsupport Encoding",e);
		}
		return null;
	}

}
