angular.module("report",[ "ui.bootstrap", "diy.dialog","services","diy.selector","report.modify"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("report", {
		url : "/report",
		abstract : true,
		template : "<div ui-view></div>",
		controller : function(){
		},
	}).state("report.print",{
		url : "/print/:reportId/:print_type",
		templateUrl : "report/report.print.html",
		controller : "reportPrintCtrl",
	}).state("report.detail", {
		url : "/detail",
		abstract : true,
		template : "<div ui-view></div>",
		controller : function(){
		},
	}).state("report.detail.yuanzhen", {
		url : "/yuanzhen/:reportId",
		templateUrl : "report/report.detail.yuanzhen.html",
		controller : "reportDetailYuanzhenCtrl",
		cache:false
	});
}).controller("reportPrintCtrl",function($scope, $http,$diyhttp, $window, $stateParams, $msgDialog) {
	if (!$stateParams.reportId) {
		$msgDialog.showMessage("无效的报告ID");
		return;
	}
	$scope.print_type = $stateParams.print_type;
	$scope.paper_type='0';
	$scope.report = null;
	$scope.showSignImg = false;
	$scope.publish_report_org = null;
	$scope.isIE = false;
	var getExplorer = function() {
		if (!!window.ActiveXObject || "ActiveXObject" in window) {
			$scope.isIE = true;
			return true;
		} else {
			$scope.isIE = false;
			return false;
		}
	};
	$scope.initData = function() {
		if (!getExplorer()) {
			$msgDialog.showMessage("请使用IE10及其以上浏览器，打印效果才最好！");
		}
		queryReportById();
	};
	
	$scope.setPrint = function() {
		if (!getExplorer()) {
			$msgDialog.showMessage("您的浏览器不支持打印控件，请尝试按住‘ALT+F+U键’或在浏览器设置菜单中打开打印设置进行设置！"
							+ "注意：经测试，打印边距全部设置为‘10’，去掉页眉页脚以及将纸张设置为纵向，打印效果最好！");
			return;
		}
		try {
			WebBrowser.ExecWB(8, 1);
		} catch (e) {
			$msgDialog.showMessage("由于浏览器的安全性限制打开ActiveX控件，请尝试按住‘ALT+F+U键’或在浏览器设置菜单中打开打印设置进行设置！"
							+ "注意：经测试，打印边距全部设置为‘10’，去掉页眉页脚以及将纸张设置为纵向，打印效果最好！");
		}
	};
	$scope.webbrowserPrintSet = function() {
		try {
			var HKEY_Root, HKEY_Path, HKEY_Key;
			HKEY_Root = "HKEY_CURRENT_USER";
			HKEY_Path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
			var Wsh = new ActiveXObject("WScript.Shell");
			HKEY_Key = "header";
			Wsh.RegWrite(HKEY_Root + HKEY_Path + HKEY_Key, "");
			HKEY_Key = "footer";
			Wsh.RegWrite(HKEY_Root + HKEY_Path + HKEY_Key, "");
			HKEY_Key = "margin_bottom";
			Wsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.39"); // 0.39相当于把页面设置里面的边距设置为10
			HKEY_Key = "margin_left";
			Wsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.39");
			HKEY_Key = "margin_right";
			Wsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.39");
			HKEY_Key = "margin_top";
			Wsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.39");
		} catch (e) {
		}
	};
	$scope.webbrowserPrint = function() {
		$scope.webbrowserPrintSet();
		var headstr = "<head></head>";
		var printData = "<body>"
			+"<script type=\"text/javascript\" language=\"JavaScript\">"
				+"document.onreadystatechange = function () {"
					+"if(document.readyState==\"complete\") {"
						+"window.print();"
						+"window.close();"
                  	+"}"
                +"};"
            +"</script>"
			+ document.getElementById("dvData").innerHTML
			+ "</body>"; // 获得
		// div里的所有html数据
		var newWindow = window
				.open("printwindow", "_blank",
						"left=0,top=0,height=943,width=734, toolbar=no, menubar=no,titlebar=no,resizable=no,scrollbars=no,status=no,location=no");// 打印窗口要换成页面的url  
		newWindow.document.write("<!DOCTYPE HTML><html>"
				+ headstr + printData + "</html>");
		newWindow.document.close();

//		newWindow.document.getElementById("container").style.position = "";
	};
	// 打印报告
	$scope.printReport = function() {
		$diyhttp.post("report!printReport.action", {
			reportId : $scope.report.id,
			print_type : $scope.print_type
		},function(data) {
			$scope.webbrowserPrint();
		});
	};
	// 通过诊断ID查询报告
	var queryReportById = function() {
		$diyhttp.post("report!queryYuanzhenReportDetailByIdPrePrint.action",{
			reportId : $stateParams.reportId,
			print_type : $scope.print_type
		},function(data) {
			$scope.report = data.report;
			if ($scope.report&&$scope.report.publish_user_sign_file_id>0) {
				$diyhttp.post("file!queryCheckFileExsit.action", {
					fileId : $scope.report.publish_user_sign_file_id
				},function(data) {
					if (data&&data.file)
						$scope.showSignImg = true;
				});
			}
			$diyhttp.post("org!queryOrgById.action",{
				orgId:$scope.report.publish_report_org_id
			},function(data){
				$scope.publish_report_org = data.org; 
			});
		});
	};
}).controller("reportDetailYuanzhenCtrl",function($scope, $diyhttp,$writerDialog, $msgDialog, $selectDialog,
		$stateParams, $location,$reportMsgChatModal,$diagnosisVideoChat){
	if (!$stateParams.reportId) {
		$msgDialog.showMessage("无效的报告ID");
		return;
	}
	$scope.report = null;
	
	$scope.handleShow = false;
	$scope.initData = function() {
		queryReportById();
	};
	$scope.openChat = function(report) {
		$reportMsgChatModal.open({report : report,currUser : $scope.currUser});
	};
	$scope.openVideo = function(diagnosis) {
		var param={
			sendUserId:$scope.user.id,
			recvUserId:0,
			chatRoom:"",
		};
		if ($scope.user.id==diagnosis.request_user_id) {
			param.recvUserId=diagnosis.verify_user_id;
		}else if ($scope.user.id==diagnosis.verify_user_id){
			param.recvUserId=diagnosis.request_user_id;
		}
		$diyhttp.post("video!callVideoComm.action",{
			videoCall:param
		},function(data){
			window.open("./common/anychat/index.html?user=YT_"
						+ $scope.user.name+"&room="+data.videoCall.chatRoom, "_blank","left=0,top=0,  location=no, toolbar=no, menubar=no");
		});
	};
	$scope.consult2DiagnosisReport=function(){
		$location.path("report/print/"+ $scope.report.id+"/2");
	};
	// 打印报告
	$scope.printReport = function() {
		$location.path("report/print/"+ $scope.report.id+"/1");
	};
	$scope.modifyReport = function() {
		$selectDialog.showMessage("不建议修改报告，请确定是否执意要修改？",function() {
			$diyhttp.post("report!checkAcceptModifyReport.action",{
				reportId:$scope.report.id
			},function(data){
				if (!data.canModify){
					$selectDialog.showMessage("报告已经被申请方查看，需要提交修改申请才能修改，是否立即提交修改申请？",function(){
						$writerDialog.openWriting({
							title:"请输入",
							msg:"请输入你要修改报告的理由：",
							writing:true
						},function(note){
							$diyhttp.post("report!requestModifyReport.action",{
								reportId:$scope.report.id,
								reason:note
							},function(data){
								$msgDialog.showMessage("修改申请提交成功，请等待申请方同意！");
							});
						});
					});
				}else {
					$location.path("report/modify/diagnosisor/writing/" + $scope.report.id);
				}
			});
		});
	};
	// 通过诊断ID查询报告
	var queryReportById = function() {
		$diyhttp.post("report!queryYuanzhenReportDetailById.action",{
			reportId : $stateParams.reportId
		},function(data) {
			$scope.report = data.report;
		});
	};
});