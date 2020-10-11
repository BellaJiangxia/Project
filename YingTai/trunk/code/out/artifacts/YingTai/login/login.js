/**
 * 登录模块
 */

angular.module("login", [])

.config(function() {
})

.controller("loginCtrl", function($scope, $http, $window,$location) {

	$scope.loginInfo = {
		name : "",
		pwd : "",
		code : "",
		autoNext : true
	};
	
	$scope.vcUrl = "vc.action?vcType=login&t=" + Math.random();
	$scope.msg = "";
	
	var m=$location.search();
	if(m){
		$scope.msg=m.msg==="r"?"该帐号已在其他地方登录，请重新登录！":"";
	}

	$scope.login = function() {
		$scope.msg = "";
		var param = {
			loginName : $scope.loginInfo.name,
			pwd : encryptByDES($scope.loginInfo.pwd, $scope.loginInfo.code)
		};

		$http.post("user!loginByPwd.action", param, {}).success(function(data, status, headers, config) {
			if (data.code === 0) {
				if(data.data.is_admin){
					$window.location.href="../admin";
				}else if (data.data.is_guest || !data.data.is_valid) {
					$window.location.href = '../user';
				}else if(data.data.mulit_org || data.data.no_org){
					$window.location.href = '../user/#/myOrg/list';
				}  else {
					$window.location.href = '../sysmain/index.html';
				}
			} else {
				$scope.msg = data.name;
				$scope.vcImage();
			}
		});
	};
	
	$scope.keyPress=function(e){
		if(e.keyCode===13)
			$scope.login();
	};

	$scope.vcImage = function() {
		$scope.vcUrl = "vc.action?vcType=login&t=" + Math.random();
	}
});
