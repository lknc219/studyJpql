package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();//디비커넥션 하나 받았다고 생각하면됨

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Team team3 = new Team();
            team3.setName("팀C");
            em.persist(team3);


            Member member = new Member();
            member.setUsername("회원1");
            member.setAge(10);
            member.changeTeam(team1);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(28);
            member2.changeTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(30);
            member3.changeTeam(team2);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("회원4");
            member4.setAge(21);
            em.persist(member4);

            em.flush();
            em.clear();
            // 페치 조인(fetch join) 실무에서 중요
            // sql 종류가 아니고 jpql 성능최적화를 위해 제공
            // sql을 한 번에 조회 가능
            // join fetch 명령어 사용
            // 즉시로딩을 명시적으로 표현하는것과 비슷함
            // 지연로딩으로 새로운 데이터를 가져올때마다 쿼리를 날리기 때문에 쿼리가 너무 많이 사용됨(fetch 사용x 일때)
            // fetch : 조인해서 필요한 데이터를 한 번에 모두 가져온다.
            // String query = "select m from Member m";
            String query = "select m from Member m join fetch m.team t";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member1 : resultList) {
                System.out.println("member1.getUsername() = " + member1.getUsername());
                System.out.println("member1.getTeam().getName() = " + member1.getTeam().getName());
            }

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }


}
