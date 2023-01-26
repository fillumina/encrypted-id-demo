package com.fillumina.demo.encryptedid.shop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fillumina.demo.encryptedid.shop.client.AccountingClient;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.dto.ItemDTO;
import com.fillumina.demo.encryptedid.shop.dto.ProductDTO;
import com.fillumina.demo.encryptedid.shop.dto.ShoppingCartDTO;
import com.fillumina.demo.encryptedid.shop.repository.ProductRepository;
import com.fillumina.demo.encryptedid.shop.repository.ShoppingCartRepository;
import com.fillumina.demo.encryptedid.shop.repository.WebUserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
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
    private MockMvc mockMvc;

    @MockBean
    private AccountingClient accountingClient;

    @Test
    public void testCreateWebUser() throws Exception {
        mockMvc.perform(post("/shop/user/" + LOGIN))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testGetWebUser() throws Exception {
        WebUser user = new WebUser(LOGIN);
        webUserRepository.save(user);
        UUID id = user.getId();

        mockMvc.perform(get("/shop/user/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(user)));
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO(SKU, PRICE);

        mockMvc.perform(post("/shop/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OM.writeValueAsBytes(productDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProduct() throws Exception {
        Product product = new Product(SKU, PRICE);
        productRepository.save(product);

        ProductDTO result = new ProductDTO(product);

        mockMvc.perform(get("/shop/product/" + SKU))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(result)));
    }

    @Test
    public void testBuyShoppingCart() throws Exception {
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

        when(accountingClient.registerNewInvoice(any()))
                .thenReturn(anyLong());

        mockMvc.perform(post("/shop/buy")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OM.writeValueAsBytes(shoppingCartDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
