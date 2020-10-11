angular.module("qualityControl",[])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("qualityControl", {
		url : "/qualityControl",
		abstract : true,
		template : "<div ui-view></div>",
		controller : function(){
		},
	}).state("qualityControl.list",{
		url : "/list",
		templateUrl : "qualityControl/qualityControl.list.html",
		controller : "qualityControlListCtrl",
	});
	$urlRouterProvider.when("/qualityControl","/qualityControl/list");
}).controller("qualityControlListCtrl",function($scope,$diyhttp,$queryService,$dialogs){
	$scope.listQualityControlFormAnswer = [];
	$scope.searchOpt={
		spu:{},
		target_type:0,
		question_occasion:0,
		question_enforceable:0,
		qcfa_status:0
	};
	$scope.initData=function(){
		$queryService.queryQualityControlTarget();
		$queryService.queryQualityControlEnforceable();
		$queryService.queryQualityControlFormAnswerStatus();
		$scope.searchQualityControlFormAnswer();
	};
	$scope.writingQualityControlFormAnswer=function(qualityControlFormAnswer){
		var finalfunc = function(){
            $scope.searchQualityControlFormAnswer();
        };
		$dialogs.showQualityControlFormsDialog({
			form_answer_id:qualityControlFormAnswer.id,
			msg:"",
			from_type:2,
			msg_class:"text-warning"
		},finalfunc,finalfunc);
	};
	$scope.searchQualityControlFormAnswer=function(){
		$diyhttp.post("qualityControl!searchQualityControlFormAnswer.action",$scope.searchOpt,function(data){
			$scope.listQualityControlFormAnswer = data.listQualityControlFormAnswer;
			$scope.searchOpt.spu = data.spu;
		});
	};
}).controller("dicomQualityControlFormCtrl",function($scope,$diyhttp,$dialogs,$vsDialogs){
	$scope.listQualityControlForm =[];
	$scope.canIgnore=false;
	$scope.qualityControlUid=null;
//	$scope.canClose = ($scope.ngDialogData.from_type==2);
	$scope.option={
//		msg:$scope.ngDialogData.msg,
//		msg_class:$scope.ngDialogData.msg_class //text-info;text-warning;text-danger
	};
	$scope.$on("to-qualityControlUid", function(event,qualityControlUid) {
		var params = {};
		$scope.qualityControlUid = qualityControlUid;
		if (qualityControlUid && qualityControlUid.length>0) {
			params.qualityControlUid = qualityControlUid;
		}else {
			return;
//			params.form_answer_id = $scope.form_answer_id;
		}
		$diyhttp.post("qualityControl!takeQualityControlFormByQcUidOrFormAnswerId.action",params,function(data){
			if (!data || !data.listQualityControlForm || data.listQualityControlForm.length<=0) {
				$scope.qualityControlFormCancelClick();
				return;
			}
			$scope.listQualityControlForm = data.listQualityControlForm;
			for (var int = 0; int < $scope.listQualityControlForm.length; int++) {
				var qualityControlForm = $scope.listQualityControlForm[int];
				if (qualityControlForm.qualityControlFormAnswer.form_question_enforceable == 10){
					$scope.canIgnore=false;
					return;
				}
			}
			$scope.canIgnore=true;
		});
	});
	$scope.ignoreForms=function(){
		if (!$scope.qualityControlUid || $scope.qualityControlUid.length<=0) {
			$scope.qualityControlFormCancelClick();
			return;
		}
		$diyhttp.post("qualityControl!ignoreFormsByQcUid.action",{
			qualityControlUid:$scope.qualityControlUid
		},function(data){
			$scope.qualityControlFormCancelClick();
//			if ($scope.ngDialogData.requestParams) {
//				$diyhttp.post($scope.ngDialogData.requestParams.url,$scope.ngDialogData.requestParams.params,
//						$scope.ngDialogData.requestParams.option.callbackOk,$scope.ngDialogData.requestParams.option.callbackNo,
//						$scope.ngDialogData.requestParams.option.callbackFinal);
//			}
		});
	};
	$scope.commitAnswer=function(){
		$diyhttp.post("qualityControl!commitQualityControlFormAnswer.action",{
			listQualityControlForm:$scope.listQualityControlForm
		},function(data){
			$vsDialogs.showMessage("提交成功！");
			$scope.qualityControlFormCancelClick();
//			if ($scope.ngDialogData.requestParams) {
//				$diyhttp.post($scope.ngDialogData.requestParams.url,$scope.ngDialogData.requestParams.params,
//						$scope.ngDialogData.requestParams.option.callbackOk,$scope.ngDialogData.requestParams.option.callbackNo,
//						$scope.ngDialogData.requestParams.option.callbackFinal);
//			}
		});
	};
	$scope.onScopeHover=function(ma,value){
		ma.percent = parseInt(value/5*100);
	};
	$scope.onScopeLeave=function(ma){
		ma.percent = parseInt(ma.score/5*100);
	};
//	$scope.confirm=function(){
//		$scope.closeThisDialog();
//	};
//	$scope.cancel=function(){
//		$scope.closeThisDialog();
//	};
});