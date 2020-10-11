package com.vastsoft.yingtai.module.financel.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.module.financel.constants.AccountRecordType;
import com.vastsoft.yingtai.module.financel.constants.AccountStatus;
import com.vastsoft.yingtai.module.financel.constants.FinacelRequestStatus;
import com.vastsoft.yingtai.module.financel.constants.FinacelRequestType;
import com.vastsoft.yingtai.module.financel.entity.TFinanceAccount;
import com.vastsoft.yingtai.module.financel.entity.TFinanceRequest;
import com.vastsoft.yingtai.module.financel.exception.FinanceException;
import com.vastsoft.yingtai.module.financel.service.FinanceService;
import com.vastsoft.yingtai.module.stat.entity.FFinanceRecord;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;

public class FinanceAction extends BaseYingTaiAction {
	private Long lOrgId;
	private long lFinacalRequestId;
	private double dfPrice;
	private int iType;
	private int iStatus;
	private SplitPageUtil spu;
	
	private String strOrgName;
	private String strOrgCode;

	@ActionDesc(value = "冻结账户")
	public String freezeAccount() {
		try {
			FinanceService.instance.freezeAccount(takePassport(), lOrgId, AccountStatus.FREEZE);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "解冻账户")
	public String unfreezeAccount() {
		try {
			FinanceService.instance.freezeAccount(takePassport(), lOrgId, AccountStatus.NORMAL);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

//	@ActionDesc(value = "查询机构的账户信息")
	public String queryAccountByOrgid() {
		try {
			Passport passport = takePassport();
			if (UserType.ADMIN.equals(passport.getUserType()) && (lOrgId == null || lOrgId<=0))
				throw new FinanceException("必须指定机构ID！");
			if (lOrgId == null || lOrgId<=0)
				lOrgId = passport.getOrgId();
			if (lOrgId == null || lOrgId<=0)
				throw new FinanceException("必须指定机构ID！");
			TFinanceAccount account = FinanceService.instance.queryWithAddAccountByOrgid(passport, lOrgId);
			addElementToData("account", account);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "为账户充值")
	public String recharge() {
		try {
			FinanceService.instance.recharge(takePassport(), lFinacalRequestId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "为账户提现")
	public String takeCash() {
		try {
			FinanceService.instance.takeCash(takePassport(), lFinacalRequestId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "拒绝账户申请")
	public String rejectFinacelRequest() {
		try {
			FinanceService.instance.rejectFinacelRequest(takePassport(), lFinacalRequestId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "删除账户申请")
	public String deleteFinacelRequest() {
		try {
			FinanceService.instance.deleteFinacelRequest(takePassport(), lFinacalRequestId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "提交提现或充值申请")
	public String commitFinacelRequest() {
		try {
			FinanceService.instance.createFinacelRequest(takePassport(), dfPrice, FinacelRequestType.parseCode(iType));
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

//	@ActionDesc(value = "搜索账户申请")
	public String searchRequest() {
		try {
			List<TFinanceRequest> listFinanceRequest = FinanceService.instance.searchRequestList(takePassport(), lOrgId,this.strOrgName,this.strOrgCode,
					FinacelRequestStatus.parseCode(iStatus), super.getStart(), super.getEnd(), FinacelRequestType.parseCode(iType), spu);
			
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>(10);
			boolean isAdmin=UserService.instance.checkUserPermission(takePassport(), UserPermission.ADMIN_FINALCAL_MGR);
			boolean isMgr=UserService.instance.checkUserPermission(takePassport(), UserPermission.ORG_MGR);
			
			if(listFinanceRequest!=null&&!listFinanceRequest.isEmpty()){
				for(TFinanceRequest tfr:listFinanceRequest){
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("id", tfr.getId());
					map.put("org_id", tfr.getOrg_id());
					map.put("org_name", tfr.getV_org_name());
					map.put("org_code", tfr.getV_org_code());
					map.put("type", FinacelRequestType.parseCode(tfr.getOperation_type()));
					map.put("price", tfr.getPrice());
					map.put("create_time", tfr.getCreate_time());
					FinacelRequestStatus frs=FinacelRequestStatus.parseCode(tfr.getStatus());
					map.put("status", frs);
					
					map.put("can_cancel",isMgr&&FinacelRequestStatus.UNHANDLE.equals(frs));
					map.put("can_confirm", isAdmin&&FinacelRequestStatus.UNHANDLE.equals(frs));
					
					list.add(map);
				}
			}
			
			this.addElementToData("listFinanceRequest", list);
			this.addElementToData("total", spu.getTotalRow());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

//	@ActionDesc(value = "查询账户记录 ")
	public String searchAccountRecord() {
		try {
			List<FFinanceRecord> listFinanceRecord = FinanceService.instance.searchAccountRecordList(takePassport(),null,null,
					lOrgId,null,AccountRecordType.parseCode(iType), super.getStart(), super.getEnd(), spu);
			addElementToData("listFinanceRecord", listFinanceRecord);
			this.addElementToData("total", spu.getTotalRow());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

//	@ActionDesc(value = "查询所有账户 ")
	public String searchAccount() {
		try {
			List<TFinanceAccount> listFinanceAccount = FinanceService.instance.searchAccountList(takePassport(),
					this.strOrgName,this.strOrgCode,AccountStatus.parseCode(iStatus), super.getStart(), super.getEnd(), spu);
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>(10);
			boolean isAdmin=UserService.instance.checkUserPermission(takePassport(), UserPermission.ADMIN_FINALCAL_MGR);
			
			if(listFinanceAccount!=null&&!listFinanceAccount.isEmpty()){
				for(TFinanceAccount tfr:listFinanceAccount){
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("id", tfr.getId());
					map.put("org_id", tfr.getOrg_id());
					map.put("org_name", tfr.getV_org_name());
					map.put("org_code", tfr.getV_org_code());
					map.put("amount", tfr.getAmount());
					AccountStatus frs=AccountStatus.parseCode(tfr.getStatus());
					map.put("status", frs);
					
					map.put("can_freeze", isAdmin&&AccountStatus.NORMAL.equals(frs));
					
					list.add(map);
				}
			}
			
			this.addElementToData("listFinanceAccount", list);
			this.addElementToData("total", spu.getTotalRow());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setOrgId(long lOrgId) {
		this.lOrgId = lOrgId;
	}

	public void setFinacalRequestId(long lFinacalRequestId) {
		this.lFinacalRequestId = lFinacalRequestId;
	}

	public void setPrice(double dfPrice) {
		this.dfPrice = dfPrice;
	}

	public void setType(int iType) {
		this.iType = iType;
	}

	public void setStatus(int iStatus) {
		this.iStatus = iStatus;
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setOrgName(String strOrgName) {
		this.strOrgName = strOrgName;
	}

	public void setOrgCode(String strOrgCode) {
		this.strOrgCode = strOrgCode;
	}
}
