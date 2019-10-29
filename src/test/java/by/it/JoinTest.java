package by.it;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.junit.Before;
import org.junit.Test;

import by.it.entity.Department;
import by.it.entity.Employee;
import by.it.util.HibernateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class JoinTest {

    @Before
    public void init() {
        Department developer = new Department("Developer");
        Department hr = new Department("HR");
        Department qa = new Department("QA");
        developer.getEmployees().add(new Employee(null, "Yuli", 27, 16000.99, developer));
        developer.getEmployees().add(new Employee(null, "Max", 38, 10000, developer));
        developer.getEmployees().add(new Employee(null, "Paul", 43, 15000, developer));
        qa.getEmployees().add(new Employee(null, "Gleb", 37, 15000, qa));
        qa.getEmployees().add(new Employee(null, "Li", 62, 13099, qa));
        hr.getEmployees().add(new Employee(null, "Alex", 22, 4500, hr));

        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(developer);
        em.persist(qa);
        em.persist(hr);
        em.getTransaction().commit();
        em.clear();
        em.close();
    }

    @Test
    public void joinTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Department> criteria = cb.createQuery(Department.class);
        Root<Department> department = criteria.from(Department.class);

        Join<Department, Employee> employeeJoin = department.join("employees", JoinType.INNER);

        criteria.where(cb.equal(employeeJoin.get("name"), "Yuli"));
        List<Department> departments = em.createQuery(criteria).getResultList();
        departments.forEach(System.out::println);
    }

    @Test
    public void fetchTest() {
        Integer age = 40;
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> employee = criteria.from(Employee.class);
        employee.fetch("department");
        ParameterExpression<Integer> ageParameter = cb.parameter( Integer.class );
        criteria.where(cb.gt(employee.get("age"), ageParameter));
        List<Employee> employees = em.createQuery(criteria)
                .setParameter(ageParameter, age)
                .getResultList();
        employees.forEach(System.out::println);
    }

    @Test
    public void nativeQueryTest() {
        EntityManager em = HibernateUtil.getEntityManager();
        Session unwrap = em.unwrap(Session.class);
        List<EmpWrapper> employees = unwrap.createNativeQuery("SELECT e.id as id, e.age as age FROM Employee e WHERE name = 'Paul'")
                .setResultTransformer(Transformers.aliasToBean(EmpWrapper.class))
                .getResultList();
        employees.forEach(System.out::println);
    }

    public static class EmpWrapper {
         public Integer ID;
         public Integer AGE;
    }
}
