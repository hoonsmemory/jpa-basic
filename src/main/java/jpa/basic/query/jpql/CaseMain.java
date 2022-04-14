package jpa.basic.query.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class CaseMain {

    public static void main(String[] args) {
        caseJPQL();
    }

    public static void caseJPQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("이성훈");
            member1.setAge(30);
            member1.setTeam(team);
            em.persist(member1);

            String query = "select " +
                             "case when m.age <= 10 then '학생요금' " +
                                  "when m.age >= 60 then '경로요금' " +
                                                   "else '일반요금' " +
                             "end " +
                             "from Member m";

            List<String> member = em.createQuery(query, String.class)
                    .getResultList();

            System.out.println("######################################################");
            member.forEach(m->{System.out.println(m);});
            System.out.println("######################################################");

            /**
             * COALESCE: 하나씩 조회해서 null이 아니면 반환
             * NULLIF: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
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
