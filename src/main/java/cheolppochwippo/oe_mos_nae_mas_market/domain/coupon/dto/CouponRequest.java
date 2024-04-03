package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CouponRequest {

    private String couponInfo;
    private Double discount;
    private LocalDateTime effectiveDate;
    private Long amount;
}
