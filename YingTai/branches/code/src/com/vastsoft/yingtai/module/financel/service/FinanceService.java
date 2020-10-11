package com.vastsoft.yingtai.module.financel.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.DateTools;
import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.services.RequestTranferService;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.financel.constants.AccountRecordRandEType;
import com.vastsoft.yingtai.module.financel.constants.AccountRecordType;
import com.vastsoft.yingtai.module.financel.constants.AccountStatus;
import com.vastsoft.yingtai.module.financel.constants.FinacelRequestStatus;
import com.vastsoft.yingtai.module.financel.constants.FinacelRequestType;
import com.vastsoft.yingtai.module.financel.constants.FreezeStatus;
import com.vastsoft.yingtai.module.financel.entity.TFinanceAccount;
import com.vastsoft.yingtai.module.financel.entity.TFinanceFreeze;
import com.vastsoft.yingtai.module.financel.entity.TFinanceRecord;
import com.vastsoft.yingtai.module.financel.entity.TFinanceRequest;
import com.vastsoft.yingtai.module.financel.exception.FinanceException;
import com.vastsoft.yingtai.module.financel.mapper.FinanceMapper;
import com.vastsoft.yingtai.module.stat.entity.FFinanceRecord;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class FinanceService {
	public static final FinanceService instance = new FinanceService();
	/** 系统账户的机构ID，固定 */
	public static final long SYS_ACCOUNT_ORG_ID = 0;
	private FinanceTrade FT = new FinacelTradeImpl();

	private FinanceService() {
	}

	public FinanceTrade getFinanceTrade() throws FinanceException {
		if (!CommonTools.judgeCaller(DiagnosisService2.class,RequestTranferService.class)) {
			throw new FinanceException("调用类无权调用此方法");
		}
		return this.FT;
	}

	public class FinacelTradeImpl implements FinanceTrade {
		private FinacelTradeImpl() {
		}

		@Override
		public void cashIn(Passport passport, TDiagnosis diagnosis, SqlSession session) throws BaseException {
			try {
				FinanceMapper mapper = session.getMapper(FinanceMapper.class);
				if (mapper == null)
					throw new DbException();
				TFinanceFreeze freeze = mapper.queryFreezeByDiagnosisIdForUpdate(diagnosis.getId());
				if (freeze == null)
					throw new FinanceException("没有指定ID的冻结记录");
				if (freeze.getStatus() != FreezeStatus.VALID.getCode())
					throw new FinanceException("冻结记录已经处理，不能重复划账！");
				TFinanceAccount account = mapper.queryAccountByOrgIdForUpdate(diagnosis.getRequest_org_id());
				TFinanceAccount sysAccount = mapper.queryAccountByOrgIdForUpdate(SYS_ACCOUNT_ORG_ID);
				if (sysAccount == null)
					throw new FinanceException("系统账户未找到！");
				// 修改目标账户金额
				double srPrice = freeze.getPrice() - freeze.getSys_deduct();
				if (srPrice < 0)
					throw new FinanceException("涉及的诊断资金不足以支付系统提成！");
				account.setAmount(account.getAmount() + srPrice);
				mapper.modifyAccount(account);
				// 记录
				FinanceService.instance.addAccountRecord(passport.getUserId(), account.getId(),
						diagnosis.getRequest_org_id(), diagnosis.getDiagnosis_org_id(), srPrice,
						AccountRecordType.DIAGNOSIS_CASH_ID, session);
				// 修改系统账户金额
				sysAccount.setAmount(sysAccount.getAmount() + freeze.getSys_deduct());
				mapper.modifyAccount(sysAccount);
				// 记录
				FinanceService.instance.addAccountRecord(0, sysAccount.getId(), diagnosis.getRequest_org_id(),
						diagnosis.getDiagnosis_org_id(), freeze.getSys_deduct(), AccountRecordType.SYSTEM_CASH_ID,
						session);
				// 修改冻结记录
				freeze.setStatus(FreezeStatus.INVALID.getCode());
				mapper.modifyFreezeStatus(freeze);
			} catch (Exception e) {
				throw e;
			}
		}

		@Override
		public void freezePrice(Passport passport, TDiagnosis diagnosis, double dfPrice, SqlSession session)
				throws BaseException {
			try {
				TFinanceAccount account;
				FinanceMapper mapper = session.getMapper(FinanceMapper.class);
				account = mapper.queryAccountByOrgIdForUpdate(passport.getOrgId());

				TFinanceFreeze freeze = mapper.queryFreezeByDiagnosisId(diagnosis.getId());
				if (freeze != null)
					throw new FinanceException("该诊断的冻结记录已经存在！");
				// 修改目标账户金额
				double amount = account.getAmount() - dfPrice;
				if (amount < 0)
					throw new FinanceException("你的账户余额不足，提交失败！");
				double sysSplit = SysService.instance.takeSysDeductQuota();
				if (dfPrice < sysSplit)
					throw new FinanceException("涉及的资金不足以支付系统提成！");
				account.setAmount(amount);
				mapper.modifyAccount(account);

				// 修改冻结记录状态
				freeze = new TFinanceFreeze();
				freeze.setAccount_id(account.getId());
				freeze.setCreate_time(new Date());
				freeze.setPrice(dfPrice);
				freeze.setDiagnosis_id(diagnosis.getId());
				freeze.setStatus(FreezeStatus.VALID.getCode());
				freeze.setSys_deduct(sysSplit);
				mapper.addFreeze(freeze);
				FinanceService.instance.addAccountRecord(passport.getUserId(), account.getId(),
						diagnosis.getRequest_org_id(), diagnosis.getDiagnosis_org_id(), dfPrice,
						AccountRecordType.DIAGNOSIS_FREEZE, session);
			} catch (Exception e) {
				throw e;
			}
		}

		// @Override
		// public TFinanceAccount createAccount(Passport passport, long lOrgId,
		// SqlSession session) throws BaseException {
		// try {
		// TFinanceAccount account;
		// FinanceMapper mapper = session.getMapper(FinanceMapper.class);
		//
		// account=mapper.queryAccountByOrgId(lOrgId);
		// if (account!=null)throw new FinanceException("该机构的账户信息已经存在！");
		// account = new TFinanceAccount();
		// account.setAmount(0.0);
		// account.setCreate_Time(new Date());
		// account.setDescription("机构账户");
		// account.setNote("");
		// account.setOrg_id(lOrgId);
		// account.setStatus(AccountStatus.NORMAL.getCode());
		// mapper.addAccount(account);
		// FinanceService.instance.addAccountRecord(passport, account.getId(),
		// 0, AccountRecordType.CREATE_ORG_ACCOUNT,
		// session);
		// return account;
		// } catch (Exception e) {
		// throw e;
		// }
		// }

		@Override
		public void unFreezePrice(Passport passport, TDiagnosis diagnosis, SqlSession session) throws BaseException {
			try {
				FinanceMapper mapper = session.getMapper(FinanceMapper.class);
				TFinanceAccount account = mapper.queryAccountByOrgIdForUpdate(diagnosis.getRequest_org_id());
				if (account == null)
					throw new FinanceException("没有查找到账户记录！");
				TFinanceFreeze freeze = mapper.queryFreezeByDiagnosisIdForUpdate(diagnosis.getId());
				if (freeze == null)
					return;
				if (freeze.getStatus() != FreezeStatus.VALID.getCode())
					throw new FinanceException("冻结记录的状态为无效！");
				account.setAmount(account.getAmount() + freeze.getPrice());
				mapper.modifyAccount(account);
				freeze.setStatus(FreezeStatus.INVALID.getCode());
				mapper.modifyFreezeStatus(freeze);
				// 记录
				FinanceService.instance.addAccountRecord(0, account.getId(),
						diagnosis.getRequest_org_id(), diagnosis.getDiagnosis_org_id(), freeze.getPrice(),
						AccountRecordType.UNFREEZE_DIAGNOSIS, session);
			} catch (Exception e) {
				throw e;
			}
		}
	}

	/**
	 * 冻结或解冻账户
	 * 
	 * @param passport
	 *            身份认证
	 * @param lOrgId
	 *            要冻结账户机构ID
	 * @param accountStatus
	 *            账户状态，冻结还是解冻
	 * @throws BaseException
	 */
	public void freezeAccount(Passport passport, long lOrgId, AccountStatus accountStatus) throws BaseException {
		SqlSession session = null;
		try {
			if (passport == null || accountStatus == null)
				throw new NullParameterException("passport&accountStatus");
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_FINALCAL_MGR))
				throw new NotPermissionException();
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);

			TFinanceAccount account = mapper.queryAccountByOrgIdForUpdate(lOrgId);
			if (accountStatus.getCode() == account.getStatus())
				throw new FinanceException("该机构账户当前已经处于此状态，无需再执行此操作！");

			account.setStatus(accountStatus.getCode());
			mapper.modifyAccount(account);

			if (accountStatus.equals(AccountStatus.FREEZE)) {
				this.addAccountRecord(passport.getUserId(), account.getId(), 0, 0, 0, AccountRecordType.FREEZE_ORG_ACCOUNT,
						session);
			} else {
				this.addAccountRecord(passport.getUserId(), account.getId(), 0, 0, 0, AccountRecordType.UNFREEZE_ORG_ACCOUNT,
						session);
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
	 * 查询机构的账户信息
	 * 
	 * @param passport
	 *            身份认证
	 * @param lOrgId
	 *            要查询的机构ID
	 * @return 返回账户余额
	 * @throws BaseException
	 */
	public TFinanceAccount queryWithAddAccountByOrgid(Passport passport, long org_id) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			boolean isAdmin = UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_FINALCAL_MGR);
			org_id = isAdmin ? org_id : passport.getOrgId();
			FinanceMapper mappper = session.getMapper(FinanceMapper.class);
			TFinanceAccount account = mappper.queryAccountByOrgId(org_id);
			if (account != null)
				return account;
			account=new TFinanceAccount();
			account.setAmount(0);
			account.setCreate_Time(new Date());
			account.setDescription("");
			account.setNote("");
			account.setOrg_id(org_id);
			account.setStatus(AccountStatus.NORMAL.getCode());
			mappper.addAccount(account);
			session.commit();
