package com.fillumina.demo.encryptedid.shop.service;

import com.fillumina.demo.encryptedid.shop.domain.Item;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.repository.ItemRepository;
import com.fillumina.demo.encryptedid.shop.repository.ProductRepository;
import com.fillumina.demo.encryptedid.shop.repository.ShoppingCartRepository;
import com.fillumina.demo.encryptedid.shop.repository.WebUserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@DataJpaTest
@Transactional
public class ShopServiceTest {
    private static final String LOGIN = "pippo";
    private static final BigDecimal PRICE = new BigDecimal("33.4");
    private static final String SKU = "TSHIRT";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ItemRepository itemRepository;

    private AccountingClientMock accountingClientMock;
    private ShopService shopService;

    @BeforeEach
    public void initMock() {
        this.accountingClientMock = new AccountingClientMock();
        this.shopService = new ShopService(webUserRepository, productRepository,
                shoppingCartRepository, itemRepository, accountingClientMock);
    }

    @Test
    public void testSaveWebUser() {
        UUID id = shopService.createWebUser(LOGIN);
        WebUser user = webUserRepository.findById(id).orElseThrow();

        assertThat(user.getLogin()).isEqualTo(LOGIN);
    }

    @Test
    public void testGetWebUser() {
        UUID uuid = shopService.createWebUser(LOGIN);
        WebUser user = shopService.getWebUser(uuid);

        assertThat(user.getLogin()).isEqualTo(LOGIN);
    }

    @Test
    public void testCreateProduct() {
        long id = shopService.createProduct(SKU, PRICE);

        Product found = productRepository.findById(id).orElseThrow();

        assertThat(found.getSku()).isEqualTo(SKU);
        assertThat(found.getPrice()).isEqualTo(PRICE);
    }

    @Test
    public void testGetProduct() {
        Product product = new Product(SKU, PRICE);
        productRepository.save(product);

        Product found = shopService.getProduct(SKU);

        assertThat(found).isEqualTo(product);
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product("SKU1", new BigDecimal(1));
        productRepository.save(product1);

        Product product2 = new Product("SKU2", new BigDecimal(2));
        productRepository.save(product2);

        List<Product> list = shopService.getAllProducts();

        assertThat(list)
                .hasSize(2)
                .containsExactly(product1, product2);
    }

    @Test
    public void testAddItemToShoppingCart() {
        UUID userId = shopService.createWebUser(LOGIN);
        Long productId = productRepository.save(new Product(SKU, new BigDecimal(1))).getId();

        shopService.addItemToShoppingCart(userId, SKU, 2);

        Product refreshedProduct = productRepository.getReferenceById(productId);
        Item item = refreshedProduct.getItems().get(0);

        assertThat(item.getShoppingCart().getWebUser().getId()).isEqualTo(userId);
        assertThat(item.getProduct().getSku()).isEqualTo(SKU);
        assertThat(item.getQuantity()).isEqualTo(2);
    }

    @Test
    public void testGetShoppingCart() {
        WebUser webUser = webUserRepository.save(new WebUser(LOGIN));
        ShoppingCart shoppingCart = shoppingCartRepository.save(new ShoppingCart(webUser));
        UUID userId = webUser.getId();

        ShoppingCart result = shopService.getShoppingCart(userId);

        assertThat(result).isEqualTo(shoppingCart);
    }

    @Test
    public void testPurchaseShoppingCart() {
        final String sku1 = "TSHIRT";
        final String sku2 = "PANTS";
        final BigDecimal price1 = new BigDecimal("3.45");
        final BigDecimal price2 = new BigDecimal("4.99");
        final int qty1 = 1;
        final int qty2 = 3;

        WebUser user = webUserRepository.save(new WebUser(LOGIN));

        Product product1 = productRepository.save(new Product(sku1, price1));
        Product product2 = productRepository.save(new Product(sku2, price2));

        ShoppingCart shoppingCart = shoppingCartRepository.save(new ShoppingCart(user));

        Item item1 = itemRepository.save(new Item(shoppingCart, product1, qty1));
        Item item2 = itemRepository.save(new Item(shoppingCart, product2, qty2));

        assertThat(shoppingCart.isSold()).isFalse();

        // call the accounting system and send the shopping cart
        long purchaseId = shopService.purchaseShoppingCart(shoppingCart.getId());

        assertThat(shoppingCart.isSold()).isTrue();
        assertThat(purchaseId).isEqualTo(accountingClientMock.getResponse());
        assertThat(shoppingCart).isEqualTo(accountingClientMock.getInvoice());

    }

}
