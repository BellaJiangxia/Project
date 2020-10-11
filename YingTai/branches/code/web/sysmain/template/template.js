angular.module("template",["ui.bootstrap","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider){
	$stateProvider.state("template",{
		url : "/template",
		abstract : true,
		template : "<div ui-view></div>",
		controller : "templateCtrl"
	})
	.state("template.private",{
		url : "/private",
		templateUrl : "template/template.private.html",
		controller : "templatePrivateCtrl"
	})
	.state("template.new",{
		url : "/new",
		templateUrl : "template/template.new.html",
		controller : "templateNewCtrl"
	})
	.state("template.modify",{
		url : "/modify/:templateId",
		templateUrl : "template/template.modify.html",
		controller : "templateModifyCtrl"
	});
	$urlRouterProvider.when("/template", "/template/private");
})
.controller("templateCtrl",function(){
})
.controller("templatePrivateCtrl",function($scope,$http,$msgDialog,$selectDialog,$queryService){
	$scope.listTemplate=[];
	$scope.searchOpt={
		deviceTypeId:"0",
		partTypeId:"0",
		spu:{}
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
		$scope.searchMyTemplate();
	};
	$scope.searchMyTemplate=function(){
		$http.post("sys!searchMyTemplate.action",$scope.searchOpt)
		.success(function(data){
			if (data.code==0) {
				$scope.listTemplate=data.data.listTemplate;
				$scope.searchOpt.spu=data.data.spu;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.deleteTemplate=function(templateId){
		$selectDialog.showMessage("你确定要删除此模板吗？",function(){
			$http.post("sys!deleteTemplate.action",{templateId:templateId})
			.success(function(data){
				$msgDialog.showMessage(data.name);
				$scope.searchMyTemplate();
			});
		});
	};
})
.controller("templateNewCtrl",function($scope,$http,$msgDialog,$queryService){
	$scope.newTemplate={
		device_type_id:"0",
		part_type_id:"0",
		template_name:"",
		pic_opinion:"",
		pic_conclusion:"",
		note:""
	};
	$scope.initData=function(){
		$queryService.queryDeviceType();
	};
	var checkInput=function(){
		if (!$scope.newTemplate.template_name) {
			return "请填写'模板名称'！";
		}
		if ($scope.newTemplate.device_type_id==0) {
			return "请选择'设备类型'！";
		}
		if ($scope.newTemplate.part_type_id==0) {
			return "请选择'部位类型'！";
		}
		if (!$scope.newTemplate.pic_opinion) {
			return "请填写'影像所见'！";
		}
		if (!$scope.newTemplate.pic_conclusion) {
			return "请填写'诊断意见'！";
		}
	};
	$scope.saveTemplate=function(){
		var tipMsg= checkInput();
		if (tipMsg) {
			$msgDialog.showMessage(tipMsg);
			return;
		}
		$http.post("sys!createPrivateTemplate.action",{template:$scope.newTemplate})
		.success(function(data){
			$msgDialog.showMessage(data.name);
			if (data.code==0) {
				$scope.$parent.goback();
			}
		});
	};
	$scope.saveTemplate=function(){
		var tipMsg= checkInput();
		if (tipMsg) {
			$msgDialog.showMessage(tipMsg);
			return;
		}
		$http.post("sys!createPrivateTemplate.action",{template:$scope.newTemplate})
		.success(function(data){
			$msgDialog.showMessage(data.name);
			if (data.code==0) {
				$scope.$parent.goback();
			}
		});
	};
})
.controller("templateModifyCtrl",function($scope,$http,$msgDialog,$selectDialog,$stateParams,$queryService){
	if (!$stateParams.templateId) {
		$msgDialog.showMessage("无效的模板！");
		return;
	}
	$scope.template=null;
	$scope.device_id="0";
	$scope.part_id="0";
	$scope.initData=function(){
		$scope.queryMyTemplate();
	};
	var checkInput=function(){
		if (!$scope.template.template_name) {
			return "请填写'模板名称'！";
		}
		if ($scope.device_id==0)return "请选择'设备类型'！";
		if ($scope.part_id==0)return "请选择'部位类型'！";
		if (!$scope.template.pic_opinion) {
			return "请填写'影像所见'！";
		}
		if (!$scope.template.pic_conclusion) {
			return "请填写'诊断意见'！";
		}
	};
	$scope.queryMyTemplate=function(){
		$http.post("sys!selectPrivateTemplateById.action",{templateId:$stateParams.templateId})
		.success(function(data){
			if (data.code==0) {
				$scope.template=data.data.reportTemplate;
				$queryService.queryDeviceType(function(){
					$scope.$parent.queryPartList($scope.template.device_type_id,function(){
//						window.setTimeout(function(){
							$scope.device_id=$scope.template.device_type_id+'';
							$scope.part_id=$scope.template.part_type_id+'';
//						},100); 
					});
				});
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.deleteTemplate=function(){
		$selectDialog.showMessage("你确定要删除此模板吗？",function(){
			$http.post("sys!deleteTemplate.action",{templateId:$scope.template.id})
			.success(function(data){
				$msgDialog.showMessage(data.name);
				if (data.code==0) {
					$scope.goback();
				}
			});
		});
	};
	$scope.modifyTemplate=function(){
		var tipMsg= checkInput();
		if (tipMsg) {
			$msgDialog.showMessage(tipMsg);
			return;
		}
		$scope.template.device_type_id=$scope.device_id;
		$scope.template.part_type_id=$scope.part_id;
		$http.post("sys!modifyPrivateTemplate.action",{template:$scope.template})
		.success(function(data){
			$msgDialog.showMessage(data.name);
			if (data.code==0) {
				$scope.template=data.data.reportTemplate;
			}
		});
	};
});