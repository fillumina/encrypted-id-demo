package com.fillumina.demo.encryptedid.shop.service;

import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.dto.ItemDTO;
import com.fillumina.demo.encryptedid.shop.dto.ShoppingCartDTO;
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
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@SpringBootTest
@Transactional
public class ShopServiceTest {
    private static final String LOGIN = "pippo";
    private static final BigDecimal PRICE = new BigDecimal("33.4");
    private static final String SKU = "TSHIRT";

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    private AccountingClientMock accountingClientMock;
    private ShopService shopService;

    @BeforeEach
    public void initMock() {
        this.accountingClientMock = new AccountingClientMock();
        this.shopService = new ShopService(webUserRepository, productRepository,
                shoppingCartRepository, accountingClientMock);
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
    public void testBuyShoppingCart() {
        final String sku1 = "TSHIRT";
        final String sku2 = "PANTS";
        final BigDecimal price1 = new BigDecimal("3.45");
        final BigDecimal price2 = new BigDecimal("4.99");
        final int qty1 = 1;
        final int qty2 = 3;

        WebUser user = new WebUser(LOGIN);
        webUserRepository.save(user);

        Product p1 = new Product(sku1, price1);
        Product p2 = new Product(sku2, price2);
        productRepository.save(p1);
        productRepository.save(p2);


        ItemDTO item1 = new ItemDTO(sku1, qty1);
        ItemDTO item2 = new ItemDTO(sku2, qty2);

        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO()
                .userId(user.getId())
                .items(List.of(item1, item2));

        // call the accounting system and send the shopping cart
        shopService.buyShoppingCart(shoppingCartDTO);

        // this is the invoice created to be sent to accounting
        InvoiceDTO invoice = accountingClientMock.getInvoice();

        assertThat(invoice.getCustomerId()).isEqualTo(user.getId());
        assertThat(invoice.getTotal()).isEqualTo(
                (price1.multiply(BigDecimal.valueOf(qty1))).add(
                        price2.multiply(BigDecimal.valueOf(qty2))));
        assertThat(invoice.getShoppingCartId()).isNotNull();

        ShoppingCart shoppingCart = shoppingCartRepository.findById(invoice.getShoppingCartId())
                .orElseThrow();

        assertThat(shoppingCart.getWebUser()).isEqualTo(user);
        assertThat(shoppingCart.getItems())
                .hasSize(2)
                .anyMatch(i -> sku1.equals(i.getProduct().getSku()) && qty1 == i.getQuantity())
                .anyMatch(i -> sku2.equals(i.getProduct().getSku()) && qty2 == i.getQuantity());
    }

}
