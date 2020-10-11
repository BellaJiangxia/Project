angular.module('diy.dialog', [ 'ngDialog',"vs.dialogs","services"])
.service("$dialogs",function(ngDialog,$vsDialogs){
	this.showSelectPatientInfoNumDialog=function(option,callBackOk,callBackNo){
		ngDialog.open({
			template:"../common/ngDialog/select.patientInfo.num.dialog.html",
			className : 'ngdialog-theme-default',
			plain : false,
			data : option,
			closeByEscape:true,
			showClose:true,
			closeByDocument:true,
			controller : function($scope,$diyhttp,$dialogs){
				$scope.listPatientInfoResult = $scope.ngDialogData;
				$scope.options = {
					tabIndex : 1,
				};
				$scope.onItemClick=function(gabEntity){
					if (!gabEntity) 
						return;
					if (!gabEntity.needCompletion) {
						$scope.closeThisDialog(gabEntity);
						return;
					}
					$scope.options.gabEntity = gabEntity;
					$scope.options.tabIndex = 2;
				};
				$scope.confirm=function(){
					if ($scope.options.tabIndex != 2 || ! $scope.options.gabEntity.value)
						return;
					$diyhttp.post("piShare!verificationCompletion.action",{
						uuid : $scope.options.gabEntity.uuid,
						value : $scope.options.gabEntity.value
					},function(){
						$scope.closeThisDialog($scope.options.gabEntity);
					});
				};
				$scope.onBackClick=function(){
					if ($scope.options.tabIndex != 2)
						return;
					$scope.options.gabEntity = null;
					$scope.options.itemIndex = null;
					$scope.options.tabIndex = 1;
				};
				$scope.cancel=function(){
					$scope.closeThisDialog();
				};
			}
		}).closePromise.then(function (data) {
		    if (data&&data.value) {
				if (callBackOk)callBackOk(data.value);
			}else {
				if (callBackNo)callBackNo();
			}
		});
	};
	this.showMessage=function(msg){
		$vsDialogs.showMessage(msg);
	};
	this.showConfirmDialog = function(msg,callbackYes,callbackNo){
		$vsDialogs.showConfirmDialog(msg,callbackYes,callbackNo);
	};
	this.showQualityControlFormsDialog = function(option,yes,no){
		ngDialog.open({
			template:"../common/ngDialog/qualityControl.froms.dialog.html",
			className : 'ngdialog-theme-default custom-width',
			plain : false,
			data : option,
			closeByEscape:true,
			showClose:false,
			closeByDocument:true,
			controller : function($scope,$diyhttp,$dialogs){
				$scope.listQualityControlForm =[];
				$scope.canIgnore=false;
//				$scope.canClose = ($scope.ngDialogData.from_type==2);
				$scope.option={
					msg:$scope.ngDialogData.msg,
					msg_class:$scope.ngDialogData.msg_class //text-info;text-warning;text-danger
				};
				$scope.initData=function(){
					var params = {};
					if ($scope.ngDialogData.qualityControlUid && $scope.ngDialogData.qualityControlUid.length>0) {
						params.qualityControlUid = $scope.ngDialogData.qualityControlUid;
					}else {
						params.form_answer_id = $scope.ngDialogData.form_answer_id;
					}
					$diyhttp.post("qualityControl!takeQualityControlFormByQcUidOrFormAnswerId.action",params,function(data){
						if (!data || !data.listQualityControlForm || data.listQualityControlForm.length<=0) {
							$scope.cancel();
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
				};
				$scope.ignoreForms=function(){
					if (!$scope.ngDialogData.qualityControlUid || $scope.ngDialogData.qualityControlUid.length<=0) {
						$scope.cancel();
						return;
					}
					$diyhttp.post("qualityControl!ignoreFormsByQcUid.action",{
						qualityControlUid:$scope.ngDialogData.qualityControlUid
					},function(data){
						$scope.cancel();
						if ($scope.ngDialogData.requestParams) {
							$diyhttp.post($scope.ngDialogData.requestParams.url,$scope.ngDialogData.requestParams.params,
									$scope.ngDialogData.requestParams.option.callbackOk,$scope.ngDialogData.requestParams.option.callbackNo,
									$scope.ngDialogData.requestParams.option.callbackFinal);
						}
					});
				};
				$scope.commitAnswer=function(){
					$diyhttp.post("qualityControl!commitQualityControlFormAnswer.action",{
						listQualityControlForm:$scope.listQualityControlForm
					},function(data){
						$vsDialogs.showMessage("提交成功！");
						$scope.cancel();
						if ($scope.ngDialogData.requestParams) {
							$diyhttp.post($scope.ngDialogData.requestParams.url,$scope.ngDialogData.requestParams.params,
									$scope.ngDialogData.requestParams.option.callbackOk,$scope.ngDialogData.requestParams.option.callbackNo,
									$scope.ngDialogData.requestParams.option.callbackFinal);
						}
					});
				};
				$scope.onScopeHover=function(ma,value){
					ma.percent = parseInt(value/5*100);
				};
				$scope.onScopeLeave=function(ma){
					ma.percent = parseInt(ma.score/5*100);
				};
				$scope.confirm=function(){
					$scope.closeThisDialog();
				};
				$scope.cancel=function(){
					$scope.closeThisDialog();
				};
			}
		}).closePromise.then(function (data) {
		    if (data&&data.value) {
				if (yes)yes(data.value);
			}else {
				if (no)no();
			}
		});
	};
	this.showSavePrivateTemplateDialog = function(option,yes,no) {
		ngDialog.open({
			template : '<div>'+
				'<div align="center"><label>请填写</label></div>'+
					'<div style="margin-left: 17px">'+
						'<div style="width: 100%">'+
							'<table style="width: 100%">'+
								'<tbody>'+
									'<tr>'+
										'<td align="right" width="25%">设备类型：<td><td width="25%">{{handle.device_type_name}}<td>'+
										'<td align="right" width="25%">部位类型：<td><td width="25%">{{handle.part_type_name}}<td>'+
									'</tr>'+
								'</tbody>'+
							'</table>'+
						'</div>'+
						'<br />'+
						'<div style="width: 100%">'+
							'模板名称：'+
							'<textarea class="form-control" rows="1" ng-model="name"></textarea>'+
						'</div>'+
						'<div style="width: 100%">'+
							'模板备注：'+
							'<textarea class="form-control" rows="3" ng-model="note"></textarea>'+
						'</div>'+
					'</div>'+
				'<div style="text-align: right;">'+
					'<p></p>'+
					'<div class="btn-group">'+
						'<a class="btn btn-info" href="javascript:void(0);" ng-click="confirm()" ng-if="name">'+
						'<i class="glyphicon glyphicon-ok"></i> 确定</a>'+
						'<a class="btn btn-warning" href ng-click="cancel()">'+
						'<i class="glyphicon glyphicon-remove"></i> 取消</a>'+
					'</div>'+
				'</div>'+
			'</div>',
			className : 'ngdialog-theme-default',
			plain : true,
			data : option,
			controller : function($scope,$diyhttp,$msgDialog){
				$scope.handle = $scope.ngDialogData.handle;
				$scope.name="";
				$scope.note="";
				$scope.confirm=function(){
					$diyhttp.post("sys!createPrivateTemplate.action",{
						template:{
							template_name:$scope.name,
							note:$scope.note,
							device_type_id:$scope.handle.device_type_id,
							part_type_id:$scope.handle.part_type_id,
							pic_opinion:$scope.handle.pic_opinion,
							pic_conclusion:$scope.handle.pic_conclusion
						}
					},function(data){
						$msgDialog.showMessage("保存成功！");
						$scope.closeThisDialog();
					});
				};
				$scope.cancel=function(){
					$scope.closeThisDialog();
				};
			}
		}).closePromise.then(function (data) {
		    if (data&&data.value) {
				if (yes)yes(data.value);
			}else {
				if (no)no();
			}
		});
	};
	this.showRequestDetailDialog=function(option){
		ngDialog.open({
			template : "../common/ngDialog/request.detail.dialog.html",
			className : 'ngdialog-theme-default report-detail-dialog-width',
			plain : false,
			showClose:false,
			data : option,
			controller : function($scope,$diyhttp){
				$scope.diagnosis =null;
				$scope.initData=function(){
					$diyhttp.post("diagnosis!queryFDiagnosisDetailById.action",{
						diagnosisId:$scope.ngDialogData.diagnosisId
					},function(data){
						$scope.diagnosis = data.diagnosis;
					});
				};
				$scope.cancel=function(){
					$scope.closeThisDialog();
				};
			}
		});
	};
	this.showReportDialog=function(option,yes,no){
		ngDialog.open({
			template : "../common/ngDialog/show.report.dialog.html",
			className : 'ngdialog-theme-default report-detail-dialog-width',
			plain : false,
			showClose:false,
			data : option,
			controller : function($scope,$diyhttp){
				if (!$scope.ngDialogData.report_id || $scope.ngDialogData.report_id<=0) {
					$scope.closeThisDialog();
					return;
				}
				$scope.report = null;
				$scope.initData = function() {
					$diyhttp.post("report!queryYuanzhenReportDetailById.action",{
						reportId : $scope.ngDialogData.report_id
					},function(data) {
						$scope.report = data.report;
					});
				};
				$scope.cancel=function(){
					$scope.closeThisDialog();
				};
			}
		});
	};
}).service("$imagesDialog",function(ngDialog) {
	this.show = function(fileMgr,yes,no) {
		ngDialog.open({
			template :"<div>"+
				"<img ng-src=\"common!image.action?imageId={{fileMgr.id}}\" width=\"100%\" alt=\"{{fileMgr.ori_name}}\" ng-click=\"confirm()\"/>"+
				"</div>",
			className : 'ngdialog-theme-default custom-width',
			plain : true,
			showClose:false,
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
}).controller("imagesDialogCtrl",function($scope){
	$scope.fileMgr=$scope.ngDialogData.fileMgr;
	$scope.confirm=function(){
		$scope.closeThisDialog();
	};
	$scope.initData=function(){
	};
}).service("$diagnosisDetailDialog",function(ngDialog) {
	this.showDialog = function(diagnosisId,yes,no) {
		ngDialog.open({
			template :"../common/ngDialog/diy.diagnosis.detail.dialog.html",
			className : 'ngdialog-theme-default custom-diagnosis-detail-width',
			plain : false,
//			size : 'lg',
			data : {
				diagnosisId:diagnosisId
			},
			controller : "diagnosisDetailDialogCtrl"
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
}).controller("diagnosisDetailDialogCtrl",function($scope, $diyhttp, $window,$msgDialog){
	// 数据区
	$scope.diagnosis = null;
	$scope.medicalHis = null;
	$scope.medicalHisImg = null;
	// 函数区
	$scope.initData = function() {
		$scope.queryFDiagnosisById($scope.ngDialogData.diagnosisId);
	};
	$scope.gotoBack = function() {
		$window.history.back();
	};
	$scope.queryFDiagnosisById = function(diagnosisId) {
		if (!diagnosisId) {
			$msgDialog.showMessage("无效的诊断ID！");
			return;
		}
		$diyhttp.post("diagnosis!queryFDiagnosisAndImgByDiagnosisId.action", {
			diagnosisId:diagnosisId
		},function(data) {
			$scope.diagnosis = data.diagnosis;
			$scope.medicalHis = data.medicalHis;
			$scope.medicalHisImg = data.medicalHisImg;
		});
	};
//	$scope.showBrowse=function(fileMgr){
//		$imagesDialog.show(fileMgr);
//	};
//	$scope.showDicom = function(markChar,orgAffixId) {
//		if (!ViewerMgr) {
//			$msgDialog.showMessage('WebWiewer控件未安装！');
//			return;
//		}
//		$dicom.openDicom(markChar,orgAffixId);
//	};
}).service("$tipDiagnosisDialog",function(ngDialog) {
	this.showMessage = function(diagnosisId,yes,no) {
		ngDialog.open({
			template :"../common/ngDialog/diy.diagnosis.dialog.html",
			className : 'ngdialog-theme-default custom-accept-request-width',
			plain : false,
			size : 'lg',
			showClose:false,
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
}).controller("diagnosisDialogCtrl",function($scope,$diyhttp){
	$scope.diagnosis=null;
	$scope.initData=function(){
		$diyhttp.post("diagnosis!queryFDiagnosisDetailById.action",{
			diagnosisId:$scope.ngDialogData.diagnosisId
		},function(data){
			$scope.diagnosis=data.diagnosis;
		});
	}
}).service("$msgDialog",function($vsDialogs) {
	this.showMessage = function(msg) {
		$vsDialogs.showMessage(msg);
	};
}).service("$writerDialog",function(ngDialog){
	this.openWriting = function(option,yes,no) {
		ngDialog.open({
			template : '<div>'+
				'<div align="center"><label>{{title}}</label></div>'+
					'<div style="margin-left: 17px">'+
						'{{show_Message}}'+
						'<textarea class="form-control" rows="{{lines}}" ng-model="note" ng-readOnly="!writing"></textarea>'+
					'</div>'+
				'<div style="text-align: right;">'+
					'<p></p>'+
					'<div class="btn-group btn-group-sm">'+
						'<a class="btn btn-success" href ng-click="confirm()" ng-if="note"><i class="glyphicon glyphicon-ok"></i> 确定</a>'+
						'<a class="btn btn-warning" href ng-click="cancel()">'+
						'<i class="glyphicon glyphicon-remove"></i> 取消</a>'+
					'</div>'+
				'</div>'+
			'</div>',
			className : 'ngdialog-theme-default',
			plain : true,
			data : option,
			controller : "writerCtrl"
		}).closePromise.then(function (data) {
		    if (data.value&&data.value.note) {
				if (yes) {
					yes(data.value.note);
				}
			}else {
				if (no) {
					no();
				}
			}
		});
	};
})
.service("$selectDialog",function($vsDialogs) {
	// 打开窗口
	this.showMessage = function(msg,yes,no) {
		$vsDialogs.showConfirmDialog(msg,yes,no);
//		ngDialog.open({
//			template : '<div>'+
//				'<div align="center"><label>请确认</label></div>'+
//				'<div style="margin-left: 20px">{{show_Message}}</div>'+
//				'<div style="text-align: right;">'+
//					'<p></p>'+
//					'<div class="btn-group btn-group-sm">'+
//						'<a class="btn btn-info" href="javascript:void(0);" ng-click="closeThisDialog(1)"> 确定</a>'+
//						'<a class="btn btn-warning" href="javascript:void(0);" ng-click="closeThisDialog(0)"> 取消</a>'+
//					'</div>'+
//				'</div>'+
//			'</div>',
//			className : 'ngdialog-theme-default',
//			plain : true,
//			data : {
//				msg:msg
//			},
//			controller : "selectCtrl"
//		}).closePromise.then(function (data) {
//		    if (data.value==1) {
//				if (yes) {
//					yes();
//				}
//			}else {
//				if (no) {
//					no();
//				}
//			}
//		});
	};
}).service("$medicalDetailDialog",function(ngDialog) {
	this.showDialog = function(caseHistoryId,yes,no) {
		ngDialog.open({
			template :"../common/ngDialog/diy.caseHistory.detail.dialog.html",
			className : 'ngdialog-theme-default custom-diagnosis-detail-width',
			plain : false,
//			size : 'lg',
			data : {
				caseHistoryId:caseHistoryId
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
	$scope.caseHistory = {};
	$scope.patient={};
	$scope.listDicomImg = [];
	$scope.initData=function(){
		if (!$scope.ngDialogData.caseHistoryId){
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
		$http.post("case!queryCaseHistoryById.action", {
			caseId : $scope.ngDialogData.caseHistoryId
		}).success(function(data) {
			if (data.code == 0) {
				$scope.caseHistory = data.data.caseHistory;
				$scope.patient = data.data.patient;
				$scope.listDicomImg = data.data.listDicomImg;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.showDicomByImgId = function(dicom_img_id) {
        $dicom.openDicoms({
            dicom_img_id:dicom_img_id
        });
	};
}).controller("writerCtrl",function($scope){
	$scope.title = $scope.ngDialogData.title;
	$scope.show_Message = $scope.ngDialogData.msg;
	$scope.writing=$scope.ngDialogData.writing;
	$scope.note=$scope.ngDialogData.note;
	$scope.lines=$scope.ngDialogData.lines?$scope.ngDialogData.lines:3;
	$scope.confirm=function(){
		$scope.closeThisDialog({note:$scope.note});
	};
	$scope.cancel=function(){
		$scope.closeThisDialog();
	};
});