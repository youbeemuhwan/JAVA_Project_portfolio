package Project.commercial.controller;

import Project.commercial.dto.cart.AddCartItemDto;
import Project.commercial.dto.cart.ResponseItemInCartAndOrderDto;
import Project.commercial.dto.cart.CartItemListDto;
import Project.commercial.dto.cart.UpdateCartItemDto;
import Project.commercial.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("cart")
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("")
    @ResponseBody
    public ResponseItemInCartAndOrderDto addOrUpdateItem(@RequestBody AddCartItemDto addCartItemDto, Authentication authentication)
    {
        return cartService.addOrUpdateItem(addCartItemDto, authentication);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public CartItemListDto updateCart(@PathVariable() Long id, @RequestBody UpdateCartItemDto updateCartItemDto, Authentication authentication)
    {
        return cartService.updateCart(updateCartItemDto, authentication);
    }

    @GetMapping("/my")
    @ResponseBody
    public CartItemListDto getMyCart(Authentication authentication)
    {
        return cartService.getMyCart(authentication);
    }

    @DeleteMapping("/item/{id}")
    @ResponseBody
    public String deleteCartItem(@PathVariable Long id, Authentication authentication)
    {
        cartService.deleteCartItem(id, authentication);
        return "DELETE DONE";
    }


}
