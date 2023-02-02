package com.fillumina.demo.encryptedid.service;

import com.fillumina.keyencryptor.EncryptorsHolder;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@SpringBootTest
public class EncryptionServiceTest {

    @Test
    public void shouldEncryptorsHolderBeInitialized() {
        assertThat(EncryptorsHolder.isInitialized()).isTrue();
    }

    @Test
    public void testGetLongEncryptor() {
        long plain = 12345L;

        String encrypted = EncryptorsHolder.encryptLong(plain);
        long decrypted = EncryptorsHolder.decryptLong(encrypted);

        assertThat(decrypted).isEqualTo(plain);
    }

    @Test
    public void testGetUuidEncryptor() {
        UUID plain = UUID.randomUUID();

        String encrypted = EncryptorsHolder.encryptUuid(plain);
        UUID decrypted = EncryptorsHolder.decryptUuid(encrypted);

        assertThat(decrypted).isEqualTo(plain);
    }

}
