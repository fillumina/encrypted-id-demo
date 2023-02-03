package com.fillumina.demo.encryptedid.shop.web;

import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.service.ShopService;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@RestController
public class ShopResource {

    private final ShopService shopService;

    public ShopResource(ShopService shopService) {
        this.shopService = shopService;
    }

    /**
     * Create a new web user and return an encrypted id (UUID actually).
     * <p>Public API.
     * @param login the login name of the new user
     * @return an encrypted id to prevent sequential guessing attack
     */
    @PostMapping("/shop/users/{login}")
    public String createWebUser(@PathVariable String login) {
        final UUID uuid = shopService.createWebUser(login);
        return EncryptorsHolder.encryptUuid(uuid);
    }

    /**
     * Retrieve a web user from its encrypted id. The returned user will have encrypted id
     * as identifier.
     * <p>Public API.
     * @param id
     * @return
     */
    @GetMapping("/shop/users/{id}")
    public WebUser getWebUser(@PathVariable String id) {
        UUID decryptedUserId = EncryptorsHolder.decryptUuid(id);
        WebUser webUser = shopService.getWebUser(decryptedUserId);
        return webUser;
    }

    @PostMapping("/shop/products")
    public Long createProduct(@RequestBody Product product) {
        return shopService.createProduct(product.getSku(), product.getPrice());
    }

    @GetMapping("/shop/products/{sku}")
    public Product getProduct(@PathVariable String sku) {
        Product product = shopService.getProduct(sku);
        return product;
    }

    @GetMapping("/shop/products")
    public List<Product> getAllProducts() {
        List<Product> products = shopService.getAllProducts();
        return products;
    }

    @PostMapping("/shop/items/{userId}/{sku}/{quantity}")
    public void addItemToShoppingCart(
            @PathVariable String userId,
            @PathVariable String sku,
            @PathVariable int quantity) {
        UUID id = EncryptorsHolder.decryptUuid(userId);
        shopService.addItemToShoppingCart(id, sku, quantity);
    }

    @GetMapping("/shop/shopping-carts/{userId}")
    public ShoppingCart getShoppingCart(@PathVariable String userId) {
        UUID id = EncryptorsHolder.decryptUuid(userId);
        ShoppingCart shoppingCart = shopService.getShoppingCart(id);
        return shoppingCart;
    }

    @PostMapping("/shop/buy-shopping-cart/{shoppingCartId}")
    public void purchaseShoppingCart(@PathVariable String shoppingCartId) {
        Long id = EncryptorsHolder.decryptLong(ShoppingCart.ENCRYPTABLE_FIELD_ID, shoppingCartId);
        shopService.purchaseShoppingCart(id);
    }
}
