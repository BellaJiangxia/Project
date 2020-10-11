angular.module("vs.services",[])
.service("$vsHttp",function($rootScope,$http){
	this.listBeforeHttpFilter=[];
	this.listSuccessHttpFilter=[];
	this.listFinalHttpFilter=[];
	this.post = function(url, params,callbackOk,callbackNo,callbackFinal) {
		if(!beforeRequest({
			callbackOk:callbackOk,
			callbackNo:callbackNo,
			callbackFinal:callbackFinal
		},url, params)){
			if (callbackNo)callbackNo();
			if (callbackFinal)callbackFinal();
			return;
		}
		$http.post(url,params).success(function(result){
			if(afterRequest({
				callbackOk:callbackOk,
				callbackNo:callbackNo,
				callbackFinal:callbackFinal
			},url,params,result)){
				if (callbackOk)callbackOk(result.data);
			}else {
				if (callbackNo)callbackNo(result);
			}
		}).finally(function(){
			finalRequest();
			if (callbackFinal)callbackFinal();
		});
	};
	this.get = function(url,params,callbackOk,callbackNo,callbackFinal){
		if(!beforeRequest({
			callbackOk:callbackOk,
			callbackNo:callbackNo,
			callbackFinal:callbackFinal
		},url, params)){
			if (callbackNo)callbackNo();
			if (callbackFinal)callbackFinal();
			return;
		}
		$http.get(url,params,{cache:true})
		.success(function(result){
			if(afterRequest({
				callbackOk:callbackOk,
				callbackNo:callbackNo,
				callbackFinal:callbackFinal
			},url,params,result)){
				if (callbackOk)callbackOk(result.data);
			}else {
				if (callbackNo)callbackNo();
			}
		}).finally(function(){
			finalRequest();
			if (callbackFinal)callbackFinal();
		});
	};
	var self = this;
	/** 执行前,返回true或者false */
	var beforeRequest=function(option,url, params){
		if (!self.listBeforeHttpFilter || self.listBeforeHttpFilter.length<=0)
			return true;
		for (var int = 0; int < self.listBeforeHttpFilter.length; int++) {
			var HttpFilter = self.listBeforeHttpFilter[int];
			if (! HttpFilter)
				continue;
			var rrr = HttpFilter(option,url, params);
			if (! rrr)
				return false;
		}
		return true;
	};
	var afterRequest=function(option,url, params,result){
		if (!self.listSuccessHttpFilter || self.listSuccessHttpFilter.length<=0)
			return true;
		for (var int = 0; int < self.listSuccessHttpFilter.length; int++) {
			var HttpFilter = self.listSuccessHttpFilter[int];
			if (! HttpFilter)
				continue;
			var rrr = HttpFilter(option,url, params,result);
			if (! rrr)
				return false;
		}
		return true;
	};
	var finalRequest=function(){
		if (!self.listFinalHttpFilter || self.listFinalHttpFilter.length<=0)
			return;
		for (var int = 0; int < self.listFinalHttpFilter.length; int++) {
			var HttpFilter = self.listFinalHttpFilter[int];
			if (! HttpFilter)
				continue;
			HttpFilter();
		}
	};
});