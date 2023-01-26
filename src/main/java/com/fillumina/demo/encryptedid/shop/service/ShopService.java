package com.fillumina.demo.encryptedid.shop.service;

import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.shop.client.AccountingClient;
import com.fillumina.demo.encryptedid.shop.domain.Item;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.dto.ItemDTO;
import com.fillumina.demo.encryptedid.shop.dto.ShoppingCartDTO;
import com.fillumina.demo.encryptedid.shop.repository.ProductRepository;
import com.fillumina.demo.encryptedid.shop.repository.ShoppingCartRepository;
import com.fillumina.demo.encryptedid.shop.repository.WebUserRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Service
public class ShopService {

    private final WebUserRepository webUserRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final AccountingClient accountingClient;

    public ShopService(WebUserRepository webUserRepository, ProductRepository productRepository,
            ShoppingCartRepository shoppingCartRepository, AccountingClient accountingClient) {
        this.webUserRepository = webUserRepository;
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.accountingClient = accountingClient;
    }

    public UUID createWebUser(String login) {
        WebUser webUser = new WebUser(login);
        webUserRepository.save(webUser);
        final UUID id = webUser.getId();
        return id;
    }

    public WebUser getWebUser(UUID id) {
        WebUser user = webUserRepository.findById(id).orElseThrow();
        return user;
    }

    public Long createProduct(String sku, BigDecimal price) {
        Product product = new Product(sku, price);
        return productRepository.save(product).getId();
    }

    public Product getProduct(String sku) {
        return productRepository.findBySku(sku).orElseThrow();
    }

    public void buyShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        WebUser user = getWebUser(shoppingCartDTO.getUserId());
        ShoppingCart shoppingCart = new ShoppingCart(user);
        if (shoppingCartDTO.getItems() != null) {
            for (ItemDTO itemDTO: shoppingCartDTO.getItems()) {
                Product product = getProduct(itemDTO.getSku());
                new Item(shoppingCart, product, itemDTO.getQuantity());
            }
        }
        shoppingCartRepository.save(shoppingCart);

        InvoiceDTO invoice = new InvoiceDTO()
                .customerId(user.getId())
                .shoppingCartId(shoppingCart.getId())
                .total(shoppingCart.getTotalCost());
        accountingClient.registerNewInvoice(invoice);
    }

}