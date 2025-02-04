package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.DeliveryStatus;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import java.time.LocalDateTime;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalOrderResponse {

	private String totalOrderId;

	private Long price;

	private Long discount;

	private Long priceAmount;

	private Long deliveryCost;

	private Long issuedId;

	private String username;

	private String orderName;

	private String address;

	private String paymentKey;

	private PaymentStatementEnum paymentStatementEnum;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

	public TotalOrderResponse(TotalOrder totalOrder, User user) {
		this.totalOrderId = totalOrder.getMerchantUid();
		this.price = totalOrder.getPrice();
		this.discount = totalOrder.getDiscount();
		this.priceAmount = totalOrder.getPriceAmount();
		this.deliveryCost = totalOrder.getDeliveryCost();
		this.issuedId = totalOrder.getIssueId();
		this.username = user.getUsername();
		this.orderName = totalOrder.getOrderName();
		this.address = totalOrder.getAddress();
		this.createdAt = totalOrder.getCreatedAt();
		this.modifiedAt = totalOrder.getModifiedAt();
		this.paymentStatementEnum = totalOrder.getPaymentStatementEnum();
		this.paymentKey = totalOrder.getPaymentKey();
	}
}
