package features;

import arquillian.AbstractDeliveryTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Et;
import cucumber.api.java.fr.Quand;
import cucumber.runtime.arquillian.CukeSpace;
import cucumber.runtime.arquillian.api.Features;
import fr.unice.polytech.isa.dd.DeliveryBean;
import fr.unice.polytech.isa.dd.DeliveryInterface;
import fr.unice.polytech.isa.dd.DeliverySchedule;
import fr.unice.polytech.isa.dd.NextDeliveryInterface;
import fr.unice.polytech.isa.dd.entities.Customer;
import fr.unice.polytech.isa.dd.entities.Database;
import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Package;
import fr.unice.polytech.isa.dd.entities.Provider;
import io.cucumber.java8.Fr;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(CukeSpace.class)
@CucumberOptions(features = "src/test/resources/")
@Transactional(TransactionMode.COMMIT)
public class MakingDeliveriesArquillianTest extends AbstractDeliveryTest implements Fr {

    @PersistenceContext private EntityManager entityManager;
    @EJB (name = "delivery-stateless") private NextDeliveryInterface nextDeliveryInterface;
    @EJB (name = "delivery-stateless") private DeliveryInterface deliveryInterface;
    @EJB(name = "delivery-stateless") private DeliverySchedule deliverySchedule;

    private HashMap<fr.unice.polytech.isa.dd.entities.Provider,List<Delivery>> providerListHashMap;
    private List<Delivery> deliveries;
    private List<Provider> providers;
    //private List<Delivery> deliveries1;

/*
    private List<Provider> providers = Database.getInstance().getProviderList();
    private List<Delivery> delivs = Database.getInstance().getDeliveryList();
    private Delivery delivery;
    private HashMap<Provider,List<Delivery>> providerListHashMap;
*/
/*
    public void initializeDatabaseDeliveryTestWithParam(int arg1, String arg2) {
        Customer c = new Customer("Pm", "adresse1");
        entityManager.persist(c);

        Provider pro1 = new Provider();
        pro1.setName(arg2);
        entityManager.persist(pro1);

        Package package1 = new Package();
        package1.setWeight(10.0);
        package1.setProvider(pro1);
        entityManager.persist(package1);

        Package package2 = new Package();
        package2.setWeight(10.0);
        package2.setProvider(pro1);
        entityManager.persist(package2);
        pro1.add(package2);

        Delivery delivery1 = new Delivery();
        delivery1.setCustomer(c);
        delivery1.setPackageDelivered(package1);
        entityManager.persist(delivery1);

        Delivery delivery2 = new Delivery();
        delivery2.setCustomer(c);
        delivery2.setPackageDelivered(package2);
        entityManager.persist(delivery2);

    }/*
    public void initializeDatabaseDeliveryTestWithMutipleProviders(int arg1, String arg2,String arg3) {
        Customer c = new Customer("Pm", "adresse1");

        DateTime dt = new DateTime();

        Provider pro1 = new Provider("1", arg2);
        Provider pro2 = new Provider("2",arg3);
        providers.add(pro1);providers.add(pro2);

        for (int i = 0; i < arg1; i++){
            delivs.add(new Delivery(c,new Package(""+i,10.0,dt,pro1.getId()),dt,null));
        }
        for (int i = 0; i < arg1; i++){
            delivs.add(new Delivery(c,new Package(""+i*2,10.0,dt,pro2.getId()),dt,null));
        }
    }

    public void cleanDatabase(){
        for(Iterator<Provider> itpor = providers.iterator(); itpor.hasNext();){
            itpor.next();
            itpor.remove();
        }
        for(Iterator<Delivery> itdel = delivs.iterator();itdel.hasNext();){
            itdel.next();
            itdel.remove();
        }
    }*/

    @Quand("^l'employé demande la prochaine livraison$")
    public void lemployer_demande_la_prochaine_livraison()  {

        deliveries = deliverySchedule.get_deliveries();
    }
    @Alors("^Il y a (\\d+) livraisons$")
    public void il_y_a_livraisons(int arg1) {
//        Database.getInstance().clearDatabase();
        //nextDeliveryInterface = new DeliveryBean();
        assertEquals(0,deliveries.size());
        assertNull(nextDeliveryInterface.getNextDelivery());
    }

