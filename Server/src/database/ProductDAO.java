/**
 * Created by Marko Ondrejicka
 */

package database;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import exceptions.CustomerNotFoundException;
import exceptions.ProductNotFoundException;
import exceptions.ShopNotFoundException;
import model.CartItem;
import model.Customer;
import model.Product;
import model.Shop;
import model.Sortiment;

public class ProductDAO {

private SessionFactory sessionFactory;
	
	/**
	 * Set the SessionFactory Bean 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Find and return product by ID, which was scanned by QR, throws exception if scanned product is not in the database
	 * @param productId
	 * @return product
	 * @throws ProductNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Product getProductQR(int productId) throws ProductNotFoundException {
		Session session = this.sessionFactory.openSession();

		Product product = session.createQuery("SELECT prod FROM Product prod WHERE prod.id =" + productId,  Product.class).stream().findFirst().orElse(null);
		session.close();
		
		if(product != null) {
			return product;
		} else {
			throw new ProductNotFoundException();
		}
	}
	
	/**
	 * Find and return all products, which belong to the requested category, throws exception if no products in the category exist
	 * @param productCategory
	 * @return products
	 * @throws ProductNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<Product> getProductsByCategory(String productCategory) throws ProductNotFoundException {
		Session session = this.sessionFactory.openSession();

		List<Product> products = session.createQuery("SELECT prod FROM Product prod WHERE prod.category ='" + productCategory + "'",  Product.class).list();
		session.close();
		
		if(products.size() > 0) {
			return products;
		} else {
			throw new ProductNotFoundException();
		}
	}
}
