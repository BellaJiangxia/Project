package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity;

import java.util.Date;

/** 门诊，病人基本信息,包含病人ID，就诊卡号，身份证号 */
public class M_BRJBXX {
	private String m_brid;// 病人ID
	private String m_brxm;// 病人性别 1-男；2-女
	private String m_sfzh;// 身份证号
	private Date m_csny;// 出生年月
	private String m_xbbm;// 病人编码
	private String m_gzdw;// 工作单位
	private String m_zz;// 住址
	private String m_lxdh;// 联系电话
	private String m_mz;// 民族
	private String m_mz_name;// 民族
	private String m_fb;// ？？
	private String pydm;// 病人拼音代码
	private String card_no;// 就诊卡号
	private Date create_date;// 创建日期
	private String ybcard;//
	
	private String jzxh;//就诊序号
	private Date rbrq;//就诊日期
	private String diagnosis;//诊断
	private String zyxs;//病人主诉
	private String zyxs2;//病人主诉2
	private String zyxs3;//病人主诉3
	private String doctor_say;//医嘱
	private String m_ghmc;//挂号名称
	private String ysxm;//医生姓名
	private String ksmc;//科室名称
	private String ct;//查体
	private String notes;//备注

	public String getM_brid() {
		return m_brid;
	}

	public void setM_brid(String m_brid) {
		this.m_brid = m_brid;
	}

	public String getM_brxm() {
		return m_brxm;
	}

	public void setM_brxm(String m_brxm) {
		this.m_brxm = m_brxm;
	}

	public String getM_sfzh() {
		return m_sfzh;
	}

	public void setM_sfzh(String m_sfzh) {
		this.m_sfzh = m_sfzh;
	}

	public Date getM_csny() {
		return m_csny;
	}

	public void setM_csny(Date m_csny) {
		this.m_csny = m_csny;
	}

	public String getM_xbbm() {
		return m_xbbm;
	}

	public void setM_xbbm(String m_xbbm) {
		this.m_xbbm = m_xbbm;
	}

	public String getM_gzdw() {
		return m_gzdw;
	}

	public void setM_gzdw(String m_gzdw) {
		this.m_gzdw = m_gzdw;
	}

	public String getM_zz() {
		return m_zz;
	}

	public void setM_zz(String m_zz) {
		this.m_zz = m_zz;
	}

	public String getM_lxdh() {
		return m_lxdh;
	}

	public void setM_lxdh(String m_lxdh) {
		this.m_lxdh = m_lxdh;
	}

	public String getM_mz() {
		return m_mz;
	}

	public void setM_mz(String m_mz) {
		this.m_mz = m_mz;
	}

	public String getM_fb() {
		return m_fb;
	}

	public void setM_fb(String m_fb) {
		this.m_fb = m_fb;
	}

	public String getPydm() {
		return pydm;
	}

