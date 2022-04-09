package jpa.basic.book.shop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 임베디드 타입 장점
 * 재사용
 * 높은 응집도
 * Period.isWork() 처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있음
 * 임베디드 타입을 포함한 모든 값 타입은 값 타입을 소유한 엔티티에 생명주기를 의존함
 * 임베디드 타입은 엔티티의 값일 뿐이다.
 * 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
 * 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능
 * 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래
 *
 * 스의 수가 더 많음
 *
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
    private Address homeAddress;

    //값 타입 컬렉션(값 타입을 하나 이상 저장할 때 사용, 컬렉션을 저장하기 위한 별도의 테이블을 생성)
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD",
                     joinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    /**
     * 한 엔티티에서 같은 값 타입을 사용할 경우 속성 재정의를 통해 중복을 막을 수 있다.
     * 값 타입 컬렉션은 영속성 전이(Cascade) + 고아 객체 제거 기능을 필수로 가진다.
     * 조회 기본값이 지연로딩이다.
     * 값 탕비 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고,
     * 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다. (실무에서는 상황에 따라 값 타입 컬렉션 대신 일대다 관계를 고려)
     * 값이 단순하며 추척할 필요가 없을 때 갑 타입 컬렉션을 사용한다.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name="WOKR_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }
}
