package com.vastsoft.yingtai.module.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.user.entity.TBaseUser;

public class OnlineManager {
	
	public static final OnlineManager instance=new OnlineManager();
	
	private OnlineManager(){}
	
	private Map<Long,BufferOrg> mapBuffer= new ConcurrentHashMap<Long, BufferOrg>();
	
	public void addOnlineOrg(TOrganization org){
		BufferOrg bo=new BufferOrg();
		bo.setOrg(org);
		this.mapBuffer.put(org.getId(), bo);
	}
	
	public TOrganization getOnlineOrgById(long lOrgId){
		BufferOrg bo=this.mapBuffer.get(lOrgId);
		return bo==null?null:bo.getOrg();
	}
	
	public Set<UserPermission> getPermisByUser(long lOrgId,long lUserId){
		BufferOrg bo=this.mapBuffer.get(lOrgId);
		if(bo==null) return null;
		BufferUser bu=bo.mapBufferUser.get(lUserId);
		return bu==null?null:bu.getListUserPermission();
	}
	
	public void updateOnlineOrg(TOrganization org){
		BufferOrg bo=this.mapBuffer.get(org.getId());
		if(bo!=null)
			bo.setOrg(org);
	}
	
	public List<BufferUser> getOnlineUserByOrg(long lOrgId){
		BufferOrg bo=this.mapBuffer.get(lOrgId);
		return bo==null?new ArrayList<BufferUser>():bo.getListBufferUser();
	}
	
	public void addOnlineUser(TOrganization org,TBaseUser user,Set<UserPermission> listUserPermission){
		BufferOrg bo=this.mapBuffer.get(org.getId());
		if(bo==null){
			bo=new BufferOrg();
			bo.setOrg(org);
		}
		
		BufferUser bu=new BufferUser();
		bu.setUser(user);
		bu.setListUserPermission(listUserPermission);
		bo.mapBufferUser.put(user.getId(), bu);
		
		this.mapBuffer.put(org.getId(), bo);
	}
	
	public TBaseUser getOnlinUser(long lOrgId,long lUserId){
		BufferOrg bo=this.mapBuffer.get(lOrgId);
		if(bo==null) return null;
		BufferUser bu=bo.mapBufferUser.get(lUserId);
		return bu==null?null:bu.getUser();
	}
	
	public void updateOnlineUser(long lOrgId,TBaseUser user){
		BufferOrg bo=this.mapBuffer.get(lOrgId);
		if(bo==null) return;
		BufferUser bu=bo.mapBufferUser.get(user.getId());
		if(bu==null) return;
		bu.setUser(user);
	}
	
	public void updateOnlineUserPermis(long lOrgId,long lUserId,Set<UserPermission> ups){
		BufferOrg bo=this.mapBuffer.get(lOrgId);
		if(bo==null) return;
		BufferUser bu=bo.mapBufferUser.get(lUserId);
		if(bu==null) return;
		bu.setListUserPermission(ups);
	}
	
	public void removeOnlineUser(long lOrgId,long lUserId){
		BufferOrg bo=this.mapBuffer.get(lOrgId);
		if(bo==null) return;
		bo.mapBufferUser.remove(lUserId);
		
		if(bo.mapBufferUser.isEmpty())
			this.mapBuffer.remove(lOrgId);
	}
	
	public void removeOnlineUser(long lUserId){
		for(BufferOrg org:mapBuffer.values()){
			org.mapBufferUser.remove(lUserId);
		}
	}
	
	public final class BufferUser{
		private TBaseUser user;
		private Set<UserPermission> listUserPermission=new HashSet<UserPermission>();
		
		private TBaseUser getUser() {
			return this.user;
		}
		
		private void setUser(TBaseUser user) {
			this.user = user;
		}
		
		private Set<UserPermission> getListUserPermission() {
			return listUserPermission;
		}
		
		private void setListUserPermission(Set<UserPermission> listUserPermission) {
			this.listUserPermission = listUserPermission;
		}
	}
	
	public final class BufferOrg{
		private TOrganization org;
		private Map<Long, BufferUser> mapBufferUser=new HashMap<Long, BufferUser>();
		
		private TOrganization getOrg() {
			return org;
		}
		
		private void setOrg(TOrganization org) {
			this.org = org;
		}
		
		private List<BufferUser> getListBufferUser() {
			return new ArrayList<BufferUser>(this.mapBufferUser.values());
		}
	}
}
