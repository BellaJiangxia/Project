package com.vastsoft.yingtai.module.legitimate.action;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.legitimate.service.LegitimateService;

/**合法性验证
 * @author jben
 *
 */
public class LegitimateAction extends BaseYingTaiAction {
	private String[] study_uids;
	private String token;
	
	/**验证令牌合法性
	 * @return
	 */
	public String verifyLegitimateForViewImage(){
		try {
			LegitimateService.instance.verifyLegitimateForViewImage(study_uids,token);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	/**通过STUDYUID请求查看影像*/
	public String requestViewImage(){
		try {
			String token = LegitimateService.instance.requestViewImage(takePassport(), study_uids);
			super.addElementToData("token", token);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setStudy_uids(String study_uids) {
		this.study_uids = StringTools.splitString(study_uids, ',','，');
	}
	
	public void setStudy_uid(String study_uid) {
		this.study_uids = new String[]{study_uid};
	}

	public void setToken(String token) {
		this.token = token;
	}
}
