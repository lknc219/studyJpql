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

//            String query = "select 'a' || 'b' from Member m";
//            String query = "select concat('a','b') from Member m";
//            String query = "select substring(m.username,2,3) from Member m";
//            String query = "select locate('de','abcdefg') from Member m"; // locate의 시작은 1부터, 없으면 0반환
//            String query = "select size(t.members) From Team t";

//            String query = "select function('group_concat', m.username) From Member m";
            String query = "select group_concat(m.username) From Member m";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList) {
                System.out.println("이름 = " + s);
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
