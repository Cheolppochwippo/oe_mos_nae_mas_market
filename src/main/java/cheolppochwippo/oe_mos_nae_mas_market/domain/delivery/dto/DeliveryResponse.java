package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DeliveryResponse {
    private Long deliveryId;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public DeliveryResponse(Delivery delivery){
        this.deliveryId = delivery.getId();
        this.address = delivery.getAddress();
        this.createdAt = delivery.getCreatedAt();
        this.modifiedAt = delivery.getModifiedAt();
    }
}