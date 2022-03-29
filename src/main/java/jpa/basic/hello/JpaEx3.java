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
             *
             * fulsh란
             * 영속성 컨텍스트의 변경내용을 데이터베이스에 반영
             *
             * flush 발생
             * 1. 변경 감지
             * 2. 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
             * 3. 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송(등록, 수정, 삭제 쿼리)
             *
             * flush 방법
             * 1. 직접 호출 : em.flush(); //1차 캐시에 있는 내용이 지워지진 않고 쓰기 지연 SQL 저장소에 있는 데이터만 저장이 된다.
             * 2. 트랜잭션 커밋 : tx.commit();
             * 3. 플러시 자동 호출 : JPQL 쿼리 실행 // 전에 persist한 경우 그 값을 못가져 왔을 때 문제가 생길 수 있으므로..
             *
             * flush 모드 옵션
             * FlushModeType.AUTO : 커밋이나 쿼리를 실행 때 플러시(기본값)
             * FlushModeType.COMMIT : 커밋할 때만 플러시
             *
             * fulsh는 영속성 컨텍스트를 비우지 않는다.
             * 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화한다.
             * 트랜잭션이라는 작업 단위가 중요하다 -> 커밋 직전에만 동기화하면 됨
             */
            Member member = em.find(Member.class, 150L);
            member.setName("ZZZZZ");

            Member member2 = em.find(Member.class, 150L);
            em.remove(member2);
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
