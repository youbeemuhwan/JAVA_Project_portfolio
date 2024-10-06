package Project.commercial.service;

import Project.commercial.domain.*;
import Project.commercial.dto.cart.ResponseItemInCartAndOrderDto;
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


    public ResponseCreateOrderDto createOrder(CreateOrderDto createOrderDto, Authentication authentication) {
        Orders newOrder = buildOrder(createOrderDto, authentication);
        orderRepository.save(newOrder);

        handlePaymentMethod(newOrder, createOrderDto);

        OrderItem newOrderItem = buildOrderItem(newOrder, createOrderDto);
        orderItemRepository.save(newOrderItem);

        return buildOrderResponse(newOrder, newOrderItem);
    }

    private Orders buildOrder(CreateOrderDto createOrderDto, Authentication authentication) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(createOrderDto.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 결제 수단입니다."));


        Item item = itemRepository.findById(createOrderDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 아이템입니다."));


        int totalPrice = item.getPrice() * createOrderDto.getQuantity();

        return Orders.builder()
                .member(getMember(authentication))
                .orderNumber(getOrderNumber())
                .createdAt(LocalDateTime.now())
                .address(createOrderDto.getAddress())
                .paymentMethod(paymentMethod)
                .totalPrice(totalPrice)
                .build();
    }
    private void handlePaymentMethod(Orders order, CreateOrderDto createOrderDto) {
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

    private OrderItem buildOrderItem(Orders order, CreateOrderDto createOrderDto) {
        return OrderItem.builder()
                .orders(order)
                .item(itemRepository.findById(createOrderDto.getItemId()).orElseThrow(() ->
                        new IllegalArgumentException("유효하지 않은 아이템입니다.")
                ))
                .quantity(createOrderDto.getQuantity())
                .build();
    }

    private ResponseCreateOrderDto buildOrderResponse(Orders order, OrderItem orderItem) {
        ResponseItemInCartAndOrderDto responseItemInCartAndOrderDto = ResponseItemInCartAndOrderDto.builder()
                .itemId(orderItem.getItem().getId())
                .itemName(orderItem.getItem().getItemName())
                .price(comma(orderItem.getItem().getPrice()))
                .color(orderItem.getItem().getColor())
                .size(orderItem.getItem().getSize())
                .thumbnailImages(orderItem.getItem().getThumbnailImage())
                .quantity(orderItem.getQuantity())
                .build();

        return ResponseCreateOrderDto.builder()
                .order_id(order.getId())
                .order_number(order.getOrderNumber())
                .created_at(order.getCreatedAt())
                .item(responseItemInCartAndOrderDto)
                .address(order.getAddress())
                .total_price(comma(order.getTotalPrice()))
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .build();
    }

    public ResponseCreateOrderInCartDto createOrderByCart(CreateOrderInCartDto createOrderInCartDto, Authentication authentication) {
        Member member = getMember(authentication);

        // 주문 생성
        Orders newOrder = buildOrder(createOrderInCartDto, member);
        orderRepository.save(newOrder);

        // 카트 조회 및 비어있으면 예외 처리
        Cart cart = cartRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new RuntimeException("해당 회원의 카트가 존재하지 않습니다."));
        List<CartItem> cartItemsByMember = cartItemRepository.findAllByCartId(cart.getId());

        if (cartItemsByMember.isEmpty()) {
            throw new RuntimeException("카트가 비어 있습니다.");
        }

        List<ResponseItemInCartAndOrderDto> orderItemList = new ArrayList<>();
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
        cartRepository.deleteByMemberId(member.getId());

        return buildOrderResponse(newOrder, orderItemList, totalPrice);
    }

    private Orders buildOrder(CreateOrderInCartDto requestDto, Member member) {
        return Orders.builder()
                .member(member)
                .orderNumber(getOrderNumber())
                .createdAt(LocalDateTime.now())
                .address(requestDto.getAddress())
                .paymentMethod(paymentMethodRepository.findById(requestDto.getPaymentMethodId())
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

    private ResponseItemInCartAndOrderDto buildCartAndOrderItemDto(CartItem cartItem) {
        return ResponseItemInCartAndOrderDto.builder()
                .itemId(cartItem.getItem().getId())
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

    private ResponseCreateOrderInCartDto buildOrderResponse(Orders order, List<ResponseItemInCartAndOrderDto> orderItemList, int totalPrice) {
        return ResponseCreateOrderInCartDto.builder()
                .orderId(order.getId())
                .createdAt(order.getCreatedAt())
                .orderNumber(order.getOrderNumber())
                .itemList(orderItemList)
                .address(order.getAddress())
                .paymentMethod(order.getPaymentMethod())
                .orderStatus(order.getOrderStatus())
                .totalPrice(comma(totalPrice))
                .build();
    }
    public List<OrderListDto> getMyOrders(Pageable pageable, Authentication authentication){
        Member member = getMember(authentication);
        Page<Orders> orderListByMember = orderRepository.findAllByMember_id(member.getId(), pageable);

        List<OrderListDto> orderListDtoList = new ArrayList<>();
        for (Orders order : orderListByMember.getContent()) {
            List<ResponseItemInCartAndOrderDto> orderItemList =  new ArrayList<>();

            OrderListDto orderListDto = OrderListDto.builder()
                    .id(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .createdAt(order.getCreatedAt())
                    .orderStatus(order.getOrderStatus())
                    .paymentMethod(order.getPaymentMethod())
                    .totalPrice(order.getTotalPrice())
                    .build();

            for (OrderItem orderItem : order.getOrderItem()) {

                Item item = orderItem.getItem();

                ResponseItemInCartAndOrderDto responseItemInCartAndOrderDto = ResponseItemInCartAndOrderDto.builder()
                        .itemId(item.getId())
                        .itemName(item.getItemName())
                        .color(item.getColor())
                        .size(item.getSize())
                        .price(comma(item.getPrice()))
                        .thumbnailImages(item.getThumbnailImage())
                        .quantity(orderItem.getQuantity())
                        .build();

                orderItemList.add(responseItemInCartAndOrderDto);
            }
            orderListDto.setOrderItem(orderItemList);
            orderListDtoList.add(orderListDto);
        }
        return orderListDtoList;
    }


    public List<OrderListDto> getOrderByStatus(Long statusId, Pageable pageable, Authentication authentication){
        Long member_id = getMember(authentication).getId();
        Page<Orders> orderList = orderRepository.findAllByMember_idAndOrderStatus_id(member_id,statusId, pageable);

        List<Orders> orders = orderList.getContent();

        List<OrderListDto> orderListDtoList = new ArrayList<>();
        for (Orders order : orders) {
            List<ResponseItemInCartAndOrderDto> orderItemList =  new ArrayList<>();

            OrderListDto orderListDto = OrderListDto.builder()
                    .id(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .createdAt(order.getCreatedAt())
                    .orderStatus(order.getOrderStatus())
                    .paymentMethod(order.getPaymentMethod())
                    .totalPrice(order.getTotalPrice())
                    .build();

            for (OrderItem orderItem : order.getOrderItem()) {

                Item item = orderItem.getItem();

                ResponseItemInCartAndOrderDto responseItemInCartAndOrderDto = ResponseItemInCartAndOrderDto.builder()
                        .itemId(item.getId())
                        .itemName(item.getItemName())
                        .color(item.getColor())
                        .size(item.getSize())
                        .price(comma(item.getPrice()))
                        .thumbnailImages(item.getThumbnailImage())
                        .quantity(orderItem.getQuantity())
                        .build();

                orderItemList.add(responseItemInCartAndOrderDto);
            }
            orderListDto.setOrderItem(orderItemList);
            orderListDtoList.add(orderListDto);
        }
        return orderListDtoList;
    }


    public String deleteOrder(Long id){
        orderRepository.deleteById(id);
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
