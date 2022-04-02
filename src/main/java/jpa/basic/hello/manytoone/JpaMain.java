package jpa.basic.hello.manytoone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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

            /**
             * 일반적으로 영속성 컨텍스트에 있는 1차 캐시를 통해 조회가 되므로 DB에서 가져올 필요가 없다.
             * 다만 조회 전  영속성 컨텍스트를 날린 후 조회를 할 경우 DB에서 직접 데이터를 가져온다.
             * em.flush();
             * em.clear();
             */
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
