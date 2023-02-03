package com.fillumina.demo.encryptedid.shop.client;

import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Implemented with a feign.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@FeignClient(value="accounting", url="${accounting.service.url}")
public interface AccountingClient {

    @PutMapping("/invoices")
    long registerNewInvoice(@RequestBody ShoppingCart invoice);

}
