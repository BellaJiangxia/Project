package com.vastsoft.yingtai.module.sys.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.sys.constants.DictionaryType;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.exception.SysOperateException;
import com.vastsoft.yingtai.module.sys.mapper.SysMapper;

abstract class SysBaseService {
	/**
	 * 根据父字典，字典类型查询子字典
	 * @throws SysOperateException 
	 * 
	 * @throws BaseException
	 */
	public List<TDicValue> queryDicValueListByParent(long lParentId, DictionaryType dicType, SqlSession session) throws SysOperateException {
		SysMapper mapper = session.getMapper(SysMapper.class);
		if (lParentId <= 0)
			throw new SysOperateException("指定的父字典无效！lParentId：" + lParentId);
		TDicValue dicvalue = mapper.selectValueById(lParentId);
		if (dicvalue.getDic_type() == DictionaryType.DEVICE_TYPE.getCode())
			if (dicType == null)
				dicType = DictionaryType.BODY_PART_TYPE;
		List<TDicValue> listDicValue = mapper
				.queryDicValueListByTypeAndParentId(new TDicValue(dicType.getCode(), lParentId));
		return listDicValue;
	}

	/**
	 * 根据ID获取字典值
	 * 
	 * @throws BaseException
	 */
	public TDicValue queryDicValueById(long lDicValueId, SqlSession session) {
		return session.getMapper(SysMapper.class).selectValueById(lDicValueId);
	}
}
