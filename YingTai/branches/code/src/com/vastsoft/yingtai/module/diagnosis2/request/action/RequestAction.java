package com.vastsoft.yingtai.module.diagnosis2.request.action;

import java.util.List;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestClass;
import com.vastsoft.yingtai.module.diagnosis2.exception.RequestException;
import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.constants.OrgVisible;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.attributeUtils.AttributeUtils;

public class RequestAction extends BaseYingTaiAction {
	private String org_name;
	private SplitPageUtil spu;
	private RequestClass request_class;

	public String searchOrgOfCanRequestTo() {
		try {
			Passport passport = takePassport();
			if (request_class == null)
				throw new RequestException("必须指定有效的申请类别！");
			List<TOrganization> listOrg = null;
			if (request_class.equals(RequestClass.DIAGNOSIS)) {
				listOrg = OrgService.instance.queryMyFriendOrgList(passport, org_name, OrgStatus.VALID);
			} else if (request_class.equals(RequestClass.CONSULT)) {
				listOrg = OrgService.instance.searchOrgList(org_name, null, null, null, OrgStatus.VALID, null,
						OrgVisible.SHOW, null, spu);
			}
			String[] attrs = { "id", "org_name", "org_code", "description", "desc" ,"isVIP"};
			addElementToData("listOrg", AttributeUtils.SerializeList(listOrg, attrs));
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setOrg_name(String org_name) {
		this.org_name = filterParam(org_name);
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setRequest_class(int request_class) {
		this.request_class = RequestClass.parseCode(request_class);
	}
}
