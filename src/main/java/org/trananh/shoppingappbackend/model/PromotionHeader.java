package org.trananh.shoppingappbackend.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
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
@Table(name = "promotion_headers")
public class PromotionHeader implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "promotion_header_id_generator")
	@GenericGenerator(name = "promotion_header_id_generator", strategy = "org.trananh.shoppingappbackend.ultilities.idGenerator.PromotionHeaderIdGenerator")
	private String id;
	
	@Column(name = "name", nullable = false, columnDefinition = "nvarchar(max)")
	private String name;
	
	@Column(name = "description", nullable = true, columnDefinition = "nvarchar(max)")
	private String description;
	
	@Column(name = "start_date", nullable = true)
	private Date startDate;
	
	@Column(name = "end_date", nullable = true)
	private Date endDate;
	
	@Column(name = "status", nullable = false)
	private int status = 0;
	
	@ManyToOne
	@JoinColumn(name = "create_user_id")
	private User createUser;
	
	@OneToMany(mappedBy = "promotionHeader")
	private List<PromotionLine> promotionLines;

	public PromotionHeader() {
		super();
	}

	public PromotionHeader(String id) {
		super();
		this.id = id;
	}

	public PromotionHeader(String id, String name, String description, Date startDate, Date endDate,
			int status, User createUser) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.createUser = createUser;
		this.promotionLines = new ArrayList<PromotionLine>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int isStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Override
	public String toString() {
		return "PromotionHeader [id=" + id + ", name=" + name + ", description=" + description + ", startDate="
				+ startDate + ", endDate=" + endDate + ", status=" + status + ", createUser=" + createUser + "]";
	}
	
	
}
