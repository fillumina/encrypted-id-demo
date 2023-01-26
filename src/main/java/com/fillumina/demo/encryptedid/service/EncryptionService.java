package com.fillumina.demo.encryptedid.service;

import com.fillumina.keyencryptor.LongEncryptor;
import com.fillumina.keyencryptor.UuidEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Service
public class EncryptionService {

    @Value("encryption.password")
    private String password;

    private LongEncryptor longEncryptor;
    private UuidEncryptor uuidEncryptor;

    public LongEncryptor getLongEncryptor() {
        if (longEncryptor == null && password != null) {
            longEncryptor = new LongEncryptor(password);
        }
        return longEncryptor;
    }

    public UuidEncryptor getUuidEncryptor() {
        if (uuidEncryptor == null && password != null) {
            uuidEncryptor = new UuidEncryptor(password);
        }
        return uuidEncryptor;
    }
}
