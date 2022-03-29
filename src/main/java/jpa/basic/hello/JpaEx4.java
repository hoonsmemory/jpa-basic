package jpa.basic.hello;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaEx4 {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = em.find(Member.class, 150L);
            member.setName("AAAAA");

            em.detach(member); //해당 객체만 JPA의 관리 대상에서 빠진다. (준영속 상태)
            em.clear(); //EntityManager에 있는 영속성 컨텍스트를 완전히 초기화 한다.

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();//영속성 컨텍스트를 종료한다.
        }
        emf.close(); //리소스 릴리즈

    }

}
