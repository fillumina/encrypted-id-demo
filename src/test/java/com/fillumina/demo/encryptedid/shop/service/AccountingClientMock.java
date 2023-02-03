package com.fillumina.demo.encryptedid.shop.service;

import com.fillumina.demo.encryptedid.shop.client.AccountingClient;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class AccountingClientMock implements AccountingClient {

    private ShoppingCart invoice;
    private long response = 123456L;

    @Override
    public long registerNewInvoice(ShoppingCart shoppingCart) {
        this.invoice = shoppingCart;
        return response;
    }

    public ShoppingCart getInvoice() {
        return invoice;
    }

    public long getResponse() {
        return response;
    }
}
