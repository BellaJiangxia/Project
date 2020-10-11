package com.vastsoft.yingtai.module.org.product.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductStatus;

public class TOrgProduct {
	private long id;
	private long org_id;
	private String name;
	private long device_type_id;
	private long part_type_id = 0;
	private String description;
	private long creator_id;
	private Date create_time;
	private int status;
	private int charge_type;// 收费类型 参阅类：OrgProductChargeType
	private double price;// 单价

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getDevice_type_id() {
		return device_type_id;
	}

	public void setDevice_type_id(long device_type_id) {
		this.device_type_id = device_type_id;
	}

	public long getPart_type_id() {
		return part_type_id;
	}

	public void setPart_type_id(long part_type_id) {
		this.part_type_id = part_type_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(long creator_id) {
		this.creator_id = creator_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}
	
	public String getStatus_name() {
		OrgProductStatus ps =  OrgProductStatus.parseCode(status);
		return ps==null?"":ps.getName();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCharge_type() {
		return charge_type;
	}
	
	public String getCharge_type_name() {
		OrgProductChargeType opct = OrgProductChargeType.parseCode(charge_type);
		return opct==null?"":opct.getName();
	}

	public void setCharge_type(int charge_type) {
		this.charge_type = charge_type;
	}
}
