package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @Column
    @Enumerated(EnumType.STRING)
    private Deleted deleted;

    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private DeliveryStatementEnum deliveryStatementEnum;

    public Delivery(User user, DeliveryRequest deliveryRequest) {
        this.user = user;
        this.address = deliveryRequest.getAddress();
        this.deleted = Deleted.UNDELETE;
    }

    public void update(DeliveryRequest deliveryRequest) {
        this.address = deliveryRequest.getAddress();
    }

    public void delete() {
        this.deleted = Deleted.DELETE;
    }

    public Delivery(TotalOrder totalOrder) {
        orderId = totalOrder.getMerchantUid();
        address = totalOrder.getAddress();
        deleted = Deleted.UNDELETE;
        user = totalOrder.getUser();
        deliveryStatementEnum = DeliveryStatementEnum.WAIT;
    }

    public Delivery(DeliveryStatementEnum deliveryStatementEnum) {
        this.deliveryStatementEnum = deliveryStatementEnum;

    }

    public void deliveryStatement(DeliveryStatementEnum deliveryStatementEnum) {
        this.deliveryStatementEnum = deliveryStatementEnum;
    }

}
