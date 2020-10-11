angular.module("patientInfoShare", [ "services" ]).config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("patientInfoShare", {
		url : "/patientInfoShare",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "patientInfoShareCtrl"
	}).state("patientInfoShare.begin", {
		url : "/beign/:relation_org_id,:relation_org_name",
		templateUrl : "patientInfoShare/patientInfoShare.begin.html",
		controller : "patientInfoShareBeginCtrl"
	});
	$urlRouterProvider.when("/patientInfoShare", "/patientInfoShare/begin");
}).controller("patientInfoShareCtrl", function($scope) {

}).controller("patientInfoShareBeginCtrl", function($scope, $diyhttp,$dialogs, $stateParams) {
	if ($stateParams.relation_org_id <= 0)
		$scope.goback();
	$scope.currMode = 1;
	$scope.beginSearchConditions = [ {
		name : "病人姓名",
		title : "病人全名，至少两个汉字或者6个英文字母",
		type : 1,
		value : null
	}, {
		name : "病人身份证号",
		title : "15位或者18位数字，末尾可以是X",
		type : 2,
		value : null
	} ];
	$scope.onBackClick=function(){
		$scope.currMode = 1;
		$scope.listCaseHistory = [];
	};
	$scope.searchParamValuesByPatientName = function(corrSearchCondition) {
		if (!corrSearchCondition)
			return;
		corrSearchCondition.relation_org_id = $stateParams.relation_org_id;
		$diyhttp.post("piShare!searchParamValuesByPatientName.action", corrSearchCondition, function(data) {
			if (data.listPatientInfoResult && data.listPatientInfoResult.length > 0) {
				$dialogs.showSelectPatientInfoNumDialog(data.listPatientInfoResult,function(patientInfoResult){
					if (!patientInfoResult) 
						return;
					$scope.queryPatientData(patientInfoResult);
				});
			} else {
				$dialogs.showMessage("暂时未找到该病人的相关信息！");
			}
		});
	};
	// 搜索病例
	$scope.queryPatientData = function(patientInfoShareGapEntity) {// MedicalNum
		$diyhttp.post("piShare!searchCaseHistorybyGapGreat.action", {
			uuid : patientInfoShareGapEntity.uuid,
			relation_org_id:$stateParams.relation_org_id
		}, function(data) {
			if (!data.listCaseHistory || data.listCaseHistory.length <= 0) {
				$msgDialog.showMessage("暂时未找到病例信息！");
			} else {
				$scope.listCaseHistory = data.listCaseHistory;
				$scope.currMode = 2;
			}
		});
	};
	
	
	$scope.listCaseHistory = [];
	$scope.searchOpt = {
		spu : {},
		relation_org_name : $stateParams.relation_org_name
	}
//		case_his_num : "",
//		patient_name : "",
//		patient_gender : 0,
//		hospitalized_num : ""
//	};
//	$scope.initData = function() {
//		$scope.searchCaseHistory();
//	};
//	$scope.searchCaseHistory = function() {
//		$diyhttp.post("piShare!searchCaseHistory.action", $scope.searchOpt, function(data) {
//			$scope.listCaseHistory = data.listCaseHistory;
//			$scope.searchOpt.spu = data.spu;
//		});
//	};
}).controller("patientInfoShareNewCtrl", function($scope, $diyhttp, $msgDialog, $queryService, $location) {
	$scope.mode = 1;
	$scope.searchHistoryCollapsed = false;
	$scope.listCaseHistory = [];
	$scope.remoteParam = null;
	$scope.listDicomImgNumObj = [];
	$scope.initData = function() {
		$diyhttp.post("piShare!queryLastDicomImgNum.action", {
			relation_org_id : $scope.searchOpt.relation_org_id
		}, function(data) {
			if (data.listDicomImgNumObj && data.listDicomImgNumObj.length > 0) {
				$scope.listRemoteParamsType = data.listDicomImgNumObj;
				$scope.remoteParam = $scope.listRemoteParamsType[0];
			} else {
				$queryService.queryRemoteParamType(function(listRemoteParamsType) {
					if (listRemoteParamsType && listRemoteParamsType.length > 0)
						$scope.remoteParam = listRemoteParamsType[0];
				});
			}
		});
	};
	// 搜索病例
	$scope.queryPatientData = function() {// MedicalNum
		$diyhttp.post("piShare!searchCaseHistoryGreat.action", {
			relation_org_id : $scope.searchOpt.relation_org_id,
			remoteParamsType : $scope.remoteParam.code,
			remoteParamsValue : $scope.remoteParam.remoteParamValue
		}, function(data) {
			if (!data.listCaseHistory || data.listCaseHistory.length <= 0) {
				$msgDialog.showMessage("暂时未找到病例信息！");
			} else {
				$scope.listCaseHistory = data.listCaseHistory;
			}
		});
	};
	$scope.selectRemoteParamClick = function(r) {
		$scope.remoteParam = r;
	};
});