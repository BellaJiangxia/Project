angular.module("case", [ "services","diy.dialog", "case.new",,"case.mgr","case.info"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("case", {
		url : "/case",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "caseCtrl"
//	}).state("case.detail",{
//		url : "/detail/:caseId",
//		templateUrl : "case/case.detail.html",
//		controller : "caseDetail"
	});
	$urlRouterProvider.when("/case", "/case/new");
}).controller("caseDetail",function($scope,$diyhttp,$msgDialog,$stateParams){
	$scope.caseHistory = null;
	$scope.patient=null;
	$scope.listDicomImg = null;
	$scope.initData=function(){
		if ($stateParams.caseId) {
			$scope.queryMedicalHis();
		}else{
			$scope.goback();
		}
	};
	$scope.gotoBack=function(){
		$window.history.back();
	};
	// 通过病例号查询病例
	$scope.queryMedicalHis = function() {// MedicalNum
		$diyhttp.post("case!queryCaseHistoryById.action", {
			caseId : $stateParams.caseId
		},function(data) {
			$scope.caseHistory = data.caseHistory;
			$scope.patient = data.patient;
			$scope.listDicomImg = data.listDicomImg;
		});
	};
}).controller("caseCtrl", function($scope,$diyhttp,$msgDialog,$location,$selectDialog,$commitDiagnosisRequestModal) {
	$scope.list_medicalHis=null;
//	//检查重复提交诊断
//	var checkRepeatCommitDiagnosis=function(caseHistory,selectedDicomImg,org_id,product_id,callBack){
//		if (! org_id || !product_id)return false;
//		$diyhttp.post("diagnosis!checkRepeatCommitDiagnosis.action",{
//			medicalHisId:caseHistory.id,
//			dicomImgId:selectedDicomImg.id,
//			diagnosisOrgId:org_id,
//			productId:product_id
//		},function(data){
//			if (callBack)callBack(data.repeat);
//		});
//	};
//	//执行诊断申请提交或病例保存
//	var executeCommitRequest=function(caseHistory,selectedDicomImg,diagnosisOrgId,diagnosisProductId,aboutCaseIds){
//		$diyhttp.post("patientInfoDiagnosisCross!savePatientInfoAndCommitDiagnosis.action",{
//			caseHistory : caseHistory,
//			selectedDicomImgId : selectedDicomImg.id,
//			diagnosisOrgId : diagnosisOrgId,
//			diagnosisProductId : diagnosisProductId,
//			aboutCaseIds : aboutCaseIds,
//		},function(data) {
//			$msgDialog.showMessage("操作成功");
//			$scope.goback();
//		});
//	};
	//提交申请 type--诊断申请，咨询申请
	$scope.commitRequest = function(caseHistory,selectedDicomImg,request_class) {
		if (!selectedDicomImg) {
			$msgDialog.showMessage("请选择一个图像！");
			return;
		}
		$commitDiagnosisRequestModal.open({
			caseHistory : caseHistory,
			img : selectedDicomImg,
			request_class : request_class
		}, function(org_id,product_id,aboutids) {
//			checkRepeatCommitDiagnosis(caseHistory,selectedDicomImg,org_id,product_id,function(repeat){
//				if (repeat) {
//					$selectDialog.showMessage("此病例已经提交过此申请了，是否要重复提交？",function(){
//						executeCommitRequest(caseHistory,selectedDicomImg,org_id,product_id,aboutids);
//					});
//				}else {
//					executeCommitRequest(caseHistory,selectedDicomImg,org_id,product_id,aboutids);
//				}
//			});
		});
	};
	$scope.patientBirthdayChange=function(patient){
		if (!patient||!patient.birthday)
			return;
		try {
			var birthday=patient.birthday;
			if (birthday==null)return 0;
			var today = new Date();
			var birh=birthday.getTime();
			if (today.getTime()<birh) {
				$msgDialog.showMessage("病人生日不正确！");
				return;
			}
			var yearNow = today.getFullYear();
			var monthNow = today.getMonth() + 1;
			var dayOfMonthNow = today.getDate();
			var yearBirth = birthday.getFullYear();
			var monthBirth = birthday.getMonth() + 1;
			var dayOfMonthBirth = birthday.getDate();
			var age = yearNow - yearBirth;
			if (monthNow > monthBirth)
				patient.age=age;
			else if (monthNow < monthBirth)
				patient.age=--age;
			else if (dayOfMonthNow < dayOfMonthBirth)
				patient.age=--age;
			else patient.age=age;
		} catch (e) {
		}
	};
	$scope.patientAgeChange=function(patient){
		var age=parseInt(patient.age);
		if (age<0||age>200)return;
		var today=new Date();
		var year=today.getFullYear();
		year=year-age;
		today.setFullYear(year);
		patient.birthday=today;
	};
});
