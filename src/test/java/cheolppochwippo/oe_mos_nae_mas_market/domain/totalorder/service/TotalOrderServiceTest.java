//package cheolppochwippo.oe_mos_nae_mas_market.domain.totalorder.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
//import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
//import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderGetResponse;
//import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
//import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
//import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service.TotalOrderServiceImpl;
//import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
//import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
//import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
//import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
//import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.MessageSource;
//
//@ExtendWith(MockitoExtension.class)
//public class TotalOrderServiceTest {
//
//	@Mock
//	TotalOrderRepository totalOrderRepository;
//
//	@Mock
//	IssuedRepository issuedRepository;
//
//	@Mock
//	MessageSource messageSource;
//
//	@InjectMocks
//	TotalOrderServiceImpl totalOrderService;
//
//	private User testUser() {
//		return new User(1L, "test", "12345678", RoleEnum.SELLER,"01012345678",false);
//	}
//
//
//
//	@Test
//	@DisplayName("주문 정보보기 테스트")
//	void getTotalOrder() {
//		//given
//		User user = testUser();
//		Long totalOrderId = 1L;
//		TotalOrder totalOrder = testTotalOrder(user);
//		when(totalOrderRepository.findById(totalOrderId)).thenReturn(Optional.of(totalOrder));
//		//when
//		TotalOrderGetResponse response = totalOrderService.getTotalOrder(user, totalOrderId);
//		//then
//		assertEquals(response.getOrderName(), totalOrder.getOrderName());
//		assertEquals(response.getPriceAmount(), totalOrder.getPriceAmount());
//	}
//
//	@Test
//	@DisplayName("주문 정보보기 실패 테스트 - NOT_FOUND")
//	void getTotalOrderNotFound() {
//		//given
//		User user = testUser();
//		Long totalOrderId = 1L;
//		TotalOrder totalOrder = testTotalOrder(user);
//		when(totalOrderRepository.findById(totalOrderId)).thenReturn(Optional.empty());
//		//when
//		Exception exception = assertThrows(NoEntityException.class,
//			() -> totalOrderService.getTotalOrder(user, totalOrderId));
//	}
//
//	@Test
//	@DisplayName("주문 정보보기 실패 테스트 - FORBIDDEN")
//	void getTotalOrderForbidden() {
//		//given
//		User user = new User(5L, "test", "12345678", RoleEnum.SELLER,"01012345678",false);
//		Long totalOrderId = 1L;
//		TotalOrder totalOrder = testTotalOrder(testUser());
//		when(totalOrderRepository.findById(totalOrderId)).thenReturn(Optional.of(totalOrder));
//		//when
//		Exception exception = assertThrows(NoPermissionException.class,
//			() -> totalOrderService.getTotalOrder(user, totalOrderId));
//	}
//
//}
