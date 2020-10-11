package com.vastsoft.yingtai.module.sys.action;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.sys.constants.LogOperatModule;
import com.vastsoft.yingtai.module.sys.constants.LogStatus;
import com.vastsoft.yingtai.module.sys.entity.FSysLog;
import com.vastsoft.yingtai.module.sys.service.LogService;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.utils.attributeUtils.AttributeUtils;

public class LogAction extends BaseYingTaiAction {
	
	private String strPeratorName;
	private String strOrgName;
	private SplitPageUtil spu;
	private Integer status;
	private Integer module;
	private Integer operType;

//	@ActionDesc("搜索系统操作日志")
	public String searchSysLogList(){
		try {
			List<FSysLog> listLog=LogService.instance.searchSysLogList(takePassport(), strPeratorName, strOrgName,
					operType==null?null:UserType.parseCode(operType),module==null?null:LogOperatModule.parseCode(module),
					status==null?null:LogStatus.parseCode(status), super.getStart(), super.getEnd(), spu);
			String[] attrs={"id","operator_name","org_name","operator_typeStr","moduleName","description","statusStr","error_msg","create_timeStr"};
			addElementToData("listLog",AttributeUtils.SerializeList(listLog, attrs));
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setPeratorName(String strPeratorName) {
		this.strPeratorName = filterParam(strPeratorName);
	}

	public void setOrgName(String strOrgName) {
		this.strOrgName = filterParam(strOrgName);
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setEnd(String dtEnd) {
		Date dd = DateTools.strToDate(dtEnd);
		super.setEnd(dd == null ? null : DateTools.getEndTimeByDay(dd));
	}

	public void setStatus(Integer status) {
		this.status = filterParam(status);
	}

	public void setModule(Integer module) {
		this.module = filterParam(module);
	}

	public void setOperType(Integer operType) {
		this.operType = filterParam(operType);
	}

	public void setStart(String dtStart) {
		Date dd = DateTools.strToDate(dtStart);
		super.setStart(dd == null ? null : DateTools.getStartTimeByDay(dd));
	}
	
}
