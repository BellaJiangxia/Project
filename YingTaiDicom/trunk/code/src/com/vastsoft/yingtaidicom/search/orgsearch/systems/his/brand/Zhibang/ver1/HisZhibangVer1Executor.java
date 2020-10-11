package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.collection.UniqueList;
import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.MapBuilder;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.assist.AbstractSystemEntity;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager.RemoteParamEntry;
import com.vastsoft.yingtaidicom.search.assist.SearchResult;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.assist.SearchB_BASYParam;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.assist.SearchM_BRJBXXParam;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.assist.SearchZ_BAHParam;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.config.HisZhibangVer1Mapper;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity.B_BASY;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity.HisObject;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity.M_BRJBXX;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity.Z_BAH;

/**
 * 智邦 his 版本v1.0 搜索执行器
 * 
 * @author jben
 * @since 2.0
 */
public class HisZhibangVer1Executor extends ExternalSystemSearchExecutor {

	public HisZhibangVer1Executor(SystemIdentity systemIdentity, SessionFactory factory) {
		super(systemIdentity, factory);
	}

	/** 临时卡卡号 */
	private static final String LINGSHI_CARDNO = "0000009999";

	@Override
	public boolean canSearch(RemoteParamsType paramType) {
		LoggerUtils.logger.info("通过参数类型在HIS-智邦-V1中确认是否可以查询！参数类型：" + String.valueOf(paramType));
		if (RemoteParamsType.IDENTITY_ID.equals(paramType))
			return true;
		if (RemoteParamsType.ZHUYUAN_NUM.equals(paramType))// 住院号和病案号一致
			return true;
		if (RemoteParamsType.BINGAN_NUM.equals(paramType))// 住院号和病案号一致
			return true;
		if (RemoteParamsType.DIAGNOSIS_CARD_NUM.equals(paramType))
			return true;
		return false;
	}

	private void searchLastBingAnNum(LastCaseNums r, HisZhibangVer1Mapper mapper)
			throws SQLException {
		List<B_BASY> listB_BASY = mapper.selectLastB_BASYByCount();
		if (!CollectionTools.isEmpty(listB_BASY)) {
			for (B_BASY b_BASY : listB_BASY) {
				if (StringTools.isEmpty(b_BASY.getBah()))
					continue;
				r.add(RemoteParamsType.ZHUYUAN_NUM, b_BASY.getBah().trim(), (b_BASY.getXm() == null ? "" : b_BASY.getXm().trim()));
				r.add(RemoteParamsType.BINGAN_NUM, b_BASY.getBah().trim(), (b_BASY.getXm() == null ? "" : b_BASY.getXm().trim()));
			}
		}
		List<Z_BAH> listZ_BAH = mapper.selectLastZ_BAHByCount();
		if (!CollectionTools.isEmpty(listZ_BAH)) {
			for (Z_BAH z_BAH : listZ_BAH) {
				if (StringTools.isEmpty(z_BAH.getZ_bah()))
					continue;
				r.add(RemoteParamsType.ZHUYUAN_NUM, z_BAH.getZ_bah().trim(), (z_BAH.getZ_xm() == null ? "" : z_BAH.getZ_xm().trim()));
				r.add(RemoteParamsType.BINGAN_NUM, z_BAH.getZ_bah().trim(), (z_BAH.getZ_xm() == null ? "" : z_BAH.getZ_xm().trim()));
			}
		}
	}

	private void searchLastCardNo(LastCaseNums r, HisZhibangVer1Mapper mapper)
			throws SQLException {
		List<M_BRJBXX> listM_BRJBXX = mapper.selectLastM_BRJBXXByCount();
		if (CollectionTools.isEmpty(listM_BRJBXX))
			return;
		for (M_BRJBXX m_BRJBXX : listM_BRJBXX) {
			if (StringTools.isEmpty(m_BRJBXX.getCard_no()))
				continue;
			r.add(RemoteParamsType.DIAGNOSIS_CARD_NUM,m_BRJBXX.getCard_no().trim(),(m_BRJBXX.getM_brxm() == null ? "" : m_BRJBXX.getM_brxm().trim()));
		}
	}

