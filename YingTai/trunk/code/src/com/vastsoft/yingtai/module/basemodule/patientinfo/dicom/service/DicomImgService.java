package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.collection.UniqueList;
import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.assist.SearchDicomImgParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.assist.SearchSeriesParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception.DicomImgException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.mapper.DicomMapper;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.RemoteServerService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteNumEntry;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.orgAffix.entity.TOrgAffix;
import com.vastsoft.yingtai.module.org.orgAffix.service.OrgAffixService;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.exception.SysOperateException;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class DicomImgService {
    public static final DicomImgService instance = new DicomImgService();

    private DicomImgService() {

    }

    /**
     * 通过ID查询病例图像详细信息
     *
     * @param dicom_img_id
     * @throws SysOperateException
     * @throws DicomImgException
     * @return
     */
    public FDicomImg queryDicomImgById(long dicom_img_id) throws DicomImgException, SysOperateException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            return this.queryDicomImgById(dicom_img_id, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 通过ID查询病例图像详细信息
     *
     * @param session
     * @throws SysOperateException
     * @throws DicomImgException
     * @throws Exception
     */
    public FDicomImg queryDicomImgById(long dicom_img_id, SqlSession session) throws DicomImgException, SysOperateException {
        SearchDicomImgParam dsp = new SearchDicomImgParam();
        dsp.setImgId(dicom_img_id);
        List<FDicomImg> listDicomImg = this.searchDicomImg(dsp, session);
        return (listDicomImg == null || listDicomImg.size() <= 0) ? null : listDicomImg.get(0);
    }

    private List<FDicomImg> searchDicomImg(SearchDicomImgParam dsp, SqlSession session) throws DicomImgException, SysOperateException {
        DicomMapper mapper = session.getMapper(DicomMapper.class);
        Map<String, Object> mapArg = dsp.buildMap();
        if (dsp.getSpu() != null) {
            int count = mapper.selectDicomImgCount(mapArg);
            dsp.getSpu().setTotalRow(count);
            if (count <= 0)
                return null;
            mapArg.put("minRow", dsp.getSpu().getCurrMinRowNum());
            mapArg.put("maxRow", dsp.getSpu().getCurrMaxRowNum());
        }
        List<FDicomImg> result = mapper.selectDicomImg(mapArg);
        if (CollectionTools.isEmpty(result))
            return result;
        for (FDicomImg dicomImg : result) {
            SearchSeriesParam ssp = new SearchSeriesParam();
            ssp.setDicom_img_id(dicomImg.getId());
            List<FSeries> listSeries = this.searchSeries(ssp);
            if (CollectionTools.isEmpty(listSeries))
                continue;
            dicomImg.setListSeries(listSeries);
            List<TDicValue> listCheckProDic = new UniqueList<TDicValue>();
            for (FSeries fSeries : listSeries) {
                List<TDicValue> listCheckProDicTmp = SysService.instance
                        .queryCheckProByPartTypeId(fSeries.getPart_type_id());
                listCheckProDic.addAll(listCheckProDicTmp);
            }
            List<TDicValue> listBodyPartType = SysService.instance.queryDicValueListByIds(dicomImg.getBody_part_ids());
            dicomImg.setListBodyPartType(listBodyPartType);
            dicomImg.buildListCheckProByDicValues(listCheckProDic);
        }
        return result;
    }

    private List<FDicomImg> searchDicomImg(SearchDicomImgParam dsp) throws Exception {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            return this.searchDicomImg(dsp, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    // /** 保存病例图像，添加或者修改 */
    // public void saveDicomImg(Passport passport, TDicomImg dicomImg,
    // DicomMapper dicomMapper) {
    //
    // }

    /**
     * 通过病例ID查询影像列表
     *
     * @throws Exception
     */
    public List<FDicomImg> queryDicomImgByCaseHisId(long case_id) throws Exception {
        SearchDicomImgParam dsp = new SearchDicomImgParam();
        dsp.setCase_id(case_id);
        List<FDicomImg> listDicomImg = this.searchDicomImg(dsp);
        return listDicomImg;
    }

    public List<FSeries> searchSeries(SearchSeriesParam ssp) throws DicomImgException {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            return this.searchSeries(ssp, session);
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FSeries> searchSeries(SearchSeriesParam ssp, SqlSession session) throws DicomImgException {
        Map<String, Object> mapArg;
        try {
            mapArg = ssp.buildMap();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DicomImgException(e);
        }
        DicomMapper mapper = session.getMapper(DicomMapper.class);
        return mapper.selectSeries(mapArg);
    }

    /**
     * 通过图像唯一标识查询图像
     *
     * @throws Exception
     */
    public FDicomImg queryDicomImgByMarkChar(String strMarkChar) throws Exception {
        SearchDicomImgParam dsp = new SearchDicomImgParam();
        dsp.setMark_char(strMarkChar);
        List<FDicomImg> listDicomImg = this.searchDicomImg(dsp);
        return (listDicomImg == null || listDicomImg.size() <= 0) ? null : listDicomImg.get(0);
    }

    public FDicomImg queryDicomImgByThumbnailUid(String thumbnail_uid) throws Exception {
        SearchDicomImgParam dsp = new SearchDicomImgParam();
        dsp.setThumbnail_uid(thumbnail_uid);
        List<FDicomImg> listDicomImg = this.searchDicomImg(dsp);
        return (listDicomImg == null || listDicomImg.size() <= 0) ? null : listDicomImg.get(0);
    }

    private TDicomImg modifyDicomImg(Passport passport, TDicomImg dicomImg, SqlSession session) throws BaseException {
        this.checkDicomImgObj(dicomImg);
        if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
            throw new DicomImgException("你没有病例管理权限！");
        if (StringTools.isEmpty(dicomImg.getCheck_pro()))
            throw new DicomImgException("影像的检查项目必须指定！");
        DicomMapper mapper = session.getMapper(DicomMapper.class);
        TDicomImg oldDicomImg = mapper.selectDicomImgByIdForUpdate(dicomImg.getId());
        if (oldDicomImg == null)
            throw new DicomImgException("没有找到要修改的影像信息！");
        if (oldDicomImg.equals(dicomImg))
            return oldDicomImg;
        oldDicomImg.setCheck_pro(dicomImg.getCheck_pro());
        oldDicomImg.setBody_part_ids_arr(dicomImg.getBody_part_ids_arr());
        oldDicomImg.setPiece_amount(dicomImg.getPiece_amount());
        mapper.updateDicomImg(oldDicomImg);
        // SysService.instance.queryAndAddCheckPro(dicomImg.getCheck_pro(),
        // dicomImg.getPart_type_id(), session);
        return dicomImg;
    }

    private void checkDicomImgObj(TDicomImg dicomImg) throws DicomImgException {
        if (dicomImg == null)
            throw new DicomImgException("请指定影像信息！");
        if (dicomImg.getAe_title() != null) {
            dicomImg.setAe_title(dicomImg.getAe_title().trim());
            if (dicomImg.getAe_title().length() >= 2000)
                throw new DicomImgException("AE号太长，最长2000个字！");
        }
        if (dicomImg.getAffix_id() <= 0)
            throw new DicomImgException("影像的机构附件ID必须指定！");
        if (dicomImg.getCase_id() <= 0)
            throw new DicomImgException("影像的所属病例ID必须指定！");
        if (dicomImg.getCheck_pro() != null) {
            dicomImg.setCheck_pro(dicomImg.getCheck_pro().trim());
            if (dicomImg.getCheck_pro().length() >= 1000)
                throw new DicomImgException("影像检查项目太长了，最长20个");
        }
        if (dicomImg.getCheck_time() == null) {
            dicomImg.setCheck_time(new Date());
        }
        if (dicomImg.getDevice_type_id() <= 0)
            throw new DicomImgException("影像的设备类型ID必须指定！");
        if (dicomImg.getEps_num() != null) {
            dicomImg.setEps_num(dicomImg.getEps_num().trim());
            if (dicomImg.getEps_num().length() >= 256)
                throw new DicomImgException("影像过的EPS号太长，最长256个字！");
        }
        if (dicomImg.getMark_char() == null)
            throw new DicomImgException("影像的唯一标识必须指定！");
        dicomImg.setMark_char(dicomImg.getMark_char().trim());
        if (dicomImg.getMark_char().isEmpty())
            throw new DicomImgException("影像的唯一标识必须指定！");
        if (dicomImg.getMark_char().length() >= 256)
            throw new DicomImgException("影像的唯一标识太长，最长256个字！");
        if (ArrayTools.isEmpty(dicomImg.getBody_part_ids_arr()))
            throw new DicomImgException("影像的部位类型必须指定！");
        if (dicomImg.getPiece_amount() < 0)
            throw new DicomImgException("影像的影片数量必须指定！");
        // if (dicomImg.getPart_type_id() <= 0)
        // throw new DicomImgException("影像的部位类型ID必须指定！");
        // if (dicomImg.getThumbnail_uid() != null) {
        // dicomImg.setThumbnail_uid(dicomImg.getThumbnail_uid().trim());
        // if (dicomImg.getThumbnail_uid().length() >= 256)
        // throw new DicomImgException("影像的缩略图标识太长，最长256个字！");
        // }
    }

    public List<TDicomImg> modifyDicomImgList(Passport passport, List<TDicomImg> listDicomImg, SqlSession session)
            throws BaseException {
        if (CollectionTools.isEmpty(listDicomImg))
            return listDicomImg;
        List<TDicomImg> result = new ArrayList<TDicomImg>();
        for (TDicomImg dicomImg : listDicomImg) {
            result.add(this.modifyDicomImg(passport, dicomImg, session));
        }
        return result;
    }

    /**
     * 查询最近几天的影像号
     *
     * @param passport
     * @return
     * @throws BaseException
     * @throws BaseException
     */
    public Map<ShareRemoteParamsType, List<ShareRemoteNumEntry>> queryLastDicomImgNum(Passport passport) throws BaseException {
        TOrgAffix orgAffix = OrgAffixService.instance.queryOrgAffixByOrgIdWithDefaultAffix(passport.getOrgId());
        if (orgAffix == null)
            throw new DicomImgException("没有找到本机构的远程配置！");
        TOrganization org = OrgService.instance.searchOrgById(passport.getOrgId());
        Map<ShareRemoteParamsType, String> params = new HashMap<ShareRemoteParamsType, String>();
        params.put(ShareRemoteParamsType.ORG_CODE, String.valueOf(org.getOrg_code()));
        return RemoteServerService.instance.onSearchRemotePascCaseNum(CollectionTools.buildList(orgAffix), params);
    }

    /**
     * 获取一个影像的缩略图，优先级顺序与参数列表顺序一致
     *
     * @param dicom_img_series_id
     * @param series_mark_char
     * @param thumbnail_uid
     * @param affix_id
     * @return
     * @throws Exception
     */
    public InputStream readThumbnail(long dicom_img_series_id, String series_mark_char, String thumbnail_uid,
                                     long affix_id) throws Exception {
        SqlSession session = null;
        try {
            session = SessionFactory.getSession();
            DicomMapper mapper = session.getMapper(DicomMapper.class);
            TSeries series = null;
            if (dicom_img_series_id > 0)
                series = mapper.selectSeriesByIdForUpdate(dicom_img_series_id);
            if (series == null && !StringTools.isEmpty(series_mark_char)) {
                List<TSeries> listSeries = mapper.selectSeriesByMarkCharForUpdate(series_mark_char);
                series = CollectionTools.isEmpty(listSeries) ? null : listSeries.get(listSeries.size() - 1);
            }
            if (series == null && !StringTools.isEmpty(thumbnail_uid)) {
                List<TSeries> listSeries = mapper.selectSeriesByThumbnailUidForUpdate(thumbnail_uid);
                series = CollectionTools.isEmpty(listSeries) ? null : listSeries.get(listSeries.size() - 1);
            }
            if (series != null) {
                if (!ArrayTools.isEmpty(series.getThumbnail_data()))
                    return new ByteArrayInputStream(series.getThumbnail_data());
                else {
                    TOrganization org = null;
                    TDicomImg dicomImg = mapper.selectDicomImgByIdForUpdate(series.getDicom_img_id());
                    TOrgAffix orgAffix = OrgAffixService.instance.selectOrgAffixById(dicomImg.getAffix_id());
                    if (orgAffix != null) {
                        if (orgAffix.getOrg_id() > 0)
                            org = OrgService.instance.searchOrgById(orgAffix.getOrg_id());
                        if (org == null) {
                            TCaseHistory caseHistory = CaseHistoryService.instance
                                    .queryCaseHistoryById(dicomImg.getCase_id(), false);
                            if (caseHistory != null && caseHistory.getOrg_id() > 0)
                                org = OrgService.instance.searchOrgById(caseHistory.getOrg_id());
                        }
                        if (org != null) {
                            Map<ShareRemoteParamsType, String> params = new HashMap<>();
                            params.put(ShareRemoteParamsType.ORG_CODE, String.valueOf(org.getOrg_code()));
                            byte[] thumbnail = RemoteServerService.instance.onReadThumbnail(orgAffix, params,
                                    series.getThumbnail_uid());
                            if (!ArrayTools.isEmpty(thumbnail)) {
                                series.setThumbnail_data(thumbnail);
                                mapper.updateSeries(series);
                                session.commit();
                                return new ByteArrayInputStream(thumbnail);
                            }
                        }
                    }
                }
            }
            if (!StringTools.isEmpty(thumbnail_uid) && affix_id > 0) {
                TOrgAffix orgAffix = OrgAffixService.instance.selectOrgAffixById(affix_id);
                if (orgAffix != null) {
                    TOrganization org = OrgService.instance.searchOrgById(orgAffix.getOrg_id());
                    Map<ShareRemoteParamsType, String> params = new HashMap<>();
                    params.put(ShareRemoteParamsType.ORG_CODE, String.valueOf(org.getOrg_code()));
                    byte[] thumbnail = RemoteServerService.instance.onReadThumbnail(orgAffix, params, thumbnail_uid);
                    if (!ArrayTools.isEmpty(thumbnail))
                        return new ByteArrayInputStream(thumbnail);
                }
            }
            return null;
        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            SessionFactory.closeSession(session);
        }
    }

    public List<FSeries> querySeriesByDicomImgId(long dicom_img_id, SqlSession session) throws DicomImgException {
        SearchSeriesParam ssp = new SearchSeriesParam();
        ssp.setDicom_img_id(dicom_img_id);
        return this.searchSeries(ssp, session);
    }
}
