package fr.unice.polytech.isa.dd.business;

import fr.unice.polytech.isa.dd.*;
import fr.unice.polytech.isa.dd.entities.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class DeliveryBeanTest {

    /*Database database = Database.getInstance();

    @org.junit.Test
    public void getAllDayDeliveries() {

        database.initializeDatabase();
        List<Provider> providers;
        DeliveryBean deliveryBeantest;
        providers = Database.getInstance().getProviderList();
        deliveryBeantest = new DeliveryBean();
        HashMap<Provider,List<Delivery>> alldeliveries = deliveryBeantest.getAllDayDeliveries();

        for(Provider provider : providers){
            assertEquals(0,alldeliveries.get(provider).size());
        }
    }

   @org.junit.Test
    public void getAllDeliveries() {

        DeliveryBean deliveryBeantest = new DeliveryBean();

        List<Delivery> providers_delivs = deliveryBeantest.getAllDeliveries("1");

        assertEquals(2,providers_delivs.size());
    }

    @Test
    public void getNextDelivery(){

        List<Delivery> delivs = Database.getInstance().getDeliveryList();

        DeliveryBean deliveryBeantest = new DeliveryBean();
        DeliveryBean deliveryBeanTestNull = new DeliveryBean();

        Delivery nextdelivery = deliveryBeanTestNull.getNextDelivery();

        assertNotNull(nextdelivery);

       // delivs.get(0).setStatus(true);
        nextdelivery = deliveryBeantest.getNextDelivery();
        assertTrue(delivs.get(1).equals(nextdelivery));

        //delivs.get(1).setStatus(true);
        nextdelivery = deliveryBeantest.getNextDelivery();
        assertFalse(delivs.get(1).equals(nextdelivery));
    }*/
}