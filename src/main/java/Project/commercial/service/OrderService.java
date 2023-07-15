package Project.commercial.service;

import Project.commercial.Dto.*;
import Project.commercial.domain.*;
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


    public OrderCreateResponseDto orderCreate(OrderCreateRequestDto orderCreateRequestDto, Authentication authentication){

        Orders createOrdersBuild = Orders.builder()
                .member(getMember(authentication))
                .orderNumber(getOrderNumber())
                .created_at(LocalDateTime.now())
                .address(orderCreateRequestDto.getAddress())
                .paymentMethod(paymentMethodRepository.findById(orderCreateRequestDto.getPaymentMethod_id()).orElseThrow())
                .totalPrice(itemRepository.findById(orderCreateRequestDto.getItem_id()).orElseThrow().getPrice() * orderCreateRequestDto.getQuantity())
                .build();

        Orders nowOrders = orderRepository.save(createOrdersBuild);
        orderRepository.flush();

        if(paymentMethodRepository.findById(nowOrders.getPaymentMethod().getId()).orElseThrow().getName().equals("포인트 결제"))
        {
            Member nowMember = memberRepository.findById(createOrdersBuild.getMember().getId()).orElseThrow();
            Integer nowPoint = nowMember.getPoint();
            Integer totalPrice = nowOrders.getTotalPrice();
            
            if(totalPrice > nowPoint){
                throw new RuntimeException("포인트가 부족합니다.");
            }

            int remainingPoint = nowPoint - totalPrice;

            nowMember.updateMemberPoint(remainingPoint);
            createOrdersBuild.updateOrderStatus(orderStatusRepository.findById(2L).orElseThrow());
        }


        if(paymentMethodRepository.findById(nowOrders.getPaymentMethod().getId()).orElseThrow().getName().equals("무통장 입금"))
        {
            createOrdersBuild.updateOrderStatus(orderStatusRepository.findById(1L).orElseThrow());
        }

        OrderItem createOrderItemBuild = OrderItem.builder()
                .orders(nowOrders)
                .item(itemRepository.findById(orderCreateRequestDto.getItem_id()).orElseThrow())
                .quantity(orderCreateRequestDto.getQuantity())
                .build();

        orderItemRepository.save(createOrderItemBuild);
        orderStatusRepository.flush();


        CartAndOrderItemDto cartAndOrderItemDto = CartAndOrderItemDto.builder()
                .item_id(orderCreateRequestDto.getItem_id())
                .itemName(createOrderItemBuild.getItem().getItemName())
                .price(comma(createOrderItemBuild.getItem().getPrice()))
                .color(createOrderItemBuild.getItem().getColor())
                .size(createOrderItemBuild.getItem().getSize())
                .thumbnailImages(createOrderItemBuild.getItem().getThumbnailImage())
                .quantity(createOrderItemBuild.getQuantity())
                .build();

        return OrderCreateResponseDto.builder()
                .order_id(nowOrders.getId())
                .order_number(nowOrders.getOrderNumber())
                .created_at(nowOrders.getCreated_at())
                .item(cartAndOrderItemDto)
                .address(nowOrders.getAddress())
                .total_price(comma(nowOrders.getTotalPrice()))
                .orderStatus(nowOrders.getOrderStatus())
                .paymentMethod(nowOrders.getPaymentMethod())
                .build();
    }

    public OrderInCartCreateResponseDto OrderInCartCreate(OrderInCartCreateRequestDto orderInCartCreateRequestDto, Authentication authentication){

        Member member = getMember(authentication);

        Orders createOrdersBuild = Orders.builder()
                .member(member)
                .orderNumber(getOrderNumber())
                .created_at(LocalDateTime.now())
                .address(orderInCartCreateRequestDto.getAddress())
                .paymentMethod(paymentMethodRepository.findById(orderInCartCreateRequestDto.getPaymentMethod_id()).orElseThrow())
                .build();

        Orders nowOrders = orderRepository.save(createOrdersBuild);
        orderRepository.flush();

        if(cartRepository.findByMember_id(member.getId()).isEmpty()){
            throw new RuntimeException("카트가 존재하지 않습니다.");
        }

        List<CartItem> cartItemsByMember
                = cartItemRepository.findAllByCartId(cartRepository.findByMember_id(member.getId()).get().getId());

        List<CartAndOrderItemDto> orderItemList = new ArrayList<>();
        int total_price =0;

        for (CartItem cartItem : cartItemsByMember){

            OrderItem orderItemBuild = OrderItem.builder()
                    .orders(nowOrders)
                    .item(cartItem.getItem())
                    .quantity(cartItem.getQuantity())
                    .build();

            orderItemRepository.save(orderItemBuild);
            orderItemRepository.flush();

            total_price += cartItem.getItem().getPrice() * cartItem.getQuantity();

            CartAndOrderItemDto cartAndOrderItemDto = CartAndOrderItemDto.builder()
                    .item_id(cartItem.getItem().getId())
                    .itemName(cartItem.getItem().getItemName())
                    .color(cartItem.getItem().getColor())
                    .size(cartItem.getItem().getSize())
                    .price(comma(cartItem.getItem().getPrice()))
                    .quantity(cartItem.getQuantity())
                    .thumbnailImages(cartItem.getItem().getThumbnailImage())
                    .build();

            orderItemList.add(cartAndOrderItemDto);
        }

        createOrdersBuild.updateTotalPrice(total_price);

        if (nowOrders.getPaymentMethod().getName().equals("포인트 결제"))
        {
            if (member.getPoint() < total_price)
            {
                throw new RuntimeException("포인트가 부족합니다.");
            }
            int remainingPoint = member.getPoint() - total_price;
            member.updateMemberPoint(remainingPoint);
            nowOrders.updateOrderStatus(orderStatusRepository.findById(2L).orElseThrow());
        }

        if (nowOrders.getPaymentMethod().getName().equals("무통장 입금")){
            nowOrders.updateOrderStatus(orderStatusRepository.findById(1L).orElseThrow());
        }

        cartRepository.deleteByMember_id(member.getId());
        cartRepository.flush();

        return OrderInCartCreateResponseDto.builder()
                .order_id(nowOrders.getId())
                .created_at(nowOrders.getCreated_at())
                .order_number(nowOrders.getOrderNumber())
                .item_list(orderItemList)
                .address(nowOrders.getAddress())
                .paymentMethod(nowOrders.getPaymentMethod())
                .orderStatus(nowOrders.getOrderStatus())
                .total_price(comma(total_price))
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


    public String delete(Map<String, Long> order_id_map){
        Long orderId = order_id_map.get("order_id");
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
