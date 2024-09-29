package Project.commercial.controller;

import Project.commercial.dto.order.*;
import Project.commercial.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @PostMapping("/order/create")
    @ResponseBody
    public OrderCreateResponseDto create(@RequestBody OrderCreateRequestDto orderCreateRequestDto, Authentication authentication)
    {
        return orderService.orderCreate(orderCreateRequestDto, authentication);
    }

    @GetMapping("/order/my_order")
    @ResponseBody
    public List<OrderListDto> list(@PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable, Authentication authentication)
    {
        return orderService.orderList(pageable, authentication);
    }

    @DeleteMapping("/order/delete")
    @ResponseBody
    public String delete(@RequestBody Map<String, Long> orderIdMap)
    {
        return orderService.delete(orderIdMap);
    }

    @PostMapping("/order/cart_order")
    @ResponseBody
    public OrderInCartCreateResponseDto createOrderInCart(@RequestBody OrderInCartCreateRequestDto orderInCartCreateRequestDto, Authentication authentication)
    {
        return orderService.createOrderInCart(orderInCartCreateRequestDto, authentication);
    }

    @GetMapping("order/my_order/order_status")
    @ResponseBody
    public List<OrderListDto> listByOrderStatus(@RequestBody Map<String, Long> orderStatusIdMap, @PageableDefault(size = 5) Pageable pageable, Authentication authentication)
    {
        return orderService.listByOrderStatus(orderStatusIdMap, pageable, authentication);
    }
}
