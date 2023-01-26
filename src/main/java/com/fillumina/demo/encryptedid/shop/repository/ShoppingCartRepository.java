package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

}
