angular.module("org", [ "ui.router", "ui.bootstrap","checklist-model","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("org", {
		url : "/org",
		templateUrl : "org/org_list.html",
		controller : "orgCtrl"
	}).state("orgInfo",{
		url:"/orgInfo/:action/:id",
		templateUrl: "org/org_info.html",
		controller:"orgInfoCtrl"
	}).state("account",{
		url:"/account",
		templateUrl:"org/org_account.html",
		controller:"orgAccountCtrl"
	}).state("record",{
		url:"/record/:id",
		templateUrl:"org/account_record.html",
		controller:"accountRecordCtrl"
	}).state("orgConfig",{
		url:"/orgConfig/:orgId",
		templateUrl:"org/org.config.html",
		controller:"orgConfigCtrl"
	}).state("newOrgConfig",{
		url:"/newOrgConfig/:orgId",
		templateUrl:"org/org.config.new.html",
		controller:"newOrgConfigCtrl"
	}).state("modifyOrgAffix",{
		url:"/modifyOrgAffix/:org_Id",
		templateUrl:"org/org.config.new.html",
		controller:"modifyOrgAffixCtrl"
	}).state("orgConfigDetail",{
		url : "/orgConfigDetail/:orgId",
		templateUrl : "org/org.config.detail.html",
		controller : "orgConfigDetailCtrl"
	}).state("orgDefaultConfig",{
		url:"/orgDefaultConfig",
		templateUrl:"org/org.config.default.html",
		controller:"orgDefaultConfig"
	}).state("newOrg",{
		url:"/newOrg/:id",
		templateUrl:"org/org.new.html",
		controller:"newOrgCtrl"
	}).state("orgUsers",{
		url:"/users/:id",
		templateUrl:"org/org.userlist.html",
		controller:"orgUserCtrl"
	});

}).controller("orgDefaultConfig",function($scope,$http,$msgDialog,$window){
	$scope.orgAffix={
		id:0,
	    ip:"",
	    query_port:"",
	    dicomweb_port:"",
	    ae_code:"",
	    user_name:"",
	    password:""
	};
	$scope.initData=function(){
		$http.post("org!queryDefaultOrgAffix.action")
		.success(function(data){
			if (data.code==0) {
				if (data.data.orgAffix)
					$scope.orgAffix=data.data.orgAffix;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.saveOrgAffix=function(){
		$http.post("org!addOrUpdateDefaultOrgAffix.action",{orgAffix:$scope.orgAffix})
		.success(function(data){
			if (data.code==0) {
				$msgDialog.showMessage("操作成功！");
				$window.history.back();
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("modifyOrgAffixCtrl",function($scope,$http,$msgDialog,$window,$stateParams){
	if (!$stateParams.org_Id) {
		$msgDialog.showMessage("无效的机构ID！");
		return;
	}
	$scope.mode=2;
	$scope.listRSV = [];
	$scope.orgAffix={
		ip:"",
		port:"",
		ae_code:"",
	};
	$scope.initData=function(){
		$http.post("org!selectOrgAffixByOrgId.action",{orgId:$stateParams.org_Id})
		.success(function(data){
			if (data.code==0) {
				if (data.data && data.data.orgAffix)
					$scope.orgAffix=data.data.orgAffix;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
		$http.post("common!queryRemoteServerVersion.action")
		.success(function(data){
			if (data.code==0) {
				$scope.listRSV=data.data.listRSV;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.saveOrgAffix=function(){
		$http.post("org!saveOrgAffix.action",{orgAffix:$scope.orgAffix})
		.success(function(data){
			if (data.code==0) {
				$msgDialog.showMessage("操作成功！");
				$window.history.back();
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("newOrgConfigCtrl",function($scope,$http,$msgDialog,$window,$stateParams){
	$scope.orgAffix={
		remote_server_version:1,
		org_id:$stateParams.orgId
	};
	$scope.listRSV=[];
	$scope.initData=function(){
		$http.post("common!queryRemoteServerVersion.action")
		.success(function(data){
			if (data.code==0) {
				$scope.listRSV=data.data.listRSV;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.saveOrgAffix=function(){
		$http.post("org!addOrgAffix.action",{orgAffix:$scope.orgAffix})
		.success(function(data){
			if (data.code==0) {
				$msgDialog.showMessage("操作成功！");
				$scope.goback();
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("orgConfigDetailCtrl",function($scope,$http,$msgDialog,$window,$stateParams){
	$scope.orgAffix=null;
	$scope.listRSV=[];
	$scope.initData=function(){
		$http.post("org!queryAndBuildOrgAffix.action",{orgId:$stateParams.orgId})
		.success(function(data){
			if (data.code==0) {
				$scope.orgAffix = data.data.orgAffix;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
		$http.post("common!queryRemoteServerVersion.action")
		.success(function(data){
			if (data.code==0) {
				$scope.listRSV=data.data.listRSV;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.saveOrgAffix=function(){
		$scope.orgAffix.org_id=$stateParams.orgId;
		$http.post("org!addOrUpdateOrgAffix.action",{orgAffix:$scope.orgAffix})
		.success(function(data){
			if (data.code==0) {
				$msgDialog.showMessage("操作成功！");
				$window.history.back();
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("orgConfigCtrl",function($scope,$http,$msgDialog,$selectDialog,$stateParams){
	if (! $stateParams.orgId) {
		$msgDialog.showMessage("无效的机构ID！");
		return ;
	}
	$scope.listOrgAffix=[];
	$scope.searchOpt={
		orgId:$stateParams.orgId
	};
	$scope.deleteOrgAffix=function(orgAffixId){
		$selectDialog.showMessage("你确定要删除此机构配置吗？",function(){
			$http.post("org!deleteOrgConfigById.action",{orgAffixId:orgAffixId})
			.success(function(data){
				if (data.code==0) {
					$msgDialog.showMessage("删除成功！");
					$scope.queryOrgConfig();
				}else {
					$msgDialog.showMessage(data.name);
				}
			});
		});
	};
	$scope.queryOrgConfig=function(){
		$http.post("org!queryOrgConfigByOrgId.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listOrgAffix=data.data.listOrgAffix;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
})
.filter("subStr",function(){
	return function(str,length){
		if(!str) return "";
		if(str.length<=length) return str;
		str=str.substr(0,length);
		return str+" ...";
	};
	
})
.controller("orgCtrl",function($scope,$http,$selectDialog,$msgDialog,$stateParams){
	$scope.orgs = [];
//	$scope.orgName="";
//	$scope.orgCode=null;
	$scope.msg="";
	$scope.query={
		orgName:"",
		orgCode:"",
		total:0,
		pageIdx:1,
		pageSize:10,
		org_status:1
	};
	
	$scope.enableOrg=function(u,enable){
		if (!u)return;
		var eo=function(){
			$http.post("org!enableOrg.action",{orgId:u.org_id,enable:enable})
			.success(function(data){
				$msgDialog.showMessage(data.name);
				if (data.code==0) {
					$scope.loadOrgList($scope.query.org_status);
				}
			});
		};
		if (!enable) {
			$selectDialog.showMessage("你确定要禁用此机构吗？禁用之后用户不可登陆此机构，且其他机构不可看到！",eo);
		}else {
			eo();
		}
	};
	
	$scope.removeOrg=function(org){
		if (!org)return;
		$selectDialog.showMessage("请确认，机构删除之后将不可恢复，且其所有合作机构关系将断裂！",function(){
			$http.post("org!removeOrgById.action",{orgId:org.org_id})
			.success(function(data){
				$msgDialog.showMessage(data.name);
			});
		});
	};
	
	$scope.template={url:"org/org_list_template.html"};
	
	$scope.loadOrgList=function(ost){
		if(typeof ost !=="undefined"){
			$scope.query.org_status=ost;
		}
		
		$scope.msg="";
		$scope.orgs=[];
		var parm={pageIdx:$scope.query.pageIdx,
				pageSize:$scope.query.pageSize,
				orgName:$scope.query.orgName,
				orgCode:$scope.query.orgCode,
				org_status:$scope.query.org_status
			};
		
		$http.post("org!queryOrgListByAdmin.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.orgs=result.data.list;
				$scope.query.total=result.data.total;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.loadChangeList=function(){
		$scope.query.org_status=null;
		$scope.msg="";
		$scope.orgs=[];
		var parm={pageIdx:$scope.query.pageIdx,
				pageSize:$scope.query.pageSize,
				orgName:$scope.query.orgName,
				orgCode:$scope.query.orgCode
			};
		
		$http.post("org!queryOrgChangeList.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.orgs=result.data.list;
				$scope.query.total=result.data.total;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.tab={isActive:false,isActive2:false};
	$scope.$watch("waitCount.a_org_count", function() {
		if($scope.tab.isActive)
			$scope.loadOrgList(11);
	});
	
	$scope.$watch("waitCount.change_org_count", function() {
		if($scope.tab.isActive2)
			$scope.loadChangeList();
	});
	
	$scope.$watch("query.org_status", function(oldStatus,newStatus) {
		if (oldStatus===newStatus)
			return;
		$scope.query.total=0;
		$scope.query.pageIdx=1;
		$scope.query.pageSize=10;
	});
	
})
.controller("orgInfoCtrl",function($scope,$http,$state,$timeout,$stateParams,$window){
	$scope.title="机构信息";
	$scope.is_approved=false;
	$scope.is_change=false;
	$scope.canOpt=false;
//	$scope.is_change_view=false;
	$scope.msg="";
	$scope.ispass=null;
	$scope.org=null;
	$scope.note="";
	$scope.ot=[];
	//$scope.ots=[];

	
	if($stateParams.action==="a"){
		$scope.title="审核机构";
		$scope.is_approved=true;
	}else if($stateParams.action==="ac"){
		$scope.title="机构认证";
		$scope.is_change=true;
		$scope.ispass=true;
	}else if($stateParams.action==="vc"){
		$scope.is_change=true;
		$scope.ispass=false;
	}
	
//	$scope.loadOrgType=function(){
//		$http.post("common!queryOrgType.action")
//		.success(function(result) {
//			if(result.code===0){
//				$scope.ots=result.data.org_types;
//			}else if(result.code===400){
//				$window.location.href="../login";
//			}else{
//				$scope.msg=result.name;
//			}
//		});
//	};
	
	$scope.approvedOrg=function(){
		$scope.msg="";
		var parm={
			orgId:$scope.org.id,
			isPass:$scope.ispass,
			ops:JSON.stringify($scope.ot),
			note:$scope.note
		};
		$http.post("org!approvedOrg.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.msg="审核成功！";
				$timeout(function(){
					$state.go("orgInfo",{action:'v',id:$scope.org.id});
				},1500);
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.saveOrgPrms=function(){
		$scope.msg="";
		var parm={
				orgId:$scope.org.id,
				ops:JSON.stringify($scope.ot)
			};
		$http.post("org!modifyOrgPermis.action",parm)
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
	};
	
	$scope.loadOrg=function(){
		$scope.msg="";
		$scope.ot=[];
		if($stateParams.id!==""&&!isNaN(parseFloat($stateParams.id)) && isFinite($stateParams.id)){
			$http.post("org!queryOrgByAdmin.action",{orgId:$stateParams.id})
			.success(function(result) {
				if(result.code===0){
					$scope.org=result.data.org;
					$scope.ispass=$scope.org.status===1?true:$scope.org.status===12?false:null;
					$scope.note=$scope.org.note;
					angular.forEach($scope.org.permissionList, function(value, key) {
						$scope.ot.push(value.code);
					});
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
		}else{
			$scope.msg="无效的机构";
			$timeout(function(){
				$state.go("org");
			},1500);
		}
	};
	
	$scope.loadOrgChange=function(){
		if($stateParams.id!==""&&!isNaN(parseFloat($stateParams.id)) && isFinite($stateParams.id)){
			$http.post("org!queryOrgChangeByOrg.action",{orgId:$stateParams.id})
			.success(function(result) {
				if(result.code===0){
					$scope.org=result.data.org;
					$scope.ispass=$scope.org.change_status===1?true:$scope.org.change_status===12?false:null;
					$scope.note=$scope.org.note;
					$scope.canOpt=$scope.org.change_status===11?true:false;
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
		}else{
			$scope.msg="无效的机构";
			$timeout(function(){
				$state.go("org");
			},1500);
		}
	};
	
	$scope.confirmChange=function(){
		$scope.msg="";
		var parm={
			orgId:$scope.org.id,
			isPass:$scope.ispass,
			note:$scope.note
		};
		$http.post("org!confirmChange.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.msg="审核成功！";
				$timeout(function(){
					$state.go("orgInfo",{action:'vc',id:$stateParams.id});
				},1500);
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	if($scope.is_change){
		$scope.loadOrgChange();
	}else{
		$scope.loadOrg();
//		$scope.loadOrgType();
	}
})
.controller("orgAccountCtrl",function($scope,$http,$selectDialog){
	$scope.msg="";
	$scope.accounts={orgName:"",orgCode:"",pageIdx:1,total:0};
	$scope.reqs={orgName:"",orgCode:"",pageIdx:1,total:0};
	
	$scope.loadAccount=function(){
		var parm={
				orgName:$scope.accounts.orgName,
				orgCode:$scope.accounts.orgCode,
				spu:{currPage:$scope.accounts.pageIdx,perPageCount:10}
			};
		
		$http.post("finance!searchAccount.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.accounts.rows=result.data.listFinanceAccount;
				$scope.accounts.total=result.data.total;
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
				orgName:$scope.reqs.orgName,
				orgCode:$scope.reqs.orgCode,
				spu:{currPage:$scope.reqs.pageIdx,perPageCount:10}
			};
		
		$http.post("finance!searchRequest.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.reqs.rows=result.data.listFinanceRequest;
				$scope.reqs.total=result.data.total;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.confirmReq=function(id,type){
		$scope.msg="";
		var url="";
		if(type&&type.code===1){
			url="finance!recharge.action";
		}else if(type&&type.code===2){
			url="finance!takeCash.action";
		}else{
			$scope.msg="未知的操作";
			return;
		}
		
		$selectDialog.showMessage('确定同意该申请?',function() {
			$http.post(url,{finacalRequestId:id})
			.success(function(result) {
				if(result.code===0){
					$scope.loadRequest();
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
        });
	};
	
	$scope.rejectReq=function(id){
		$scope.msg="";
		$selectDialog.showMessage('确定拒绝该申请?',function() {
			$http.post("finance!rejectFinacelRequest.action",{finacalRequestId:id})
			.success(function(result) {
				if(result.code===0){
					$scope.loadRequest();
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
        });
	};
	
	$scope.freeze=function(id,isFreeze){
		$scope.msg="";
		var url="";
		var cfmtxt="";
		
		if(isFreeze){
			url="finance!freezeAccount.action";
			cfmtxt="是否确定冻结该账户？";
		}else{
			url="finance!unfreezeAccount.action";
			cfmtxt="是否确定解冻该账户？";
		}
		
		$selectDialog.showMessage(cfmtxt,function() {
			$http.post(url,{orgId:id})
			.success(function(result) {
				if(result.code===0){
					$scope.loadAccount();
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
        });
		
	};
	
	$scope.loadAccount();
	
})
.controller("accountRecordCtrl",function($scope,$http,$stateParams,$timeout,$state){
	$scope.msg="";
	$scope.records={pageIdx:1,total:0};
	
	$scope.loadRecordByOrg=function(){
		if(!isNaN(parseFloat($stateParams.id)) && isFinite($stateParams.id)){
			var parm={
					orgId:$stateParams.id,
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
		}else{
			$scope.msg="无效的机构";
			$timeout(function(){
				$state.go("account");
			},1500);
		}
	};
	
	$scope.loadRecordByOrg();
	
})
.controller("newOrgCtrl",function($scope,$http,$compile,$timeout,$state,$uibModal,$msgDialog,$stateParams){
	$scope.msg="";
	//$scope.ots=[];
	$scope.ot=[];
	$scope.org={};
	$scope.userList=[];
	$scope.isShowInfo=true;
	$scope.isShowUser=false;
	$scope.title="新增机构";
	
	$scope.initOrg=function(id){
		$http.post("org!queryOrgByAdmin.action",{orgId:id})
		.success(function(result) {
			if(result.code===0){
				var o=result.data.org;
				$scope.org.id=o.id;
				$scope.org.mgr={};
				$scope.org.mgr.user_name=o.creator_name;
				$scope.org.mgr.mobile=o.v_creator_mobile;
				$scope.org.mgr.type=o.v_creator_type;
				$scope.title=o.org_name+" - 增加用户";
			}
		});
	}
	
	$scope.initType=function(){
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

	$scope.changType=function(type){
		$scope.default_ol=" 请选择 ";
		$scope.org.levels=null;
		$scope.org.level_name=null;
		$scope.org.property_name=type.name;
		if(type.code===11){
			initLevel(3);
		}else if(type.code===12){
			initLevel(23);
		}
	};
	
	$scope.initUserType=function(){
		$http.post("common!queryUserType.action")
		.success(function(result) {
			$scope.UserType=result.data.user_type;
		});
	};
	
	//$scope.initOrgRoleType=function(){
	//	$http.post("common!queryOrgType.action")
	//	.success(function(result) {
	//		if(result.code===0){
	//			$scope.ots=result.data.org_types;
	//		}else{
	//			$scope.msg=result.name;
	//		}
	//	});
	//};
	
	if($stateParams.id>0){
		$scope.initOrg($stateParams.id);
		$scope.isShowInfo=false;
		$scope.isShowUser=true;
	}else{
		//$scope.initOrgRoleType();
		$scope.initType();
	}
	
	$scope.initUserType();
	
	$scope.goUser=function(isGo){
		if(isGo){
			$scope.isShowInfo=false;
			$scope.isShowUser=true;
		}else{
			$scope.isShowInfo=true;
			$scope.isShowUser=false;
		}
	};
	
	$scope.batSave=function(){
		$scope.msg="";
		$scope.userList=[];
		$scope.userList=getUserList($scope);
		
		if($scope.userList && checkData()){
			var parms={
				org:$scope.org,
				ops:JSON.stringify($scope.ot),
				orgAdminUser:$scope.org.mgr,
				userList:JSON.stringify($scope.userList)
			};

			$http.post("org!batCreateUser4OrgByAdmin.action",parms)
			.success(function(result) {
				if(result.code===0){
					var allUserTmpl = angular.element(document.getElementsByTagName("add-user-tmpl"));
					clearUserList(allUserTmpl);
					$msgDialog.showMessage(result.name);
					$timeout(function(){
						$state.go("org");
					},2000);
				}else{
					$scope.msg=result.name;
				}
			});
		}else{
			$timeout(function(){
				$scope.msg="";
			},2000);
		}
		
	};
	
	$scope.addUserInfo=function(){
		var divElement = angular.element(document.querySelector('#userInfoDiv'));
		var appendHtml = $compile('<add-user-tmpl user-type="UserType" tmpl-index="tmplIndex"></add-user-tmpl>')($scope);
		divElement.append(appendHtml);
	};
	
	$scope.slctUser=function(){
		var modalInstance = $uibModal.open({
	      templateUrl: 'org/org.new.selectUser.html',
	      controller: 'slctUserCtrl'
	    });
			
		modalInstance.result.then(function (result) {
			if(result){
				$scope.org.mgr={
					id:result.user_id,
					user_name:result.user_name,
					mobile:result.mobile,
					type:result.user_type.code
				};
			}
	    });
	};
	
	$scope.uploadFile=function(elmt,fileType,dataType){
		$scope.msg="";
		
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange=function(){
			if(xhr.readyState===4 && xhr.status===200){
				var fm=JSON.parse(xhr.response);
				if(fm.code===0){
					$scope.$apply(function(){
						if(dataType==4){
							$scope.org.logo_file_id=fm.data.file_id[0];
						}else if(dataType==5){
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
		var formData = new FormData();
		var fs = elmt.prop("files");
		for(var i=0;i<fs.length;i++){
			var f=fs[i];
			formData.append("file", f);
		}
		formData.append("fileType",fileType);
		xhr.send(formData);
	};
	
	var getUserList = function() {
		var userList = [];
	    var ChildHeads = [$scope.$$childHead];
	    var currentScope;
	    while (ChildHeads.length) {
	      currentScope = ChildHeads.shift();
	      while (currentScope) {
	    	if(currentScope.hasOwnProperty("user_name")){
	    		if(!currentScope.user_name){
	    			$scope.msg="用户姓名不能为空！";
	    			return null;
	    		}
	    		if(!currentScope.mobile){
	    			$scope.msg="用户手机不能为空！";
	    			return null;
	    		}
	    		if(!currentScope.type){
	    			$scope.msg="用户类型不能为空！"
	    			return null;
	    		}
	    		
	    		userList.push({
	    			user_name:currentScope.user_name,
	    			mobile:currentScope.mobile,
	    			type:currentScope.type
	    		});
	    	}
	
	        currentScope = currentScope.$$nextSibling;
	      }
	    }
	    return userList;
	  };

	  var clearUserList = function(list) {
	    for (var i = 0,len=list.length; i < len; i++)
	      angular.element(list[i]).remove();

	    $scope.userList = [];
	    $scope.$$childHead = $scope.$new(true);
	  }
	  
	  var checkData=function(){
		if(!$scope.org.id>0){
			if(!$scope.org.org_name){
				$scope.msg="机构名称不能为空！";
				return false;
			}
			//if($scope.ot.length==0){
			//	$scope.msg="机构角色不能为空！";
			//	return false;
			//}
		}
		if(!$scope.org.mgr){
			$scope.msg="机构负责人信息不能为空";
			return false;
		}else{
			if(!$scope.org.mgr.user_name){
				$scope.msg="机构负责人姓名不能为空";
				return false;
			}
			if(!$scope.org.mgr.mobile){
				$scope.msg="机构负责人手机不能为空";
				return false;
			}
			if(!$scope.org.mgr.type){
				$scope.msg="机构负责人类型不能为空";
				return false;
			}
		}
	   return true;
	  };
	  
}).controller("slctUserCtrl",function($scope,$http,$uibModalInstance){
	$scope.query={
		userName:"",
		mobile:"",
		total:0,
		pageIdx:1,
		pageSize:10,
		userStatus:2
	};
	$scope.users=[];
	
	$scope.queryUser=function(){
		$scope.users=[];
		var parm={pageIdx:$scope.query.pageIdx,
			pageSize:$scope.query.pageSize,
			userName:$scope.query.userName,
			mobile:$scope.query.mobile,
			userStatus:$scope.query.userStatus
		};
		
		$http.post("user!queryUserListByAdmin.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.users=result.data.list;
				$scope.query.total=result.data.total;
			}else{
				$scope.users=[];
				$scope.query.total=0;
			}
		});
	};
	
	$scope.ok=function(){
		$uibModalInstance.close($scope.slctUser);
	};
	
	$scope.slctUser=null;
	
	$scope.chkChange=function(isSlcted,user){
		if($scope.slctUser){
			$scope.slctUser.isSelected=false;
			$scope.slctUser=null;
			$scope.slctUser=user;
			$scope.slctUser.isSelected=isSlcted;
		}else{
			$scope.slctUser=user;
		}
	};
	
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
	
	$scope.queryUser();

}).controller("orgUserCtrl",function($scope,$http,$stateParams){
	$scope.org={};
	$scope.userList=[];
	
	if($stateParams.id){
		$http.post("org!queryOrgLite.action", {orgId:$stateParams.id})
		.success(function(result) {
			if(result.code===0){
				$scope.org=result.data.org;
				getUserList();
			}
		});
	}
	
	var getUserList=function(){
		$http.post("user!queryUserLiteByOrg.action", {orgId:$stateParams.id})
		.success(function(result) {
			if(result.code===0)
				$scope.userList=result.data.list;
		});
	};
	
}).directive('addUserTmpl', function() {
	return {
	   restrict: "E",
	   scope: {
		   userType:'='
	   },
	   templateUrl:'userInfoTmpl.html',
	   controller:function($scope,$element,$attrs){
		  $scope.deletTmpl=function(e){
			$element.remove();
			$scope.$destroy();
		  };
	   }
	};	
});
