package com.fillumina.demo.encryptedid.shop.web;

import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.dto.ProductDTO;
import com.fillumina.demo.encryptedid.shop.dto.ShoppingCartDTO;
import com.fillumina.demo.encryptedid.shop.dto.WebUserDTO;
import com.fillumina.demo.encryptedid.shop.service.ShopService;
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

    @PostMapping("/shop/user/{login}")
    public UUID createWebUser(@PathVariable String login) {
        return shopService.createWebUser(login);
    }

    @GetMapping("/shop/user/{id}")
    public WebUserDTO getWebUser(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        WebUser webUser = shopService.getWebUser(uuid);
        return new WebUserDTO(webUser);
    }

    @PostMapping("/shop/product")
    public Long createProduct(@RequestBody ProductDTO productDTO) {
        return shopService.createProduct(productDTO.getSku(), productDTO.getPrice());
    }

    @GetMapping("/shop/product/{sku}")
    public ProductDTO getProduct(@PathVariable String sku) {
        Product product = shopService.getProduct(sku);
        return new ProductDTO(product);
    }

    @PostMapping("/shop/buy")
    public void buyShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shopService.buyShoppingCart(shoppingCartDTO);
    }

}
