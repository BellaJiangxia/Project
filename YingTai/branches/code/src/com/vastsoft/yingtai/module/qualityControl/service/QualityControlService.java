package com.vastsoft.yingtai.module.qualityControl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.module.qualityControl.assist.SearchQualityControlFormAnswerParam;
import com.vastsoft.yingtai.module.qualityControl.assist.SearchQualityControlFormParam;
import com.vastsoft.yingtai.module.qualityControl.assist.SearchQualityControlMeasureParam;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlEnforceable;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormAnswerStatus;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormState;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlMeasureQuestionType;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;
import com.vastsoft.yingtai.module.qualityControl.entity.FQualityControlForm;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlForm;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlFormAnswer;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlMeasure;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlMeasureAnswer;
import com.vastsoft.yingtai.module.qualityControl.entity.VQualityControlFormAnswer;
import com.vastsoft.yingtai.module.qualityControl.exception.QualityControlException;
import com.vastsoft.yingtai.module.qualityControl.mapper.QualityControlMapper;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class QualityControlService {
	public static final QualityControlService instance = new QualityControlService();
	private static final Map<String, Set<Long>> mapListQualityControlFormAnswerQCUid = new HashMap<String, Set<Long>>();
	private static final Set<Long> listIgnoredFormAnswer = new HashSet<Long>();

	public void addQualityControlFormAnswerQCUid(String uid, Set<Long> listControlFormAnswerIds) {
		mapListQualityControlFormAnswerQCUid.put(uid, listControlFormAnswerIds);
	}

	/**
	 * 搜索质控指标
	 * 
	 * @param sqcmp
	 * @param mapper
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private List<TQualityControlMeasure> searchQualityControlMeasure(SearchQualityControlMeasureParam sqcmp,
			QualityControlMapper mapper) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> mapArg = sqcmp.buildMap();
		if (sqcmp.getSpu() != null) {
			long count = mapper.selectQualityControlMeasureCount(mapArg);
			sqcmp.getSpu().setTotalRow(count);
			if (count <= 0)
				return null;
			mapArg.put("minRow", sqcmp.getSpu().getCurrMinRowNum());
			mapArg.put("maxRow", sqcmp.getSpu().getCurrMaxRowNum());
		}
		return mapper.selectQualityControlMeasure(mapArg);
	}

	/**
	 * 添加质控表
	 * 
	 * @param passport
	 * @param controlForm
	 * @param listQualityControlMeasure
	 * @return
	 * @throws Exception
	 */
	public TQualityControlForm addQualityControlForm(Passport passport, TQualityControlForm controlForm,
			List<TQualityControlMeasure> listQualityControlMeasure) throws Exception {
		if (passport.getUserType().getCode() != UserType.SUPER_ADMIN.getCode()
				&& !UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_QUALITY_CONTROL))
			throw new NotPermissionException(UserPermission.ADMIN_QUALITY_CONTROL);
		this.checkQualityControlFormObj(controlForm);
		if (CollectionTools.isEmpty(listQualityControlMeasure))
			throw new QualityControlException("质控问卷的质控指标必须至少有一个！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			SearchQualityControlFormParam sqcfp = new SearchQualityControlFormParam();
			sqcfp.setName(controlForm.getName());
			List<FQualityControlForm> listOldQualityControlForm = this.searchQualityControlForm(sqcfp, mapper);
			if (!CollectionTools.isEmpty(listOldQualityControlForm))
				throw new QualityControlException("质控问卷重名，请重新命名！");
			controlForm.setCreate_time(new Date());
			controlForm.setCreate_user_id(passport.getUserId());
			controlForm.setState(QualityControlFormState.NORMAL.getCode());
			mapper.insertQualityControlForm(controlForm);
			for (TQualityControlMeasure qualityControlMeasure : listQualityControlMeasure)
				this.addQualityControlMeasure(passport, controlForm.getId(), qualityControlMeasure, mapper);
			session.commit();
			return controlForm;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private void checkQualityControlFormObj(TQualityControlForm controlForm) throws QualityControlException {
		if (controlForm == null)
			throw new QualityControlException("请指定质控问卷对象！");
		if (StringTools.isEmpty(controlForm.getName()))
			throw new QualityControlException("请指定质控问卷的名称！");
		controlForm.setName(controlForm.getName().trim());
		if (controlForm.getName().length() >= 500)
			throw new QualityControlException("指定的质控问卷的名称太长了，最长500个字！");
		if (StringTools.isEmpty(controlForm.getNote()))
			throw new QualityControlException("质控问卷的详细说明必须指定！");
		controlForm.setNote(controlForm.getNote().trim());
		if (controlForm.getNote().length() >= 2000)
			throw new QualityControlException("指定的质控问卷的详细说明太长了，最长2000个字！");
		if (QualityControlEnforceable.parseCode(controlForm.getQuestion_enforceable()) == null)
			throw new QualityControlException("问卷的质控强制性必须指定！");
		if (QualityControlTarget.parseCode(controlForm.getTarget_type()) == null)
			throw new QualityControlException("问卷的质控类型必须指定！");
	}

	/**
	 * 删除质控表
	 * 
	 * @param passport
	 * @param controlFormId
	 * @throws QualityControlException
	 * @throws NotPermissionException
	 */
	public void deleteQualityControlFormById(Passport passport, long controlFormId)
			throws QualityControlException, NotPermissionException {
		if (passport.getUserType().getCode() != UserType.SUPER_ADMIN.getCode()
				&& !UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_QUALITY_CONTROL))
			throw new NotPermissionException(UserPermission.ADMIN_QUALITY_CONTROL);
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			TQualityControlForm oldQualityControlForm = mapper.selectQualityControlFormByIdForUpdate(controlFormId);
			if (oldQualityControlForm == null)
				throw new QualityControlException("要删除的质控问卷未找到！");
			mapper.deleteQualityControlFormById(controlFormId);
			mapper.deleteQualityControlMeasureByFormId(controlFormId);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 禁用/启用质控表
	 * 
	 * @param passport
	 * @param controlFormId
	 * @param disabled
	 * @return
	 * @throws NotPermissionException
	 * @throws QualityControlException
	 */
	public TQualityControlForm disabledQualityControlFormById(Passport passport, long controlFormId, boolean disabled)
			throws NotPermissionException, QualityControlException {
		if (passport.getUserType().getCode() != UserType.SUPER_ADMIN.getCode()
				&& !UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_QUALITY_CONTROL))
			throw new NotPermissionException(UserPermission.ADMIN_QUALITY_CONTROL);
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			TQualityControlForm oldQualityControlForm = mapper.selectQualityControlFormByIdForUpdate(controlFormId);
			if (oldQualityControlForm == null)
				throw new QualityControlException("要操作的质控问卷未找到！");
			oldQualityControlForm.setState(
					disabled ? QualityControlFormState.DISABLED.getCode() : QualityControlFormState.NORMAL.getCode());
			oldQualityControlForm.setModify_time(new Date());
			oldQualityControlForm.setModify_user_id(passport.getUserId());
			mapper.updateQualityControlForm(oldQualityControlForm);
			session.commit();
			return oldQualityControlForm;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 新增质控指标
	 * 
	 * @param qualityControlMeasure
	 * @param mapper
	 * @return
	 * @throws QualityControlException
	 */
	private TQualityControlMeasure addQualityControlMeasure(Passport passport, long form_id,
			TQualityControlMeasure qualityControlMeasure, QualityControlMapper mapper) throws QualityControlException {
		this.checkQualityControlMeasureObj(qualityControlMeasure);
		qualityControlMeasure.setCreate_time(new Date());
		qualityControlMeasure.setCreate_user_id(passport.getUserId());
		qualityControlMeasure.setForm_id(form_id);
		mapper.insertQualityControlMeasure(qualityControlMeasure);
		return qualityControlMeasure;

	}

	private void checkQualityControlMeasureObj(TQualityControlMeasure qualityControlMeasure)
			throws QualityControlException {
		if (qualityControlMeasure == null)
			throw new QualityControlException("请指定质控指标对象！");
		if (StringTools.isEmpty(qualityControlMeasure.getQuestion()))
			throw new QualityControlException("质控指标的问题必须指定！");
		qualityControlMeasure.setQuestion(qualityControlMeasure.getQuestion().trim());
		if (qualityControlMeasure.getQuestion().length() >= 1000)
			throw new QualityControlException("质控指标问题太长了，最长1000个字！");
		if (QualityControlMeasureQuestionType.parseCode(qualityControlMeasure.getQuestion_type()) == null)
			throw new QualityControlException("指标问题类型必须指定！");
	}

	/**
	 * 搜索质控表格
	 * 
	 * @param sqcfp
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private List<FQualityControlForm> searchQualityControlForm(SearchQualityControlFormParam sqcfp,
			QualityControlMapper mapper) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> mapArg = sqcfp.buildMap();
		if (sqcfp.getSpu() != null) {
			long count = mapper.selectQualityControlFormCount(mapArg);
			sqcfp.getSpu().setTotalRow(count);
			if (count <= 0)
				return null;
			mapArg.put("minRow", sqcfp.getSpu().getCurrMinRowNum());
			mapArg.put("maxRow", sqcfp.getSpu().getCurrMaxRowNum());
		}
		return mapper.selectQualityControlForm(mapArg);
	}

	/**
	 * 搜索质控表格
	 * 
	 * @param sqcfp
	 * @return
	 * @throws Exception
	 */
	public List<FQualityControlForm> searchQualityControlForm(SearchQualityControlFormParam sqcfp) throws Exception {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			return this.searchQualityControlForm(sqcfp, mapper);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 通过passport和质控目标类型和质控目标ID查询质控UID
	 * 
	 * @throws Exception
	 */
	public String queryQualityControlFormUid(Passport passport, Map<QualityControlTarget, Long> mapTargetEntry)
			throws Exception {
		if (CommonTools.isEmpty(mapTargetEntry))
			return null;
		Set<Long> listQualityControlFormAnswerQCUid = new HashSet<Long>();
		for (Iterator<Entry<QualityControlTarget, Long>> iterator = mapTargetEntry.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<QualityControlTarget, Long> type = (Entry<QualityControlTarget, Long>) iterator.next();
			listQualityControlFormAnswerQCUid = (Set<Long>) CollectionTools.merge(listQualityControlFormAnswerQCUid,
					this.queryListQualityControlFormAnswerQCUid(passport, type.getKey(), type.getValue()));
			// List<TQualityControlFormAnswer> listQualityControlFormAnswer;
			// try {
			// listQualityControlFormAnswer = QualityControlService.instance
			// .queryAndAddQualityControlFormAnswer(passport, type.getKey(),
			// type.getValue());
			// } catch (Exception e) {
			// e.printStackTrace();
			// continue;
			// }
			// if (CommonTools.isEmpty(listQualityControlFormAnswer))
			// continue;
			// for (TQualityControlFormAnswer qualityControlFormAnswer :
			// listQualityControlFormAnswer) {
			// if (qualityControlFormAnswer.getStatus() !=
			// QualityControlFormAnswerStatus.FINISH.getCode()) {
			// if
			// (!QualityControlService.instance.isIgnoreWithCancelIgnore(qualityControlFormAnswer.getId()))
			// listQualityControlFormAnswerQCUid.add(qualityControlFormAnswer.getId());
			// }
			// }
		}
		if (CollectionTools.isEmpty(listQualityControlFormAnswerQCUid))
			return null;
		String uid = CommonTools.getUUID();
		QualityControlService.instance.addQualityControlFormAnswerQCUid(uid, listQualityControlFormAnswerQCUid);
		return uid;
	}

	private Set<Long> queryListQualityControlFormAnswerQCUid(Passport passport, QualityControlTarget target_type,
			long target_id) throws Exception {
		Set<Long> listQualityControlFormAnswerQCUid = new HashSet<Long>();
		List<TQualityControlFormAnswer> listQualityControlFormAnswer;
		listQualityControlFormAnswer = this.queryAndAddQualityControlFormAnswer(passport, target_type, target_id);
		if (CollectionTools.isEmpty(listQualityControlFormAnswer))
			return null;
		for (TQualityControlFormAnswer qualityControlFormAnswer : listQualityControlFormAnswer) {
			if (qualityControlFormAnswer.getStatus() != QualityControlFormAnswerStatus.FINISH.getCode()) {
				if (!QualityControlService.instance.isIgnoreWithCancelIgnore(qualityControlFormAnswer.getId()))
					listQualityControlFormAnswerQCUid.add(qualityControlFormAnswer.getId());
			}
		}
		return listQualityControlFormAnswerQCUid;
	}

	/**
	 * 通过passport和质控目标类型和质控目标ID查询质控UID
	 * 
	 * @throws Exception
	 */
	public String queryQualityControlFormUid(Passport passport, QualityControlTarget target_type, long target_id)
			throws Exception {
		Set<Long> listQualityControlFormAnswerQCUid = this.queryListQualityControlFormAnswerQCUid(passport, target_type,
				target_id);
		if (CollectionTools.isEmpty(listQualityControlFormAnswerQCUid))
			return null;
		String uid = CommonTools.getUUID();
		QualityControlService.instance.addQualityControlFormAnswerQCUid(uid, listQualityControlFormAnswerQCUid);
		return uid;
	}

	/**
	 * 拉取质控指标填表
	 * 
	 * @param passport
	 * @param form_answer_id
	 * @param occasion
	 * @param mapTargetEntry
	 * @return
	 * @throws QualityControlException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public List<VQualityControlFormAnswer> takeQualityControlFormByQcUidOrFormAnswerId(Passport passport, String qcUid,
			Long form_answer_id) throws QualityControlException, IllegalArgumentException, IllegalAccessException {
		if (StringTools.isEmpty(qcUid) && (form_answer_id == null || form_answer_id <= 0))
			throw new QualityControlException("必须指定质控UID");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			Set<Long> listQcFormIds = mapListQualityControlFormAnswerQCUid.get(qcUid);
			List<VQualityControlFormAnswer> result = new ArrayList<VQualityControlFormAnswer>();
			if (!CollectionTools.isEmpty(listQcFormIds)) {
				for (Long faid : listQcFormIds) {
					VQualityControlFormAnswer formAnswer = this.takeQualityControlFormById(faid, mapper);
					if (formAnswer != null)
						result.add(formAnswer);
				}
			}
			if (form_answer_id != null && form_answer_id > 0) {
				VQualityControlFormAnswer formAnswer = this.takeQualityControlFormById(form_answer_id, mapper);
				if (formAnswer != null)
					result.add(formAnswer);
			}
			return result;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private VQualityControlFormAnswer takeQualityControlFormById(long form_answer_id, QualityControlMapper mapper)
			throws QualityControlException {
		TQualityControlFormAnswer formAnswer = mapper.selectQualityControlFormAnswerById(form_answer_id);
		if (formAnswer == null)
			throw new QualityControlException("指定的答卷ID未找到！");
		VQualityControlFormAnswer controlFormAnswer = new VQualityControlFormAnswer();
		controlFormAnswer.setQualityControlFormAnswer(formAnswer);
		SearchQualityControlMeasureParam sqcmp = new SearchQualityControlMeasureParam();
		sqcmp.setForm_id(formAnswer.getForm_id());
		List<TQualityControlMeasureAnswer> listQualityControlMeasureAnswer = mapper
				.selectQualityControlMeasureAnswerByFormId(formAnswer.getId());
		controlFormAnswer.setListMeasureAnswer(listQualityControlMeasureAnswer);
		return controlFormAnswer;
	}

	// public VQualityControlFormAnswer takeQualityControlFormById(Passport
	// takePassport, long form_answer_id)
	// throws QualityControlException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// QualityControlMapper mapper =
	// session.getMapper(QualityControlMapper.class);
	// return this.takeQualityControlFormById(form_answer_id, mapper);
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	/**
	 * 忽略一次质控
	 * 
	 * @throws QualityControlException
	 */
	public void ignoreFormsByQcUid(Passport passport, String qualityControlUid) throws QualityControlException {
		Set<Long> listIgnoreFormIdsTmp = mapListQualityControlFormAnswerQCUid.get(qualityControlUid);
		if (CollectionTools.isEmpty(listIgnoreFormIdsTmp))
			return;
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			for (Long formAnswerId : listIgnoreFormIdsTmp) {
				TQualityControlFormAnswer oldQcfa = mapper.selectQualityControlFormAnswerById(formAnswerId);
				if (oldQcfa == null)
					throw new QualityControlException("要忽略的答卷不存在！");
				if (oldQcfa.getStatus() == QualityControlFormAnswerStatus.FINISH.getCode())
					continue;
				if (oldQcfa.getForm_question_enforceable() == QualityControlEnforceable.FORCE.getCode())
					throw new QualityControlException("有强制性质控问卷必须回答，不能忽略！");
				listIgnoredFormAnswer.add(formAnswerId);
			}
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 保存质控指标表格
	 * 
	 * @param passport
	 * @param listFrom
	 * @throws QualityControlException
	 */
	public void commitQualityControlFormAnswer(Passport passport, List<VQualityControlFormAnswer> listFromAnswer)
			throws QualityControlException {
		if (CollectionTools.isEmpty(listFromAnswer))
			throw new QualityControlException("提交失败，没有需要提交的表格！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			for (VQualityControlFormAnswer vQualityControlFormAnswer : listFromAnswer) {
				if (vQualityControlFormAnswer.getQualityControlFormAnswer().getCreate_user_id() != passport.getUserId())
					throw new QualityControlException("提交的质控答卷不是您的答卷！");
				if (vQualityControlFormAnswer.getQualityControlFormAnswer().getCreate_org_id() != passport.getOrgId())
					throw new QualityControlException("提交的质控答卷不是您的答卷！");
				this.commitQualityControlFormAnswer(vQualityControlFormAnswer.getQualityControlFormAnswer(),
						vQualityControlFormAnswer.getListMeasureAnswer(), mapper);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 保存质控指标表格
	 * 
	 * @param passport
	 * @param listFrom
	 * @throws QualityControlException
	 */
	private void commitQualityControlFormAnswer(TQualityControlFormAnswer qualityControlFormAnswer,
			List<TQualityControlMeasureAnswer> listQualityControlMeasureAnswer, QualityControlMapper mapper)
			throws QualityControlException {
		TQualityControlFormAnswer oldQualityControlFormAnswer = mapper
				.selectQualityControlFormAnswerByIdForUpdate(qualityControlFormAnswer.getId());
		if (oldQualityControlFormAnswer == null)
			throw new QualityControlException("要提交的答卷未找到！");
		if (oldQualityControlFormAnswer.getStatus() != QualityControlFormAnswerStatus.WRITING.getCode())
			return;
		oldQualityControlFormAnswer.setStatus(QualityControlFormAnswerStatus.FINISH.getCode());
		mapper.updateQualityControlFormAnswer(oldQualityControlFormAnswer);
		if (CollectionTools.isEmpty(listQualityControlMeasureAnswer))
			throw new QualityControlException("答卷中没有包含要回答的问题！");
		List<TQualityControlMeasureAnswer> listQcmaTmp = mapper
				.selectQualityControlMeasureAnswerByFormId(oldQualityControlFormAnswer.getId());
		if (CollectionTools.isEmpty(listQcmaTmp))
			throw new QualityControlException("答卷中没有包含要回答的问题！");
		if (listQcmaTmp.size() != listQualityControlMeasureAnswer.size())
			throw new QualityControlException("提交的答案与要提交的答案数量不符！");
		for (TQualityControlMeasureAnswer qualityControlMeasureAnswer : listQualityControlMeasureAnswer) {
			TQualityControlMeasureAnswer oldQcma = TQualityControlMeasureAnswer.findByIdFromList(listQcmaTmp,
					qualityControlMeasureAnswer.getId());
			if (oldQcma == null)
				throw new QualityControlException("要回答的问题不存在！");
			if (qualityControlMeasureAnswer.getQuestion_type() == QualityControlMeasureQuestionType.SCORING.getCode()) {
				if (qualityControlMeasureAnswer.getPercent() <= 0 || qualityControlMeasureAnswer.getPercent() > 100)
					throw new QualityControlException("打分的题目分数必须在0-100之间，包含100！");
			} else if (qualityControlMeasureAnswer.getQuestion_type() == QualityControlMeasureQuestionType.TEXT
					.getCode()) {
				if (StringTools.isEmpty(qualityControlMeasureAnswer.getAnswer()))
					throw new QualityControlException("文本回答的题目必须填写文本！");
				qualityControlMeasureAnswer.setAnswer(qualityControlMeasureAnswer.getAnswer().trim());
				if (qualityControlMeasureAnswer.getAnswer().length() >= 2000)
					throw new QualityControlException("您的回答太长了，最长2000个字！");
			} else {
				throw new QualityControlException("不支持的问题类型！");
			}
			oldQcma.setAnswer(qualityControlMeasureAnswer.getAnswer());
			oldQcma.setPercent(qualityControlMeasureAnswer.getPercent());
			mapper.updateQualityControlMeasureAnswer(oldQcma);
		}
	}

	public FQualityControlForm queryQualityControlFormById(long id) throws Exception {
		SearchQualityControlFormParam sqcfp = new SearchQualityControlFormParam();
		sqcfp.setId(id);
		List<FQualityControlForm> listQualityControlForm = this.searchQualityControlForm(sqcfp);
		return CollectionTools.isEmpty(listQualityControlForm) ? null : listQualityControlForm.get(0);
	}

	public List<TQualityControlMeasure> searchQualityControlMeasure(SearchQualityControlMeasureParam sqcmp)
			throws Exception {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return this.searchQualityControlMeasure(sqcmp, session.getMapper(QualityControlMapper.class));
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public TQualityControlForm modifyQualityControlForm(Passport passport, TQualityControlForm qualityControlForm,
			List<TQualityControlMeasure> listQualityControlMeasure) throws Exception {
		if (passport.getUserType().getCode() != UserType.SUPER_ADMIN.getCode()
				&& !UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_QUALITY_CONTROL))
			throw new NotPermissionException(UserPermission.ADMIN_QUALITY_CONTROL);
		this.checkQualityControlFormObj(qualityControlForm);
		if (CollectionTools.isEmpty(listQualityControlMeasure))
			throw new QualityControlException("质控问卷的质控指标必须至少有一个！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			TQualityControlForm oldQualityControlForm = mapper
					.selectQualityControlFormByIdForUpdate(qualityControlForm.getId());
			if (oldQualityControlForm == null)
				throw new QualityControlException("要修改的质控问卷未找到！");
			if (!qualityControlForm.equals(oldQualityControlForm)) {
				if (!oldQualityControlForm.getName().equals(qualityControlForm.getName())) {
					SearchQualityControlFormParam sqcfp = new SearchQualityControlFormParam();
					sqcfp.setName(qualityControlForm.getName());
					List<FQualityControlForm> listOldQualityControlForm = this.searchQualityControlForm(sqcfp, mapper);
					if (!CollectionTools.isEmpty(listOldQualityControlForm))
						throw new QualityControlException("质控问卷重名，请重新命名！");
				}
				oldQualityControlForm.setModify_time(new Date());
				oldQualityControlForm.setModify_user_id(passport.getUserId());
				oldQualityControlForm.setName(qualityControlForm.getName());
				oldQualityControlForm.setNote(qualityControlForm.getNote());
				oldQualityControlForm.setQuestion_enforceable(qualityControlForm.getQuestion_enforceable());
				oldQualityControlForm.setTarget_type(qualityControlForm.getTarget_type());
				mapper.updateQualityControlForm(oldQualityControlForm);
				qualityControlForm = oldQualityControlForm;
			}
			mapper.deleteQualityControlMeasureByFormId(qualityControlForm.getId());
			for (TQualityControlMeasure qualityControlMeasure : listQualityControlMeasure)
				this.addQualityControlMeasure(passport, qualityControlForm.getId(), qualityControlMeasure, mapper);
			session.commit();
			return qualityControlForm;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TQualityControlFormAnswer> searchQualityControlFormAnswer(SearchQualityControlFormAnswerParam sqcfap)
			throws Exception {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			return this.searchQualityControlFormAnswer(sqcfap, mapper);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private List<TQualityControlFormAnswer> searchQualityControlFormAnswer(SearchQualityControlFormAnswerParam sqcfap,
			QualityControlMapper mapper) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> mapArg = sqcfap.buildMap();
		if (sqcfap.getSpu() != null) {
			long count = mapper.selectQualityControlFormAnswerCount(mapArg);
			sqcfap.getSpu().setTotalRow(count);
			if (count <= 0)
				return null;
			mapArg.put("minRow", sqcfap.getSpu().getCurrMinRowNum());
			mapArg.put("maxRow", sqcfap.getSpu().getCurrMaxRowNum());
		}
		return mapper.selectQualityControlFormAnswer(mapArg);
	}

	/**
	 * 查询和添加答卷
	 * 
	 * @param passport
	 * @param target_type
	 * @param target_id
	 * @return
	 * @throws Exception
	 */
	public List<TQualityControlFormAnswer> queryAndAddQualityControlFormAnswer(Passport passport,
			QualityControlTarget target_type, long target_id) throws Exception {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			QualityControlMapper mapper = session.getMapper(QualityControlMapper.class);
			SearchQualityControlFormParam sqcfp = new SearchQualityControlFormParam();
			sqcfp.setTarget_type(target_type);
			List<FQualityControlForm> listQualityControlForm = this.searchQualityControlForm(sqcfp, mapper);
			if (CollectionTools.isEmpty(listQualityControlForm))
				return null;
			List<TQualityControlFormAnswer> result = new ArrayList<TQualityControlFormAnswer>();
			for (FQualityControlForm qualityControlForm : listQualityControlForm) {
				SearchQualityControlFormAnswerParam sqcfap = new SearchQualityControlFormAnswerParam();
				sqcfap.setCreate_org_id(passport.getOrgId());
				sqcfap.setCreate_user_id(passport.getUserId());
				sqcfap.setTarget_type(target_type);
				sqcfap.setTarget_id(target_id);
				sqcfap.setForm_id(qualityControlForm.getId());
				List<TQualityControlFormAnswer> listOldQualityControlFormAnswer = this
						.searchQualityControlFormAnswer(sqcfap, mapper);
				if (!CollectionTools.isEmpty(listOldQualityControlFormAnswer)) {
					result.addAll(listOldQualityControlFormAnswer);
					continue;
				}
				TQualityControlFormAnswer qualityControlFormAnswer = new TQualityControlFormAnswer();
				qualityControlFormAnswer.setCreate_org_id(passport.getOrgId());
				qualityControlFormAnswer.setCreate_time(new Date());
				qualityControlFormAnswer.setCreate_user_id(passport.getUserId());
				qualityControlFormAnswer.setForm_id(qualityControlForm.getId());
				qualityControlFormAnswer.setForm_name(qualityControlForm.getName());
				qualityControlFormAnswer.setForm_note(qualityControlForm.getNote());
				qualityControlFormAnswer.setForm_question_enforceable(qualityControlForm.getQuestion_enforceable());
				qualityControlFormAnswer.setStatus(QualityControlFormAnswerStatus.WRITING.getCode());
				qualityControlFormAnswer.setTarget_id(target_id);
				qualityControlFormAnswer.setTarget_type(target_type.getCode());
				this.addQualityControlFormAnswer(qualityControlFormAnswer, mapper);
				result.add(qualityControlFormAnswer);
			}
			session.commit();
			return result;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private void addQualityControlFormAnswer(TQualityControlFormAnswer qualityControlFormAnswer,
			QualityControlMapper mapper)
			throws QualityControlException, IllegalArgumentException, IllegalAccessException {
		this.checkQualityControlFormAnswerObj(qualityControlFormAnswer);
		mapper.insertQualityControlFormAnswer(qualityControlFormAnswer);
		SearchQualityControlMeasureParam sqcmp = new SearchQualityControlMeasureParam();
		sqcmp.setForm_id(qualityControlFormAnswer.getForm_id());
		List<TQualityControlMeasure> listQualityControlMeasure = this.searchQualityControlMeasure(sqcmp, mapper);
		if (CollectionTools.isEmpty(listQualityControlMeasure))
			throw new QualityControlException("指定的问卷没有指标！");
		for (TQualityControlMeasure qualityControlMeasure : listQualityControlMeasure) {
			TQualityControlMeasureAnswer qualityControlMeasureAnswer = new TQualityControlMeasureAnswer(
					qualityControlFormAnswer.getId(), qualityControlMeasure.getId());
			qualityControlMeasureAnswer.setQuestion(qualityControlMeasure.getQuestion());
			qualityControlMeasureAnswer.setQuestion_type(qualityControlMeasure.getQuestion_type());
			mapper.insertQualityControlMeasureAnswer(qualityControlMeasureAnswer);
		}
	}

	private void checkQualityControlFormAnswerObj(TQualityControlFormAnswer qualityControlFormAnswer)
			throws QualityControlException {
		if (qualityControlFormAnswer == null)
			throw new QualityControlException("请指定答卷对象！");
		if (qualityControlFormAnswer.getCreate_org_id() <= 0)
			throw new QualityControlException("答卷创建人所在机构必须指定！");
		if (qualityControlFormAnswer.getCreate_user_id() <= 0)
			throw new QualityControlException("答卷创建人必须指定！");
		if (qualityControlFormAnswer.getCreate_time() == null)
			qualityControlFormAnswer.setCreate_time(new Date());
		if (qualityControlFormAnswer.getForm_id() <= 0)
			throw new QualityControlException("答卷对应的问卷必须指定！");
		if (StringTools.isEmpty(qualityControlFormAnswer.getForm_name()))
			throw new QualityControlException("答卷对应的问卷名称必须指定！");
		if (StringTools.isEmpty(qualityControlFormAnswer.getForm_note()))
			throw new QualityControlException("答卷对应的问卷的详细说明必须指定！");
		if (QualityControlEnforceable.parseCode(qualityControlFormAnswer.getForm_question_enforceable()) == null)
			throw new QualityControlException("答卷的强制性必须指定！");
		if (QualityControlFormAnswerStatus.parseCode(qualityControlFormAnswer.getStatus()) == null)
			throw new QualityControlException("答卷的状态无效！");
		if (qualityControlFormAnswer.getTarget_id() <= 0)
			throw new QualityControlException("答卷的目标对象必须指定！");
		if (QualityControlTarget.parseCode(qualityControlFormAnswer.getTarget_type()) == null)
			throw new QualityControlException("答卷的目标类型必须指定！");
	}

	public boolean isIgnore(long qualityControlFormAnswerId) {
		return listIgnoredFormAnswer.contains(qualityControlFormAnswerId);
	}

	/**
	 * 判断此答卷是否已忽略一次<br>
	 * 执行之后会解除指定回复表格的忽略状态
	 * 
	 * @param qualityControlFormAnswerId
	 * @return
	 */
	public boolean isIgnoreWithCancelIgnore(long qualityControlFormAnswerId) {
		boolean result = listIgnoredFormAnswer.contains(qualityControlFormAnswerId);
		if (result)
			listIgnoredFormAnswer.remove(qualityControlFormAnswerId);
		return result;
	}
}
