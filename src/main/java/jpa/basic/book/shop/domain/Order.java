package jpa.basic.book.shop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue // default : strategy = GenerationType.AUTO
    @Column(name = "ORDER_ID", nullable = false)
    private Long id;

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;
    private LocalDateTime orderDate; //스프링 부트에서는 order_date로 바꿔준다.(관례)

    @Enumerated(EnumType.STRING) //EnumType.ORDINAL 으로 할 경우 잘못된 값이 들어갈 수 있다.
    private OrderState status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
