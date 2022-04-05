package jpa.basic.book.shop.domain;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑정보를 모으는 역할
 * 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
 * 참고: @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속 가능
 */
@MappedSuperclass
public abstract class BaseEntity {

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifyedBy;
    private LocalDateTime lastModifyedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifyedBy() {
        return lastModifyedBy;
    }

    public void setLastModifyedBy(String lastModifyedBy) {
        this.lastModifyedBy = lastModifyedBy;
    }

    public LocalDateTime getLastModifyedDate() {
        return lastModifyedDate;
    }

    public void setLastModifyedDate(LocalDateTime lastModifyedDate) {
        this.lastModifyedDate = lastModifyedDate;
    }
}
