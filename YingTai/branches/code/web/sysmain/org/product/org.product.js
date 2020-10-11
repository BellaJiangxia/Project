angular.module("org.product", [])
.config(function($stateProvider,$urlRouterProvider){
	$stateProvider.state("org.product",{
		abstract: true,
		url:"/product",
		template: "<ui-view />",
		controller : function(){
		}
	}).state("org.product.list",{
		url:"/list",
		templateUrl: "org/product/org.product.list.html",
		controller:"orgProductListCtrl"
	}).state("org.product.new",{
		url:"/new",
		templateUrl: "org/product/org.product.new.html",
		controller:"orgProductNewCtrl"
	});
	$urlRouterProvider.when("/org/product","");
}).controller("orgProductListCtrl",function($scope,$diyhttp,$dialogs,$queryService){
	$scope.devices=[];
	$scope.parts=[];
	$scope.listOrgProduct=[];
	$scope.searchOpt={
		productName:"",
		deviceTypeId:0,
		charge_type:0,
		spu:{}
	};
	$scope.initData=function(){
		$queryService.queryOrgProductChargeType();
		$queryService.queryDeviceType();
		$scope.loadProducts();
	};
	$scope.loadProducts=function(){
		$diyhttp.post("org!queryProductsByOrg.action",$scope.searchOpt,function(data) {
			$scope.listOrgProduct=data.listOrgProduct;
			$scope.searchOpt.spu=data.spu;
		});
	};
	
	$scope.enableProduct=function(id,isEnable){
		var doExecute = function(){
			$diyhttp.post("org!enableProduct.action",{
				orgId:id,
				isPass:isEnable
			},function(data) {
				$dialogs.showMessage("操作成功！");
				$scope.loadProducts();
			});
		};
		if (!isEnable) {
			$dialogs.showConfirmDialog("您确定要禁用此诊断服务吗？",function(){
				doExecute();
			});
		}else
			doExecute();
	};
	
	$scope.deleteProduct=function(id){
		$dialogs.showConfirmDialog("您确定要删除此诊断服务吗？",function(){
			$diyhttp.post("org!deleteProduct.action",{
				orgId:id
			},function(data) {
				$dialogs.showMessage("删除成功！");
				$scope.loadProducts();
			});
		});
	};
}).controller("orgProductNewCtrl",function($scope,$diyhttp,$dialogs,$queryService){
	$scope.product={
		name:"",
		price:0.0,
		device_type_id:0,
		description:"",
		charge_type:0
	};
	$scope.initData=function(){
		$queryService.queryOrgProductChargeType();
		$queryService.queryDeviceType();
	};
	$scope.deviceChange=function(){
		productService.getDicValuesByParent($scope.product.device_type_id).then(function(data){
			$scope.parts=data;
		});
	};
	
	$scope.saveProduct=function(){
		$diyhttp.post("org!saveProduct.action",{
			product:$scope.product
		},function(data) {
			$dialogs.showMessage("保存成功！");
			$scope.goback();
		});
	};
	
//	$scope.checkPrice=function(){
//		$scope.isValidPrice=!(!isNaN(parseFloat($scope.product.price)) && isFinite($scope.product.price) && $scope.product.price>0);
//	}
});