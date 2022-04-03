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
 * 둘 중 하나로 외래 키를 관리해야 한다.(연관관계의 주인(Owner))
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
 * 
 * 단 순수한 객체 관계를 고려하면 항상 양쪽 다 같은 값을 입력해야한다.
 * 아래 [예시1] 참조
 *
 * 끝으로..
 * 단방향 매핑만으로도 이미 연관관계 매핑은 완료되었다.
 * (양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능이 추가된 것 뿐)
 * 단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도 된다.
 * (테이블에 영향을 주지 않음)
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

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(team1); // 단방향 연관관계 설정, 참조 저장
            em.persist(member2);

            /**
             * [예시 1]
             * 현재 위와 같이 저장 후 1차 캐시(영속성 컨텍스트애 저장된 값)를 이용해 팀의 멤버스를 조회할 경우 값을 가져올 수 없다.
             * (영속성 컨텍스트를 클리어(em.flush(), em.clear()) 후 DB에서 저장된 값을 불러 올 경우는 가져올 수 있다.)
             * 따라서 멤버를 저장할 때 member2.setTeam()만 해주는게 아니라
             * 멤버 저장 후 team1.getMembers().add()에다가 값을 넣어줘야 한다.
             * team1.getMembers().add(member1);
             * team1.getMembers().add(member2);
             *
             * 연관관계 편의 메소드
             * 위와 같이 일일이 team1.getMembers().add()해주기 귀찮으니
             * member2.setTeam()할 때 해당 메소드 안에서 team.getMembers().add(this)를 해주는 방법이 있다.
             */


            Team findTeam = em.find(Team.class, team1.getId());

            for(Member member : findTeam.getMembers()) {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ " + member.getId() + " : " + member.getUsername());
            }


            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();//영속성 컨텍스트를 종료한다.
        }
        emf.close(); //리소스 릴리즈

    }

}
