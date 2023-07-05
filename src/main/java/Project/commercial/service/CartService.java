package Project.commercial.service;

import Project.commercial.Dto.*;
import Project.commercial.domain.Cart;
import Project.commercial.domain.CartItem;
import Project.commercial.domain.Item;
import Project.commercial.domain.Member;
import Project.commercial.repository.CartItemRepository;
import Project.commercial.repository.CartRepository;
import Project.commercial.repository.ItemRepository;
import Project.commercial.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public CartItemDto addItem(CartAddRequestDto cartAddRequestDto, Authentication authentication) {

        Member member = getMember(authentication);
        Long member_id = member.getId();
        
        if (cartRepository.findByMember_id(member_id).isEmpty()) {
            Cart cart = Cart.builder()
                    .member(member)
                    .build();

            cartRepository.save(cart);
        }


        Cart cart = cartRepository.findByMember_id(member_id).orElseThrow();
        
        CartItemCreateRequestDto cartItemCreateRequestDto = CartItemCreateRequestDto.builder()
                .cart(cart)
                .item(itemRepository.findById(cartAddRequestDto.getItem_id()).orElseThrow())
                .quantity(cartAddRequestDto.getQuantity())
                .build();

        if (cartItemRepository.findByCartIdAndItemId(cart.getId(), cartItemCreateRequestDto.getItem().getId()).isPresent()) {
            CartItem existCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), cartItemCreateRequestDto.getItem().getId()).orElseThrow();

            CartItemModifiedRequestDto cartItemModifiedRequestDto = CartItemModifiedRequestDto.builder()
                    .quantity(cartAddRequestDto.getQuantity() + existCartItem.getQuantity())
                    .build();

            existCartItem.updateCartItem(cartItemModifiedRequestDto);
            cartItemRepository.flush();
            Item existItem = existCartItem.getItem();

            return CartItemDto.builder()
                    .id(existItem.getId())
                    .itemName(existItem.getItemName())
                    .color(existItem.getColor())
                    .size(existItem.getSize())
                    .price(comma(existItem.getPrice()))
                    .thumbnailImages(existItem.getThumbnailImage())
                    .quantity(existCartItem.getQuantity())
                    .build();
        }

        CartItem cartItemEntity = cartItemCreateRequestDto.toEntity(cartItemCreateRequestDto);

        CartItem savedCartItem = cartItemRepository.save(cartItemEntity);
        Item item = savedCartItem.getItem();
        
        return CartItemDto.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .color(item.getColor())
                .size(item.getSize())
                .thumbnailImages(item.getThumbnailImage())
                .price(comma(item.getPrice()))
                .quantity(savedCartItem.getQuantity())
                .build();

    }

    public CartItemListDto modified(CartItemModifiedRequestDto cartItemModifiedRequestDto, Authentication authentication){

        Member member = getMember(authentication);

        Cart memberCart = cartRepository.findByMember_id(member.getId()).orElseThrow(
                () -> new RuntimeException("잘못된 접근 입니다."));

        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(memberCart.getId(), cartItemModifiedRequestDto.getCart_item_id()).orElseThrow(
                () -> new RuntimeException("해당 상품은 카트에 담겨 있지 않습니다."));

        cartItem.updateCartItem(cartItemModifiedRequestDto);

        List<CartItem> cartItemList = memberCart.getCartItemList();
        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        int total_price = 0;

        for(CartItem forCartItem : cartItemList){
            Item item = forCartItem.getItem();
            CartItemDto cartItemDto = CartItemDto.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .color(item.getColor())
                    .size(item.getSize())
                    .price(comma(item.getPrice()))
                    .quantity(forCartItem.getQuantity())
                    .thumbnailImages(item.getThumbnailImage())
                    .build();
            
            cartItemDtoList.add(cartItemDto);
            total_price += item.getPrice() * forCartItem.getQuantity();
        }

        CartItemListDto cartItemListDto = CartItemListDto.builder()
                .CartItemList(cartItemDtoList)
                .total_price(comma(total_price))
                .build();

        return cartItemListDto;
    }
    
    public CartItemListDto list(Authentication authentication){
        Member member = getMember(authentication);

        Cart memberCart = cartRepository.findByMember_id(member.getId()).orElseThrow(
                () -> new RuntimeException("카트에 담은 아이템이 존재하지 않습니다."));

        List<CartItem> cartItemList = memberCart.getCartItemList();
        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        int total_price = 0;

        for(CartItem forCartItem : cartItemList){
            Item item = forCartItem.getItem();
            CartItemDto cartItemDto = CartItemDto.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .color(item.getColor())
                    .size(item.getSize())
                    .price(comma(item.getPrice()))
                    .quantity(forCartItem.getQuantity())
                    .thumbnailImages(item.getThumbnailImage())
                    .build();

            cartItemDtoList.add(cartItemDto);
            total_price += item.getPrice() * forCartItem.getQuantity();
        }

        CartItemListDto cartItemListDto = CartItemListDto.builder()
                .CartItemList(cartItemDtoList)
                .total_price(comma(total_price))
                .build();
        
        return cartItemListDto;
    }
    
    public void delete(Map<String, Long> item_id_map, Authentication authentication){
        Long item_id = item_id_map.get("item_id");
        Member member = getMember(authentication);
        Cart memberCart = cartRepository.findByMember_id(member.getId()).orElseThrow(
                () -> new RuntimeException("잘못된 접근 입니다."));

        log.info("member id = {}", memberCart.getId());

        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(memberCart.getId(), item_id).orElseThrow(
                () -> new RuntimeException("해당 아이템은 존재하지 않습니다."));

        cartItemRepository.delete(cartItem);
        cartItemRepository.flush();
    }

    public String comma(int value) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(value);
    }

    private Member getMember(Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow();
        return member;
    }
}







