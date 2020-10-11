package com.vastsoft.yingtaidicom.search.orgsearch.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.constants.PatientDataSourceType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.emr.EmrSysAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.HisSysAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.PacsSysAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.ris.RisSysAdapter;

public final class ExternalSystemType extends AbstractSystemAdapterContant {
	private PatientDataSourceType source_type;
	private ExternalSystemType parent_type;
	/** his系统 */
	public static final ExternalSystemType HIS = new ExternalSystemType(10, "HIS", null,
			PatientDataSourceType.REMOTE_HIS_SYS, HisSysAdapter.class);
	/** RIS系统 */
	public static final ExternalSystemType RIS = new ExternalSystemType(20, "RIS", HIS,
			PatientDataSourceType.REMOTE_RIS_SYS, RisSysAdapter.class);
	/** PACS系统 */
	public static final ExternalSystemType PACS = new ExternalSystemType(30, "PACS", RIS,
			PatientDataSourceType.REMOTE_PASC_SYS, PacsSysAdapter.class);
	/** EMR系统 */
	public static final ExternalSystemType EMR = new ExternalSystemType(50, "EMR", HIS,
			PatientDataSourceType.REMOTE_EMR_SYS, EmrSysAdapter.class);

	private static Map<Integer, ExternalSystemType> mapExternalSystemType = new HashMap<Integer, ExternalSystemType>();

	static {
		mapExternalSystemType.put(HIS.getCode(), HIS);
		mapExternalSystemType.put(RIS.getCode(), RIS);
		mapExternalSystemType.put(PACS.getCode(), PACS);
		mapExternalSystemType.put(EMR.getCode(), EMR);
	}

	protected ExternalSystemType(int iCode, String strName, ExternalSystemType parent_type,
			PatientDataSourceType source_type, Class<? extends ExternalSystemAdapter> externalSystemAdapter_class) {
		super(iCode, strName, externalSystemAdapter_class);
		this.source_type = source_type;
		this.parent_type = parent_type;
	}

	public static ExternalSystemType parseCode(int code) {
		return mapExternalSystemType.get(code);
	}

	public static int count() {
		return mapExternalSystemType.size();
	}

	public static List<ExternalSystemType> getAll() {
		return new ArrayList<ExternalSystemType>(mapExternalSystemType.values());
	}

	public static ExternalSystemType parseName(String name) {
		for (Iterator<ExternalSystemType> iterator = mapExternalSystemType.values().iterator(); iterator.hasNext();) {
			ExternalSystemType type = (ExternalSystemType) iterator.next();
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

	public ExternalSystemType getParentType() {
		return this.parent_type;
	}
}