    @Quand("L'entreprise reçoit une livraison")
    public void lEntrepriseReçoitUneLivraison() {
        //initializeDatabaseDeliveryTest();
        //delivs =  databaseTest.getDeliveryList();
        Customer c = new Customer("Pm", "adresse1");
        entityManager.persist(c);

        Provider pro1 = new Provider();
        pro1.setName("Aug");
        entityManager.persist(pro1);

        Package package1 = new Package();
        package1.setWeight(10.0);
        package1.setProvider(pro1);
        package1.setSecret_number("AXXXX2");
        entityManager.persist(package1);

        Delivery delivery1 = new Delivery();
        delivery1.setCustomer(c);
        delivery1.setPackageDelivered(package1);
        entityManager.persist(delivery1);
    }

    @Et("l'employé regarde la prochaine livraison")
    public void lEmployéRegardeLaProchaineLivraison() {
        //delivery = nextDeliveryInterface.getNextDelivery();
        deliveries = deliverySchedule.get_deliveries();

    }

    @Alors("Il devrait y avoir (\\d+) livraison")
    public void ilYLivraison(int arg0) {
       // assertNotNull(delivery);
        // deliveries1 = deliverySchedule.get_deliveries();
        assertEquals(arg0,deliveries.size());

    }

    @Et("après il n'y a plus de livraisons")
    public void aprèsIlNYAPlusDeLivraisons() {
        //assertNull(nextDeliveryInterface.getNextDelivery());
        //cleanDatabase();
        assertNotNull(nextDeliveryInterface.getNextDelivery());
    }

    @Quand("Lentreprise doit livrer (\\d+) colis dun seul fournisseur de nom (.*)")
    public void lEntrepriseDoitLivrerColisDUnSeulFournisseurDeNom(int arg0, String arg1) {
       // initializeDatabaseDeliveryTestWithParam(arg0,arg1);
       // delivs = databaseTest.getDeliveryList();
       // providers = databaseTest.getProviderList();
        //nextDeliveryInterface = new DeliveryBean();
        //deliveryInterface = new DeliveryBean();
        Customer c = new Customer("Pm", "adresse1");
        entityManager.persist(c);

        Provider pro1 = new Provider();
        pro1.setName(arg1);
        entityManager.persist(pro1);

        Package package1 = new Package();
        package1.setWeight(10.0);
        package1.setProvider(pro1);
        package1.setSecret_number("AXXXX4");
        entityManager.persist(package1);

        Package package2 = new Package();
        package2.setWeight(10.0);
        package2.setProvider(pro1);
        package2.setSecret_number("AXXXX4");
        entityManager.persist(package2);
        pro1.add(package2);

        Delivery delivery1 = new Delivery();
        delivery1.setCustomer(c);
        delivery1.setPackageDelivered(package1);
        entityManager.persist(delivery1);

        Delivery delivery2 = new Delivery();
        delivery2.setCustomer(c);
        delivery2.setPackageDelivered(package2);
        entityManager.persist(delivery2);

    }

    @Et("l'employé demande la prochaine livraison à envoyer")
    public void lEmployéDemandeLaProchaineLivraisonÀEnvoyer() {
        deliveries = deliverySchedule.get_deliveries();
        providers = deliverySchedule.providerList();
        assertNotNull(nextDeliveryInterface.getNextDelivery());
        providerListHashMap = deliveryInterface.getAllDayDeliveries();
    }

    @Et("après il devrait rester (\\d+) livraison")
    public void aprèsIlDevraitResterLivraison(int arg0) {
        assertNotNull(nextDeliveryInterface.getNextDelivery());
        assertNull(nextDeliveryInterface.getNextDelivery());
    }

    @Et("le fournisseur a (\\d+) livraison à payer")
    public void leFournisseurALivraisonÀPayer(int arg0) {
        assertEquals(arg0,providerListHashMap.get(providers.get(0)).size());
        //cleanDatabase();
    }

