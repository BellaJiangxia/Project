angular.module("stat", [ "ui.bootstrap", "diy.dialog","myDirectives" ])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("stat", {
		url : "/stat",
		templateUrl : "stat/stat.html",
		controller : "statCtrl"
	}).state("/statFom",{
		url : "/statFom",
		templateUrl : "stat/stat.fom.html",
		controller : "statFomCtrl"
	}).state("statDeviceReport",{
		url : "/statDeviceReport",
		templateUrl : "stat/stat.device.report.html",
		controller : "statDeviceReportCtrl"
	}).state("statReportCost",{
		url : "/statReportCost",
		templateUrl : "stat/stat.report.cost.html",
		controller : "statReportCostCtrl"
	}).state("statReportDoctor",{
		url : "/statReportDoctor",
		templateUrl : "stat/stat.report.doctor.html",
		controller : "statReportDoctorCtrl"
	}).state("statReportSick",{
		url : "/statReportSick",
		templateUrl : "stat/stat.report.sick.html",
		controller : "statReportSickCtrl"
	}).state("sysFlowing",{
		url:"/sysFlowing",
		templateUrl : "stat/sysFlowing.html",
		controller : "sysFlowingCtrl"
	}).state("reportStat",{
		url:"/reportStat",
		templateUrl :"stat/stat.report.html",
		controller:"statReportCtrl"
	});
}).controller("statReportSickCtrl",function($scope,$http,$msgDialog){
	$scope.listReport=[];
	$scope.searchOpt={
		requestOrgId:"0",
		diagnosisOrgId:"0",
		sickName:"",
		start:null,
		end:null,
		mode:0,
	};
	$scope.initData=function(){
		$scope.queryAllValidOrg();
	};
	$scope.downloadStatOrgFom=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		window.location.target="_ablank" ;
		var url="downloadStatReportSick.action";
		url=url+"?diagnosisOrgId="+$scope.searchOpt.diagnosisOrgId+
			"&requestOrgId="+$scope.searchOpt.requestOrgId+"&sickName="+$scope.searchOpt.sickName+
			"&start="+$scope.searchOpt.start+"&end="+
			$scope.searchOpt.end+"&mode="+$scope.searchOpt.mode;
		window.location.href =url;
	};
	$scope.statOrgFom=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		$http.post("stat!statReportSick.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listReport=data.data.listReport;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("statReportDoctorCtrl",function($scope,$http,$msgDialog){
	$scope.listReport=[];
	$scope.searchOpt={
		requestOrgId:"0",
		diagnosisOrgId:"0",
		doctorId:"0",
		start:null,
		end:null,
		type:1,
		mode:0
	};
	$scope.selectStatType=function(sty){
		$scope.searchOpt.type=sty;
		$scope.statOrgFom();
	};
	$scope.initData=function(){
		$scope.queryAllValidOrg();
		$scope.statOrgFom();
	};
	$scope.downloadStatOrgFom=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		window.location.target="_ablank" ; 
		var url="downloadstatReportDoctor.action";
		url=url+"?requestOrgId="+$scope.searchOpt.requestOrgId+
			"&start="+$scope.searchOpt.start+
			"&end="+$scope.searchOpt.end+
			"&diagnosisOrgId="+$scope.searchOpt.diagnosisOrgId+
			"&mode="+$scope.searchOpt.mode+
			"&doctorId="+$scope.searchOpt.doctorId+
			"&type="+$scope.searchOpt.type;
		window.location.href =url;
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
}).controller("statReportCostCtrl",function($scope,$http,$msgDialog){
	$scope.listReport=[];
	$scope.searchOpt={
		requestOrgId:"0",
		diagnosisOrgId:"0",
		start:null,
		end:null,
		mode:0
	};
	$scope.initData=function(){
		$scope.queryAllValidOrg();
		$scope.statOrgFom();
	};
	$scope.downloadStatOrgFom=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		window.location.target="_ablank" ; 
		var url="downloadstatReportCost.action";
		url=url+"?requestOrgId="+$scope.searchOpt.requestOrgId+
			"&start="+$scope.searchOpt.start+"&end="+$scope.searchOpt.end+
			"&diagnosisOrgId="+$scope.searchOpt.diagnosisOrgId+"&mode="+
			$scope.searchOpt.mode;
		window.location.href =url;
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
}).controller("statDeviceReportCtrl",function($scope,$http,$msgDialog){
	$scope.listReport=[];
	$scope.searchOpt={
		requestOrgId:"0",
		diagnosisOrgId:"0",
		start:null,
		end:null,
		mode:0
	};
	$scope.initData=function(){
		$scope.queryAllValidOrg();
		$scope.statOrgFom();
	};
	
	$scope.downloadStatOrgFom=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		window.location.target="_ablank" ; 
		var url="downloadstatDeviceReport.action";
		url=url+"?requestOrgId="+$scope.searchOpt.requestOrgId+
			"&start="+$scope.searchOpt.start+"&end="+$scope.searchOpt.end+
			"&diagnosisOrgId="+$scope.searchOpt.diagnosisOrgId+"&mode="+
			$scope.searchOpt.mode;
		window.location.href =url;
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
}).controller("statFomCtrl",function($scope,$http,$msgDialog){
	$scope.listReport=[];
	$scope.searchOpt={
		requestOrgId:"0",
		diagnosisOrgId:"0",
		start:null,
		end:null,
		mode:0
	};
	$scope.initData=function(){
		$scope.queryAllValidOrg();
		$scope.statOrgFom();
	};
	$scope.downloadStatOrgFom=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		window.location.target="_ablank" ; 
		var url="downloadStatOrgFom.action";
		url=url+"?requestOrgId="+$scope.searchOpt.requestOrgId+
			"&start="+$scope.searchOpt.start+"&end="+$scope.searchOpt.end+
			"&diagnosisOrgId="+$scope.searchOpt.diagnosisOrgId+"&mode="+
			$scope.searchOpt.mode;
		window.location.href =url;
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
}).controller("statReportCtrl",function($scope,$http,$msgDialog,$queryService){
	$scope.searchOpt={
		medicalHisNum:"",
		requestOrgName:"",
		diagnosisOrgName:"",
		publishOrgName:"",
		deviceTypeId:"0",
		partTypeId:"0",
		requestUserName:"",
		acceptUserName:"",
		vertifyUserName:"",
		start:null,
		end:null,
		fom:"-1",
		spu:{perPageCount:15}
	};
	$scope.listReport=[];
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.statReport();
	};
	$scope.statReport=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		$http.post("stat!statReport.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listReport=data.data.listReport;
				$scope.searchOpt.spu=data.data.spu;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
})
.controller("sysFlowingCtrl",function($scope,$http,$msgDialog){
	$scope.searchOpt={
		type:"0",
		start:null,
		end:null,
		diagnosis_org_id:'0',
		request_org_id:'0',
		spu:{
			perPageCount:10
		}
	};
	$scope.listFlowing=[];
	$scope.initData=function(){
		$scope.queryAllValidOrg();
		$scope.searchSysAccountFlowing();
	};
	
	$scope.downloadSysAccountFlowing=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		window.location.target="_ablank" ; 
		var url="downloadSysAccountFlowing.action";
		url=url+"?type="+$scope.searchOpt.type+
			"&start="+$scope.searchOpt.start+"&end="+$scope.searchOpt.end+
			"&request_org_id="+$scope.searchOpt.request_org_id+"&diagnosis_org_id="+
			$scope.searchOpt.diagnosis_org_id;
		window.location.href =url;
	};
	
	$scope.searchSysAccountFlowing=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		$http.post("stat!searchSysAccountFlowing.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listFlowing=data.data.listFinanceRecord;
				$scope.searchOpt.spu=data.data.spu;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
})
.controller("statCtrl",function($http, $scope, $location, $msgDialog) {
	$scope.statTypeMap = {
		year : true,
		month : false,
		day : false,
		reset : function() {
			this.year = false;
			this.month = false;
			this.day = false;
		}
	};
	$scope.selectStatType = function(currPage) {
		$scope.statTypeMap.reset();
		switch (currPage) {
		case "year":
			$scope.statTypeMap.year = true;
			$scope.statType = 1;
			$scope.orgMedicalHisStatByType();
			$scope.dataSource.title.text="按年统计";
			$scope.dataSource.title.subtext="最近5年的数据";
			break;
		case "month":
			$scope.statTypeMap.month = true;
			$scope.statType = 2;
			$scope.orgMedicalHisStatByType();
			$scope.dataSource.title.text="按月统计";
			$scope.dataSource.title.subtext="最近12月的数据";
			break;
		case "day":
			$scope.statTypeMap.day = true;
			$scope.statType = 3;
			$scope.orgMedicalHisStatByType();
			$scope.dataSource.title.text="按天统计";
			$scope.dataSource.title.subtext="最近10天的数据";
			break;
		default:
			break;
		}
	};
	$scope.statType = 0;
	$scope.dataSource = {
		title:{
			text:"按年统计",
			subtext:"最近5年的数据"
		},
        tooltip: {show: true},
        legend: {
        	data:["新增病例","新增申请","新增人员","新增诊断"]
        },
         xAxis : [{
                 type : 'category',
                 data : ["2011","2012","2013","2014","2015"]
             }],
         yAxis : [{type : 'value'}],
         series : [{
                "name":"新增病例",
                "type":"bar",
                "data":[0,0,0,0,0]
            },{
                "name":"新增申请",
                "type":"bar",
                "data":[0,0,0,0,0]
            },{
                "name":"新增人员",
                "type":"bar",
                "data":[0,0,0,0,0]
            },{
                "name":"新增诊断",
                "type":"bar",
                "data":[0,0,0,0,0]
            }]
	};
	$scope.medicalHisTotal = 0;
	$scope.initData = function() {
		$scope.orgMedicalHisStatByType();
	};
	$scope.orgMedicalHisStatByType = function() {
		$http.post("stat!sysStatByType.action", {
			type : $scope.statType
		}).success(function(data) {
			if (data.code == 0) {
				$scope.medicalHisTotal = data.data.medicalHisTotal;
				$scope.dataSource.xAxis[0].data=data.data.listStatmh.label;
				$scope.dataSource.series[0]["data"]	= data.data.listStatmh.value;
				$scope.dataSource.series[1]["data"]	= data.data.listRequest.value;
				$scope.dataSource.series[2]["data"]	= data.data.listOrgUser.value;
				$scope.dataSource.series[3]["data"]	= data.data.listDiagnosis.value;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
});