package com.fillumina.demo.encryptedid.service;

import com.fillumina.idencryptor.EncryptorsHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Service
public class EncryptionService {

    @Value("encryption.password")
    public void setPassword(String password) {
        EncryptorsHolder.initEncryptorsWithPassword(password);
    }
}
