package com.eric.ecommerce.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	private SecretKey getSigningKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secret);

		return Keys.hmacShaKeyFor(keyBytes);

	}

	public String generateToken(String email) {

		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(getSigningKey()).compact();

	}

	public String extractEmail(String token) {

		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();

	}

	public boolean isTokenValid(String token) {

		try {

			Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);

			return true;

		} catch (JwtException ex) {

			return false;

		}

	}

}
