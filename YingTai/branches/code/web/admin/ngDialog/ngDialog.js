angular.module('diy.dialog', [ 'ngDialog' ])
.service("$msgDialog",function(ngDialog) {
	this.showMessage = function(msg) {
		ngDialog.open({
			template : '<div class="div div-success">'+
					'<div align="center">'+
						'<label>消息</label>'+
					'</div>'+
					'<div style="margin-left: 20px">'+
						'<span>{{show_Message}}</span>'+
					'</div>'+
					'<p></p>'+
					'<div style="text-align: right;">'+
						'<a class="btn btn-info glyphicon glyphicon-ok" href="javascript:void(0);" ng-click="closeThisDialog(1)">确定</a>'+
					'</div>'+
				'</div>',
			className : 'ngdialog-theme-default',
			plain : true,
			data : {
				msg:msg
			},
			controller : "showmessageCtrl"
		});
	};
})
.service("$writerDialog",function(ngDialog){
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
						'<a class="btn btn-info glyphicon glyphicon-ok" href="javascript:void(0);" ng-click="confirm()" ng-if="note">确定</a>'+
						'<a class="btn btn-warning glyphicon glyphicon-remove" href="javascript:void(0);" ng-click="cancel()">取消</a>'+
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
.service("$selectDialog",function(ngDialog) {
	// 打开窗口
	this.showMessage = function(msg,yes,no) {
		ngDialog.open({
			template : '<div>'+
				'<div align="center"><label>请确认</label></div>'+
				'<div style="margin-left: 20px">{{show_Message}}</div>'+
				'<div style="text-align: right;">'+
					'<p></p>'+
					'<div class="btn-group btn-group-sm">'+
						'<a class="btn btn-info" href="javascript:void(0);" ng-click="closeThisDialog(1)">确定</a>'+
						'<a class="btn btn-warning" href="javascript:void(0);" ng-click="closeThisDialog(0)">取消</a>'+
					'</div>'+
				'</div>'+
			'</div>',
			className : 'ngdialog-theme-default',
			plain : true,
			data : {
				msg:msg
			},
			controller : "selectCtrl"
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
}).controller("selectCtrl",function($scope){
	$scope.show_Message = $scope.ngDialogData.msg;
}).controller("showmessageCtrl", function($scope) {
	$scope.show_Message = $scope.ngDialogData.msg;
});