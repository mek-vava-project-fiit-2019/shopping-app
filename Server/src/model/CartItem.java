package model;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.ArrayList;

@Entity
public class CartItem {
	
	@Id
	@GenericGenerator(name="userSequence" , strategy="increment")
	@GeneratedValue(generator="userSequence")
    private int id;
    
	private int amount;
    
	@ManyToOne
	@JoinColumn
	private Customer user;
    
	@ManyToOne
	@JoinColumn
	private Product product;
    
    public CartItem() {
	}
    
	public CartItem(int id, int amount, Customer user, Product product) {
		this.id = id;
		this.amount = amount;
		this.user = user;
		this.product = product;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Customer getUser() {
		return user;
	}
	public void setUser(Customer user) {
		this.user = user;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

    
}
