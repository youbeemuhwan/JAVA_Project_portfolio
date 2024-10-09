package Project.commercial.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
    CARD("카드"),
    BANK_TRANSFER("무통장 입금"),
    MOBILE_PAYMENT("휴대폰 결제"),
    SIMPLE_PAYMENT("간편 결제"),
    POINT("포인트 결제");


    private final String description;
    PaymentMethodEnum(String description) {
        this.description = description;
    }
}