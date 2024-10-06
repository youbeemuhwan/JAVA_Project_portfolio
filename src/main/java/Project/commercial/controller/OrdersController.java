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

@Controller
@RequestMapping("order")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @PostMapping()
    @ResponseBody
    public ResponseCreateOrderDto createOrder(@RequestBody CreateOrderDto createOrderDto, Authentication authentication)
    {
        return orderService.createOrder(createOrderDto, authentication);
    }

    @GetMapping("/my")
    @ResponseBody
    public List<OrderListDto> getMyOrders(@PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable, Authentication authentication)
    {
        return orderService.getMyOrders(pageable, authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteOrder(@PathVariable() Long id)
    {
        return orderService.deleteOrder(id);
    }

    @PostMapping("/myCart")
    @ResponseBody
    public ResponseCreateOrderInCartDto createOrderByCart(@RequestBody CreateOrderInCartDto createOrderInCartDto, Authentication authentication)
    {
        return orderService.createOrderByCart(createOrderInCartDto, authentication);
    }

    @GetMapping("/status/{id}")
    @ResponseBody
    public List<OrderListDto> getOrderByStatus(@PathVariable Long id, @PageableDefault(size = 5) Pageable pageable, Authentication authentication)
    {
        return orderService.getOrderByStatus(id, pageable, authentication);
    }
}