	private void searchLastIdentityId(LastCaseNums r, HisZhibangVer1Mapper mapper)
			throws SQLException {
		List<B_BASY> listB_BASY = mapper.selectLastB_BASYByCount();
		if (!CollectionTools.isEmpty(listB_BASY)) {
			for (B_BASY b_BASY : listB_BASY) {
				if (!StringTools.wasIdentityId(b_BASY.getSfzh()))
					continue;
				r.add(RemoteParamsType.IDENTITY_ID,b_BASY.getSfzh().trim(),(b_BASY.getXm() == null ? "" : b_BASY.getXm().trim()));
			}
		}

		List<M_BRJBXX> listM_BRJBXX = mapper.selectLastM_BRJBXXByCount();
		if (!CollectionTools.isEmpty(listM_BRJBXX)) {
			for (M_BRJBXX m_BRJBXX : listM_BRJBXX) {
				if (!StringTools.wasIdentityId(m_BRJBXX.getM_sfzh()))
					continue;
				r.add(RemoteParamsType.IDENTITY_ID,m_BRJBXX.getM_sfzh().trim(),(m_BRJBXX.getM_brxm() == null ? "" : m_BRJBXX.getM_brxm().trim()));
			}
		}

		List<Z_BAH> listZ_BAH = mapper.selectLastZ_BAHByCount();
		if (!CollectionTools.isEmpty(listZ_BAH)) {
			for (Z_BAH z_BAH : listZ_BAH) {
				if (!StringTools.wasIdentityId(z_BAH.getZ_sfzh()))
					continue;
				r.add(RemoteParamsType.IDENTITY_ID,z_BAH.getZ_sfzh().trim() , (z_BAH.getZ_xm() == null ? "" : z_BAH.getZ_xm().trim()));
			}
		}
	}

