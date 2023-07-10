package Project.commercial.controller;

import Project.commercial.Dto.*;
import Project.commercial.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart/add")
    @ResponseBody
    public CartAndOrderItemDto addCartItem(@RequestBody CartAddRequestDto cartAddRequestDto, Authentication authentication)
    {
        return cartService.addItem(cartAddRequestDto, authentication);
    }

    @PatchMapping("/cart/modified")
    @ResponseBody
    public CartItemListDto modified(@RequestBody CartItemModifiedRequestDto cartItemModifiedRequestDto, Authentication authentication)
    {
        return cartService.modified(cartItemModifiedRequestDto, authentication);
    }

    @GetMapping("/cart/my_cart")
    @ResponseBody
    public CartItemListDto myCartList(Authentication authentication)
    {
        return cartService.list(authentication);
    }

    @DeleteMapping("/cart/delete")
    @ResponseBody
    public String delete(@RequestBody Map<String, Long> item_id_map, Authentication authentication)
    {
        cartService.delete(item_id_map, authentication);
        return "DELETE DONE";
    }


}
