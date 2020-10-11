package com.vastsoft.yingtai.module.user.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.user.entity.FOrgUserMapping;
import com.vastsoft.yingtai.module.user.entity.TAdminUser;
import com.vastsoft.yingtai.module.user.entity.TBaseUser;
import com.vastsoft.yingtai.module.user.entity.TDoctorUser;
import com.vastsoft.yingtai.module.user.entity.TGeneralUser;
import com.vastsoft.yingtai.module.user.entity.TOrgUserMapping;
import com.vastsoft.yingtai.module.user.entity.TUserChange;
import com.vastsoft.yingtai.module.user.entity.TUserConfig;

/**
 * @author jyb
 */
public interface UserMapper {

	public TBaseUser loginUser(Map<String, Object> map);

	public TBaseUser queryBaseUserById(long userId);
	
	public TBaseUser queryUserByIdAndLock(long userId);

	public TAdminUser queryAdminUserById(long userId);

	public TAdminUser queryAdminUserByIdAndLock(long userId);

	public TGeneralUser queryGeneralUserById(long userId);

	public TGeneralUser queryGeneralUserAndLock(long id);

	public TDoctorUser queryDoctorUserById(long userId);

	public TDoctorUser queryDoctorUserAndLock(long userId);
	
	public void deleteDoctorById(long userId);
	
	public void deleteGeneralById(long userId);
	
	public void modifyDoctorVarify(Map<String,Object> prms);
	
	public void modifyGeneralVarify(Map<String,Object> prms);
	
	public List<TAdminUser> selectAdminList(Map<String,Object> prms);
	
	public Integer selectAdminCount(Map<String,Object> prms);

	public void addAdmin(TAdminUser user);

	public Integer insertUser(TBaseUser user);

	public void registerGeneral(TGeneralUser user);

	public void registerDoctor(TDoctorUser user);

	public void modifyUserInfo(TBaseUser user);

	public List<TBaseUser> queryBaseUsersByOrg(Map<String, Object> map);
	
	public List<TBaseUser> selectUserListByAdmin(Map<String,Object> prms);
	
	public Integer selectUserCountByAdmin(Map<String,Object> prms);

	public void modifyAdminInfo(TAdminUser adminUser);
	
	public void modifyUserEmailOrMobile(Map<String,Object> prms);
	
	public void modifyDoctorInfo(TDoctorUser baseUser);

	public void modifyGeneralInfo(TGeneralUser baseUser);
	
	public TBaseUser selectUserByLoginName(String strLoginName);

	public void modifyUserPwd(Map<String, Object> prms);
	
	public void resetUserPwd(Map<String,Object> prms);

	public void approveUser(Map<String, Object> prms);

	public void authorizeAdmin(Map<String, Object> prms);

	public void authorizeUser(Map<String, Object> prms);
	
	public void approveUserByAdmin(Map<String,Object> prms);
	
	public void insertUserChange(TUserChange tuc);
	
	public List<TUserChange> selectUserChangeList(Map<String,Object> prms);
	
	public int selectUserChangeCount(Map<String,Object> prms);
	
	public TUserChange selectUserChangeByUser(long userId);
	
	public TUserChange selectUserChangeByIdAndLock(long id);
	
	public void approveUserChange(Map<String,Object> prms);
	
	// 映射
	public TOrgUserMapping selectMapperByUser(Map<String, Object> prms);

	public TOrgUserMapping selectMapperByUserAndLock(Map<String, Object> prms);

	public List<TOrgUserMapping> selectMapperByOrg(Map<String, Object> prms);
	
	public int selectMapperCountByOrg(Map<String, Object> prms);

	public Integer insertOrg2User(TOrgUserMapping tum);

	public void modifyMapper(TOrgUserMapping tum);

	public void deleteOrg2User(long id);

	public List<TDoctorUser> searchDoctorsByOrgIdAndPermission(Map<String, Object> mapArg);

	public TBaseUser queryBaseUserByIdForUpdate(long id);

	public void modifyBaseUserStatus(TBaseUser user);

	public void deleteUserOrgMappingByUserId(long user_id);

	public void deleteMapper(TOrgUserMapping tum);

	public FOrgUserMapping selectFOrgUserMapperByUserIdAndOrgId(TOrgUserMapping tOrgUserMapping);
	
	public List<TBaseUser> selectUserLiteByOrg(Map<String, Object> prms);

	public TUserConfig selectUserConfigByUserIdForUpdate(long user_id);

	public TUserConfig selectUserConfigByUserId(long user_id);
	
	public void insertUserConfig(TUserConfig oldUserConfig);

	public void updateUserConfig(TUserConfig oldUserConfig);
}
