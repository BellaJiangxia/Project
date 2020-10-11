package com.vastsoft.yingtai.module.stat.assist;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.stat.entity.AbstractStatEntity;

/**
 * Created by jiangyubin on 2017/1/18.
 */
public class SearchStatParam extends AbstractSearchParam<AbstractStatEntity> {
    private Long diagnosis_org_id;
    private Long request_org_id;
    private Long dicom_img_device_type_id;
    private PatientDataSourceType source_type;
    private OrgProductChargeType charge_type;
    private Integer work_type = 1;//1标识报告，2标识审核，其他都默认为报告
    private Long doctor_id;

    public Long getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Integer getWork_type() {
        return work_type;
    }

    public void setWork_type(Integer work_type) {
        this.work_type = work_type;
    }

    public Long getDicom_img_device_type_id() {
        return dicom_img_device_type_id;
    }

    public void setDicom_img_device_type_id(Long dicom_img_device_type_id) {
        this.dicom_img_device_type_id = dicom_img_device_type_id;
    }

    public PatientDataSourceType getSource_type() {
        return source_type;
    }

    public void setSource_type(PatientDataSourceType source_type) {
        this.source_type = source_type;
    }

    public Long getRequest_org_id() {
        return request_org_id;
    }

    public void setRequest_org_id(Long request_org_id) {
        this.request_org_id = request_org_id;
    }

    public Long getDiagnosis_org_id() {
        return diagnosis_org_id;
    }

    public void setDiagnosis_org_id(Long diagnosis_org_id) {
        this.diagnosis_org_id = diagnosis_org_id;
    }

    public OrgProductChargeType getCharge_type() {
        return charge_type;
    }

    public void setCharge_type(OrgProductChargeType charge_type) {
        this.charge_type = charge_type;
    }
}
