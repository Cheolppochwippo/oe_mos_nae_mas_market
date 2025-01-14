package cheolppochwippo.oe_mos_nae_mas_market.domain.order.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInStoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInStoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderInCartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository.OrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InsufficientQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.MinimumQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final MessageSource messageSource;

	@Transactional
	public SingleOrderInCartResponse createOrderInCart(User user, Long quantity, Long productId) {
		Product findProduct = productRepository.findById(productId).orElseThrow(
			() -> new NoEntityException(
				messageSource.getMessage("noEntity.product", null, Locale.KOREA)));
		if (quantity > findProduct.getQuantity()) {
			throw new InsufficientQuantityException(
				messageSource.getMessage("insufficient.quantity.product", null, Locale.KOREA));
		}
		Order order;
		Optional<Order> orderByProductIdAndUserBeforeBuy = orderRepository.findOrderByProductIdAndUserBeforeBuy(
			user, productId);
		if (orderByProductIdAndUserBeforeBuy.isEmpty()) {
			order = new Order(quantity, findProduct, user);
			orderRepository.save(order);
		} else {
			order = orderByProductIdAndUserBeforeBuy.get();
			if (order.getQuantity() + quantity > findProduct.getQuantity()) {
				throw new InsufficientQuantityException(
					messageSource.getMessage("insufficient.quantity.product", null, Locale.KOREA));
			}
			order.updateQuantity(order.getQuantity() + quantity);
		}
		return new SingleOrderInCartResponse(order);
	}

	@Override
	@Transactional
	public Long createOrderByCart(User user) {
		orderRepository.deleteOrders(user.getId());
		Long count = orderRepository.updateCartToOrder(user.getId());
		if (count == 0) {
			throw new NoEntityException(
				messageSource.getMessage("noEntity.cart", null, Locale.KOREA));
		}
		return count;
	}

	@Override
	@Transactional
	public SingleOrderInCartResponse createOrderByDirect(User user, Long quantity, Long productId) {
		orderRepository.deleteOrders(user.getId());
		Product findProduct = productRepository.findById(productId).orElseThrow(
			() -> new NoEntityException(
				messageSource.getMessage("noEntity.product", null, Locale.KOREA)));
		if (quantity > findProduct.getQuantity()) {
			throw new InsufficientQuantityException(
				messageSource.getMessage("insufficient.quantity.product", null, Locale.KOREA));
		}
		Order order = new Order(quantity, findProduct, user, false);
		orderRepository.save(order);
		return new SingleOrderInCartResponse(order);
	}

	@Transactional
	public SingleOrderInCartResponse updateQuantity(Long quantity, Long orderId) {
		if (quantity < 1) {
			throw new MinimumQuantityException(
				messageSource.getMessage("minimum.quantity.product", null, Locale.KOREA));
		}
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new NoEntityException(
				messageSource.getMessage("noEntity.order", null, Locale.KOREA)));
		Product findProduct = productRepository.findById(order.getProduct().getId()).orElseThrow();
		if (findProduct.getQuantity() < quantity) {
			throw new InsufficientQuantityException(
				messageSource.getMessage("insufficient.quantity.product", null, Locale.KOREA));
		}
		order.updateQuantity(quantity);

		return new SingleOrderInCartResponse(order);
	}

	@Transactional
	public SingleOrderInCartResponse deleteOrderInCart(User user, Long orderId) {
		//단건 주문 검색
		Order findOrder = orderRepository.findById(orderId)
			.orElseThrow(() -> new NoEntityException(
				messageSource.getMessage("noEntity.order", null, Locale.KOREA)));
		//자신의 주문인지 아닌지
		if (user.equals(findOrder.getUser())) {
			throw new NoPermissionException(
				messageSource.getMessage("noPermission.order", null, Locale.KOREA));
		}

		orderRepository.delete(findOrder);

		return new SingleOrderInCartResponse(findOrder);
	}

	@Transactional(readOnly = true)
	public List<SingleOrderInCartResponse> showOrdersInCart(User user) {
		List<Order> orders = orderRepository.findOrderByUserBeforeBuy(user);
		return orders.stream().map(SingleOrderInCartResponse::new).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllOrderInStoreResponse> showOrdersInStore(User user,
		AllOrderInStoreRequest request) {
		if (!user.getRole().equals(RoleEnum.SELLER)) {
			throw new NoPermissionException(
				messageSource.getMessage("noPermission.role.seller.update", null, Locale.KOREA));
		}
		return orderRepository.findOrderByUserStore(user.getId(), request);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SingleOrderInCartResponse> getStateOrder(User user){
		List<Order> orders = orderRepository.getStateOrders(user.getId());
		return orders.stream().map(SingleOrderInCartResponse::new).toList();
	}

}
