package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;

public final class ShareExternalSystemType extends SingleClassConstant {
	private PatientDataSourceType source_type;
	private ShareExternalSystemType parent_type;
	/** his系统 */
	public static final ShareExternalSystemType HIS = new ShareExternalSystemType(10, "HIS", null,
			PatientDataSourceType.REMOTE_HIS_SYS);
	/** RIS系统 */
	public static final ShareExternalSystemType RIS = new ShareExternalSystemType(20, "RIS", HIS,
			PatientDataSourceType.REMOTE_RIS_SYS);
	/** PACS系统 */
	public static final ShareExternalSystemType PACS = new ShareExternalSystemType(30, "PACS", RIS,
			PatientDataSourceType.REMOTE_PASC_SYS);
	/** EMR系统 */
	public static final ShareExternalSystemType EMR = new ShareExternalSystemType(50, "EMR", HIS,
			PatientDataSourceType.REMOTE_EMR_SYS);

	private static Map<Integer, ShareExternalSystemType> mapExternalSystemType = new HashMap<Integer, ShareExternalSystemType>();

	static {
		mapExternalSystemType.put(HIS.getCode(), HIS);
		mapExternalSystemType.put(RIS.getCode(), RIS);
		mapExternalSystemType.put(PACS.getCode(), PACS);
		mapExternalSystemType.put(EMR.getCode(), EMR);
	}

	protected ShareExternalSystemType(int iCode, String strName, ShareExternalSystemType parent_type,
			PatientDataSourceType source_type) {
		super(iCode, strName);
		this.source_type = source_type;
		this.parent_type = parent_type;
	}

	public static ShareExternalSystemType parseCode(int code) {
		return mapExternalSystemType.get(code);
	}

	public static int count() {
		return mapExternalSystemType.size();
	}

	public static List<ShareExternalSystemType> getAll() {
		return new ArrayList<ShareExternalSystemType>(mapExternalSystemType.values());
	}

	public static ShareExternalSystemType parseName(String name) {
		for (Iterator<ShareExternalSystemType> iterator = mapExternalSystemType.values().iterator(); iterator.hasNext();) {
			ShareExternalSystemType type = (ShareExternalSystemType) iterator.next();
			if (type.getName().equalsIgnoreCase(name))
				return type;
		}
		return null;
	}

	public PatientDataSourceType getSource_type() {
		return this.source_type;
	}

	@Override
	public String toString() {
		return "系统：" + super.getName();
	}

	public ShareExternalSystemType getParentType() {
		return this.parent_type;
	}
}
