/**
 * Created by Erik Podola
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RespectBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import database.ShopDAO;
import exceptions.CustomerNotFoundException;
import exceptions.ShopNotFoundException;
import model.Product;
import model.Shop;
import model.Sortiment;

@Controller
public class ShopController {
	@Autowired
	private ShopDAO shopDAO;
	
	private Gson gson = new Gson();
	private static Logger log = LoggerFactory.getLogger(ProductController.class.getName());
<<<<<<< HEAD

	@RequestMapping(value = "/Shop/Near", method = RequestMethod.GET, produces = "application/json")
=======
	/**
	 * Function that finds and returns closest shop based on his coordinates, throws ShopNotFoundException if no shop is found
	 * @param longitude
	 * @param latitude
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/Shop/Closest", method = RequestMethod.GET, produces = "application/json")
>>>>>>> origin/master
	public void getClossestShopBassedOnCoordinates(@RequestParam BigDecimal longitude,@RequestParam BigDecimal latitude, HttpServletRequest req, HttpServletResponse res)throws IOException {
        log.info("Executing getClossestShopBassedOnCoordinates webservice with parameters - latitude = ",latitude, ", longitude = ", longitude);
		Shop[] shop = null;
		String shopJSON = null;
		PrintWriter out = res.getWriter();
		res.setCharacterEncoding("UTF-8");	
		try {
			shop = shopDAO.getClosestShop(longitude, latitude);
			log.info("Found closest shop with name " + shop[0].getName());	
		} catch (ShopNotFoundException e) {
			res.setStatus(HttpServletResponse.SC_NO_CONTENT);
			log.error("Did not found any shop");	

		}
		finally {
			shopJSON = this.gson.toJson(shop);
			out.print(shopJSON);
		}
	}
	
	/**
	 * Function fins and returns all shops from database, throws ShopNotFoundException if no shop is not found
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/Shop", method = RequestMethod.GET, produces = "application/json")
	public void getAllShops(HttpServletRequest req, HttpServletResponse res)throws IOException {
        log.info("Executing getClossestShopBassedOnCoordinates webservice");
		List<Shop> shop = new ArrayList<Shop>();
		String shopJSON = null;
		PrintWriter out = res.getWriter();
		res.setCharacterEncoding("UTF-8");	
		try {
			shop = shopDAO.getAllShops();	
			log.info("Got all shops");
			//TODO add loggers
		} catch (ShopNotFoundException e) {
			res.setStatus(HttpServletResponse.SC_NO_CONTENT);
			log.error("No shop was found");

		}
		finally {
			shopJSON = this.gson.toJson(shop);
			out.print(shopJSON);
		}
	}
	
	/**
	 * Function that finds and returns shop that contains most of the products which user select, throws ShopNotFoundException if no shop was found or CustomerNotFoundException if customers cart was not found
	 * @param userId
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "/Shop/Suitable", method = RequestMethod.GET, produces = "application/json")
	public void getShopWithMostWantedProducts(@RequestParam int userId, HttpServletRequest req, HttpServletResponse res )throws IOException {
        log.info("Executing getShopWithMostWantedProducts webservice with parameters - userId = ", userId);
		Shop shop = null;
		String shopJSON = null;
		PrintWriter out = res.getWriter();
		res.setCharacterEncoding("UTF-8");	
		try {
			shop = shopDAO.getSuitableShop(userId);
			log.info("Most suitable shop was found");
			//TODO add functionality
		} catch (ShopNotFoundException e) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			log.error("No shop was found");
		}catch (CustomerNotFoundException r) {
			res.setStatus(HttpServletResponse.SC_NO_CONTENT);
			log.error("Customers cart was not found");
		}
		finally {
			shopJSON = this.gson.toJson(shop);
			out.print(shopJSON);
		}
	}
	/**
	 * Function finds and returns shop sortiment based on shop id and product id, throws ShopNotFoundException if sortiment was not found
	 * @param req
	 * @param res
	 * @param shopId
	 * @param productId
	 * @throws IOException
	 */
	@RequestMapping(value = "/Shop/Sortiment", produces = "application/json")
	public void getShopSortiment(HttpServletRequest req, HttpServletResponse res, @RequestParam int shopId, @RequestParam int productId)throws IOException{
        log.info("Executing getShopWithMostWantedProducts webservice with parameters - shopId = ", shopId, " and productId = ", productId);

		Sortiment sortiment = null;
		String sortimentJSON = null;
		PrintWriter out = res.getWriter();
		res.setCharacterEncoding("UTF-8");
		try {
			sortiment = shopDAO.getShopSortiment(shopId, productId);
			log.info("Shop sortiment was found");

		} catch (ShopNotFoundException e) {
			res.setStatus(HttpServletResponse.SC_NO_CONTENT);
			log.error("Shop sortiment was not found ");
		}finally {
			sortimentJSON = this.gson.toJson(sortiment);
			out.print(sortimentJSON);
		}
	}
}
