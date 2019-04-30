/**
 * Created by Marko Ondrejicka
 */

package database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import exceptions.CustomerNotFoundException;
import exceptions.ShopNotFoundException;
import model.CartItem;
import model.Customer;
import model.Shop;
import model.Sortiment;

public class ShopDAO {

private SessionFactory sessionFactory;

@Autowired
private CustomerDAO customerDAO;
	
	/**
	 * Set the SessionFactory Bean 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Find and return shop, which is closest to the requested coordinates, throws exception if no shops are available
	 * @param userLatitude
	 * @param userLongitude
	 * @return shop
	 * @throws ShopNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Shop getClosestShop(BigDecimal userLatitude, BigDecimal userLongitude) throws ShopNotFoundException{
		Session session = this.sessionFactory.openSession();

		List<Shop> shopList = session.createQuery("SELECT shop FROM Shop shop",  Shop.class).list();
		session.close();
		
		if(shopList.size() > 0) {
			
			double minimum = Double.MAX_VALUE;
			int shop_index = -1;
			double distance;
			
			for(Shop shop : shopList) {
				distance = Math.sqrt((((shop.getLatitude().subtract(userLatitude)).pow(2)).add((shop.getLongitude().subtract(userLongitude)).pow(2))).doubleValue());
				System.out.println("Shop id - " + shop.getId() + " ma distance - " + distance);
				
				if(distance < minimum) {
					shop_index = shopList.indexOf(shop);
					minimum = distance;
				}
			}
			
			return shopList.get(shop_index);
		} else {
			throw new ShopNotFoundException();
		}
	}
	
	/**
	 * Return all shops present in the database
	 * @return shopList
	 * @throws ShopNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<Shop> getAllShops() throws ShopNotFoundException{
		Session session = this.sessionFactory.openSession();

		List<Shop> shopList = session.createQuery("SELECT shop FROM Shop shop",  Shop.class).list();
		session.close();
		
		if(shopList.size() > 0) {
			return shopList;
		} else {
			throw new ShopNotFoundException();
		}
	}
	
	/**
	 * Find and return sortiment of the shop depending on requeste shop and product, throws exception if sortiment
	 * @param shop_id
	 * @param product_id
	 * @return sortiment
	 * @throws ShopNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Sortiment getShopSortiment(int shop_id, int product_id) throws ShopNotFoundException{
		Session session = this.sessionFactory.openSession();

		Sortiment sortiment = session.createQuery("SELECT sort FROM Sortiment sort WHERE sort.shop.id =" + shop_id + " AND sort.product.id =" + product_id,  Sortiment.class).stream().findFirst().orElse(null);
		session.close();
		
		if(sortiment != null) {
			return sortiment;
		} else {
			throw new ShopNotFoundException();
		}
	}
	
	/**
	 * Find and return shop, which has the most of the products customer want to buy, throws exception if none of the shops satisfies the condition
	 * @param user_id
	 * @return filteredShop
	 * @throws ShopNotFoundException
	 * @throws CustomerNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Shop getSuitableShop(int user_id) throws ShopNotFoundException, CustomerNotFoundException{
		Session session = this.sessionFactory.openSession();
		
		List<CartItem> userCartItems = customerDAO.getCustomerCartItems(user_id);
		
		StringBuilder cartItems_id = new StringBuilder();
		cartItems_id.append("(");
		
		for(CartItem crt : userCartItems) {
			cartItems_id.append(crt.getProduct().getId());
			if(userCartItems.indexOf(crt) < userCartItems.size() - 1) {
				cartItems_id.append(",");
			}
		}
		cartItems_id.append(")");
		System.out.println(cartItems_id.toString());
		
		Integer bestShop_id = session.createQuery("SELECT sort.shop.id FROM Sortiment sort WHERE sort.product.id IN " 
		+ cartItems_id.toString() + " GROUP BY sort.shop.id ORDER BY (COUNT(sort.shop.id)) desc", Integer.class).stream().findFirst().orElse(null);
		
		Shop filteredShop = session.createQuery("SELECT filteredSort.shop FROM Sortiment filteredSort WHERE filteredSort.shop.id =" + bestShop_id, Shop.class).stream().findFirst().orElse(null);
		
		if(filteredShop != null) {
			return filteredShop;
		} else {
			throw new ShopNotFoundException();
		}
	}
}
