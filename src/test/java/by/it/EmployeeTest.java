package by.it;

import by.it.entity.Department;
import by.it.entity.Employee;
import by.it.util.HibernateUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public class EmployeeTest {

    @Before
    public void init() {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        Department d = new Department("D");
        em.persist(d);
        em.persist(new Employee(null, "Yuli", 27, 16000.99, d));
        em.persist(new Employee(null, "Max", 38, 10000, d));
        em.persist(new Employee(null, "Paul", 43, 15000, d));
        em.persist(new Employee(null, "Gleb", 37, 15000, d));
        em.persist(new Employee(null, "Li", 62, 13099, d));
        em.persist(new Employee(null, "Alex", 22, 4500, d));
        em.persist(new Employee(null, null, 18, 300, d));
        em.getTransaction().commit();
        em.clear();
    }

    @Test
    public void getAllEmployee() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        criteria.select(criteria.from(Employee.class));
        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void getEmployeeByNameTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        criteria.select(emp)
                .where(cb.equal(emp.get("name"), "sd"));
        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void greaterTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        criteria.select(emp)
                .where(cb.gt(emp.get("salary"), 10000));
        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void lessTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        criteria.select(emp)
                .where(cb.le(emp.get("age"), 43));
        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void likeTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        criteria.select(emp)
                .where(cb.like(emp.get("name"), "%ul%"));
        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void betweenTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        criteria.select(emp)
                .where(cb.between(emp.get("age"), 35, 50));
        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void isNullTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        criteria.select(emp)
                .where(cb.isNotNull(emp.get("name")));
//              .where(cb.isNull(emp.get("name")));
        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void logicalPredicateTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        Predicate predicate = cb.and(
                cb.like(emp.get("name"), "%ul%"),
                cb.gt(emp.get("age"), 30)
        );
        criteria.select(emp).where(predicate);

        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void logicalDisjunctionPredicateTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        Expression<Integer> age = emp.get("age");
        Predicate predicate = cb.and(
                cb.or(age.in(24, 28, 35),
                        cb.equal(emp.get("name"), "Yulij")),
                cb.like(emp.get("name"), "%ul%"),
                cb.gt(emp.get("age"), 30)
        );
        criteria.select(emp).where(predicate);

        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void orderTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> emp = criteria.from(Employee.class);
        criteria.select(emp).orderBy(
                cb.desc(emp.get("salary")),
                cb.asc(emp.get("name"))
        );

        List<Employee> employees = em.createQuery(criteria).getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void pagingTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        criteria.select(criteria.from(Employee.class));

        TypedQuery<Employee> typedQuery = em.createQuery(criteria);
        typedQuery.setFirstResult(2);
        typedQuery.setMaxResults(2);
        List<Employee> employees = typedQuery.getResultList();

        employees.forEach(System.out::println);
    }

    @Test
    public void countTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        criteria.select(cb.count(criteria.from(Employee.class)));
        long count = em.createQuery(criteria).getSingleResult();
        System.out.println(count);
    }

    @Test
    public void countDistinctTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        criteria.select(cb.countDistinct(criteria.from(Employee.class)));
        long count = em.createQuery(criteria).getSingleResult();
        System.out.println(count);
    }

    @Test
    public void avgTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);
        criteria.select(cb.avg(criteria.from(Employee.class).get("salary")));
        double count = em.createQuery(criteria).getSingleResult();
        System.out.println(count);
    }

    @Test
    public void maxTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);
        criteria.select(cb.max(criteria.from(Employee.class).get("salary")));
        double count = em.createQuery(criteria).getSingleResult();
        System.out.println(count);
    }

    @Test
    public void minTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);
        criteria.select(cb.min(criteria.from(Employee.class).get("age")));
        double count = em.createQuery(criteria).getSingleResult();
        System.out.println(count);
    }

    @Test
    public void sumTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);
        criteria.select(cb.sum(criteria.from(Employee.class).get("salary")));
        Number count = em.createQuery(criteria).getSingleResult();
        System.out.println(count);
    }

    @Test
    public void orderByTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<Employee> employee = criteria.from(Employee.class);

        criteria.groupBy(employee.get("name"));
        criteria.multiselect(employee.get("name"), cb.count(employee));
        criteria.where(cb.equal(employee.get("name"), "Yuli"));

        List<Tuple> tuples = em.createQuery(criteria).getResultList();
        tuples.forEach(t -> {
            String name = (String) t.get(0);
            long count = (long) t.get(1);
            System.out.println("Name:" + name + " count:" + count);
        });
    }

    @AfterClass
    public static void cleanUp() {
        HibernateUtil.closeEMFactory();
    }
}
