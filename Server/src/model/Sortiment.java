/**
 * Created by Marko Ondrejicka
 */

package model;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.ArrayList;

@Entity
public class Sortiment {

	@Id
	@GenericGenerator(name="userSequence" , strategy="increment")
	@GeneratedValue(generator="userSequence")
    private int id;
	
    private double price;
    
    private int amount;

    @ManyToOne
    @JoinColumn
    private Product product;
    
    @ManyToOne
    @JoinColumn
    private Shop shop;

    public Sortiment() {
	}
    
	public Sortiment(int id, double price, int amount, Product product, Shop shop) {
		this.id = id;
		this.price = price;
		this.amount = amount;
		this.product = product;
		this.shop = shop;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
}
