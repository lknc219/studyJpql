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
            member2.setUsername("A");
            member2.setAge(28);
            member2.changeTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();
            //Enum 타입은 패키지 경로도 다 입력해줘야함
            //아니면 setParameter 에 셋팅
            String query = "select m.username, 'HELLO', TRUE from Member m where " +
                    "m.type = :userType";
            List<Object[]> resultList = em.createQuery(query)
                    .setParameter("userType",MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : resultList) {
                System.out.println("o[0] = " + objects[0]);
                System.out.println("o[0] = " + objects[1]);
                System.out.println("o[0] = " + objects[2]);
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