	@Override
	public void searchLastCaseNums(LastCaseNums result,RemoteParamsManager remoteParamsManager) throws SearchExecuteException {
		SqlSession session = super.getSession();
		try {
			HisZhibangVer1Mapper mapper = session.getMapper(HisZhibangVer1Mapper.class);
			this.searchLastIdentityId(result, mapper);
			this.searchLastCardNo(result, mapper);
			this.searchLastBingAnNum(result, mapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		} finally {
			super.closeSession(session);
		}
	}

	@Override
	public byte[] readThumbnail(RemoteParamsManager remoteParamsManager, String thumbnail_uid)
			throws SearchExecuteException {
		return null;
	}

	@Override
	public void searchPatientData(SearchResult searchResult, RemoteParamsManager remoteParamsManager)
			throws SearchExecuteException {
		Iterator<RemoteParamEntry> iterator = remoteParamsManager.getKeyIterator();
		if (iterator == null)
			return;
		try {
			while (iterator.hasNext()) {
				RemoteParamEntry remoteParamEntry = (RemoteParamEntry) iterator.next();
				if (remoteParamEntry == null)
					continue;
				if (!this.canSearch(remoteParamEntry.getRemoteParamsType()))
					continue;
				AbstractSystemEntity abstractSystemEntity;
				if (RemoteParamsType.BINGAN_NUM.equals(remoteParamEntry.getRemoteParamsType())
						|| RemoteParamsType.ZHUYUAN_NUM.equals(remoteParamEntry.getRemoteParamsType())) {
					abstractSystemEntity = searchResult.findEntityByRemoteParamEntry(ExternalSystemType.HIS,
							new RemoteParamEntry(RemoteParamsType.BINGAN_NUM, remoteParamEntry.getRemoteParamValue()));
				} else {
					abstractSystemEntity = searchResult.findEntityByRemoteParamEntry(ExternalSystemType.HIS,
							remoteParamEntry);
				}
				if (abstractSystemEntity != null)
					continue;
				HisObject hisObject = this.searchHisObjectByRemoteParamEntry(remoteParamEntry);
				if (hisObject != null)
					searchResult.saveObject(this.getSystemIdentity(), hisObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		}
	}

	private HisObject searchHisObjectByRemoteParamEntry(RemoteParamEntry remoteParamEntry)
			throws SearchExecuteException {
		if (RemoteParamsType.BINGAN_NUM.equals(remoteParamEntry.getRemoteParamsType())
				|| RemoteParamsType.ZHUYUAN_NUM.equals(remoteParamEntry.getRemoteParamsType())) {
			return this.searchHisObjectByBingan_Num(remoteParamEntry.getRemoteParamValue());
		}
		if (RemoteParamsType.DIAGNOSIS_CARD_NUM.equals(remoteParamEntry.getRemoteParamsType())) {
			return this.searchHisObjectByCardNo(remoteParamEntry.getRemoteParamValue());
		}
		if (RemoteParamsType.IDENTITY_ID.equals(remoteParamEntry.getRemoteParamsType())) {
			return this.searchHisObjectByIdentity_id(remoteParamEntry.getRemoteParamValue());
		}
		return null;
	}

	private HisObject searchHisObjectByIdentity_id(String identity_id) throws SearchExecuteException {
		SqlSession session = super.getSession();
		try {
			HisObject result = new HisObject(new RemoteParamEntry(RemoteParamsType.IDENTITY_ID, identity_id),
					super.getSystemIdentity());
			HisZhibangVer1Mapper mapper = session.getMapper(HisZhibangVer1Mapper.class);
			// --------------------Z_BAH--------------------------------
			List<Z_BAH> listZ_BAH = this.searchZ_BAHByIdentityIds(mapper, CollectionTools.buildList(identity_id));
			result.addZ_BAH(listZ_BAH);
			if (!CollectionTools.isEmpty(listZ_BAH)) {
				@SuppressWarnings("unchecked")
				List<String> listCard_no = SetUniqueList.decorate(new ArrayList<String>());
				@SuppressWarnings("unchecked")
				List<String> listM_brid = SetUniqueList.decorate(new ArrayList<String>());
				@SuppressWarnings("unchecked")
				List<String> listBah = SetUniqueList.decorate(new ArrayList<String>());
				for (Z_BAH z_BAH : listZ_BAH) {
					listBah.add(z_BAH.getZ_bah());
					listCard_no.add(z_BAH.getCard_no());
					listM_brid.add(z_BAH.getM_brid());
				}
				listZ_BAH = this.searchZ_BAHByBinganNums(mapper, listBah);
				result.addZ_BAH(listZ_BAH);
				listZ_BAH = this.searchZ_BAHByCardNos(mapper, listCard_no);
				result.addZ_BAH(listZ_BAH);
				listZ_BAH = this.searchZ_BAHByBrIds(mapper, listM_brid);
				result.addZ_BAH(listZ_BAH);
				List<B_BASY> listB_BASY = this.searchB_BASYByBinganNums(mapper, listBah);
				result.addB_BASY(listB_BASY);
				List<M_BRJBXX> listM_BRJBXX = this.searchM_BRJBXXByBrIds(mapper, listM_brid);
				result.addM_BRJBXX(listM_BRJBXX);
				listM_BRJBXX = this.searchM_BRJBXXByCardNos(mapper, listCard_no);
				result.addM_BRJBXX(listM_BRJBXX);
			}
			// ------------------B_BASY------------------------------------
			List<B_BASY> listB_BASY = this.searchB_BASYByIdentityIds(mapper, CollectionTools.buildList(identity_id));
			result.addB_BASY(listB_BASY);
			if (!CollectionTools.isEmpty(listB_BASY)) {
				@SuppressWarnings("unchecked")
				List<String> listBah = SetUniqueList.decorate(new ArrayList<String>());
				for (B_BASY b_BASY : listB_BASY) {
					listBah.add(b_BASY.getBah());
				}
				listZ_BAH = this.searchZ_BAHByBinganNums(mapper, listBah);
				result.addZ_BAH(listZ_BAH);
			}
			// -----------------M_BRJBXX-----------------------------------
			List<M_BRJBXX> listM_BRJBXX = this.searchM_BRJBXXByBrIdentityIds(mapper, CollectionTools.buildList(identity_id));
			result.addM_BRJBXX(listM_BRJBXX);
			if (!CollectionTools.isEmpty(listM_BRJBXX)) {
				@SuppressWarnings("unchecked")
				List<String> listCard_no = SetUniqueList.decorate(new ArrayList<String>());
				@SuppressWarnings("unchecked")
				List<String> listM_brid = SetUniqueList.decorate(new ArrayList<String>());
				for (M_BRJBXX m_BRJBXX : listM_BRJBXX) {
					listCard_no.add(m_BRJBXX.getCard_no());
					listM_brid.add(m_BRJBXX.getM_brid());
				}
				listZ_BAH = this.searchZ_BAHByCardNos(mapper, listCard_no);
				result.addZ_BAH(listZ_BAH);
				listZ_BAH = this.searchZ_BAHByBrIds(mapper, listM_brid);
				result.addZ_BAH(listZ_BAH);
				listM_BRJBXX = this.searchM_BRJBXXByCardNos(mapper, listCard_no);
				result.addM_BRJBXX(listM_BRJBXX);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		} finally {
			super.closeSession(session);
		}
	}

	private HisObject searchHisObjectByCardNo(String card_no) throws SearchExecuteException {
		if (LINGSHI_CARDNO.equals(card_no))
			return null;
		SqlSession session = super.getSession();
		try {
			HisObject result = new HisObject(new RemoteParamEntry(RemoteParamsType.DIAGNOSIS_CARD_NUM, card_no),
					super.getSystemIdentity());
			HisZhibangVer1Mapper mapper = session.getMapper(HisZhibangVer1Mapper.class);
			// -------------------Z_BAH----------------------
			List<Z_BAH> listZ_BAH = this.searchZ_BAHByCardNos(mapper, CollectionTools.buildList(card_no));
			result.addZ_BAH(listZ_BAH);
			if (!CollectionTools.isEmpty(listZ_BAH)) {
				@SuppressWarnings("unchecked")
				List<String> listM_brid = SetUniqueList.decorate(new ArrayList<String>());
				@SuppressWarnings("unchecked")
				List<String> listIdentityId = SetUniqueList.decorate(new ArrayList<String>());
				for (Z_BAH z_BAH : listZ_BAH) {
					listM_brid.add(z_BAH.getM_brid());
					listIdentityId.add(z_BAH.getZ_sfzh());
				}
				List<M_BRJBXX> listM_BRJBXX = this.searchM_BRJBXXByBrIds(mapper, listM_brid);
				result.addM_BRJBXX(listM_BRJBXX);
				listZ_BAH = this.searchZ_BAHByIdentityIds(mapper, listIdentityId);
				result.addZ_BAH(listZ_BAH);
				listM_BRJBXX = this.searchM_BRJBXXByBrIdentityIds(mapper, listIdentityId);
				result.addM_BRJBXX(listM_BRJBXX);
				List<B_BASY> listB_BASY = this.searchB_BASYByIdentityIds(mapper, listIdentityId);
				result.addB_BASY(listB_BASY);
			}
			// --------------------M_BRJBXX-----------------------------
			List<M_BRJBXX> listM_BRJBXX = this.searchM_BRJBXXByCardNos(mapper, CollectionTools.buildList(card_no));
			result.addM_BRJBXX(listM_BRJBXX);
			if (!CollectionTools.isEmpty(listM_BRJBXX)) {
				@SuppressWarnings("unchecked")
				List<String> listM_brid = SetUniqueList.decorate(new ArrayList<String>());
				@SuppressWarnings("unchecked")
				List<String> listIdentityId = SetUniqueList.decorate(new ArrayList<String>());
				for (M_BRJBXX m_BRJBXX : listM_BRJBXX) {
					listIdentityId.add(m_BRJBXX.getM_sfzh());
					listM_brid.add(m_BRJBXX.getM_brid());
				}
				listZ_BAH = this.searchZ_BAHByBrIds(mapper, listM_brid);
				result.addZ_BAH(listZ_BAH);
				listZ_BAH = this.searchZ_BAHByIdentityIds(mapper, listIdentityId);
				result.addZ_BAH(listZ_BAH);
				listM_BRJBXX = this.searchM_BRJBXXByBrIdentityIds(mapper, listIdentityId);
				result.addM_BRJBXX(listM_BRJBXX);
				List<B_BASY> listB_BASY = this.searchB_BASYByIdentityIds(mapper, listIdentityId);
				result.addB_BASY(listB_BASY);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		} finally {
			super.closeSession(session);
		}
	}

	private List<Z_BAH> searchZ_BAHByBrIds(HisZhibangVer1Mapper mapper, List<String> listM_brid)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(listM_brid))
			return null;
		SearchZ_BAHParam szp = new SearchZ_BAHParam();
		szp.setM_brids(ArrayTools.collectionToArray(listM_brid,String.class));
		return this.searchZ_BAH(mapper, szp);
	}

	private List<Z_BAH> searchZ_BAHByCardNos(HisZhibangVer1Mapper mapper, List<String> listCard_nos)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(listCard_nos))
			return null;
		SearchZ_BAHParam szp = new SearchZ_BAHParam();
		szp.setCard_nos(ArrayTools.collectionToArray(listCard_nos,String.class));
		return this.searchZ_BAH(mapper, szp);
	}

	private HisObject searchHisObjectByBingan_Num(String bingan_num) throws SearchExecuteException {
		SqlSession session = super.getSession();
		try {
			HisObject result = new HisObject(new RemoteParamEntry(RemoteParamsType.BINGAN_NUM, bingan_num),
					super.getSystemIdentity());
			HisZhibangVer1Mapper mapper = session.getMapper(HisZhibangVer1Mapper.class);
			// ----------------Z_BAH---------------------------------
			List<Z_BAH> listZ_BAH = this.searchZ_BAHByBinganNums(mapper, CollectionTools.buildList(bingan_num));
			result.addZ_BAH(listZ_BAH);
			if (!CollectionTools.isEmpty(listZ_BAH)) {
				@SuppressWarnings("unchecked")
				List<String> listCard_no = SetUniqueList.decorate(new ArrayList<String>());
				@SuppressWarnings("unchecked")
				List<String> listM_brid = SetUniqueList.decorate(new ArrayList<String>());
				@SuppressWarnings("unchecked")
				List<String> listIdentityId = SetUniqueList.decorate(new ArrayList<String>());
				for (Z_BAH z_BAH : listZ_BAH) {
					listCard_no.add(z_BAH.getCard_no());
					listM_brid.add(z_BAH.getM_brid());
					listIdentityId.add(z_BAH.getZ_sfzh());
				}
				List<M_BRJBXX> listM_BRJBXX = this.searchM_BRJBXXByCardNos(mapper, listCard_no);
				result.addM_BRJBXX(listM_BRJBXX);
				listM_BRJBXX = this.searchM_BRJBXXByBrIds(mapper, listM_brid);
				result.addM_BRJBXX(listM_BRJBXX);
				listZ_BAH = this.searchZ_BAHByIdentityIds(mapper, listIdentityId);
				result.addZ_BAH(listZ_BAH);
				listM_BRJBXX = this.searchM_BRJBXXByBrIdentityIds(mapper, listIdentityId);
				result.addM_BRJBXX(listM_BRJBXX);
				List<B_BASY> listB_BASY = this.searchB_BASYByIdentityIds(mapper, listIdentityId);
				result.addB_BASY(listB_BASY);
			}
			// ------------------B_BASY---------------------------
			List<B_BASY> listB_BASY = this.searchB_BASYByBinganNums(mapper, CollectionTools.buildList(bingan_num));
			result.addB_BASY(listB_BASY);
			if (!CollectionTools.isEmpty(listB_BASY)) {
				@SuppressWarnings("unchecked")
				List<String> listIdentityId = SetUniqueList.decorate(new ArrayList<String>());
				for (B_BASY b_BASY : listB_BASY) {
					listIdentityId.add(b_BASY.getSfzh());
				}
				listZ_BAH = this.searchZ_BAHByIdentityIds(mapper, listIdentityId);
				result.addZ_BAH(listZ_BAH);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		} finally {
			super.closeSession(session);
		}
	}

	private List<B_BASY> searchB_BASYByBinganNums(HisZhibangVer1Mapper mapper, List<String> listBinganNum)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(listBinganNum))
			return null;
		SearchB_BASYParam sbp = new SearchB_BASYParam();
		sbp.setBahs(ArrayTools.collectionToArray(listBinganNum,String.class));
		return this.searchB_BASY(mapper, sbp);
	}

