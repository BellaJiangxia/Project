package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity;

import java.util.ArrayList;
import java.util.List;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.NumberTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.common.constants.Gender;
import com.vastsoft.yingtaidicom.search.entity.TPatient;
import com.vastsoft.yingtaidicom.search.entity.TSeries;
import com.vastsoft.yingtaidicom.search.exception.PatientDataException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.constants.PacsSystenBrand;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity.Series;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.WPacsVersion;

public class Study {
	public static final String UNKNOW_MODALITY = "UNKNOW_MODALITY";
	private String stuuid;
    private String stuid;
    private String studate;
    private String studesc;
    private String accessno;
    private String modality;
    private String pid;
    private String pname;
    private String gender;
    private String dob;
    private String age;
    private String srcaet;
    
    private List<Series> listSeries;
    
	public String getStuuid() {
		return stuuid;
	}
	public void setStuuid(String stuuid) {
		this.stuuid = stuuid;
	}
	public String getStuid() {
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getStudate() {
		return studate;
	}
	public void setStudate(String studate) {
		this.studate = studate;
	}
	public String getStudesc() {
		return studesc;
	}
	public void setStudesc(String studesc) {
		this.studesc = studesc;
	}
	public String getAccessno() {
		return accessno;
	}
	public void setAccessno(String accessno) {
		this.accessno = accessno;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessno == null) ? 0 : accessno.hashCode());
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((dob == null) ? 0 : dob.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((modality == null) ? 0 : modality.hashCode());
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		result = prime * result + ((pname == null) ? 0 : pname.hashCode());
		result = prime * result + ((studate == null) ? 0 : studate.hashCode());
		result = prime * result + ((studesc == null) ? 0 : studesc.hashCode());
		result = prime * result + ((stuid == null) ? 0 : stuid.hashCode());
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
		Study other = (Study) obj;
		if (accessno == null) {
			if (other.accessno != null)
				return false;
		} else if (!accessno.equals(other.accessno))
			return false;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (dob == null) {
			if (other.dob != null)
				return false;
		} else if (!dob.equals(other.dob))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (modality == null) {
			if (other.modality != null)
				return false;
		} else if (!modality.equals(other.modality))
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		if (pname == null) {
			if (other.pname != null)
				return false;
		} else if (!pname.equals(other.pname))
			return false;
		if (studate == null) {
			if (other.studate != null)
				return false;
		} else if (!studate.equals(other.studate))
			return false;
		if (studesc == null) {
			if (other.studesc != null)
				return false;
		} else if (!studesc.equals(other.studesc))
			return false;
		if (stuid == null) {
			if (other.stuid != null)
				return false;
		} else if (!stuid.equals(other.stuid))
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
		return "TStudy [stuuid=" + stuuid + ", stuid=" + stuid + ", studate=" + studate + ", studesc=" + studesc
				+ ", accessno=" + accessno + ", modality=" + modality + ", pid=" + pid + ", pname=" + pname
				+ ", gender=" + gender + ", dob=" + dob + ", age=" + age + "]";
	}
	public List<Series> getListSeries() {
		return listSeries;
	}
	public void setListSeries(List<Series> listSeries) {
		this.listSeries = listSeries;
	}
	public static Study listMerge(List<Study> listStudy) {
		if (CollectionTools.isEmpty(listStudy))
			throw new NullPointerException();
		if (listStudy.size() == 1)
			return listStudy.get(0);
		Study result = new Study();
		for (Study tStudy : listStudy)
			result.merge(tStudy);
		return result;
	}
	
	public void merge(Study anotherStudy) {
		if (anotherStudy == null)
			return;
		if(StringTools.isEmpty(this.stuuid))
			this.stuuid = anotherStudy.getStuuid();
		if(StringTools.isEmpty(this.stuid))
			this.stuid = anotherStudy.getStuid();
		if(StringTools.isEmpty(this.studate))
			this.studate = anotherStudy.getStudate();
		if(StringTools.isEmpty(this.accessno))
			this.accessno = anotherStudy.getAccessno();
		if(StringTools.isEmpty(this.modality))
			this.modality = anotherStudy.getModality();
		if(StringTools.isEmpty(this.pid))
			this.pid = anotherStudy.getPid();
		if(StringTools.isEmpty(this.pname))
			this.pname = anotherStudy.getPname();
		if(StringTools.isEmpty(this.gender))
			this.gender = anotherStudy.getGender();
		if(StringTools.isEmpty(this.dob))
			this.dob = anotherStudy.getDob();
		if(StringTools.isEmpty(this.age))
			this.age = anotherStudy.getAge();
	}
	public void addSeries(Series series) {
		if (series == null)
			return;
		if (this.listSeries == null)
			this.listSeries = new ArrayList<Series>();
		this.listSeries.add(series);
	}
	
	public TPatient takePatient(SystemIdentity systemIdentity) {
		TPatient result = new TPatient(systemIdentity);
		result.setBirthday(DateTools.getBirthdayByAge((int) NumberTools.takeInteger(this.getAge(), 0)));
		result.setBrand(PacsSystenBrand.WPACS.getName());
		result.setCreate_time(DateTools.strToDate(this.studate));
		result.setGender(Gender.parseString(this.gender, Gender.MALE).getCode());
		result.setName(this.pname);
		result.setSystemType(PacsSystenBrand.WPACS.getSystem_type());
		result.setVersion(systemIdentity.getSystemVersion().getName());
		return result;
	}
	public String getDevice_name() {
		if (!StringTools.isEmpty(this.modality))
			return this.modality;
		return UNKNOW_MODALITY;
	}
	public List<TSeries> organize(SystemIdentity systemIdentity) throws PatientDataException {
		if (CollectionTools.isEmpty(this.listSeries))
			throw new PatientDataException("此次检查没有序列！");
		List<TSeries> result = new ArrayList<>(this.listSeries.size());
		for (Series series : listSeries) {
			TSeries tSeries = new TSeries(systemIdentity);
			tSeries.setPart_name(StringTools.isEmpty(series.getSrsdesc())?Series.UNKNOW_BODYPART:series.getSrsdesc().trim());
			tSeries.setMark_char(series.getSrsuid());
			tSeries.setThumbnail_uid(series.getThrumbnail_uid());
			tSeries.setThumbnail_data(series.getThumbnail_data());
			tSeries.setExpose_times(series.getExpose_times());
			result.add(tSeries);
		}
		return result;
	}
	public String getSrcaet() {
		return srcaet;
	}
	public void setSrcaet(String srcaet) {
		this.srcaet = srcaet;
	}    
}
