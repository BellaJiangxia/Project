package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity;

import java.util.Date;

public abstract class AbstractDicomEntity {
	private String uuid;
	private String udid;// 保存者的身份标识码
	private int participant_type;// 身份标识类型
	private Date create_time;//
	private byte[] thumbnail;// 缩略图
	private String specific_character_set;

//	public void valueOf(final Attributes attrs) throws DcmStorageException {
//		try {
//			final AbstractDicomEntity entity = this;
//			//attrs.setSpecificCharacterSet(attrs.getString(Tag.SpecificCharacterSet));
//			DicomAttribute.foreach(this.getDicomEntityClass(), new IterateRunnable<DicomAttribute>() {
//				public void run(DicomAttribute attribute) throws DcmStorageException {
//					Object value;
//					if (attribute.getClassType() == Integer.class) {
//						value = attrs.getInt(attribute.getTag().getTag(), 0);
//					}else if (attribute.getClassType() == String.class) {
//						value = attrs.getString(attribute.getTag().getTag());
//					}else if (attribute.getClassType() == Long.class) {
//						value = attrs.getString(attribute.getTag().getTag());
//						value = Long.valueOf(value.toString());
//					}else if (attribute.getClassType() == Double.class) {
//						value = attrs.getDouble(attribute.getTag().getTag(), 0.0);
//					}else {
//						value = attrs.getString(attribute.getTag().getTag());
//					}
//					if (value == null){
//						LoggerUtils.logger.debug("获取到属性："+attribute.getName()+" 的值为null！");
//						if (attribute.isRequired())
//							throw new DcmStorageException("实体："+entity.getClass().getName()+"字段[" + attribute.getName() + "]是必须值，但当前为null！");
//						return;
//					}
//					if (!ReflectTools.writeProperty(entity, attribute.getName(), value))
//						throw new DcmStorageException("实体："+entity.getClass().getName()+"字段[" + attribute.getName() + "]是写入失败！");
//				}
//			});
//		} catch (BaseException e) {
//			e.printStackTrace();
//			throw (DcmStorageException) e;
//		}
//	}
//	
//	public Attributes toAttrs() throws DcmStorageException {
//		try {
//			final Attributes attrs = new Attributes();
//			final AbstractDicomEntity entity = this;
//			DicomAttribute.foreach(this.getDicomEntityClass(), new IterateRunnable<DicomAttribute>() {
//				public void run(DicomAttribute attribute) {
//					Object value = ReflectTools.readProperty(entity, attribute.getName());
//					attrs.setValue(attribute.getTag().getTag(), attribute.getVr(), value);
//				}
//			});
//			return attrs;
//		} catch (BaseException e) {
//			e.printStackTrace();
//			throw new DcmStorageException(e);
//		}
//	}

//	public abstract DicomEntityClass getDicomEntityClass();
	
//	@JsonIgnore
//	public String getUniqueKeyValue() {
//		DicomAttribute attribute = DicomAttribute.takeUniqueKeyByDicomEntityClass(this.getDicomEntityClass());
//		Object rr = ReflectTools.readProperty(this, attribute.getName());
//		return rr==null?null:rr.toString();
//	}

	public abstract void writeFatherUuid(String father_uuid);

	public abstract String readFatherUuid();

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public int getParticipant_type() {
		return participant_type;
	}

	public void setParticipant_type(int participant_type) {
		this.participant_type = participant_type;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

//	public ParticipantType getParticipantType() throws BaseException {
//		return ParticipantType.parseCode(this.participant_type);
//	}

//	public boolean isSameOne(AbstractDicomEntity entityTmp) throws BaseException {
//		if (this.getClass() != entityTmp.getClass())
//			return false;
//		if (this.getUniqueKeyValue() != null && this.getUniqueKeyValue().equals(entityTmp.getUniqueKeyValue()))
//			if (!StringTools.isEmpty(this.getUdid()) && this.getUdid().equals(entityTmp.getUdid()))
//				if (this.getParticipantType() != null && this.participant_type == entityTmp.getParticipant_type())
//					return true;
//		return false;
//	}
//	
//	/**获取判断是否相同的MAP*/
//	@JsonIgnore
//	public Map<String, Object> getSamePropertyMap() {
//		DicomAttribute attribute = DicomAttribute.takeUniqueKeyByDicomEntityClass(this.getDicomEntityClass());
//		return new MapBuilder<String, Object>().put(attribute.getName(), this.getUniqueKeyValue()).put("udid", udid)
//				.put("participant_type", participant_type).toMap();
//	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

//	public abstract void checkEntityWholeness() throws DcmEntityUnWholenessException;

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getSpecific_character_set() {
		return specific_character_set;
	}

	public void setSpecific_character_set(String specific_character_set) {
		this.specific_character_set = specific_character_set;
	}
}
