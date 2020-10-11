angular.module('diy.medical.detail.dialog', [ 'ngDialog',"diy.dialog","dicom","diy.images.dialog"])
.service("$medicalDetailDialog",function(ngDialog) {
	this.showDialog = function(medicalId,yes,no) {
		ngDialog.open({
			template :"ngDialog/diy.medical.detail.dialog.html",
			className : 'ngdialog-theme-default custom-diagnosis-detail-width',
			plain : false,
//			size : 'lg',
			data : {
				medicalHisId:medicalId
			},
			controller : "medicalDetailDialogCtrl"
		}).closePromise.then(function (data) {
		    if (data.value==1) {
				if (yes) {
					yes();
				}
			}else {
				if (no) {
					no();
				}
			}
		});
	};
}).controller("medicalDetailDialogCtrl",function($scope, $http, $window,$msgDialog,$dicom,$imagesDialog){
	$scope.medical_his = {};
	$scope.patient={};
	$scope.initData=function(){
		if (!$scope.ngDialogData.medicalHisId){
			$msgDialog.showMessage("无效的病例ID！");
			return;
		}
		$scope.queryMedicalHis();
	};
	$scope.gotoBack=function(){
		$window.history.back();
	};
	
	$scope.showBrowse=function(fileMgr){
		$imagesDialog.show(fileMgr);
	};
	// 通过病例号查询病例
	$scope.queryMedicalHis = function() {// MedicalNum
		$http.post("medicalHis!queryMedicalHisById.action", {
			medicalHisId : $scope.ngDialogData.medicalHisId
		}).success(function(data) {
			if (data.code == 0) {
				$scope.medical_his = data.data.medicalHis;
				$scope.patient = data.data.patient;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.showDicom = function(dicom_img_id) {
		$dicom.openDicoms({
            dicom_img_id:dicom_img_id
		});
	};
});