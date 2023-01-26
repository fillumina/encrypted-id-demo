package com.fillumina.demo.encryptedid.shop.web;

import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import com.fillumina.demo.encryptedid.shop.dto.ProductDTO;
import com.fillumina.demo.encryptedid.shop.dto.ShoppingCartDTO;
import com.fillumina.demo.encryptedid.shop.service.ShopService;
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
    public String createWebUser(@PathVariable String login) {
        return shopService.createWebUser(login);
    }

    @GetMapping("/shop/user/{id}")
    public WebUser getWebUser(@PathVariable String id) {
        return shopService.getWebUser(id);
    }

    @PostMapping("/shop/product")
    public Long createProduct(@RequestBody ProductDTO productDTO) {
        return shopService.createProduct(productDTO.getSku(), productDTO.getPrice());
    }

    @GetMapping("/shop/product/{sku}")
    public Product getProduct(@PathVariable String sku) {
        return shopService.getProduct(sku);
    }

    @PostMapping("/shop/buy")
    public void buyShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shopService.buyShoppingCart(shoppingCartDTO);
    }

}
