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
	public Shop[] getClosestShop(BigDecimal userLatitude, BigDecimal userLongitude) throws ShopNotFoundException{
		Session session = this.sessionFactory.openSession();

		List<Shop> shopList = session.createQuery("SELECT shop FROM Shop shop",  Shop.class).list();
		session.close();
		
		if(shopList.size() > 0) {
			
			double closestShopDistance = Double.MAX_VALUE;
			double currentShopDistance;
			int closestShopIndex = -1;
			
			for(Shop currentShop : shopList) {
				currentShopDistance = Math.sqrt((((currentShop.getLatitude().subtract(userLatitude)).pow(2)).add((currentShop.getLongitude().subtract(userLongitude)).pow(2))).doubleValue());
				System.out.println("Shop id - " + currentShop.getId() + " ma distance - " + currentShopDistance);
				
				if(currentShopDistance < closestShopDistance) {
					closestShopIndex = shopList.indexOf(currentShop);
					closestShopDistance = currentShopDistance;
				}
			}
			
			return new Shop[] {shopList.get(closestShopIndex)};
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
	 * @param shopId
	 * @param productId
	 * @return sortiment
	 * @throws ShopNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Sortiment getShopSortiment(int shopId, int productId) throws ShopNotFoundException{
		Session session = this.sessionFactory.openSession();

		Sortiment sortiment = session.createQuery("SELECT sort FROM Sortiment sort WHERE sort.shop.id =" + shopId + " AND sort.product.id =" + productId,  Sortiment.class).stream().findFirst().orElse(null);
		session.close();
		
		if(sortiment != null) {
			return sortiment;
		} else {
			throw new ShopNotFoundException();
		}
	}
	
	/**
	 * Find and return shop, which has the most of the products customer want to buy, throws exception if none of the shops satisfies the condition
	 * @param userId
	 * @return filteredShop
	 * @throws ShopNotFoundException
	 * @throws CustomerNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Shop getSuitableShop(int userId) throws ShopNotFoundException, CustomerNotFoundException{
		Session session = this.sessionFactory.openSession();
		
		List<CartItem> userCartItems = customerDAO.getCustomerCartItems(userId);
		
		StringBuilder requestedProductIDs = new StringBuilder();
		requestedProductIDs.append("(");
		
		for(CartItem cartItem : userCartItems) {
			requestedProductIDs.append(cartItem.getProduct().getId());
			if(userCartItems.indexOf(cartItem) < userCartItems.size() - 1) {
				requestedProductIDs.append(",");
			}
		}
		requestedProductIDs.append(")");
		System.out.println(requestedProductIDs.toString());
		
		Integer bestShopId = session.createQuery("SELECT sort.shop.id FROM Sortiment sort WHERE sort.product.id IN " 
		+ requestedProductIDs.toString() + " GROUP BY sort.shop.id ORDER BY (COUNT(sort.shop.id)) desc", Integer.class).stream().findFirst().orElse(null);
		
		Shop filteredShop = session.createQuery("SELECT filteredSort.shop FROM Sortiment filteredSort WHERE filteredSort.shop.id =" + bestShopId, Shop.class).stream().findFirst().orElse(null);
		
		if(filteredShop != null) {
			return filteredShop;
		} else {
			throw new ShopNotFoundException();
		}
	}
}
