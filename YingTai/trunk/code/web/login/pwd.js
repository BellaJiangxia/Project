angular.module("pwd", [ "ui.bootstrap","services","diy.dialog"])
.config(function() {
}).controller("pwdCtrl", function($scope, $http, $window,$interval,$msgDialog) {
	$scope.loginName="";
	$scope.pwd="";
	$scope.pwd2="";
	$scope.code="";
	$scope.msg="";
	$scope.timeOut=0;
	
	$scope.getCode=function(){
		$scope.msg="";
		var param={
			loginName:$scope.loginName,
			vcType:3
		};
		$http.post("user!reqValidateCode.action", param)
		.success(function(result) {
			if(result.code!==0){
				$scope.msg=result.name;
			}else {
				$msgDialog.showMessage("获取短信验证码成功，请注意查收！");
				$scope.timeOut=60;
			}
		});
	};
	var ttt=$interval(function(){
		if($scope.timeOut>0){
			$scope.timeOut--;
		}
	},1000);
	$scope.$on('$destroy', function() {
		if (angular.isDefined(t)) {
		    $interval.cancel(t);
		    t = undefined;
		 }
	});
	$scope.resetpwd=function(){
		$scope.msg="";
		var param={
			loginName:$scope.loginName,
			newPwd:encryptByDES($scope.pwd,$scope.code)
		};
		
		$http.post("user!resetPwd.action", param)
		.success(function(result) {
			if(result.code===0){
				$scope.msg="密码重置成功，请重新登录！";
				$timeout(function(){
					$window.location.href="index.html";
				},3000);
			}else{
				$scope.msg=result.name;
			}
		});
	};
	
});
