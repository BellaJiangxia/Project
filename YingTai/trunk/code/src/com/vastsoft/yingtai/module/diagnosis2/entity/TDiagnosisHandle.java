package com.vastsoft.yingtai.module.diagnosis2.entity;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportFomType;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisHandleStatus2;

public class TDiagnosisHandle {
	private long id;
	private long diagnosis_id;
	private long curr_user_id;
	private long org_id;
	private long next_user_id;
	private String pic_opinion;//影像所见
	private String pic_conclusion;//诊断意见
	private int status;
	private int f_o_m=1;
	private Date create_time;
	private Date transfer_time; 
	private int can_tranfer;//只有1才可以
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCurr_user_id() {
		return curr_user_id;
	}

	public void setCurr_user_id(long curr_user_id) {
		this.curr_user_id = curr_user_id;
	}

	public long getNext_user_id() {
		return next_user_id;
	}

	public void setNext_user_id(long next_user_id) {
		this.next_user_id = next_user_id;
	}

	public String getPic_opinion() {
		return pic_opinion;
	}
	
	public void setPic_opinion(String pic_opinion) {
		this.pic_opinion = pic_opinion;
	}

	public int getStatus() {
		return status;
	}
	
	public String getStatusStr() {
		return DiagnosisHandleStatus2.parseFrom(status).getName();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreate_time() {
		return create_time;
	}
	public String getCreate_timeStr() {
		return DateTools.dateToString(create_time);
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getPic_conclusion() {
		return pic_conclusion;
	}
	public List<String> getPic_conclusions() {
		if (pic_conclusion==null)
			return null;
		String pco=pic_conclusion.replace("\r\n", "<p>");
		pco=pco.replace("\n", "<p>");
		return StringTools.strArrayTrim(pco.split("<p>"));
	}
	public List<String> getPic_opinions() {
		if (pic_opinion==null)
			return null;
		String pco=pic_opinion.replace("\r\n", "<p>");
		pco=pco.replace("\n", "<p>");
		return StringTools.strArrayTrim(pco.split("<p>"));
	}
	public void setPic_conclusion(String pic_conclusion) {
		this.pic_conclusion = pic_conclusion;
	}

	public long getDiagnosis_id() {
		return diagnosis_id;
	}

	public void setDiagnosis_id(long diagnosis_id) {
		this.diagnosis_id = diagnosis_id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public Date getTransfer_time() {
		return transfer_time;
	}
	
	public String getTransfer_timeStr() {
		return DateTools.dateToString(transfer_time);
	}

	public void setTransfer_time(Date transfer_time) {
		this.transfer_time = transfer_time;
	}
	
	public int getF_o_m() {
		return f_o_m==1||f_o_m==2?f_o_m:1;
	}
	
	public String getF_o_mStr() {
		ReportFomType fom=ReportFomType.parseCode(f_o_m);
		return fom==null?"无":fom.getName();
	}

	public void setF_o_m(int f_o_m) {
		this.f_o_m = f_o_m;
	}

	public int getCan_tranfer() {
		return can_tranfer;
	}

	public void setCan_tranfer(int can_tranfer) {
		this.can_tranfer = can_tranfer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + can_tranfer;
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + (int) (curr_user_id ^ (curr_user_id >>> 32));
		result = prime * result + (int) (diagnosis_id ^ (diagnosis_id >>> 32));
		result = prime * result + f_o_m;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (next_user_id ^ (next_user_id >>> 32));
		result = prime * result + (int) (org_id ^ (org_id >>> 32));
		result = prime * result + ((pic_conclusion == null) ? 0 : pic_conclusion.hashCode());
		result = prime * result + ((pic_opinion == null) ? 0 : pic_opinion.hashCode());
		result = prime * result + status;
		result = prime * result + ((transfer_time == null) ? 0 : transfer_time.hashCode());
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
		TDiagnosisHandle other = (TDiagnosisHandle) obj;
		if (can_tranfer != other.can_tranfer)
			return false;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (curr_user_id != other.curr_user_id)
			return false;
		if (diagnosis_id != other.diagnosis_id)
			return false;
		if (f_o_m != other.f_o_m)
			return false;
		if (id != other.id)
			return false;
		if (next_user_id != other.next_user_id)
			return false;
		if (org_id != other.org_id)
			return false;
		if (pic_conclusion == null) {
			if (other.pic_conclusion != null)
				return false;
		} else if (!pic_conclusion.equals(other.pic_conclusion))
			return false;
		if (pic_opinion == null) {
			if (other.pic_opinion != null)
				return false;
		} else if (!pic_opinion.equals(other.pic_opinion))
			return false;
		if (status != other.status)
			return false;
		if (transfer_time == null) {
			if (other.transfer_time != null)
				return false;
		} else if (!transfer_time.equals(other.transfer_time))
			return false;
		return true;
	}
}
