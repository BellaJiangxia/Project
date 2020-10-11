angular.module("case.mgr", [ "ui.bootstrap","diy.dialog","services","diy.selector"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider
	.state("case.mgr", {
		url : "/mgr",
		templateUrl : "case/case.mgr.html",
		controller : "caseMgrCtrl"
	});
}).controller("caseMgrCtrl",function($scope, $diyhttp,$msgDialog,$selectDialog,$patientSelector) {
	$scope.listCaseHistory=[];
	$scope.arrayOrg=[];
	$scope.searchOpt={
		patient_name:"",
		patient_gender:0,
		hospitalized_num:"",
		case_his_num:"",
		spu:{
			perPageCount:10
		}
	};
	$scope.initData=function(){
		$scope.searchCaseHistory();
	};
	
	$scope.setMedicalHis=function(medicalHis){
		$scope.list_medicalHis = medicalHis;
	};
	// 搜索我的病例
	$scope.searchCaseHistory = function() {// MedicalNum
		$diyhttp.post("case!searchThisOrgCaseHistory.action", $scope.searchOpt,function(data) {
			$scope.listCaseHistory =data.listCaseHistory;
			$scope.searchOpt.spu=data.spu;
		});
	};
});







