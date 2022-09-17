package jpa.basic.book.shop.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

//값 타입을 정의하는 곳에 표시
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;

    public Address() {

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    /*
        equals, hashCode 를 Override 할 때는 Use getters during code generation 으로 선택해야 한다.
        그 이유는 메서드가 아닌 필드를 직접 접근할 경우 프록시일 때는 계산이 안된다.
        프록시를 고려하여 메서드로 접근할 수 있도록 하는 것이 좋다.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
