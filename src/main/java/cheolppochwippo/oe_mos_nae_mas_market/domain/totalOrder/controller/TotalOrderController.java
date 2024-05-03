package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrdersGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service.TotalOrderServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TotalOrder API", description = "총주문 API")
@RestController
@RequiredArgsConstructor
public class TotalOrderController {

	private final TotalOrderServiceImpl totalOrderService;

	//주문 요청시 totalOrder 생성
	@Operation(summary = "총주문 생성", description = "총주문 생성")
	@PostMapping("/totalOrders")
	public ResponseEntity<CommonResponse<TotalOrderResponse>> createTotalOrder(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody TotalOrderRequest request) {
		TotalOrderResponse totalOrderResponse = totalOrderService.createTotalOrder(
			userDetails.getUser(), request);
		return ResponseEntity.ok()
			.body(CommonResponse.<TotalOrderResponse>builder()
				.data(totalOrderResponse)
				.msg("create totalOrder")
				.build());
	}

	@Operation(summary = "총주문 상세 보기", description = "총주문 상세 보기")
	@GetMapping("/totalOrders/{totalOrderId}")
	public ResponseEntity<CommonResponse<TotalOrderGetResponse>> getTotalOrder(
		@PathVariable Long totalOrderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		TotalOrderGetResponse totalOrderResponse = totalOrderService.getTotalOrder(
			userDetails.getUser(), totalOrderId);
		return ResponseEntity.ok().body(CommonResponse.<TotalOrderGetResponse>builder()
			.msg("show totalOrder complete!")
			.data(totalOrderResponse)
			.build());
	}

	@Operation(summary = "총주문 전체 보기", description = "총주문 전체 보기")
	@GetMapping("/totalOrders")
	public ResponseEntity<CommonResponse<Page<TotalOrdersGetResponse>>> getTotalOrders(
		@RequestParam(defaultValue = "1") int page,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Page<TotalOrdersGetResponse> totalOrdersGetResponses = totalOrderService.getTotalOrders(
			userDetails.getUser(), page);
		return ResponseEntity.ok().body(CommonResponse.<Page<TotalOrdersGetResponse>>builder()
			.msg("show totalOrders complete!")
			.data(totalOrdersGetResponses)
			.build());
	}

}
