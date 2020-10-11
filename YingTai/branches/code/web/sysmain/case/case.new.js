angular.module("case.new", [ "ui.bootstrap",'ui.router',"diy.dialog","services", "diy.selector","diy.uploadfile" ])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider
	.state("case.new", {
		url : "/new",
		templateUrl : "case/case.new.html",
		controller : "caseNewCtrl"
	})
	.state("case.modify",{
		url : "/modify/:caseId",
		templateUrl : "case/case.modify.html",
		controller : "caseModifyCtrl"
	});
})
.controller("caseNewCtrl",function($scope,$diyhttp,$location,$msgDialog,$selectDialog,$queryService,$location,$uploadfile,$commitDiagnosisRequestModal){
	$scope.mode=1;
	$scope.searchHistoryCollapsed=false;
	$scope.listCaseHistory=[];
	$scope.searchCaseHistoryTimes = 0;
	$scope.remoteParam=null;
	$scope.listDicomImgNumObj =[];
	$scope.initData=function(){
		$diyhttp.post("dicomImg!queryLastDicomImgNum.action",{
		},function(data){
			if (data.listDicomImgNumObj && data.listDicomImgNumObj.length>0) {
				$scope.listRemoteParamsType = data.listDicomImgNumObj;
				$scope.remoteParam=$scope.listRemoteParamsType[0];
			}else {
				$queryService.queryRemoteParamType(function(listRemoteParamsType){
					if (listRemoteParamsType && listRemoteParamsType.length>0)
						$scope.remoteParam=listRemoteParamsType[0];
				});
			}
		});
	};
	// 搜索病例
	$scope.queryPatientData = function() {// MedicalNum
		$diyhttp.post("case!searchCaseHistoryGreat.action", {
			remoteParamsType:$scope.remoteParam.code,
			remoteParamsValue:$scope.remoteParam.remoteParamValue
		},function(data) {
			if (!data.listCaseHistory||data.listCaseHistory.length<=0){
				$msgDialog.showMessage("暂时未找到病例信息！");
			}else{
				if (data.listCaseHistory&&data.listCaseHistory.length>0)
					$location.path("case/modify/"+data.listCaseHistory[0].id);
			}
		});
	};
	$scope.selectRemoteParamClick=function(r){
		$scope.remoteParam = r;
	};
}).controller("caseModifyCtrl",function($scope, $diyhttp,$msgDialog,$selectDialog,$uploadfile,
		$commitDiagnosisRequestModal,$stateParams,uibDateParser,$queryService){
	$scope.mode=2;
	$scope.patient=null;
	$scope.caseHistory=null;
	$scope.listDicomImg=[];
	
	$scope.selectedDicomImg=null;
	
	$scope.setSelectImg=function(img){
		$scope.selectedDicomImg=img;
	};
	$scope.listCheckPro=[];
	$scope.diyCheckPro="";
	$scope.diagnosis={};
	$scope.selectOpt={};
	$scope.new_medical_his = {
		eafFiles:[]
	};
	$scope.editbtntext="编辑";
	$scope.dynamicPopover={
		templateUrl: 'myPopoverTemplate.html',
	};
	$scope.initData=function(){
		$queryService.queryCaseHistoryType();
		$diyhttp.post("patientInfo!queryCaseHistoryDetailByCaseId.action", {
			caseId:$stateParams.caseId
		},function(data) {
			$scope.patient = data.patient;
			if($scope.patient&&$scope.patient.birthday&&!angular.isDate($scope.patient.birthday)){
				$scope.patient.birthday = $scope.jsonStrToDate($scope.patient.birthday);
			}
			$scope.caseHistory = data.caseHistory;
			$scope.listDicomImg = data.listDicomImg;
			if ($scope.listDicomImg&&$scope.listDicomImg.length==1)
				$scope.selectedDicomImg=$scope.listDicomImg[0];
		});
	};
	$scope.addDiyCheckPro=function(img,diyCheckPro){
		if (!img)return;
		if (!img.listCheckPro)img.listCheckPro=[];
		for (var int = 0; int < img.listCheckPro.length; int++) {
			var cp = img.listCheckPro[int];
			if (cp.name==diyCheckPro) {
				$msgDialog.showMessage("不能重复添加！");
				return;
			}
		}
		img.listCheckPro.push({
			id:0,
			name:diyCheckPro,
			selected:true,
			custom:true
		});
	};
	//修改一个部位
	$scope.modifyBodyPartType=function(img,bpt){
		if(!bpt)
			return;
		$diyhttp.post("sys!addBodyPartType.action", {
			partTypeName:bpt.value,
			deviceTypeId:img.device_type_id
		},function(data){
			if(!data.bodyPartType)
				return;
			if(!img.listBodyPartType)
				img.listBodyPartType = [];
			for (var int = img.listBodyPartType.length-1; int >=0 ; int--) {
				var bodyPartType = img.listBodyPartType[int];
				if (bodyPartType.id == bpt.id){
					img.listBodyPartType.splice(int,1,data.bodyPartType);
					return;
				}
			}
		});
	};
	//删除一个部位
	$scope.onRemoveImgBodyPartType=function(img,bodyPartType){
		$selectDialog.showMessage("你确定要删除此部位吗？",function() {
			if(!img.listBodyPartType)
				img.listBodyPartType = [];
			for (var int = img.listBodyPartType.length-1; int >=0 ; int--) {
				var dpt = img.listBodyPartType[int];
				if (dpt.id == bodyPartType.id)
					img.listBodyPartType.splice(int,1);
			}
		});
	};
	//添加部位
	$scope.addBodyPartType=function(img,diyBodyPartType){
		if (!diyBodyPartType)
			return;
		$diyhttp.post("sys!addBodyPartType.action", {
			partTypeName:diyBodyPartType,
			deviceTypeId:img.device_type_id
		},function(data){
			if(!data.bodyPartType)
				return;
			if(!img.listBodyPartType)
				img.listBodyPartType = [];
			for (var int = 0; int < img.listBodyPartType.length; int++) {
				var bodyPartType = img.listBodyPartType[int];
				if (bodyPartType.id == data.bodyPartType.id)
					return;
			}
			img.listBodyPartType.push(data.bodyPartType);
		});
	};
	//保存病例
	$scope.savePatientInfo = function(callback) {
		$diyhttp.post("patientInfo!savePatientInfo.action", {
			caseHistory : $scope.caseHistory,
			patient : $scope.patient,
			listDicomImg:$scope.listDicomImg
		},function(data) {
			if (callback)
				callback();
			else
				$msgDialog.showMessage("保存成功");
		});
	};
	//提交申请
	$scope.savePatientInfoAndCommitRequest = function(request_class) {
		$scope.savePatientInfo(function(){
			$scope.commitRequest($scope.caseHistory,$scope.selectedDicomImg,request_class);
		});
	};
	//同步图像
	$scope.syncImg=function(){
		$diyhttp.post("query_dicom!execute.action", {
			medicalHisNum : $scope.new_medical_his.medical_his_num
		},function(data) {
			if (!$scope.patient){
				$scope.patient=data.patient;
				return;
			}
			if (!$scope.patient.dicom_list||$scope.patient.dicom_list.length<=0){
				$scope.patient.dicom_list=data.patient.dicom_list;
				return;
			}
			if (data.patient.dicom_list&&data.patient.dicom_list.length>0) {
				la:for (var int = 0; int < data.patient.dicom_list.length; int++) {
					var qDicom=data.patient.dicom_list[int];
					lb:for (var int2 = 0; int2 < $scope.patient.dicom_list.length; int2++) {
						var lDicom=$scope.patient.dicom_list[int2];
						if (lDicom.mark_char==qDicom.mark_char)
							continue la;
					}
					$scope.patient.dicom_list.push(qDicom);
				}
			}
		});
	};
	//选择文件
	$scope.selectFile=function(){
		$uploadfile.selectFile(function(arrayFileMgr){
			if (arrayFileMgr&&arrayFileMgr.length>0) {
				if (!$scope.caseHistory.eafFiles)
					$scope.caseHistory.eafFiles=[];
				for (var int = 0; int < arrayFileMgr.length; int++) {
					$scope.caseHistory.eafFiles.push(arrayFileMgr[int]);
				}
			}
		});
	};
	//删除文件
	$scope.removeFile=function(index){
		var fmr=$scope.caseHistory.eafFiles[index];
		$diyhttp.post("file!deleteFileById.action",{
			fileId:fmr.id
		},function(data){
			$scope.caseHistory.eafFiles.splice(index,1);
		});
	};
});







