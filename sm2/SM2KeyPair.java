package com.sunxue.common.sm2;

import java.math.BigInteger;

//import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint;

/**
 * SM2密钥对Bean
 * @author Potato
 *
 */
public class SM2KeyPair {

	private final ECPoint publicKey;
	private final BigInteger privateKey;

	public SM2KeyPair(ECPoint publicKey, BigInteger privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public ECPoint getPublicKey() {
		return publicKey;
	}

	public BigInteger getPrivateKey() {
		return privateKey;
	}

}
