package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.entities.Customer;
import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Provider;
import utils.MyDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;


@Stateless(name="delivery-stateless")
public class DeliveryBean implements DeliveryInterface, NextDeliveryInterface, DeliverySchedule {

    @PersistenceContext private EntityManager entityManager;
    @EJB(name = "provider-stateless") private ProviderFinder providerFinder;

    HashMap<Provider, List<Delivery>> deliveries_by_provider = new HashMap<>();

    @Override
    public HashMap<Provider, List<Delivery>> getAllDayDeliveries(){
        deliveries_by_provider = new HashMap<>();
        List<Provider> provider_set = providerFinder.providerList();
        for (Provider pro : provider_set) {
            List<Delivery> alldeliveries = getAllDeliveriesOfAProvider(pro.getId()).stream().filter(d->d.getDeliveryDate().equals(MyDate.date_now)).collect(Collectors.toList());
            alldeliveries = alldeliveries.stream().filter(Delivery::getStatus).collect(Collectors.toList());
            if (!alldeliveries.isEmpty()) {
                this.deliveries_by_provider.put(pro, alldeliveries);
            }
        }
        return deliveries_by_provider;
    }

    @Override
    public List<Delivery> getAllDeliveriesOfAProvider(int provider_id) {
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
    public Delivery getNextDelivery(){
        List<Delivery> deliveries = all_deliveries_of_today();
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

    private List<Delivery> all_deliveries_of_today() {
        if(!get_deliveries().isEmpty()){
            List<Delivery> deliveriesList = get_deliveries();
            deliveriesList = deliveriesList.stream().filter(d->d.getDeliveryDate().equals(MyDate.date_now)).collect(Collectors.toList());
            deliveriesList.sort(Comparator.comparingInt(Delivery::getDeliveryBeginTimeInSeconds));
            return deliveriesList;
        }
        return null;
    }

    @Override
    public Delivery findDeliveryByDateAndHour(String deliveryDate, String deliveryhour) throws Exception {
        MyDate myDate = new MyDate(deliveryDate,deliveryhour);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Delivery> criteria = builder.createQuery(Delivery.class);
        Root<Delivery> root =  criteria.from(Delivery.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("deliveryDate"), deliveryDate));
        predicates.add(builder.equal(root.get("deliveryBeginTimeInSeconds"),myDate.getDate_seconds()));
        criteria.select(root).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Delivery> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult()).get();
        } catch (NoResultException nre){
            return null;
        }
    }

    @Override
    public List<Delivery> all_deliveries_of_theDate(String date){
        List<Delivery> deliveriesList = get_deliveries();
        deliveriesList = deliveriesList.stream().filter(d->d.getDeliveryDate().equals(date)).collect(Collectors.toList());
        deliveriesList.sort(Comparator.comparingInt(Delivery::getDeliveryBeginTimeInSeconds));
        return deliveriesList;
    }
}
