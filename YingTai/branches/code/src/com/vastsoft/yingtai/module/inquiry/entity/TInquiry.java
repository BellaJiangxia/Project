package com.vastsoft.yingtai.module.inquiry.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vastsoft on 2016/7/30.
 */
public class TInquiry {
  private long inquiry_id;
  private long patient_id;
  private long org_id;
  private long case_id;
  private String inquiry_content;
  private String inquiry_image;
  private Date inquiry_time;
  private Date last_read_time;

  private String patient_name;

  private List<TInquiryReply> replies;
  private List<Map<String,Object>> images;

  public long getInquiry_id() {
    return inquiry_id;
  }

  public void setInquiry_id(long inquiry_id) {
    this.inquiry_id = inquiry_id;
  }

  public long getPatient_id() {
    return patient_id;
  }

  public void setPatient_id(long patient_id) {
    this.patient_id = patient_id;
  }

  public long getOrg_id() {
    return org_id;
  }

  public void setOrg_id(long org_id) {
    this.org_id = org_id;
  }

  public long getCase_id() {
    return case_id;
  }

  public void setCase_id(long case_id) {
    this.case_id = case_id;
  }

  public String getInquiry_content() {
    return inquiry_content;
  }

  public void setInquiry_content(String inquiry_content) {
    this.inquiry_content = inquiry_content;
  }

  public String getInquiry_image() {
    return inquiry_image;
  }

  public void setInquiry_image(String inquiry_image) {
    this.inquiry_image = inquiry_image;
  }

  public Date getInquiry_time() {
    return inquiry_time;
  }

  public void setInquiry_time(Date inquiry_time) {
    this.inquiry_time = inquiry_time;
  }

  public Date getLast_read_time() {
    return last_read_time;
  }

  public void setLast_read_time(Date last_read_time) {
    this.last_read_time = last_read_time;
  }

  public String getPatient_name() {
    return patient_name;
  }

  public void setPatient_name(String patient_name) {
    this.patient_name = patient_name;
  }

  public List<TInquiryReply> getReplies() {
    return replies;
  }

  public void setReplies(List<TInquiryReply> replies) {
    this.replies = replies;
  }

  public List<Map<String,Object>> getImages() {
    if(this.inquiry_image==null || this.inquiry_image.isEmpty()) return null;
    try {
      JSONArray arr=new JSONArray(this.inquiry_image);

      if(arr!=null){
        images=new ArrayList<>();
        for(int i=0,len=arr.length();i<len;i++){
          JSONObject oo=arr.getJSONObject(i);
          Map<String,Object> map=new HashMap<>();
          Iterator it=oo.keys();
          while(it.hasNext()){
            String key= (String) it.next();
            if(oo.get(key) instanceof JSONArray){
              List<String> list=new ArrayList<>();
              JSONArray items= (JSONArray) oo.get(key);
              for(int k=0,itemLen=items.length();k<itemLen;k++){
                list.add(items.getString(k));
              }
              map.put(key,list);
            }else{
              map.put(key,oo.get(key));
            }
          }
          images.add(map);
        }
      }
    } catch (JSONException e) {
      images=null;
    }
    return images;
  }

  public void setImageJsonString(List<Map<String,Object>> list){
    try {
      if(list!=null&&!list.isEmpty()){
        JSONArray arr=new JSONArray();
        for(Map<String,Object> map:list){
          JSONObject jo=new JSONObject();
          jo.put("idx", map.get("idx"));
          jo.put("images",new JSONArray((Collection) map.get("images")));
          arr.put(jo);
        }

        this.inquiry_image=arr.toString();
      }
    } catch (Exception e) {
      this.inquiry_image="";
    }
  }
}
