package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.db.VsSqlSession;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.AbstractShareEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteEntityAgent;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteResult;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.SharePatientDataType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.exception.RemotePatientInfoException;

public abstract class AbstractRemoteDataHandler {
	private ShareRemoteResult remoteResult;
	private Map<ShareRemoteParamsType, String> params;
	private List<String> savedUids = new ArrayList<String>();

	public void handleRemotePatientData(ShareRemoteResult remoteResult, Map<ShareRemoteParamsType, String> params)
			throws RemotePatientInfoException {
		if (remoteResult == null)
			throw new RemotePatientInfoException("结果集为空！");
		this.remoteResult = remoteResult;
		this.params = params;
		this.organizeDicomImg();
		VsSqlSession session = null;
		String sqlSessionKey = CommonTools.getUUID();
		try {
			session = SessionFactory.getSession(sqlSessionKey);
			this.handlePatientData(session);
			session.commit(sqlSessionKey, true);
		} catch (RemotePatientInfoException e) {
			session.rollback(true, sqlSessionKey);
			throw e;
		} finally {
			SessionFactory.closeSession(session, sqlSessionKey);
		}
	}
	
	/**整理影像对象，此处提供默认的实现,实现可以重写此方法*/
	protected void organizeDicomImg() {
		List<ShareRemoteEntityAgent> listDicomRea = this.getListReaByDataType(SharePatientDataType.DICOM_IMG);
		if (CollectionTools.isEmpty(listDicomRea))
			return;
		for (ShareRemoteEntityAgent shareRemoteEntityAgent : listDicomRea) {
			TDicomImg dicomImg = (TDicomImg) shareRemoteEntityAgent.getEntity();
			long[] body_part_ids = new long[0];
			int piece_amount = 0;
			List<ShareRemoteEntityAgent> listSeriseRea = this.getListSubReaByUid(shareRemoteEntityAgent.getUid(), SharePatientDataType.DICOM_IMG_SERIES);
			if (CollectionTools.isEmpty(listSeriseRea)){
				dicomImg.setBody_part_ids_arr(null);
				dicomImg.setPiece_amount(0);
				continue;
			}
			for (ShareRemoteEntityAgent seriesRea : listSeriseRea) {
				TSeries series = (TSeries) seriesRea.getEntity();
				if (!ArrayTools.contains(body_part_ids, series.getPart_type_id()))
					body_part_ids = (long[]) ArrayTools.add(body_part_ids, series.getPart_type_id());
				piece_amount +=series.getExpose_times();
			}
			dicomImg.setBody_part_ids_arr(body_part_ids);
			dicomImg.setPiece_amount(piece_amount);
		}
	}

	private void handlePatientData(VsSqlSession session) throws RemotePatientInfoException {
		for (Iterator<ShareRemoteEntityAgent> iterator = this.remoteResult.listRea().iterator(); iterator.hasNext();) {
			ShareRemoteEntityAgent remoteEntityAgent = (ShareRemoteEntityAgent) iterator.next();
			this.handleRemoteRea(remoteEntityAgent, session);
		}
	}

	private void handleRemoteRea(ShareRemoteEntityAgent rea, VsSqlSession session) throws RemotePatientInfoException {
		ShareRemoteEntityAgent parentRea = this.remoteResult.findReaByUid(rea.getParentUid());
		if (parentRea != null) {
			if (!this.savedUids.contains(parentRea.getUid()))
				this.handleRemoteRea(parentRea, session);
		}
		if (this.savedUids.contains(rea.getUid()))
			return;
		AbstractShareEntity oldEntity = this.tryTakeOldEntity(rea, session);
		if (oldEntity != null) {
			this.mergeEntity(oldEntity, rea, session);
			this.savedUids.add(rea.getUid());
		} else {
			this.saveReaForAdd(rea, parentRea == null ? null : parentRea.getEntity(), session);
			this.savedUids.add(rea.getUid());
		}
	}

	protected abstract void mergeEntity(AbstractShareEntity oldEntity, ShareRemoteEntityAgent rea, VsSqlSession session)
			throws RemotePatientInfoException;

	protected abstract AbstractShareEntity tryTakeOldEntity(ShareRemoteEntityAgent rea, VsSqlSession session)
			throws RemotePatientInfoException;

	protected abstract void saveReaForAdd(ShareRemoteEntityAgent rea, AbstractShareEntity parentEntity,
			VsSqlSession session) throws RemotePatientInfoException;

	protected long getCurrOrgId() throws RemotePatientInfoException {
		long result = this.remoteResult.getOrg_id();
		if (result <= 0)
			throw new RemotePatientInfoException("获取到的机构ID无效！");
		return result;
	}

	protected ShareRemoteEntityAgent getReaByUid(String uid) {
		return this.remoteResult.findReaByUid(uid);
	}

	protected List<ShareRemoteEntityAgent> getListSubReaByUid(String parentUid, SharePatientDataType pdType) {
		return this.remoteResult.findSubReaListByPdType(parentUid, pdType);
	}
	
	protected List<ShareRemoteEntityAgent> getListReaByDataType(SharePatientDataType pdType){
		return this.remoteResult.findReaListByPdType(pdType);
	}

	protected String getParamValue(ShareRemoteParamsType rpType) {
		return this.params.get(rpType);
	}

	public long getAffix_id() {
		return this.remoteResult.getAffix_id();
	}
}
