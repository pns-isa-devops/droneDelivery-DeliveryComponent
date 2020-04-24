package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Provider;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DeliverySchedule {

     List<Provider> providerList();

     List<Delivery> get_deliveries();

     Delivery findDeliveryByPackageNumber(String deliveryDate, String deliveryhour) throws Exception;
}