    @Quand("Lentreprise doit livrer (\\d+) colis de 2 fournisseurs de noms (.*) et (.*)")
    public void lentrepriseDoitLivrerColisDeFournisseursDeNomsAGEtPK(int arg0,String arg1,String arg2) {
        //initializeDatabaseDeliveryTestWithMutipleProviders(arg0,arg1,arg2);
        //delivs = databaseTest.getDeliveryList();
        //providers = databaseTest.getProviderList();
        //nextDeliveryInterface = new DeliveryBean();
        //deliveryInterface = new DeliveryBean();

        Customer c = new Customer("Pm", "adresse1");
        entityManager.persist(c);

        Provider pro1 = new Provider();
        pro1.setName(arg1);
        entityManager.persist(pro1);

        Provider pro2 = new Provider();
        pro2.setName(arg2);
        entityManager.persist(pro2);

        Package package1 = new Package();
        package1.setWeight(10.0);
        package1.setProvider(pro1);
        package1.setSecret_number("AXXXX8");
        entityManager.persist(package1);


        Package package2 = new Package();
        package2.setWeight(15.0);
        package2.setProvider(pro1);
        package2.setSecret_number("AXXX45");
        entityManager.persist(package2);
        pro1.add(package1);
        pro1.add(package2);

        Delivery delivery1 = new Delivery();
        delivery1.setCustomer(c);
        delivery1.setPackageDelivered(package1);
        entityManager.persist(delivery1);

        Delivery delivery2 = new Delivery();
        delivery2.setCustomer(c);
        delivery2.setPackageDelivered(package2);
        entityManager.persist(delivery2);

        Package package3 = new Package();
        package3.setWeight(20.0);
        package3.setProvider(pro2);
        package3.setSecret_number("AXXyX2");
        entityManager.persist(package3);


        Package package4 = new Package();
        package4.setWeight(25.0);
        package4.setProvider(pro2);
        package4.setSecret_number("AXXXU2");
        entityManager.persist(package4);
        pro2.add(package3);
        pro2.add(package4);

        Delivery delivery3 = new Delivery();
        delivery3.setCustomer(c);
        delivery3.setPackageDelivered(package3);
        entityManager.persist(delivery3);

        Delivery delivery4 = new Delivery();
        delivery4.setCustomer(c);
        delivery4.setPackageDelivered(package4);
        entityManager.persist(delivery4);
    }

    @Et("l'employé effectue les (\\d+) livraison de AG et une livraison de PK")
    public void lEmployéEffectueLesLivraisonDeAGEtUneLivraisonDePK(int arg0) {
        //delivery = nextDeliveryInterface.getNextDelivery();
        //delivery = nextDeliveryInterface.getNextDelivery();
        //delivery = nextDeliveryInterface.getNextDelivery();
        //providerListHashMap = deliveryInterface.getAllDayDeliveries();

        //assertNotNull(nextDeliveryInterface.getNextDelivery());

        deliveries = deliverySchedule.get_deliveries();
        providers = deliverySchedule.providerList();
        nextDeliveryInterface.getNextDelivery();
        nextDeliveryInterface.getNextDelivery();
        nextDeliveryInterface.getNextDelivery();

        System.out.println("/*******************************\n********************************/");
        System.out.println("/************************\n"+deliveries.size()+"*********************/");
        for(Delivery delivery : deliveries){
            System.out.println(delivery.getStatus());
        }

        providerListHashMap = deliveryInterface.getAllDayDeliveries();
    }

    @Alors("(.*) devra devra payer (\\d+) livraisons pour cette journée")
    public void agDevraDevraPayerLivraisonsPourCetteJournée(String arg0,int arg1) {
        assertEquals(arg1,providerListHashMap.get(providers.get(2)).size());
    }

    @Et("(.*) devra en payer (\\d+)")
    public void pkDevraEnPayer(String arg0,int arg1) {
        assertEquals(arg1,providerListHashMap.get(providers.get(3)).size());
        //cleanDatabase();
    }
}