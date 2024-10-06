package Project.commercial.service;

import Project.commercial.domain.Cart;
import Project.commercial.domain.CartItem;
import Project.commercial.domain.Item;
import Project.commercial.domain.Member;
import Project.commercial.dto.cart.*;
import Project.commercial.repository.CartItemRepository;
import Project.commercial.repository.CartRepository;
import Project.commercial.repository.ItemRepository;
import Project.commercial.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ResponseItemInCartAndOrderDto addOrUpdateItem(AddCartItemDto addCartItemDto, Authentication authentication) {

        Member member = getMember(authentication);
        Long memberId = member.getId();

        // 카트가 없을 시 카트 생성 또는 조회
        Cart cart = getOrCreateCart(memberId, member);

        // 카트에 상품이 이미 담겨있는지 확인
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), addCartItemDto.getItemId()).orElse(null);

        // 상품이 이미 담겨있다면 수량을 업데이트
        if (cartItem != null) {
            int newQuantity = addCartItemDto.getQuantity() + cartItem.getQuantity();
            return updateCartItem(cartItem, newQuantity);
        }

        // 상품이 카트에 없으면 새로운 아이템 추가
        CreateCartItemDto createCartItemDto = CreateCartItemDto.builder()
                .cart(cart)
                .item(itemRepository.findById(addCartItemDto.getItemId()).orElseThrow())
                .quantity(addCartItemDto.getQuantity())
                .build();

        CartItem cartItemEntity = createCartItemDto.toEntity(createCartItemDto);
        CartItem savedCartItem = cartItemRepository.save(cartItemEntity);
        Item item = savedCartItem.getItem();

        return ResponseItemInCartAndOrderDto.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .color(item.getColor())
                .size(item.getSize())
                .thumbnailImages(item.getThumbnailImage())
                .price(comma(item.getPrice()))
                .quantity(savedCartItem.getQuantity())
                .build();
    }

    // 카트가 없으면 생성하는 메서드
    private Cart getOrCreateCart(Long memberId, Member member) {
        return cartRepository.findByMemberId(memberId)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .member(member)
                            .build();
                    return cartRepository.save(cart);
                });
    }

    // 기존 카트 아이템의 수량을 업데이트하는 메서드
    private ResponseItemInCartAndOrderDto updateCartItem(CartItem existingCartItem, int additionalQuantity) {
        UpdateCartItemDto updateCartItemDto = UpdateCartItemDto.builder()
                .quantity(additionalQuantity)
                .build();

        existingCartItem.updateCartItem(updateCartItemDto);
        Item existItem = existingCartItem.getItem();

        return ResponseItemInCartAndOrderDto.builder()
                .itemId(existItem.getId())
                .itemName(existItem.getItemName())
                .color(existItem.getColor())
                .size(existItem.getSize())
                .price(comma(existItem.getPrice()))
                .thumbnailImages(existItem.getThumbnailImage())
                .quantity(existingCartItem.getQuantity())
                .build();
    }
    @Transactional
    public CartItemListDto updateCart(UpdateCartItemDto updateCartItemDto, Authentication authentication) {
        Member member = getMember(authentication);
        Cart memberCart = cartRepository.findByMemberId(member.getId()).orElseThrow(
                () -> new EntityNotFoundException("카트를 찾을 수 없습니다."));

        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(memberCart.getId(), updateCartItemDto.getCartItemId()).orElseThrow(
                () -> new EntityNotFoundException("카트에 해당 상품이 존재하지 않습니다."));

        cartItem.updateCartItem(updateCartItemDto);

        List<CartItem> cartItemList = memberCart.getCartItemList();
        List<ResponseItemInCartAndOrderDto> responseItemInCartAndOrderDtoList = mapToCartAndOrderItemDtoList(cartItemList);
        int totalPrice = calculateTotalPrice(cartItemList);

        return CartItemListDto.builder()
                .cartItemList(responseItemInCartAndOrderDtoList)
                .totalPrice(comma(totalPrice))
                .build();
    }
    @Transactional(readOnly = true)
    public CartItemListDto getMyCart(Authentication authentication) {
        Member member = getMember(authentication);
        Cart memberCart = cartRepository.findByMemberId(member.getId()).orElseThrow(
                () -> new EntityNotFoundException("카트에 담은 아이템이 존재하지 않습니다."));

        List<CartItem> cartItemList = memberCart.getCartItemList();
        List<ResponseItemInCartAndOrderDto> responseItemInCartAndOrderDtoList = mapToCartAndOrderItemDtoList(cartItemList);
        int totalPrice = calculateTotalPrice(cartItemList);

        return CartItemListDto.builder()
                .cartItemList(responseItemInCartAndOrderDtoList)
                .totalPrice(comma(totalPrice))
                .build();
    }

    @Transactional
    public void deleteCartItem(Long itemId, Authentication authentication) {
        Member member = getMember(authentication);
        Cart memberCart = cartRepository.findByMemberId(member.getId()).orElseThrow(
                () -> new EntityNotFoundException("카트를 찾을 수 없습니다."));

        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(memberCart.getId(), itemId).orElseThrow(
                () -> new EntityNotFoundException("해당 아이템은 존재하지 않습니다."));

        cartItemRepository.delete(cartItem);
    }



    private List<ResponseItemInCartAndOrderDto> mapToCartAndOrderItemDtoList(List<CartItem> cartItemList) {
        List<ResponseItemInCartAndOrderDto> responseItemInCartAndOrderDtoList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            Item item = cartItem.getItem();
            ResponseItemInCartAndOrderDto responseItemInCartAndOrderDto = ResponseItemInCartAndOrderDto.builder()
                    .itemId(item.getId())
                    .itemName(item.getItemName())
                    .color(item.getColor())
                    .size(item.getSize())
                    .price(comma(item.getPrice()))
                    .quantity(cartItem.getQuantity())
                    .thumbnailImages(item.getThumbnailImage())
                    .build();
            responseItemInCartAndOrderDtoList.add(responseItemInCartAndOrderDto);
        }
        return responseItemInCartAndOrderDtoList;
    }

    private int calculateTotalPrice(List<CartItem> cartItemList) {
        int totalPrice = 0;
        for (CartItem cartItem : cartItemList) {
            totalPrice += cartItem.getItem().getPrice() * cartItem.getQuantity();
        }
        return totalPrice;
    }


    public String comma(int value) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(value);
    }

    private Member getMember(Authentication authentication) {
        return memberRepository.findByEmail(authentication.getName()).orElseThrow();
    }
}







