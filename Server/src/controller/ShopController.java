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

	@RequestMapping(value = "/Shop/Near", method = RequestMethod.GET, produces = "application/json")
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
			log.error("ShopNotFoundException was thrown, did not found any shop");	

		}
		finally {
			shopJSON = this.gson.toJson(shop);
			out.print(shopJSON);


		}
	}
	
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
			log.error("ShoptNotFoundException was thrown, no shop was found");

		}
		finally {
			shopJSON = this.gson.toJson(shop);
			out.print(shopJSON);
		}
	}
	
	@RequestMapping(value = "/shops/todo", method = RequestMethod.GET, produces = "application/json")
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
			log.error("ShopNotFoundException was thrown, no shop was found");
		}catch (CustomerNotFoundException r) {
			res.setStatus(HttpServletResponse.SC_NO_CONTENT);
			log.error("CustomerNotFoundException was thrown, customers cart was not found");

			// TODO: handle exception
		}
		finally {
			shopJSON = this.gson.toJson(shop);
			out.print(shopJSON);
		}
	}
	@RequestMapping(value = "/Shop/Sortiment", produces = "application/json")
	public void getShopSortiment(HttpServletRequest req, HttpServletResponse res, @RequestParam int shopId, @RequestParam int productId)throws IOException{
		Sortiment sortiment = null;
		String sortimentJSON = null;
		PrintWriter out = res.getWriter();
		res.setCharacterEncoding("UTF-8");
		try {
			sortiment = shopDAO.getShopSortiment(shopId, productId);
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_NO_CONTENT);
			// TODO: handle exception
		}finally {
			sortimentJSON = this.gson.toJson(sortiment);
			out.print(sortimentJSON);
		}
	}
}
