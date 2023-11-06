package org.trananh.shoppingappbackend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "unit_of_measures")
public class UnitOfMeasure implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "unit_of_measure_id_generator")
	@GenericGenerator(name = "unit_of_measure_id_generator", strategy = "org.trananh.shoppingappbackend.ultilities.idGenerator.UnitOfMeasureIdGenerator")
	private int id;
	
	@Column(name = "value", columnDefinition = "nvarchar(max)")
	private String value;
	
	@Column(name = "quantity")
	private int quantity;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@ManyToOne
	@JoinColumn(name = "base_unit_of_measure_id")
	private BaseUnitOfMeasure baseUnitOfMeasure;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@OneToMany(mappedBy = "unitOfMeasure")
	private List<PriceDetail> priceDetails;
	
	@OneToMany(mappedBy = "unitOfMeasure")
	private List<WarehouseHeaderDetail> warehouseHeaderDetails;
	
	@OneToMany(mappedBy = "unitOfMeasure")
	private List<Cart> carts;
	
	public UnitOfMeasure() {
		this.priceDetails = new ArrayList<PriceDetail>();
		this.warehouseHeaderDetails = new ArrayList<WarehouseHeaderDetail>();
		this.carts = new ArrayList<Cart>();
	}

	public UnitOfMeasure(int id) {
		super();
		this.id = id;
		this.priceDetails = new ArrayList<PriceDetail>();
		this.warehouseHeaderDetails = new ArrayList<WarehouseHeaderDetail>();
		this.carts = new ArrayList<Cart>();
	}

	public UnitOfMeasure(int id, String value,BaseUnitOfMeasure baseUnitOfMeasure, Product product, int quantity, String imageUrl) {
		super();
		this.id = id;
		this.value = value;
		this.baseUnitOfMeasure = baseUnitOfMeasure;
		this.product = product;
		this.quantity = quantity;
		this.imageUrl = imageUrl;
		this.carts = new ArrayList<Cart>();
	}

	public BaseUnitOfMeasure getBaseUnitOfMeasure() {
		return baseUnitOfMeasure;
	}

	public void setBaseUnitOfMeasure(BaseUnitOfMeasure baseUnitOfMeasure) {
		this.baseUnitOfMeasure = baseUnitOfMeasure;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "UnitOfMeasure [id=" + id + ", value=" + value + ", quantity=" + quantity + ", imageUrl=" + imageUrl
				+ ", baseUnitOfMeasure=" + baseUnitOfMeasure + ", product=" + product + "]";
	}
	
}
