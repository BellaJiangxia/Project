angular.module("stat.request", ["diy.dialog" ,"services"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("statRequest",{
		url : "/statRequest",
		templateUrl : "stat/request/stat.request.html",
		controller : "statRequestCtrl",
		cache:false
	}).state("statRequest.fom",{
		url : "/fom",
		templateUrl : "stat/request/stat.request.fom.html",
		controller : "statRequestFomCtrl",
		cache:false
	}).state("statRequest.device",{
		url : "/device",
		templateUrl : "stat/request/stat.request.device.report.html",
		controller : "statRequestDeviceReportCtrl",
		cache:false
	}).state("statRequest.cost",{
		url : "/cost",
		templateUrl : "stat/request/stat.request.report.cost.html",
		controller : "statRequestReportCostCtrl",
		cache:false
	}).state("statRequest.doctor",{
		url : "/doctor",
		templateUrl : "stat/request/stat.request.report.doctor.html",
		controller : "statRequestReportDoctorCtrl",
		cache:false
	}).state("statRequest.sick",{
		url : "/sick",
		templateUrl : "stat/request/stat.request.report.sick.html",
		controller : "statRequestReportSickCtrl",
		cache:false
	}).state("statRequest.patient",{
		url : "/patient",
		templateUrl : "stat/request/stat.request.patient.html",
		controller : "statRequestPatientCtrl",
		cache:false
	});
}).controller("statRequestPatientCtrl",function($scope,$msgDialog,$diyhttp){
	$scope.listReport=[];
	$scope.searchOpt={
		sick_key_words:"",
		diagnosisOrgId:0,
		start:null,
		end:null,
		spu:{
			totalRow:0
		}
	};
	$scope.initData=function(){
		$scope.queryArrayOrg();
		$scope.statPatientInfoForRequestOrg();
	};
	$scope.statPatientInfoForRequestOrg=function(){
		$diyhttp.post("stat!statPatientInfoForRequestOrg.action",{
			sick_key_words:$scope.searchOpt.sick_key_words,
			diagnosisOrgId:$scope.searchOpt.diagnosisOrgId,
			start:($scope.searchOpt.start&&$scope.searchOpt.start instanceof Date)?($scope.searchOpt.start.format('yyyy-MM-dd')):null,
			end:($scope.searchOpt.end&&$scope.searchOpt.end instanceof Date)?($scope.searchOpt.end.format('yyyy-MM-dd')):null,
			spu:$scope.searchOpt.spu
		},function(data){
			$scope.listReport=data.listReport;
			$scope.searchOpt.spu=data.spu;
		});
	};
	$scope.downloadRequests=function(){
		window.location.target="_ablank" ; 
		var url="downloadStatPatientInfoForRequestOrg.action";
		url+="?sick_key_words="+$scope.searchOpt.sick_key_words;
		url+="&diagnosisOrgId="+$scope.searchOpt.diagnosisOrgId
		url+="&start="+(($scope.searchOpt.start&&$scope.searchOpt.start instanceof Date)?($scope.searchOpt.start.format('yyyy-MM-dd')):"");
		url+="&end="+(($scope.searchOpt.end&&$scope.searchOpt.end instanceof Date)?($scope.searchOpt.end.format('yyyy-MM-dd')):"");
		window.location.href =url;
	};
//	$scope.downloadStatPatientInfoForDiagnosisOrg=function(){
//		window.location.target="_ablank";
//		var url="downloadStatPatientInfoForRequestOrg.action";
//		url=url+"?sick_key_words="+$scope.searchOpt.sick_key_words;
////			"&start="+$scope.searchOpt.start+"&end="+$scope.searchOpt.end+
////			"&request_org_id="+$scope.searchOpt.request_org_id+"&diagnosis_org_id="+
////			$scope.searchOpt.diagnosis_org_id;
//		window.location.href =url;
//	};
}).controller("statRequestCtrl",function($scope,$location){
}).controller("statRequestReportSickCtrl",function($scope,$http,$msgDialog,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		diagnosisOrgId:"0",
		deviceTypeId:"0",
		partTypeId:"0",
		sickName:"",
		start:null,
		end:null,
		mode:2,
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
//		$scope.statOrgFom();
	};
	$scope.statOrgFom=function(){
		$http.post("stat!statReportSick.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listReport=data.data.listReport;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("statRequestReportDoctorCtrl",function($scope,$http,$msgDialog,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		requestOrgId:"0",
		deviceTypeId:"0",
		partTypeId:"0",
		doctorId:"0",
		start:null,
		end:null,
		type:1,
		mode:1,
	};
	$scope.selectStatType=function(sty){
		$scope.searchOpt.type=sty;
		$scope.statOrgFom();
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryDoctorsInOrg();
		$scope.statOrgFom();
	};
	$scope.statOrgFom=function(){
		$http.post("stat!statReportDoctor.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listReport=data.data.listReport;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("statRequestReportCostCtrl",function($scope,$http,$msgDialog,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		diagnosisOrgId:"0",
		deviceTypeId:"0",
		partTypeId:"0",
		start:null,
		end:null,
		mode:2
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.statOrgFom();
	};
	$scope.statOrgFom=function(){
		$http.post("stat!statReportCost.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listReport=data.data.listReport;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("statRequestDeviceReportCtrl",function($scope,$http,$msgDialog,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		diagnosisOrgId:"0",
		deviceTypeId:"0",
		partTypeId:"0",
		start:null,
		end:null,
		mode:2
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.statOrgFom();
	};
	$scope.statOrgFom=function(){
		$http.post("stat!statDeviceReport.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listReport=data.data.listReport;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("statRequestFomCtrl",function($scope,$http,$msgDialog,$queryService){
	$scope.listReport=[];
	$scope.searchOpt={
		diagnosisOrgId:"0",
		deviceTypeId:"0",
		partTypeId:"0",
		start:null,
		end:null,
		mode:2
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.statOrgFom();
	};
	$scope.statOrgFom=function(){
		$http.post("stat!statReportOrgFom.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listReport=data.data.listReport;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
});