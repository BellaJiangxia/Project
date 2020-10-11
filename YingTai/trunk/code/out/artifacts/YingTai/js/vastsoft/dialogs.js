angular.module("vs.dialogs",["ngDialog"])
.service("$vsDialogs",function(ngDialog){
	this.showMessage = function(msg) {
		ngDialog.open({
			template : '<div class="div div-success">'+
					'<div align="center">'+
						'<label>消息</label>'+
					'</div>'+
					'<div style="margin-left: 20px;">'+
						'<span>{{show_Message}}</span>'+
					'</div>'+
					'<p></p>'+
					'<div style="text-align: right;">'+
						'<a class="btn btn-info btn-sm" href ng-click="closeThisDialog(1)">'+
						'<i class="glyphicon glyphicon-ok"></i> 确定</a>'+
					'</div>'+
				'</div>',
			className : 'ngdialog-theme-default',
			plain : true,
			showClose:false,
			data : {msg:msg},
			controller : function($scope){
				$scope.show_Message = $scope.ngDialogData.msg;
			}
		});
	};
	this.showConfirmDialog=function(msg,callbackYes,callbackNo){
		ngDialog.open({
			template : '<div>'+
				'<div align="center"><label>请确认</label></div>'+
				'<div style="margin-left: 20px;">{{show_Message}}</div>'+
				'<div style="text-align: right;">'+
					'<p></p>'+
					'<div class="btn-group btn-group-sm">'+
						'<a class="btn btn-info" href ng-click="closeThisDialog(1)"> 确定</a>'+
						'<a class="btn btn-warning" href ng-click="closeThisDialog(0)"> 取消</a>'+
					'</div>'+
				'</div>'+
			'</div>',
			className : 'ngdialog-theme-default',
			plain : true,
			showClose:false,
			data : {
				msg:msg
			},
			controller : function($scope){
				$scope.show_Message = $scope.ngDialogData.msg;
			}
		}).closePromise.then(function (data) {
		    if (data.value==1) {
				if (callbackYes) {
					callbackYes();
				}
			}else {
				if (callbackNo) {
					callbackNo();
				}
			}
		});
	};
});