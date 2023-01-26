package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

}
