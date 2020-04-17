package features;

import arquillian.AbstractDeliveryTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Et;
import cucumber.api.java.fr.Quand;
import cucumber.runtime.arquillian.CukeSpace;
import fr.unice.polytech.isa.dd.DeliverySchedule;
import fr.unice.polytech.isa.dd.NextDeliveryInterface;
import fr.unice.polytech.isa.dd.entities.Customer;
import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Package;
import fr.unice.polytech.isa.dd.entities.Provider;
import io.cucumber.java8.Fr;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.runner.RunWith;
import utils.MyDate;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(CukeSpace.class)
@CucumberOptions(features = "src/test/resources/features/MakingDeliveriesFR.feature")
@Transactional(TransactionMode.COMMIT)
public class MakingDeliveriesArquillianTest extends AbstractDeliveryTest implements Fr {

    @PersistenceContext private EntityManager entityManager;
    @EJB (name = "delivery-stateless") private NextDeliveryInterface nextDeliveryInterface;
    @EJB(name = "delivery-stateless") private DeliverySchedule deliverySchedule;
    @Inject
    private UserTransaction utx;

    private List<Delivery> deliveries;
    private Customer c = new Customer("Pm", "adresse1");
    private Package package1 = new Package();
    private Provider pro1 = new Provider();

    @After
    public void cleanUp() throws HeuristicRollbackException, HeuristicMixedException, RollbackException, SystemException, NotSupportedException {
        utx.begin();
        Delivery delivery = deliverySchedule.get_deliveries().get(0);
        entityManager.merge(delivery);
        entityManager.remove(delivery);

        package1 = entityManager.merge(package1);
        entityManager.remove(package1);

        pro1 = entityManager.merge(pro1);
        entityManager.remove(pro1);

        c = entityManager.merge(c);
        entityManager.remove(c);

        utx.commit();
    }

    @Quand("^l'employé demande la prochaine livraison$")
    public void lemployer_demande_la_prochaine_livraison()  {

        deliveries = deliverySchedule.get_deliveries();
    }
    @Alors("^Il y a (\\d+) livraisons$")
    public void il_y_a_livraisons(int arg1) throws Exception {
        assertEquals(0,deliveries.size());
        assertNull(nextDeliveryInterface.getNextDelivery());
    }

    @Quand("L'entreprise reçoit une livraison")
    public void lEntrepriseReçoitUneLivraison() {

        entityManager.persist(c);


        pro1.setName("Aug");
        entityManager.persist(pro1);

        package1.setWeight(10.0);
        package1.setProvider(pro1);
        package1.setSecret_number("AXXXX2");
        entityManager.persist(package1);

        Delivery delivery1 = new Delivery();
        delivery1.setCustomer(c);
        delivery1.setPackageDelivered(package1);
        delivery1.setDeliveryDate("18/04/2020");
        entityManager.persist(delivery1);
        MyDate.date_now = "18/04/2020";
    }

    @Et("l'employé regarde la prochaine livraison")
    public void lEmployéRegardeLaProchaineLivraison() {
        deliveries = deliverySchedule.get_deliveries();
    }

    @Alors("Il devrait y avoir (\\d+) livraison")
    public void ilYLivraison(int arg0) {
        assertEquals(arg0,deliveries.size());
    }

    @Et("après il n'y a plus de livraisons")
    public void aprèsIlNYAPlusDeLivraisons() throws Exception {
        assertNotNull(nextDeliveryInterface.getNextDelivery());
    }
}