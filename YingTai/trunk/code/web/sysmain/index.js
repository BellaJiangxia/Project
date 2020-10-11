angular.module("main",[ "ui.router","ngAnimate","ui.bootstrap","vastsoft.directives","vastsoft.filters","vastsoft.yingtai.controllers",
                        "myDirectives","filters",
                        "services","diy.dialog","diy.selector","yingtai.common",
                        "diagnosis","request","report","case","share","org","dicom","stat","template", "returnvisit",
                        "returnvisit.batch", "returnvisit.template","patientInfoShare","qualityControl","inquiry","requestTranfer","reserveService","reservation"])
.constant('HTML5_VIEWER_URL',"http://localhost:8080/YingTaiDSS/wado/index.html")
.run(function($rootScope,$window,$location,$state,$diyhttp,$imagesDialog,$diagnosisDetailDialog,
		$medicalDetailDialog,$selectDialog,$msgDialog,$dicom,$dialogs,$templateCache) {
	$templateCache.put("templates/vs-accordion-heading.tamplate.html",'<div class="panel panel-default">'+
			'<div class="panel-heading" class="accordion-toggle" ng-click="toggleOpen()" style="cursor: pointer;">'+
			'<span class="pull-right">&nbsp;<i ng-class="isOpen?\'glyphicon glyphicon-chevron-up\':\'glyphicon glyphicon-chevron-down\'"></i></span>'+
			'<h4 class="panel-title" style="color:white;" uib-accordion-transclude="heading">'+
			'<span uib-accordion-header ng-class="{\'text-muted\': isDisabled}">'+
				'{{heading}}'+
			'</span></h4></div>'+
			'<div class="panel-collapse collapse" uib-collapse="!isOpen">'+
			'<div class="panel-body" ng-transclude></div>'+
			'</div>'+
			'</div>');
	$rootScope.goback = function() {
		$window.history.back();
	};
	$rootScope.showBrowse = function(fileMgr) {
		$imagesDialog.show(fileMgr);
	};
	$rootScope.showRequestDetailDialog=function(option){
		$dialogs.showRequestDetailDialog(option);
	};
	$rootScope.jsonStrToDate=function(a) {
		try {
			if (a&&(typeof a=="string"))
				return new Date(a.replace("-", "/").replace("-", "/").replace("T", " "))
		} catch (e) {
			return null;
		}
	}
	$rootScope.arrayPart = [];
	$rootScope.listTimeLimit=[];
	$rootScope.listDiagnosisStatus=[];
	$rootScope.queryDiagnosisStatus=function(){
		$diyhttp.post("common!queryDiagnosisStatus.action",{},function(data){
			$rootScope.listDiagnosisStatus=data.listDiagnosisStatus;
		});
	};
	$rootScope.queryTimeLimit=function(){
		$diyhttp.post("common!queryTimeLimit.action",{},function(data){
			$rootScope.listTimeLimit=data.listTimeLimit;
		});
	};
	$rootScope.currUser = null;
	$rootScope.currOrg = null;
	$rootScope.user = {};
	$rootScope.org = {};
	$rootScope.listGender=[{
		code:1,
		name:"男"
	},{
		code:2,
		name:"女"
	}];
	// 获取指定设备类型下的部位类型
	$rootScope.queryPartList = function(device_type_id,callOk,callNo) {
		if (!device_type_id||device_type_id<=0) {
			if (callNo)callNo();
			return;
		}
		$diyhttp.post("sys!queryPartList.action", {
			parentId : device_type_id
		},function(data) {
			$rootScope.arrayPart = data.listDicValue;
			if (callOk)callOk(data.listDicValue);
		},function(){
			if (callNo)callNo();
		});
	};
	$rootScope.queryCurrUser = function() {
		$diyhttp.post("user!currentUser.action",{},function(data) {
			$rootScope.user.id = data.curr_user.userId;
			$rootScope.user.name = data.curr_user.userName;
			$rootScope.user.userType = data.curr_user.userType;
			$rootScope.org.id = data.curr_user.orgId;
			$rootScope.org.name = data.curr_user.orgName;
			$rootScope.user.isAuditer = data.isAuditer;
			$rootScope.user.isMedicalHisMgr = data.isMedicalHisMgr;
			$rootScope.user.isOrgMgr = data.isOrgMgr;
			$rootScope.user.isReportor = data.isReportor;
			$rootScope.user.photo_file_id = data.logo_id;
			
			$rootScope.currOrg = data.currOrg;
			$rootScope.currUser = data.curr_user;
			$rootScope.currUser.isAuditer = data.isAuditer;
			$rootScope.currUser.isMedicalHisMgr = data.isMedicalHisMgr;
			$rootScope.currUser.isOrgMgr = data.isOrgMgr;
			$rootScope.currUser.isReportor = data.isReportor;
			$rootScope.currUser.photo_file_id = data.logo_id;
		});
	};
	// 查看申请详情
	$rootScope.viewDiagnosisDetail = function(diagnosisId) {
		$diagnosisDetailDialog.showDialog(diagnosisId);
	};
	//查看病例详情
	$rootScope.showMedicalHisDetail=function(medicalHis){
		if (angular.isObject(medicalHis)) {
			$medicalDetailDialog.showDialog(medicalHis.id);
		}else if (angular.isNumber(medicalHis)) {
			$medicalDetailDialog.showDialog(medicalHis);
		}
	};
	$rootScope.viewDiagnosisReport=function(reportId){
		$rootScope.viewReportDetail(reportId);
	};
	$rootScope.viewReportDetail=function(reportId){
		$diyhttp.post("report!viewReport.action",{reportId:reportId},function(data){
			if (data&&data.report) {
				$location.path("report/detail/yuanzhen/"+reportId);
			}
		});
	};
	$rootScope.showReportDialog=function(option){
		$dialogs.showReportDialog(option);
	};
	/*分享报告*/
	$rootScope.shareReport=function(reportId){
		$selectDialog.showMessage("分享之后所有人都可以查看此申请的相关信息，请再次确定？",function(){
			$diyhttp.post("report!shareReport.action",{
				reportId:reportId
			},function(data) {
				$msgDialog.showMessage("分享成功！");
			});
		});
	};
	$rootScope.showDicoms = function(option) {
		$dicom.openDicoms(option);
	};
	$rootScope.showDicom = function(markChar,orgAffixId) {
		$dicom.openDicom(markChar,orgAffixId);
	};
    $rootScope.showDicomByImgId = function(dicom_img_id) {
        $dicom.openDicoms({
            dicom_img_id:dicom_img_id
		});
    };
	//$rootScope.$broadcast("testtestevent","123123123");
}).controller("mainCtrl",function($scope,$state,$http,$window,$interval,$electList,
		$msgDialog,$selectDialog,$imagesDialog,$diyhttp,$patientInfoShareSelectOrgModal) {
	$scope.innerPatientInfoShareModule=function(){
		$patientInfoShareSelectOrgModal.open({},function(orgRelation){
			$state.go("patientInfoShare.begin",{
				relation_org_id:orgRelation.relation_org_id,
				relation_org_name:orgRelation.relation_org_name
			});
		});
	};
	$scope.showElectMedicalList=function(medicalHisNum){
		$electList.open({medicalHisNum:medicalHisNum},function(){
			
		});
	};
	$scope.logout = function() {
		$http.post("user!logout.action").success(
			function(data, status, headers, config) {
				if (data.code === 0) {
					$window.location.href = '../login';
				}
			});
	};
//	$scope.recvCall=function(){
//		if (!$scope.newCall) {
//			$msgDialog.showMessage("你没有新的呼叫！");
//			return;
//		}
//		$scope.newCall=false;
//		$http.post("video!recvVideoComm.action").success(function(data){
//			if (data.code==0) {
//				if (data.data.videoCall) {
//					window.open("./common/anychat/index.html?user=YT_"
//							+ $scope.user.name+"&room="+data.data.videoCall.chatRoom, "_blank","left=0,top=0,  location=no, toolbar=no, menubar=no");
//			
//				}else {
//					$scope.newCall=false;
//					$scope.videoCall=null;
//				}
//			}else{
//				$scope.newCall=false;
//				$scope.videoCall=null;
//			}
//		});
//	};
//	$scope.videoCall=null;
//	$scope.newCall=false;
//	$scope.classIndex=1;
//	var t3=$interval(function(){
//		if ($scope.newCall) {
//			if ($scope.classIndex==1) {
//				$scope.classIndex=2;
//			}else {
//				$scope.classIndex=1;
//			}
//		}
//	},700);
//	var t2=$interval(function(){
//		if ($scope.newCall) {
//			if ($scope.classIndex==1) {
//				$scope.classIndex=2;
//			}else {
//				$scope.classIndex=1;
//			}
//		}
//		$http.post("video!queryVideoComm.action").success(function(data){
//			if (data.code==0) {
//				if (data.data.videoCall) {
//					$scope.newCall=true;
//					$scope.videoCall=data.data.videoCall;
//					console.log(data.data.videoCall);
//				}else {
//					$scope.newCall=false;
//					$scope.videoCall=null;
//				}
//			}else {
//				$scope.newCall=false;
//				$scope.videoCall=null;
//			}
//		});
//	},3000);
	//-----------------------------------------------------------------
	$scope.diagnosisCount={
		waitDiagnosisHandleCount:0,
		unreadDiagnosisMsgCount:0,
		unreadRequestMsgCount:0,
		joiningUserCount:0,
		joiningOrgCount:0,
		unfinish:false,
		unhandle:{
			unAuditCount:0,
			unAcceptHandleCount:0
		},
		modifyRequestCount:0,
		modifyConfirmCount:0,
		newInquiryCount:0,
		visitFeedbackCount:0,
		unviewed_report_count:0,
		reserveCount:0
	};
	var t=$interval(function(){
		$scope.queryDiagnosisCount();
	},15000);
	$scope.initData=function(){
		$scope.queryCurrUser();
		$scope.queryDiagnosisCount();
	};
	$scope.queryDiagnosisCount=function(){
		$diyhttp.post("timer!timerQuery.action",{
			httpConfig:{showSpinner:false}
		},function(data){
			$scope.diagnosisCount.waitDiagnosisHandleCount=data.waitDiagnosisHandleCount;
			$scope.diagnosisCount.unreadDiagnosisMsgCount=data.unreadDiagnosisMsgCount;
			$scope.diagnosisCount.unreadRequestMsgCount=data.unreadRequestMsgCount;
			$scope.diagnosisCount.unfinish=data.unfinish;
			$scope.diagnosisCount.unhandle.unAuditCount=data.unAuditCount;
			$scope.diagnosisCount.unhandle.unAcceptHandleCount=data.unAcceptHandleCount;
			$scope.diagnosisCount.modifyRequestCount=data.modifyRequestCount;
			$scope.diagnosisCount.modifyConfirmCount=data.modifyConfirmCount;
			$scope.diagnosisCount.joiningUserCount=data.joining_user_count;
			$scope.diagnosisCount.joiningOrgCount=data.joining_org_count;
			$scope.diagnosisCount.newInquiryCount=data.newInquiryCount;
			$scope.diagnosisCount.unviewed_report_count = data.unviewed_report_count;
			$scope.diagnosisCount.reserveCount=data.reserveCount;
		});

		$diyhttp.post("returnvisit!visitFeedbackCount.action",{
			httpConfig:{showSpinner:false}
		},function(result) {
			$scope.diagnosisCount.visitFeedbackCount=result.count;
		});
	};
	// ------------------------------------------------------------------------------
	$scope.arrayOrg = [];
	$scope.arrayDoctors = [];
	$scope.arrayVerityUser = [];
	// 查询机构下的所有审核员
	$scope.queryVerityUserInOrg = function(){
		$http.post("diagnosis!queryVerityUserInOrg.action")
		.success(function(data) {
			if (data.code == 0) {
				$scope.arrayVerityUser = data.data.listVerityUser;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	// 查询本机构下的所有医生
	$scope.queryDoctorsInOrg = function() {
		$http.post("diagnosis!queryDoctorsInOrg.action")
		.success(function(data) {
			if (data.code == 0) {
				$scope.arrayDoctors = data.data.listDoctor;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	// 查询我机构的所有好友机构
	$scope.queryArrayOrg = function() {
		$http.post("org!queryMyOrgValidFriends.action")
		.success(function(data) {
			if (data.code == 0) {
				$scope.arrayOrg = data.data.listOrg;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	
	$scope.queryCheckProByImg=function(img){
		if (! img)return [];
		$diyhttp.post("sys!queryCheckProListByPartTypeName.action",{
			partTypeName:img.part_type,
			deviceTypeId:img.device_type_id
		},function(data){
			img.sysCheckPro=data.listDicValue; 
		},function(){
			img.sysCheckPro=[];
		});
	};
	$scope.$on('$destroy', function() {
		if (angular.isDefined(t)) {
		    $interval.cancel(t);
		    $interval.cancel(t2);
		    $interval.cancel(t3);
		    t = undefined;
		    t2= undefined;
		    t3= undefined;
		 }
	});
});
