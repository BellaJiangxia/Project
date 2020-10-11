angular.module("qualityControl", [])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("qualityControl", {
		url : "/qualityControl",
		abstract: true,
		template : "<div ui-view></div>",
		controller : "qualityControlCtrl"
	}).state("qualityControl.form",{
		url : "/form",
		abstract: true,
		template : "<div ui-view></div>",
		controller : "qualityControlFormCtrl"
	}).state("qualityControl.form.list",{
		url : "/list",
		templateUrl : "qualityControl/qualityControl.form.list.html",
		controller : "qualityControlFormListCtrl"
	}).state("qualityControl.form.new",{
		url : "/new",
		templateUrl : "qualityControl/qualityControl.form.new.html",
		controller : "qualityControlFormNewCtrl"
	}).state("qualityControl.form.modify",{
		url : "/modify/:form_id",
		templateUrl : "qualityControl/qualityControl.form.new.html",
		controller : "qualityControlFormModifyCtrl"
	});
	$urlRouterProvider.when("/qualityControl", "/qualityControl/form/list");
}).controller("qualityControlCtrl", function() {
	
}).controller("qualityControlFormCtrl" ,function(){
	
}).controller("qualityControlFormListCtrl",function($scope,$diyhttp,$msgDialog,$selectDialog,$queryService){
	$scope.listQualityControlForm = [];
	$scope.searchOpt={
		spu:{},
		target_type:0,
		question_occasion:0,
		question_enforceable:0,
		qcf_state:0
	};
	$scope.initData=function(){
		$queryService.queryQualityControlTarget();
		$queryService.queryQualityControlEnforceable();
		$queryService.queryQualityControlFormState();
		$scope.searchQualityControlForm();
	};
	$scope.validQualityControlForm=function(form_id){
		$diyhttp.post("qualityControl!validQualityControlFormById.action",{
			form_id:form_id
		},function(data){
			$scope.searchQualityControlForm();
		});
	};
	$scope.disableQualityControlForm=function(form_id){
		$selectDialog.showMessage("您确定要禁用此问卷吗？",function(){
			$diyhttp.post("qualityControl!disableQualityControlFormById.action",{
				form_id:form_id
			},function(data){
				$scope.searchQualityControlForm();
			});
		});
	};
	$scope.removeQualityControlForm=function(form_id){
		$selectDialog.showMessage("您确定要删除此问卷吗？",function(){
			$diyhttp.post("qualityControl!deleteQualityControlFormById.action",{
				form_id:form_id
			},function(data){
				$scope.searchQualityControlForm();
			});
		});
	};
	$scope.searchQualityControlForm=function(){
		$diyhttp.post("qualityControl!searchQualityControlForm.action",$scope.searchOpt,function(data){
			$scope.listQualityControlForm = data.listQualityControlForm;
			$scope.searchOpt.spu = data.spu;
		});
	};
}).controller("qualityControlFormNewCtrl",function($scope,$diyhttp,$msgDialog,$queryService){
	$scope.currQualityControlForm = {};
	$scope.listQualityControlMeasure = [];
	$scope.initData=function(){
		$queryService.queryQualityControlTarget();
		$queryService.queryQualityControlEnforceable();
		$queryService.queryQualityControlFormState();
		$queryService.queryQualityControlMeasureQuestionType();
	};
	$scope.saveQualityControlForm=function(){
		$diyhttp.post("qualityControl!addQualityControlForm.action",{
			qualityControlForm:$scope.currQualityControlForm,
			listQualityControlMeasure:$scope.listQualityControlMeasure
		},function(data){
			$msgDialog.showMessage("新增成功！");
			$scope.goback();
		});
	};
	$scope.addQualityControlMeasureClick=function(){
		if (!$scope.listQualityControlMeasure)
			$scope.listQualityControlMeasure = new Array();
		$scope.listQualityControlMeasure.push({});
	};
	$scope.removeQualityControlMeasureClick=function(index){
		if (!$scope.listQualityControlMeasure || $scope.listQualityControlMeasure.length<=index)
			return;
		$scope.listQualityControlMeasure.splice(index,1);
	};
}).controller("qualityControlFormModifyCtrl",function($scope,$stateParams,$diyhttp,$msgDialog,$queryService){
	$scope.currQualityControlForm = {};
	$scope.listQualityControlMeasure = [];
	$scope.initData=function(){
		$queryService.queryQualityControlTarget();
		$queryService.queryQualityControlEnforceable();
		$queryService.queryQualityControlFormState();
		$diyhttp.post("qualityControl!queryQualityControlFormById.action",{
			form_id:$stateParams.form_id
		},function(data){
			$scope.currQualityControlForm = data.qualityControlForm;
			if (!$scope.currQualityControlForm) {
				$msgDialog.showMessage("指定的问卷未找到！");
				$scope.goback();
				return;
			}
			$queryService.queryQualityControlMeasureQuestionType();
			$diyhttp.post("qualityControl!queryQualityControlMeasureByFormId.action",{
				form_id:$stateParams.form_id
			},function(data){
				$scope.listQualityControlMeasure = data.listQualityControlMeasure;
			});
		});
	};
	$scope.saveQualityControlForm=function(){
		$diyhttp.post("qualityControl!modifyQualityControlForm.action",{
			qualityControlForm:$scope.currQualityControlForm,
			listQualityControlMeasure:$scope.listQualityControlMeasure
		},function(data){
			$msgDialog.showMessage("修改成功！");
			$scope.goback();
		});
	};
	$scope.addQualityControlMeasureClick=function(){
		if (!$scope.listQualityControlMeasure)
			$scope.listQualityControlMeasure = new Array();
		$scope.listQualityControlMeasure.push({});
	};
	$scope.removeQualityControlMeasureClick=function(index){
		if (!$scope.listQualityControlMeasure || $scope.listQualityControlMeasure.length<=index)
			return;
		$scope.listQualityControlMeasure.splice(index,1);
	};
});