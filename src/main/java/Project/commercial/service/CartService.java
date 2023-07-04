package Project.commercial.service;

import Project.commercial.Dto.CartAddRequestDto;
import Project.commercial.Dto.CartAddResponseDto;
import Project.commercial.Dto.CartItemDto;
import Project.commercial.domain.Cart;
import Project.commercial.domain.CartItem;
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



@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public Cart addItem(CartAddRequestDto cartAddRequestDto, Authentication authentication){
        log.info("as = {}",memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new RuntimeException("akakakakak")
        ));
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow();
        Long member_id = member.getId();


        if(cartRepository.findByMember_id(member_id).isEmpty()){
            Cart cart = Cart.builder()
                    .member(member)
                    .build();

            cartRepository.save(cart);
        }
        Cart cart = cartRepository.findByMember_id(member_id).orElseThrow();


        CartItemDto cartItemDto = CartItemDto.builder()
                .cart(cart)
                .item(itemRepository.findById(cartAddRequestDto.getItem_id()).orElseThrow())
                .quantity(cartAddRequestDto.getQuantity())
                .build();

        CartItem cartItemEntity = cartItemDto.toEntity(cartItemDto);
        cartItemRepository.save(cartItemEntity);



        return cart;
    }
}
