package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
//import cheolppochwippo.oe_mos_nae_mas_market.global.config.SQSConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.SQSConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InvalidCouponDataException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    private final SQSConfig sqsConfig;

    private final MessageSource messageSource;

    @Transactional
    @Override
    @CacheEvict(value = "coupons", cacheManager = "cacheManager", key = "'allCoupons'")
    public CouponResponse createCoupon(CouponRequest couponRequest) {
        if (couponRequest.getDiscount() < 0.01) {
            throw new InvalidCouponDataException(
                messageSource.getMessage("invalid.coupon.discount", null, Locale.KOREA));
        }
        if (couponRequest.getEffectiveDate().isBefore(LocalDateTime.now())) {
            throw new InvalidCouponDataException(
                messageSource.getMessage("invalid.coupon.effectiveDate", null, Locale.KOREA));
        }
        if (couponRequest.getAmount() == 0) {
            throw new InvalidCouponDataException(
                messageSource.getMessage("invalid.coupon.amount", null, Locale.KOREA));
        }

        Coupon coupon = new Coupon(couponRequest);
        Coupon savedCoupon = couponRepository.save(coupon);

        List<String> phoneNumbers = userRepository.getPhoneNumberFindByConsentTrue();
        if (!phoneNumbers.isEmpty()) {
            sqsConfig.sendCouponMessages(phoneNumbers,
                "[외못내맛]" + "\n" + coupon.getCouponInfo() + "쿠폰이 발행되었습니다!");
        }

        return new CouponResponse(savedCoupon);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "coupons", cacheManager = "cacheManager", key = "'allCoupons'")
    public List<CouponResponse> getCoupons() {
        List<Coupon> coupons = couponRepository.findAllUndeletedCoupons();
        return coupons.stream()
            .map(CouponResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    @CacheEvict(value = "coupons", cacheManager = "cacheManager", key = "'allCoupons'")
    public CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest) {
        Coupon coupon = findCoupon(couponId);
        coupon.update(couponRequest);

        Coupon updatedCoupon = couponRepository.save(coupon);
        return new CouponResponse(updatedCoupon);
    }

    @Transactional
    @Override
    @CacheEvict(value = "coupons", cacheManager = "cacheManager", key = "'allCoupons'")
    public CouponResponse deleteCoupon(Long couponId) {
        Coupon coupon = findCoupon(couponId);
        coupon.delete();
        return new CouponResponse(coupon);
    }

    private Coupon findCoupon(Long couponId) {
        return couponRepository.findById(couponId)
            .orElseThrow(() -> new NoEntityException(
                messageSource.getMessage("noEntity.coupon", null, Locale.KOREA)));
    }
}
