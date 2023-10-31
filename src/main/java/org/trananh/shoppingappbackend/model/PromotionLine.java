package org.trananh.shoppingappbackend.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "promotion_lines")
public class PromotionLine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "promotion_line_id_generator")
	@GenericGenerator(name = "promotion_line_id_generator", strategy = "org.trananh.shoppingappbackend.ultilities.idGenerator.PromotionLineIdGenerator")
	private String id;
	
	@Column(name = "type", nullable = false)
	private int type;
	
	@ManyToOne
	@JoinColumn(name = "promotion_header_id")
	private PromotionHeader promotionHeader;
	
	@OneToMany(mappedBy = "promotionLine")
	private List<PromotionDetail> promotionDetails;

	public PromotionLine() {
		super();
	}

	public PromotionLine(String id) {
		super();
		this.id = id;
	}

	public PromotionLine(String id, int type, PromotionHeader promotionHeader) {
		super();
		this.id = id;
		this.type = type;
		this.promotionHeader = promotionHeader;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public PromotionHeader getPromotionHeader() {
		return promotionHeader;
	}

	public void setPromotionHeader(PromotionHeader promotionHeader) {
		this.promotionHeader = promotionHeader;
	}

	@Override
	public String toString() {
		return "PromotionLine [id=" + id + ", type=" + type + ", promotionHeader=" + promotionHeader + "]";
	}
	
}
