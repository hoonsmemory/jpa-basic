package jpa.basic.query.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class SubQueryMain {

    public static void main(String[] args) {
        subQuery();
    }

    public static void subQuery() {
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
                member1.setMemberType(MemberType.ADMIN);
                member1.setTeam(team);
                em.persist(member1);

                em.flush();
                em.clear();

                /**
                 * exists, All, ANY 지원
                 * JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
                 * SELECT 절도 가능(하이버네이트에서 지원)
                 * FROM 절의 서브 쿼리는 현재 JPQL에서 불가능
                 * 조인으로 풀 수 있으면 풀어서 해결
                 */
                String query = "select m " +
                                 "from Member m " +
                                "where exists (select t " +
                                                "from m.team t " +
                                               "where t.name = 'teamA') ";

                List<Object[]> member = em.createQuery(query)
                        .getResultList();

                System.out.println("====================================================================");
                member.forEach(m->{System.out.println(m[0] + ", " + m[1] + ", " + m[2]);});
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
