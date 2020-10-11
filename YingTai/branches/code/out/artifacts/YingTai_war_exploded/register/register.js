/**
 * 用户注册
 */
angular.module("register", ['ui.bootstrap','ajax4struts'])
.controller("regCtrl",function($scope,$http,$window,$interval){
	$scope.reg={
		mobile:"",
		pwd:"",
		code:""
	};
	$scope.isCheck=true;
	
	$scope.msg="";
	
	$scope.register = function() {
		$scope.msg="";
		var param = {
			loginName : $scope.reg.mobile,
			pwd : encryptByDES($scope.reg.pwd,$scope.reg.code)
		};
		
		$http.post("user!register.action", param)
			.success(function(data, status, headers, config) {
				if(data.code==0){
					$window.location.href = '../user';
				}else{
					$scope.msg=data.name;
				}
			})
			.error(function(data, status, headers, config) {
			});
	};
	
	$scope.codetxt="获取验证码";
	$scope.wait=false;
	var t;
	$scope.getCode=function(){
		$scope.msg="";
		var param={
			loginName:$scope.reg.mobile,
			vcType:1
		};
		
		$http.post("user!reqValidateCode.action", param)
			.success(function(data, status, headers, config) {
				if(data.code!==0){
					$scope.msg=data.name;
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
			})
			.error(function(data, status, headers, config) {
			});
	};
	
	$scope.checkLoginName=function(){
		$scope.msg="";
		var param={
				loginName:$scope.reg.mobile	
			};
		
		if($scope.reg.mobile){
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
	
});
