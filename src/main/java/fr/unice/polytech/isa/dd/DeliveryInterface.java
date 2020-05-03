package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.entities.Customer;
import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Provider;

import javax.ejb.Local;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Local
public interface DeliveryInterface {
     HashMap<Provider,List<Delivery>> getAllDayDeliveries();
     List<Delivery> getAllDeliveriesOfAProvider(int provider_id);
}
