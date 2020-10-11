angular.module("user", ['ui.bootstrap','ui.router',"yingtai.common",'diy.dialog'])
.config(function($stateProvider,$urlRouterProvider){
	$stateProvider
//		.state("user",{
//			url:"/user",
//			abstract: true,
//			template: "<ui-view/>"
//		})
		.state("userInfo",{
			url:"/userInfo",
			templateUrl: 'user.info.html',
			controller:"userCtrl"
		}).state("loginName",{
			url:"/userInfo/:name",
			templateUrl: "user.loginName.html",
			controller:"loginNameCtrl"
		})
		.state("myOrg",{
			abstract: true,
			url:"/myOrg",
			template: "<ui-view />",
			redirectTo: 'myOrg.info'
		})
		.state("myOrg.list",{
			url:"/list",
			templateUrl:"org.list.html",
			controller:"orgCtrl"
		})
		.state("myOrg.info",{
			url:"/info/:id",
			templateUrl:"org.info.html",
			controller:"orgInfoCtrl"
		})
		.state("myOrg.join",{
			url:"/join",
			templateUrl: 'org.join.html',
			controller:"orgJoinCtrl"
		})
		.state("mdPwd",{
			url:"/pwd",
			templateUrl: "user.pwd.html",
			controller:"pwdCtrl"
		})
		.state("logout",{
			url:"/logout",
			controller: function($http,$window){
				$http.post("user!logout.action")
				.success(function(data, status, headers, config) {
					if(data.code===0){
						$window.location.href = '../login';
					}
				});
			}
		})
		.state("orgDetail",{
			url:"/detail/:id",
			templateUrl:"org.desc.html",
			controller:"orgDetailCtrl"
		}).state("userConfig",{
			url:"/userConfig",
			templateUrl:"user.config.html",
			controller:"userConfigCtrl"
		});
	
	  $urlRouterProvider.otherwise(function ($injector, $location) {
		  if($location.path().indexOf("/myOrg")!==-1){
			  return "/myOrg/info";
		  }else{
			  return "/userInfo";
		  }
	  });
})

