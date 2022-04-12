package jpa.basic.query.jpql;

import javax.persistence.*;
import java.util.List;

public class JoinMain {

    public static void main(String[] args) {
        basicJPQL();
    }

    public static void basicJPQL() {
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
            member1.setTeam(team);
            em.persist(member1);

            em.flush();
            em.clear();

            String query = "SELECT m " +
                             "FROM Member m " +
                            "INNER JOIN m.team t";
            List<Member> member = em.createQuery(query, Member.class)
                    .setFirstResult(0)
                    .setMaxResults(5)
                    .getResultList();

            System.out.println("====================================================================");
            member.forEach(m->{System.out.println(m.getId() + " ," + m.getUsername() + " ," + m.getTeam().getName());});
            System.out.println("====================================================================");

            /**
             * ON절을 활용한 조인(JPA 2.1부터 지원)
             * 1. 조인 대상 필터링
             * 2. 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터)
             */

            String query2 = "SELECT m, t " +
                              "FROM Member m " +
                              "LEFT JOIN m.team t " +
                                "ON t.name = '이성훈'";
            List<Member> member2 = em.createQuery(query2, Member.class)
                    .getResultList();

            System.out.println("====================================================================");
            member2.forEach(m->{System.out.println(m.getId() + " ," + m.getUsername() + " ," + m.getTeam().getName());});
            System.out.println("====================================================================");


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
