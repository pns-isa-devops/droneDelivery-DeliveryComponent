package features;

import arquillian.AbstractDeliveryTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.fr.Alors;
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
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(CukeSpace.class)
@CucumberOptions(features = "src/test/resources/features/MakingTwoDeliveriesFR.feature")
@Transactional(TransactionMode.COMMIT)
public class MakingTwoDeliveriesArquillianTest extends AbstractDeliveryTest implements Fr {

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
    private Package package1 = new Package();
    private Package package2 = new Package();
    private Package package3 = new Package();
    private Package package4 = new Package();
    private Drone drone1 = new Drone("1");
    private Drone drone2 = new Drone("2");

    @After
    public void cleanUp() throws HeuristicRollbackException, HeuristicMixedException, RollbackException, SystemException, NotSupportedException {
        utx.begin();

        drone1 = entityManager.merge(drone1);
        entityManager.remove(drone1);
        drone2 = entityManager.merge(drone2);
        entityManager.remove(drone2);

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

        package3 = entityManager.merge(package3);
        entityManager.remove(package3);

        package4 = entityManager.merge(package4);
        entityManager.remove(package4);

        int sizep = providerFinder.providerList().size();
        for(int i = 0; i < sizep; i++ ){
            Provider provider = providerFinder.providerList().get(0);
            entityManager.merge(provider);
            entityManager.remove(provider);
        }
        c = entityManager.merge(c);
        entityManager.remove(c);

        utx.commit();
    }

    @Quand("Lentreprise doit livrer (\\d+) colis de 2 fournisseurs de noms (.*) et (.*)")
    public void lentrepriseDoitLivrerColisDeFournisseursDeNomsAGEtPK(int arg0,String arg1,String arg2) {

        entityManager.persist(c);

        drone1.addStatut(new DroneStatus(DRONE_STATES.AVAILABLE,"12/12/2020"));
        entityManager.persist(drone1);
        drone2.addStatut(new DroneStatus(DRONE_STATES.AVAILABLE,"12/12/2020"));
        entityManager.persist(drone2);

        Provider pro1 = new Provider();
        pro1.setName(arg1);
        entityManager.persist(pro1);

        Provider pro2 = new Provider();
        pro2.setName(arg2);
        entityManager.persist(pro2);

        package1 = new Package();
        package1.setWeight(10.0);
        package1.setProvider(pro1);
        package1.setSecret_number("AXXXX8");
        entityManager.persist(package1);

        package2.setWeight(15.0);
        package2.setProvider(pro1);
        package2.setSecret_number("AXXX45");
        entityManager.persist(package2);
        pro1.add(package1);
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

        package3.setWeight(20.0);
        package3.setProvider(pro2);
        package3.setSecret_number("AXXyX2");
        entityManager.persist(package3);

        package4.setWeight(25.0);
        package4.setProvider(pro2);
        package4.setSecret_number("AXXXU2");
        entityManager.persist(package4);
        pro2.add(package3);
        pro2.add(package4);

        Delivery delivery3 = new Delivery();
        delivery3.setCustomer(c);
        delivery3.setPackageDelivered(package3);
        delivery3.setDeliveryDate("17/04/2020");
        entityManager.persist(delivery3);

        Delivery delivery4 = new Delivery();
        delivery4.setCustomer(c);
        delivery4.setPackageDelivered(package4);
        delivery4.setDeliveryDate("17/04/2020");
        entityManager.persist(delivery4);
        MyDate.date_now = "17/04/2020";
    }

    @Et("l'employé effectue les (\\d+) livraison de AG et une livraison de PK")
    public void lEmployéEffectueLesLivraisonDeAGEtUneLivraisonDePK(int arg0) throws Exception {
        deliveries = deliverySchedule.get_deliveries();
        providers = providerFinder.providerList();
        nextDeliveryInterface.getNextDelivery();
        nextDeliveryInterface.getNextDelivery();
        droneStatusInterface.changeStatus(DRONE_STATES.AVAILABLE,drone1,"17/04/2020","14h00");
        nextDeliveryInterface.getNextDelivery();

        providerListHashMap = deliveryInterface.getAllDayDeliveries();
    }

    @Alors("(.*) devra devra payer (\\d+) livraisons pour cette journée")
    public void agDevraDevraPayerLivraisonsPourCetteJournée(String arg0,int arg1) {
        assertEquals(arg1,providerListHashMap.get(providers.get(0)).size());
    }

    @Et("(.*) devra en payer (\\d+)")
    public void pkDevraEnPayer(String arg0,int arg1) {
        assertEquals(arg1,providerListHashMap.get(providers.get(1)).size());
    }
}
