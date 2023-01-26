package com.fillumina.demo.encryptedid.service;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@SpringBootTest
public class EncryptionServiceTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void shouldEncryptionServiceBeNotNull() {
        assertThat(encryptionService).isNotNull();
    }

    @Test
    public void testGetLongEncryptor() {
        long plain = 12345L;
        long encrypted = encryptionService.getLongEncryptor().encrypt(plain);

        assertThat(encrypted).isNotEqualTo(plain);

        long decrypted = encryptionService.getLongEncryptor().decrypt(encrypted);

        assertThat(decrypted).isEqualTo(plain);
    }

    @Test
    public void testGetUuidEncryptor() {
        UUID plain = UUID.randomUUID();
        UUID encrypted = encryptionService.getUuidEncryptor().encrypt(plain);

        assertThat(encrypted).isNotEqualTo(plain);

        UUID decrypted = encryptionService.getUuidEncryptor().decrypt(encrypted);

        assertThat(decrypted).isEqualTo(plain);
    }

}
