/**
 * Created by Marko Ondrejicka
 */

package database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.CartItem;
import model.Customer;
import exceptions.CustomerNotFoundException;

public class CustomerDAO {

	private SessionFactory sessionFactory;
	
	/**
	 * Set the SessionFactory Bean 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Find Customer in database based on entered email and password, throws exception if customer does not exist
	 * @param email
	 * @param password
	 * @return customer
	 * @throws CustomerNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Customer getCustomer(String email, String password) throws CustomerNotFoundException {
		Session session = this.sessionFactory.openSession();

		Customer customer = session.createQuery("SELECT cust FROM Customer cust WHERE cust.email = '" + email + "' AND cust.password = '" + password + "'",  Customer.class).stream().findFirst().orElse(null);
		session.close();
		
		if(customer != null) {
			return customer;
		} else {
			throw new CustomerNotFoundException();
		}
	}
	
	/**
	 * Find all cart items related to requested user
	 * @param userId
	 * @return cartItems
	 * @throws CustomerNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<CartItem> getCustomerCartItems(int userId) throws CustomerNotFoundException {
		Session session = this.sessionFactory.openSession();

		List<CartItem> cartItems = session.createQuery("SELECT cart FROM CartItem cart WHERE cart.user.id = " + userId,  CartItem.class).list();
		session.close();
		
		if(cartItems.size() > 0) {
			return cartItems;
		} else {
			throw new CustomerNotFoundException();
		}
	}
	
	/**
	 * Insert new cart item into database
	 * @param cartItemNew
	 */
	@SuppressWarnings("unchecked")
	public void saveCartItem(CartItem cartItemNew) {
		Session session = this.sessionFactory.openSession();
		
		CartItem cartItem = session.createQuery("SELECT cart FROM CartItem cart WHERE cart.id = " + cartItemNew.getId(),  CartItem.class).stream().findFirst().orElse(null);
		
		if(cartItem != null) {
			
			Transaction tx = session.beginTransaction();
			session.delete(cartItem);
			
			if(cartItemNew.getAmount() > 0) {

				session.save(cartItemNew);
			}
			tx.commit();
			session.close();
		}
	}
}
