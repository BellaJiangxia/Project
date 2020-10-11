package com.vastsoft.yingtai.module.sys.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.sys.entity.FReportTemplate;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.entity.TReportTemplate;
import com.vastsoft.yingtai.module.sys.entity.TTemplateStat;

public interface SysMapper {
	
	public void insertDicValue(TDicValue tdv);

	public void deleteDicValueById(long id);

	public void modifyDicValueyId(TDicValue dicv);

	public List<TDicValue> selectValuesByType(int dic_type);

	public TDicValue selectValueById(long id);

	public TDicValue selectValueByIdForUpdate(long id);

//	public List<TDicValue> queryDicValueListByParentId(long father_dic_id);
	
	public List<TDicValue> selectValuesByTypeAndValue(Map<String, Object> mapArg);
	
	public List<TDicValue> selectValuesByTypeForUpdate(int dic_type);
	
	

	public void insertReportTemplate(TReportTemplate trt);

	public FReportTemplate selectTemplateById(long templId);

	public void modifyReportTemplate(TReportTemplate template);

	public void deleteReportTemplate(long lTemplId);

	public List<TReportTemplate> searchTemplate(Map<String, Object> prms);

	public int searchTemplateCount(Map<String, Object> prms);

	public int searchFTemplateCount(Map<String, Object> prms);

	public List<FReportTemplate> searchFTemplate(Map<String, Object> prms);

	public int searchMyAndPublicFTemplateCount(Map<String, Object> mapArg);

	public List<FReportTemplate> searchMyAndPublicFTemplate(Map<String, Object> mapArg);

	public List<FReportTemplate> queryCommonTemplateList(long user_id);

	public List<TTemplateStat> selectTemplateByUserIdAndTemplateId(Map<String, Object> mapArg);

	public void addTemplateStat(TTemplateStat templateStat);

	public void updateTemplateStat(TTemplateStat templateStat);

	public List<TDicValue> queryDicValueListByTypeAndParentId(TDicValue dicValue);


}
