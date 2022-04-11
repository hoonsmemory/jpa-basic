package jpa.basic.query.jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Queue;

public class JPQLMain {

    public static void main(String[] args) {
        basicJPQL();
    }

    public static void basicJPQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("이성훈");
            em.persist(member1);

            em.flush();
            em.clear();

            //반환 타입이 명확하지 않을 때 사용
            TypedQuery<Member> typedQuery = em.createQuery("SELECT m FROM Member m", Member.class);

            //반환 타입이 명확할 때 사용
            Member member = (Member) em.createQuery("SELECT m from Member m where m.username = :username")
                              .setParameter("username", "이성훈")
                              .getSingleResult();

            System.out.println("====================================================================");
            typedQuery.getResultList().forEach(m -> System.out.println(m.getId() + " ," + m.getUsername()));
            System.out.println("====================================================================");

            System.out.println("====================================================================");
            System.out.println(member.getId() + " ,"+ member.getUsername());
            System.out.println("====================================================================");

            /**
             * getResultList()   : 결과가 하나 이상일 때 리시트 반환 (결과가 없으면 빈 리스트 반환)
             * getSingleResult() : 결과가 없을 경우 : NoResultException, 결과가 둘 이상일 경우 : NonUniqueResultException
             */
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
