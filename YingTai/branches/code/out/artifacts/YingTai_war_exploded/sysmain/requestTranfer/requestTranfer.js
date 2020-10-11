angular.module("requestTranfer",["requestTranfer.requestor"])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("requestTranfer", {
		url : "/requestTranfer",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "requestTranferCtrl"
	});
}).controller("requestTranferCtrl",function($scope,$diyhttp){
	
});