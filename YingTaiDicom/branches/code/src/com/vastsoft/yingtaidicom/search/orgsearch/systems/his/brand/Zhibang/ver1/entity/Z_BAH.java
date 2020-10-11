package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity;

import java.util.Date;

/**住院病历，包含病案号，就诊卡号，身份证号（猜测：住院号即为病案号）*/
public class Z_BAH {
	private String z_bah; // 病案号
	private String z_xm; // 病人姓名
	private int z_nl; // 病人年龄
	private String z_csny; // 病人出生年月
	private String z_xb; // 病人性别
	private String z_gzdw; // 工作单位
	private String mzbm; // 民族编码
	private String mzbm_name;//民族
	private String jgbm; // 籍贯编码
	private String z_jtzz; // 家庭住址
	private Date z_ryrq; // 入院日期
	private String z_fb; //
	private String czy; // 操作员
	private String z_jsr; // 经手人
	private String z_zy; // 住院 是否住院，1=是
	private Date z_cyrq; // 出院日期
	private String z_hf; //
	private String z_sfzh; // 身份证号
	private String z_ryfs; // 入院方式 暂时未知对应值
	private String z_ryqk; // 入院情况 暂时未知对应值
	private String z_mzzd; // 门诊诊断
	private String ysbm; // 医生编码
	private String ysbm_name; // 医生姓名
	private String z_sf; //
	private String gzdwdh; // 工作单位电话
	private String lxr; // 联系人
	private String lxrgx; // 联系人关系
	private String lxrdz; // 联系人地址
	private String lxrdh; // 联系人电话
	private String pydm; // 病人姓名拼音编码
	private String m_brid; // 病人ID；
	private String card_no; // 就诊卡号
	private String z_mzzd_icd; // 门诊诊断附加信息

	public String getZ_bah() {
		return z_bah;
	}

	public void setZ_bah(String z_bah) {
		this.z_bah = z_bah;
	}

	public String getZ_xm() {
		return z_xm;
	}

	public void setZ_xm(String z_xm) {
		this.z_xm = z_xm;
	}

	public int getZ_nl() {
		return z_nl;
	}

	public void setZ_nl(int z_nl) {
		this.z_nl = z_nl;
	}

	public String getZ_csny() {
		return z_csny;
	}

	public void setZ_csny(String z_csny) {
		this.z_csny = z_csny;
	}

	public String getZ_xb() {
		return z_xb;
	}

	public void setZ_xb(String z_xb) {
		this.z_xb = z_xb;
	}

	public String getZ_gzdw() {
		return z_gzdw;
	}

	public void setZ_gzdw(String z_gzdw) {
		this.z_gzdw = z_gzdw;
	}

	public String getMzbm() {
		return mzbm;
	}

	public void setMzbm(String mzbm) {
		this.mzbm = mzbm;
	}

