angular.module("case.info", [ "services","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("case.info", {
		url : "/info/:caseId",
		templateUrl : "case/info/case.info.html",
		controller : "caseInfoCtrl"
	});
	//$urlRouterProvider.when("/case", "/case/new");
}).controller("caseInfoCtrl",function($scope,$diyhttp,$state,$stateParams,$queryService,HTML5_VIEWER_URL){
	$scope.patient =null;
	$scope.caseHistory = null;
	$scope.listDicomImg = [];
	$scope.listReport = [];
	$scope.initData=function(){
		$queryService.queryCaseHistoryType();
		$scope.queryCaseInfo();
	};
	$scope.requestViewImage=function(study_uid){
		$diyhttp.post("legitimate!requestViewImage.action",{
			study_uid:study_uid
		},function(data){
			window.open(HTML5_VIEWER_URL+"?studyUID="+study_uid+"&token="+data.token); 
		});
	};
	$scope.queryCaseInfo=function(){
		$diyhttp.post("case!queryCaseInfo.action",{
			caseId:$stateParams.caseId
		},function(data){
			$scope.patient = data.patient;
			$scope.caseHistory = data.caseHistory;
			$scope.listDicomImg = data.listDicomImg;
			$scope.listReport = data.listReport;
		});
	};
	$scope.gotoModifyCaseHistory=function(){
		$state.go("case.modify",{caseId:$scope.caseHistory.id});
	};
});