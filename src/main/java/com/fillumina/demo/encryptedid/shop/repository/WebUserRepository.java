package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface WebUserRepository extends JpaRepository<WebUser, UUID> {

}
