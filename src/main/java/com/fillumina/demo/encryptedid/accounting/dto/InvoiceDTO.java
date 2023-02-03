package com.fillumina.demo.encryptedid.accounting.dto;

import java.util.List;

/**
 * Mimics the content exported by shop API with the call registerNewInvoice(ShoppingCart)
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class InvoiceDTO {

    /**
     * Contains the encrypted long of
     * {@link com.fillumina.demo.encryptedid.shop.domain.ShoppingCart#id} as string.
     */
    private String id;

    private CustomerDTO webUser;
    private List<ItemDTO> items;

    public InvoiceDTO id(final String value) {
        this.id = value;
        return this;
    }

    public InvoiceDTO webUser(final CustomerDTO value) {
        this.webUser = value;
        return this;
    }

    public InvoiceDTO items(final List<ItemDTO> value) {
        this.items = value;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomerDTO getWebUser() {
        return webUser;
    }

    public void setWebUser(CustomerDTO webUser) {
        this.webUser = webUser;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

}
