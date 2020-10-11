angular.module("diagnosis.handle",["services","diy.selector","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("diagnosis.handle", {
		url : "/handle",
		template : "<div ui-view></div>",
		controller : "diagnosisHandleCtrl",
	}).state("diagnosis.handle.writing",{
		url : "/writing",
		templateUrl : "diagnosis/handle/diagnosis.handle.writing.html",
		controller : "diagnosisHandleWritingCtrl",
	});
}).controller("diagnosisHandleCtrl",function(){
	
}).filter("diagnosisHandleWritingExcludeCurrFilter",function(){
	return function(input,handleId){
		if (!input|| !(input instanceof Array) || input.length<=0)
			return;
		
		return input;
	};
}).controller("diagnosisHandleWritingCtrl",function($scope,$diyhttp,$location,$msgDialog,$modals,$dialogs,
		$writerDialog,$selectDialog,$selectTranferUserModal,$electList){
	$scope.diagnosis=null;
	$scope.diagnosisHandle =null;
	$scope.arrayDiagnosisHandle=[];
	$scope.dicomImg=null;
	$scope.historyCaseHistory=[];
	
	
	$scope.needQualityControl = true;//是否需要显示质控遮罩
	$scope.qualityControlFormCancelClick=function(){
		$scope.needQualityControl = false;
	};
	$scope.queryNeedQualityControlByDicomId=function(){
		$diyhttp.post("qualityControl!needQualityControlByTargetTypeAndTargetId.action",{
			target_type:10,
			target_id:$scope.dicomImg.id
		},function(data){
			if (data.qualityControlUid && data.qualityControlUid.length>0) {
				$scope.$broadcast('to-qualityControlUid', data.qualityControlUid);
			}else
				$scope.qualityControlFormCancelClick();
		});
	};
	
//	//查看病例详情
//	$scope.saveHandleAndViewDiagnosisDetail=function(){
//		$scope.saveHandle(function(){
//			$location.path("request/detail/"+$scope.diagnosis.id);
//		});
//	};
	//检查报告
	$scope.checkDiagnosis=function(callOk,callNo){
		var maxopLine=15,maxcoLine=8,maxWordCount=44;
		if(!$scope.diagnosisHandle.pic_opinion&&!$scope.diagnosisHandle.pic_conclusion){
			if (callOk)callOk();
			return;
		}
		var formatStr=function(srcStr){
			if (! srcStr)return "";
			if (! srcStr instanceof String)return null;
			var sstr=srcStr.replace(new RegExp('\r\n',"gm"),'\n');
			return srcStr.split('\n');
		};
		var arrPic_opinion=formatStr($scope.diagnosisHandle.pic_opinion);
		if (arrPic_opinion.length>maxopLine) {
			$selectDialog.showMessage("你的影像所见行数较多，在下级机构打印时有可能造成格式混乱！请问您是否要继续？",callOk,callNo);
			return;
		}
		var line=0;
		for (var int = 0; int < arrPic_opinion.length; int++) {
			var pop=arrPic_opinion[int];
			var num= pop.length/maxWordCount;
			if (pop.length%maxWordCount>0) {
				num=num+1;
			}
			line+=num;
		}
		if (line>maxopLine) {
			$selectDialog.showMessage("你的影像所见字数较多，在下级机构打印时有可能造成格式混乱！请问您是否要继续？",callOk,callNo);
			return;
		}
		var arrpic_conclusion=formatStr($scope.diagnosisHandle.pic_conclusion);
		if (arrpic_conclusion.length>maxcoLine) {
			$selectDialog.showMessage("你的诊断意见行数较多，在下级机构打印时有可能造成格式混乱！请问您是否要继续？",callOk,callNo);
			return;
		}
		var line=0;
		for (var int = 0; int < arrpic_conclusion.length; int++) {
			var pop=arrpic_conclusion[int];
			var num= pop.length/maxWordCount;
			if (pop.length%maxWordCount>0) {
				num=num+1;
			}
			line+=num;
		}
		if (line>maxcoLine) {
			$selectDialog.showMessage("你的诊断意见字数较多，在下级机构打印时有可能造成格式混乱！请问您是否要继续？",callOk,callNo);
			return;
		}
		if (callOk)callOk();
	};
	
	//初始化数据
	$scope.initData=function(){
		$scope.searchMyUnFinishHandle();
	};
	var canExecute=function(){
		if (! $scope.diagnosisHandle) {
			$msgDialog.showMessage("你当前没有正在处理诊断！");
			$scope.goback();
			return false;
		}
		return true;
	};
	//回退处理
	$scope.backHandle=function(){
		if (!canExecute())
			return;
		$selectDialog.showMessage("回退诊断将退回到待诊断的状态，机构其他用户可以再次接受诊断！你确定要回退吗？",function(){
			$diyhttp.post("diagnosis!revokeDiagnosis.action",{
				handleId:$scope.diagnosisHandle.id
			},function(data) {
				$msgDialog.showMessage("回退成功！");
				$scope.goback();
			});
		});
	};
	//查询我的未处理
	$scope.searchMyUnFinishHandle = function() {
		$diyhttp.post("diagnosis!queryUnFinishHandle.action",{
		},function(data) {
			if (!data.handle) {
				$msgDialog.showMessage("你当前没有正在处理诊断！");
				$scope.goback();
				return;
			}
			$scope.diagnosisHandle=data.handle;
			$scope.diagnosis = data.diagnosis;
			$scope.dicomImg = data.dicomImg;
			if ($scope.dicomImg) {
				$scope.queryNeedQualityControlByDicomId();
			}
			$scope.historyCaseHistory = data.listCaseHistory;
			$scope.arrayDiagnosisHandle = data.listHandle;
			if ($scope.arrayDiagnosisHandle && ($scope.arrayDiagnosisHandle instanceof Array) 
					&& $scope.arrayDiagnosisHandle.length>0 && $scope.diagnosisHandle){
				for (var int = $scope.arrayDiagnosisHandle.length-1; int >=0 ; int--) {
					var obj = $scope.arrayDiagnosisHandle[int];
					if (!obj)
						$scope.arrayDiagnosisHandle.splice(int,1);
					if (obj.id == $scope.diagnosisHandle.id)
						$scope.arrayDiagnosisHandle.splice(int,1);
				}
			}
		});
	};
	//移交诊断
	$scope.tranferDiagnosis=function(){
		if (!canExecute())return;
		$scope.checkDiagnosis(function(){
			$selectTranferUserModal.open({
				size:'',
				diagnosisId:$scope.diagnosisHandle.diagnosis_id
			},function(user_id){
				if (user_id<0)return ;
				$diyhttp.post("diagnosis!tranferDiagnosis.action",{
					diagnosisHandle:$scope.diagnosisHandle,
					userId:user_id
				},function(data) {
					// 根据返回的列表填充
					$msgDialog.showMessage("移交成功！");
					$scope.goback();
				});
			});
		});
	};
	//保存处理
	$scope.saveHandle=function(callOk){
		if (!canExecute()) {
			return;
		}
		$diyhttp.post("diagnosis!saveOpinion.action",{
			diagnosisHandle:$scope.diagnosisHandle
		},function(data) {
			if (callOk) {
				callOk();
			}else
				$msgDialog.showMessage("保存成功！");
		});
	};
	//选择模板
	$scope.selectTemplate=function(){
		$modals.openSelectTemplateModal({},function(obj){
			if (obj.type=='plus') {
				if ($scope.diagnosisHandle.pic_conclusion) {
					$scope.diagnosisHandle.pic_conclusion += "\r\n" + obj.template.pic_conclusion;
				}else {
					$scope.diagnosisHandle.pic_conclusion = obj.template.pic_conclusion;
				}
				if ($scope.diagnosisHandle.pic_opinion) {
					$scope.diagnosisHandle.pic_opinion += "\r\n" + obj.template.pic_opinion;
				}else {
					$scope.diagnosisHandle.pic_opinion = obj.template.pic_opinion;
				}
			}else if (obj.type=='apply') {
				$scope.diagnosisHandle.pic_conclusion = obj.template.pic_conclusion;
				$scope.diagnosisHandle.pic_opinion = obj.template.pic_opinion;
			}
		});
	};
	//存为模板
	$scope.saveTemplate=function(){
		$dialogs.showSavePrivateTemplateDialog({
			handle:$scope.diagnosisHandle
		},function(retu){
		});
	};
	//应用到报告
	$scope.applyToReport=function(handle){
		$scope.diagnosisHandle.pic_conclusion=handle.pic_conclusion;
		$scope.diagnosisHandle.pic_opinion=handle.pic_opinion;
		$scope.diagnosisHandle.f_o_m=handle.f_o_m;
	};
	//发布报告
	$scope.publishReport=function(){
		$scope.checkDiagnosis(function(){
			$selectDialog.showMessage("发布报告将完成整个诊断流转过程，你确定要发布此报告吗？",function(){
				$diyhttp.post("diagnosis!publishReport.action",{
					diagnosisHandle:$scope.diagnosisHandle
				},function(data) {
					$msgDialog.showMessage("发布成功！");
					$scope.goback();
				});
			});
		});
	};
});