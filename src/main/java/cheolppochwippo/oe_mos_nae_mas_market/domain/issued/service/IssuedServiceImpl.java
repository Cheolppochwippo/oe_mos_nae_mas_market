package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.RedisConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InsufficientQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableCaching
public class IssuedServiceImpl implements IssuedService {

    private final IssuedRepository issuedRepository;
    private final CouponRepository couponRepository;
    private final RedisConfig redisConfig;
    private final MessageSource messageSource;
    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;

    @Override
    public IssuedResponse issueCoupon(Long couponId, User user) {
        if (isCouponAlreadyIssued(couponId, user.getId())) {
            throw new IllegalArgumentException("이미 쿠폰을 발급 받으셨습니다.");
        }
        Coupon coupon = getCouponById(couponId);
        Issued issuedCoupon = saveIssuedCoupon(coupon, user);
        cacheIssuedCoupon(couponId, user.getId(), issuedCoupon, coupon.getEffective_date());
        return createIssuedResponse(couponId, coupon, issuedCoupon);
    }


    private boolean isCouponAlreadyIssued(Long couponId, Long userId) {
        RBucket<Issued> cacheBucket = redisConfig.redissonClient()
            .getBucket("IssuedCoupon:" + couponId + ":" + userId);
        Issued issuedCoupon = cacheBucket.get();
        return issuedCoupon != null;
    }

    private Coupon getCouponById(Long couponId) {
        return couponRepository.findById(couponId)
            .orElseThrow(() -> new NoEntityException(
                (messageSource.getMessage("noEntity.coupon", null, Locale.KOREA))));
    }

    public void decreaseCouponAmountAndProductStock(Long issuedId, Order order) {
        RLock lock = redissonClient.getFairLock(
            "issuedAndProduct:" + issuedId + ":" + order.getProduct().getId());
        try {
            boolean isLocked = lock.tryLock(1000, 3000, TimeUnit.SECONDS);
            if (isLocked) {
                try {
//                    Coupon coupon = issuedRepository.findByIssued(issuedId);
//                    coupon.decreaseAmount();
//                    couponRepository.save(coupon);
//                    Product product = productRepository.findByOrder(order);
//                    long newStock = product.getQuantity() - order.getQuantity();
//                    if (newStock < 0) {
//                        throw new InsufficientQuantityException(
//                            messageSource.getMessage("insufficient.quantity.product", null,
//                                Locale.KOREA));
//                    }
//                    product.quatityUpdate(newStock);
//                    productRepository.save(product);
                    decreaseCouponAmountAndProductStockTransaction(issuedId, order);
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public void decreaseCouponAmountAndProductStockTransaction(Long issuedId, Order order) {
        Coupon coupon = issuedRepository.findByIssued(issuedId);
        coupon.decreaseAmount();
        couponRepository.save(coupon);
        Product product = productRepository.findByOrder(order);
        long newStock = product.getQuantity() - order.getQuantity();
        if (newStock < 0) {
            throw new InsufficientQuantityException(
                messageSource.getMessage("insufficient.quantity.product", null,
                    Locale.KOREA));
        }
        product.quatityUpdate(newStock);
        productRepository.save(product);
    }

    public void decreaseCouponAmount(Long issuedId) {
        Issued issuedCoupon = issuedRepository.findById(issuedId)
            .orElseThrow(() -> new InsufficientQuantityException(
                messageSource.getMessage("insufficient.quantity.coupon",
                    null, Locale.KOREA)));
        Long couponId = issuedCoupon.getCoupon().getId();
        RLock couponLock = redissonClient.getFairLock("coupon:" + couponId);
        try {
            try {
                boolean isCouponLocked = couponLock.tryLock(1000, 3000, TimeUnit.SECONDS);
                if (isCouponLocked) {
                    decreaseCouponAmountTransaction(issuedId);
                }
            } finally {
                couponLock.unlock();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            System.out.println(e.getMessage());
        }
    }
//            } finally {
//                lock.unlock();
//            }


    @Transactional
    public void decreaseCouponAmountTransaction(Long issuedId) {
        Issued issuedCoupon = issuedRepository.findById(issuedId)
            .orElseThrow(() -> new IllegalArgumentException("발급된 쿠폰이 없습니다."));
        Coupon coupon = issuedCoupon.getCoupon();
        if (coupon.getAmount() > 0) {
            coupon.decreaseAmount();
        } else {
            throw new IllegalArgumentException("쿠폰 재고가 소진되었습니다.");
        }
        couponRepository.save(coupon);
    }


    private Issued saveIssuedCoupon(Coupon coupon, User user) {
        Issued issuedCoupon = new Issued(coupon, user);
        return issuedRepository.save(issuedCoupon);
    }

    private void cacheIssuedCoupon(Long couponId, Long userId, Issued issuedCoupon,
        LocalDateTime expirationDate) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, expirationDate);
        long timeToLive = duration.toMillis();

        RBucket<Issued> cacheBucket = redisConfig.redissonClient()
            .getBucket("IssuedCoupon:" + couponId + ":" + userId);
        cacheBucket.set(issuedCoupon, timeToLive, TimeUnit.MILLISECONDS);
    }

    public boolean checkCouponStock(Long issuedId, int requiredCouponCount) {
        Issued issuedCoupon = issuedRepository.findById(issuedId)
            .orElseThrow(() -> new IllegalArgumentException("발급된 쿠폰이 없습니다."));
        Long availableCouponCount = issuedCoupon.getCoupon().getAmount();
        return availableCouponCount >= requiredCouponCount;
    }

    private IssuedResponse createIssuedResponse(Long couponId, Coupon coupon, Issued issuedCoupon) {
        return new IssuedResponse(issuedCoupon.getId(), couponId, coupon.getCouponInfo(),
            issuedCoupon.getCreatedAt(), issuedCoupon.getDeleted());
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "issuedCoupons", key = "#user.id", cacheManager = "cacheManager")
    public List<IssuedResponse> getIssuedCoupons(User user) {
        return issuedRepository.findCouponByUser(user);
    }
}
