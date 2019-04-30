package controller;

import java.io.IOException;
import java.io.PrintWriter;
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
        
		Customer customer = null;
		String customerJSON = null;
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		
		try {
			customer = customerDAO.getCustomer(email, password);
			customerJSON = this.gson.toJson(customer);
			
			System.out.println("customer name - " + customer.getName());
			out.print(customerJSON);
		} catch (CustomerNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			System.out.println("User sa nenasiel");
			out.print(customerJSON);
		}
    }
}
