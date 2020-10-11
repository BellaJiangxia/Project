angular.module("adminuser", [ "ui.router", "ui.bootstrap","checklist-model","diy.dialog"])

.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider
	.state("adminuser",{
		abstract: true,
		url:"/adminuser",
		template: "<ui-view />",
		redirectTo:"adminuser.info"
	})
	.state("adminuser.info",{
		url:"/info/:action/:userId",
		templateUrl: "adminuser/adminuser.info.html",
		controller:"adminInfoCtrl"
	})
	.state("adminuser.pwd",{
		url:"/pwd",
		templateUrl:"adminuser/adminuser.pwd.html",
		controller:"adminPwdCtrl"
	})
	.state("adminuser.mgr", {
		url : "/mgr",
		templateUrl : "adminuser/adminuser.mgr.html",
		controller : "adminMgrCtrl"
	});
	
})
.filter("subStr",function(){
	return function(str,length){
		if(!str) return "";
		if(str.length<=length) return str;
		str=str.substr(0,length);
		return str+" ...";
	};
	
})
.controller("adminInfoCtrl",function($scope,$http,$state,$stateParams,$timeout,$window){
	$scope.msg="";
	$scope.title="个人资料";
	$scope.photoUrl="../image/user_photo.jpg";
	$scope.Gender=[];
	$scope.permissions=[];
	$scope.ups=[];
	
	$scope.notEdit=false;
	$scope.notAuth=true;
	
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
	
	$scope.user={};
	
	$scope.initUserGender=function(){
		$http.post("common!queryUserGender.action")
		.success(function(result) {
			$scope.Gender=result.data.user_gender;
		});
	};
	
	$scope.initUser=function(){
		$scope.msg="";
		$http.post("user!queryUserByPassport.action")
		.success(function(result) {
			if(result.code===0){
				$scope.user=result.data.user;
				angular.forEach($scope.user.permission, function(value, key) {
					$scope.ups.push(value.code);
				});
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.saveUser=function(){
		$scope.msg="";
		var parm={
				userInfo:JSON.stringify($scope.user),
				ups:JSON.stringify($scope.ups)
			};
		$http.post("user!saveAdminUser.action", parm)
		.success(function(result) {
			if(result.code===0){
				$scope.msg="保存成功！";
				$timeout(function(){
					if(result.data){
						$state.go("adminuser.info",{action:"c",userId:result.data.user_id});
					}else{
						$state.reload();
					}
				},1500);
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.authUser=function(){
		$scope.msg="";
		if($stateParams.action=="c"&&$scope.user.id!=null){
			var parm={userId:$scope.user.id,ups:JSON.stringify($scope.ups)};
			$http.post("user!conferPowerToUser.action",parm)
			.success(function(result) {
				if(result.code===0){
					$scope.msg="授权成功！";
					$timeout(function(){
						$state.reload();
					},1500);
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
		}else{
			$scope.msg="无效操作！";
		}
	};
	
	$scope.loadUserPermission=function(){
		$scope.msg="";
		$http.post("common!queryAdminPermission.action")
		.success(function(result) {
			if(result.code===0){
				$scope.permissions=result.data.admin_perms;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.loadUser=function(id){
		$http.post("user!queryUserById.action",{userId:id})
		.success(function(result) {
			if(result.code===0){
				$scope.user=result.data.user;
				angular.forEach($scope.user.permission, function(value, key) {
					$scope.ups.push(value.code);
				});
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.initUserGender();
	$scope.loadUserPermission();
	
	if($stateParams.action=="m"){
		
	}else if($stateParams.action=="c"){
		$scope.title="权限设置";
		$scope.loadUser($stateParams.userId);
		$scope.notEdit=true;
		$scope.notAuth=false;
	}else if($stateParams.action=="a"){
		$scope.title="添加后台人员";
		$scope.notEdit=false;
		$scope.notAuth=false;
	}else{
		$scope.initUser();
	}
	
})
.controller("adminPwdCtrl",function($scope,$http,$timeout){
	$scope.oldPwd="";
	$scope.newPwd="";
	$scope.msg="";
	
	$scope.modifyPwd=function(){
		$scope.msg="";
		var key="";
		var param={
				pwd:$scope.oldPwd,//encryptByDES($scope.oldPwd,$scope.oldPwd),
				newPwd:encryptByDES($scope.newPwd,$scope.oldPwd)
			};
			
		$http.post("user!modifyLoginPwd.action", param)
			.success(function(result) {
				if(result.code===0){
					$scope.msg="修改成功！";
				}else{
					$scope.msg=result.name;
				}
			});
	}
	
})
.controller("adminMgrCtrl", function($scope,$http,$window,$timeout,$selectDialog) {
	$scope.users = [];
	$scope.userName="";
	$scope.mobile="";
	$scope.msg="";
	
	$scope.total=0;
	$scope.pageIdx=1;
	$scope.pageSize=10;
	
	$scope.loadUser=function(){
		$scope.msg="";
		var parm={pageIdx:$scope.pageIdx,pageSize:$scope.pageSize,userName:$scope.userName,mobile:$scope.mobile};
		$http.post("user!queryAdminUserList.action",parm)
		.success(function(result) {
			if(result.code===0){
				$scope.users=result.data.list;
				$scope.total=result.data.total;
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.enableUser = function(id,isEnable){
		$scope.msg="";
		$http.post("user!enableAdmin.action",{userId:id,isPass:isEnable})
		.success(function(result) {
			if(result.code===0){
				$scope.loadUser();
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.resetPwd=function(id){
		$scope.msg="";
		$selectDialog.showMessage("是否确定重置密码?",function() {
        	$http.post("user!resetPwdByAdmin.action",{userId:id})
    		.success(function(result) {
    			if(result.code===400){
    				$window.location.href="../login";
    			}else{
    				$scope.msg=result.name;
    				$timeout(function(){
    					$scope.msg="";
    				},2000);
    			}
    		});
        });
		
	};
	
	$scope.loadUser();
	
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