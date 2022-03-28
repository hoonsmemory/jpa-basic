package jpa.basic.hello;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        /**
         * 주의 사항
         * 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유해야한다.
         * 엔티티 매너지는 쓰레드간에 공유하면 안된다.
         * JPA의 모든 데이터 변경은 트랜잭션 안에서 실행한다.
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        //요청이 올 때 DB 작업이 필요하다면 EntityManager를 통해 수행해야 한다.
        EntityManager em = emf.createEntityManager();
        //JPA의 모든 데이터 변경은 트랜잭션 안에서 이뤄져야한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*
            //저장 명령어 : persist
            Member member = new Member();
            member.setId(3L);
            member.setName("HelloC");
            em.persist(member);
            */
            
            /*
            //가장 단순하게 조회하는 명령어 : find
            Member member = em.find(Member.class, 1L);
            member.setName("HelloJPA"); //커밋 직전 변경이 일어날 경우 업데이트가 적용된다.
            System.out.println("ID : " + member.getId());
            System.out.println("Name : " + member.getName());
            */
            
            //JPQL : 객체 지향 쿼리(SQL과 문법 유사 : SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원)
            List<Member> members = em.createQuery("select m from Member as m", Member.class)
                    //페이징
                    .setFirstResult(0) //가져올 순서
                    .setMaxResults(2) //가져올 개수
                    //리스트 출력
                    .getResultList();

            for(Member member : members) {
                System.out.println("member : " + member.getName());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close(); //리소스 릴리즈

    }
}
