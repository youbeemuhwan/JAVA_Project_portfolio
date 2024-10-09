package Project.commercial.enums;

import lombok.Getter;


@Getter
public enum OrderStatusEnum {

    WAITING_DEPOSIT("임급 대기"),
    DEPOSIT_CHECK("입금 확인"),
    PREPARING("배송 준비 중"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    CANCELLED("주문 취소"),
    REFUND_REQUESTED("환불 요청"),
    REFUND_PROCESSING("환불 진행 중"),
    REFUND_COMPLETED("환불 완료"),
    EXCHANGE_REQUESTED("교환 요청"),
    EXCHANGE_PROCESSING("교환 진행 중"),
    EXCHANGE_COMPLETED("교환 완료"),
    RETURN_REQUESTED("환불 요청"),
    RETURN_PROCESSING("환불 진행 중"),
    RETURN_COMPLETED("환불 완료");

    private final String description;

    OrderStatusEnum(String description) {
        this.description = description;
    }
}
