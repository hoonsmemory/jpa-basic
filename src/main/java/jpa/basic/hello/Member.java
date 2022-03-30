package jpa.basic.hello;

import javax.persistence.*;
import java.util.Date;

//JPA 사용
@Entity
@Table(name = "Member")
public class Member {

    public Member() {}

    @Id
    private Long id;

    /**
     * DDL 생성 기능(JPA의 실행 로직에는 영향을 주지 않는다.)
     * insertable, updatable : 등록, 변경 가능 여부
     * unique : @Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제약조건을 걸 때 사용한다.(유니크 값을 알아 볼 수 없어 비추)
     * columnDefinition = "varchar(100) default ‘EMPTY'" 과 같이 컬럼 정보를 직접 줄 수 있다.
     *
     */
    @Column(name = "name", unique = true, length = 20, nullable = false)
    private String name;

    private Integer age;

    /**
     * EnumType.ORDINAL : enum 순서를 데이터베이스에 저장
     * EnumType.STRING : enum 이름을 데이터베이스에 저장
     * 필수로 EnumType.STRING을 사용해야 한다.
     */
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    // DB 컬럼과 관련없는 필드
    @Transient
    private String temp;

    public Member(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
