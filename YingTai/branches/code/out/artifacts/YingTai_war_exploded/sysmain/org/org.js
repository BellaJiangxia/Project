angular.module("org", ["org.friends","org.product"])//"ui.bootstrap","services","diy.selector","ui.router","checklist-model","diy.dialog",
.config(function($stateProvider,$urlRouterProvider){
	$stateProvider.state("org",{
		abstract: true,
		url:"/org",
		template: "<ui-view />",
		redirectTo: 'org.info'
	}).state("org.info",{
		url:"/info",
		templateUrl:"org/org.info.html",
		controller:"orgInfoCtrl"
	}).state("org.users",{
		url:"/users",
		templateUrl:"org/org.users.html",
		controller:"orgUserCtrl"
	})
	.state("org.userMgr",{
		url:"/userMgr/{userId:int}/{flag}",
		templateUrl:"org/org.userMgr.html",
		controller:"orgUserMgrCtrl"
	})
	.state("org.addFriend",{
		url:"/addFriend",
		templateUrl: "org/org.friendsAdd.html",
		controller:"addOrgFriendCtrl"
	}).state("org.account",{
		url:"/account",
		templateUrl:"org/org.account.html",
		controller:"orgAccountCtrl"
	}).state("org.config",{
		url:"/config",
		templateUrl:"org/org.config.html",
		controller:"orgConfigCtrl"
	});
//	  $urlRouterProvider.otherwise(function ($injector, $location) {
//		  if($location.path().indexOf("/org")!==-1){
//			  return "/org/info";
//		  }
//	  });
})
.filter("subStr",function(){
	return function(str,length){
		if(!str) return "";
		if(str.length<=length) return str;
		str=str.substr(0,length);
		return str+" ...";
	};
}).controller("orgConfigCtrl",function($scope,$diyhttp,$queryService,$msgDialog){
	$scope.mapDeviceMapper=[];
	$scope.org_device_name_2_sys_device_name_mapper_config = [];
	$scope.initData=function(){
		$diyhttp.post("org!takeDeviceMapper.action",{},function(data){
			$scope.mapDeviceMapper=data.mapDeviceMapper;
		});
		$diyhttp.post("orgConfig!takeOrgDeviceNameTransToSysDeviceNameMapper.action",{},function(data){
			$scope.org_device_name_2_sys_device_name_mapper_config=data.org_device_name_2_sys_device_name_mapper_config;
		});
	};
	var makeMap= function(map){
		if (!map||map.length<=0)
			return {};
		var obj={};
		for (var int = 0; int < map.length; int++) {
			var onk=map[int];
			obj[onk.key]=onk.value;
		}
		return obj;
	};
	$scope.saveOrg_device_name_2_sys_device_name_mapper_config=function(){
		var obj=makeMap($scope.org_device_name_2_sys_device_name_mapper_config);
		$diyhttp.post("orgConfig!saveOrgDeviceName2SysDeviceNameMapperConfig.action",{
			jsonDeviceMapper:angular.toJson(obj)
		},function(data){
			$msgDialog.showMessage("保存成功！");
		});
	};
	$scope.saveDeviceMapper=function(){
		var obj=makeMap($scope.mapDeviceMapper);
		$diyhttp.post("org!saveDeviceMapper.action",{
			jsonDeviceMapper:angular.toJson(obj)
		},function(data){
			$msgDialog.showMessage("保存成功！");
		});
	};
}).service("productService", function($http) {
	this.getDicValuesByType=function(type){
		return $http.post("sys!queryDeviceList.action",{type:type})
				.then(function(result){
					return result.data.data.listDicValue || [];
				});
	};
	this.getDicValuesByParent=function(parent){
		return $http.post("sys!queryPartList.action",{parentId:parent})
			.then(function(result){
				return result.data.data.listDicValue || [];
			});
	};
}).controller("orgInfoCtrl",function($scope,$http,$state,$timeout,$msgDialog,$window){
	$scope.msg="";
	$scope.types=[];
	$scope.levels=[];
	$scope.default_ol=" 请选择 ";
	$scope.publicStatus=[];
	
	$scope.msg="";
	$scope.notEdit=false;
	$scope.showTip=false;
	
	$scope.org={};
	
	//显示和隐藏我的机构
	$scope.showOrg=function(){
		$http.post("org!showOrg.action")
		.success(function(data){
			if (data.code==0) {
				$msgDialog.showMessage('切换成功！');
				if ($scope.org.visible==1) {
					$scope.org.visible=2
					$scope.org.visibleStr='隐藏';
				}else {
					$scope.org.visible=1;
					$scope.org.visibleStr='可见';
				}
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};

	var initProp=function(){
		$http.post("common!queryOrgProperty.action")
			.success(function(result) {
				if(result.code===0){
					$scope.types=result.data.list;
				}else{
					$scope.types=[];
				}
			});
	};
	
	var initLevel=function(type){
		$http.post("sys!queryDicValueListByType.action",{type:type})
		.success(function(result) {
			if(result.code===0){
				$scope.levels=result.data.listDicValue;
			}else{
				$scope.levels=[];
			}
		});
	};

	$scope.$watch("org.org_property",function(a,b){
		if(a){
			//if(b)$scope.showTip=true;
			if(a===11){
				initLevel(3);
			}else if(a===12){
				initLevel(23);
			}
		}
	});

	$scope.initPublicStatus=function(){
		$http.post("common!queryPublicStatus.action")
		.success(function(result) {
			if(result.code===0)
				$scope.publicStatus=result.data.status;
		});
	};
	
	$scope.loadOrg=function(){
		$http.post("org!queryOrgByPassport.action")
		.success(function(result) {
			if(result.code===0){
				$scope.org=result.data.org;
				$scope.notEdit=result.data.is_mgr;
				$scope.default_ol=result.data.org.level_name;
				var rn="";
				angular.forEach($scope.org.permissionList, function(value, key) {
					rn+=","+value.name;
				});
				$scope.org.roleName=rn.substr(1);
			}else if(result.code===400){
				$scope.msg=result.name;
				$timeout(function(){
					$window.location.href="../login";
				},2000);
			}else if(result.code===500){
				$scope.msg=result.name;
				$timeout(function(){
					$state.go("myOrg.list");
				},2000);
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.changePublic=function(){
		if($scope.org){
			$http.post("org!changePublicStatus.action",{
				orgId:$scope.org.id,
				publicStatus:$scope.org.is_public
			})
			.success(function(result) {
				$msgDialog.showMessage(result.name);
			});
		}
	};
	
	$scope.saveOrg=function(){
		if($scope.notEdit){
			$http.post("org!saveOrg.action",{org:$scope.org})
			.success(function(result) {
				$scope.msg=result.name;
				if(result.code===0){
					$scope.loadOrg();
				}else if(result.code===400){
					$window.location.href="../login";
				}
			});
		}
	};

	$scope.certification=function(){
		if($scope.notEdit){
			$http.post("org!requestCertification.action",{org:$scope.org})
				.success(function(result) {
					$scope.msg=result.name;
					if(result.code===0){
						$timeout(function(){
							$state.reload();
						},1500);
					}else if(result.code===400){
						$window.location.href="../login";
					}
				});
		}
	};
	
	$scope.uploadFile=function(elmt,fileType,dataType){
		$scope.msg="";
		
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange=function(){
			if(xhr.readyState===4 && xhr.status===200){
				var fm=JSON.parse(xhr.response);
				if(fm.code===0){
					$scope.$apply(function(){
						if(dataType==21){
							$scope.org.logo_file_id=fm.data.file_id[0];
						}else if(dataType==22){
							$scope.org.scan_file_ids=fm.data.file_id.join(",");
						}
					});
				}else{
					$scope.$apply(function(){
						$scope.msg=fm.name;
					});
				}
			}
		};
		
		xhr.open("POST", "common!uploadFiles.action", true);
		
//		if(typeof(FormData)=="undefined"){
//			var formData = new FormDataCompatibility();
//		}else{
			var formData = new FormData();
//		}
		
		var fs = elmt.prop("files");
		for(var i=0;i<fs.length;i++){
			var f=fs[i];
			formData.append("file", f);
		}
		formData.append("fileType",fileType);
		
//		if(typeof(FormData)=="undefined"){
//			formData.setContentTypeHeader(xhr);
//			xhr.sendAsBinary(formData.buildBody());
//		}else{
			xhr.send(formData);
//		}
	};

		initProp();
	$scope.loadOrg();
	$scope.initPublicStatus();
})
.directive('uploadChange', function() {
  return {
    restrict: 'A',
    scope:{
    	change:'='
    },
    link: function (scope, element, attrs) {
    	var onChangeHandler=scope.change;
    	element.bind('change',function(){
    		onChangeHandler(element,attrs.fileType,attrs.fileBind);
    	});
    }
  };
  
})
.controller("orgUserCtrl",function($scope,$http,$vsDialogs){
	$scope.users=[];
//	$scope.userName="";
//	$scope.mobile="";
	$scope.msg="";
	$scope.query={
		userName:"",
		mobile:"",
		total:0,
		pageIdx:1,
		pageSize:10,
	};
	
	$scope.template={url:"org/org_user_template.html"};
	
	var userStatus;
	$scope.loadUser=function(ust){
		if(typeof ust !=="undefined"){
			userStatus=ust;
		}
		
		var parm = {
			pageIdx : $scope.query.pageIdx,
			pageSize : $scope.query.pageSize,
			userName:$scope.query.userName,
			mobile:$scope.query.mobile,
			oumStatus:userStatus
		};
		$http.post("org!queryUserListByOrg.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.users=result.data.list;
				$scope.query.total=result.data.total;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.deleteUser=function(id){
		$scope.msg="";

		$vsDialogs.showConfirmDialog("是否确定删除该用户？",function(){
			$http.post("user!kickOutUser.action",{userId:id})
				.success(function(result) {
					if(result.code===0){
						$scope.loadUser();
					}else{
						$scope.msg=result.name;
					}
				});
		});
	};
	
	$scope.tab={isActive:false};
	$scope.$watch("diagnosisCount.joiningUserCount", function() {
		$scope.tab.isActive=true;
	});
})
.controller("orgUserMgrCtrl",function($scope,$http,$state,$stateParams,$timeout){
	$scope.user={};
	$scope.permission=[];
	$scope.ups=[];
	$scope.isApproved=$stateParams.flag=="A";
	$scope.note="";
	$scope.msg="";
	
	$scope.ispass=null;
	
	$scope.loadUser=function(uid){
		$http.post("user!queryUserById.action",{userId:uid})
		.success(function(result) {
			if(result.code===0){
				$scope.user=result.data.user;
				angular.forEach($scope.user.permission, function(value, key) {
					$scope.ups.push(value.code);
				})
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.ffunc=function(p){
		if (p) {
			
		}
		return u.type.code==3
	};
	
	$scope.saveUser=function(){
		var parm=null,url="";
		
		if($scope.isApproved){
			url="user!verityUser.action";
			parm={
				userId:$scope.user.id,
				ups:JSON.stringify($scope.ups),
				note:$scope.note,
				isPass:$scope.ispass
			};
		}else{
			url="user!conferPowerToUser.action";
			parm={
				userId:$scope.user.id,
				ups:JSON.stringify($scope.ups)
			};
		}
		
		$http.post(url,parm)
		.success(function(result) {
			$scope.msg=result.name;
			if(result.code===0){
				$timeout(function(){
					if($scope.ispass==null || $scope.ispass==true){
						$state.go("org.userMgr",{userId:$scope.user.id,flag:'V'});
					}else{
						$state.go("org.users");
					}
				},1500);
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$timeout(function(){$scope.msg="";},2000);
			}
		});
	};
	
	$scope.loadUserPermission=function(){
		$http.post("user!queryUserPersission.action")
		.success(function(result) {
			if(result.code===0){
				$scope.permission=result.data.user_perms;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.loadUser($stateParams.userId);
	$scope.loadUserPermission();
})
.controller("addOrgFriendCtrl",function($scope,$http,$uibModal,$timeout,$state,$requestCreateOrgRelationModal){
	$scope.searchType=[{code:1,name:"按机构名称"},{code:2,name:"按机构编号"}];
	$scope.st=$scope.searchType[0];
	$scope.orgs=[];
	$scope.msg="";
	$scope.queryStr="";
	
	$scope.total=0;
	$scope.pageIdx=1;
	$scope.pageSize=10;
	
	$scope.setSearchType=function(t){
		if(t){
			$scope.st=t;
		}
	};
	
	$scope.searchOrg=function(){
		$scope.msg="";
		var parm={
			pageIdx:$scope.pageIdx,
			pageSize:$scope.pageSize,
			isJoin:2
		};
		if($scope.st.code===2){
			parm.orgCode=parseInt($scope.queryStr);
		}else{
			parm.orgName=$scope.queryStr;
		}
		
		$http.post("org!queryOrgListByNameOrCode.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.orgs=result.data.list;
				$scope.total=result.data.total;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.selectReportType=function(id){
		$requestCreateOrgRelationModal.open({id:id},function(result){
			$state.go("org.friends.auditing");
		});
	};
	
}).controller("friendReportCtrl",function($scope,$http,$uibModalInstance,relation){
	$scope.orgs=relation?[{id:relation.org_id,name:relation.org_name},{id:relation.friend_id,name:relation.friend_name}]:[];
	$scope.config={};
	
	$scope.loadRelationConfig=function(){
		if(relation){
			$http.post("org!queryRelationConfig.action",{orgId:relation.friend_id})
			.success(function(result) {
				if(result.code===0){
					if(result.data.config){
						$scope.config=result.data.config;
					}else{
						$scope.config.relation_id=relation.id;
						$scope.config.org_id=relation.org_id;
					}
				}
			});
		}
	};

	$scope.ok=function(){
		if(relation){
			var msg={};
			$http.post("org!saveRelationConfig.action",{config:$scope.config})
			.success(function(result) {
//				if(result.code===0){
//				}else if(result.code===400){
//					$window.location.href="../login";
//				}else{
//				}
				msg={code:result.code,name:result.name};
				$uibModalInstance.close(msg);
			});
		}
	};
	
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
	
	$scope.loadRelationConfig();
})
.controller("orgAccountCtrl",function($scope,$http,$state,$timeout){
	$scope.msg="";
	$scope.account={};
	$scope.trans={money:0};
	$scope.records={pageIdx:1,total:0};
	$scope.req={pageIdx:1,total:0};
	
	$scope.loadAccount=function(){
		$http.post("finance!queryAccountByOrgid.action")
		.success(function(result) {
			if(result.code===0){
				$scope.account=result.data.account;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.accountRequest=function(type){
		$scope.msg="";
		if(!isNaN(parseFloat($scope.trans.money)) && isFinite($scope.trans.money) && $scope.trans.money>0){
			var parm={price:$scope.trans.money,type:type};
			$http.post("finance!commitFinacelRequest.action",parm)
			.success(function(result) {
				$scope.msg=result.name;
				if(result.code===0){
					$timeout(function(){
						$state.reload();
					},1500);
				}else if(result.code===400){
					$window.location.href="../login";
				}
			});
		}else{
			$scope.msg="请输入正确金额！";
		}
	};
	
	$scope.loadAccountLog=function(){
		$scope.msg="";
		
		var parm={
			spu:{currPage:$scope.records.pageIdx,perPageCount:10}
		};
		
		$http.post("finance!searchAccountRecord.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.records.rows=result.data.listFinanceRecord;
				$scope.records.total=result.data.total;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
		
	};
	
	$scope.loadRequest=function(){
		$scope.msg="";
		
		var parm={
			spu:{currPage:$scope.req.pageIdx,perPageCount:10}
		};
		
		$http.post("finance!searchRequest.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.req.rows=result.data.listFinanceRequest;
				$scope.req.total=result.data.total;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.cancelReq=function(id){
		$http.post("finance!deleteFinacelRequest.action",{finacalRequestId:id})
		.success(function(result) {
			if(result.code===0){
				$scope.loadRequest();
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
});