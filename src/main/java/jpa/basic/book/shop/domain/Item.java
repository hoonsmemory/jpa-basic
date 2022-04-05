package jpa.basic.book.shop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 상속관계 매핑
 * 부모 엔티티가 사용하지 않는다면 추상화 클래스로 만든다.
 *
 * Inheritance의 3가지 전략
 * JOINED: 조인 전략
 * 장점
 * • 테이블 정규화
 * • 외래 키 참조 무결성 제약조건 활용가능
 * • 저장공간 효율화
 * • 단점
 * • 조회시 조인을 많이 사용, 성능 저하
 * • 조회 쿼리가 복잡함
 * • 데이터 저장시 INSERT SQL 2번 호출
 *
 * SINGLE_TABLE: 단일 테이블 전략
 * 장점
 * • 조인이 필요 없으므로 일반적으로 조회 성능이 빠름
 * • 조회 쿼리가 단순함
 * • 단점
 * • 자식 엔티티가 매핑한 컬럼은 모두 null 허용
 * • 단일 테이블에 모든 것을 저장하므로 테이블이 커질 수 있다.
 * • 상황에 따라서 조회 성능이 오히려 느려질 수 있다.
 *
 * TABLE_PER_CLASS: 구현 클래스마다 테이블 전략(이 전략은 데이터베이스 설계자와 ORM 전문가 둘 다 추천X)
 * • 장점
 * • 서브 타입을 명확하게 구분해서 처리할 때 효과적
 * • not null 제약조건 사용 가능
 * • 단점
 * • 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL 필요)
 * • 자식 테이블을 통합해서 쿼리하기 어려움
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE") // 기본 DTYPE
public abstract class Item extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID", nullable = false)
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
