package by.it;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;

import by.it.entity.Hero;
import by.it.entity.Power;
import by.it.util.HibernateUtil;

public class DetachedQueriesTest {

    @Before
    public void init() {
        Hero archer = new Hero("Archer");
        archer.getPowers().add(new Power("Accuracy", 24));
        archer.getPowers().add(new Power("Durability", 16));
        Hero mage = new Hero("Mage");
        mage.getPowers().add(new Power("Ice storm", 95));
        mage.getPowers().add(new Power("Armageddon", 385));
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(archer);
        em.persist(mage);
        em.getTransaction().commit();
        em.clear();
    }

    @Test
    public void getHeroByPowerNameTest() {
        Session session = HibernateUtil.getSession();
        DetachedCriteria criteria = DetachedCriteria.forClass(Hero.class);
        DetachedCriteria power = criteria.createCriteria("powers");
        power.add(Restrictions.eq("name", "Armageddon"));
        power.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        Hero hero = (Hero) power.getExecutableCriteria(session).uniqueResult();
        if (hero != null) {
            System.out.println(hero);
        }
    }

}
