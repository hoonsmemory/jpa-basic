package jpa.basic.book.shop.domain;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

//값 타입을 정의하는 곳에 표시
@Embeddable
public class Period {
    LocalDateTime startDate;
    LocalDateTime endDate;

    public Period() {
    }

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

}
