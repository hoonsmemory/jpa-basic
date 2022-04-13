package jpa.basic.query.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class OthersMain {

    public static void main(String[] args) {
        others();
    }

    public static void others() {
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
             * 문자: HELLO, She’s
             * 숫자: 10L(Long), 10D(Double), 10F(Float)
             * Boolean: TRUE, FALSE
             * ENUM: jpa.basic.query.jpql.MemberType.ADMIN (패키지명 포함)
             * 엔티티 타입: TYPE(m) = Member (상속 관계에서 사용)
             */
            String query = "SELECT m.username " +
                               " , 'HELLO' " +
                               " , true " +
                             "FROM Member m " +
                            "WHERE m.memberType = :memberType";

            List<Object[]> member = em.createQuery(query)
                    .setParameter("memberType", MemberType.ADMIN)
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
