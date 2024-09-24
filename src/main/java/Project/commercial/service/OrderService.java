package Project.commercial.service;

import Project.commercial.domain.*;
import Project.commercial.dto.cart.CartAndOrderItemDto;
import Project.commercial.dto.order.*;
import Project.commercial.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    public OrderCreateResponseDto orderCreate(OrderCreateRequestDto orderCreateRequestDto, Authentication authentication) {
        Orders newOrder = buildOrder(orderCreateRequestDto, authentication);
        orderRepository.save(newOrder);

        handlePaymentMethod(newOrder, orderCreateRequestDto);

        OrderItem newOrderItem = buildOrderItem(newOrder, orderCreateRequestDto);
        orderItemRepository.save(newOrderItem);

        return buildOrderResponse(newOrder, newOrderItem);
    }

    private Orders buildOrder(OrderCreateRequestDto orderCreateRequestDto, Authentication authentication) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderCreateRequestDto.getPaymentMethod_id())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 결제 수단입니다."));


        Item item = itemRepository.findById(orderCreateRequestDto.getItem_id())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 아이템입니다."));


        int totalPrice = item.getPrice() * orderCreateRequestDto.getQuantity();

        return Orders.builder()
                .member(getMember(authentication))
                .orderNumber(getOrderNumber())
                .created_at(LocalDateTime.now())
                .address(orderCreateRequestDto.getAddress())
                .paymentMethod(paymentMethod)
                .totalPrice(totalPrice)
                .build();
    }
    private void handlePaymentMethod(Orders order, OrderCreateRequestDto orderCreateRequestDto) {
        PaymentMethod paymentMethod = order.getPaymentMethod();
        if (paymentMethod.getName().equals("포인트 결제")) {
            handlePointPayment(order);
        } else if (paymentMethod.getName().equals("무통장 입금")) {
            order.updateOrderStatus(orderStatusRepository.findById(1L).orElseThrow());
        }
    }

    private void handlePointPayment(Orders order) {
        Member member = memberRepository.findById(order.getMember().getId()).orElseThrow(() ->
                new IllegalArgumentException("유효하지 않은 회원입니다.")
        );
        int nowPoint = member.getPoint();
        int totalPrice = order.getTotalPrice();

        if (totalPrice > nowPoint) {
            throw new RuntimeException("포인트가 부족합니다.");
        }

        int remainingPoint = nowPoint - totalPrice;
        member.updateMemberPoint(remainingPoint);
        order.updateOrderStatus(orderStatusRepository.findById(2L).orElseThrow());
    }

    private OrderItem buildOrderItem(Orders order, OrderCreateRequestDto orderCreateRequestDto) {
        return OrderItem.builder()
                .orders(order)
                .item(itemRepository.findById(orderCreateRequestDto.getItem_id()).orElseThrow(() ->
                        new IllegalArgumentException("유효하지 않은 아이템입니다.")
                ))
                .quantity(orderCreateRequestDto.getQuantity())
                .build();
    }

    private OrderCreateResponseDto buildOrderResponse(Orders order, OrderItem orderItem) {
        CartAndOrderItemDto cartAndOrderItemDto = CartAndOrderItemDto.builder()
                .item_id(orderItem.getItem().getId())
                .itemName(orderItem.getItem().getItemName())
                .price(comma(orderItem.getItem().getPrice()))
                .color(orderItem.getItem().getColor())
                .size(orderItem.getItem().getSize())
                .thumbnailImages(orderItem.getItem().getThumbnailImage())
                .quantity(orderItem.getQuantity())
                .build();

        return OrderCreateResponseDto.builder()
                .order_id(order.getId())
                .order_number(order.getOrderNumber())
                .created_at(order.getCreated_at())
                .item(cartAndOrderItemDto)
                .address(order.getAddress())
                .total_price(comma(order.getTotalPrice()))
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .build();
    }

    public OrderInCartCreateResponseDto createOrderInCart(OrderInCartCreateRequestDto orderInCartCreateRequestDto, Authentication authentication) {
        Member member = getMember(authentication);

        // 주문 생성
        Orders newOrder = buildOrder(orderInCartCreateRequestDto, member);
        orderRepository.save(newOrder);

        // 카트 조회 및 비어있으면 예외 처리
        Cart cart = cartRepository.findByMember_id(member.getId())
                .orElseThrow(() -> new RuntimeException("해당 회원의 카트가 존재하지 않습니다."));
        List<CartItem> cartItemsByMember = cartItemRepository.findAllByCartId(cart.getId());

        if (cartItemsByMember.isEmpty()) {
            throw new RuntimeException("카트가 비어 있습니다.");
        }

        List<CartAndOrderItemDto> orderItemList = new ArrayList<>();
        int totalPrice = 0;

        for (CartItem cartItem : cartItemsByMember) {
            OrderItem orderItem = buildOrderItem(newOrder, cartItem);
            orderItemRepository.save(orderItem);

            totalPrice += cartItem.getItem().getPrice() * cartItem.getQuantity();
            orderItemList.add(buildCartAndOrderItemDto(cartItem));
        }

        newOrder.updateTotalPrice(totalPrice);
        processPayment(member, newOrder, totalPrice);

        // 카트 삭제
        cartRepository.deleteByMember_id(member.getId());

        return buildOrderResponse(newOrder, orderItemList, totalPrice);
    }

    private Orders buildOrder(OrderInCartCreateRequestDto requestDto, Member member) {
        return Orders.builder()
                .member(member)
                .orderNumber(getOrderNumber())
                .created_at(LocalDateTime.now())
                .address(requestDto.getAddress())
                .paymentMethod(paymentMethodRepository.findById(requestDto.getPaymentMethod_id())
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 결제 수단입니다.")))
                .build();
    }

    private OrderItem buildOrderItem(Orders order, CartItem cartItem) {
        return OrderItem.builder()
                .orders(order)
                .item(cartItem.getItem())
                .quantity(cartItem.getQuantity())
                .build();
    }

    private CartAndOrderItemDto buildCartAndOrderItemDto(CartItem cartItem) {
        return CartAndOrderItemDto.builder()
                .item_id(cartItem.getItem().getId())
                .itemName(cartItem.getItem().getItemName())
                .color(cartItem.getItem().getColor())
                .size(cartItem.getItem().getSize())
                .price(comma(cartItem.getItem().getPrice()))
                .quantity(cartItem.getQuantity())
                .thumbnailImages(cartItem.getItem().getThumbnailImage())
                .build();
    }

    private void processPayment(Member member, Orders order, int totalPrice) {
        if (order.getPaymentMethod().getName().equals("포인트 결제")) {
            if (member.getPoint() < totalPrice) {
                throw new RuntimeException("포인트가 부족합니다.");
            }
            int remainingPoint = member.getPoint() - totalPrice;
            member.updateMemberPoint(remainingPoint);
            order.updateOrderStatus(orderStatusRepository.findById(2L).orElseThrow());
        } else if (order.getPaymentMethod().getName().equals("무통장 입금")) {
            order.updateOrderStatus(orderStatusRepository.findById(1L).orElseThrow());
        }
    }

    private OrderInCartCreateResponseDto buildOrderResponse(Orders order, List<CartAndOrderItemDto> orderItemList, int totalPrice) {
        return OrderInCartCreateResponseDto.builder()
                .order_id(order.getId())
                .created_at(order.getCreated_at())
                .order_number(order.getOrderNumber())
                .item_list(orderItemList)
                .address(order.getAddress())
                .paymentMethod(order.getPaymentMethod())
                .orderStatus(order.getOrderStatus())
                .total_price(comma(totalPrice))
                .build();
    }
    public List<OrderListDto> orderList(Pageable pageable, Authentication authentication){
        Member member = getMember(authentication);
        Page<Orders> orderListByMember = orderRepository.findAllByMember_id(member.getId(), pageable);

        List<OrderListDto> orderListDtoList = new ArrayList<>();
        for (Orders order : orderListByMember.getContent()) {
            List<CartAndOrderItemDto> orderItemList =  new ArrayList<>();

            OrderListDto orderListDto = OrderListDto.builder()
                    .id(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .created_at(order.getCreated_at())
                    .orderStatus(order.getOrderStatus())
                    .paymentMethod(order.getPaymentMethod())
                    .totalPrice(order.getTotalPrice())
                    .build();

            for (OrderItem orderItem : order.getOrderItem()) {

                Item item = orderItem.getItem();

                CartAndOrderItemDto cartAndOrderItemDto = CartAndOrderItemDto.builder()
                        .item_id(item.getId())
                        .itemName(item.getItemName())
                        .color(item.getColor())
                        .size(item.getSize())
                        .price(comma(item.getPrice()))
                        .thumbnailImages(item.getThumbnailImage())
                        .quantity(orderItem.getQuantity())
                        .build();

                orderItemList.add(cartAndOrderItemDto);
            }
            orderListDto.setOrderItem(orderItemList);
            orderListDtoList.add(orderListDto);
        }
        return orderListDtoList;
    }


    public List<OrderListDto> listByOrderStatus(Map<String, Long> order_status_id_map, Pageable pageable, Authentication authentication){
        Long member_id = getMember(authentication).getId();
        Long order_status_id = order_status_id_map.get("order_status_id");
        Page<Orders> orderList = orderRepository.findAllByMember_idAndOrderStatus_id(member_id,order_status_id, pageable);

        List<Orders> orders = orderList.getContent();

        List<OrderListDto> orderListDtoList = new ArrayList<>();
        for (Orders order : orders) {
            List<CartAndOrderItemDto> orderItemList =  new ArrayList<>();

            OrderListDto orderListDto = OrderListDto.builder()
                    .id(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .created_at(order.getCreated_at())
                    .orderStatus(order.getOrderStatus())
                    .paymentMethod(order.getPaymentMethod())
                    .totalPrice(order.getTotalPrice())
                    .build();

            for (OrderItem orderItem : order.getOrderItem()) {

                Item item = orderItem.getItem();

                CartAndOrderItemDto cartAndOrderItemDto = CartAndOrderItemDto.builder()
                        .item_id(item.getId())
                        .itemName(item.getItemName())
                        .color(item.getColor())
                        .size(item.getSize())
                        .price(comma(item.getPrice()))
                        .thumbnailImages(item.getThumbnailImage())
                        .quantity(orderItem.getQuantity())
                        .build();

                orderItemList.add(cartAndOrderItemDto);
            }
            orderListDto.setOrderItem(orderItemList);
            orderListDtoList.add(orderListDto);
        }
        return orderListDtoList;
    }


    public String delete(Map<String, Long> orderIdMap){
        Long orderId = orderIdMap.get("order_id");
        orderRepository.deleteById(orderId);
        return "DELETE DONE";
    }


    private Member getMember(Authentication authentication) {
        return memberRepository.findByEmail(authentication.getName()).orElseThrow();
    }

    private String getOrderNumber(){
        LocalDateTime now = LocalDateTime.now();
        String time = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
        Random random = new Random();

        return time + "-" + random.nextInt(10000000);
    }

    public String comma(int value) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(value);
    }
}
