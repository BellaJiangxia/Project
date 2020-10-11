package com.vastsoft.yingtai.module.qualityControl.assist;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;

public interface QualityControlTargetIdTranfer {
	
	/**
	 * 从请求参数中获取质控目标ID
	 * 
	 * @param request
	 * @return
	 */
	public Map<QualityControlTarget, Long> tranfer(ActionInvocation invocation);
	//
	// public static class TargetEntry{
	// private long target_entity_id;
	// private boolean ignore;
	// public long getTarget_entity_id() {
	// return target_entity_id;
	// }
	// public void setTarget_entity_id(long target_entity_id) {
	// this.target_entity_id = target_entity_id;
	// }
	// public boolean isIgnore() {
	// return ignore;
	// }
	// public void setIgnore(boolean ignore) {
	// this.ignore = ignore;
	// }
	// }
}