	public String getJgbm() {
		return jgbm;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

	public String getZ_jtzz() {
		return z_jtzz;
	}

	public void setZ_jtzz(String z_jtzz) {
		this.z_jtzz = z_jtzz;
	}

	public Date getZ_ryrq() {
		return z_ryrq;
	}

	public void setZ_ryrq(Date z_ryrq) {
		this.z_ryrq = z_ryrq;
	}

	public String getZ_fb() {
		return z_fb;
	}

	public void setZ_fb(String z_fb) {
		this.z_fb = z_fb;
	}

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {
		this.czy = czy;
	}

	public String getZ_jsr() {
		return z_jsr;
	}

	public void setZ_jsr(String z_jsr) {
		this.z_jsr = z_jsr;
	}

	public String getZ_zy() {
		return z_zy;
	}

	public void setZ_zy(String z_zy) {
		this.z_zy = z_zy;
	}

	public Date getZ_cyrq() {
		return z_cyrq;
	}

	public void setZ_cyrq(Date z_cyrq) {
		this.z_cyrq = z_cyrq;
	}

	public String getZ_hf() {
		return z_hf;
	}

	public void setZ_hf(String z_hf) {
		this.z_hf = z_hf;
	}

	public String getZ_sfzh() {
		return z_sfzh;
	}

	public void setZ_sfzh(String z_sfzh) {
		this.z_sfzh = z_sfzh;
	}

	public String getZ_ryfs() {
		return z_ryfs;
	}

	public void setZ_ryfs(String z_ryfs) {
		this.z_ryfs = z_ryfs;
	}

	public String getZ_ryqk() {
		return z_ryqk;
	}

	public void setZ_ryqk(String z_ryqk) {
		this.z_ryqk = z_ryqk;
	}

	public String getZ_mzzd() {
		return z_mzzd;
	}

	public void setZ_mzzd(String z_mzzd) {
		this.z_mzzd = z_mzzd;
	}

	public String getYsbm() {
		return ysbm;
	}

	public void setYsbm(String ysbm) {
		this.ysbm = ysbm;
	}

	public String getZ_sf() {
		return z_sf;
	}

	public void setZ_sf(String z_sf) {
		this.z_sf = z_sf;
	}

	public String getGzdwdh() {
		return gzdwdh;
	}

	public void setGzdwdh(String gzdwdh) {
		this.gzdwdh = gzdwdh;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getLxrgx() {
		return lxrgx;
	}

	public void setLxrgx(String lxrgx) {
		this.lxrgx = lxrgx;
	}

	public String getLxrdz() {
		return lxrdz;
	}

	public void setLxrdz(String lxrdz) {
		this.lxrdz = lxrdz;
	}

	public String getLxrdh() {
		return lxrdh;
	}

	public void setLxrdh(String lxrdh) {
		this.lxrdh = lxrdh;
	}

	public String getPydm() {
		return pydm;
	}

	public void setPydm(String pydm) {
		this.pydm = pydm;
	}

	public String getM_brid() {
		return m_brid;
	}

	public void setM_brid(String m_brid) {
		this.m_brid = m_brid;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getZ_mzzd_icd() {
		return z_mzzd_icd;
	}

	public void setZ_mzzd_icd(String z_mzzd_icd) {
		this.z_mzzd_icd = z_mzzd_icd;
	}
	public String getMzbm_name() {
		return mzbm_name;
	}

	public void setMzbm_name(String mzbm_name) {
		this.mzbm_name = mzbm_name;
	}

	public String getYsbm_name() {
		return ysbm_name;
	}

	public void setYsbm_name(String ysbm_name) {
		this.ysbm_name = ysbm_name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((card_no == null) ? 0 : card_no.hashCode());
		result = prime * result + ((czy == null) ? 0 : czy.hashCode());
		result = prime * result + ((gzdwdh == null) ? 0 : gzdwdh.hashCode());
		result = prime * result + ((jgbm == null) ? 0 : jgbm.hashCode());
		result = prime * result + ((lxr == null) ? 0 : lxr.hashCode());
		result = prime * result + ((lxrdh == null) ? 0 : lxrdh.hashCode());
		result = prime * result + ((lxrdz == null) ? 0 : lxrdz.hashCode());
		result = prime * result + ((lxrgx == null) ? 0 : lxrgx.hashCode());
		result = prime * result + ((m_brid == null) ? 0 : m_brid.hashCode());
		result = prime * result + ((mzbm == null) ? 0 : mzbm.hashCode());
		result = prime * result + ((mzbm_name == null) ? 0 : mzbm_name.hashCode());
		result = prime * result + ((pydm == null) ? 0 : pydm.hashCode());
		result = prime * result + ((ysbm == null) ? 0 : ysbm.hashCode());
		result = prime * result + ((ysbm_name == null) ? 0 : ysbm_name.hashCode());
		result = prime * result + ((z_bah == null) ? 0 : z_bah.hashCode());
		result = prime * result + ((z_csny == null) ? 0 : z_csny.hashCode());
		result = prime * result + ((z_cyrq == null) ? 0 : z_cyrq.hashCode());
		result = prime * result + ((z_fb == null) ? 0 : z_fb.hashCode());
		result = prime * result + ((z_gzdw == null) ? 0 : z_gzdw.hashCode());
		result = prime * result + ((z_hf == null) ? 0 : z_hf.hashCode());
		result = prime * result + ((z_jsr == null) ? 0 : z_jsr.hashCode());
		result = prime * result + ((z_jtzz == null) ? 0 : z_jtzz.hashCode());
		result = prime * result + ((z_mzzd == null) ? 0 : z_mzzd.hashCode());
		result = prime * result + ((z_mzzd_icd == null) ? 0 : z_mzzd_icd.hashCode());
		result = prime * result + z_nl;
		result = prime * result + ((z_ryfs == null) ? 0 : z_ryfs.hashCode());
		result = prime * result + ((z_ryqk == null) ? 0 : z_ryqk.hashCode());
		result = prime * result + ((z_ryrq == null) ? 0 : z_ryrq.hashCode());
		result = prime * result + ((z_sf == null) ? 0 : z_sf.hashCode());
		result = prime * result + ((z_sfzh == null) ? 0 : z_sfzh.hashCode());
		result = prime * result + ((z_xb == null) ? 0 : z_xb.hashCode());
		result = prime * result + ((z_xm == null) ? 0 : z_xm.hashCode());
		result = prime * result + ((z_zy == null) ? 0 : z_zy.hashCode());
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
		Z_BAH other = (Z_BAH) obj;
		if (card_no == null) {
			if (other.card_no != null)
				return false;
		} else if (!card_no.equals(other.card_no))
			return false;
		if (czy == null) {
			if (other.czy != null)
				return false;
		} else if (!czy.equals(other.czy))
			return false;
		if (gzdwdh == null) {
			if (other.gzdwdh != null)
				return false;
		} else if (!gzdwdh.equals(other.gzdwdh))
			return false;
		if (jgbm == null) {
			if (other.jgbm != null)
				return false;
		} else if (!jgbm.equals(other.jgbm))
			return false;
		if (lxr == null) {
			if (other.lxr != null)
				return false;
		} else if (!lxr.equals(other.lxr))
			return false;
		if (lxrdh == null) {
			if (other.lxrdh != null)
				return false;
		} else if (!lxrdh.equals(other.lxrdh))
			return false;
		if (lxrdz == null) {
			if (other.lxrdz != null)
				return false;
		} else if (!lxrdz.equals(other.lxrdz))
			return false;
		if (lxrgx == null) {
			if (other.lxrgx != null)
				return false;
		} else if (!lxrgx.equals(other.lxrgx))
			return false;
		if (m_brid == null) {
			if (other.m_brid != null)
				return false;
		} else if (!m_brid.equals(other.m_brid))
			return false;
		if (mzbm == null) {
			if (other.mzbm != null)
				return false;
		} else if (!mzbm.equals(other.mzbm))
			return false;
		if (mzbm_name == null) {
			if (other.mzbm_name != null)
				return false;
		} else if (!mzbm_name.equals(other.mzbm_name))
			return false;
		if (pydm == null) {
			if (other.pydm != null)
				return false;
		} else if (!pydm.equals(other.pydm))
			return false;
		if (ysbm == null) {
			if (other.ysbm != null)
				return false;
		} else if (!ysbm.equals(other.ysbm))
			return false;
		if (ysbm_name == null) {
			if (other.ysbm_name != null)
				return false;
		} else if (!ysbm_name.equals(other.ysbm_name))
			return false;
		if (z_bah == null) {
			if (other.z_bah != null)
				return false;
		} else if (!z_bah.equals(other.z_bah))
			return false;
		if (z_csny == null) {
			if (other.z_csny != null)
				return false;
		} else if (!z_csny.equals(other.z_csny))
			return false;
		if (z_cyrq == null) {
			if (other.z_cyrq != null)
				return false;
		} else if (!z_cyrq.equals(other.z_cyrq))
			return false;
		if (z_fb == null) {
			if (other.z_fb != null)
				return false;
		} else if (!z_fb.equals(other.z_fb))
			return false;
		if (z_gzdw == null) {
			if (other.z_gzdw != null)
				return false;
		} else if (!z_gzdw.equals(other.z_gzdw))
			return false;
		if (z_hf == null) {
			if (other.z_hf != null)
				return false;
		} else if (!z_hf.equals(other.z_hf))
			return false;
		if (z_jsr == null) {
			if (other.z_jsr != null)
				return false;
		} else if (!z_jsr.equals(other.z_jsr))
			return false;
		if (z_jtzz == null) {
			if (other.z_jtzz != null)
				return false;
		} else if (!z_jtzz.equals(other.z_jtzz))
			return false;
		if (z_mzzd == null) {
			if (other.z_mzzd != null)
				return false;
		} else if (!z_mzzd.equals(other.z_mzzd))
			return false;
		if (z_mzzd_icd == null) {
			if (other.z_mzzd_icd != null)
				return false;
		} else if (!z_mzzd_icd.equals(other.z_mzzd_icd))
			return false;
		if (z_nl != other.z_nl)
			return false;
		if (z_ryfs == null) {
			if (other.z_ryfs != null)
				return false;
		} else if (!z_ryfs.equals(other.z_ryfs))
			return false;
		if (z_ryqk == null) {
			if (other.z_ryqk != null)
				return false;
		} else if (!z_ryqk.equals(other.z_ryqk))
			return false;
		if (z_ryrq == null) {
			if (other.z_ryrq != null)
				return false;
		} else if (!z_ryrq.equals(other.z_ryrq))
			return false;
		if (z_sf == null) {
			if (other.z_sf != null)
				return false;
		} else if (!z_sf.equals(other.z_sf))
			return false;
		if (z_sfzh == null) {
			if (other.z_sfzh != null)
				return false;
		} else if (!z_sfzh.equals(other.z_sfzh))
			return false;
		if (z_xb == null) {
			if (other.z_xb != null)
				return false;
		} else if (!z_xb.equals(other.z_xb))
			return false;
		if (z_xm == null) {
			if (other.z_xm != null)
				return false;
		} else if (!z_xm.equals(other.z_xm))
			return false;
		if (z_zy == null) {
			if (other.z_zy != null)
				return false;
		} else if (!z_zy.equals(other.z_zy))
			return false;
		return true;
	}
}
