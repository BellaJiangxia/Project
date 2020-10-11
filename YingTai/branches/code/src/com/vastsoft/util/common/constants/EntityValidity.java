package com.vastsoft.util.common.constants;

import com.vastsoft.util.common.SingleClassConstant;

/**实体有效性
 * @author jben
 *
 */
public final class EntityValidity extends SingleClassConstant {
	/**有效*/
	public static final EntityValidity VALID = new EntityValidity(1, "有效");
	/**已删除*/
	public static final EntityValidity DELETED = new EntityValidity(99, "已删除");
	
	protected EntityValidity(int code, String name) {
		super(code, name);
	}
	
	public static EntityValidity parseCode(int code){
		return parseCode(code, null);
	}
	
	public static EntityValidity parseCode(int code,EntityValidity default_val){
		if (code == 1)
			return VALID;
		if (code == 99)
			return DELETED;
		return default_val;
	}
}
