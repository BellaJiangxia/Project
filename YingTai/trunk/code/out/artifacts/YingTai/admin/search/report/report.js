angular.module("diagnosis", [ "ui.bootstrap", "diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider
	.state("diagnosis", {
		url : "/diagnosis",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "diagnosisCtrl"
	})

	.state("diagnosis.report", {
		url : "/report/:diagnosisId",
		templateUrl : "search/report/diagnosis.report.html",
		controller : "diagnosisReportCtrl"
	}).state("diagnosis.detail", {
		url : "/detail/:diagnosisId",
		templateUrl : "search/report/diagnosis.detail.html",
		controller : "diagnosisDetailCtrl"
	});
}).controller("diagnosisDetailCtrl", function($scope, $http, $window, $msgDialog, $stateParams) {
	// 数据区
	$scope.diagnosis = null;
	$scope.medicalHis = null;
	$scope.medicalHisImg = null;
	// 函数区
	$scope.initData = function() {
		$scope.queryFDiagnosisById($stateParams.diagnosisId);
	};
	$scope.gotoBack = function() {
		$window.history.back();
	};
	$scope.queryFDiagnosisById = function(diagnosisId) {
		if (!$stateParams.diagnosisId) {
			$msgDialog.showMessage("无效的诊断ID！");
			return;
		}
		$http.post("diagnosis!queryFDiagnosisAndImgByDiagnosisId.action", {
			diagnosisId : diagnosisId
		}).success(function(data) {
			if (data.code == 0) {
				$scope.diagnosis = data.data.diagnosis;
				$scope.medicalHis = data.data.medicalHis;
				$scope.medicalHisImg = data.data.medicalHisImg;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("diagnosisReportCtrl", function($scope, $http, $window,$msgDialog, $stateParams, $location) {
	if (!$stateParams.diagnosisId) {
		$msgDialog.showMessage("无效的报告ID");
		return;
	}
	$scope.wasRequestOrg = false;
	$scope.report = null;
	$scope.medicalHis=null;
	$scope.arrayDiagnosisHandle = null;
	$scope.historyMedicalHis=[];
	$scope.handleShow = false;
	$scope.initData = function() {
		queryReportByDiagnosisId();
	};

	// 打印报告
	$scope.printReport = function() {
		alert();
		$location.path("diagnosis/reportprint/" + $stateParams.diagnosisId);
	};

	// 通过诊断ID查询报告
	var queryReportByDiagnosisId = function() {
		$http.post("report!queryReportDetailById.action", {
			reportId : $stateParams.diagnosisId
		}).success(function(data) {
			if (data.code == 0) {
				if (data.data && data.data.report) {
					$scope.diagnosis = data.data.report;
				} else
					$msgDialog.showMessage("诊断报告未找到！");
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.gotoBack = function() {
		$window.history.back();
	};
}).controller("diagnosisCtrl", function($scope, $http, $msgDialog) {
	
});
