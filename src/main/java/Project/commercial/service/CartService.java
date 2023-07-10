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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public CartAndOrderItemDto addItem(CartAddRequestDto cartAddRequestDto, Authentication authentication) {

        Member member = getMember(authentication);
        Long member_id = member.getId();
        // 카트가 없을시 카트 생성
        if (cartRepository.findByMember_id(member_id).isEmpty()) {
            Cart cart = Cart.builder()
                    .member(member)
                    .build();
            cartRepository.save(cart);
        }

        Cart findCart = cartRepository.findByMember_id(member_id).orElseThrow();

// 장바구니에 상품을 담을때 해당 상품이 이미 담겨있다면 담겨있는 아이템의 수량을 추가하는 로직
        if (cartItemRepository.findByCartIdAndItemId(findCart.getId(), cartAddRequestDto.getItem_id()).isPresent()) {
            CartItem existCartItem = cartItemRepository.findByCartIdAndItemId(findCart.getId(), cartAddRequestDto.getItem_id()).orElseThrow();

            CartItemModifiedRequestDto cartItemModifiedRequestDto = CartItemModifiedRequestDto.builder()
                    .quantity(cartAddRequestDto.getQuantity() + existCartItem.getQuantity())
                    .build();

            existCartItem.updateCartItem(cartItemModifiedRequestDto);
            cartItemRepository.flush();
            Item existItem = existCartItem.getItem();

            return CartAndOrderItemDto.builder()
                    .item_id(existItem.getId())
                    .itemName(existItem.getItemName())
                    .color(existItem.getColor())
                    .size(existItem.getSize())
                    .price(comma(existItem.getPrice()))
                    .thumbnailImages(existItem.getThumbnailImage())
                    .quantity(existCartItem.getQuantity())
                    .build();
        }
// 카트에 담으려는 상품이 카트 안에 중복으로 존재하지 않는다면 정상적으로 카트 아이템 생성하기
        CartItemCreateRequestDto cartItemCreateRequestDto = CartItemCreateRequestDto.builder()
                .cart(findCart)
                .item(itemRepository.findById(cartAddRequestDto.getItem_id()).orElseThrow())
                .quantity(cartAddRequestDto.getQuantity())
                .build();

        CartItem cartItemEntity = cartItemCreateRequestDto.toEntity(cartItemCreateRequestDto);

        CartItem savedCartItem = cartItemRepository.save(cartItemEntity);
        Item item = savedCartItem.getItem();
        
        return CartAndOrderItemDto.builder()
                .item_id(item.getId())
                .itemName(item.getItemName())
                .color(item.getColor())
                .size(item.getSize())
                .thumbnailImages(item.getThumbnailImage())
                .price(comma(item.getPrice()))
                .quantity(savedCartItem.getQuantity())
                .build();
    }
// 카트에 담겨져있는 상품 수량 변경 로직
    public CartItemListDto modified(CartItemModifiedRequestDto cartItemModifiedRequestDto, Authentication authentication){

        Member member = getMember(authentication);

        Cart memberCart = cartRepository.findByMember_id(member.getId()).orElseThrow(
                () -> new RuntimeException("잘못된 접근 입니다."));

        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(memberCart.getId(), cartItemModifiedRequestDto.getCart_item_id()).orElseThrow(
                () -> new RuntimeException("해당 상품은 카트에 담겨 있지 않습니다."));

        cartItem.updateCartItem(cartItemModifiedRequestDto);

        List<CartItem> cartItemList = memberCart.getCartItemList();
        List<CartAndOrderItemDto> cartAndOrderItemDtoList = new ArrayList<>();
        int total_price = 0;

        for(CartItem forCartItem : cartItemList){
            Item item = forCartItem.getItem();
            CartAndOrderItemDto cartAndOrderItemDto = CartAndOrderItemDto.builder()
                    .item_id(item.getId())
                    .itemName(item.getItemName())
                    .color(item.getColor())
                    .size(item.getSize())
                    .price(comma(item.getPrice()))
                    .quantity(forCartItem.getQuantity())
                    .thumbnailImages(item.getThumbnailImage())
                    .build();
            
            cartAndOrderItemDtoList.add(cartAndOrderItemDto);
            total_price += item.getPrice() * forCartItem.getQuantity();
        }

       return CartItemListDto.builder()
                .CartItemList(cartAndOrderItemDtoList)
                .total_price(comma(total_price))
                .build();
    }
    public CartItemListDto list(Authentication authentication){
        Member member = getMember(authentication);

        Cart memberCart = cartRepository.findByMember_id(member.getId()).orElseThrow(
                () -> new RuntimeException("카트에 담은 아이템이 존재하지 않습니다."));

        List<CartItem> cartItemList = memberCart.getCartItemList();
        List<CartAndOrderItemDto> cartAndOrderItemDtoList = new ArrayList<>();
        int total_price = 0;

        for(CartItem forCartItem : cartItemList){
            Item item = forCartItem.getItem();
            CartAndOrderItemDto cartAndOrderItemDto = CartAndOrderItemDto.builder()
                    .item_id(item.getId())
                    .itemName(item.getItemName())
                    .color(item.getColor())
                    .size(item.getSize())
                    .price(comma(item.getPrice()))
                    .quantity(forCartItem.getQuantity())
                    .thumbnailImages(item.getThumbnailImage())
                    .build();

            cartAndOrderItemDtoList.add(cartAndOrderItemDto);
            total_price += item.getPrice() * forCartItem.getQuantity();
        }

        return CartItemListDto.builder()
                    .CartItemList(cartAndOrderItemDtoList)
                    .total_price(comma(total_price))
                    .build();
    }
    public void delete(Map<String, Long> item_id_map, Authentication authentication){
        Long item_id = item_id_map.get("item_id");
        Member member = getMember(authentication);
        Cart memberCart = cartRepository.findByMember_id(member.getId()).orElseThrow(
                () -> new RuntimeException("잘못된 접근 입니다."));

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
        return memberRepository.findByEmail(authentication.getName()).orElseThrow();
    }
}







