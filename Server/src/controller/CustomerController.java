package controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import database.CustomerDAO;
import model.CartItem;
import model.Customer;
import exceptions.CustomerNotFoundException;

@Controller
public class CustomerController {
	
	private Gson gson = new Gson();
	
	@Autowired
	private CustomerDAO customerDAO;
	
	private static Logger log = LoggerFactory.getLogger(CustomerController.class.getName());
	
	@RequestMapping(value = "/Customer", produces = "application/json")  
    public void getCustomer(HttpServletRequest request, HttpServletResponse response, @RequestParam("email") String email, @RequestParam("password") String password) throws IOException {  
        log.info("Executing getCustomer webservice with parameters: email" + email + " and password: " + password);
		Customer customer = null;
		String customerJSON = null;
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		
		try {
			customer = customerDAO.getCustomer(email, password);
			customerJSON = this.gson.toJson(customer);
			log.info("Customer was found, his name: " + customer.getName());
			System.out.println("customer name - " + customer.getName());
		} catch (CustomerNotFoundException e) {
			log.info("Customer was not found in database, exception CustomerNotFoundException was thrown");
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			System.out.println("User sa nenasiel");
		}finally {
			out.print(customerJSON);
			out.close();
		}
    }
	
	@RequestMapping(value = "/Customer/Cart", produces = "application/json")
	public void getCustomerCart(HttpServletRequest request, HttpServletResponse response,@RequestParam int userId)throws IOException{
        log.info("Executing getCustomerCart webservice with parameters: userId ",userId);
		List<CartItem> cartItems = new ArrayList<CartItem>();
		String cartItemsJSON = null;
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		
		try {
			cartItems = customerDAO.getCustomerCartItems(userId);
			cartItemsJSON = this.gson.toJson(cartItems);
			log.info("Cart items for user with id: ", userId," were successfuly found");
			
		} catch (CustomerNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			System.out.println("User sa nenasiel");
			log.info("Cart items for user with id: ", userId," were not found");

		}finally {
			out.print(cartItemsJSON);
			out.close();
		}
		
	}
	
	@RequestMapping(value = "/Customer/UpdateCart", produces = "application/json", method = RequestMethod.PUT )
	public void updateCustomerCart(HttpServletRequest request, HttpServletResponse response, @RequestBody String id, @RequestBody String user_id, @RequestBody String product_id, @RequestBody String amount) {
		//customerDAO.saveCartItem(cartItem);
		System.out.println(user_id);
	}
}
