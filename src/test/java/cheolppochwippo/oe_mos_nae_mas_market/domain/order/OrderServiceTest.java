package cheolppochwippo.oe_mos_nae_mas_market.domain.order;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.service.OrderService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    @DisplayName("주문생성")
    void createOrder(){
        User userA = new User("userA","1234");
    }

}
