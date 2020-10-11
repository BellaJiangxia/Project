package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity;

import java.util.ArrayList;
import java.util.List;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;

public class Series {
	public static final String UNKNOW_BODYPART = "UNKNOW_BODYPART";
	private String srsuid;
    private String srsid;
    private String srsdesc;
    private String modality;
    private String stuuid;
    
    private List<Image> listImage;
    
	public String getSrsuid() {
		return srsuid;
	}
	public void setSrsuid(String srsuid) {
		this.srsuid = srsuid;
	}
	public String getSrsid() {
		return srsid;
	}
	public void setSrsid(String srsid) {
		this.srsid = srsid;
	}
	public String getSrsdesc() {
		return srsdesc;
	}
	public void setSrsdesc(String srsdesc) {
		this.srsdesc = srsdesc;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getStuuid() {
		return stuuid;
	}
	public void setStuuid(String stuuid) {
		this.stuuid = stuuid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modality == null) ? 0 : modality.hashCode());
		result = prime * result + ((srsdesc == null) ? 0 : srsdesc.hashCode());
		result = prime * result + ((srsid == null) ? 0 : srsid.hashCode());
		result = prime * result + ((srsuid == null) ? 0 : srsuid.hashCode());
		result = prime * result + ((stuuid == null) ? 0 : stuuid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Series other = (Series) obj;
		if (modality == null) {
			if (other.modality != null)
				return false;
		} else if (!modality.equals(other.modality))
			return false;
		if (srsdesc == null) {
			if (other.srsdesc != null)
				return false;
		} else if (!srsdesc.equals(other.srsdesc))
			return false;
		if (srsid == null) {
			if (other.srsid != null)
				return false;
		} else if (!srsid.equals(other.srsid))
			return false;
		if (srsuid == null) {
			if (other.srsuid != null)
				return false;
		} else if (!srsuid.equals(other.srsuid))
			return false;
		if (stuuid == null) {
			if (other.stuuid != null)
				return false;
		} else if (!stuuid.equals(other.stuuid))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TSeries [srsuid=" + srsuid + ", srsid=" + srsid + ", srsdesc=" + srsdesc + ", modality=" + modality
				+ ", stuuid=" + stuuid + "]";
	}
	public List<Image> getListImage() {
		return listImage;
	}
	public void setListImage(List<Image> listImage) {
		this.listImage = listImage;
	}
	public void addImage(Image tImage) {
		if (tImage == null)
			return;
		if (this.listImage == null)
			this.listImage = new ArrayList<>();
		this.listImage.add(tImage);
	}
	
	
	private String thrumbnail_uid;
	private byte[] thrumbnail_data;
	
	
	public String getThrumbnail_uid() {
		this.buildThrumbnail();
		return this.thrumbnail_uid;
	}
	private void buildThrumbnail() {
		if (!ArrayTools.isEmpty(this.thrumbnail_data))
			return;
		if (CollectionTools.isEmpty(this.listImage))
			return;
		for (Image image : listImage) {
			if (!ArrayTools.isEmpty(image.getThumbnail())) {
				this.thrumbnail_uid = image.getImguid();
				this.thrumbnail_data = image.getThumbnail();
				return;
			}
		}
	}
	public byte[] getThumbnail_data() {
		this.buildThrumbnail();
		return this.thrumbnail_data;
	}
	public int getExpose_times() {
		return this.listImage == null ? 0 : this.listImage.size();
	}
}
