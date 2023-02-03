package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.webUser.id = :userId AND sc.sold = false")
    Optional<ShoppingCart> findLastNotPurchasedByUser(UUID userId);
}
