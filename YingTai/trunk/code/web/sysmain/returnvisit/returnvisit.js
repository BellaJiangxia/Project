angular.module("returnvisit", [ "ui.bootstrap", "diy.dialog", "services", "diy.selector" ])

.config(function($stateProvider, $urlRouterProvider) {

	// 回访批次状态机-------------------------------------------------------
	$stateProvider.state("returnvisit", {
		url : "/returnvisit",
		template : "<div ui-view></div>",
		controller : "returnvisitCtrl",
		abstract : true
	});

	// 回访------------------------------------------------------

	$stateProvider.state("returnvisit.list", {
		url : "/list/:batch_id,:batch_name",
		templateUrl : "returnvisit/returnvisit.list.html",
		controller : "returnvisitListCtrl"
	});

	$stateProvider.state("returnvisit.detail", {
		url : "/detail/:id",
		templateUrl : "returnvisit/returnvisit.detail.html",
		controller : "returnvisitDetailCtrl"
	});
})

.filter("batchstatus2name", function() {
	return function(input, uppercase) {
		if (input == 1)
			return "未发送";
		if (input == 2)
			return "已发送";
		if (input == 9)
			return "未发送完成";

		return "";
	}
})

// 此控制器负责处理创建回访批次的所有逻辑，页面控制在各个子状态机内部
.controller("returnvisitCtrl", function($scope, $http, $msgDialog) {

	$scope.template4new = null;
	$scope.setTemplate4New = function(template) {
		$scope.template4new = template;
	};

	$scope.patients4new = [];

})

.controller("returnvisitListCtrl", function($scope, $http, $stateParams, $msgDialog,$selectDialog) {
	$scope.searchOpt = {
		visit_name : "",
		onlyMe : "false",
		batch_id : $stateParams.batch_id == '' ? null : $stateParams.batch_id,
		spu : {}
	};

	$scope.batch_name = $stateParams.batch_name == '' ? null : $stateParams.batch_name;

	$scope.visits = [];

	$scope.searchVisits = function() {
		$http.post("returnvisit!searchVisits.action", $scope.searchOpt).success(function(data) {
			if (data.code == 0) {
				$scope.visits = data.data.visits;
				$scope.searchOpt.spu=data.data.spu;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});

	};

		$scope.sendVisit=function(id){
			$selectDialog.showMessage("您确定要发送该问卷吗？",function(){
				$http.post("returnvisit!sendVisit.action", {template_id:id}).success(function(data) {
					if (data.code == 0) {
						$msgDialog.showMessage("已成功发送");
						$scope.searchVisits();
					} else {
						$msgDialog.showMessage(data.name);
					}
				});
			});
		};

	//$scope.initData = function() {
		$scope.searchVisits();
	//};

})

.controller("returnvisitDetailCtrl", function($scope, $http, $msgDialog, $stateParams) {

	var template_id = $stateParams.id;
	if (template_id == "")
		template_id = null;

	$scope.template = {};

	$scope.onChangeQuesitonType = function(q, type) {
		q.question_type = type;
	}

	$scope.loadTemplate = function(param) {

		if (param == null)
			return;

		$http.post("returnvisit!queryVisitInfoById.action", param).success(function(data) {
			if (data.code == 0) {
				$scope.template = data.data.visit;
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

		if ($scope.template.listQ.length == 0) {
			$msgDialog.showMessage("请至少增加一个问题");
			return;
		}

		if ($scope.template.template_name == null || $scope.template.template_name == "") {
			$msgDialog.showMessage("请录入回访模版名称");
			return;
		}

		$http.post("returnvisit!createReturnVisit.action", $scope.template).success(function(data) {
			if (data.code == 0) {
				$msgDialog.showMessage("模版保存成功");
				$scope.goback();
			} else {
				$msgDialog.showMessage(data.name);
			}
		});

	}

});
