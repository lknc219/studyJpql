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

            String query = "select t from Team t";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for (Team team : resultList) {
                System.out.println("team.getName() = " + team.getName());
                System.out.println("team.getMembers() = " + team.getMembers().size());
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
