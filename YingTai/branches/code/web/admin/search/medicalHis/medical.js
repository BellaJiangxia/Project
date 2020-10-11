angular.module("medical", [ "ui.bootstrap","diy.dialog","diagnosis"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider
	.state("medical", {
		url : "/medical",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "medicalCtrl"
	}).state("medical.detail",{
		url : "/detail/:medicalHisId",
		templateUrl : "search/medicalHis/medical.detail.html",
		controller : "medicalDetail"
	}).state("medical.list",{
		url : "/list",
		templateUrl : "search/medicalHis/medical.list.html",
		controller : "medicalListCtrl"
	});
	$urlRouterProvider.when("/medical","/medical/list");
}).controller("medicalListCtrl",function($scope,$http,$msgDialog){
	$scope.listMedicalHis=[];
	$scope.searchOpt={
		org_name:"",
		org_code:"",
		medicalHisNum:"",
		sicker_name:"",
		sicker_gender:"0",
		status:"0",
		start:null,
		end:null,
		spu:{}
	};
	$scope.initData=function(){
		$scope.searchMedicalHis();
	};
	$scope.searchMedicalHis=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		$http.post("medicalHis!searchAdminMedicalHis.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listMedicalHis=data.data.listMedicalHis;
				$scope.searchOpt.spu=data.data.spu;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("medicalDetail",function($scope,$http,$window,$msgDialog,$stateParams){
	$scope.medical_his = {};
	$scope.patient={};
	$scope.initData=function(){
		if ($stateParams.medicalHisId) {
			$scope.queryMedicalHis();
		}else{
			$window.history.back();
		}
	};
	
	$scope.gotoBack=function(){
		$window.history.back();
	};
	// 通过病例号查询病例
	$scope.queryMedicalHis = function() {// MedicalNum
		var data = {
			medicalHisId : $stateParams.medicalHisId
		};
		$http.post("medicalHis!queryMedicalHisById.action", data)
		.success(function(data) {
			if (data.code == 0) {
				$scope.medical_his = data.data.medicalHis;
				$scope.patient = data.data.patient;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
})
.controller("medicalCtrl", function($scope) {
	$scope.list_medicalHis=null;
});
