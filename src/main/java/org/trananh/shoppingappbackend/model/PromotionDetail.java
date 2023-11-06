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
	
	@Column(name = "unit_of_measure_id", nullable = true)
	private int unitOfMeasureId;
	
	@Column(name = "purchase_value", nullable = false)
	private double purchaseValue;
	
	@Column(name = "promotion_value", nullable = false)
	private double promotionValue;
	
	@Column(name = "max_value", nullable = false)
	private double maxValue;
	
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

	public PromotionDetail(String id, int unitOfMeasureId, double purchaseValue, double promotionValue, double maxValue, PromotionLine promotionLine) {
		super();
		this.id = id;
		this.purchaseValue = purchaseValue;
		this.promotionValue = promotionValue;
		this.maxValue = maxValue;
		this.promotionLine = promotionLine;
		this.unitOfMeasureId = unitOfMeasureId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public PromotionLine getPromotionLine() {
		return promotionLine;
	}

	public void setPromotionLine(PromotionLine promotionLine) {
		this.promotionLine = promotionLine;
	}

	public int getUnitOfMeasureId() {
		return unitOfMeasureId;
	}

	public void setUnitOfMeasureId(int unitOfMeasureId) {
		this.unitOfMeasureId = unitOfMeasureId;
	}

	@Override
	public String toString() {
		return "PromotionDetail [id=" + id + ", unitOfMeasureId=" + unitOfMeasureId + ", purchaseValue=" + purchaseValue
				+ ", promotionValue=" + promotionValue + ", maxValue=" + maxValue + ", promotionLine=" + promotionLine
				+ "]";
	}
	
}
