package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service.CouponService;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Coupon API", description = "쿠폰 API")
@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "쿠폰 생성", description = "쿠폰 생성")
    @PostMapping("/coupons")
    public ResponseEntity<CommonResponse<CouponResponse>> createCoupon(
        @RequestBody CouponRequest couponRequest
    ) {
        CouponResponse createdCoupon = couponService.createCoupon(couponRequest);
        return ResponseEntity.status(HttpStatus.CREATED.value())
            .body(CommonResponse.<CouponResponse>builder()
                .msg("coupon created complete!")
                .data(createdCoupon)
                .build());
    }

    @Operation(summary = "발급 가능한 쿠폰 리스트 확인", description = "발급 가능한 쿠폰 리스트 확인")
    @GetMapping("/coupons")
    public ResponseEntity<CommonResponse<List<CouponResponse>>> getCoupons() {
        List<CouponResponse> couponResponses = couponService.getCoupons();
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<List<CouponResponse>>builder()
                .msg("coupon get complete!")
                .data(couponResponses)
                .build());
    }

    @Operation(summary = "쿠폰 수정", description = "쿠폰 수정")
    @PatchMapping("/coupons/{couponId}")
    public ResponseEntity<CommonResponse<CouponResponse>> updateCoupon(
        @PathVariable Long couponId,
        @RequestBody CouponRequest couponRequest
    ) {
        CouponResponse updatedCoupon = couponService.updateCoupon(couponId, couponRequest);
        return ResponseEntity.status(HttpStatus.CREATED.value())
            .body(CommonResponse.<CouponResponse>builder()
                .msg("coupon updated complete!")
                .data(updatedCoupon)
                .build());
    }

    @Operation(summary = "쿠폰 삭제", description = "쿠폰 삭제")
    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<CommonResponse<CouponResponse>> deleteCoupon(
        @PathVariable Long couponId
    ) {
        CouponResponse deletedCoupon = couponService.deleteCoupon(couponId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value())
            .body(CommonResponse.<CouponResponse>builder()
                .msg("coupon deleted complete!")
                .data(deletedCoupon)
                .build());
    }

}
