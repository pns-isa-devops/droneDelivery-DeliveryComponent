package fr.unice.polytech.isa.dd.business;

import arquillian.AbstractDeliveryTest;
import cucumber.api.CucumberOptions;
import cucumber.runtime.arquillian.CukeSpace;
import fr.unice.polytech.isa.dd.*;
import fr.unice.polytech.isa.dd.entities.*;
import fr.unice.polytech.isa.dd.entities.Package;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import utils.MyDate;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(Arquillian.cl
@Transactional(TransactionMode.COMMIT)
public class DeliveryBeanTest extends AbstractDeliveryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(name = "delivery-stateless") private DeliverySchedule deliverySchedule;
    @EJB(name = "delivery-stateless") private NextDeliveryInterface nextDeliveryInterface;
    @EJB(name = "drone-stateless") private DroneStatusInterface droneStatusInterface;
    @Inject
    private UserTransaction utx;

    private Customer c;
    private Package package1;
    private Provider pro1;
    private Delivery delivery1;
    private Delivery delivery2;
    private Drone drone = new Drone("1");

    @After
    public void cleanUp() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        utx.begin();

        drone = entityManager.merge(drone);
        entityManager.remove(drone);

        delivery1 = entityManager.merge(delivery1);
        entityManager.remove(delivery1);
        delivery2 = entityManager.merge(delivery2);
        entityManager.remove(delivery2);

        package1 = entityManager.merge(package1);
        entityManager.remove(package1);

        pro1 = entityManager.merge(pro1);
        entityManager.remove(pro1);

        c = entityManager.merge(c);
        entityManager.remove(c);

        MyDate.date_now = "17/04/2020";
        utx.commit();
    }

    @Test
    public void tesst_with_date() throws Exception {

        drone.addStatut(new DroneStatus(DRONE_STATES.AVAILABLE,"12/12/2020"));
        entityManager.persist(drone);

        MyDate.date_now = "17/04/2020";

        c = new Customer("Pm", "adresse1");
        entityManager.persist(c);

        pro1 = new Provider();
        pro1.setName("Aug");
        entityManager.persist(pro1);

        package1 = new Package();
        package1.setWeight(10.0);
        package1.setProvider(pro1);
        package1.setSecret_number("AXXXX2");
        entityManager.persist(package1);

        delivery1 = new Delivery();
        delivery1.setCustomer(c);
        delivery1.setPackageDelivered(package1);
        delivery1.setDeliveryDate("17/04/2020");
        entityManager.persist(delivery1);

        delivery2 = new Delivery();
        delivery2.setCustomer(c);
        delivery2.setPackageDelivered(package1);
        delivery2.setDeliveryDate("18/04/2020");
        entityManager.persist(delivery2);

        assertEquals(2,deliverySchedule.get_deliveries().size());
        assertEquals("17/04/2020",nextDeliveryInterface.getNextDelivery().getDeliveryDate());

        droneStatusInterface.changeStatus(DRONE_STATES.AVAILABLE,drone,"12/04/2020","12h00");

        droneStatusInterface.UpdtateAttributsDrone(drone,11.7,"14/04/2020","10h00");

        MyDate.date_now = "18/04/2020";
        assertNull("18/04/2020",nextDeliveryInterface.getNextDelivery());

        droneStatusInterface.changeStatus(DRONE_STATES.AVAILABLE,drone,"12/04/2020","12h00");
        assertEquals("18/04/2020",nextDeliveryInterface.getNextDelivery().getDeliveryDate());
    }
}