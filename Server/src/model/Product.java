/**
 * Created by Marko Ondrejicka
 */

package model;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Product {
	@Id
	@GenericGenerator(name="userSequence" , strategy="increment")
	@GeneratedValue(generator="userSequence")
    private int id;
   
    private String name;
   
    private String category;
   
    private String picture;

    public Product() {
	}
    
	public Product(int id, String name, String category, String picture) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.picture = picture;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