	private List<B_BASY> searchB_BASY(HisZhibangVer1Mapper mapper, SearchB_BASYParam sbp)
			throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> mapArg = sbp.buildMap();
		if (CommonTools.isEmpty(mapArg))
			return null;
		List<B_BASY> result = mapper.selectB_BASY(mapArg);
		LoggerUtils.logger.info("查询到B_BASY数据" + (result == null ? 0 : result.size()) + "条！");
		return result;
	}

	private List<B_BASY> searchB_BASYByIdentityIds(HisZhibangVer1Mapper mapper, List<String> listIdentityId)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(listIdentityId))
			return null;
		SearchB_BASYParam sbp = new SearchB_BASYParam();
		sbp.setSfzhs(ArrayTools.collectionToArray(listIdentityId,String.class));
		return this.searchB_BASY(mapper, sbp);
	}

	private List<M_BRJBXX> searchM_BRJBXXByBrIdentityIds(HisZhibangVer1Mapper mapper, List<String> listIdentityId)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(listIdentityId))
			return null;
		SearchM_BRJBXXParam smp = new SearchM_BRJBXXParam();
		smp.setM_sfzhs(ArrayTools.collectionToArray(listIdentityId,String.class));
		return this.searchM_BRJBXX(mapper, smp);
	}

	private List<M_BRJBXX> searchM_BRJBXX(HisZhibangVer1Mapper mapper, SearchM_BRJBXXParam smp)
			throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> mapArg = smp.buildMap();
		if (CommonTools.isEmpty(mapArg))
			return null;
		List<M_BRJBXX> result = mapper.selectM_BRJBXX(mapArg);
		LoggerUtils.logger.info("查询到M_BRJBXX数据" + (result == null ? 0 : result.size()) + "条！");
		return result;
	}

	private List<Z_BAH> searchZ_BAHByIdentityIds(HisZhibangVer1Mapper mapper, List<String> listIdentityId)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(listIdentityId))
			return null;
		SearchZ_BAHParam szp = new SearchZ_BAHParam();
		szp.setZ_sfzhs(ArrayTools.collectionToArray(listIdentityId,String.class));
		return this.searchZ_BAH(mapper, szp);
	}

	private List<M_BRJBXX> searchM_BRJBXXByBrIds(HisZhibangVer1Mapper mapper, List<String> listM_brid)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(listM_brid))
			return null;
		SearchM_BRJBXXParam smp = new SearchM_BRJBXXParam();
		smp.setM_brids(ArrayTools.collectionToArray(listM_brid,String.class));
		return this.searchM_BRJBXX(mapper, smp);
	}

	private List<M_BRJBXX> searchM_BRJBXXByCardNos(HisZhibangVer1Mapper mapper, List<String> listCard_no)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(listCard_no))
			return null;
		SearchM_BRJBXXParam smp = new SearchM_BRJBXXParam();
		smp.setCard_nos(ArrayTools.collectionToArray(listCard_no,String.class));
		return this.searchM_BRJBXX(mapper, smp);
	}

	private List<Z_BAH> searchZ_BAH(HisZhibangVer1Mapper mapper, SearchZ_BAHParam szp)
			throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> mapArg = szp.buildMap();
		if (CommonTools.isEmpty(mapArg))
			return null;
		List<Z_BAH> result = mapper.selectZ_BAH(mapArg);
		LoggerUtils.logger.info("查询到Z_BAH数据" + (result == null ? 0 : result.size()) + "条！");
		return result;
	}

	private List<Z_BAH> searchZ_BAHByBinganNums(HisZhibangVer1Mapper mapper, List<String> bingan_nums)
			throws IllegalArgumentException, IllegalAccessException {
		if (CollectionTools.isEmpty(bingan_nums))
			return null;
		SearchZ_BAHParam szp = new SearchZ_BAHParam();
		szp.setZ_bahs(ArrayTools.collectionToArray(bingan_nums,String.class));
		return this.searchZ_BAH(mapper, szp);
	}

	@Override
	public List<PatientInfoResult> searchPatientInfoByPatientNameOrIdentifyId(RemoteParamsManager remoteParamsManager, String patient_name, String identify_id)
			throws SearchExecuteException {
		try {
			return this.searchPatientInfoByPatientName(patient_name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		}
	}

	private List<PatientInfoResult> searchPatientInfoByPatientName(String patient_name) throws SearchExecuteException {
		if (StringTools.isEmpty(patient_name))
			return null;
		List<PatientInfoResult> result = new UniqueList<PatientInfoResult>();
		SqlSession session = super.getSession();
		try {
			HisZhibangVer1Mapper mapper = session.getMapper(HisZhibangVer1Mapper.class);
			// --------------------Z_BAH--------------------------------
			List<Z_BAH> listZ_BAH = mapper.searchZ_BAHByPatientName(new MapBuilder<String,Object>("like_patient_name", patient_name).toMap());
			HisObject.tranfarZ_BAH(result, listZ_BAH, this.getSystemIdentity());
			// ------------------B_BASY------------------------------------
			List<B_BASY> listB_BASY = mapper.searchB_BASYByPatientName(new MapBuilder<String,Object>("like_patient_name", patient_name).toMap());
			HisObject.tranfarB_BASY(result, listB_BASY, this.getSystemIdentity());
			// -----------------M_BRJBXX-----------------------------------
			List<M_BRJBXX> listM_BRJBXX = mapper.searchM_BRJBXXByByPatientName(new MapBuilder<String,Object>("like_patient_name", patient_name).toMap());
			HisObject.tranfarM_BRJBXX(result, listM_BRJBXX, this.getSystemIdentity());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchExecuteException(e);
		} finally {
			super.closeSession(session);
		}
	}
}
