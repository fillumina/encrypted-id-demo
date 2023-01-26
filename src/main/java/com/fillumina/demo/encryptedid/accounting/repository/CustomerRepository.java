package com.fillumina.demo.encryptedid.accounting.repository;

import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUserId(UUID userId);
}
