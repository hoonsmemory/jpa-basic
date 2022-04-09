package jpa.basic.book.shop;

import jpa.basic.book.shop.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {
    public static void main(String[] args) {
        emebeddedType();
    }


    public static void emebeddedType() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setName("이성훈");
            member.setHomeAddress(new Address("city", "street", "zip"));
            member.setWorkAddress(new Address("city","street","zip"));
            member.setPeriod(new Period(LocalDateTime.now(), LocalDateTime.now()));
            em.persist(member);

            /**
             * 값 타입과 불변 객체
             * 만약 member의 workAddress를 수정해야한다면 새로운 객체를 생성 후 바꿔줘야한다.(불변 객체를 이용해야한다.)
             *  예를 들어 city만 변경해야한다면
             *  member.getWorkAddress().setCity(""); ==> 이런식으로 사용하면 안된다.
             *  아래와 같이 객체 생성 후 새로 넣어줘야한다.
             *  member.setHomeAddress(new Address("city", member.getHomeAddress.getStreet(), member.getHomeAddress.getZipCode()));
             */

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();//영속성 컨텍스트를 종료한다.
        }
        emf.close(); //리소스 릴리즈
    }

}
