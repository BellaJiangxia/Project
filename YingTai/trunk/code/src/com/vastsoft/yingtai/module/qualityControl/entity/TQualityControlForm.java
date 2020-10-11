package com.vastsoft.yingtai.module.qualityControl.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlEnforceable;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormState;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;

/** 一张质控填表 */
public class TQualityControlForm {
	private long id;
	private String name;
	private int target_type;//控制的目标类型
//	private int question_occasion;// 询问时机,可以是过个，使用,隔开
	private int question_enforceable;// 回答强制性
	private int state;
	private long create_user_id;
	private Date create_time;
	private long modify_user_id;
	private Date modify_time;
	private String note;//表格说明
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTarget_type() {
		return target_type;
	}

	public String getTarget_type_name() {
		QualityControlTarget qct = QualityControlTarget.parseCode(target_type);
		return qct == null ? "" : qct.getName();
	}

	public void setTarget_type(int target_type) {
		this.target_type = target_type;
	}

	public int getQuestion_enforceable() {
		return question_enforceable;
	}
	
	public String getQuestion_enforceable_name() {
		QualityControlEnforceable q = QualityControlEnforceable.parseCode(question_enforceable);
		return q==null?"":q.getName();
	}

	public void setQuestion_enforceable(int question_enforceable) {
		this.question_enforceable = question_enforceable;
	}

	public int getState() {
		return state;
	}
	
	public String getState_name() {
		QualityControlFormState q = QualityControlFormState.parseCode(state);
		return q==null?"":q.getName();
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public long getModify_user_id() {
		return modify_user_id;
	}

	public void setModify_user_id(long modify_user_id) {
		this.modify_user_id = modify_user_id;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + (int) (create_user_id ^ (create_user_id >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((modify_time == null) ? 0 : modify_time.hashCode());
		result = prime * result + (int) (modify_user_id ^ (modify_user_id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + question_enforceable;
		result = prime * result + state;
		result = prime * result + target_type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TQualityControlForm other = (TQualityControlForm) obj;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (create_user_id != other.create_user_id)
			return false;
		if (id != other.id)
			return false;
		if (modify_time == null) {
			if (other.modify_time != null)
				return false;
		} else if (!modify_time.equals(other.modify_time))
			return false;
		if (modify_user_id != other.modify_user_id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (question_enforceable != other.question_enforceable)
			return false;
		if (state != other.state)
			return false;
		if (target_type != other.target_type)
			return false;
		return true;
	}
	
}
