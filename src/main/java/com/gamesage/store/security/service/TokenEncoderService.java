package com.gamesage.store.security.service;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class TokenEncoderService {

    private final Cipher cipher;
    private final IvParameterSpec iv;
    private final SecretKey key;

    private TokenEncoderService(SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.iv = iv;
        this.key = key;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    public byte[] encryptToken(byte[] token) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(token);
    }
}
