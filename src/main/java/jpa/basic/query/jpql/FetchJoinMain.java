package jpa.basic.query.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 페치 조인
 * 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능이다. (지연 로딩 X, 즉시 로딩)
 * SQL 조인 종류가 아니며 JPQL에서 성능 최적화를 위해 제공하는 기술이다.
 * JOIN 뒤에 FETCH를 입력하면 끝
 *
 * 페치 조인과 일반 조인의 차이
 * 일반 조인 실행 시 연관된 엔티티를 함께 조회하지 않는다. (조인에 members를 넣었어도 team만 조회됨..)
 * JPQL은 결과를 반환할 때 연관관계를 고려하지 않는다. (단지 SELECT절에 지정한 엔티티만 조회한다.)
 *
 * 페치 조인의 특징과 한계
 * 페치 조인 대상에는 별칭을 줄 수 없다. (하이버네이트는 가능)
 * 둘 이상의 컬렉션은 페치 조인 할 수 없다.
 * 컬렉션을 페치 조인하면 페이징 API를 사용할 수 없다. (다대일일 경우 데이터 뻥튀기 때문)
 * (일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징이 가능하다.)
 * 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함 (@OneToMany(fetch=FetchType.LAZY)
 * 실무에서 글로벌 로딩 전략은 모두 지연 로딩
 * 최적화가 필요한 곳은 페치 조인 적용( N + 1 문제가 발생되는 곳)
 *
 * 페치 조인 - 정리
 * 모든 것을 페치 조인으로 해결할 수는 없음
 * 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
 * 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,
 * 페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
 */
public class FetchJoinMain {

    public static void main(String[] args) {
        fetchJoin();
    }

    public static void fetchJoin() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("이성훈");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("에밀리");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("홍길동");
            member3.setTeam(teamB);
            em.persist(member3);
            
            em.flush();
            em.clear();

            /**
             * fetch를 입력 안할 경우 쿼리가 3번 실행된다. (N+1 문제..)
             * 1. 멤버 쿼리
             * 2. 팀A 쿼리
             * 3. 팀B 쿼리
             */
            String query = "SELECT m " +
                    "FROM Member m " +
                    "INNER JOIN fetch m.team";
            List<Member> result1 = em.createQuery(query, Member.class)
                    .getResultList();

            System.out.println("====================================================================");
            result1.forEach(m->{System.out.println(m.getId() + " ," + m.getUsername() + " ," + m.getTeam().getName());});
            System.out.println("====================================================================");

            em.flush();
            em.clear();

            /**
             * 일대다일 경우 쿼리 값이 뻥튀기가 된다.
             * DISTINCT로 중복된 결과를 제거할 수 있다.
             * (SQL에서 DISTINCT는 모든 값이 같아야 중복제거가 되지만
             * JPA에서는 같은 식별자를 가진 엔티티를 제거한다.)
             */
            String query2 = "SELECT DISTINCT t " +
                    "FROM Team t " +
                    "JOIN fetch t.members";

            List<Team> result2 = em.createQuery(query2, Team.class)
                    .getResultList();

            System.out.println("====================================================================");
            result2.forEach(t->{System.out.println("team : " + t.getName() + "|members : " + t.getMembers().size());});
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
