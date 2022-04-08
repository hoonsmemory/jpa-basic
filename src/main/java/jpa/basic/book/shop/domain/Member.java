package jpa.basic.book.shop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 임베디드 타입 장점
 * 재사용
 * 높은 응집도
 * Period.isWork() 처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있음
 * 임베디드 타입을 포함한 모든 값 타입은 값 타입을 소유한 엔티티에 생명주기를 의존함
 */
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID", nullable = false)
    private Long id;
    private String name;

    //값 타입을 사용하는 곳에 표시
    @Embedded
    private Period period;

    //값 타입을 사용하는 곳에 표시
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }


}
