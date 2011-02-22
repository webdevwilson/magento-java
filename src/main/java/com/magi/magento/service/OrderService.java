package com.magi.magento.service;

import com.magi.magento.MagentoException;
import com.magi.magento.domain.OrderStatus;
import com.magi.magento.ws.client.AssociativeEntity;
import com.magi.magento.ws.client.Filters;
import com.magi.magento.ws.client.OrderItemIdQty;
import com.magi.magento.ws.client.SalesOrderEntity;
import com.magi.magento.ws.client.SalesOrderInvoiceEntity;
import com.magi.magento.ws.client.SalesOrderItemEntity;
import com.magi.magento.ws.client.SalesOrderShipmentEntity;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class OrderService extends BaseService {

    public OrderService(final URL url, final String user, final String password) {
        super(url, user, password);
    }

    public SalesOrderEntity[] listSalesOrdersByStatus(final OrderStatus state) {
        login();
        return queryForSalesOrders("state", state.toString());
    }

    public SalesOrderEntity salesOrderInfo(final String orderId) {
        login();
        try {
            return getPort().salesOrderInfo(getSessionId(), orderId);
        } catch (RemoteException ex) {
            throw new MagentoException(ex);
        }
    }

    public String salesOrderShipmentCreate(final String orderId, final OrderItemIdQty... itemsQty) {
        login();
        try {
            return getPort().salesOrderShipmentCreate(getSessionId(), orderId, itemsQty, "", 0, 0);
        } catch (RemoteException ex) {
            throw new MagentoException(ex);
        }
    }

    public void salesOrderShipmentAddTrack(final String shipmentId, final String carrier, final String title, final String trackingNumber) {
        login();
        try {
            getPort().salesOrderShipmentAddTrack(getSessionId(), shipmentId, carrier, title, trackingNumber);
        } catch (RemoteException ex) {
            throw new MagentoException(ex);
        }
    }

    public SalesOrderShipmentEntity[] salesOrderShipmentList(final String... keyValuePairs) {
        login();
        try {
            return getPort().salesOrderShipmentList(getSessionId(), createFilter(keyValuePairs));
        } catch (RemoteException ex) {
            throw new MagentoException(ex);
        }
    }

    public SalesOrderInvoiceEntity getOrderInvoice(final SalesOrderEntity salesOrder) {
        login();
        try {
            return getPort().salesOrderInvoiceInfo(getSessionId(), salesOrder.getIncrement_id());
        } catch (RemoteException ex) {
            throw new MagentoException(ex);
        }
    }


    public String createInvoice(final SalesOrderEntity salesOrder) {
        login();
        try {
            final List<OrderItemIdQty> orderItemQtys = new ArrayList<OrderItemIdQty>();
            for( final SalesOrderItemEntity item : salesOrder.getItems() ) {
                final OrderItemIdQty oiq = new OrderItemIdQty();
                oiq.setOrder_item_id(Integer.parseInt(item.getItem_id()));
                oiq.setQty(Float.parseFloat(item.getQty_ordered()));
                orderItemQtys.add(oiq);
            }

            return getPort().salesOrderInvoiceCreate(getSessionId(), salesOrder.getIncrement_id(),
                    orderItemQtys.toArray(new OrderItemIdQty[0]), "Your order is in the process of being fulfilled", "true", "true");
        } catch (RemoteException ex) {
            throw new MagentoException(ex);
        }
    }

    public void holdSalesOrder(final SalesOrderEntity salesOrder) {
        login();
        try {
            getPort().salesOrderHold(getSessionId(), salesOrder.getIncrement_id());
        } catch (RemoteException ex) {
            throw new MagentoException(ex);
        }
    }

    public void unholdSalesOrder(final SalesOrderEntity salesOrder) {
        login();
        try {
            getPort().salesOrderUnhold(getSessionId(), salesOrder.getIncrement_id());
        } catch (RemoteException ex) {
            throw new MagentoException(ex);
        }
    }

    private SalesOrderEntity[] queryForSalesOrders(final String... keyValuePairs) {
        try {
            return getPort().salesOrderList(getSessionId(), createFilter(keyValuePairs));
        } catch (Exception ex) {
            throw new MagentoException(ex);
        }
    }

    private Filters createFilter(final String... keyValuePairs) {
        if (keyValuePairs.length % 2 == 1) {
            throw new IllegalArgumentException("Odd number of key-value pair arguments");
        }
        List<AssociativeEntity> filters = new ArrayList<AssociativeEntity>(keyValuePairs.length / 2);
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            AssociativeEntity keyValuePair = new AssociativeEntity();
            keyValuePair.setKey(keyValuePairs[i]);
            keyValuePair.setValue(keyValuePairs[i + 1]);
            filters.add(keyValuePair);
        }
        Filters f = new Filters();
        f.setFilter(filters.toArray(new AssociativeEntity[filters.size()]));
        return f;
    }
}
