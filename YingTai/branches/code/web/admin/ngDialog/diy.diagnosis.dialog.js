angular.module('diy.diagnosis.dialog', [ 'ngDialog',"diy.dialog"])
.service("$tipDiagnosisDialog",function(ngDialog) {
	this.showMessage = function(diagnosisId,yes,no) {
		ngDialog.open({
			template :"ngDialog/diy.diagnosis.dialog.html",
			className : 'ngdialog-theme-default',
			plain : false,
			size : 'lg',
			data : {
				diagnosisId:diagnosisId
			},
			controller : "diagnosisDialogCtrl"
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
}).controller("diagnosisDialogCtrl",function($scope,$http,$msgDialog){
	$scope.diagnosis=null;
	$scope.initData=function(){
		$http.post("diagnosis!queryFDiagnosisDetailById.action",{diagnosisId:$scope.ngDialogData.diagnosisId})
		.success(function(data){
			if (data.code==0) {
				$scope.diagnosis=data.data.diagnosis;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	}
});