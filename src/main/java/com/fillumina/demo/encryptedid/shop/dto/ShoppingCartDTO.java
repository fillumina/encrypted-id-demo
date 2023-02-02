package com.fillumina.demo.encryptedid.shop.dto;

import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ShoppingCartDTO {

    private UUID userId;
    private List<ItemDTO> items;

    public ShoppingCartDTO() {
    }

    public ShoppingCartDTO(ShoppingCart shoppingCart, UUID userId) {
        this.userId = userId;
        if (shoppingCart.getItems() != null) {
            this.items = shoppingCart.getItems().stream()
                    .map(i -> new ItemDTO(i))
                    .toList();
        }
    }

    public ShoppingCartDTO userId(final UUID value) {
        this.userId = value;
        return this;
    }

    public ShoppingCartDTO items(final List<ItemDTO> value) {
        this.items = value;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ShoppingCartDTO{" + "userId=" + userId + ", items=" + items + '}';
    }

}
