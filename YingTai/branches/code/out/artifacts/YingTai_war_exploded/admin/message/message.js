angular.module("message", ["diy.dialog"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("message",{
		url : "/message",
		templateUrl : "message/sys.message.html",
		controller : "messageCtrl"
	});
}).controller("messageCtrl",function($scope,$http,$msgDialog){
	$scope.searchOpt={
		start:null,
		eng:null,
		spu:{}
	};
	$scope.listMessage=[];
	$scope.initData=function(){
		$scope.searchMessage();
	};
	$scope.searchMessage=function(){
		if ($scope.searchOpt.start&&$scope.searchOpt.end) {
			if ($scope.searchOpt.start instanceof Date)
				$scope.searchOpt.start=$scope.searchOpt.start.format('yyyy-MM-dd');
			if ($scope.searchOpt.end instanceof Date)
				$scope.searchOpt.end=$scope.searchOpt.end.format('yyyy-MM-dd');
		}
		$http.post("msg!searchMessage.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listMessage=data.data.listMessage;
				$scope.searchOpt.spu=data.data.spu;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
});