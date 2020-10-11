angular.module("stat.diagnosis", ["diy.dialog", "services"])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider.state("statDiagnosis", {
            url: "/statDiagnosis",
            templateUrl: "stat/diagnosis/stat.diagnosis.html",
            controller: "statDiagnosisCtrl",
            cache: false
        }).state("statDiagnosis.fom", {
            url: "/fom",
            templateUrl: "stat/diagnosis/stat.diagnosis.fom.html",
            controller: "statDiagnosisFomCtrl",
            cache: false
        }).state("statDiagnosis.device", {
            url: "/device",
            templateUrl: "stat/diagnosis/stat.diagnosis.device.report.html",
            controller: "statDiagnosisDeviceReportCtrl",
            cache: false
        }).state("statDiagnosis.cost", {
            url: "/cost",
            templateUrl: "stat/diagnosis/stat.diagnosis.report.cost.html",
            controller: "statDiagnosisReportCostCtrl",
            cache: false
        }).state("statDiagnosis.doctor", {
            url: "/doctor",
            templateUrl: "stat/diagnosis/stat.diagnosis.report.doctor.html",
            controller: "statDiagnosisReportDoctorCtrl",
            cache: false
        }).state("statDiagnosis.sick", {
            url: "/sick",
            templateUrl: "stat/diagnosis/stat.diagnosis.report.sick.html",
            controller: "statDiagnosisReportSickCtrl",
            cache: false
        }).state("statDiagnosis.patient", {
            url: "/patient",
            templateUrl: "stat/diagnosis/stat.diagnosis.patient.html",
            controller: "statDiagnosisPatientCtrl",
            cache: false
        });
    }).controller("statDiagnosisPatientCtrl", function ($scope, $msgDialog, $diyhttp) {
    $scope.listReport = [];
    $scope.searchOpt = {
        sick_key_words: "",
        requestOrgId: 0,
        start: null,
        end: null,
        spu: {
            totalRow: 0
        }
    };
    $scope.initData = function () {
        $scope.queryArrayOrg();
        $scope.statPatientInfoForDiagnosisOrg();
    };
    $scope.statPatientInfoForDiagnosisOrg = function () {
        $diyhttp.post("stat!statPatientInfoForDiagnosisOrg.action", {
            sick_key_words: $scope.searchOpt.sick_key_words,
            requestOrgId: $scope.searchOpt.requestOrgId,
            start: ($scope.searchOpt.start && $scope.searchOpt.start instanceof Date) ? ($scope.searchOpt.start.format('yyyy-MM-dd')) : null,
            end: ($scope.searchOpt.end && $scope.searchOpt.end instanceof Date) ? ($scope.searchOpt.end.format('yyyy-MM-dd')) : null,
            spu: $scope.searchOpt.spu
        }, function (data) {
            $scope.listReport = data.listReport;
            $scope.searchOpt.spu = data.spu;
        });
    };
    $scope.downloadStatPatientInfoForDiagnosisOrg = function () {
        window.location.target = "_ablank";
        var url = "downloadStatPatientInfoForDiagnosisOrg.action";
        url += "?sick_key_words=" + $scope.searchOpt.sick_key_words;
        url += "&requestOrgId=" + $scope.searchOpt.requestOrgId;
        url += "&start=" + (($scope.searchOpt.start && $scope.searchOpt.start instanceof Date) ? ($scope.searchOpt.start.format('yyyy-MM-dd')) : "");
        url += "&end=" + (($scope.searchOpt.end && $scope.searchOpt.end instanceof Date) ? ($scope.searchOpt.end.format('yyyy-MM-dd')) : "");
        window.location.href = url;
    };
}).controller("statDiagnosisCtrl", function ($scope, $location, $msgDialog) {
//	$scope.statTypeMap={
//		index:0
//	};
//	$scope.selectStatType=function(ind){
//		$scope.statTypeMap.index=ind;
//		switch (ind) {
//		case 1:
//			$location.path("statDiagnosis/sick");
//			break;
//		case 2:
//			$location.path("statDiagnosis/fom");
//			break;
//		case 3:
//			$location.path("statDiagnosis/device");
//			break;
//		case 4:
//			$location.path("statDiagnosis/cost");
//			break;
//		case 5:
//			$location.path("statDiagnosis/doctor");
//			break;
//		default:
//			break;
//		}
//	};
}).controller("statDiagnosisReportSickCtrl", function ($scope, $http, $msgDialog, $queryService) {
    $scope.listReport = [];
    $scope.searchOpt = {
        requestOrgId: "0",
        deviceTypeId: "0",
        partTypeId: "0",
        sickName: "",
        start: null,
        end: null,
        mode: 1,
    };
    $scope.initData = function () {
        $scope.queryArrayOrg();
        $queryService.queryDeviceType();
//		$scope.statOrgFom();
    };
    $scope.statOrgFom = function () {
        $http.post("stat!statReportSick.action", $scope.searchOpt)
            .success(function (data) {
                if (data.code == 0) {
                    $scope.listReport = data.data.listReport;
                } else {
                    $msgDialog.showMessage(data.name);
                }
            });
    };
}).controller("statDiagnosisReportDoctorCtrl", function ($scope, $http, $msgDialog, $queryService) {
    $scope.listReport = [];
    $scope.searchOpt = {
        requestOrgId: "0",
        doctorId: "0",
        deviceTypeId: "0",
        start: null,
        end: null,
        type: "1",
        mode: 1,
    };
    $scope.selectStatType = function (sty) {
        $scope.statOrgFom();
    };
    $scope.initData = function () {
        $queryService.queryDeviceType();
        $scope.queryArrayOrg();
        $scope.queryDoctorsInOrg();
        $scope.statOrgFom();
    };
    $scope.statOrgFom = function () {
        $http.post("stat!statDoctorWorkOfMyOrgDiagnosis.action", $scope.searchOpt)
            .success(function (data) {
                if (data.code == 0) {
                    $scope.listReport = data.data.listReport;
                } else {
                    $msgDialog.showMessage(data.name);
                }
            });
    };
    $scope.downloadStatDoctorWorkOfMyOrgDiagnosis = function(){
        $scope.statOrgFom();
        if ($scope.listReport.length <= 0)
            return;
        if ($scope.listReport.length > 500){
            $msgDialog.showMessage("数据量过大，请使用时间筛选！");
            return;
        }
        window.location.target = "_ablank";
        var url = "statDownload!downloadStatDoctorWorkOfMyOrgDiagnosis.action";
        url += "?requestOrgId=" + $scope.searchOpt.requestOrgId;
        url += "&doctorId=" + $scope.searchOpt.doctorId;
        url += "&deviceTypeId=" + $scope.searchOpt.deviceTypeId;
        url += "&start=" + (($scope.searchOpt.start && $scope.searchOpt.start instanceof Date) ? ($scope.searchOpt.start.format('yyyy-MM-dd')) : "");
        url += "&end=" + (($scope.searchOpt.end && $scope.searchOpt.end instanceof Date) ? ($scope.searchOpt.end.format('yyyy-MM-dd')) : "");
        url += "&type=" + $scope.searchOpt.type;
        window.location.href = url;
    };
}).controller("statDiagnosisReportCostCtrl", function ($scope, $http, $msgDialog, $queryService) {
    $scope.listReport = [];
    $scope.footing = null;
    $scope.searchOpt = {
        requestOrgId: 0,
        deviceTypeId: 0,
        charge_type: 0,
        start: null,
        end: null,
        spu: {}
    };
    $scope.initData = function () {
        $scope.queryArrayOrg();
        $queryService.queryDeviceType();
        $queryService.queryOrgProductChargeType();
        $scope.statOrgFom();
    };
    $scope.statOrgFom = function () {
        $http.post("stat!statReportByMyOrgDiagnosis.action", $scope.searchOpt)
            .success(function (data) {
                if (data.code == 0) {
                    $scope.listReport = data.data.listReport;
                    $scope.footing = data.data.footing;
                    $scope.searchOpt.spu = data.data.spu;
                } else {
                    $msgDialog.showMessage(data.name);
                }
            });
    };
    $scope.downloadReportCost = function () {
        $scope.statOrgFom();
        if ($scope.searchOpt.spu.totalRow > 500){
            $msgDialog.showMessage("数据量过大，请使用时间筛选！");
            return;
        }
        window.location.target = "_ablank";
        var url = "statDownload!downloadStatReportByMyOrgDiaginosisCost.action";
        url += "?requestOrgId=" + $scope.searchOpt.requestOrgId;
        url += "&deviceTypeId=" + $scope.searchOpt.deviceTypeId;
        url += "&charge_type=" + $scope.searchOpt.charge_type;
        url += "&start=" + (($scope.searchOpt.start && $scope.searchOpt.start instanceof Date) ? ($scope.searchOpt.start.format('yyyy-MM-dd')) : "");
        url += "&end=" + (($scope.searchOpt.end && $scope.searchOpt.end instanceof Date) ? ($scope.searchOpt.end.format('yyyy-MM-dd')) : "");
        window.location.href = url;
    };
}).controller("statDiagnosisDeviceReportCtrl", function ($scope, $http, $msgDialog, $queryService) {
    $scope.listReport = [];
    $scope.searchOpt = {
        requestOrgId: "0",
        deviceTypeId: "0",
        partTypeId: "0",
        start: null,
        end: null,
        mode: 1
    };
    $scope.initData = function () {
        $queryService.queryDeviceType();
        $scope.queryArrayOrg();
        $scope.statOrgFom();
    };
    $scope.statOrgFom = function () {
        $http.post("stat!statDeviceReport.action", $scope.searchOpt)
            .success(function (data) {
                if (data.code == 0) {
                    $scope.listReport = data.data.listReport;
                } else {
                    $msgDialog.showMessage(data.name);
                }
            });
    };
}).controller("statDiagnosisFomCtrl", function ($scope, $http, $msgDialog, $queryService) {
    $scope.listReport = [];
    $scope.searchOpt = {
        requestOrgId: "0",
        deviceTypeId: "0",
        partTypeId: "0",
        start: null,
        end: null,
        mode: 1
    };
    $scope.initData = function () {
        $queryService.queryDeviceType();
        $scope.queryArrayOrg();
        $scope.statOrgFom();
    };
    $scope.statOrgFom = function () {
        $http.post("stat!statReportOrgFom.action", $scope.searchOpt)
            .success(function (data) {
                if (data.code == 0) {
                    $scope.listReport = data.data.listReport;
                } else {
                    $msgDialog.showMessage(data.name);
                }
            });
    };
});