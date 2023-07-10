package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class OrderInCartCreateRequestDto {

    private String address;

    private Long paymentMethod_id;


    public OrderInCartCreateRequestDto() {
    }
}
