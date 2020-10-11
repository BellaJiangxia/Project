package com.vastsoft.yingtai.module.sys.mapper;

import java.util.List;

import com.vastsoft.yingtai.module.sys.entity.TFile;

public interface FileMapper {

	public void addFileMgr(TFile file);

	public TFile queryFileMgrById(long lFileId);

	public void deleteFileMgrById(long lFileId);

	public List<TFile> queryFileMgrByIdsStr(List<Long> buildINSqlStr);

//	public List<TFile> queryFileMgrByIdsStr(List<Long> listFileId);

}
