angular.module("report.modify",["services"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("report.modify", {
		url : "/modify",
		abstract : true,
		template : "<div ui-view></div>",
		controller : function(){
		},
	}).state("report.modify.diagnosisor",{
		url : "/diagnosisor",
		abstract : true,
		template : "<div ui-view></div>",
		controller : function(){
		},
	}).state("report.modify.diagnosisor.request",{
		url : "/request",
		abstract : true,
		template : "<div ui-view></div>",
		controller : function(){
		},
	}).state("report.modify.diagnosisor.request.list",{
		url : "/list",
		templateUrl : "report/modify/report.modify.diagnosisor.request.list.html",
		controller : function($scope,$diyhttp,$queryService){
			$scope.listModifyReportRequest=[];
			$scope.listMrr=[];
			$scope.searchOpt={
				modifyReportRequestStatus:0,
				spu:{}
			};
			$scope.initData=function(){
				$queryService.queryModifyReportRequestStatus(function(listMrr){
					$scope.listMrr=listMrr;
				});
				$scope.searchModifyReportRequest();
			};
			$scope.searchModifyReportRequest=function(){
				$diyhttp.post("report!searchModifyReportRequestFromMe.action",$scope.searchOpt,function(data){
					$scope.listModifyReportRequest=data.listModifyReportRequest;
					$scope.searchOpt.spu=data.spu;
				});
			};
		},
	}).state("report.modify.diagnosisor.writing",{
		url : "/writing/:reportId",
		templateUrl : "report/modify/report.modify.diagnosisor.writing.html",
		controller : function($scope, $diyhttp, $msgDialog, $selectDialog,
				$dialogs,$modals, $diagnosisDetailDialog,$selectDialog, $stateParams){
			if (!$stateParams.reportId) {
				$msgDialog.showMessage("无效的报告ID");
				return;
			}
			$scope.report = null;
			$scope.handle = {};
			$scope.historyMedicalHis = [];
			$scope.medicalHis = null;
			$scope.arrayDiagnosisHandle = [];
			// 检查报告
			$scope.checkDiagnosis = function(callOk, callNo) {
				var maxopLine = 15, maxcoLine = 8, maxWordCount = 44;
				if (!$scope.handle.pic_opinion&& !$scope.handle.pic_conclusion) {
					if (callOk)callOk();
					return;
				}
				var formatStr = function(srcStr) {
					if (!srcStr)return "";
					if (!srcStr instanceof String)return null;
					var sstr = srcStr.replace(new RegExp('\r\n', "gm"),'\n');
					return srcStr.split('\n');
				};
				var arrPic_opinion = formatStr($scope.handle.pic_opinion);
				if (arrPic_opinion.length > maxopLine) {
					$selectDialog.showMessage("你的影像所见行数较多，在下级机构打印时有可能造成格式混乱！请问您是否要继续？",callOk, callNo);
					return;
				}
				var line = 0;
				for (var int = 0; int < arrPic_opinion.length; int++) {
					var pop = arrPic_opinion[int];
					var num = pop.length / maxWordCount;
					if (pop.length % maxWordCount > 0)
						num = num + 1;
					line += num;
				}
				if (line > maxopLine) {
					$selectDialog.showMessage("你的影像所见字数较多，在下级机构打印时有可能造成格式混乱！请问您是否要继续？",callOk, callNo);
					return;
				}
				var arrpic_conclusion = formatStr($scope.handle.pic_conclusion);
				if (arrpic_conclusion.length > maxcoLine) {
					$selectDialog.showMessage("你的诊断意见行数较多，在下级机构打印时有可能造成格式混乱！请问您是否要继续？",callOk, callNo);
					return;
				}
				var line = 0;
				for (var int = 0; int < arrpic_conclusion.length; int++) {
					var pop = arrpic_conclusion[int];
					var num = pop.length / maxWordCount;
					if (pop.length % maxWordCount > 0)
						num = num + 1;
					line += num;
				}
				if (line > maxcoLine) {
					$selectDialog.showMessage("你的诊断意见字数较多，在下级机构打印时有可能造成格式混乱！请问您是否要继续？",callOk, callNo);
					return;
				}
				if (callOk)callOk();
			};
			// 查看病例详情
			$scope.viewDiagnosisDetail = function(diagnosisId) {
				$diagnosisDetailDialog.showDialog(diagnosisId);
			};
			// 初始化数据
			$scope.initData = function() {
				queryReportByDiagnosisId();
			};
			// 通过诊断ID查询报告
			var queryReportByDiagnosisId = function() {
				$diyhttp.post("report!requestModifyReportById.action",{
					reportId : $stateParams.reportId
				},function(data) {
					if (data&& data.report) {
						$scope.report = data.report;
						$scope.handle = data.handle;
//						$scope.medicalHis = data.medicalHis;
						$scope.historyMedicalHis = data.listMedicalHis;
						$scope.arrayDiagnosisHandle = data.listHandle;
					} else
						$msgDialog.showMessage("诊断报告未找到！");
				});
			};
			// 保存处理
			$scope.saveHandle = function() {
				$scope.checkDiagnosis(function() {
					$selectDialog.showMessage("保存之后，新报告将以此次修改为准，你确定要再次发布吗？",function() {
						$diyhttp.post("report!modifyReport.action",{
							diagnosisHandle : $scope.handle,
							reportId : $scope.report.id
						},function(data) {
							$msgDialog.showMessage("保存成功！");
							$scope.goback();
						});
					});
				}, null);
			};
			// 选择模板
			$scope.selectTemplate = function() {
				$modals.openSelectTemplateModal({},function(obj){
					if (obj.type=='plus') {
						if ($scope.handle.pic_conclusion) {
							$scope.handle.pic_conclusion += "\r\n" + obj.template.pic_conclusion;
						}else {
							$scope.handle.pic_conclusion = obj.template.pic_conclusion;
						}
						if ($scope.handle.pic_opinion) {
							$scope.handle.pic_opinion += "\r\n" + obj.template.pic_opinion;
						}else {
							$scope.handle.pic_opinion = obj.template.pic_opinion;
						}
					}else if (obj.type=='apply') {
						$scope.handle.pic_conclusion = obj.template.pic_conclusion;
						$scope.handle.pic_opinion = obj.template.pic_opinion;
					}
				});
			};
			// 存为模板
			$scope.saveTemplate = function() {
				$dialogs.showSavePrivateTemplateDialog({
					handle:$scope.handle
				},function(retu){
				});
			};
//			// 应用到报告
//			$scope.applyToReport = function(handle) {
//				$scope.diagnosis.pic_conclusion = handle.pic_conclusion;
//				$scope.diagnosis.pic_opinion = handle.pic_opinion;
//				$scope.diagnosis.report_note = handle.report_note;
//				$scope.diagnosis.f_o_m = handle.f_o_m;
//			};
		},
	}).state("report.modify.requestor",{
		url : "/requestor",
		abstract : true,
		template : "<div ui-view></div>",
		controller : function(){
		},
	}).state("report.modify.requestor.request",{
		url : "/request",
		abstract : true,
		template : "<div ui-view></div>",
		controller : function(){
		},
	}).state("report.modify.requestor.request.list",{
		url : "/list",
		templateUrl : "report/modify/report.modify.requestor.request.list.html",
		controller : function($scope,$diyhttp,$msgDialog,$queryService,$selectDialog,$writerDialog){
			$scope.listModifyReportRequest=[];
			$scope.listMrr=[];
			$scope.searchOpt={
				modifyReportRequestStatus:0,
				spu:{}
			};
			$scope.initData=function(){
				console.log($scope.searchOpt.modifyReportRequestStatus);
				$scope.searchModifyReportRequest();
				$queryService.queryModifyReportRequestStatus(function(listMrr){
					$scope.listMrr=listMrr;
				});
			};
			$scope.searchModifyReportRequest=function(){
				$diyhttp.post("report!searchModifyReportRequestToMe.action",$scope.searchOpt,function(data){
					$scope.listModifyReportRequest=data.listModifyReportRequest;
					$scope.searchOpt.spu=data.spu;
				});
			};
			$scope.confirmRequest=function(mrrId,flag){
				if (flag==1) {
					$selectDialog.showMessage("同意修改之后，诊断方将有一次修改本报告的机会，是否确定？",function(){
						$diyhttp.post("report!confirmModifyRequest.action",{
							mrrId:mrrId
						},function(data){
							$msgDialog.showMessage("操作成功！");
							$scope.searchModifyReportRequest();
						});
					});
				}else {
					$writerDialog.openWriting({
						title:"请填写",
						msg:"请填写拒绝的原因：",
						writing:true,
						note:""
					},function(note){
						$diyhttp.post("diagnosis!rejestModifyRequest.action",{
							mrrId:mrrId,
							answer:note
						},function(data){
							$msgDialog.showMessage("操作成功！");
							$scope.searchModifyReportRequest();
						});
					});
				}
			};
		},
//	}).state("report.modify.requestor.request.list",{
//		
//	}).state("report.modify.requestor.request.list",{
//		
//	}).state("report.modify.requestor.request.list",{
		
	});
});