	public void setPydm(String pydm) {
		this.pydm = pydm;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getYbcard() {
		return ybcard;
	}

	public void setYbcard(String ybcard) {
		this.ybcard = ybcard;
	}


	public String getM_mz_name() {
		return m_mz_name;
	}

	public void setM_mz_name(String m_mz_name) {
		this.m_mz_name = m_mz_name;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getZyxs() {
		return zyxs;
	}

	public void setZyxs(String zyxs) {
		this.zyxs = zyxs;
	}

	public String getDoctor_say() {
		return doctor_say;
	}

	public void setDoctor_say(String doctor_say) {
		this.doctor_say = doctor_say;
	}

	public String getM_ghmc() {
		return m_ghmc;
	}

	public void setM_ghmc(String m_ghmc) {
		this.m_ghmc = m_ghmc;
	}

	public String getYsxm() {
		return ysxm;
	}

	public void setYsxm(String ysxm) {
		this.ysxm = ysxm;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((card_no == null) ? 0 : card_no.hashCode());
		result = prime * result + ((create_date == null) ? 0 : create_date.hashCode());
		result = prime * result + ((ct == null) ? 0 : ct.hashCode());
		result = prime * result + ((diagnosis == null) ? 0 : diagnosis.hashCode());
		result = prime * result + ((doctor_say == null) ? 0 : doctor_say.hashCode());
		result = prime * result + ((ksmc == null) ? 0 : ksmc.hashCode());
		result = prime * result + ((m_brid == null) ? 0 : m_brid.hashCode());
		result = prime * result + ((m_brxm == null) ? 0 : m_brxm.hashCode());
		result = prime * result + ((m_csny == null) ? 0 : m_csny.hashCode());
		result = prime * result + ((m_fb == null) ? 0 : m_fb.hashCode());
		result = prime * result + ((m_ghmc == null) ? 0 : m_ghmc.hashCode());
		result = prime * result + ((m_gzdw == null) ? 0 : m_gzdw.hashCode());
		result = prime * result + ((m_lxdh == null) ? 0 : m_lxdh.hashCode());
		result = prime * result + ((m_mz == null) ? 0 : m_mz.hashCode());
		result = prime * result + ((m_mz_name == null) ? 0 : m_mz_name.hashCode());
		result = prime * result + ((m_sfzh == null) ? 0 : m_sfzh.hashCode());
		result = prime * result + ((m_xbbm == null) ? 0 : m_xbbm.hashCode());
		result = prime * result + ((m_zz == null) ? 0 : m_zz.hashCode());
		result = prime * result + ((pydm == null) ? 0 : pydm.hashCode());
		result = prime * result + ((ybcard == null) ? 0 : ybcard.hashCode());
		result = prime * result + ((ysxm == null) ? 0 : ysxm.hashCode());
		result = prime * result + ((zyxs == null) ? 0 : zyxs.hashCode());
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
		M_BRJBXX other = (M_BRJBXX) obj;
		if (card_no == null) {
			if (other.card_no != null)
				return false;
		} else if (!card_no.equals(other.card_no))
			return false;
		if (create_date == null) {
			if (other.create_date != null)
				return false;
		} else if (!create_date.equals(other.create_date))
			return false;
		if (ct == null) {
			if (other.ct != null)
				return false;
		} else if (!ct.equals(other.ct))
			return false;
		if (diagnosis == null) {
			if (other.diagnosis != null)
				return false;
		} else if (!diagnosis.equals(other.diagnosis))
			return false;
		if (doctor_say == null) {
			if (other.doctor_say != null)
				return false;
		} else if (!doctor_say.equals(other.doctor_say))
			return false;
		if (ksmc == null) {
			if (other.ksmc != null)
				return false;
		} else if (!ksmc.equals(other.ksmc))
			return false;
		if (m_brid == null) {
			if (other.m_brid != null)
				return false;
		} else if (!m_brid.equals(other.m_brid))
			return false;
		if (m_brxm == null) {
			if (other.m_brxm != null)
				return false;
		} else if (!m_brxm.equals(other.m_brxm))
			return false;
		if (m_csny == null) {
			if (other.m_csny != null)
				return false;
		} else if (!m_csny.equals(other.m_csny))
			return false;
		if (m_fb == null) {
			if (other.m_fb != null)
				return false;
		} else if (!m_fb.equals(other.m_fb))
			return false;
		if (m_ghmc == null) {
			if (other.m_ghmc != null)
				return false;
		} else if (!m_ghmc.equals(other.m_ghmc))
			return false;
		if (m_gzdw == null) {
			if (other.m_gzdw != null)
				return false;
		} else if (!m_gzdw.equals(other.m_gzdw))
			return false;
		if (m_lxdh == null) {
			if (other.m_lxdh != null)
				return false;
		} else if (!m_lxdh.equals(other.m_lxdh))
			return false;
		if (m_mz == null) {
			if (other.m_mz != null)
				return false;
		} else if (!m_mz.equals(other.m_mz))
			return false;
		if (m_mz_name == null) {
			if (other.m_mz_name != null)
				return false;
		} else if (!m_mz_name.equals(other.m_mz_name))
			return false;
		if (m_sfzh == null) {
			if (other.m_sfzh != null)
				return false;
		} else if (!m_sfzh.equals(other.m_sfzh))
			return false;
		if (m_xbbm == null) {
			if (other.m_xbbm != null)
				return false;
		} else if (!m_xbbm.equals(other.m_xbbm))
			return false;
		if (m_zz == null) {
			if (other.m_zz != null)
				return false;
		} else if (!m_zz.equals(other.m_zz))
			return false;
		if (pydm == null) {
			if (other.pydm != null)
				return false;
		} else if (!pydm.equals(other.pydm))
			return false;
		if (ybcard == null) {
			if (other.ybcard != null)
				return false;
		} else if (!ybcard.equals(other.ybcard))
			return false;
		if (ysxm == null) {
			if (other.ysxm != null)
				return false;
		} else if (!ysxm.equals(other.ysxm))
			return false;
		if (zyxs == null) {
			if (other.zyxs != null)
				return false;
		} else if (!zyxs.equals(other.zyxs))
			return false;
		return true;
	}

	public String getJzxh() {
		return jzxh;
	}

	public void setJzxh(String jzxh) {
		this.jzxh = jzxh;
	}

	public Date getRbrq() {
		return rbrq;
	}

	public void setRbrq(Date rbrq) {
		this.rbrq = rbrq;
	}

	public String getZyxs2() {
		return zyxs2;
	}

	public void setZyxs2(String zyxs2) {
		this.zyxs2 = zyxs2;
	}

	public String getZyxs3() {
		return zyxs3;
	}

	public void setZyxs3(String zyxs3) {
		this.zyxs3 = zyxs3;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
