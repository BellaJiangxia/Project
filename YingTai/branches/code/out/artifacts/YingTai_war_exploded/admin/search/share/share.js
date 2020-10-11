angular.module("share", [ "ui.bootstrap",'ui.router',"ui.bootstrap.pagination","diy.dialog","medical"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("share",{url :"/share",abstract : true,
		template :"<div ui-view></div>",controller : "shareCtrl"
	})
	.state("share.list", {
		url : "/list",
		templateUrl : "search/share/share.list.html",
		controller : "sharelistCtrl"
	}).state("share.detail", {
		url : "/detail/:diagnosisShareId",
		templateUrl : "search/share/share.detail.html",
		controller : "sharedetailCtrl"
	});
}).controller("shareCtrl",function($scope){
//
}).controller("sharelistCtrl", function($scope,$http,$msgDialog) {
/////////////////////////////////////////////////////
	$scope.listDiagnosisShare=[];
	$scope.searchOpt={
		shareOrgName:"",
		symptom : "",
		start:null,
		end:null,
		spu : {
			currPage : 0,
			perPageCount : 10
		}
	};
	//初始化数据
	$scope.initData=function(){
		$scope.searchDiagnosisShare();
	};
	//搜索分享的病例
	$scope.searchDiagnosisShare=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		$http.post("diagnosis!queryDiagnosisShareList.action",$scope.searchOpt)
		.success(function(result){
			if (result.code==0) {
				$scope.listDiagnosisShare=result.data.listDiagnosisShare;
				$scope.searchOpt.spu = result.data.spu;
			}else {
				$msgDialog.showMessage(result.name);
			}
		});
	};
}).controller("sharedetailCtrl",function($scope,$http,$msgDialog,$location,$writerDialog,$stateParams){
////////////////////////////////////////////////////////////////
	if (! $stateParams.diagnosisShareId) {
		$msgDialog.showMessage("无效的分享！");
		$scope.$parent.goback();
		return;
	}
	$scope.medicalHis=null;
	$scope.diagnosisShare=null;
	$scope.diagnosis=null;
	$scope.listSpeech=[];
	$scope.searchOpt={
		spu:{}
	};
	$scope.gotoReport=function(diagnosis){
		if (diagnosis.status!=5) {
			$msgDialog.showMessage("此诊断还未完成，暂时不能查看报告！");
			return ;
		}
		$location.path("diagnosis/report/"+diagnosis.id);
	};
	$scope.initData=function(){
		$scope.selectDiagnosisShare();
	};
	$scope.selectDiagnosisShare=function(){
		$http.post("diagnosis!selectDiagnosisShareDetail.action",{
			diagnosisShareId:$stateParams.diagnosisShareId
		}).success(function(data){
			if (data.code==0) {
				$scope.medicalHis=data.data.medicalHis;
				$scope.diagnosisShare=data.data.diagnosisShare;
				$scope.diagnosis=data.data.diagnosis;
				$scope.listSpeech=data.data.listSpeech;
				$scope.searchOpt.spu=data.data.spu;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	//查询发言
	$scope.queryDiagnosisShareSpeech=function(){
		$http.post("diagnosis!queryFDiagnosisShareSpeechByShareId.action",{
			diagnosisShareId:$scope.diagnosisShare.id,
			spu:$scope.searchOpt.spu
		}).success(function(data){
			if (data.code==0) {
				$scope.listSpeech=data.data.listSpeech;
				$scope.searchOpt.spu=data.data.spu;
			}else {
				$msgDialog.showMessage(data.name);
			}
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
			$http.post("diagnosis!addDiagnosisShareSpeech.action",{
				diagnosisShareId:$scope.diagnosisShare.id,
				content:note
			}).success(function(data){
				if (data.code==0) {
					$scope.queryDiagnosisShareSpeech();
					$msgDialog.showMessage("发言成功！");
				}else {
					$msgDialog.showMessage(data.name);
				}
			});
		});
	};
});