.service("isValidaUserService", function() {
	this.isValida=true;
	this.setIsValida=function(isValida){
		this.isValida=isValida;
	};
	this.getIsValida=function(){
		return this.isValida;
	};
	
}).filter("subStr",function(){
	return function(str,length){
		if(!str) return "";
		if(str.length<=length) return str;
		str=str.substr(0,length);
		return str+" ...";
	};
	
}).controller("userConfigCtrl",function($scope,$http,$msgDialog){
	$scope.userConfig=null;
	$scope.initData=function(){
		$http.post("user!takePersonalConfig.action").success(function(data){
			if (data.code==0) {
				$scope.userConfig=data.data.userConfig;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.savePersonalConfig=function(data){
		$http.post("user!savePersonalConfig.action",{
			userConfig:$scope.userConfig
		}).success(function(data){
			if (data.code==0) {
				$msgDialog.showMessage("保存成功！");
				$scope.userConfig=data.data.userConfig;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("userCtrl",function($scope,$http,$state,$window,$timeout,isValidaUserService,$filter){
	$scope.photoUrl="../image/user_photo.jpg";
	
	$scope.msg="";
	$scope.adt={};
	$scope.UserType=[];
	$scope.Gender=[];
	$scope.utt="请选择";
	$scope.isDoctor=false;
	$scope.notEdit=false;
	$scope.isGuest=true;
	$scope.is_not_pass=null;
	
	$scope.chk={
		hasPC:false,
		hasAC:false
	};
	
	$scope.doc_grade={};
	
	$scope.user=null;
	
	$scope.initUserType=function(){
		$http.post("common!queryUserType.action")
		.success(function(result) {
			$scope.UserType=result.data.user_type;
		});
	};
	
	$scope.initUserGender=function(){
		$http.post("common!queryUserGender.action")
		.success(function(result) {
			$scope.Gender=result.data.user_gender;
		});
	};
	
	$scope.initUser=function(){
		$http.post("user!queryUserByPassport.action")
		.success(function(result) {
			if(result.code===0){
				$scope.user=result.data.user;
				$scope.user.birthday=result.data.user.birthday!=null?Json2date(result.data.user.birthday).format("yyyy-MM-dd"):"";
				$scope.user.startwork_time=result.data.user.startwork_time!=null?Json2date(result.data.user.startwork_time).format("yyyy-MM-dd"):"";
				$scope.notEdit=result.data.is_approving;
				$scope.isGuest=result.data.is_guest;
				$scope.is_not_pass=result.data.is_not_pass;
				
				if(result.data.is_not_pass){
					$scope.adt.type="danger";
					$scope.adt.txt="用户审核未通过！"+($scope.user.note?" 原因："+$scope.user.note:"");
				}else if($scope.notEdit){
					$scope.adt.type="warning";
					$scope.adt.txt="请稍候 ,用户正在审核中...";
				}
				if($scope.user.type){
					$scope.isDoctor=$scope.user.type.code===3;
				}
				if($scope.user.device_opreator_id>0){
					$scope.chk.hasAC=true;
				}
				if($scope.user.qualification_id>0){
					$scope.chk.hasPC=true;
				}
				if($scope.user.grade){
					var names=$scope.user.grade.split(" ");
					if(names){
						names=names.filter(function(str){ return str!==""});
						$scope.doc_grade.a={name:names[0]};
						$scope.doc_grade.b={name:names[1]};
					}
				}
				isValidaUserService.setIsValida(!$scope.notEdit&&!$scope.isGuest&&!result.data.is_not_pass);
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.$watch("user.type",function(a,b){
		if(a){
			$scope.isDoctor=a.code===3;
			if(!b) changeSelectType();
		}else{
			$scope.detailList=[];
		}
	},true);
	
	var changeSelectType=function(){
		$scope.detailList=[];
		$http.post("common!queryUserTypeDetail.action")
		.success(function(data){
			if (data.code===0) {
				$scope.detailList=data.data.list;
			}
		});
	};
	
	var getGrade=function(){
		var grade="";
		if($scope.doc_grade){
			angular.forEach($scope.doc_grade,function(v,k){
				if(v) grade+=" "+v.name
			});
		}
		return grade;
	}
	
	$scope.saveUser=function(){
		$scope.msg="";
		$scope.user.grade=getGrade();
		$http.post("user!saveUser.action", {userInfo:JSON.stringify($scope.user)})
		.success(function(result, status, headers, config) {
			$scope.msg=result.name;
			if(result.code===0){
				$timeout(function(){
					$scope.msg="";
					$state.reload();
				},1500);
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
						if(dataType==1){
							$scope.user.photo_file_id=fm.data.file_id[0];
						}else if(dataType==2){
							$scope.user.identify_file_id=fm.data.file_id[0];
						}else if(dataType==3){
							$scope.user.scan_file_ids=fm.data.file_id.join(",");
						}else if(dataType==4){
							$scope.user.sign_file_id=fm.data.file_id[0];
						}else if(dataType==5){
							$scope.user.qualification_id=fm.data.file_id[0];
						}else if(dataType==6){
							$scope.user.device_opreator_id=fm.data.file_id[0];
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
	
	$scope.initUserType();
	$scope.initUserGender();
	$scope.initUser();
})
.controller("orgCtrl",function($scope,$http,$window,$state,$selectDialog,$msgDialog,isValidaUserService){
	$scope.msg="";
	$scope.orgs=[];
	$scope.org={};
	$scope.isValidaUser=isValidaUserService.getIsValida();
	
	$scope.initOrg=function(){
		$http.post("org!queryMyOrgList.action")
		.success(function(result) {
			if(result.code===0){
				$scope.orgs=result.data.list;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	$scope.quitOrg=function(orgId){
		$selectDialog.showMessage("退出前请完结在此机构下的所有工作，你确定要退出本机构吗？",function(){
			$http.post("user!quitOrg.action",{orgId:orgId})
			.success(function(data){
				if (data.code==0) {
					$scope.initOrg();
				}else {
					$msgDialog.showMessage(data.name);
				}
			});
		});
	};
	$scope.entryOrg=function(id){
		$http.post("user!entryOrg.action",{orgId:id})
		.success(function(result) {
			if(result.code===0){
				$window.location.href="../sysmain/index.html";
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	}
	
	if(isValidaUserService.getIsValida())
		$scope.initOrg();
})
.controller("orgInfoCtrl",function($scope,$http,$window,$state,$timeout,$uibModal,$stateParams){
	$scope.msg="";
	$scope.types=[];
	$scope.levels=[];
	$scope.default_ol=" 请选择 ";
	
	$scope.msg="";
	$scope.notEdit=false;
	
	$scope.org={};

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
				$scope.msg=result.name;
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

	$scope.setLevel=function(l){
		$scope.default_ol=l.value;
		$scope.org.levels=l.id;
		$scope.org.level_name=l.value;
	};
	
	var loadOrg=function(id){
		$http.post("org!queryMyOrgInfoById.action",{orgId:id})
		.success(function(result) {
			if(result.code===0){
				$scope.org=result.data.org;
				$scope.default_ol=result.data.org.level_name;
				$scope.org.property_name=$scope.org.property.name;
				if($scope.org.org_property===11){
					initLevel(3);
				}else if($scope.org.org_property===12){
					initLevel(23);
				}

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
	
	$scope.saveOrg=function(){
		var modalInstance = $uibModal.open({
	      templateUrl: 'orgView.html',
	      controller: 'orgViewCtrl',
	      resolve:{org:$scope.org}
	    });
		
		modalInstance.result.then(function(result){
			if(result){
				if(result.code===0){
					$state.go("myOrg.list");
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			}
		});
	};

	$scope.resubmit=function(){
		$http.post("org!reSubmit.action",{org:$scope.org})
			.success(function(result) {
				if(result){
					if(result.code===0){
						$state.go("myOrg.list");
					}else if(result.code===400){
						$window.location.href="../login";
					}else{
						$scope.msg=result.name;
					}
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

	if($stateParams.id>0){
		loadOrg($stateParams.id);
	}

	$scope.initType();
})
.controller("orgViewCtrl",function($scope,$http,$uibModalInstance,org){
	$scope.org=org;
	$scope.publicStatus=[];
	
	$scope.initPublicStatus=function(){
		$http.post("common!queryPublicStatus.action")
		.success(function(result) {
			if(result.code===0)
				$scope.publicStatus=result.data.status;
				
			$scope.org.is_public=12;
		});
	};
	
	$scope.ok=function(){
		if(org){
			var msg={};
			$http.post("org!saveOrg.action",{org:$scope.org})
			.success(function(result) {
				msg={code:result.code,name:result.name};
				$uibModalInstance.close(msg);
			});
		}
	};
	
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
	
	$scope.initPublicStatus();
})
	
.controller("orgJoinCtrl",function($scope,$http,$window,$state,$timeout){
	$scope.queryStr="";
	$scope.searchType=[{code:1,name:"按机构名称"},{code:2,name:"按机构编号"}];
	$scope.st=$scope.searchType[0];
	$scope.orgs=[];
	$scope.msg="";
	
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
			isJoin:1
		};
		if($scope.st.code===2){
			parm.orgCode=$scope.queryStr;
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
	
	$scope.requestJoin=function(id){
		$scope.msg="";
		if(id!==0){
			$http.post("user!requestJoinOrg.action",{orgId:id})
			.success(function(result) {
				if(result.code===0){
					$scope.msg="已发送请求，请耐心等候对方审核...";
					$timeout(function(){
						$state.go("myOrg.list");
					},1000);
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
		}else{
			$scope.msg="无效的机构";
		}
	}
})
.controller("loginNameCtrl",function($scope,$http,$state,$stateParams,$timeout,$interval){
	$scope.mobile="";
	$scope.email="";
	$scope.code="";
	$scope.isMobile=false;
	$scope.isEmail=false;
	$scope.isCheck=true;
	$scope.msg="";
	$scope.title="信息变更";
	$scope.codetxt="获取验证码";
	$scope.wait=false;
	var t;
	
	if($stateParams.name){
		if($stateParams.name==="mobile"){
			$scope.isMobile=true;
			$scope.title="绑定手机";
		}else if($stateParams.name==="email"){
			$scope.isEmail=true;
			$scope.title="绑定邮箱";
		}else{
			$state.go("userInfo");
		}
	}
	
	$scope.getCode=function(){
		$scope.msg="";
		var param={
			loginName:$scope.mobile?$scope.mobile:$scope.email?$scope.email:"",
			vcType:2
		};
		
		$http.post("user!reqValidateCode.action", param)
			.success(function(result) {
				if(result.code!==0){
					$scope.msg=result.name;
				}else{
					var time=60;
					$scope.codetxt="等待"+(time)+"秒";
					$scope.wait=true;
					$scope.msg="验证码已成功发送，请注意查收！";
					t=$interval(function(){
						if(time===0){
							$interval.cancel(t);
							$scope.codetxt="获取验证码";
							$scope.msg="";
							$scope.wait=false;
						}else{
							$scope.codetxt="等待"+(--time)+"秒";
						}
					},1000);
				}
			});
	}
	
	$scope.modifyLoginName=function(){
		$scope.msg="";
		var param={
			loginName:$scope.mobile?$scope.mobile:$scope.email?$scope.email:"",
			validateCode:$scope.code
		};
		
		$http.post("user!modifyMobileOrEmail.action", param)
			.success(function(result) {
				if(result.code===0){
					$scope.msg="修改成功！";
					$timeout(function(){
						$state.go("userInfo");
					},1000);
				}else{
					$scope.msg=result.name;
				}
			});
	}
	
	$scope.checkLoginName=function(loginName){
		$scope.msg="";
		if(loginName!=null&&loginName!=""){
			var param={loginName:loginName};
			
			$http.post("user!isExistsLogName.action", param)
			.success(function(result) {
				if(result.code!==0){
					$scope.msg=result.name;
				}else{
					$scope.isCheck=result.data.is_exists;
					if($scope.isCheck){
						$scope.msg="帐号已存在";
					}
				}
			});
		}
	};
	
	$scope.$on('$destroy', function() {
		if (angular.isDefined(t)) {
		    $interval.cancel(t);
		    t = undefined;
		 }
	});
	
})
.controller("pwdCtrl",function($scope,$http,$state,$timeout){
	$scope.oldPwd="";
	$scope.newPwd="";
	$scope.renewPwd="";
	$scope.msg="";
	$scope.isEquals=false;
	
	$scope.modifyPwd=function(){
		var key="";
		var param={
				pwd:$scope.oldPwd,//encryptByDES($scope.oldPwd,$scope.oldPwd),
				newPwd:encryptByDES($scope.newPwd,$scope.oldPwd)
			};
			
		$http.post("user!modifyLoginPwd.action", param)
			.success(function(result) {
				
				if(result.code===0){
					$scope.msg="修改成功！";
					$timeout(function(){
						$state.go("userInfo");
					},1000);
				}else{
					$scope.msg=result.name;
				}
			});
	}
	
	$scope.checkNewPwd=function(){
		if($scope.newPwd!=""&&$scope.renewPwd!=""){
			$scope.isEquals=$scope.newPwd==$scope.renewPwd;
			$scope.msg=$scope.isEquals?"":"密码输入不一致！";
		}
	};
})
.controller("activeHeaderCtrl",function($scope, $location){
	$scope.isActive = function (viewLocation) { 
        return $location.path().indexOf(viewLocation)!==-1;
    };
    
})
.controller("orgDetailCtrl",function($scope,$stateParams,$state,$http){
	$scope.org={};
	$scope.userList=[];
	
	if($stateParams.id){
		$http.post("org!queryOrgLite.action", {orgId:$stateParams.id})
		.success(function(result) {
			if(result.code===0){
				$scope.org=result.data.org;
				if($scope.org.is_public===12){
					getUserList();
				}
			}
		});
	}
	
	var getUserList=function(){
		$http.post("user!queryUserLiteByOrg.action", {orgId:$stateParams.id,userStatus:2,oumStatus:1})
		.success(function(result) {
			if(result.code===0)
				$scope.userList=result.data.list;
		});
	};
	
	
	
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
  
});