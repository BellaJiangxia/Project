angular.module("services",["diy.dialog","vs.services","vs.dialogs"])
.run(function($rootScope){
	$rootScope.spinner={
		isShow:false,
		msg:"",
		show:function(msg){
			this.isShow=true;
			if (msg)
				this.msg=msg;
			else
				this.msg='载入中，请稍候...';
		},
		hide:function(){
			this.isShow=false;
		}
	};
	$rootScope.showSpinner=function(msg){
		$rootScope.spinner.hide();
		$rootScope.spinner.show(msg);
	};
	$rootScope.hideSpinner=function(){
		$rootScope.spinner.hide();
	};
}).service("$diyhttp",function($rootScope,$vsHttp,$vsDialogs,$dialogs){
	$vsHttp.listSuccessHttpFilter.push(function(option,url, params,result){
		if (result.code==99999 || result.code == 999999999 || result.code == 22221) {
			location.reload(true);
			return false;
		}
		return true;
	});
	$vsHttp.listSuccessHttpFilter.push(function(option,url, params,result){
		if (result.code == -9483) {
			$dialogs.showQualityControlFormsDialog({
				qualityControlUid:result.data.qualityControlUid,
				requestParams:{
					option:option,
					url:url,
					params:params
				},
				msg:result.name,
				msg_class:"text-warning"
			},function(){
			});
			return false;
		}
		return true;
	});
	$vsHttp.listSuccessHttpFilter.push(function(option,url, params,result){
		if (result.code != 0) {
			$vsDialogs.showMessage(result.name);
			return false;
		}
		return true;
	});
	$vsHttp.listBeforeHttpFilter.push(function(option,url, params){
		if (!params){$rootScope.showSpinner();return true;}
		if (!params.httpConfig){$rootScope.showSpinner();return true;}
		if (params.httpConfig.showSpinner){$rootScope.showSpinner();return true;}
		return true;
	});
	$vsHttp.listFinalHttpFilter.push(function(){
		$rootScope.hideSpinner();
	});
	this.post=function(url,params,callbackOk,callbackNo,callbackFinal) {
		$vsHttp.post(url,params,callbackOk,callbackNo,callbackFinal);
	};
	this.get=function(url,params,callbackOk,callbackNo,callbackFinal){
		$vsHttp.get(url,params,callbackOk,callbackNo,callbackFinal);
	};
}).service("$queryService",function($diyhttp,$rootScope){
	this.queryModifyReportRequestStatus=function(ok){
		$diyhttp.post("common!queryModifyReportRequestStatus.action",{},function(data){
			$rootScope.listMrr = data.listMrr;
			if (ok)ok(data.listMrr);
		});
	};
	this.queryDeviceType=function(ok){
		$diyhttp.post("common!queryDeviceType.action",{},function(data){
			$rootScope.listDeviceType = data.listDeviceType;
			if (ok)ok(data.listDeviceType);
		});
	};
	this.queryRemoteParamType=function(ok){
		$diyhttp.post("common!queryRemoteParamType.action",{},function(data){
			$rootScope.listRemoteParamsType = data.listRemoteParamsType;
			if (ok)ok(data.listRemoteParamsType);
		});
	};
	this.queryQualityControlTarget=function(ok){
		if ($rootScope.listQualityControlTarget && $rootScope.listQualityControlTarget.length>0) {
			if (ok)ok($rootScope.listQualityControlTarget);
			return;
		}
		$diyhttp.post("common!queryQualityControlTarget.action",{},function(data){
			$rootScope.listQualityControlTarget = data.listQualityControlTarget;
			if (ok)ok(data.listQualityControlTarget);
		});
	};
	this.queryQualityControlEnforceable=function(ok){
		if ($rootScope.listQualityControlEnforceable && $rootScope.listQualityControlEnforceable.length>0) {
			if (ok)ok($rootScope.listQualityControlEnforceable);
			return;
		}
		$diyhttp.post("common!queryQualityControlEnforceable.action",{},function(data){
			$rootScope.listQualityControlEnforceable = data.listQualityControlEnforceable;
			if (ok)ok(data.listQualityControlEnforceable);
		});
	};
	this.queryQualityControlFormState=function(ok){
		if ($rootScope.listQualityControlFormState && $rootScope.listQualityControlFormState.length>0) {
			if (ok)ok($rootScope.listQualityControlFormState);
			return;
		}
		$diyhttp.post("common!queryQualityControlFormState.action",{},function(data){
			$rootScope.listQualityControlFormState = data.listQualityControlFormState;
			if (ok)ok(data.listQualityControlFormState);
		});
	};
	this.queryQualityControlMeasureQuestionType=function(ok){
		if ($rootScope.listQualityControlMeasureQuestionType && $rootScope.listQualityControlMeasureQuestionType.length>0) {
			if (ok)ok($rootScope.listQualityControlMeasureQuestionType);
			return;
		}
		$diyhttp.post("common!queryQualityControlMeasureQuestionType.action",{},function(data){
			$rootScope.listQualityControlMeasureQuestionType = data.listQualityControlMeasureQuestionType;
			if (ok)ok(data.listQualityControlMeasureQuestionType);
		});
	};
	this.queryQualityControlFormAnswerStatus=function(ok){
		if ($rootScope.listQualityControlFormAnswerStatus && $rootScope.listQualityControlFormAnswerStatus.length>0) {
			if (ok)ok($rootScope.listQualityControlFormAnswerStatus);
			return;
		}
		$diyhttp.post("common!queryQualityControlFormAnswerStatus.action",{},function(data){
			$rootScope.listQualityControlFormAnswerStatus = data.listQualityControlFormAnswerStatus;
			if (ok)ok(data.listQualityControlFormAnswerStatus);
		});
	};
	this.queryOrgProductChargeType=function(ok){
		if ($rootScope.listOrgProductChargeType && $rootScope.listOrgProductChargeType.length>0) {
			if (ok)ok($rootScope.listOrgProductChargeType);
			return;
		}
		$diyhttp.post("common!queryOrgProductChargeType.action",{},function(data){
			$rootScope.listOrgProductChargeType = data.listOrgProductChargeType;
			if (ok)ok(data.listOrgProductChargeType);
		});
	};
	this.queryCaseHistoryType=function(ok){
		if ($rootScope.listCaseHistoryType && $rootScope.listCaseHistoryType.length>0) {
			if (ok)ok($rootScope.listCaseHistoryType);
			return;
		}
		$diyhttp.post("common!queryCaseHistoryType.action",{},function(data){
			$rootScope.listCaseHistoryType = data.listCaseHistoryType;
			if (ok)ok(data.listCaseHistoryType);
		});
	};
});