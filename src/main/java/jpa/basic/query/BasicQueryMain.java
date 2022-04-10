package jpa.basic.query;

import jpa.basic.book.shop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class BasicQueryMain {

    public static void main(String[] args) {
        //JPQL();
        //Criteria();
    }

    /**
     * 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
     * SQL을 추상화해서 특정 데이터베이스 SQL에 의존X
     * JPQL을 한마디로 정의하면 객체 지향 SQL
     */
    public static void JPQL() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            String jpql = "select m From Member m";
            List<Member> result = em.createQuery(jpql, Member.class)
                    .getResultList();

            result.forEach(member -> System.out.println(member.getId() + ", " + member.getName()));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /**
     * 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
     * 동적쿼리로 만들기 수월하다.
     * JPQL 빌더 역할
     * JPA 공식 기능
     * 단점: 너무 복잡하고 실용성이 없다.
     * Criteria 대신에 QueryDSL 사용 권장
     */
    public static void Criteria() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //Criteria 사용 준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            //루트 클래스 (조회를 시작할 클래스)
            Root<Member> m = query.from(Member.class);

            //쿼리 생성
            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "이"));
            List<Member> resultList = em.createQuery(cq).getResultList();

            resultList.forEach(member -> System.out.println(member.getId() + ", " + member.getName()));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /**
     * 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
     * JPQL 빌더 역할
     * 컴파일 시점에 문법 오류를 찾을 수 있음
     * 동적쿼리 작성 편리함
     * 단순하고 쉬움
     * 실무 사용 권장
     */
    public static void QueryDSL() {
        //JPQL
        //select m from Member m where m.age > 18
        /*
        JPAFactoryQuery query = new JPAQueryFactory(em);
        QMember m = QMember.member;
        List<Member> list =
                query.selectFrom(m)
                        .where(m.age.gt(18))
                        .orderBy(m.name.desc())
                        .fetch();
         */
    }

    /**
     * 네이티브 SQL
     * JPA가 제공하는 SQL을 직접 사용하는 기능
     * JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능
     * 예) 오라클 CONNECT BY, 특정 DB만 사용하는 SQL 힌트
     * String sql = "SELECT ID, AGE, TEAM_ID, NAME FROM MEMBER WHERE NAME = '이성훈'";
     * List<Member> resultList = em.createNativeQuery(sql, Member.class).getResultList();
     *
     *
     * JDBC 직접 사용, SpringJdbcTemplate 등
     * JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, 마이바티스등을 함께 사용 가능
     * 단 영속성 컨텍스트를 적절한 시점에 강제로 플러시 필요
     * 예) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트 수동 플러시
     */
}
