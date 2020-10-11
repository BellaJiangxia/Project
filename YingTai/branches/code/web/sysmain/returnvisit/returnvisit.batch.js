angular.module("returnvisit.batch", ["ui.bootstrap", "diy.dialog", "services", "diy.selector"])

  .config(function ($stateProvider, $urlRouterProvider) {

    // 回访批次状态机-------------------------------------------------------
    $stateProvider.state("returnvisit.batch", {
      url: "/batch",
      template: "<div ui-view></div>",
      abstract: true
    });

    $stateProvider.state("returnvisit.batch.new", {
      url: "/new",
      templateUrl: "returnvisit/returnvisit.batch.new.html",
      controller: "returnvisitBatchNewCtrl"
    });

    // $stateProvider.state("returnvisit.batch.new.template", {
    // url : "/template",
    // templateUrl : "returnvisit/returnvisit.batch.new.template.html",
    // controller : "returnvisitBatchNewTemplateCtrl"
    // });
    //
    // $stateProvider.state("returnvisit.batch.new.patients", {
    // url : "/patients",
    // templateUrl : "returnvisit/returnvisit.batch.new.patients.html",
    // controller : "returnvisitBatchNewPatientsCtrl"
    // });

    $stateProvider.state("returnvisit.batch.list", {
      url: "/list",
      templateUrl: "returnvisit/returnvisit.batch.list.html",
      controller: "returnvisitBatchListCtrl"
    });
  })

  .filter("batchstatus2name", function () {
    return function (input, uppercase) {
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
  .controller("returnvisitBatchNewCtrl", function ($scope, $http, $msgDialog,$state) {

    $scope.batch_id = null;
    $scope.patients = [];
    $scope.template = $scope.template4new;

    $scope.saveRV = function () {

      var patientIds = [];
      for (var idx in $scope.patients)
        patientIds.push($scope.patients[idx].patient_id);

      var params = {
        batch_id: $scope.batch_id,
        visit_name: $scope.template.template_name,
        listQ: $scope.template.listQ,
        patientIds: patientIds
      };

      if(patientIds.length==0){
        $msgDialog.showMessage("未指定问卷访问对象!");
        return;
      }

      $http.post("returnvisit!saveBatch.action", params).success(function (data) {
        if (data.code == 0) {
          $scope.batch_id=data.data.batch_id;
          $msgDialog.showMessage("保存成功");
        } else {
          $msgDialog.showMessage(data.name);
        }
      });

    };

    $scope.sendRV = function () {
      var patientIds = [];
      for (var idx in $scope.patients)
        patientIds.push($scope.patients[idx].patient_id);

      var params = {
        batch_id: $scope.batch_id,
        visit_name: $scope.template.template_name,
        listQ: $scope.template.listQ,
        patientIds: patientIds
      };

      if(patientIds.length==0){
        $msgDialog.showMessage("未指定问卷访问对象!");
        return;
      }

      $http.post("returnvisit!sendBatch.action", params).success(function (data) {
        if (data.code == 0) {
          $msgDialog.showMessage("成功发送");
          $scope.goback();
        } else {
          $msgDialog.showMessage(data.name);
        }
      });
    };
  })

  .controller("returnvisitBatchNewTemplateCtrl", function ($scope, $http, $msgDialog) {

    $scope.onChangeQuesitonType = function (q, type) {
      q.question_type = type;
    }

    $scope.addQuestion = function () {
      var listQ = $scope.template.listQ;

      listQ.push({
        question_type: 1,
        listOpt: []
      });

      for (var idx = 0; idx < listQ.length; idx++)
        listQ[idx].question_idx = idx + 1;
    };

    $scope.moveQuestionDown = function (q) {
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

    $scope.moveQuestionUp = function (q) {
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

    $scope.delQuestion = function (q) {
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

    $scope.addOpt = function (q) {

      if (q.listOpt.length >= 5) {
        $msgDialog.showMessage("最多添加5个选项");
        return;
      }

      q.listOpt.push({
        option_name: ""
      });
    };

    $scope.moveOptDown = function (q, opt) {
      for (var idx = 0; idx < q.listOpt.length; idx++) {
        if (q.listOpt[idx] == opt) {
          if (idx >= q.listOpt.length - 1)
            return;

          q.listOpt[idx + 1] = q.listOpt.splice(idx, 1, q.listOpt[idx + 1])[0];

          return;
        }
      }
    };

    $scope.moveOptUp = function (q, opt) {
      for (var idx = 0; idx < q.listOpt.length; idx++) {
        if (q.listOpt[idx] == opt) {
          if (idx <= 0)
            return;

          q.listOpt[idx - 1] = q.listOpt.splice(idx, 1, q.listOpt[idx - 1])[0];

          return;
        }
      }
    };

    $scope.delOpt = function (q, opt) {
      for (var idx = 0; idx < q.listOpt.length; idx++) {
        if (q.listOpt[idx] == opt) {
          q.listOpt.splice(idx, 1);
          return;
        }
      }
    };

  })

  .controller("returnvisitBatchNewPatientsCtrl", function ($scope, $http, $msgDialog, $uibModal) {

    $scope.addPatients = function () {

      $uibModal.open({
        animation: true,
        templateUrl: 'returnvisit/returnvisit.batch.new.model.addpatients.html',
        controller: "returnvisitBatchNewModelAddPatientsCtrl",
        size: "lg",
        scope: $scope
      });

    };

    $scope.onPatientChecked = function (patient, checked) {
      for (var idx in $scope.patients) {
        if ($scope.patients[idx].patient_id == patient.patient_id && checked == false) {
          $scope.patients.splice(idx, 1);
          return;
        }
      }

      $scope.patients.push(patient);
    };

    $scope.removePatient = function (patient) {
      for (var idx in $scope.patients) {
        if ($scope.patients[idx].patient_id == patient.patient_id) {
          $scope.patients.splice(idx, 1);
          return;
        }
      }
    };

  })

  .controller("returnvisitBatchNewModelAddPatientsCtrl", function ($scope, $http, $msgDialog,$uibModalInstance) {

    $scope.searchOpt = {
      patient_name: "",
      patient_gender: "0",
      patient_mobile: ""
    };

    $scope.onClickPatient = function (patient) {
      patient.checked = !(patient.checked == undefined ? false : patient.checked);

      $scope.onPatientChecked(patient, patient.checked);
    };

    $scope.searchWeixinPatients = function () {

      $http.post("returnvisit!searchWeixinPatients.action", $scope.searchOpt).success(function (data) {
        if (data.code == 0) {
          $scope.o_patients = data.data.patients;

          for (var idx in $scope.o_patients) {

            for (idx2 in $scope.patients) {
              if ($scope.o_patients[idx].patient_id == $scope.patients[idx2].patient_id) {
                $scope.o_patients[idx].checked = true;
                break;
              }
            }

          }

        } else {
          $msgDialog.showMessage(data.name);
        }
      });

    };

    $scope.cancel=function(){
      $uibModalInstance.dismiss('cancel');
    };

  })

  .controller("returnvisitBatchListCtrl", function ($scope, $http, $msgDialog,$uibModal,$state,$selectDialog) {

    $scope.searchOpt = {
      template_name: "",
      onlyMe: "false",
      status: "0",
      spu: {}
    };

    $scope.batchs = [];

    $scope.searchBatchs = function () {
      $http.post("returnvisit!searchBatchs.action", $scope.searchOpt).success(function (data) {
        if (data.code == 0) {
          $scope.batchs = data.data.batchs;
          $scope.searchOpt.spu=data.data.spu;
        } else {
          $msgDialog.showMessage(data.name);
        }
      });

    };

    $scope.initData = function () {
      $scope.searchBatchs();
    };

    $scope.addBatch = function () {
      var modalInstance = $uibModal.open({
        templateUrl: 'returnvisit/returnvisit.model.seletTemplate.html',
        controller: 'dlgSelectTmplCtrl'
      });

      modalInstance.result.then(function (result) {
        if(result){
          $http.post("returnvisit!queryTemplateById.action", {template_id:result.template_id}).success(function(data) {
            if (data.code == 0) {
              $scope.template = data.data.template;

              $scope.setTemplate4New($scope.template);

              $state.go("returnvisit.batch.new");

            } else {
              $msgDialog.showMessage(data.name);
            }
          });
        }
      });
    };

    $scope.sendBatch=function(id){
      $selectDialog.showMessage("您确定要发送该批次吗？",function(){
        $http.post("returnvisit!sendBatch.action", {batch_id:id}).success(function(data) {
          if (data.code == 0) {
            $msgDialog.showMessage("已成功发送");
            $scope.searchBatchs();
          } else {
            $msgDialog.showMessage(data.name);
          }
        });
      });
    };

  })

.controller("dlgSelectTmplCtrl",function($scope, $http, $msgDialog,$uibModalInstance){
    $scope.query = {
      template_name : "",
      onlyMe : "false",
      spu : {}
    };

    $scope.templates = [];
    $scope.slctTmpl=null;

    $scope.searchTemplates = function() {
      $http.post("returnvisit!searchTemplates.action", $scope.query).success(function(data) {
        if (data.code == 0) {
          $scope.templates = data.data.templates;
          $scope.query.spu=data.data.spu;
        } else {
          $msgDialog.showMessage(data.name);
        }
      });

    };

    $scope.ok=function(){
      $uibModalInstance.close($scope.slctTmpl);
    };

    $scope.cancel=function(){
      $uibModalInstance.dismiss('cancel');
    };

    $scope.chkChange=function(isSlcted,tmpl){
      if($scope.slctTmpl){
        $scope.slctTmpl.isSelected=false;
        $scope.slctTmpl=null;
        $scope.slctTmpl=tmpl;
        $scope.slctTmpl.isSelected=isSlcted;
      }else{
        $scope.slctTmpl=tmpl;
      }
    };

    $scope.searchTemplates();

  });

