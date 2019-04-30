package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.gson.Gson;
import database.CustomerDAO;
import database.ProductDAO;
import database.ShopDAO;
import model.CartItem;
import model.Customer;
import model.Product;
import model.Shop;
import model.Sortiment;
import exceptions.CustomerNotFoundException;
import exceptions.ProductNotFoundException;
import exceptions.ShopNotFoundException;

@Controller
public class testovaci {
	
	private Gson gson = new Gson();
	
	@Autowired
	private ShopDAO shopDAO;
	
	@Autowired
	private CustomerDAO customerDAO;	
	
	@Autowired
	private ProductDAO productDAO;	
	
	@RequestMapping(value = "/", produces = "application/json")  
    public void getSortiment(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        
		/*Shop shop = null;
		
		try {
			shop = shopDAO.getClosestShop(BigDecimal.valueOf(48.145620), BigDecimal.valueOf(17.131582));
			System.out.println(shop.getName());
		} catch (ShopClosestNotFoundException e) {
		
		}*/
		
		/*Sortiment sortiment = null;
		
		try {
			sortiment = shopDAO.getShopSortiment(1, 1);
			System.out.println("Pocet - " + sortiment.getAmount() + " Cena - " + sortiment.getPrice());
		} catch (ShopNotFoundException e) {
		
		}*/
		
		/*List<CartItem> cartItems = null;
		
		try {
			cartItems = customerDAO.getCustomerCartItems(1);
			for(CartItem cartItem : cartItems) {
				System.out.println("Nazov produktu - " + cartItem.getProduct().getName() + " Mnozstvo - " + cartItem.getAmount());
			}
		} catch (CustomerNotFoundException e){
			
		}*/
		
		/*List<Product> products = null;
		
		try {
			products = productDAO.getProductsByCategory("jedlo");
			for(Product product : products) {
				System.out.println("Nazov produktu - " + product.getName());
			}
		} catch (ProductNotFoundException e){
			
		}*/
		
		/*CartItem ct = cartItems.get(0);
		ct.setAmount(10);
		
		customerDAO.saveCartItem(ct);*/
		
		Shop shop = null;
		
		try {
			shop = shopDAO.getSuitableShop(1);
			System.out.println("Najlepsi obchod sa vola - " + shop.getName());
		} catch (ShopNotFoundException | CustomerNotFoundException e) {
			
		}
		
    }
}
