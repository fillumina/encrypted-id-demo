package com.fillumina.demo.encryptedid.shop.service;

import com.fillumina.demo.encryptedid.shop.client.AccountingClient;
import com.fillumina.demo.encryptedid.shop.domain.Item;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.repository.ItemRepository;
import com.fillumina.demo.encryptedid.shop.repository.ProductRepository;
import com.fillumina.demo.encryptedid.shop.repository.ShoppingCartRepository;
import com.fillumina.demo.encryptedid.shop.repository.WebUserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * The service layer doesn't have to care about hot the actual identifiers are transmitted
 * outside so it expects them as they are defined.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Service
public class ShopService {

    private final WebUserRepository webUserRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ItemRepository itemRepository;
    private final AccountingClient accountingClient;

    public ShopService(WebUserRepository webUserRepository, ProductRepository productRepository,
            ShoppingCartRepository shoppingCartRepository, ItemRepository itemRepository,
            AccountingClient accountingClient) {
        this.webUserRepository = webUserRepository;
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.itemRepository = itemRepository;
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addItemToShoppingCart(UUID userId, String sku, int quantity) {
        Product product = productRepository.findBySku(sku).orElseThrow();
        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        Item item = new Item(shoppingCart, product, quantity);
        itemRepository.save(item);
    }

    private ShoppingCart getOrCreateShoppingCart(UUID userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findLastNotPurchasedByUser(userId)
                .orElseGet(() -> {
                    WebUser user = webUserRepository.getReferenceById(userId);
                    return shoppingCartRepository.save(new ShoppingCart(user));
                });
        return shoppingCart;
    }

    public ShoppingCart getShoppingCart(UUID userId) {
        return shoppingCartRepository.findLastNotPurchasedByUser(userId).orElseThrow();
    }

    public long purchaseShoppingCart(Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.getReferenceById(shoppingCartId);
        if (shoppingCart.getItems() == null || shoppingCart.getItems().isEmpty()) {
            throw new IllegalStateException("shopping cart is empty");
        }
        shoppingCart.setSold(true);
        long purchaseId = accountingClient.registerNewInvoice(shoppingCart);
        return purchaseId;
    }

}