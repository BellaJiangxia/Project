package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity;

import java.util.Date;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.common.SystemTools;

/** 病案信息，用于完善查询 包含：病案号；身份证号 */
public class B_BASY {
	private long id;
	private String bah;// 病案号
	private String xm;// 姓名
	private String xb;// 性别
	private Date csny;// 出生年月
	private String nl;// 年龄
	private String hyzk;// 婚姻状况 1-未婚 2-已婚 3 - 未知
	private String zy;// 职业
	private String csd;// 出生地
	private String mzbm;// 民族编码
	private String mzbm_name;// 民族名称
	private String sfzh;// 身份证号
	private String gzdw;// 工作单位
	private String gzdwdh;// 工作单位电话
	private String gzdwyb;// 工作单位邮编
	private String hkdz;// 户口地址
	private String hkdh;// 户口电话
	private String hkyb;// 户口邮编
	private String lxrxm;// 联系人姓名
	private String lxrgx;// 联系人关系
	private String lxrdz;// 联系人地址
	private String lxrdh;// 联系人电话
	private String rykb;// 入院科别；
	private String rykb_name;// 入院科别,名称；
	private Date rysj;// 入院时间
	private char ryqk;// 入院情况，暂时未知其对应值含义
	private int zyts;// 住院天数
	private String cykb;// 出院科别
	private Date cysj;// 出院时间
	private String zkqk;// 账款情况
	private String mzzd;// 门诊诊断
	private String mzzd_icd;// 门诊诊断附加信息；
	private String mzblh;// 门诊病历号
	private String mzys;// 门诊医生
	private String mzys_name;// 门诊医生姓名
	private String ryzd;// 入院诊断
	private String ryzd_icd;// 入院诊断附加信息
	private Date ryqzsj;// 入院签字时间
	private String zyzd;// 住院诊断
	private String zyzd_icd;// 住院诊断附件信息
	private String zyzd_qk;// 住院诊断情况
	private String zyzd2;// 住院诊断2
	private String zyzd2_icd;// 住院诊断附件信息2
	private char zyzd2_qk;// 住院诊断情况2
	private String qtzd;// 其他诊断
	private String qtzd_icd;// 其他诊断
	private char qtzd_qk;// 其他诊断情况
	private String qtzd2;// 其他诊断
	private String qtzd2_icd;// 其他诊断
	private char qtzd2_qk;// 其他诊断情况
	private String qtzd3;// 其他诊断
	private String qtzd3_icd;// 其他诊断
	private char qtzd3_qk;// 其他诊断情况
	private String bfz;// 并发症
	private String bfz_icd;
	private char bfz_qk;
	private String bfz2;// 并发症
	private String bfz2_icd;
	private char bfz2_qk;
	private String yngr;// 医疗感染 ？？
	private String yngr_icd;
	private char yngr_qk;
	private String xszd;// XS诊断
	private String xszd_icd;
	private Date ssrq;// 手术日期
	private String ssmc;// 手术名称
	private String mzfs;// 门诊方式?
	private String qk;
	private String ssys;// 手术医生
	private String ccbm;// 场次编码
	private String zyh;// 住院号
	private String add_jg;// 附加信息：籍贯
	private String add_xzz;// 附加信息：新住址
	private String add_xzz_lxdh;// 附加信息：新住址联系电话
	private String add_xzz_yzbh;// 附加信息：新住址邮政编码
	private String ryksch;// 入院床号
	private String cyksch;// 出院床号
	private String add_zzys;// 主治医生
	private String add_zzys_name;// 主治医生姓名

