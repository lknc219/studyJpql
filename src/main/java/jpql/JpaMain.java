package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();//디비커넥션 하나 받았다고 생각하면됨

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team team = new Team();
            team.setName("namChulTeam");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("namChul");
            member2.setAge(28);
            member2.changeTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select m from Member m inner join m.team t";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .setFirstResult(0)  //(페이지값-1)*10 넣으면 10개씩 보여주는 페이지 될 듯
                    .setMaxResults(10)
                    .getResultList();

            for (Member members : resultList) {
                System.out.println("member = " + members.toString());
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
