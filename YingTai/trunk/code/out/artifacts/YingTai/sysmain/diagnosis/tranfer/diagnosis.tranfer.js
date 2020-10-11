angular.module("diagnosis.tranfer",["ui.bootstrap","services","diy.selector","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("diagnosis.tranfer", {
		url : "/tranfer",
		abstract : true,
		templateUrl : "diagnosis/tranfer/diagnosis.tranfer.html",
		controller : "diagnosisTranferCtrl",
		cache:false
	}).state("diagnosis.tranfer.waitAudit",{
		url : "/waitAudit",
		templateUrl : "diagnosis/tranfer/diagnosis.tranfer.waitAudit.html",
		controller : "diagnosisTranferWaitAuditCtrl",
		cache:false
	}).state("diagnosis.tranfer.waitHandle",{
		url : "/waitHandle",
		templateUrl : "diagnosis/tranfer/diagnosis.tranfer.waitHandle.html",
		controller : "diagnosisTranferWaitHandleCtrl",
		cache:false
	}).state("diagnosis.tranfer.handled",{
		url : "/handled",
		templateUrl : "diagnosis/tranfer/diagnosis.tranfer.handled.html",
		controller : "diagnosisTranferHandledCtrl",
		cache:false
	});
}).controller("diagnosisTranferCtrl", function() {
}).controller("diagnosisTranferWaitAuditCtrl",function($scope,$diyhttp,$location,$queryService){
	$scope.listHandle = [];
	$scope.searchOpt = {
		gender : 0,
		curr_user_id : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		partTypeId : 0,
		requestOrgId : 0,
		spu : {},
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryDoctorsInOrg();
		$scope.searchTranferToAuditHandle();
	};
	$scope.searchTranferToAuditHandle=function(){
		$diyhttp.post("diagnosis!searchTranferToAuditHandle.action",$scope.searchOpt,function(data) {
			$scope.listHandle = data.listHandle;
			$scope.searchOpt.spu = data.spu;
		});
	};
	$scope.beginHandle=function(handleId){
		if (!handleId) {
			$msgDialog.showMessage("你没有选择记录！");
			return;
		}
		$diyhttp.post("diagnosis!acceptTranferDiagnosis.action",{
			handleId:handleId
		},function(data) {
			$location.path("diagnosis/handle/writing");
		});
	};
}).controller("diagnosisTranferWaitHandleCtrl",function($scope,$diyhttp,$location,$queryService){
	$scope.listHandle = [];
	$scope.searchOpt = {
		gender : 0,
		curr_user_id : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		partTypeId : 0,
		requestOrgId : 0,
		spu : {},
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryDoctorsInOrg();
		$scope.searchTranferToMeHandle();
	};
	$scope.$watch(function(){
		return $scope.$parent.diagnosisCount.unhandle;
	},function(newVal,oldVal){
		$scope.searchTranferToMeHandle();
	});
	$scope.searchTranferToMeHandle=function(){
		$diyhttp.post("diagnosis!searchTranferToMeHandle.action",$scope.searchOpt,function(data) {
			$scope.listHandle = data.listHandle;
			$scope.searchOpt.spu = data.spu;
		});
	};
	$scope.beginHandle=function(handleId){
		if (!handleId) {
			$msgDialog.showMessage("你没有选择记录！");
			return;
		}
		$diyhttp.post("diagnosis!acceptTranferDiagnosis.action",{
			handleId:handleId
		},function(data) {
			$location.path("diagnosis/handle/writing");
		});
	};
}).controller("diagnosisTranferHandledCtrl",function($scope,$diyhttp,$selectDialog,$msgDialog,$location,$queryService){
	$scope.listHandle = [];
	$scope.searchOpt = {
		gender : 0,
		next_user_id : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		partTypeId : 0,
		requestOrgId : 0,
		spu : {},
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryDoctorsInOrg();
		$scope.searchTranferOfMeHandled();
	};
	$scope.searchTranferOfMeHandled=function(){
		$diyhttp.post("diagnosis!searchTranferOfMeHandled.action",$scope.searchOpt,function(data) {
			$scope.listHandle = data.listHandle;
			$scope.searchOpt.spu = data.spu;
		});
	};
	$scope.cancelTranfar=function(handle){
		$selectDialog.showMessage("你确定要撤销此移交，并修改诊断吗？",function(){
			$diyhttp.post("diagnosis!cancelTranfar.action",{
				handleId:handle.id
			},function(data){
				$msgDialog.showMessage("撤销成功，请继续书写！");
				$location.path("diagnosis/handle/writing");
			});
		});
	};
});