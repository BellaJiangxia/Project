angular.module("returnvisit.template", [ "ui.bootstrap", "diy.dialog", "services", "diy.selector"])

.config(function($stateProvider, $urlRouterProvider) {

	$stateProvider.state("returnvisit.template", {
		url : "/template",
		template : "<div ui-view></div>",
		abstract : true
	});

	$stateProvider.state("returnvisit.template.list", {
		url : "/list",
		templateUrl : "returnvisit/returnvisit.template.list.html",
		controller : "returnvisitTemplateListCtrl"
	});

	$stateProvider.state("returnvisit.template.detail", {
		url : "/detail/:id",
		templateUrl : "returnvisit/returnvisit.template.detail.html",
		controller : "returnvisitTemplateDetailCtrl"
	});
})

.controller("returnvisitTemplateListCtrl", function($scope, $http, $msgDialog,$selectDialog) {

	$scope.searchOpt = {
		template_name : "",
		onlyMe : "false",
		spu : {}
	};

	$scope.templates = [];

	$scope.searchTemplates = function() {

		$http.post("returnvisit!searchTemplates.action", $scope.searchOpt).success(function(data) {
			if (data.code == 0) {
				$scope.templates = data.data.templates;
				$scope.searchOpt.spu=data.data.spu;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});

	};

	$scope.initData = function() {
		$scope.searchTemplates();
	};

		$scope.deleteTmpl=function(id){
			$selectDialog.showMessage("您确定要作废该模版吗？",function(){
				$http.post("returnvisit!deleteTmplById.action", {template_id:id}).success(function(data) {
					if (data.code == 0) {
						$msgDialog.showMessage("已成功作废");
						$scope.searchTemplates();
					} else {
						$msgDialog.showMessage(data.name);
					}
				});
			});
		};

})

.filter("qtype2name", function() {
	return function(input, uppercase) {
		if (input == 3)
			return "问答";
		if (input == 1)
			return "单选";
		if (input == 2)
			return "多选";
		if (input == 4)
			return "打分";
	}
})

.controller("returnvisitTemplateDetailCtrl", function($scope, $http, $msgDialog, $state, $stateParams) {

	var template_id = $stateParams.id;
	if (template_id == "")
		template_id = null;

	$scope.template = $stateParams.template;
	if ($stateParams.template == null)
		$scope.template = {};

	$scope.onChangeQuesitonType = function(q, type) {
		q.question_type = type;
	}

	$scope.loadTemplate = function(param) {

		if (param == null)
			return;

		$http.post("returnvisit!queryTemplateById.action", param).success(function(data) {
			if (data.code == 0) {
				$scope.template = data.data.template;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});

	};

	if (template_id != null) {
		$scope.loadTemplate({
			template_id : template_id
		});
	} else {
		$scope.template.listQ = [ {
			question_idx : 1,
			question_type : 1,
			listOpt : []
		} ];
	}

	$scope.addQuestion = function() {
		var listQ = $scope.template.listQ;

		listQ.push({
			question_type : 1,
			listOpt : []
		});

		for (var idx = 0; idx < listQ.length; idx++)
			listQ[idx].question_idx = idx + 1;
	};

	$scope.moveQuestionDown = function(q) {
		var listQ = $scope.template.listQ;

		for (var idx = 0; idx < listQ.length; idx++) {
			if (listQ[idx] == q) {
				if (idx >= listQ.length - 1)
					return;

				listQ[idx + 1] = listQ.splice(idx, 1, listQ[idx + 1])[0];

				break;
			}
		}

		for (var idx = 0; idx < listQ.length; idx++) {
			listQ[idx].question_idx = idx + 1;
		}
	};

	$scope.moveQuestionUp = function(q) {
		var listQ = $scope.template.listQ;

		for (var idx = 0; idx < listQ.length; idx++) {
			if (listQ[idx] == q) {
				if (idx <= 0)
					return;

				listQ[idx - 1] = listQ.splice(idx, 1, listQ[idx - 1])[0];

				break;
			}
		}

		for (var idx = 0; idx < listQ.length; idx++) {
			listQ[idx].question_idx = idx + 1;
		}
	};

	$scope.delQuestion = function(q) {
		var listQ = $scope.template.listQ;

		for (var idx = 0; idx < listQ.length; idx++) {
			if (listQ[idx] == q) {
				listQ.splice(idx, 1);
				break;
			}
		}

		for (var idx = 0; idx < listQ.length; idx++) {
			listQ[idx].question_idx = idx + 1;
		}
	};

	$scope.addOpt = function(q) {

		if (q.listOpt.length >= 5) {
			$msgDialog.showMessage("最多添加5个选项");
			return;
		}

		q.listOpt.push({
			option_name : ""
		});
	};

	$scope.moveOptDown = function(q, opt) {
		for (var idx = 0; idx < q.listOpt.length; idx++) {
			if (q.listOpt[idx] == opt) {
				if (idx >= q.listOpt.length - 1)
					return;

				q.listOpt[idx + 1] = q.listOpt.splice(idx, 1, q.listOpt[idx + 1])[0];

				return;
			}
		}
	};

	$scope.moveOptUp = function(q, opt) {
		for (var idx = 0; idx < q.listOpt.length; idx++) {
			if (q.listOpt[idx] == opt) {
				if (idx <= 0)
					return;

				q.listOpt[idx - 1] = q.listOpt.splice(idx, 1, q.listOpt[idx - 1])[0];

				return;
			}
		}
	};

	$scope.delOpt = function(q, opt) {
		for (var idx = 0; idx < q.listOpt.length; idx++) {
			if (q.listOpt[idx] == opt) {
				q.listOpt.splice(idx, 1);
				return;
			}
		}
	};

	$scope.saveTemplate = function() {

		if ($scope.template.listQ.length == 0) {
			$msgDialog.showMessage("请至少增加一个问题");
			return;
		}

		if ($scope.template.template_name == null || $scope.template.template_name == "") {
			$msgDialog.showMessage("请录入回访模版名称");
			return;
		}

		$http.post("returnvisit!saveTemplate.action", $scope.template).success(function(data) {
			if (data.code == 0) {
				$msgDialog.showMessage("模版保存成功");
				$scope.goback();
			} else {
				$msgDialog.showMessage(data.name);
			}
		});

	};

	$scope.createVisit = function() {

		$scope.setTemplate4New($scope.template);

		$state.go("returnvisit.batch.new");

	}

});
