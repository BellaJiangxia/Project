package com.vastsoft.yingtai.module.stat.entity;

public class FStatDeviceReport extends FStatData{
	private String device_name;
	
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public int getScale(){
		return Math.round(this.getCount()*100/((float)this.getTotal()));
	}
	
//	@Override
//	public boolean deviceReportEquals(FStatData obj) {
//		if(!super.deviceReportEquals(obj))return false;
//		return StringTools.strEquals(getDevice_name(), ((FStatDeviceReport)obj).getDevice_name());
//	}
//	
//	public static List<FStatDeviceReport> built(List<FStatDeviceReport> listSrc){
//		if (listSrc==null)return null;
//		for (FStatDeviceReport fsdr : listSrc) {
//			for (FStatDeviceReport fsdrTmp : listSrc) {
//				if (fsdr.deviceReportEquals(fsdrTmp)) {
//					fsdrTmp.setTotal(fsdrTmp.getTotal()+fsdr.getCount());
//				}
//			}
//		}
//		return listSrc;
//	} 
}
