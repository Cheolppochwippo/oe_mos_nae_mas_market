package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.QIssued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponses;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.QPayment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.QTotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.QUser;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom{

	private final JpaConfig jpaConfig;


	@Override
	public Page<PaymentResponses> getPaymentPageFindByUserId(Long userId, Pageable pageable) {
		List<Payment> query = jpaConfig.jpaQueryFactory()
			.selectFrom(QPayment.payment)
			.where(
				userIdEq(userId)
			)
			.orderBy(QPayment.payment.modifiedAt.desc())
			.fetch();
		List<PaymentResponses> paymentResponses = query.stream().map(PaymentResponses::new).toList();
		return new PageImpl<>(paymentResponses,pageable,paymentResponses.size());
	}

	@Override
	public Optional<Payment> findPaymentKey(String paymentKey) {
		Payment query = jpaConfig.jpaQueryFactory()
			.select(QPayment.payment)
			.from(QPayment.payment)
			.where(
				QPayment.payment.paymentKey.eq(paymentKey),
				QPayment.payment.statement.eq(PaymentStatementEnum.COMPLETE)
			).fetchOne();
		return Optional.ofNullable(query);
	}

	private BooleanExpression userIdEq(Long userId) {
		return Objects.nonNull(userId) ? QPayment.payment.totalOrder.user.id.eq(userId) : null;
	}
}
