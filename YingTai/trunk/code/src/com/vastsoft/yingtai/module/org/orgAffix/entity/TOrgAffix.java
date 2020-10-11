package com.vastsoft.yingtai.module.org.orgAffix.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.OrgRemoteConfig;
import com.vastsoft.yingtai.utils.annotation.IgnoreNull;
import com.vastsoft.yingtai.utils.annotation.IgnoreZero;

/**
 * 
 * @author jyb
 *
 */
public class TOrgAffix implements OrgRemoteConfig {
	private long id;
	private int remote_server_version;// 远程服务器版本 默认为1
	private String query_url;// 检索服务器URL
	private String dicomweb_url;// 图像查看服务器URL
	private String internet_ip;// 公网上网IP地址
	private String intranet_url;// 内网服务器IP地址
	private String ae_code;// 机构IE号，在版本2中不存在
	private Date create_time;// 记录创建时间
	private int status;// 记录状态
	private long org_id;// 机构ID
	private String user_name;// 打开图像登陆用户名
	private String password;// 打开图像登陆密码
	private int view_type; //影响查看服务类型，10:EPS(默认);20:WPACS;

//	private boolean wasLan = false;

	@IgnoreNull
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setStatus(int status) {
		this.status = status;
	}

	public static List<String> getQueryUrlList(List<TOrgAffix> listOrgAffix) {
		if (listOrgAffix == null || listOrgAffix.size() <= 0)
			return null;
		HashSet<String> listUrl = new HashSet<String>();
		for (TOrgAffix tOrgAffix : listOrgAffix) {
			listUrl.add(StringTools.endWith(tOrgAffix.getQuery_url(), '/'));
		}
		return new ArrayList<String>(listUrl);
	}

	public static List<String> getAeList(List<TOrgAffix> listOrgAffix) {
		if (listOrgAffix == null || listOrgAffix.size() <= 0)
			return null;
		HashSet<String> listUrl = new HashSet<String>();
		for (TOrgAffix tOrgAffix : listOrgAffix) {
			listUrl.addAll(StringTools.splitStrAsList(tOrgAffix.getAe_code(), ','));
		}
		return new ArrayList<String>(listUrl);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ae_code == null) ? 0 : ae_code.hashCode());
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + ((dicomweb_url == null) ? 0 : dicomweb_url.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((internet_ip == null) ? 0 : internet_ip.hashCode());
		result = prime * result + ((intranet_url == null) ? 0 : intranet_url.hashCode());
		result = prime * result + (int) (org_id ^ (org_id >>> 32));
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((query_url == null) ? 0 : query_url.hashCode());
		result = prime * result + status;
		result = prime * result + ((user_name == null) ? 0 : user_name.hashCode());
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
		TOrgAffix other = (TOrgAffix) obj;
		if (ae_code == null) {
			if (other.ae_code != null)
				return false;
		} else if (!ae_code.equals(other.ae_code))
			return false;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (dicomweb_url == null) {
			if (other.dicomweb_url != null)
				return false;
		} else if (!dicomweb_url.equals(other.dicomweb_url))
			return false;
		if (id != other.id)
			return false;
		if (internet_ip == null) {
			if (other.internet_ip != null)
				return false;
		} else if (!internet_ip.equals(other.internet_ip))
			return false;
		if (intranet_url == null) {
			if (other.intranet_url != null)
				return false;
		} else if (!intranet_url.equals(other.intranet_url))
			return false;
		if (org_id != other.org_id)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (query_url == null) {
			if (other.query_url != null)
				return false;
		} else if (!query_url.equals(other.query_url))
			return false;
		if (status != other.status)
			return false;
		if (user_name == null) {
			if (other.user_name != null)
				return false;
		} else if (!user_name.equals(other.user_name))
			return false;
		return true;
	}

	@IgnoreZero
	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public String getAe_code() {
		return ae_code;
	}

	public void setAe_code(String ae_code) {
		this.ae_code = ae_code;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQuery_url() {
		return StringTools.endWith(query_url, '/');
	}

	public String getDicomweb_url() {
		return StringTools.endWith(dicomweb_url, '/');
	}

	public void setQuery_url(String query_url) {
		this.query_url = query_url;
	}

	public void setDicomweb_url(String dicomweb_url) {
		this.dicomweb_url = dicomweb_url;
	}

	public String getInternet_ip() {
		return internet_ip;
	}

	public void setInternet_ip(String internet_ip) {
		this.internet_ip = internet_ip;
	}

	public String getIntranet_url() {
		return intranet_url;
	}

	public void setIntranet_url(String intranet_url) {
		this.intranet_url = intranet_url;
	}

	@Override
	@JSON(serialize=false)
	public ShareRemoteServerVersion getRemoteServerVersion() {
		return ShareRemoteServerVersion.parseCode(this.remote_server_version);
	}

	@Override
	public boolean valid() {
		ShareRemoteServerVersion rsv= this.getRemoteServerVersion();
		if (rsv==null||rsv.equals(ShareRemoteServerVersion.V_1)) {
			if (this.ae_code==null||this.ae_code.trim().isEmpty())
				return false;
			if (this.query_url==null||this.query_url.trim().isEmpty())
				return false;
			return true;
		}else if (rsv.equals(ShareRemoteServerVersion.V_2)) {
			if (this.query_url==null||this.query_url.trim().isEmpty())
				return false;
			return true;
		}else {
			return false;
		}
	}
	@Override
	public String toString() {
		return "TOrgAffix [id=" + id + ", remote_server_version=" + remote_server_version + ", query_url=" + query_url
				+ ", dicomweb_url=" + dicomweb_url + ", internet_ip=" + internet_ip + ", intranet_url=" + intranet_url
				+ ", ae_code=" + ae_code + ", create_time=" + create_time + ", status=" + status + ", org_id=" + org_id
				+ ", user_name=" + user_name + ", password=" + password + "]";
	}

	@SuppressWarnings("deprecation")
	@Override
	@JSON(serialize=false)
	public Map<ShareRemoteParamsType, String> getAdditionalRemoteParams() {
		Map<ShareRemoteParamsType, String> params = new HashMap<ShareRemoteParamsType, String>();
		if (ShareRemoteServerVersion.V_1.equals(this.getRemoteServerVersion())) {
			params.put(ShareRemoteParamsType.ORG_AE_NUM, this.ae_code);
		}
		return params;
	}

	public int getRemote_server_version() {
		return remote_server_version;
	}
	
	public String getRemote_server_versionStr() {
		ShareRemoteServerVersion rsv=ShareRemoteServerVersion.parseCode(remote_server_version);
		return rsv==null?"":rsv.getName();
	}

	public void setRemote_server_version(int remote_server_version) {
		ShareRemoteServerVersion rsv = ShareRemoteServerVersion.parseCode(remote_server_version);
		this.remote_server_version = (rsv==null?ShareRemoteServerVersion.V_1.getCode():rsv.getCode());
	}

	public int getView_type() {
		return view_type;
	}

	public void setView_type(int view_type) {
		this.view_type = view_type;
	}
}
