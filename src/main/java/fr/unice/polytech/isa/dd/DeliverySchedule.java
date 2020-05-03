package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Provider;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DeliverySchedule {
     List<Delivery> get_deliveries();
     Delivery findDeliveryByDateAndHour(String deliveryDate, String deliveryhour) throws Exception;
     List<Delivery> all_deliveries_of_theDate(String date);
}
