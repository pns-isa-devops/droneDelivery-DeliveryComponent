package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.entities.Delivery;

import javax.ejb.Local;

@Local
public interface NextDeliveryInterface {

    Delivery getNextDelivery();
}
