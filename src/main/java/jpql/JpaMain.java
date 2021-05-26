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
            Team team = new Team();
            team.setName("A");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            member.changeTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자");
            member2.setAge(28);
            member2.changeTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();
            // 경로 표현시 .(점)을 찍어 그래프를 탐색하는 것
            // 상태필드는 더 이상 경로탐색이 안됨 상태필드 (m.username)
            // 묵시적 조인은 사용하지 말고 명시적 조인만 사용할 것
            // String query = "select m.team From Member m";
            // String query = "select t.members.size From Team t";
            String query = "select m.username From Team t join t.members m,";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();
            System.out.println("result = " + resultList);

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
