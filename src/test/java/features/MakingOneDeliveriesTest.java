package features;

import arquillian.AbstractDeliveryTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.fr.Et;
import cucumber.api.java.fr.Quand;
import cucumber.runtime.arquillian.CukeSpace;
import fr.unice.polytech.isa.dd.*;
import fr.unice.polytech.isa.dd.entities.*;
import fr.unice.polytech.isa.dd.entities.Package;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(CukeSpace.class)
@CucumberOptions(features = "src/test/resources/features/MakingOneDeliveriesFR.feature")
@Transactional(TransactionMode.COMMIT)
public class MakingOneDeliveriesTest extends AbstractDeliveryTest implements Fr {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(name = "delivery-stateless") private NextDeliveryInterface nextDeliveryInterface;
    @EJB (name = "delivery-stateless") private DeliveryInterface deliveryInterface;
    @EJB(name = "delivery-stateless") private DeliverySchedule deliverySchedule;
    @EJB(name = "provider-stateless") private ProviderFinder providerFinder;
    @EJB(name = "drone-stateless") private DroneStatusInterface droneStatusInterface;
    @Inject
    private UserTransaction utx;


    private HashMap<Provider, List<Delivery>> providerListHashMap;
    private List<Delivery> deliveries;
    private List<Provider> providers;
    private Customer c = new Customer("Pm", "adresse1");
    private Provider pro1 = new Provider();
    private Package package1 = new Package();
    private Package package2 = new Package();
    private Drone drone = new Drone(12,0,"1");

    @After
    public void cleanUp() throws HeuristicRollbackException, HeuristicMixedException, RollbackException, SystemException, NotSupportedException {
        utx.begin();
        drone = entityManager.merge(drone);
        entityManager.remove(drone);

        int size = deliverySchedule.get_deliveries().size();
        for(int i = 0; i < size; i++ ){
            Delivery delivery = deliverySchedule.get_deliveries().get(0);
            entityManager.merge(delivery);
            entityManager.remove(delivery);
        }
        package1 = entityManager.merge(package1);
        entityManager.remove(package1);

        package2 = entityManager.merge(package2);
        entityManager.remove(package2);

        pro1 = entityManager.merge(pro1);
        entityManager.remove(pro1);

        c = entityManager.merge(c);
        entityManager.remove(c);

        utx.commit();
    }


    @Quand("Lentreprise doit livrer (\\d+) colis dun seul fournisseur de nom (.*)")
    public void lEntrepriseDoitLivrerColisDUnSeulFournisseurDeNom(int arg0, String arg1) {
        entityManager.persist(c);

        drone.addStatut(new DroneStatus(DRONE_STATES.AVAILABLE,"12/12/2020"));
        entityManager.persist(drone);

        pro1.setName(arg1);
        entityManager.persist(pro1);

        package1.setWeight(10.0);
        package1.setProvider(pro1);
        package1.setSecret_number("AXXXX4");
        entityManager.persist(package1);

        package2.setWeight(10.0);
        package2.setProvider(pro1);
        package2.setSecret_number("AXXXX4");
        entityManager.persist(package2);
        pro1.add(package2);

        Delivery delivery1 = new Delivery();
        delivery1.setCustomer(c);
        delivery1.setPackageDelivered(package1);
        delivery1.setDeliveryDate("17/04/2020");
        entityManager.persist(delivery1);

        Delivery delivery2 = new Delivery();
        delivery2.setCustomer(c);
        delivery2.setPackageDelivered(package2);
        delivery2.setDeliveryDate("17/04/2020");
        entityManager.persist(delivery2);
        MyDate.date_now = "17/04/2020";
    }

    @Et("l'employé demande la prochaine livraison à envoyer")
    public void lEmployéDemandeLaProchaineLivraisonÀEnvoyer() throws ParseException {
        deliveries = deliverySchedule.get_deliveries();
        providers = providerFinder.providerList();
        assertNotNull(nextDeliveryInterface.getNextDelivery());
        providerListHashMap = deliveryInterface.getAllDayDeliveries();
        droneStatusInterface.changeStatus(DRONE_STATES.AVAILABLE,drone,"12/04/2020","12h00");
    }

    @Et("après il devrait rester (\\d+) livraison")
    public void aprèsIlDevraitResterLivraison(int arg0) throws ParseException {
        assertNotNull(nextDeliveryInterface.getNextDelivery());
        droneStatusInterface.changeStatus(DRONE_STATES.AVAILABLE,drone,"12/04/2020","16h00");
        assertNull(nextDeliveryInterface.getNextDelivery());
    }

    @Et("le fournisseur a (\\d+) livraison à payer")
    public void leFournisseurALivraisonÀPayer(int arg0) {
        assertEquals(arg0,providerListHashMap.get(providers.get(0)).size());
    }
}
