package jpa.basic.hello.bothway;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 객체의 양방향 관계는 서로 다른 단방향 관계가 2개다.
 * MEMBER -> TEAM
 * TEAM -> MEMBER
 *
 * 즉 객체를 양방향으로 참조하려면 단방향 연관관계를 2개 만들어야 하며,
 * 둘 중 하나로 외래 키를 관리해야 한다.(연관관계의 주인(Owner)
 *
 * 양방향 매핑 규칙
 * 객체의 두 관계 중 하나를 연관관계의 주인으로 지정
 * 연관관계의 주인만이 외래 키를 관리(등록, 수정)
 * 주인이 아닌쪽은 읽기만 가능
 * 주인은 mappedBy 속성 사용 X
 * 주인이 아니면 mappedBy 속성으로 주인 지정
 * 
 * 그렇다면 누구를 주인으로?
 * 외래키가 있는 곳을 주인으로 정해라
 */
public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //팀 저장
            Team team1 = new Team();
            team1.setName("TeamA");
            em.persist(team1);

            //멤버 저장
            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(team1); // 단방향 연관관계 설정, 참조 저장
            em.persist(member1);

            Member findMember = em.find(Member.class, member1.getId());
            Team findTeam = findMember.getTeam();
            System.out.println("Team : " + findTeam.getName());

            /**
             * 연관관계 수정
             */
            //팀 저장
            Team team2 = new Team();
            team2.setName("TeamB");
            em.persist(team2);

            findMember.setTeam(team2);
            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();//영속성 컨텍스트를 종료한다.
        }
        emf.close(); //리소스 릴리즈

    }

}
