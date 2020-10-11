/**
 * Created by vastsoft on 2016/7/30.
 */

angular.module("inquiry", ["ngAnimate","ui.bootstrap","diy.dialog", "services", "diy.selector"])

  .config(function($stateProvider, $urlRouterProvider) {

    $stateProvider.state("inquiry", {
      url : "/inquiry",
      template : "<div ui-view></div>",
      abstract : true
    });

    $stateProvider.state("inquiry.list", {
      url : "/list",
      templateUrl : "inquiry/inquiry.list.html",
      controller : "inquiryListCtrl"
    });

    $stateProvider.state("inquiry.reply", {
      url : "/reply",
      templateUrl : "inquiry/reply.list.html",
      controller : "replyListCtrl"
    });

    $stateProvider.state("inquiry.detail", {
      url : "/detail/:id",
      templateUrl : "inquiry/inquiry.detail.html",
      controller : "inquiryDetailCtrl"
    });

  })

  .filter("imgFilter",function(){
    return function(arr){
      var idx=2;
      angular.forEach(arr,function(value){
        if(value&&value.reply_type===10){
          value.idx=idx++;
        }
      });
      return arr;
    };
  })

.controller("inquiryListCtrl",function($scope,$diyhttp){
    $scope.searchOpt={spu:{}};
    $scope.listInquriy=[];

    $scope.loadList=function(){
      $diyhttp.post("inquiry!queryNewInquiryList.action",$scope.searchOpt,function(data) {
        $scope.listInquriy=data.list;
        $scope.searchOpt.spu=data.spu;
      });
    };

    $scope.loadList();

  })

.controller("inquiryDetailCtrl",function($scope,$diyhttp,$stateParams,$msgDialog,$uibModal){
    var id=$stateParams.id;
    $scope.inquiry={};

    $scope.loadDetail=function(){
      if(id){
        $diyhttp.post("inquiry!queryInquiryById.action",{id:id},function(data) {
          $scope.inquiry=data.inquiry;
        });
      }else{
        $msgDialog.showMessage("未知的问诊");
        $scope.goback();
      }
    };

    $scope.reply={};

    $scope.replyInquiry=function(){
      var reply={
        id:$scope.inquiry.inquiry_id,
        content:$scope.reply.reply_content
      };

      $diyhttp.post("inquiry!sendReply4Inquiry.action",reply,function(data) {
        $msgDialog.showMessage("已成功回复");
        $scope.goback();
      });
    };

    $scope.showImage=function(idx){
      if(!idx) return [];
      var imgs=$scope.inquiry.images || [];
      var mm=imgs.filter(function(obj){
        return obj.idx==idx;
      });

      if(mm&&mm.length>0) return mm[0];
    };

    $scope.openImage=function(arr){
      var modalInstance = $uibModal.open({
        templateUrl: 'inquiryImages.html',
        controller: 'inquiryImagesCtrl',
        resolve: {
          images: function () {
            return arr;
          }
        }
      });
    };

    $scope.loadDetail();
  })

.controller("inquiryImagesCtrl",function($scope, $uibModalInstance, images){
  $scope.currImages=images;

  $scope.close = function () {
    $uibModalInstance.close();
  };
})


.controller("replyListCtrl",function($scope,$diyhttp){
    $scope.searchOpt={spu:{}};
    $scope.listInquriy=[];

    $scope.loadList=function(){
      $diyhttp.post("inquiry!queryReplyList.action",$scope.searchOpt,function(data) {
        $scope.listInquriy=data.list;
        $scope.searchOpt.spu=data.spu;
      });
    };

    $scope.loadList();
  });