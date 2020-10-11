angular.module('diy.images.dialog', [ 'ngDialog',"angular-carousel"])
.service("$imagesDialog",function(ngDialog) {
	this.show = function(fileMgr,yes,no) {
		ngDialog.open({
			template :"ngDialog/diy.images.dialog.html",
			className : 'ngdialog-theme-default custom-width',
			plain : false,
			//size : 'lg',
			data : {
				fileMgr:fileMgr
			},
			controller : "imagesDialogCtrl"
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
}).controller("imagesDialogCtrl",function($scope,$element){
	$scope.fileMgr=$scope.ngDialogData.fileMgr;
	$scope.confirm=function(){
		$scope.closeThisDialog();
	};
	$scope.initData=function(){
//		$http.post("diagnosis!queryFDiagnosisDetailById.action",{diagnosisId:$scope.ngDialogData.listFileMgr})
//		.success(function(data){
//			if (data.code==0) {
//				$scope.diagnosis=data.data.diagnosis;
//			}else {
//				$msgDialog.showMessage(data.name);
//			}
//		});
	};
});