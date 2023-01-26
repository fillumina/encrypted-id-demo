package com.fillumina.demo.encryptedid.shop.dto;

import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class WebUserDTO {
    private UUID id;

    private String login;

    private Set<ShoppingCartDTO> shoppingCarts;

    public WebUserDTO() {
    }

    public WebUserDTO(WebUser webUser) {
        this(webUser.getId(), webUser.getLogin(), webUser.getShoppingCarts());
    }

    public WebUserDTO(UUID id, String login, Set<ShoppingCart> shoppingCarts) {
        this.id = id;
        this.login = login;
        this.shoppingCarts = shoppingCarts.stream()
                .map(s -> new ShoppingCartDTO(s))
                .collect(Collectors.toSet());
    }

    public WebUserDTO id(final UUID value) {
        this.id = value;
        return this;
    }

    public WebUserDTO login(final String value) {
        this.login = value;
        return this;
    }

    public WebUserDTO shoppingCarts(final Set<ShoppingCartDTO> value) {
        this.shoppingCarts = value;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<ShoppingCartDTO> getShoppingCarts() {
        return shoppingCarts;
    }

    public void setShoppingCarts(Set<ShoppingCartDTO> shoppingCarts) {
        this.shoppingCarts = shoppingCarts;
    }

}
