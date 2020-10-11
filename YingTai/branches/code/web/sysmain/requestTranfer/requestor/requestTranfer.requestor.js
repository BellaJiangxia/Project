angular.module("requestTranfer.requestor",[])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("requestTranfer.requestor",{
		url : "/requestor",
		abstract : true,
		templateUrl : "requestTranfer/requestor/requestTranfer.requestor.html",
		controller : "requestTranferRequestorCtrl"
	}).state("requestTranfer.requestor.waiting", {
		url : "/waiting",
		templateUrl : "requestTranfer/requestor/requestTranfer.requestor.waiting.html",
		controller : "requestTranferRequestorWaitingCtrl"
	}).state("requestTranfer.requestor.finish",{
		url : "/finish",
		templateUrl : "requestTranfer/requestor/requestTranfer.requestor.finish.html",
		controller : "requestTranferRequestorFinishCtrl"
	}).state("requestTranfer.requestor.accept_tranfer",{
		url : "/accept_tranfer",
		templateUrl : "requestTranfer/requestor/requestTranfer.requestor.accept_tranfer.html",
		controller : "requestTranferRequestorAccept_tranferCtrl"
	}).state("requestTranfer.requestor.diaging",{
		url : "/diaging",
		templateUrl : "requestTranfer/requestor/requestTranfer.requestor.diaging.html",
		controller : "requestTranferRequestorDiagingCtrl"
	}).state("requestTranfer.requestor.canceled",{
		url : "/canceled",
		templateUrl : "requestTranfer/requestor/requestTranfer.requestor.canceled.html",
		controller : "requestTranferRequestorCanceledCtrl"
	}).state("requestTranfer.requestor.backed",{
		url : "/backed",
		templateUrl : "requestTranfer/requestor/requestTranfer.requestor.backed.html",
		controller : "requestTranferRequestorBackedCtrl"
	});
	$urlRouterProvider.when("/requestTranfer/requestor","/requestTranfer/requestor/finish");
}).controller("requestTranferRequestorCtrl",function($scope,$state,$diyhttp,$queryService,$dialogs,$modals){
	$scope.listRequestTranfer = [];
	$scope.searchOpt={
		spu:{},
		new_patient_name:"",
		new_case_his_num:"",
		new_request_status:0,
		new_patient_gender:0,
		new_device_type_id:0,
		new_diagnosis_org_id:0,
		timelimit:0
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.queryArrayOrg();
		$scope.queryTimeLimit();
		$scope.searchReqtestTranfer();
	};
	$scope.searchReqtestTranfer = function(){
		$diyhttp.post("requestTranfer!searchRequestTranferFromThisOrg.action",$scope.searchOpt,function(data){
			$scope.listRequestTranfer = data.listRequestTranfer,
			$scope.searchOpt.spu = data.spu;
		});
	};
	$scope.againTranferRequest=function(requestTranfer){
		$modals.openTranferRequestModal({
			diagnosis_id:requestTranfer.old_request_id,
			againTranfer:true
		});
	};
}).controller("requestTranferRequestorWaitingCtrl",function($scope,$diyhttp,$dialogs){
	$scope.searchOpt.new_request_status=1;
	$scope.cancelTranferRequest=function(request_tranfer_id){
		$dialogs.showConfirmDialog("您确定要撤回此移交申请吗？",function(){
			$diyhttp.post("requestTranfer!cancelTranferRequest.action",{
				request_tranfer_id:request_tranfer_id
			},function(data){
				$dialogs.showMessage("撤回成功！");
				$scope.searchReqtestTranfer();
			});
		});
	};
}).controller("requestTranferRequestorFinishCtrl",function($scope){
	$scope.searchOpt.new_request_status=5;
}).controller("requestTranferRequestorAccept_tranferCtrl",function($scope){
	$scope.searchOpt.new_request_status=10;
}).controller("requestTranferRequestorDiagingCtrl",function($scope){
	$scope.searchOpt.new_request_status=4;
}).controller("requestTranferRequestorCanceledCtrl",function($scope){
	$scope.searchOpt.new_request_status=2;
}).controller("requestTranferRequestorBackedCtrl",function($scope,$writerDialog){
	$scope.searchOpt.new_request_status=3;
	$scope.showNote=function(requestNote){
		$writerDialog.openWriting({
			title:"备注",
			msg:"拒绝备注",
			writing:false,
			note:requestNote
		});
	};
});