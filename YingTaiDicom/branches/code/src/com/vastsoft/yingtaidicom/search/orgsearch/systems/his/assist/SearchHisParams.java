package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.assist;

import com.vastsoft.util.db.AbstractSearchParam;

/**
 * @author jben
 *
 */
public class SearchHisParams extends AbstractSearchParam{
	private String identity_id;
	private long bingan_id;
	private String bingan_num;
//	private String zhuyuan_num;
	private String his_card_num;
	
	public SearchHisParams(){
	}
	
	public SearchHisParams(String identity_id2) {
		this.identity_id = identity_id2;
	}
	public SearchHisParams(long bingan_id2) {
		this.bingan_id = bingan_id2;
	}

	public String getIdentity_id() {
		return identity_id;
	}
	public void setIdentity_id(String identity_id) {
		this.identity_id = identity_id;
	}
	public long getBingan_id() {
		return bingan_id;
	}
	public void setBingan_id(long bingan_id) {
		this.bingan_id = bingan_id;
	}

	public String getBingan_num() {
		return bingan_num;
	}

	public void setBingan_num(String bingan_num) {
		this.bingan_num = bingan_num;
	}

//	public String getZhuyuan_num() {
//		return zhuyuan_num;
//	}
//
//	public void setZhuyuan_num(String zhuyuan_num) {
//		this.zhuyuan_num = zhuyuan_num;
//	}

	public String getHis_card_num() {
		return his_card_num;
	}

	public void setHis_card_num(String his_card_num) {
		this.his_card_num = his_card_num;
	}
}
