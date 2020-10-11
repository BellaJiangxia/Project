angular.module("log", [ "ui.bootstrap", "diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("log", {
		url : "/log",
		templateUrl : "log/sys.log.html",
		controller : "logCtrl"
	});
}).controller("logCtrl",function($scope,$http,$msgDialog){
	$scope.searchOpt={
		peratorName:"",
		orgName:"",
		spu:{perPageCount:30},
		start:null,
		end:null,
		status:"0",
		module:"0",
		operType:"0",
	};
	$scope.listLog=[];
	$scope.initData=function(){
		$scope.searchSysLogList();
	};
	$scope.searchSysLogList=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		$http.post("log!searchSysLogList.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listLog=data.data.listLog;
				$scope.searchOpt.spu=data.data.spu;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
});