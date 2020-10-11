angular.module("diagnosis",[ "ui.bootstrap", "diy.dialog","services", 
                             "diy.selector","diy.diagnosis.msg.chat",
                             "diagnosis.tranfer","diagnosis.handle","diagnosis.mgr","diagnosis.report"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("diagnosis", {
		url : "/diagnosis",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "diagnosisCtrl",
	}).state("diagnosis.request",{
		url : "/request",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "diagnosisRequestCtrl",
	});
//	.state("diagnosis.request.detail",{
//		url : "/detail/:diagnosisId",
//		templateUrl : "diagnosis/diagnosis.request.detail.html",
//		controller : "diagnosisRequestDetailCtrl",
//	});
}).controller("diagnosisCtrl", function($scope, $http, $msgDialog) {

}).controller("diagnosisReportPrintWbCtrl",function($scope, $http, $window, $stateParams, $msgDialog) {
	$scope.dynamicPopover = {
		templateUrl : 'popoverTemplate.html',
	};
	if (!$stateParams.diagnosisId) {
		$msgDialog.showMessage("无效的报告ID");
		return;
	}
	$scope.diagnosis = null;
	$scope.org = null;
	$scope.isIE = false;
	$scope.medicalHisImg = null;
	$scope.medicalHis = null;
	var getExplorer = function() {
		if (!!window.ActiveXObject || "ActiveXObject" in window) {
			$scope.isIE = true;
			return true;
		} else {
			$scope.isIE = false;
			return false;
		}
	};
	getExplorer();
	$scope.initData = function() {
		if (!getExplorer()) {
			$msgDialog.showMessage("请使用IE10及其以上浏览器，打印效果才最好！");
		}
		queryReportByDiagnosisId();
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
			Wsh.RegWrite(hkey_root + hkey_path + hkey_key,
					"0.39"); // 0.39相当于把页面设置里面的边距设置为10
			HKEY_Key = "margin_left";
			Wsh.RegWrite(hkey_root + hkey_path + hkey_key,
					"0.39");
			HKEY_Key = "margin_right";
			Wsh.RegWrite(hkey_root + hkey_path + hkey_key,
					"0.39");
			HKEY_Key = "margin_top";
			Wsh.RegWrite(hkey_root + hkey_path + hkey_key,
					"0.39");
		} catch (e) {
		}
	};
	$scope.webbrowserPrint = function() {
		$scope.webbrowserPrintSet();
		var headstr = "<head></head>";
		var printData = "<body>"
				+ document.getElementById("dvData").innerHTML
				+ "</body>"; // 获得
		// div里的所有html数据
		var newWindow = window
				.open("printwindow", "_blank",
						"left=0,top=0,height=943, width=734, toolbar=no, menubar=no");// 打印窗口要换成页面的url
		// newWindow.moveTo(0, 0);
		newWindow.document.write("<!DOCTYPE HTML><html>"
				+ headstr + printData + "</html>");
		newWindow.document.close();
		// newWindow.WebBrowser.ExecWB(8,1);

		newWindow.document.getElementById("container").style.position = "";

		newWindow.print();
		newWindow.close();

		// var headstr="<object id=\"WebBrowser\"
		// classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2
		// height=\"0\" width=\"0\"></object>";
		// var printData =
		// document.getElementById("dvData").innerHTML; //获得 div
		// 里的所有 html 数据
		// var oldstr = document.body.innerHTML;
		// document.body.innerHTML =headstr+printData;
		// // document.head.innerHTML="";
		// WebBrowser.ExecWB(7,1);
		// setTimeout(function(){
		// // document.body.innerHTML = oldstr;
		// location.reload();
		// }, 1);
	};
	// 打印报告
	$scope.printReport = function() {
		printDiagnosisReport(function() {
			$scope.webbrowserPrint();
		});
	};
	/** 通知服务端打印报告！ */
	var printDiagnosisReport = function(callBackOk, callBackNo) {
		$http.post("diagnosis!printDiagnosisReport.action", {
			diagnosisId : $scope.diagnosis.id
		}).success(function(data) {
			if (data.code == 0) {
				if (callBackOk)
					callBackOk();
			} else {
				$msgDialog.showMessage(data.name);
				if (callBackNo)
					callBackNo();
			}
		});
	};
	$scope.vertifyUser = {};
	$scope.showSignImg = false;
	$scope.queryCheckFileExsit = function(fileId) {
		if (!fileId)
			return;
		$http.post("file!queryCheckFileExsit.action", {
			fileId : fileId
		}).success(function(data) {
			if (data.code == 0) {
				$scope.showSignImg = data.data&&data.data.file?true:false;
			}
		});
	};
	// 通过诊断ID查询报告
	var queryReportByDiagnosisId = function() {
		var formatStr = function(srcStr) {
			if (!srcStr)return "";
			if (!srcStr instanceof String)
				return null;
			var sstr = srcStr.replace(new RegExp('\r\n', "gm"),'\n');
			return srcStr.split('\n');
			// sstr=sstr.replace(new RegExp('\n',"gm"),'<br/>');
			// return sstr;
		};
		$http.post("diagnosis!queryReportByDiagnosisId.action",{
			diagnosisId : $stateParams.diagnosisId,
			mappingDevice : true
		}).success(function(data) {
			if (data.code == 0) {
				if (data.data&& data.data.diagnosisReport) {
					$scope.diagnosis = data.data.diagnosisReport;
//					$scope.diagnosis.pic_opinion = formatStr($scope.diagnosis.pic_opinion);
//					$scope.diagnosis.pic_conclusion = formatStr($scope.diagnosis.pic_conclusion);
					$scope.medicalHis = data.data.medicalHis;
					$scope.medicalHisImg = data.data.medicalHisImg;
					// $scope.arrayDiagnosisHandle
					// = data.data.listHandle;
					$scope.wasRequestOrg = data.data.wasRequestOrg;
					$scope.org = data.data.org;
					$scope.vertifyUser = data.data.vertifyUser;
					if ($scope.vertifyUser) {
						$scope.queryCheckFileExsit($scope.vertifyUser.sign_file_id);
					}
				} else
					$msgDialog.showMessage("诊断报告未找到！");
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.gotoBack = function() {
		$window.history.back();
	};
}).controller("diagnosisRequestCtrl",function(){
	
});
//.controller("diagnosisRequestDetailCtrl",function($scope, $diyhttp, $msgDialog, $stateParams){
//	$scope.diagnosis = null;
//	$scope.medicalHis = null;
//	$scope.medicalHisImg = null;
//	$scope.initData = function() {
//		$scope.queryFDiagnosisById($stateParams.diagnosisId);
//	};
//	$scope.queryFDiagnosisById = function(diagnosisId) {
//		if (!$stateParams.diagnosisId) {
//			$msgDialog.showMessage("无效的诊断ID！");
//			return;
//		}
//		$diyhttp.post("diagnosis!queryFDiagnosisAndImgByDiagnosisId.action",{
//			diagnosisId : diagnosisId
//		},function(data) {
//			$scope.diagnosis = data.diagnosis;
//			$scope.medicalHis = data.medicalHis;
//			$scope.medicalHisImg = data.medicalHisImg;
//		});
//	};
//});
