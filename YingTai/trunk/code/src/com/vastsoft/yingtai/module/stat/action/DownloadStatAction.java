package com.vastsoft.yingtai.module.stat.action;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.FReport;
import com.vastsoft.yingtai.module.stat.entity.*;
import com.vastsoft.yingtai.module.stat.exception.StatException;
import com.vastsoft.yingtai.module.stat.service.DateAdapter;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.constants.GenderAdapter;
import com.vastsoft.yingtai.utils.FileTools;
import com.vastsoft.yingtai.utils.poi.excel.worker.ExcelWorker;
import com.vastsoft.yingtai.utils.poi.interfase.DataAdapter;

public class DownloadStatAction extends StatAction {
    private InputStream fileInputStream;

    public InputStream getDownloadFile() {
        return this.fileInputStream;
    }

    public String downloadStatDoctorWorkOfMyOrgDiagnosis(){
        try {
            super.statDoctorWorkOfMyOrgDiagnosis();
            List<Map<String,Object>> listReport = (List<Map<String,Object>>) super.takeElementFromData("listReport");
            if (listReport == null || listReport.size() <= 0)
                throw new StatException("没有数据需要导出！");
            if (listReport.size() > 500)
                throw new StatException("数据量太大，请使用时间段进行过滤！");
            if (CollectionTools.isEmpty(listReport))
                return null;
            File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
            if (!cacheFile.exists())
                cacheFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cacheFile);
            try {
                String[] fieldNames = {"user_name>医生姓名", "device_name>设备名称", "count>人次数",
                        "body_part_amount>部位数", "piece_amount>曝光次数", "stat_type>类型"};
                ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
                this.fileInputStream = new FileInputStream(cacheFile);
                this.fileName = "医生工作统计-"+DateTools.dateToString(new Date(), DateTools.TimeExactType.DAY)+".xls";
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            catchException(e);
            super.clearData();
            return ERROR;
        }
        return SUCCESS;
    }

    public String downloadStatReportByMyOrgDiaginosisCost() {
        try {
            SplitPageUtil spu = new SplitPageUtil(1, 500);
            super.setSpu(spu);
            super.statReportByMyOrgDiagnosis();
            if (spu.getTotalRow() > 500)
                throw new StatException("数据量太大，请使用时间段进行过滤！");
            List<Map<String,Object>> listReport = (List<Map<String,Object>>) super.takeElementFromData("listReport");
            if (CollectionTools.isEmpty(listReport))
                return null;
            listReport.add((Map<String,Object>) super.takeElementFromData("footing"));
            File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
            if (!cacheFile.exists())
                cacheFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cacheFile);
            try {
                String[] fieldNames = {"case_num>病历编号", "patient_name>病人姓名", "patient_gender>病人性别",
                        "dicom_img_device_type_name>影像类型", "request_org_name>申请机构", "dicom_img_check_pro>检查项目","charge_type>计费方式",
                        "charge_amount>计费数量",
                        "earn_price| 元>诊断收入", "audit_doctor_name>报告医师",
                        "report_time>报告时间"};
                ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
                this.fileInputStream = new FileInputStream(cacheFile);
                this.fileName = "报告收入统计-"+DateTools.dateToString(new Date(), DateTools.TimeExactType.DAY)+".xls";
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            catchException(e);
            super.clearData();
            return ERROR;
        }
        return SUCCESS;
    }

    public String downloadSysAccountFlowing() {
        return "downloadSysAccountFlowing";
    }

    private Map<Class<?>, DataAdapter> getMapAdapter() {
        Map<Class<?>, DataAdapter> mapAdapter = new HashMap<Class<?>, DataAdapter>();
        mapAdapter.put(Date.class, new DateAdapter());
        mapAdapter.put(Gender.class, new GenderAdapter());
        return mapAdapter;
    }

