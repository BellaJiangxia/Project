angular.module("diagnosis.mgr",["services","diy.selector","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("diagnosis.mgr",{
		url : "/mgr",
		abstract : true,
		templateUrl : "diagnosis/mgr/diagnosis.mgr.html",
		controller : "diagnosisMgrCtrl",
		cache:false
	}).state("diagnosis.mgr.waiting",{
		url : "/waiting",
		templateUrl : "diagnosis/mgr/diagnosis.mgr.waiting.html",
		controller : "diagnosisMgrWaitingCtrl",
		cache:false
	}).state("diagnosis.mgr.diaging",{
		url : "/diaging",
		templateUrl : "diagnosis/mgr/diagnosis.mgr.diaging.html",
		controller : "diagnosisMgrDiagingCtrl",
		cache:false
	}).state("diagnosis.mgr.canceled",{
		url : "/canceled",
		templateUrl : "diagnosis/mgr/diagnosis.mgr.canceled.html",
		controller : "diagnosisMgrCanceledCtrl",
		cache:false
	}).state("diagnosis.mgr.backed",{
		url : "/backed",
		templateUrl : "diagnosis/mgr/diagnosis.mgr.backed.html",
		controller : "diagnosisMgrBackedCtrl",
		cache:false
	});
}).controller("diagnosisMgrCtrl",function(){
	
}).controller("diagnosisMgrWaitingCtrl",function($scope,$diyhttp,$writerDialog,$modals,$msgDialog,$queryService,$tipDiagnosisDialog,$location){
	$scope.listDiagnosis = [];
	$scope.searchOpt = {
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		requestOrgId : 0,
		status : 1,
		spu : {}
	};
	//初始化数据
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.searchDiagnosis();
	};
	$scope.$watch(function(){
		return $scope.$parent.diagnosisCount.waitDiagnosisHandleCount;
	},function(newVal,oldVal,scope){
		$scope.searchDiagnosis();
	});
	//搜索提交到我机构的申请
	$scope.searchDiagnosis = function() {
		$diyhttp.post("diagnosis!searchSelfDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listDiagnosis=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
	//拒绝诊断
	$scope.rejectDiagnosis=function(diagnosis){
		if (!diagnosis)return;
		$writerDialog.openWriting({
			msg:"请填写拒绝诊断的理由:",
			title:"编辑",
			writing:true
		},function(note){
			if (! note)return;
			$diyhttp.post("diagnosis!rejectDiagnosis.action",{
				diagnosisId:diagnosis.id,
				note: note
			},function(data) {
				$msgDialog.showMessage("拒绝成功！");
				$scope.searchDiagnosis();
			});
		});
	};
	$scope.acceptRequestAndTranferRequest=function(diagnosis){
		if (!diagnosis)return;
		if (diagnosis.status!=1) {
			$msgDialog.showMessage($scope,"你不能再处理此申请！");
			return;
		}
		$modals.openTranferRequestModal({
			diagnosis_id:diagnosis.id,
			againTranfer:false
		},function(){
			$scope.searchDiagnosis();
		});
	};
	//接受申请并开始诊断
	$scope.acceptDiagnosis=function(diagnosis){
		if (!diagnosis)return;
		if (diagnosis.status!=1) {
			$msgDialog.showMessage($scope,"你不能再处理此申请！");
			return;
		}
		$tipDiagnosisDialog.showMessage(diagnosis.id,function(){
			$diyhttp.post("diagnosis!acceptDiagnosis.action",{
				diagnosisId:diagnosis.id
			},function(data) {
				//跳转
				var handle=data.handle;
				$location.path("diagnosis/handle/writing");
			});
		});
	};
}).controller("diagnosisMgrDiagingCtrl",function($scope,$diyhttp,$queryService){
	$scope.listDiagnosis = [];
	$scope.searchOpt = {
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		requestOrgId : 0,
		status : 4,
		spu : {}
	};
	//初始化数据
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.$parent.queryArrayOrg();
		$scope.searchDiagnosis();
	};
	$scope.searchDiagnosis = function() {
		$diyhttp.post("diagnosis!searchSelfDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listDiagnosis=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
}).controller("diagnosisMgrCanceledCtrl",function($scope,$diyhttp,$queryService){
	$scope.listDiagnosis = [];
	$scope.searchOpt = {
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		requestOrgId : 0,
		status : 2,
		spu : {}
	};
	//初始化数据
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.$parent.queryArrayOrg();
		$scope.searchDiagnosis();
	};
	$scope.searchDiagnosis = function() {
		$diyhttp.post("diagnosis!searchSelfDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listDiagnosis=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
}).controller("diagnosisMgrBackedCtrl",function($scope,$diyhttp,$queryService){
	$scope.listDiagnosis = [];
	$scope.searchOpt = {
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		requestOrgId : 0,
		status : 3,
		spu : {}
	};
	//初始化数据
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.$parent.queryArrayOrg();
		$scope.searchDiagnosis();
	};
	$scope.searchDiagnosis = function() {
		$diyhttp.post("diagnosis!searchSelfDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listDiagnosis=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
});