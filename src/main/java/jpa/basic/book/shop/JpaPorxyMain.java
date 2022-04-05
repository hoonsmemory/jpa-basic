package jpa.basic.book.shop;

import jpa.basic.book.shop.domain.Book;
import org.hibernate.Hibernate;
import org.hibernate.jpa.internal.PersistenceUnitUtilImpl;

import javax.persistence.*;

public class JpaPorxyMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("이성훈");
            em.persist(book);

            em.flush();
            em.clear();

            /**
             * find() : 데이터베이스를 통해서 실제 엔티티 객체 조회
             * getReference(): 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
             *
             * 프록시 특징 - 1
             * 실제 클래스를 상속 받아서 만들어짐 (하이버네이트가 내부적으로 만든다.)
             * 실제 클래스와 겉 모양이 같다.
             * 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨(이론상)
             * 프록시 객체는 실제 객체의 참조(target)를 보관
             * 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드 호출
             *
             * 실제 Entity(target)의 값 가져오는 순서(초기화 과정)
             * 1. getName() 호출 (프록시에 있는 함수)
             * 2. 진짜의 값을 가져오기 위해 영속성 컨텍스트에 초기화 요청
             * 3. DB 조회
             * 4. 실제 Entity 생성
             * 5. target.getName() (실제 ENtity의 값을 가져온다.)
             *
             * 프록시 특징 - 2
             * 프록시 객체는 처음 사용할 때 한 번만 초기화
             * 프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님,
             * 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
             * 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함 (== 비교 실패, 대신 instance of 사용)
             * 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환 (굳이 다시 가져오면 이점이 없다.)
             * 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때(detach, close, clear), 프록시를 초기화하면 문제 발생
             * (하이버네이트는 org.hibernate.LazyInitializationException 예외를 터트림)
             *
             **** JPA 트랜잭션에서 em.getReference() 후 em.find() 하면 객체는 proxy로 맞춰진다.
             */
            Book refBook = em.getReference(Book.class, book.getId());
            System.out.println(refBook.getClass());
            System.out.println("ID : " + book.getId());
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(refBook));
            System.out.println("===================================");
            /**
             * 프록시 인스턴스의 초기화 여부 확인
             * PersistenceUnitUtil.isLoaded(Object entity)
             *
             * 프록시 클래스 확인 방법
             * entity.getClass().getName() 출력(..javasist.. or HibernateProxy…)
             *
             * 프록시 강제 초기화
             * org.hibernate.Hibernate.initialize(entity);
             * 참고: JPA 표준은 강제 초기화 없음
             * 강제 호출: member.getName()
             */

            // System.out.println("NAME : " + refBook.getName());
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(refBook));
            //프록시 강제 초기화
            Hibernate.initialize(refBook);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();//영속성 컨텍스트를 종료한다.
        }
        emf.close(); //리소스 릴리즈

    }

}
