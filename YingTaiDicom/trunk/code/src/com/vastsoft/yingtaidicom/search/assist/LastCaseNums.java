package com.vastsoft.yingtaidicom.search.assist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;

public class LastCaseNums {
	private Map<RemoteParamsType, LinkedHashSet<CaseNum>> data = new HashMap<RemoteParamsType, LinkedHashSet<CaseNum>>();

	public void add(RemoteParamsType paramsType, String caseNum,String patient_name) {
		LinkedHashSet<CaseNum> setStr = this.data.get(paramsType);
		if (setStr==null) {
			setStr = new LinkedHashSet<CaseNum>();
			this.data.put(paramsType, setStr);
		}
		setStr.add(new CaseNum(caseNum, patient_name));
	}
	
	public void itera(DoEach doEach) throws Exception{
		for (Iterator<RemoteParamsType> iterator = this.data.keySet().iterator(); iterator.hasNext();) {
			RemoteParamsType type = (RemoteParamsType) iterator.next();
			Set<CaseNum> listCaseNum = this.data.get(type);
			if (listCaseNum == null || listCaseNum.size() <= 0)
				continue;
			for (CaseNum caseNum : listCaseNum) {
				doEach.run(type, caseNum.caseNum, caseNum.patient_name);
			}
		}
	}
	
	public interface DoEach{
		public void run(RemoteParamsType paramsType,String caseNum,String patient_name) throws Exception;
	}
	
	private class CaseNum{
		private String caseNum;
		private String patient_name;
		public CaseNum(String caseNum, String patient_name) {
			super();
			this.caseNum = caseNum;
			this.patient_name = patient_name;
		}
		
	}
}
