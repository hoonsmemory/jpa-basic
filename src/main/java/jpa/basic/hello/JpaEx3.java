package jpa.basic.hello;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaEx3 {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /**
             * JPA의 목적은 객체를 마치 컬렉션 다루듯이 하는게 목적이다.
             * 값을 꺼내거나 변경을 하면 em.persist(member);를 사용할 필요가 없다.
             * JPA에서는 변경 감지라는 기능이 제공된다. (Dirty Checking)
             *
             * 커밋을 하는 시점
             * 1. 내부적으로 flush() 기능을 호출
             * 2. 엔티티(1차 캐싱된 값)와 스냅샷(해당 값이 최초로 영속 컨텍스트에 들어온 시점)을 비교
             * 3. 변경된 값에 한해서 UPDATE SQL 생성 후 쓰기 지연 SQL 저장소에 저장
             * 4. DB에 반영
             * 5. commit
             */
            Member member = em.find(Member.class, 150L);
            member.setName("ZZZZZ");

            System.out.println("=======================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close(); //리소스 릴리즈

    }

}