    public InputStream getStatPatientInfoForRequestOrgFileStream() throws IOException {
        super.setSpu(null);
        super.statPatientInfoForRequestOrg();
        @SuppressWarnings("unchecked")
        List<FReport> listReport = (List<FReport>) super.takeElementFromData("listReport");
        File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
        if (!cacheFile.exists())
            cacheFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(cacheFile);
        try {
//			Passport passport = takePassport();
//			SearchReportParam dsp = new SearchReportParam(false);
//			dsp.setDiagnosis_org_id(diagnosisOrgId);
//			dsp.setRequest_org_name(passport.getOrgId());
//			dsp.setSick_key_words(sick_key_words);
//			dsp.setStart(start);
//			dsp.setEnd(end);
//			dsp.setSpu(new SplitPageUtil(1, 500));
//			List<FReport> listDiagnosis = ReportService.instance.searchReport(dsp);
            String[] fieldNames = {"case_his_num>病历编号", "patient_name>病人姓名", "patient_genderStr>病人性别",
                    "device_type_name>影像类型", "part_type_name>拍摄部位", "dicom_img_check_pro>检查项目",
                    "publish_user_name>报告医师", "diagnosis_org_name>诊断机构", "create_time>申请时间"};
            ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
            return new FileInputStream(cacheFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            fos.close();
        }
    }

    public InputStream getStatPatientInfoForDiagnosisOrgFileStream() throws IOException {
        try {
            super.setSpu(new SplitPageUtil(1, 500));
            super.statPatientInfoForDiagnosisOrg();
            @SuppressWarnings("unchecked")
            List<FReport> listReport = (List<FReport>) super.takeElementFromData("listReport");
            if (CollectionTools.isEmpty(listReport))
                throw new StatException("没有结果需要导出！");
            File cacheFile = File.createTempFile(CommonTools.getUUID(), null);
            FileOutputStream fos = new FileOutputStream(cacheFile);
            try {
//				Passport passport = takePassport();
//				SearchReportParam rsp = new SearchReportParam(false);
//				rsp.setDiagnosis_org_id(passport.getOrgId());
//				rsp.setRequest_org_name(requestOrgId);
//				rsp.setSick_key_words(sick_key_words);
//				rsp.setStart(start);
//				rsp.setEnd(end);
//				rsp.setSpu(new SplitPageUtil(1, 400));
//				List<FReport> listReport = ReportService.instance.searchReport(rsp);
                String[] fieldNames = {"case_his_num>病历编号", "patient_name>病人姓名", "patient_genderStr>病人性别",
                        "device_type_name>影像类型", "dicom_img_check_pro>检查项目",
                        "request_org_name>申请机构", "publish_user_name>报告医师", "create_time>报告时间"};
                ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
            } finally {
                fos.close();
            }
            return new FileInputStream(cacheFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream getStatReportDoctorFileStream() throws IOException {
        super.setSpu(null);
        super.statReportDoctor();
        @SuppressWarnings("unchecked")
        List<FStatReportDoctor> listReport = (List<FStatReportDoctor>) takeElementFromData("listReport");
        File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
        if (!cacheFile.exists())
            cacheFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(cacheFile);
        try {
//			List<FStatReportDoctor> listReport = StatService.instance.statReportDoctor(takePassport(), mode,
//					requestOrgId, diagnosisOrgId, start, end, deviceTypeId, partTypeId, doctorId, type);
            String[] fieldNames = {"user_name>医生名称", "device_name>设备名称", "count>诊断数", "statType>类型"};
            ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
            return new FileInputStream(cacheFile);
        } catch (Exception e) {
            return null;
        } finally {
            fos.close();
        }
    }

    public InputStream getStatReportCostFileStream() throws IOException {
        return null;
//		File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
//		if (!cacheFile.exists())
//			cacheFile.createNewFile();
//		FileOutputStream fos = new FileOutputStream(cacheFile);
//		try {
//			List<FStatReportCost> listReport = StatService.instance.statReportCost(takePassport(), mode, requestOrgId,
//					diagnosisOrgId, start, end, deviceTypeId, partTypeId);
//			String[] fieldNames = { "device_name>设备名称", "part_name>部位", "print_count>打印个数", "price_count>花费" };
//			ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
//			return new FileInputStream(cacheFile);
//		} catch (Exception e) {
//			return null;
//		} finally {
//			fos.close();
//		}
    }

    public InputStream getStatDeviceReportFileStream() throws IOException {
        super.setSpu(null);
        super.statDeviceReport();
        @SuppressWarnings("unchecked")
        List<FReport> listReport = (List<FReport>) takeElementFromData("listReport");
        File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
        if (!cacheFile.exists())
            cacheFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(cacheFile);
        try {
//			List<FStatDeviceReport> listReport = StatService.instance.statDeviceReport(takePassport(), mode,
//					requestOrgId, diagnosisOrgId, start, end, deviceTypeId, partTypeId);
            String[] fieldNames = {"device_name>设备名称", "count>份数", "total>总份数", "scale|%>比例"};
            ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
            return new FileInputStream(cacheFile);
        } catch (Exception e) {
            return null;
        } finally {
            fos.close();
        }
    }

    public InputStream getStatOrgFomFileStream() throws IOException {
        super.setSpu(null);
        super.statReportOrgFom();
        @SuppressWarnings("unchecked")
        List<FStatOrgFomData> listReport = (List<FStatOrgFomData>) super.takeElementFromData("listReport");
        File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(cacheFile);
        try {
//			List<FStatOrgFomData> listReport = StatService.instance.statReportOrgFom(takePassport(), mode, start, end,
//					deviceTypeId, partTypeId, requestOrgId, diagnosisOrgId);
            String[] fieldNames = {"m_count>阳性数量", "f_count>阴性数量", "total>总数", "mScale|%>阳性比例"};
            ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
            return new FileInputStream(cacheFile);
        } catch (Exception e) {
            return null;
        } finally {
            fos.close();
        }
    }

    public InputStream getReportSickFileStream() throws IOException {
        super.setSpu(null);
        super.statReportSick();
        @SuppressWarnings("unchecked")
        List<FStatReportSick> listReport = (List<FStatReportSick>) takeElementFromData("listReport");
        File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(cacheFile);
        try {
//			List<FStatReportSick> listReport = StatService.instance.statReportSick(takePassport(), mode, start, end,
//					deviceTypeId, partTypeId, requestOrgId, diagnosisOrgId, sickName);
            String[] fieldNames = {"sick_name>病症", "count>病患数量", "total>病例总数", "scale|%>比例"};
            ExcelWorker.createExcel(listReport, fieldNames, this.getMapAdapter(), fos);
            return new FileInputStream(cacheFile);
        } catch (Exception e) {
            catchException(e);
            return null;
        } finally {
            fos.close();
        }
    }

    public InputStream getSysAccountFlowingFileStream() throws IOException {
        super.setSpu(new SplitPageUtil(1, 500));
        super.searchSysAccountFlowing();
        @SuppressWarnings("unchecked")
        List<FFinanceRecord> listFinanceRecord = (List<FFinanceRecord>) super.takeElementFromData("listFinanceRecord");
        File cacheFile = new File(FileTools.getSysPath() + "/cache" + CommonTools.getUUID() + ".xls");
        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(cacheFile);
        try {
//			Passport passport = takePassport();
//			List<FFinanceRecord> listFinanceRecord = StatService.instance.searchOrgAccountFlowing(passport,
//					request_org_id, diagnosis_org_id, type == null ? null : AccountRecordRandEType.parseCode(type),
//					FinanceService.SYS_ACCOUNT_ORG_ID, null, start, end, new SplitPageUtil(1, 500));
            String[] fieldNames = {"id>记录编号", "opeartTypeStr>操作类型", "request_org_name>申请机构", "diagnosis_org_name>诊断机构",
                    "v_opeaator_name>操作人", "reTypeName>收支类型", "price>金额", "operat_timeStr>时间"};
            ExcelWorker.createExcel(listFinanceRecord, fieldNames, this.getMapAdapter(), fos);
            return new FileInputStream(cacheFile);
        } catch (Exception e) {
            return null;
        } finally {
            fos.close();
        }
    }
    private String fileName;
    public String getFileName() {
        if (!StringTools.isEmpty(fileName)) {
            try {
                return new String(fileName.trim().getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return DateTools.dateToString(new Date()) + ".xls";
    }

}