	public String getDiagnosis() {
		StringBuilder sbb = new StringBuilder();
		if (!StringTools.isEmpty(mzzd))
			sbb.append("门诊诊断：").append(mzzd).append(SystemTools.getSeparator());
		if (!StringTools.isEmpty(ryzd))
			sbb.append("入院诊断：").append(ryzd).append(SystemTools.getSeparator());
		if (!StringTools.isEmpty(zyzd))
			sbb.append("住院诊断1：").append(zyzd).append(SystemTools.getSeparator());
		if (!StringTools.isEmpty(zyzd2))
			sbb.append("住院诊断2：").append(zyzd2).append(SystemTools.getSeparator());
		return sbb.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBah() {
		return bah;
	}

	public void setBah(String bah) {
		this.bah = bah;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public Date getCsny() {
		return csny;
	}

	public void setCsny(Date csny) {
		this.csny = csny;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getHyzk() {
		return hyzk;
	}

	public void setHyzk(String hyzk) {
		this.hyzk = hyzk;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public String getCsd() {
		return csd;
	}

	public void setCsd(String csd) {
		this.csd = csd;
	}

	public String getMzbm() {
		return mzbm;
	}

	public void setMzbm(String mzbm) {
		this.mzbm = mzbm;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getGzdw() {
		return gzdw;
	}

	public void setGzdw(String gzdw) {
		this.gzdw = gzdw;
	}

	public String getGzdwdh() {
		return gzdwdh;
	}

	public void setGzdwdh(String gzdwdh) {
		this.gzdwdh = gzdwdh;
	}

	public String getGzdwyb() {
		return gzdwyb;
	}

	public void setGzdwyb(String gzdwyb) {
		this.gzdwyb = gzdwyb;
	}

	public String getHkdz() {
		return hkdz;
	}

	public void setHkdz(String hkdz) {
		this.hkdz = hkdz;
	}

	public String getHkdh() {
		return hkdh;
	}

	public void setHkdh(String hkdh) {
		this.hkdh = hkdh;
	}

	public String getHkyb() {
		return hkyb;
	}

	public void setHkyb(String hkyb) {
		this.hkyb = hkyb;
	}

	public String getLxrxm() {
		return lxrxm;
	}

	public void setLxrxm(String lxrxm) {
		this.lxrxm = lxrxm;
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

	public String getRykb() {
		return rykb;
	}

	public void setRykb(String rykb) {
		this.rykb = rykb;
	}

	public Date getRysj() {
		return rysj;
	}

	public void setRysj(Date rysj) {
		this.rysj = rysj;
	}

	public char getRyqk() {
		return ryqk;
	}

	public void setRyqk(char ryqk) {
		this.ryqk = ryqk;
	}

	public int getZyts() {
		return zyts;
	}

	public void setZyts(int zyts) {
		this.zyts = zyts;
	}

	public String getCykb() {
		return cykb;
	}

	public void setCykb(String cykb) {
		this.cykb = cykb;
	}

	public Date getCysj() {
		return cysj;
	}

	public void setCysj(Date cysj) {
		this.cysj = cysj;
	}

	public String getZkqk() {
		return zkqk;
	}

	public void setZkqk(String zkqk) {
		this.zkqk = zkqk;
	}

	public String getMzzd() {
		return mzzd;
	}

	public void setMzzd(String mzzd) {
		this.mzzd = mzzd;
	}

	public String getMzzd_icd() {
		return mzzd_icd;
	}

	public void setMzzd_icd(String mzzd_icd) {
		this.mzzd_icd = mzzd_icd;
	}

	public String getMzblh() {
		return mzblh;
	}

	public void setMzblh(String mzblh) {
		this.mzblh = mzblh;
	}

	public String getMzys() {
		return mzys;
	}

	public void setMzys(String mzys) {
		this.mzys = mzys;
	}

	public String getRyzd() {
		return ryzd;
	}

	public void setRyzd(String ryzd) {
		this.ryzd = ryzd;
	}

	public String getRyzd_icd() {
		return ryzd_icd;
	}

	public void setRyzd_icd(String ryzd_icd) {
		this.ryzd_icd = ryzd_icd;
	}

	public Date getRyqzsj() {
		return ryqzsj;
	}

	public void setRyqzsj(Date ryqzsj) {
		this.ryqzsj = ryqzsj;
	}

	public String getZyzd() {
		return zyzd;
	}

	public void setZyzd(String zyzd) {
		this.zyzd = zyzd;
	}

	public String getZyzd_icd() {
		return zyzd_icd;
	}

	public void setZyzd_icd(String zyzd_icd) {
		this.zyzd_icd = zyzd_icd;
	}

	public String getZyzd_qk() {
		return zyzd_qk;
	}

	public void setZyzd_qk(String zyzd_qk) {
		this.zyzd_qk = zyzd_qk;
	}

	public String getZyzd2() {
		return zyzd2;
	}

	public void setZyzd2(String zyzd2) {
		this.zyzd2 = zyzd2;
	}

	public String getZyzd2_icd() {
		return zyzd2_icd;
	}

	public void setZyzd2_icd(String zyzd2_icd) {
		this.zyzd2_icd = zyzd2_icd;
	}

	public char getZyzd2_qk() {
		return zyzd2_qk;
	}

	public void setZyzd2_qk(char zyzd2_qk) {
		this.zyzd2_qk = zyzd2_qk;
	}

	public String getQtzd() {
		return qtzd;
	}

	public void setQtzd(String qtzd) {
		this.qtzd = qtzd;
	}

	public String getQtzd_icd() {
		return qtzd_icd;
	}

	public void setQtzd_icd(String qtzd_icd) {
		this.qtzd_icd = qtzd_icd;
	}

	public char getQtzd_qk() {
		return qtzd_qk;
	}

	public void setQtzd_qk(char qtzd_qk) {
		this.qtzd_qk = qtzd_qk;
	}

	public String getQtzd2() {
		return qtzd2;
	}

	public void setQtzd2(String qtzd2) {
		this.qtzd2 = qtzd2;
	}

	public String getQtzd2_icd() {
		return qtzd2_icd;
	}

	public void setQtzd2_icd(String qtzd2_icd) {
		this.qtzd2_icd = qtzd2_icd;
	}

	public char getQtzd2_qk() {
		return qtzd2_qk;
	}

	public void setQtzd2_qk(char qtzd2_qk) {
		this.qtzd2_qk = qtzd2_qk;
	}

	public String getQtzd3() {
		return qtzd3;
	}

	public void setQtzd3(String qtzd3) {
		this.qtzd3 = qtzd3;
	}

	public String getQtzd3_icd() {
		return qtzd3_icd;
	}

	public void setQtzd3_icd(String qtzd3_icd) {
		this.qtzd3_icd = qtzd3_icd;
	}

	public char getQtzd3_qk() {
		return qtzd3_qk;
	}

	public void setQtzd3_qk(char qtzd3_qk) {
		this.qtzd3_qk = qtzd3_qk;
	}

	public String getBfz() {
		return bfz;
	}

	public void setBfz(String bfz) {
		this.bfz = bfz;
	}

	public String getBfz_icd() {
		return bfz_icd;
	}

	public void setBfz_icd(String bfz_icd) {
		this.bfz_icd = bfz_icd;
	}

	public char getBfz_qk() {
		return bfz_qk;
	}

	public void setBfz_qk(char bfz_qk) {
		this.bfz_qk = bfz_qk;
	}

	public String getBfz2() {
		return bfz2;
	}

	public void setBfz2(String bfz2) {
		this.bfz2 = bfz2;
	}

	public String getBfz2_icd() {
		return bfz2_icd;
	}

	public void setBfz2_icd(String bfz2_icd) {
		this.bfz2_icd = bfz2_icd;
	}

	public char getBfz2_qk() {
		return bfz2_qk;
	}

	public void setBfz2_qk(char bfz2_qk) {
		this.bfz2_qk = bfz2_qk;
	}

	public String getYngr() {
		return yngr;
	}

	public void setYngr(String yngr) {
		this.yngr = yngr;
	}

	public String getYngr_icd() {
		return yngr_icd;
	}

	public void setYngr_icd(String yngr_icd) {
		this.yngr_icd = yngr_icd;
	}

	public char getYngr_qk() {
		return yngr_qk;
	}

	public void setYngr_qk(char yngr_qk) {
		this.yngr_qk = yngr_qk;
	}

	public String getXszd() {
		return xszd;
	}

	public void setXszd(String xszd) {
		this.xszd = xszd;
	}

	public String getXszd_icd() {
		return xszd_icd;
	}

	public void setXszd_icd(String xszd_icd) {
		this.xszd_icd = xszd_icd;
	}

	public Date getSsrq() {
		return ssrq;
	}

	public void setSsrq(Date ssrq) {
		this.ssrq = ssrq;
	}

	public String getSsmc() {
		return ssmc;
	}

	public void setSsmc(String ssmc) {
		this.ssmc = ssmc;
	}

	public String getMzfs() {
		return mzfs;
	}

	public void setMzfs(String mzfs) {
		this.mzfs = mzfs;
	}

	public String getQk() {
		return qk;
	}

	public void setQk(String qk) {
		this.qk = qk;
	}

	public String getSsys() {
		return ssys;
	}

	public void setSsys(String ssys) {
		this.ssys = ssys;
	}

	public String getCcbm() {
		return ccbm;
	}

	public void setCcbm(String ccbm) {
		this.ccbm = ccbm;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getAdd_jg() {
		return add_jg;
	}

	public void setAdd_jg(String add_jg) {
		this.add_jg = add_jg;
	}

	public String getAdd_xzz() {
		return add_xzz;
	}

	public void setAdd_xzz(String add_xzz) {
		this.add_xzz = add_xzz;
	}

	public String getAdd_xzz_lxdh() {
		return add_xzz_lxdh;
	}

	public void setAdd_xzz_lxdh(String add_xzz_lxdh) {
		this.add_xzz_lxdh = add_xzz_lxdh;
	}

	public String getAdd_xzz_yzbh() {
		return add_xzz_yzbh;
	}

	public void setAdd_xzz_yzbh(String add_xzz_yzbh) {
		this.add_xzz_yzbh = add_xzz_yzbh;
	}

	public String getRyksch() {
		return ryksch;
	}

	public void setRyksch(String ryksch) {
		this.ryksch = ryksch;
	}

	public String getCyksch() {
		return cyksch;
	}

	public void setCyksch(String cyksch) {
		this.cyksch = cyksch;
	}

	public String getAdd_zzys() {
		return add_zzys;
	}

	public void setAdd_zzys(String add_zzys) {
		this.add_zzys = add_zzys;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((add_jg == null) ? 0 : add_jg.hashCode());
		result = prime * result + ((add_xzz == null) ? 0 : add_xzz.hashCode());
		result = prime * result + ((add_xzz_lxdh == null) ? 0 : add_xzz_lxdh.hashCode());
		result = prime * result + ((add_xzz_yzbh == null) ? 0 : add_xzz_yzbh.hashCode());
		result = prime * result + ((add_zzys == null) ? 0 : add_zzys.hashCode());
		result = prime * result + ((bah == null) ? 0 : bah.hashCode());
		result = prime * result + ((bfz == null) ? 0 : bfz.hashCode());
		result = prime * result + ((bfz2 == null) ? 0 : bfz2.hashCode());
		result = prime * result + ((bfz2_icd == null) ? 0 : bfz2_icd.hashCode());
		result = prime * result + bfz2_qk;
		result = prime * result + ((bfz_icd == null) ? 0 : bfz_icd.hashCode());
		result = prime * result + bfz_qk;
		result = prime * result + ((ccbm == null) ? 0 : ccbm.hashCode());
		result = prime * result + ((csd == null) ? 0 : csd.hashCode());
		result = prime * result + ((csny == null) ? 0 : csny.hashCode());
		result = prime * result + ((cykb == null) ? 0 : cykb.hashCode());
		result = prime * result + ((cyksch == null) ? 0 : cyksch.hashCode());
		result = prime * result + ((cysj == null) ? 0 : cysj.hashCode());
		result = prime * result + ((gzdw == null) ? 0 : gzdw.hashCode());
		result = prime * result + ((gzdwdh == null) ? 0 : gzdwdh.hashCode());
		result = prime * result + ((gzdwyb == null) ? 0 : gzdwyb.hashCode());
		result = prime * result + ((hkdh == null) ? 0 : hkdh.hashCode());
		result = prime * result + ((hkdz == null) ? 0 : hkdz.hashCode());
		result = prime * result + ((hkyb == null) ? 0 : hkyb.hashCode());
		result = prime * result + ((hyzk == null) ? 0 : hyzk.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lxrdh == null) ? 0 : lxrdh.hashCode());
		result = prime * result + ((lxrdz == null) ? 0 : lxrdz.hashCode());
		result = prime * result + ((lxrgx == null) ? 0 : lxrgx.hashCode());
		result = prime * result + ((lxrxm == null) ? 0 : lxrxm.hashCode());
		result = prime * result + ((mzblh == null) ? 0 : mzblh.hashCode());
		result = prime * result + ((mzbm == null) ? 0 : mzbm.hashCode());
		result = prime * result + ((mzfs == null) ? 0 : mzfs.hashCode());
		result = prime * result + ((mzys == null) ? 0 : mzys.hashCode());
		result = prime * result + ((mzzd == null) ? 0 : mzzd.hashCode());
		result = prime * result + ((mzzd_icd == null) ? 0 : mzzd_icd.hashCode());
		result = prime * result + ((nl == null) ? 0 : nl.hashCode());
		result = prime * result + ((qk == null) ? 0 : qk.hashCode());
		result = prime * result + ((qtzd == null) ? 0 : qtzd.hashCode());
		result = prime * result + ((qtzd2 == null) ? 0 : qtzd2.hashCode());
		result = prime * result + ((qtzd2_icd == null) ? 0 : qtzd2_icd.hashCode());
		result = prime * result + qtzd2_qk;
		result = prime * result + ((qtzd3 == null) ? 0 : qtzd3.hashCode());
		result = prime * result + ((qtzd3_icd == null) ? 0 : qtzd3_icd.hashCode());
		result = prime * result + qtzd3_qk;
		result = prime * result + ((qtzd_icd == null) ? 0 : qtzd_icd.hashCode());
		result = prime * result + qtzd_qk;
		result = prime * result + ((rykb == null) ? 0 : rykb.hashCode());
		result = prime * result + ((ryksch == null) ? 0 : ryksch.hashCode());
		result = prime * result + ryqk;
		result = prime * result + ((ryqzsj == null) ? 0 : ryqzsj.hashCode());
		result = prime * result + ((rysj == null) ? 0 : rysj.hashCode());
		result = prime * result + ((ryzd == null) ? 0 : ryzd.hashCode());
		result = prime * result + ((ryzd_icd == null) ? 0 : ryzd_icd.hashCode());
		result = prime * result + ((sfzh == null) ? 0 : sfzh.hashCode());
		result = prime * result + ((ssmc == null) ? 0 : ssmc.hashCode());
		result = prime * result + ((ssrq == null) ? 0 : ssrq.hashCode());
		result = prime * result + ((ssys == null) ? 0 : ssys.hashCode());
		result = prime * result + ((xb == null) ? 0 : xb.hashCode());
		result = prime * result + ((xm == null) ? 0 : xm.hashCode());
		result = prime * result + ((xszd == null) ? 0 : xszd.hashCode());
		result = prime * result + ((xszd_icd == null) ? 0 : xszd_icd.hashCode());
		result = prime * result + ((yngr == null) ? 0 : yngr.hashCode());
		result = prime * result + ((yngr_icd == null) ? 0 : yngr_icd.hashCode());
		result = prime * result + yngr_qk;
		result = prime * result + ((zkqk == null) ? 0 : zkqk.hashCode());
		result = prime * result + ((zy == null) ? 0 : zy.hashCode());
		result = prime * result + ((zyh == null) ? 0 : zyh.hashCode());
		result = prime * result + zyts;
		result = prime * result + ((zyzd == null) ? 0 : zyzd.hashCode());
		result = prime * result + ((zyzd2 == null) ? 0 : zyzd2.hashCode());
		result = prime * result + ((zyzd2_icd == null) ? 0 : zyzd2_icd.hashCode());
		result = prime * result + zyzd2_qk;
		result = prime * result + ((zyzd_icd == null) ? 0 : zyzd_icd.hashCode());
		result = prime * result + ((zyzd_qk == null) ? 0 : zyzd_qk.hashCode());
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
		B_BASY other = (B_BASY) obj;
		if (add_jg == null) {
			if (other.add_jg != null)
				return false;
		} else if (!add_jg.equals(other.add_jg))
			return false;
		if (add_xzz == null) {
			if (other.add_xzz != null)
				return false;
		} else if (!add_xzz.equals(other.add_xzz))
			return false;
		if (add_xzz_lxdh == null) {
			if (other.add_xzz_lxdh != null)
				return false;
		} else if (!add_xzz_lxdh.equals(other.add_xzz_lxdh))
			return false;
		if (add_xzz_yzbh == null) {
			if (other.add_xzz_yzbh != null)
				return false;
		} else if (!add_xzz_yzbh.equals(other.add_xzz_yzbh))
			return false;
		if (add_zzys == null) {
			if (other.add_zzys != null)
				return false;
		} else if (!add_zzys.equals(other.add_zzys))
			return false;
		if (bah == null) {
			if (other.bah != null)
				return false;
		} else if (!bah.equals(other.bah))
			return false;
		if (bfz == null) {
			if (other.bfz != null)
				return false;
		} else if (!bfz.equals(other.bfz))
			return false;
		if (bfz2 == null) {
			if (other.bfz2 != null)
				return false;
		} else if (!bfz2.equals(other.bfz2))
			return false;
		if (bfz2_icd == null) {
			if (other.bfz2_icd != null)
				return false;
		} else if (!bfz2_icd.equals(other.bfz2_icd))
			return false;
		if (bfz2_qk != other.bfz2_qk)
			return false;
		if (bfz_icd == null) {
			if (other.bfz_icd != null)
				return false;
		} else if (!bfz_icd.equals(other.bfz_icd))
			return false;
		if (bfz_qk != other.bfz_qk)
			return false;
		if (ccbm == null) {
			if (other.ccbm != null)
				return false;
		} else if (!ccbm.equals(other.ccbm))
			return false;
		if (csd == null) {
			if (other.csd != null)
				return false;
		} else if (!csd.equals(other.csd))
			return false;
		if (csny == null) {
			if (other.csny != null)
				return false;
		} else if (!csny.equals(other.csny))
			return false;
		if (cykb == null) {
			if (other.cykb != null)
				return false;
		} else if (!cykb.equals(other.cykb))
			return false;
		if (cyksch == null) {
			if (other.cyksch != null)
				return false;
		} else if (!cyksch.equals(other.cyksch))
			return false;
		if (cysj == null) {
			if (other.cysj != null)
				return false;
		} else if (!cysj.equals(other.cysj))
			return false;
		if (gzdw == null) {
			if (other.gzdw != null)
				return false;
		} else if (!gzdw.equals(other.gzdw))
			return false;
		if (gzdwdh == null) {
			if (other.gzdwdh != null)
				return false;
		} else if (!gzdwdh.equals(other.gzdwdh))
			return false;
		if (gzdwyb == null) {
			if (other.gzdwyb != null)
				return false;
		} else if (!gzdwyb.equals(other.gzdwyb))
			return false;
		if (hkdh == null) {
			if (other.hkdh != null)
				return false;
		} else if (!hkdh.equals(other.hkdh))
			return false;
		if (hkdz == null) {
			if (other.hkdz != null)
				return false;
		} else if (!hkdz.equals(other.hkdz))
			return false;
		if (hkyb == null) {
			if (other.hkyb != null)
				return false;
		} else if (!hkyb.equals(other.hkyb))
			return false;
		if (hyzk != other.hyzk)
			return false;
		if (id != other.id)
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
		if (lxrxm == null) {
			if (other.lxrxm != null)
				return false;
		} else if (!lxrxm.equals(other.lxrxm))
			return false;
		if (mzblh == null) {
			if (other.mzblh != null)
				return false;
		} else if (!mzblh.equals(other.mzblh))
			return false;
		if (mzbm == null) {
			if (other.mzbm != null)
				return false;
		} else if (!mzbm.equals(other.mzbm))
			return false;
		if (mzfs == null) {
			if (other.mzfs != null)
				return false;
		} else if (!mzfs.equals(other.mzfs))
			return false;
		if (mzys == null) {
			if (other.mzys != null)
				return false;
		} else if (!mzys.equals(other.mzys))
			return false;
		if (mzzd == null) {
			if (other.mzzd != null)
				return false;
		} else if (!mzzd.equals(other.mzzd))
			return false;
		if (mzzd_icd == null) {
			if (other.mzzd_icd != null)
				return false;
		} else if (!mzzd_icd.equals(other.mzzd_icd))
			return false;
		if (nl == null) {
			if (other.nl != null)
				return false;
		} else if (!nl.equals(other.nl))
			return false;
		if (qk == null) {
			if (other.qk != null)
				return false;
		} else if (!qk.equals(other.qk))
			return false;
		if (qtzd == null) {
			if (other.qtzd != null)
				return false;
		} else if (!qtzd.equals(other.qtzd))
			return false;
		if (qtzd2 == null) {
			if (other.qtzd2 != null)
				return false;
		} else if (!qtzd2.equals(other.qtzd2))
			return false;
		if (qtzd2_icd == null) {
			if (other.qtzd2_icd != null)
				return false;
		} else if (!qtzd2_icd.equals(other.qtzd2_icd))
			return false;
		if (qtzd2_qk != other.qtzd2_qk)
			return false;
		if (qtzd3 == null) {
			if (other.qtzd3 != null)
				return false;
		} else if (!qtzd3.equals(other.qtzd3))
			return false;
		if (qtzd3_icd == null) {
			if (other.qtzd3_icd != null)
				return false;
		} else if (!qtzd3_icd.equals(other.qtzd3_icd))
			return false;
		if (qtzd3_qk != other.qtzd3_qk)
			return false;
		if (qtzd_icd == null) {
			if (other.qtzd_icd != null)
				return false;
		} else if (!qtzd_icd.equals(other.qtzd_icd))
			return false;
		if (qtzd_qk != other.qtzd_qk)
			return false;
		if (rykb == null) {
			if (other.rykb != null)
				return false;
		} else if (!rykb.equals(other.rykb))
			return false;
		if (ryksch == null) {
			if (other.ryksch != null)
				return false;
		} else if (!ryksch.equals(other.ryksch))
			return false;
		if (ryqk != other.ryqk)
			return false;
		if (ryqzsj == null) {
			if (other.ryqzsj != null)
				return false;
		} else if (!ryqzsj.equals(other.ryqzsj))
			return false;
		if (rysj == null) {
			if (other.rysj != null)
				return false;
		} else if (!rysj.equals(other.rysj))
			return false;
		if (ryzd == null) {
			if (other.ryzd != null)
				return false;
		} else if (!ryzd.equals(other.ryzd))
			return false;
		if (ryzd_icd == null) {
			if (other.ryzd_icd != null)
				return false;
		} else if (!ryzd_icd.equals(other.ryzd_icd))
			return false;
		if (sfzh == null) {
			if (other.sfzh != null)
				return false;
		} else if (!sfzh.equals(other.sfzh))
			return false;
		if (ssmc == null) {
			if (other.ssmc != null)
				return false;
		} else if (!ssmc.equals(other.ssmc))
			return false;
		if (ssrq == null) {
			if (other.ssrq != null)
				return false;
		} else if (!ssrq.equals(other.ssrq))
			return false;
		if (ssys == null) {
			if (other.ssys != null)
				return false;
		} else if (!ssys.equals(other.ssys))
			return false;
		if (xb != other.xb)
			return false;
		if (xm == null) {
			if (other.xm != null)
				return false;
		} else if (!xm.equals(other.xm))
			return false;
		if (xszd == null) {
			if (other.xszd != null)
				return false;
		} else if (!xszd.equals(other.xszd))
			return false;
		if (xszd_icd == null) {
			if (other.xszd_icd != null)
				return false;
		} else if (!xszd_icd.equals(other.xszd_icd))
			return false;
		if (yngr == null) {
			if (other.yngr != null)
				return false;
		} else if (!yngr.equals(other.yngr))
			return false;
		if (yngr_icd == null) {
			if (other.yngr_icd != null)
				return false;
		} else if (!yngr_icd.equals(other.yngr_icd))
			return false;
		if (yngr_qk != other.yngr_qk)
			return false;
		if (zkqk == null) {
			if (other.zkqk != null)
				return false;
		} else if (!zkqk.equals(other.zkqk))
			return false;
		if (zy == null) {
			if (other.zy != null)
				return false;
		} else if (!zy.equals(other.zy))
			return false;
		if (zyh == null) {
			if (other.zyh != null)
				return false;
		} else if (!zyh.equals(other.zyh))
			return false;
		if (zyts != other.zyts)
			return false;
		if (zyzd == null) {
			if (other.zyzd != null)
				return false;
		} else if (!zyzd.equals(other.zyzd))
			return false;
		if (zyzd2 == null) {
			if (other.zyzd2 != null)
				return false;
		} else if (!zyzd2.equals(other.zyzd2))
			return false;
		if (zyzd2_icd == null) {
			if (other.zyzd2_icd != null)
				return false;
		} else if (!zyzd2_icd.equals(other.zyzd2_icd))
			return false;
		if (zyzd2_qk != other.zyzd2_qk)
			return false;
		if (zyzd_icd == null) {
			if (other.zyzd_icd != null)
				return false;
		} else if (!zyzd_icd.equals(other.zyzd_icd))
			return false;
		if (zyzd_qk == null) {
			if (other.zyzd_qk != null)
				return false;
		} else if (!zyzd_qk.equals(other.zyzd_qk))
			return false;
		return true;
	}

	public String getAdd_zzys_name() {
		return add_zzys_name;
	}

	public void setAdd_zzys_name(String add_zzys_name) {
		this.add_zzys_name = add_zzys_name;
	}

	public String getMzys_name() {
		return mzys_name;
	}

	public void setMzys_name(String mzys_name) {
		this.mzys_name = mzys_name;
	}

	public String getMzbm_name() {
		return mzbm_name;
	}

	public void setMzbm_name(String mzbm_name) {
		this.mzbm_name = mzbm_name;
	}

	public String getRykb_name() {
		return rykb_name;
	}

	public void setRykb_name(String rykb_name) {
		this.rykb_name = rykb_name;
	}

	public String getSymptom() {
		StringBuilder sbb = new StringBuilder();
		if (!StringTools.isEmpty(qtzd))
			sbb.append("1、").append(qtzd).append(" ").append(qtzd2).append(" ").append(qtzd3)
					.append(SystemTools.getSeparator());
		if (!StringTools.isEmpty(xszd))
			sbb.append("2、").append(xszd).append(SystemTools.getSeparator());
		return sbb.toString();
	}
}
