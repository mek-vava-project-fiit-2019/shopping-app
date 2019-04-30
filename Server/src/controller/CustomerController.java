/**
 * Created by Erik Podola
 */

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	/**
	 * Function finds customer based on his email and password, throws CustomerNotFoundException if he is not found
	 * @param request
	 * @param response
	 * @param email
	 * @param password
	 * @throws IOException
	 */
	@RequestMapping(value = "/Customer", produces = "application/json")  
    public void getCustomer(HttpServletRequest request, HttpServletResponse response, @RequestParam("email") String email, @RequestParam("password") String password) throws IOException {  
        log.info("Executing getCustomer webservice with parameters - email: " + email + " and password: " + password);
		Customer customer = null;
		String customerJSON = null;
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		
		try {
			customer = customerDAO.getCustomer(email, password);
			log.info("Customer was found, his name: " + customer.getName());
			System.out.println("customer name - " + customer.getName());
		} catch (CustomerNotFoundException e) {
			log.error("User with parameters: " + email + password + " was not found");
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}finally {
			out.print(customerJSON);
			customerJSON = this.gson.toJson(customer);
			out.close();
		}
    }
	/**
	 * Find and return customer's cart based on his user id, throws CustomerNotFoundException if he has no cart
	 * @param request
	 * @param response
	 * @param userId
	 * @throws IOException
	 */
	@RequestMapping(value = "/Customer/Cart", produces = "application/json")
	public void getCustomerCart(HttpServletRequest request, HttpServletResponse response,@RequestParam int userId)throws IOException{
        log.info("Executing getCustomerCart webservice with parameters - userId: ",userId);
		List<CartItem> cartItems = new ArrayList<CartItem>();
		String cartItemsJSON = null;
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		
		try {
			cartItems = customerDAO.getCustomerCartItems(userId);
			log.info("Cart items for user with id: ", userId," were successfuly found");
			
		} catch (CustomerNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			System.out.println("User sa nenasiel");
			log.error("Cart items for user with id: ", userId," were not found");

		}finally {
			cartItemsJSON = this.gson.toJson(cartItems);
			out.print(cartItemsJSON);
			out.close();
		}
		
	}
	/**
	 * Update (or create) customers cart based on his cart item that he selected
	 * @param request
	 * @param response
	 * @param cartItem
	 */
	@RequestMapping(value = "/Customer/UpdateCart", produces = "application/json", method =RequestMethod.PUT )
	public void updateCustomerCart(HttpServletRequest request, HttpServletResponse response, CartItem cartItem) {
		log.info("Executing updateCustomerCart webservice");
		customerDAO.saveCartItem(cartItem);
		
	}
}