//				throw new FinanceException("没有查询到对应机构ID的账户记录！");
			return account;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 充值，系统管理员调用
	 * 
	 * @throws BaseException
	 * 
	 * @throws Exception
	 */
	public void recharge(Passport passport, long lFinacalRequestId) throws BaseException {
		SqlSession session = null;
		try {
			// 判断权限
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_FINALCAL_MGR)) {
				throw new NotPermissionException();
			}
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			TFinanceRequest finacalRequest = mapper.queryFinacalRequestByIdForUpdate(lFinacalRequestId);
			if (finacalRequest == null)
				throw new FinanceException("没有找到对应的账户操作申请");
			if (finacalRequest.getOperation_type() != FinacelRequestType.CHARGE.getCode())
				throw new FinanceException("此条申请不是充值申请");
			if (finacalRequest.getStatus() != FinacelRequestStatus.UNHANDLE.getCode()) {
				throw new FinanceException("此条申请已经处理，不能重复处理");
			}
			TFinanceAccount account = mapper.queryAccountByOrgIdForUpdate(finacalRequest.getOrg_id());
			if (account == null)
				throw new FinanceException("通过机构ID没有查询到账户");
			if (account.getStatus() != AccountStatus.NORMAL.getCode())
				throw new FinanceException("机构账户处于被冻结状态，不能执行此操作");
			account.setAmount(account.getAmount() + finacalRequest.getPrice());
			// 修改账户余额
			mapper.modifyAccount(account);
			// 记录
			this.addAccountRecord(finacalRequest.getRequsetor_id(), account.getId(), 0, 0, finacalRequest.getPrice(),
					AccountRecordType.CHARGE, session);

			finacalRequest.setStatus(FinacelRequestStatus.HANDLED.getCode());
			finacalRequest.setConfirm_id(passport.getUserId());
			finacalRequest.setConfirm_time(new Date());
			// 修改申请状态
			mapper.modifyFinacalRequestStatus(finacalRequest);

			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 提现，系统管理员调用
	 * 
	 * @param passport
	 *            身份认证
	 * @param lFinacalRequestId
	 *            账户处理申请ID
	 * @throws BaseException
	 * @throws Exception
	 */
	public void takeCash(Passport passport, long lFinacalRequestId) throws BaseException {
		SqlSession session = null;
		try {
			// 判断权限
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_FINALCAL_MGR)) {
				throw new NotPermissionException();
			}
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			TFinanceRequest finacalRequest = mapper.queryFinacalRequestByIdForUpdate(lFinacalRequestId);
			if (finacalRequest == null)
				throw new FinanceException("没有找到对应的账户操作申请");
			if (finacalRequest.getOperation_type() != FinacelRequestType.TAKECASH.getCode())
				throw new FinanceException("此条申请不是体现申请");
			if (finacalRequest.getStatus() != FinacelRequestStatus.UNHANDLE.getCode()) {
				throw new FinanceException("此条申请已经处理，不能重复处理");
			}
			TFinanceAccount account = mapper.queryAccountByOrgIdForUpdate(finacalRequest.getOrg_id());
			if (account == null)
				throw new FinanceException("通过机构ID没有查询到账户");
			if (account.getStatus() != AccountStatus.NORMAL.getCode())
				throw new FinanceException("机构账户处于被冻结状态，不能执行此操作");
			double dfPrice = account.getAmount() - finacalRequest.getPrice();
			if (dfPrice < 0)
				throw new FinanceException("机构账户中余额不足，不能执行本操作");
			account.setAmount(dfPrice);
			mapper.modifyAccount(account);
			finacalRequest.setStatus(FinacelRequestStatus.HANDLED.getCode());
			finacalRequest.setConfirm_id(passport.getUserId());
			finacalRequest.setConfirm_time(new Date());
			mapper.modifyFinacalRequestStatus(finacalRequest);

			this.addAccountRecord(finacalRequest.getRequsetor_id(), account.getId(), 0, 0, finacalRequest.getPrice(),
					AccountRecordType.CASH_OUT, session);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 拒绝申请，系统管理员调用
	 * 
	 * @throws BaseException
	 */
	public void rejectFinacelRequest(Passport passport, long lFinacalRequestId) throws BaseException {
		SqlSession session = null;
		try {
			// 判断权限
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_FINALCAL_MGR)) {
				throw new NotPermissionException();
			}
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			TFinanceRequest finacalRequest = mapper.queryFinacalRequestByIdForUpdate(lFinacalRequestId);
			if (finacalRequest.getStatus() != FinacelRequestStatus.UNHANDLE.getCode())
				throw new FinanceException("此条申请已经处理，请勿重复处理");
			finacalRequest.setStatus(FinacelRequestStatus.REJECT.getCode());
			finacalRequest.setConfirm_id(passport.getUserId());
			finacalRequest.setConfirm_time(new Date());
			mapper.modifyFinacalRequestStatus(finacalRequest);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 添加账户记录，内部调用
	 * 
	 * @param passport
	 *            身份认证
	 * @param lAccountId
	 *            账户ID
	 * @param dfPrice
	 *            金额
	 * @param art
	 *            账户记录类型
	 * @throws BaseException
	 * @throws NullParameterException
	 * @throws DbException
	 */
	private void addAccountRecord(long lOperatorId, long lAccountId, long lRequestOrgId, long lDiagnosisOrgId,
			double dfPrice, AccountRecordType art, SqlSession session) throws BaseException {
		try {
			TFinanceRecord record = new TFinanceRecord();
			record.setAccount_id(lAccountId);
			record.setPrice(dfPrice);
			record.setOperat_type(art.getCode());
			record.setDescription(art.getName() + " 金额￥" + dfPrice + "元");
			record.setNote("");
			record.setOperator_id(lOperatorId);
			record.setOperat_time(new Date());
			record.setRe_type(art.getReType().getCode());
			record.setRequest_org_id(lRequestOrgId);
			record.setDiagnosis_org_id(lDiagnosisOrgId);
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			mapper.addAccountRecord(record);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除申请，机构管理员调用
	 * 
	 * @throws BaseException
	 */
	public void deleteFinacelRequest(Passport passport, long lRequestId) throws BaseException {
		SqlSession session = null;
		try {
			// 判断权限
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR)) {
				throw new NotPermissionException();
			}
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			// 获取
			TFinanceAccount account = mapper.queryAccountByOrgId(passport.getOrgId());
			if (account == null)
				throw new FinanceException("未知的账户");

			if (!AccountStatus.NORMAL.equals(AccountStatus.parseCode(account.getStatus())))
				throw new FinanceException("无效的账户");

			TFinanceRequest request = mapper.queryFinacalRequestByIdForUpdate(lRequestId);
			if (request == null)
				throw new FinanceException("没有找到对应的机构账户操作申请记录");
			if (request.getOrg_id() != passport.getOrgId())
				throw new FinanceException("该申请不是你当前所在机构的申请，不能执行此操作");
			if (request.getStatus() != FinacelRequestStatus.UNHANDLE.getCode())
				throw new FinanceException("该申请记录已经处理，不允许删除");
			// 删除
			mapper.removeFinanceRequestById(lRequestId);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 提交提现或充值申请
	 * 
	 * @param passport
	 *            身份认证
	 * @param dfPrice
	 *            金额
	 * @param finacelRequestType
	 *            申请类型
	 * @throws BaseException
	 */
	public void createFinacelRequest(Passport passport, double dfPrice, FinacelRequestType finacelRequestType)
			throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();

			// 判断权限
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
				throw new NotPermissionException();

			TFinanceAccount account = this.queryWithAddAccountByOrgid(passport, passport.getOrgId());
			if (account == null)
				throw new FinanceException("通过机构ID没有查询到账户");
			if (account.getStatus() != AccountStatus.NORMAL.getCode())
				throw new FinanceException("机构账户处于被冻结状态，不能执行此操作");

			if (finacelRequestType.equals(FinacelRequestType.TAKECASH)) {
				double dfAmount = account.getAmount();
				if (dfAmount < dfPrice)
					throw new FinanceException("你机构的账户余额不足，不能执行此操作");
			}

			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			TFinanceRequest request = new TFinanceRequest();
			request.setCreate_time(new Date());
			request.setRequsetor_id(passport.getUserId());
			request.setNote(finacelRequestType.getName());
			request.setOperation_type(finacelRequestType.getCode());
			request.setOrg_id(passport.getOrgId());
			request.setPrice(dfPrice);
			request.setStatus(FinacelRequestStatus.UNHANDLE.getCode());
			mapper.addFinanceRequest(request);

			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取申请总条数
	 * 
	 * @throws BaseException
	 */
	private int searchRequestCount(Map<String, Object> mapArg) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			return mapper.searchRequestCount(mapArg);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询申请，后台管理员使用
	 * 
	 * @param passport
	 *            身份认证
	 * @param lOrgId
	 *            机构ID
	 * @param status
	 *            状态
	 * @param dtStart
	 *            开始时间
	 * @param dtEnd
	 *            结束时间
	 * @param type
	 *            查询的类型
	 * @param spu
	 *            分页模型
	 * @return 查询得到的结果
	 * @throws BaseException
	 */
	public List<TFinanceRequest> searchRequestList(Passport passport, Long lOrgId, String strOrgName, String strOrgCode,
			FinacelRequestStatus status, Date dtStart, Date dtEnd, FinacelRequestType type, SplitPageUtil spu)
					throws BaseException {
		SqlSession session = null;
		try {
			// 判断权限
			boolean isAdmin = UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_FINALCAL_MGR);

			lOrgId = isAdmin ? lOrgId : passport.getOrgId();

			// 查询
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("org_id", lOrgId);
			mapArg.put("org_name", strOrgName == null || strOrgName.isEmpty() ? null : strOrgName);
			mapArg.put("org_code", strOrgCode == null || strOrgCode.isEmpty() ? null : strOrgCode);
			mapArg.put("status", status == null ? null : status.getCode());
			mapArg.put("start", dtStart == null ? null : new Timestamp(dtStart.getTime()));
			mapArg.put("end", dtEnd == null ? null : new Timestamp(dtEnd.getTime()));
			mapArg.put("type", type == null ? null : type.getCode());
			int iCount = searchRequestCount(mapArg);
			spu.setTotalRow(iCount);
			if (iCount <= 0)
				return null;
			mapArg.put("minRow", spu.getCurrMinRowNum());
			mapArg.put("maxRow", spu.getCurrMaxRowNum());
			List<TFinanceRequest> listRequest = mapper.searchRequestList(mapArg);
			return listRequest;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询账户记录总条数 ，后台管理员使用
	 * 
	 * @param mapArg
	 * @return
	 * @throws BaseException
	 */
	private int searchAccountRecordCount(Map<String, Object> mapArg) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			return mapper.searchAccountRecordCount(mapArg);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询账户记录 ，后台管理员使用
	 * 
	 * @param passport
	 * @param lOrgId
	 * @param art
	 * @param dtStart
	 * @param dtEnd
	 * @param spu
	 * @return
	 * @throws BaseException
	 */
	public List<FFinanceRecord> searchAccountRecordList(Passport passport,Long lRequestOrgId,Long lDiagnosisOrgId, Long lOrgId,
			List<AccountRecordRandEType> listReType, AccountRecordType art, Date dtStart, Date dtEnd, SplitPageUtil spu)
					throws BaseException {
		SqlSession session = null;
		try {
			// 判断权限
			boolean isAdmin = UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_FINALCAL_MGR);
			lOrgId = isAdmin ? lOrgId : passport.getOrgId();
			// 查询
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			TFinanceAccount account = mapper.queryAccountByOrgId(lOrgId);
			if (account == null)
				throw new FinanceException("没有查询到指定机构的账户！");
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("account_id", account.getId());
			mapArg.put("request_org_id", lRequestOrgId);
			mapArg.put("diagnosis_org_id", lDiagnosisOrgId);
			mapArg.put("type", art == null ? null : art.getCode());
			mapArg.put("re_type", AccountRecordRandEType.typesToString(listReType));
			mapArg.put("start", dtStart == null ? null : new Timestamp(dtStart.getTime()));
			mapArg.put("end", dtEnd == null ? null : new Timestamp(dtEnd.getTime()));
			int iCount = searchAccountRecordCount(mapArg);
			if (iCount <= 0)
				return null;
			spu.setTotalRow(iCount);
			mapArg.put("minRow", spu.getCurrMinRowNum());
			mapArg.put("maxRow", spu.getCurrMaxRowNum());
			List<FFinanceRecord> listRecord = mapper.searchAccountRecordList(mapArg);
			return listRecord;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}

	}

	/**
	 * 查询账户的条数
	 * 
	 * @throws BaseException
	 */
	private int searchAccountCount(Map<String, Object> mapArg) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			return mapper.searchAccountCount(mapArg);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询所有账户，后台管理员使用
	 * 
	 * @throws BaseException
	 */
	public List<TFinanceAccount> searchAccountList(Passport passport, String strOrgName, String strOrgCode,
			AccountStatus accountStatus, Date dtStart, Date dtEnd, SplitPageUtil spu) throws BaseException {
		SqlSession session = null;
		try {
			// 判断权限
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_FINALCAL_MGR))
				throw new NotPermissionException();
			// 查询
			session = SessionFactory.getSession();
			FinanceMapper mapper = session.getMapper(FinanceMapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("status", accountStatus == null ? null : accountStatus.getCode());
			mapArg.put("start", dtStart == null ? null : DateTools.dateToString(dtStart));
			mapArg.put("end", dtEnd == null ? null : DateTools.dateToString(dtEnd));
			mapArg.put("org_name", strOrgName == null || strOrgName.isEmpty() ? null : strOrgName);
			mapArg.put("org_code", strOrgCode == null || strOrgCode.isEmpty() ? null : strOrgCode);
			int iCount = searchAccountCount(mapArg);
			spu.setTotalRow(iCount);
			if (iCount <= 0)
				return null;
			mapArg.put("minRow", spu.getCurrMinRowNum());
			mapArg.put("maxRow", spu.getCurrMaxRowNum());
			List<TFinanceAccount> listRecord = mapper.searchAccountList(mapArg);
			return listRecord;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}
}
