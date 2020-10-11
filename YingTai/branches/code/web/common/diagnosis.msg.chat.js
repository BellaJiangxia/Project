angular.module('diy.diagnosis.msg.chat', [ 'ui.bootstrap',"diy.dialog"])
.service("$diagnosisVideoChat", function($uibModal) {
	this.openChat = function(params,callbackOk, callbackCancel) {
		$uibModal.open({
			animation : true,
			templateUrl : '../common/diagnosis.video.chat.html',
			controller : 'diagnosisVideoChatCtrl',
			size : "",
			resolve : {
				params : params
			}
		}).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
})
.controller("diagnosisVideoChatCtrl",function($scope,$http,$interval,$msgDialog,$uibModalInstance,params){
	$scope.diagnosis=params.diagnosis;
	$scope.currUser=params.currUser;
	$scope.listDiagnosisMsg=[];
	$scope.pageContent={
		msgContent:""
	};
	$scope.initData=function(){
		$scope.queryDiagnosisMsg();
	};
	
	$scope.queryDiagnosisMsg=function(){
		$http.post("diagnosis!queryDiagnosisMsgByDiagnosisId.action",{diagnosisId:$scope.diagnosis.id})
		.success(function(data){
			if (data.code==0) {
				$scope.listDiagnosisMsg=data.data.listDiagnosisMsg;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	var timer=$interval(function(){
		$scope.queryDiagnosisMsg();
	},3000);
	$scope.sendDiagnosisMsg=function(){
		if (!$scope.pageContent.msgContent) {
			return;
		}
		$http.post("diagnosis!sendDisgnosisMsg.action",{
			diagnosisId:$scope.diagnosis.id,
			content:$scope.pageContent.msgContent
		}).success(function(data){
			if (data.code==0) {
				var msgTmp=data.data.diagnosisMsg;
//				msgTmp.
				$scope.listDiagnosisMsg.push(msgTmp);
				$scope.pageContent.msgContent="";
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	$scope.$on('$destroy', function() {
		if (angular.isDefined(timer)) {
		    $interval.cancel(timer);
		    timer = undefined;
		 }
	});
});