angular.module("request", [ "ui.bootstrap",'ui.router',"diy.dialog","services","diy.selector" ])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider
	.state("request", {
		url : "/request",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "requestCtrl",
		cache:false
	}).state("request.list",{
		url : "/list",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "requestListCtrl",
		cache:false
	}).state("request.list.org", {
		url : "/org",
		abstract : true,
		templateUrl : "request/request.list.org.html",
		controller : "requestListOrgCtrl",
		cache:false
	}).state("request.list.org.finish",{
		url : "/finish",
		templateUrl : "request/request.list.org.finish.html",
		controller : "requestListOrgFinishCtrl",
		cache:false
	}).state("request.list.org.waiting",{
		url : "/waiting",
		templateUrl : "request/include.request.list.org.template.html",
		controller : "requestListOrgWaitingCtrl",
		cache:false
	}).state("request.list.org.diaging",{
		url : "/diaging",
		templateUrl : "request/include.request.list.org.template.html",
		controller : "requestListOrgDiagingCtrl",
		cache:false
	}).state("request.list.org.canceled",{
		url : "/canceled",
		templateUrl : "request/include.request.list.org.template.html",
		controller : "requestListOrgCanceledCtrl",
		cache:false
	}).state("request.list.org.backed",{
		url : "/backed",
		templateUrl : "request/include.request.list.org.template.html",
		controller : "requestListOrgBackedCtrl",
		cache:false
	}).state("request.list.my",{
		url : "/my",
		abstract : true,
		templateUrl : "request/request.list.my.html",
		controller : "requestListMyCtrl",
		cache:false
	}).state("request.list.my.finish",{
		url : "/finish",
		templateUrl : "request/request.list.my.finish.html",
		controller : "requestListMyFinishCtrl",
		cache:false
	}).state("request.list.my.waiting",{
		url : "/waiting",
		templateUrl : "request/request.list.my.waiting.html",
		controller : "requestListMyWaitingCtrl",
		cache:false
	}).state("request.list.my.diaging",{
		url : "/diaging",
		templateUrl : "request/include.request.list.my.template.html",
		controller : "requestListMyDiagingCtrl",
		cache:false
	}).state("request.list.my.canceled",{
		url : "/canceled",
		templateUrl : "request/include.request.list.my.template.html",
		controller : "requestListMyCanceledCtrl",
		cache:false
	}).state("request.list.my.backed",{
		url : "/backed",
		templateUrl : "request/request.list.my.backed.html",
		controller : "requestListMyBackedCtrl",
		cache:false
	}).state("request.list.medicalNum",{
		url : "/medicalNum/:medicalHisNum",
		templateUrl : "request/request.list.medicalNum.html",
		controller : "requestListMedicalNumCtrl",
		cache:false
//	}).state("request.detail",{
//		url : "/detail/:diagnosisId",
//		templateUrl : "request/request.detail.html",
//		controller : "requestDetailCtrl",
//		cache:false
	});
	$urlRouterProvider.when("/request/list", "/request/list/org/finish");
}).controller("requestDetailCtrl",function($scope,$diyhttp,$msgDialog,$stateParams){
	$scope.diagnosis =null;
	$scope.initData=function(){
		$diyhttp.post("diagnosis!queryFDiagnosisDetailById.action",{
			diagnosisId:$stateParams.diagnosisId
		},function(data){
			$scope.diagnosis = data.diagnosis;
		});
	};
}).controller("requestCtrl",function($scope,$http,$msgDialog,$stateParams){
	
}).controller("requestListCtrl",function($scope,$diyhttp,$location){
}).controller("requestListOrgCtrl",function($scope){
}).controller("requestListOrgFinishCtrl",function($scope,$diyhttp,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		patient_name : "",
		patient_gender : 0,
		case_his_num : "",
		deviceTypeId : 0,
		diagnosis_org_id : 0,
		timelimit:1,
		spu : {}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchReport();
	};
	$scope.searchReport = function() {
		$diyhttp.post("report!searchThisOrgReportByOrgRequest.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listReport=data.listReport;
			$scope.searchOpt.spu=data.spu;
		});
	};
}).controller("requestListOrgWaitingCtrl",function($scope,$diyhttp,$selectDialog,$msgDialog,$queryService){
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 1,
		timelimit:1,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchOrgRequestDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
	$scope.cancelDiagnosis=function(diagnosis){
		$selectDialog.showMessage("你确定要撤回此诊断申请吗？",function(){
			$diyhttp.post("diagnosis!cancelDiagnosis.action",{
				diagnosisId:diagnosis.id
			},function(data) {
				$msgDialog.showMessage("操作成功！");
				diagnosis.status=data.diagnosis.status;
				diagnosis.statusStr=data.diagnosis.statusStr;
			});
		});
	};
}).controller("requestListOrgDiagingCtrl",function($scope,$diyhttp,$selectDialog,$queryService){
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 4,
		timelimit:1,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchOrgRequestDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
}).controller("requestListOrgCanceledCtrl",function($scope,$diyhttp,$selectDialog,$queryService){
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 2,
		timelimit:1,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchOrgRequestDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
}).controller("requestListOrgBackedCtrl",function($scope,$diyhttp,$selectDialog,$writerDialog,$queryService){
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 3,
		timelimit:1,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchOrgRequestDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
	$scope.showNote=function(requestNote){
		$writerDialog.openWriting({
			title:"备注",
			msg:"拒绝备注",
			writing:false,
			note:requestNote
		});
	};
}).controller("requestListMyCtrl",function(){
	
}).controller("requestListMyFinishCtrl",function($scope,$diyhttp,$reportMsgChatModal,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		patient_name : "",
		patient_gender : 0,
		case_his_num : "",
		deviceTypeId : 0,
		diagnosis_org_id : 0,
		timelimit:1,
		spu : {}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchReport();
	};
	$scope.$watch(function(){
		return $scope.$parent.diagnosisCount.unreadRequestMsgCount;
	},function(oldVal,newVal,scope){
		$scope.searchReport();
	});
	$scope.searchReport = function() {
		$diyhttp.post("report!searchThisOrgReportByMeRequest.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listReport=data.listReport;
			$scope.searchOpt.spu=data.spu;
		});
	};
	$scope.openChat=function(report){
		$reportMsgChatModal.open({report:report,currUser:$scope.currUser});
		report.newMsgCount=0;
	};
}).controller("requestListMyWaitingCtrl",function($scope,$diyhttp,$msgDialog,$selectDialog,$queryService){
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 1,
		timelimit:1,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchSelfRequestDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
	$scope.cancelDiagnosis=function(diagnosis){
		$selectDialog.showMessage("你确定要撤回此诊断申请吗？",function(){
			$diyhttp.post("diagnosis!cancelDiagnosis.action",{
				diagnosisId:diagnosis.id
			},function(data) {
				$msgDialog.showMessage("操作成功！");
				$scope.searchRequest();
			});
		});
	};
}).controller("requestListMyDiagingCtrl",function($scope,$diyhttp,$queryService){
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 4,
		timelimit:1,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchSelfRequestDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
}).controller("requestListMyCanceledCtrl",function($scope,$diyhttp,$queryService){
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 2,
		timelimit:1,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchSelfRequestDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
}).controller("requestListMyBackedCtrl",function($scope,$diyhttp,$writerDialog,$queryService){
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : "",
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 3,
		timelimit:1,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchSelfRequestDiagnosis.action",$scope.searchOpt,function(data) {
			// 根据返回的列表填充
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
	$scope.showNote=function(requestNote){
		$writerDialog.openWriting({
			title:"备注",
			msg:"拒绝备注",
			writing:false,
			note:requestNote
		});
	};
}).controller("requestListMedicalNumCtrl",function($scope,$diyhttp,$msgDialog,$stateParams,$queryService){
	if (!$stateParams.medicalHisNum) {
		$msgDialog.showMessage("无效的病历号！");
		return;
	}
	$scope.listRequest=[];
	$scope.searchOpt={
		sickerName : "",
		gender : 0,
		medicalHisNum : $stateParams.medicalHisNum,
		deviceTypeId : 0,
		diagnosisOrgId : 0,
		status : 0,
		timelimit:0,
		spu : {
			currPage:0,
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryDiagnosisStatus();
		$scope.queryArrayOrg();
		$scope.searchRequest();
	};
	$scope.searchRequest = function() {
		$diyhttp.post("diagnosis!searchOrgRequestDiagnosis.action",$scope.searchOpt,function(data) {
			$scope.listRequest=data.listFDiagnosis;
			$scope.searchOpt.spu=data.spu;
		});
	};
});