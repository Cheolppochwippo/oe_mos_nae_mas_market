package cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.QuantityUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {@Index(name = "idx_product_id",columnList = "id")})
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String info;

    private Long realPrice;

    private Long price;

    private Long discount;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Product(ProductRequest product, Store store) {
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.realPrice = product.getRealPrice();
        this.price = product.getRealPrice() - product.getDiscount();
        this.discount = product.getDiscount();
        this.quantity = product.getQuantity();
        this.deleted = Deleted.UNDELETE;
        this.store = store;
    }

    public void update(ProductUpdateRequest product) {
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.realPrice = product.getRealPrice();
        this.price = product.getRealPrice() - product.getDiscount();
        this.discount = product.getDiscount();
    }

    public void delete() {
        this.deleted = Deleted.DELETE;
    }

    public void quantity(QuantityUpdateRequest productRequest) {
        this.quantity = productRequest.getQuantity();
    }

}
