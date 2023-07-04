package Project.commercial.controller;

import Project.commercial.Dto.CartAddRequestDto;
import Project.commercial.Dto.CartAddResponseDto;
import Project.commercial.domain.Cart;
import Project.commercial.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart/add")
    @ResponseBody
    public Cart addCartItem(@RequestBody CartAddRequestDto cartAddRequestDto, Authentication authentication){

        log.info("cartAddRequest = {}", cartAddRequestDto);
        log.info("cartAddRequest = {}", cartAddRequestDto.getItem_id());
        log.info("cartAddRequest = {}", cartAddRequestDto.getQuantity());

        log.info("cartAddRequest = {}", authentication.getName());
        log.info("cartAddRequest = {}", authentication);

        return cartService.addItem(cartAddRequestDto, authentication);

    }
}
