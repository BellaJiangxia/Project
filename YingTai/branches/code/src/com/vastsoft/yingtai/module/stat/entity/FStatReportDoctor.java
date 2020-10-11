package com.vastsoft.yingtai.module.stat.entity;

import java.util.List;

public class FStatReportDoctor {
    private String user_name;
    private String org_name;
    private String device_name;
    private int count;
    private int stat_type;
    private int body_part_amount;//部位数
    private int piece_amount;//影片数

    public String getUser_name() {
        return user_name;
    }

    public String getOrg_name() {
        return org_name;
    }

    public String getDevice_name() {
        return device_name;
    }

    public int getCount() {
        return count;
    }

    public int getStat_type() {
        return stat_type;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStat_type(int stat_type) {
        this.stat_type = stat_type;
    }

    public int getBody_part_amount() {
        return body_part_amount;
    }

    public void setBody_part_amount(int body_part_amount) {
        this.body_part_amount = body_part_amount;
    }

    public int getPiece_amount() {
        return piece_amount;
    }

    public void setPiece_amount(int piece_amount) {
        this.piece_amount = piece_amount;
    }

    public String getStatType() {
        if (this.stat_type == 2)
            return "审核";
        return "报告";
    }

    public static void setStatTypeForList(List<FStatReportDoctor> listReport, int stat_type) {
        if (listReport == null) return;
        for (FStatReportDoctor fStatReportDoctor : listReport)
            fStatReportDoctor.setStat_type(stat_type);
    }
}
