angular.module("share", [ "services","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("share",{
		url :"/share",
		abstract : true,
		template :"<div ui-view></div>",
		controller : "shareCtrl"
	}).state("share.list", {
		url : "/list",
		templateUrl : "share/share.list.html",
		controller : "shareListCtrl"
	}).state("share.org",{
		url : "/org",
		templateUrl : "share/share.org.html",
		controller : "shareOrgCtrl"
	}).state("share.detail", {
		url : "/detail/:reportShareId",
		templateUrl : "share/share.detail.html",
		controller : "shareDetailCtrl"
	});
}).controller("shareCtrl",function($scope){
//
}).controller("shareListCtrl", function($scope,$diyhttp,$msgDialog) {
	$scope.listReportShare=[];
	$scope.searchOpt={
		shareOrgName:"",
		symptom : "",
		start:null,
		end:null,
		spu : {}
	};
	//初始化数据
	$scope.initData=function(){
		$scope.searchReportShare();
	};
	//搜索分享的病例
	$scope.searchReportShare=function(){
		$diyhttp.post("report!queryReportShareList.action",$scope.searchOpt,function(data){
			$scope.listReportShare=data.listReportShare;
			$scope.searchOpt.spu = data.spu;
		});
	};
}).controller("shareOrgCtrl",function($scope,$diyhttp,$msgDialog,$selectDialog){
	$scope.listReportShare=[];
	$scope.searchOpt={
		case_his_num:"",
		case_symptom:"",
		start:null,
		end:null,
		spu : {}
	};
	//初始化数据
	$scope.initData=function(){
		$scope.queryReportShareByThisOrg();
	};
	//取消分享
	$scope.cancelReportShare = function(reportId){
		$selectDialog.showMessage("你确定要取消分享吗？",function(){
			$diyhttp.post("report!cancelReportShare.action",{
				reportId:reportId
			},function(data){
				$scope.queryReportShareByThisOrg();
				$msgDialog.showMessage("取消成功");
			});
		});
	};
	//搜索分享的病例
	$scope.queryReportShareByThisOrg=function(){
		$diyhttp.post("report!queryReportShareByThisOrg.action",$scope.searchOpt,function(data){
			$scope.listReportShare=data.listReportShare;
			$scope.searchOpt.spu = data.spu;
		});
	};
}).controller("shareDetailCtrl",function($scope,$diyhttp,$msgDialog,$location,$writerDialog,$stateParams){
	$scope.reportShare=null;
	$scope.report=null;
	$scope.listSpeech=[];
	$scope.searchOpt={
		spu:{}
	};
	$scope.gotoReport=function(diagnosis){
		if (diagnosis.status!=5) {
			$msgDialog.showMessage("此诊断还未完成，暂时不能查看报告！");
			return ;
		}
		$location.path("report/detail/"+diagnosis.id);
	};
	
	$scope.initData=function(){
		$scope.selectReportShare();
	};
	$scope.selectReportShare=function(){
		$diyhttp.post("report!queryReportShareDetail.action",{
			reportShareId:$stateParams.reportShareId
		},function(data){
			$scope.reportShare=data.reportShare;
			$scope.report=data.report;
			$scope.listSpeech=data.listSpeech;
			$scope.searchOpt.spu=data.spu;
		});
	};
	//查询发言
	$scope.queryReportShareSpeech=function(){
		$diyhttp.post("report!queryReportShareSpeechByShareId.action",{
			reportShareId:$scope.reportShare.id,
			spu:$scope.searchOpt.spu
		},function(data){
			$scope.listSpeech=data.listSpeech;
			$scope.searchOpt.spu=data.spu;
		});
	};
	//添加发言
	$scope.writeSpeech=function(){
		$writerDialog.openWriting({
			title:"请填写",
			msg:"请填写发言内容：",
			lines:4,
			writing:true
		},function(note){
			$diyhttp.post("report!addReportShareSpeech.action",{
				reportShareId:$scope.reportShare.id,
				content:note
			},function(data){
				$scope.queryReportShareSpeech();
			});
		});
	};
});
