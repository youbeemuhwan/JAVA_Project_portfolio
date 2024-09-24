package Project.commercial.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderInCartCreateRequestDto {

    private String address;

    private Long paymentMethod_id;
}
