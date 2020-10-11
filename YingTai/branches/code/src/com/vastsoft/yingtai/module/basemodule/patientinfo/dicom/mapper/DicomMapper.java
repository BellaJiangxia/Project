package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;

public interface DicomMapper {

	public void insertDicomImg(TDicomImg dicomImg);

	public int selectDicomImgCount(Map<String, Object> mapArg);

	public List<FDicomImg> selectDicomImg(Map<String, Object> mapArg);

	public TDicomImg selectDicomImgByIdForUpdate(long id);

	public void updateDicomImg(TDicomImg dicomImg);

	public TDicomImg selectDicomImgByMarkCharForUpdate(String mark_char);
	
	public TDicomImg selectDicomImgByMarkCharAndCaseIdForUpdate(TDicomImg dicomImg);

	public TDicomImg selectDicomImgByThumbnailUidForUpdate(String thumbnail_uid);

	public List<FSeries> selectSeries(Map<String, Object> mapArg);

	public TSeries selectSeriesByIdForUpdate(long id);

	public List<TSeries> selectSeriesByMarkCharForUpdate(String mark_char);

	public List<TSeries> selectSeriesByThumbnailUidForUpdate(String thumbnail_uid);

	public void updateSeries(TSeries series);

	public void insertSeries(TSeries series);

	public TSeries selectSeriesByMarkCharAndImgIdForUpdate(TSeries tSeries);
	
}
