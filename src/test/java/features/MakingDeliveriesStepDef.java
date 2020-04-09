package features;

/*import arquillian.AbstractDeliveryTest;
import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Et;
import cucumber.api.java.fr.Quand;
import fr.unice.polytech.isa.dd.DeliveryBean;
import fr.unice.polytech.isa.dd.DeliveryInterface;
import fr.unice.polytech.isa.dd.NextDeliveryInterface;
import fr.unice.polytech.isa.dd.entities.Customer;
import fr.unice.polytech.isa.dd.entities.Database;
import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Package;
import fr.unice.polytech.isa.dd.entities.Provider;
import io.cucumber.java8.Fr;
import org.joda.time.DateTime;

import javax.ejb.EJB;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MakingDeliveriesStepDef  {

   /* @EJB (name = "delivery-stateless") private NextDeliveryInterface nextDeliveryInterface;
    @EJB (name = "delivery-stateless") private DeliveryInterface deliveryInterface;
    @EJB (name = "database-stateless") private Database databaseTest = Database.getInstance();


    private List<Provider> providers = Database.getInstance().getProviderList();
    private List<Delivery> delivs = Database.getInstance().getDeliveryList();
    private Delivery delivery;
    private HashMap<Provider,List<Delivery>> providerListHashMap;

    public void initializeDatabaseDeliveryTest() {
        Customer c = new Customer("Pm", "adresse1");

        DateTime dt = new DateTime();

        Provider pro1 = new Provider("1", "Aug1");
        providers.add(pro1);

        Package pk1 = new Package("1", 2.0, dt, "1");

        Delivery d1 = new Delivery(c, pk1, dt, null);
        delivs.add(d1);

    }
    public void initializeDatabaseDeliveryTestWithParam(int arg1, String arg2) {
        Customer c = new Customer("Pm", "adresse1");

        DateTime dt = new DateTime();

        Provider pro1 = new Provider("1", arg2);
        providers.add(pro1);

        for (int i = 0; i < arg1; i++){
            delivs.add(new Delivery(c,new Package(""+i,10.0,dt,pro1.getId()),dt,null));
        }
    }
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
    }

    @Quand("^l'employé demande la prochaine livraison$")
    public void lemployer_demande_la_prochaine_livraison()  {
        delivs = Database.getInstance().getDeliveryList();
    }
    @Alors("^Il y a (\\d+) livraisons$")
    public void il_y_a_livraisons(int arg1) {
//        Database.getInstance().clearDatabase();
        nextDeliveryInterface = new DeliveryBean();
        assertNull(nextDeliveryInterface.getNextDelivery());
    }

    @Quand("L'entreprise reçoit une livraison")
    public void lEntrepriseReçoitUneLivraison() {
        initializeDatabaseDeliveryTest();
        delivs =  databaseTest.getDeliveryList();
    }

    @cucumber.api.java.fr.Et("l'employé regarde la prochaine livraison")
    public void lEmployéRegardeLaProchaineLivraison() {
        nextDeliveryInterface = new DeliveryBean();
    }

    @Alors("Il devrait y avoir (\\d+) livraison")
    public void ilYLivraison(int arg0) {
        assertNotNull(nextDeliveryInterface.getNextDelivery());
    }

    @Et("après il n'y a plus de livraisons")
    public void aprèsIlNYAPlusDeLivraisons() {
        assertNull(nextDeliveryInterface.getNextDelivery());
        cleanDatabase();
    }

    @Quand("Lentreprise doit livrer (\\d+) colis dun seul fournisseur de nom (.*)")
    public void lEntrepriseDoitLivrerColisDUnSeulFournisseurDeNom(int arg0, String arg1) {
        initializeDatabaseDeliveryTestWithParam(arg0,arg1);
        delivs = databaseTest.getDeliveryList();
        providers = databaseTest.getProviderList();
        nextDeliveryInterface = new DeliveryBean();
        deliveryInterface = new DeliveryBean();
    }

    @Et("l'employé demande la prochaine livraison à envoyer")
    public void lEmployéDemandeLaProchaineLivraisonÀEnvoyer() {
        assertNotNull(nextDeliveryInterface.getNextDelivery());
        providerListHashMap = deliveryInterface.getAllDayDeliveries();
    }

    @Et("après il devrait rester (\\d+) livraison")
    public void aprèsIlDevraitResterLivraison(int arg0) {
        assertNotNull(nextDeliveryInterface.getNextDelivery());
    }

    @Et("le fournisseur a (\\d+) livraison à payer")
    public void leFournisseurALivraisonÀPayer(int arg0) {
        assertEquals(arg0,providerListHashMap.get(providers.get(0)).size());
        cleanDatabase();
    }

    @Quand("Lentreprise doit livrer (\\d+) colis de 2 fournisseurs de noms (.*) et (.*)")
    public void lentrepriseDoitLivrerColisDeFournisseursDeNomsAGEtPK(int arg0,String arg1,String arg2) {
        initializeDatabaseDeliveryTestWithMutipleProviders(arg0,arg1,arg2);
        delivs = databaseTest.getDeliveryList();
        providers = databaseTest.getProviderList();
        nextDeliveryInterface = new DeliveryBean();
        deliveryInterface = new DeliveryBean();
    }

    @Et("l'employé effectue les (\\d+) livraison de AG et une livraison de PK")
    public void lEmployéEffectueLesLivraisonDeAGEtUneLivraisonDePK(int arg0) {
        delivery = nextDeliveryInterface.getNextDelivery();
        delivery = nextDeliveryInterface.getNextDelivery();
        delivery = nextDeliveryInterface.getNextDelivery();
        providerListHashMap = deliveryInterface.getAllDayDeliveries();

        assertNotNull(nextDeliveryInterface.getNextDelivery());
    }

    @Alors("(.*) devra devra payer (\\d+) livraisons pour cette journée")
    public void agDevraDevraPayerLivraisonsPourCetteJournée(String arg0,int arg1) {
        assertEquals(arg1,providerListHashMap.get(providers.get(0)).size());
    }

    @Et("(.*) devra en payer (\\d+)")
    public void pkDevraEnPayer(String arg0,int arg1) {
        assertEquals(arg1,providerListHashMap.get(providers.get(1)).size());
        cleanDatabase();
    }
}*/