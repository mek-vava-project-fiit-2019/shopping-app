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
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public Product getProductQR(int product_id) throws ProductNotFoundException {
		Session session = this.sessionFactory.openSession();

		Product product = session.createQuery("SELECT prod FROM Product prod WHERE prod.id =" + product_id,  Product.class).stream().findFirst().orElse(null);
		session.close();
		
		if(product != null) {
			return product;
		} else {
			throw new ProductNotFoundException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductsByCategory(String product_category) throws ProductNotFoundException {
		Session session = this.sessionFactory.openSession();

		List<Product> products = session.createQuery("SELECT prod FROM Product prod WHERE prod.category ='" + product_category + "'",  Product.class).list();
		session.close();
		
		if(products.size() > 0) {
			return products;
		} else {
			throw new ProductNotFoundException();
		}
	}
	
}
