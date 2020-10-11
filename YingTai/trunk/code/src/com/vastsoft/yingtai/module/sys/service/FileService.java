package com.vastsoft.yingtai.module.sys.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.sys.constants.FileStatus;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.sys.mapper.FileMapper;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class FileService {
	public static final FileService instance = new FileService();

	private FileService() {
	}

//	/**
//	 * 文件上传
//	 * 
//	 * @throws BaseException
//	 */
//	public TFileMgr fileUpload(Passport passport, String strFileName) throws BaseException {
//		SqlSession session = null;
//		try {
//			session = SessionFactory.getSession();
//			TFileMgr file = new TFileMgr();
//			file.setCreate_time(new Date());
//			file.setOri_name(strFileName);
//			String strPath = SysService.instance.takeFilePath(DictionaryType.FILE_PATH);
//			file.setPath(strPath.endsWith("/") ? strPath
//					: strPath.endsWith("\\") ? strPath
//							: strPath + "/" + DateTools.YearToStr(new Date()) + "/"
//									+ DateTools.MonthDayToStr(new Date()) + "/" + CommonTools.getUUID()
//									+ CommonTools.getExtName(strFileName));//
//			file.setStatus(FileStatus.VALID.getCode());
//			FileMgrMapper mapper = session.getMapper(FileMgrMapper.class);
//			mapper.addFileMgr(file);
//			session.commit();
//			return file;
//		} catch (Exception e) {
//			session.rollback();
//			throw e;
//		} finally {
//			SessionFactory.closeSession(session);
//		}
//	}
	
	/**根据ID列表获取文件列表
	 * @throws BaseException
	 */
	public List<TFile> queryFileByIdList(List<Long> listFileId) {
		if (listFileId==null||listFileId.size()<=0)return null;
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FileMapper mapper = session.getMapper(FileMapper.class);
			return mapper.queryFileMgrByIdsStr(listFileId);
		} finally {
			SessionFactory.closeSession(session);
		}
	}
	
	/**
	 * 根据ID获取文件
	 * 
	 * @throws BaseException
	 */
	public TFile queryFileById(Passport passport, long lFileId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FileMapper mapper = session.getMapper(FileMapper.class);
			return mapper.queryFileMgrById(lFileId);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public TFile getFileByID(long lFileId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FileMapper mapper = session.getMapper(FileMapper.class);
			return mapper.queryFileMgrById(lFileId);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @throws BaseException
	 */
	public void deleteFile(Passport passport, long lFileId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FileMapper mapper = session.getMapper(FileMapper.class);
			File file = new File(this.queryFileById(passport, lFileId).getPath());
			if (file.exists())file.delete();
			mapper.deleteFileMgrById(lFileId);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}
	
	public void saveFiles(Passport passport, List<TFile> tfms) throws BaseException {
		if(passport==null || tfms==null || tfms.isEmpty())
			throw new NullParameterException("passport & tfms");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FileMapper mapper = session.getMapper(FileMapper.class);
			for(TFile file:tfms){
				file.setCreate_time(new Date());
				file.setStatus(FileStatus.VALID.getCode());
				mapper.addFileMgr(file);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TFile> queryFileByIdList(long[] strToLongArray) {
		if (strToLongArray==null||strToLongArray.length<=0)
			return null;
		List<Long> rrr = new ArrayList<Long>();
		for (long l : strToLongArray) {
			rrr.add(l);
		}
		return this.queryFileByIdList(rrr);
	}

//	public List<TFile> queryFileByIdList(long[] fileIds) throws BaseException {
//		return this.queryFileByIdList(CommonTools.arrayToList(fileIds));
//	}
	
}
