angular.module("user", [ "ui.router", "ui.bootstrap","diy.dialog" ])

.config(function($stateProvider, $urlRouterProvider) {

	$stateProvider
	.state("user", {
		url : "/user",
		templateUrl : "user/user.html",
		controller : "userCtrl"
	})
	.state("info",{
		url:"/info/:action/:id",
		templateUrl: "user/user.info.html",
		controller:"userInfoCtrl"
	});

})

.controller("userCtrl", function($scope,$http,$msgDialog,$selectDialog,$window) {
	$scope.users = [];
//	$scope.userName="";
//	$scope.mobile="";
	$scope.msg="";
	$scope.query={
		userName:"",
		mobile:"",
		total:0,
		pageIdx:1,
		pageSize:10,
		userStatus:2
	};
	$scope.deleteUser=function(user){
		if (!user)return;
		$selectDialog.showMessage("你确定要删除该用户吗？用户删除之后不可恢复！",function(){
			$http.post("user!deleteUser.action",{userId:user.user_id})
			.success(function(data){
				$msgDialog.showMessage(data.name);
				if (data.code==0) {
					$scope.loadUser($scope.query.userStatus);
				}
			});
		});
	};
	
	$scope.enableUser=function(user,enable){
		if (!user)return;
		var eu=function(){
			$http.post("user!enableUser.action",{userId:user.user_id,enable:enable})
			.success(function(data){
				$msgDialog.showMessage(data.name);
				if (data.code==0) {
					$scope.loadUser($scope.query.userStatus);
				}
			});
		};
		if (!enable) {
			$selectDialog.showMessage("你确定要禁用该用户吗？",eu);
		}else {
			eu();
		}
	};
	
	$scope.template={url:"user/user_list_template.html"};
	
	$scope.loadUser=function(ust){
		if(typeof ust !=="undefined"){
			$scope.query.userStatus=ust;
		}
		
		$scope.msg="";
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
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.loadUserChange=function(){
		$scope.query.userStatus=null;
		$scope.msg="";
		$scope.users=[];
		var parm={pageIdx:$scope.query.pageIdx,
				pageSize:$scope.query.pageSize,
				userName:$scope.query.userName,
				mobile:$scope.query.mobile
			};
		
		$http.post("user!queryUserChangeList.action",parm)
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
	
	$scope.tab={isActive:false,isActive2:false};
	$scope.$watch("waitCount.a_user_count", function() {
		if($scope.tab.isActive)
			$scope.loadUser(1);
	});
	
	$scope.$watch("waitCount.change_user_count", function() {
		if($scope.tab.isActive2)
			$scope.loadUserChange();
	});
	
	$scope.$watch("query.userStatus", function() {
		$scope.query.total=0;
		$scope.query.pageIdx=1;
		$scope.query.pageSize=10;
	});
})
.controller("userInfoCtrl",function($scope,$http,$state,$timeout,$stateParams){
	$scope.title="用户信息";
	$scope.is_approved=false;
	$scope.is_change=false;
	$scope.isDoctor=false;
	$scope.msg="";
	$scope.ispass=true;
	$scope.user=null;
	$scope.note="";
	
	if($stateParams.action==="a"){
		$scope.title="审核用户";
		$scope.is_approved=true;
	}else if($stateParams.action==="ac"){
		$scope.title="变更用户";
		$scope.is_change=true;
	}
	
	$scope.loadUser=function(){
		if($stateParams.id!==""&&!isNaN(parseFloat($stateParams.id)) && isFinite($stateParams.id)){
			$http.post("user!queryUserById.action",{userId:$stateParams.id})
			.success(function(result) {
				if(result.code===0){
					$scope.user=result.data.user;
					if($scope.user.type){
						$scope.isDoctor=$scope.user.type.code===3;
					}
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
		}else{
			$scope.msg="无效的用户";
			$timeout(function(){
				$state.go("user");
			},1500);
		}
	};
	
	$scope.loadUserChange=function(){
		if($stateParams.id!==""&&!isNaN(parseFloat($stateParams.id)) && isFinite($stateParams.id)){
			$http.post("user!queryUserChangeByUser.action",{userId:$stateParams.id})
			.success(function(result) {
				if(result.code===0){
					$scope.user=result.data.user;
					if($scope.user.CUserType){
						$scope.isDoctor=$scope.user.CUserType.code===3;
					}
				}else if(result.code===400){
					$window.location.href="../login";
				}else{
					$scope.msg=result.name;
				}
			});
		}else{
			$scope.msg="无效的用户";
			$timeout(function(){
				$state.go("user");
			},1500);
		}
	};
	
	$scope.approvedUser=function(){
		var parm={
				userId:$scope.user.id,
				isPass:$scope.ispass,
				note:$scope.note
			};
		$http.post("user!approvedUserByAdmin.action",parm)
		.success(function(result) {
			if(result.code===0){
				$state.go("info",{action:'v',id:$scope.user.id});
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	$scope.confirmChange=function(){
		var parm={
				userId:$scope.user.id,
				isPass:$scope.ispass,
				note:$scope.note
			};
		
		$http.post("user!confirmChange.action",parm)
		.success(function(result) {
			if(result.code===0){
				$state.go("info",{action:'v',id:$scope.user.user_id});
			}else if(result.code===400){
				$window.location.href="../login";
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
	if($scope.is_change){
		$scope.loadUserChange();
	}else{
		$scope.loadUser();
	}
	
});
