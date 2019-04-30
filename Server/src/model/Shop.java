package model;

import java.math.BigDecimal;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Shop {

	@Id
	@GenericGenerator(name="userSequence" , strategy="increment")
	@GeneratedValue(generator="userSequence")
    private int id;
    
	private String name;
   
	@Column(precision=12, scale=8)
	private BigDecimal longitude;
    
	@Column(precision=12, scale=8)
	private BigDecimal latitude;
	
    public Shop() {
	}
    
    public Shop(int id, String name, BigDecimal longitude, BigDecimal latitude) {
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
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
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
    
}
