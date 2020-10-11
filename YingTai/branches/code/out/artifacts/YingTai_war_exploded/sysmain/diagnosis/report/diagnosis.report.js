angular.module("diagnosis.report",["services","diy.selector","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("diagnosis.report",{
		url : "/report",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "diagnosisReportCtrl",
		cache:false
	}).state("diagnosis.report.list",{
		url : "/list",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "diagnosisReportListCtrl",
		cache:false
	}).state("diagnosis.report.list.my",{
		url : "/my",
		templateUrl : "diagnosis/report/diagnosis.report.list.my.html",
		controller : "diagnosisReportListMyCtrl",
		cache:false
	}).state("diagnosis.report.list.org",{
		url : "/org",
		templateUrl : "diagnosis/report/diagnosis.report.list.org.html",
		controller : "diagnosisReportListOrgCtrl",
		cache:false
	});
}).controller("diagnosisReportCtrl",function(){
	
}).controller("diagnosisReportListCtrl",function(){
	
}).controller("diagnosisReportListMyCtrl",function($scope,$diyhttp,$msgDialog,$reportMsgChatModal,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		case_his_num:"",
		requestOrgId:0,
		deviceTypeId:0,
		partTypeId:0,
		timelimit:1,
		spu:{}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryTimeLimit();
		$scope.queryArrayOrg();
		$scope.searchMyReport();
	};
	$scope.$watch(function(){
		return $scope.$parent.diagnosisCount.unreadDiagnosisMsgCount;
	},function(oldVal,newVal,scope){
		$scope.searchMyReport();
	});
	//查询我编写的诊断报告
	$scope.searchMyReport=function(){
		$diyhttp.post("report!searchMyReport.action",$scope.searchOpt,function(data){
			$scope.listReport=data.listReport;
			$scope.searchOpt.spu=data.spu;
		});
	};
	$scope.openChat=function(report){
		$reportMsgChatModal.open({report:report,currUser:$scope.currUser});
		report.newMsgCount=0;
	};
}).controller("diagnosisReportListOrgCtrl",function($scope,$diyhttp,$msgDialog,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		case_his_num:"",
		requestOrgId:0,
		deviceTypeId:0,
		partTypeId:0,
		publish_user_id:0,
		timelimit:1,
		spu:{}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryTimeLimit();
		$scope.queryArrayOrg();
		$scope.queryVerityUserInOrg();
		$scope.searchOrgReport();
	};
	
	//查询机构的诊断报告
	$scope.searchOrgReport=function(){
		$diyhttp.post("report!searchOrgReport.action",$scope.searchOpt,function(data){
			$scope.listReport=data.listReport;
			$scope.searchOpt.spu=data.spu;
		});
	};
});