package com.ecom.Shopping_cart.service;

import java.util.List;

import com.ecom.Shopping_cart.model.OrderRequest;
import com.ecom.Shopping_cart.model.ProductOrder;

public interface OrderService {
    public void saveOrder(Integer userid,OrderRequest orderRequest);
	
	public List<ProductOrder> getOrdersByUser(Integer userId);
	
	public Boolean updateOrderStatus(Integer id,String status);
}
