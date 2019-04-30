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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import database.ProductDAO;
import model.Product;

@Controller("/Product")
public class ProductController {
	private Gson gson = new Gson();
	@Autowired
	private ProductDAO productDAO;
	private static Logger log = LoggerFactory.getLogger(ProductController.class.getName());
	
	@RequestMapping(value = "/Product/QR",produces = "application/json")
	public void getProductByQR(HttpServletRequest request, HttpServletResponse response, @RequestParam int QR) throws IOException{
        log.info("Executing getProductByQR webservice with parameters: QR = ",QR);
		Product product = null;
		String productJSON = null;
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		try {
			product = productDAO.getProductQR(QR);
			productJSON = this.gson.toJson(product);
			log.info("Found product with name: " + product.getName());
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			System.out.println("Produkt sa nenasiel");
			log.info("Did not found product with QR: ",QR);

			}finally {
			out.print(productJSON);
			out.close();
		}
	}
	
	@RequestMapping(value = "/Product/Category", produces = "application/json")
	public void getProductByCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam String category) throws IOException{
        log.info("Executing getProductByCategory webservice with parameters - category: " + category);
		List<Product> product = new ArrayList<Product>();
		String productJSON = null;
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		try {
			product = productDAO.getProductsByCategory(category);
			productJSON = this.gson.toJson(product);
			log.info("Found products in category: " + category);
			System.out.println("Found products: " + product);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			System.out.println("Ziaden produkt sa nenasiel v kategorii " + category);
			log.info("Didn't found products in category: " + category);

			}finally {
			out.print(productJSON);
			out.close();
		}
	}
}
