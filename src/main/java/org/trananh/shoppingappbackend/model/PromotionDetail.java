package org.trananh.shoppingappbackend.model;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "promotion_details")
public class PromotionDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "promotion_detail_id_generator")
	@GenericGenerator(name = "promotion_detail_id_generator", strategy = "org.trananh.shoppingappbackend.ultilities.idGenerator.PromotionDetailIdGenerator")
	private String id;
	
	@Column(name = "product_id", nullable = true)
	private String productId;
	
	@Column(name = "purchase_value", nullable = false)
	private double purchaseValue;
	
	@Column(name = "promotion_value", nullable = false)
	private double promotionValue;
	
	@Column(name = "max_turn", nullable = false)
	private int maxTurn;
	
	@Column(name = "max_money", nullable = false)
	private double maxMoney;
	
	@ManyToOne
	@JoinColumn(name = "promotion_line_id")
	private PromotionLine promotionLine;

	public PromotionDetail() {
		super();
	}

	public PromotionDetail(String id) {
		super();
		this.id = id;
	}

	public PromotionDetail(String id, String productId, double purchaseValue, double promotionValue, int maxTurn,
			double maxMoney, PromotionLine promotionLine) {
		super();
		this.id = id;
		this.productId = productId;
		this.purchaseValue = purchaseValue;
		this.promotionValue = promotionValue;
		this.maxTurn = maxTurn;
		this.maxMoney = maxMoney;
		this.promotionLine = promotionLine;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public double getPurchaseValue() {
		return purchaseValue;
	}

	public void setPurchaseValue(double purchaseValue) {
		this.purchaseValue = purchaseValue;
	}

	public double getPromotionValue() {
		return promotionValue;
	}

	public void setPromotionValue(double promotionValue) {
		this.promotionValue = promotionValue;
	}

	public int getMaxTurn() {
		return maxTurn;
	}

	public void setMaxTurn(int maxTurn) {
		this.maxTurn = maxTurn;
	}

	public double getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(double maxMoney) {
		this.maxMoney = maxMoney;
	}

	public PromotionLine getPromotionLine() {
		return promotionLine;
	}

	public void setPromotionLine(PromotionLine promotionLine) {
		this.promotionLine = promotionLine;
	}

	@Override
	public String toString() {
		return "PromotionDetail [id=" + id + ", productId=" + productId + ", purchaseValue=" + purchaseValue
				+ ", promotionValue=" + promotionValue + ", maxTurn=" + maxTurn + ", maxMoney=" + maxMoney
				+ ", promotionLine=" + promotionLine + "]";
	}
	
}
