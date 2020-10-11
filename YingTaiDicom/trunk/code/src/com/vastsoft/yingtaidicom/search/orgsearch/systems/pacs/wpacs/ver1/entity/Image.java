package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity;

public class Image {
	private String imguid;
	private String imgno;
	private String clsuid;
	private String srsuid;
	private String stuuid;
	private String fpath;
	private String acqno;
	private String vurl;
	
	private byte[] thumbnail;

	public String getImguid() {
		return imguid;
	}

	public void setImguid(String imguid) {
		this.imguid = imguid;
	}

	public String getImgno() {
		return imgno;
	}

	public void setImgno(String imgno) {
		this.imgno = imgno;
	}

	public String getClsuid() {
		return clsuid;
	}

	public void setClsuid(String clsuid) {
		this.clsuid = clsuid;
	}

	public String getSrsuid() {
		return srsuid;
	}

	public void setSrsuid(String srsuid) {
		this.srsuid = srsuid;
	}

	public String getStuuid() {
		return stuuid;
	}

	public void setStuuid(String stuuid) {
		this.stuuid = stuuid;
	}

	public String getFpath() {
		return fpath;
	}

	public void setFpath(String fpath) {
		this.fpath = fpath;
	}

	public String getAcqno() {
		return acqno;
	}

	public void setAcqno(String acqno) {
		this.acqno = acqno;
	}

	public String getVurl() {
		return vurl;
	}

	public void setVurl(String vurl) {
		this.vurl = vurl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acqno == null) ? 0 : acqno.hashCode());
		result = prime * result + ((clsuid == null) ? 0 : clsuid.hashCode());
		result = prime * result + ((fpath == null) ? 0 : fpath.hashCode());
		result = prime * result + ((imgno == null) ? 0 : imgno.hashCode());
		result = prime * result + ((imguid == null) ? 0 : imguid.hashCode());
		result = prime * result + ((srsuid == null) ? 0 : srsuid.hashCode());
		result = prime * result + ((stuuid == null) ? 0 : stuuid.hashCode());
		result = prime * result + ((vurl == null) ? 0 : vurl.hashCode());
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
		Image other = (Image) obj;
		if (acqno == null) {
			if (other.acqno != null)
				return false;
		} else if (!acqno.equals(other.acqno))
			return false;
		if (clsuid == null) {
			if (other.clsuid != null)
				return false;
		} else if (!clsuid.equals(other.clsuid))
			return false;
		if (fpath == null) {
			if (other.fpath != null)
				return false;
		} else if (!fpath.equals(other.fpath))
			return false;
		if (imgno == null) {
			if (other.imgno != null)
				return false;
		} else if (!imgno.equals(other.imgno))
			return false;
		if (imguid == null) {
			if (other.imguid != null)
				return false;
		} else if (!imguid.equals(other.imguid))
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
		if (vurl == null) {
			if (other.vurl != null)
				return false;
		} else if (!vurl.equals(other.vurl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TImages [imguid=" + imguid + ", imgno=" + imgno + ", clsuid=" + clsuid + ", srsuid=" + srsuid
				+ ", stuuid=" + stuuid + ", fpath=" + fpath + ", acqno=" + acqno + ", vurl=" + vurl + "]";
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

}
