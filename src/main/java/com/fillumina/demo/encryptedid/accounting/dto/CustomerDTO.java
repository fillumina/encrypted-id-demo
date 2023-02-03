package com.fillumina.demo.encryptedid.accounting.dto;

import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CustomerDTO {
    private UUID id;
    private String login;

    public CustomerDTO id(final UUID value) {
        this.id = value;
        return this;
    }

    public CustomerDTO login(final String value) {
        this.login = value;
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
}
