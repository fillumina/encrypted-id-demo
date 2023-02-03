package com.fillumina.demo.encryptedid.shop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fillumina.demo.encryptedid.shop.client.AccountingClient;
import com.fillumina.demo.encryptedid.shop.domain.Item;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.repository.ItemRepository;
import com.fillumina.demo.encryptedid.shop.repository.ProductRepository;
import com.fillumina.demo.encryptedid.shop.repository.ShoppingCartRepository;
import com.fillumina.demo.encryptedid.shop.repository.WebUserRepository;
import com.fillumina.keyencryptor.EncryptorsHolder;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ShopResourceTest {
    private static final String LOGIN = "pippo";
    private static final BigDecimal PRICE = new BigDecimal("33.4");
    private static final String SKU = "TSHIRT";

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountingClient accountingClient;

    @Test
    public void testCreateWebUser() throws Exception {
        mockMvc.perform(post("/shop/users/" + LOGIN))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testGetWebUser() throws Exception {
        WebUser user = new WebUser(LOGIN);
        webUserRepository.save(user);
        UUID id = user.getId();

        String encryptedUserId = EncryptorsHolder.encryptUuid(id);

        mockMvc.perform(get("/shop/users/" + encryptedUserId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(user)));
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product productDTO = new Product(SKU, PRICE);

        mockMvc.perform(post("/shop/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OM.writeValueAsBytes(productDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProduct() throws Exception {
        Product product = new Product(SKU, PRICE);
        productRepository.save(product);

        mockMvc.perform(get("/shop/products/" + SKU))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(product)));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product1 = productRepository.save(new Product("SKU1", new BigDecimal(1)));
        Product product2 = productRepository.save(new Product("SKU2", new BigDecimal(2)));

        List<Product> products = List.of(product1, product2);

        mockMvc.perform(get("/shop/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(products)));
    }

    @Test
    public void testAddItemToShoppingCart() throws Exception {
        final int quantity = 7;
        WebUser user = webUserRepository.save(new WebUser(LOGIN));
        Long productId = productRepository.save(new Product(SKU, new BigDecimal(1))).getId();

        String userId = EncryptorsHolder.encryptUuid(user.getId());

        mockMvc.perform(post("/shop/items/" + userId + "/" + SKU + "/" + quantity))
                .andDo(print())
                .andExpect(status().isOk());

        Product refreshedProduct = productRepository.getReferenceById(productId);
        Item item = refreshedProduct.getItems().get(0);

        assertThat(item.getShoppingCart().getWebUser().getId()).isEqualTo(user.getId());
        assertThat(item.getProduct().getSku()).isEqualTo(SKU);
        assertThat(item.getQuantity()).isEqualTo(quantity);
    }

    @Test
    public void testGetShoppingCart() throws Exception {
        WebUser webUser = webUserRepository.save(new WebUser(LOGIN));
        ShoppingCart shoppingCart = shoppingCartRepository.save(new ShoppingCart(webUser));

        String webUserId = EncryptorsHolder.encryptUuid(webUser.getId());

        mockMvc.perform(get("/shop/shopping-carts/" + webUserId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(shoppingCart)));
    }

    @Test
    public void testPurchaseShoppingCart() throws Exception {
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
        String shoppingCartId = EncryptorsHolder.encryptLong(
                ShoppingCart.ENCRYPTABLE_FIELD_ID, shoppingCart.getId());

        mockMvc.perform(post("/shop/buy-shopping-cart/" + shoppingCartId))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(shoppingCart.isSold()).isTrue();
    }

}
