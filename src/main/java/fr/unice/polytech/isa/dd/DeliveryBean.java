package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.entities.Database;
import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Provider;
import utils.MyDate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;


@Stateless(name="delivery-stateless")
public class DeliveryBean implements DeliveryInterface, NextDeliveryInterface, DeliverySchedule {

    @PersistenceContext private EntityManager entityManager;

    HashMap<Provider, List<Delivery>> deliveries_by_provider = new HashMap<>();

    /*public DeliveryBean() {
        deliveries_by_provider = new HashMap<>();
    }*/

    @Override
    public HashMap<Provider, List<Delivery>> getAllDayDeliveries() {
        List<Provider> provider_set = providerList();
        for (Provider pro : provider_set) {
            List<Delivery> alldeliveries = getAllDeliveries(pro.getId());
            if (!alldeliveries.isEmpty()) {
                alldeliveries = alldeliveries.stream().filter(d ->d.getStatus()).collect(Collectors.toList());
                this.deliveries_by_provider.put(pro, alldeliveries);
            }
        }
        return deliveries_by_provider;
    }

    @Override
    public List<Delivery> getAllDeliveries(int provider_id) {
        List<Delivery> deliveries = get_deliveries();
        List<Delivery> provider_deliveries = new ArrayList<>();
        for (Delivery dev : deliveries) {
            if (dev.getPackageDelivered().getProvider_id()==(provider_id)) {
                provider_deliveries.add(dev);
            }
        }
        return provider_deliveries;
    }

    @Override
    public Delivery getNextDelivery() throws Exception {
        /******* TEST ******/
        // Database.getInstance().initializeDatabase();
       // System.out.println("Passage dans mon service");
        List<Delivery> deliveries = all_deliveries();
        if(deliveries != null){
            if (deliveries.size() !=0) {
                for (Delivery del : deliveries
                ) {
                    if (!del.getStatus()) {
                        del.setStatus(true);
                        return del;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public List<Provider> providerList(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Provider> criteria = builder.createQuery(Provider.class);
        Root<Provider> root =  criteria.from(Provider.class);
        criteria.select(root);
        TypedQuery<Provider> query = entityManager.createQuery(criteria);
        try {
            List<Provider> toReturn = new ArrayList<>(query.getResultList());
            return Optional.of(toReturn).get();
        } catch (NoResultException nre){
            return null;
        }
    }

    @Override
    public List<Delivery> get_deliveries(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Delivery> criteria = builder.createQuery(Delivery.class);
        Root<Delivery> root =  criteria.from(Delivery.class);
        criteria.select(root);
        TypedQuery<Delivery> query = entityManager.createQuery(criteria);
        try {
            List<Delivery> toReturn = new ArrayList<>(query.getResultList());
            return Optional.of(toReturn).get();
        } catch (NoResultException nre){
            return null;
        }
    }

    private List<Delivery> all_deliveries() throws Exception {
        if(!get_deliveries().isEmpty()){
            List<Delivery> deliveriesList = get_deliveries();
            MyDate myDate = new MyDate();
            deliveriesList = deliveriesList.stream().filter(d->d.getDeliveryDate().equals(myDate.getDate_now())).collect(Collectors.toList());
            deliveriesList.sort(Comparator.comparingInt(Delivery::getDeliveryBeginTimeInSeconds));
            return deliveriesList;
        }
        return null;
    }
}
