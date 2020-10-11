/**
 * Created by vastsoft on 2016/9/12.
 */
angular.module("reserveService", ["ngAnimate","ui.bootstrap","diy.dialog", "services", "diy.selector"])

  .config(function($stateProvider, $urlRouterProvider) {

    $stateProvider.state("service", {
      url : "/service",
      template : "<div ui-view></div>",
      abstract : true
    });

    $stateProvider.state("service.list", {
      url : "/list",
      templateUrl : "reservation/service.list.html",
      controller : "serviceListCtrl"
    });

    $stateProvider.state("service.detail", {
      url : "/detail/:id",
      templateUrl : "reservation/service.detail.html",
      controller : "serviceDetailCtrl"
    });
  })

.controller("serviceListCtrl",function($scope,$queryService,$diyhttp,$vsDialogs,$msgDialog){
    $scope.searchOpt={spu:{}};
    $scope.DeviceList=[];
    $scope.ItemList=[];

    $queryService.queryDeviceType(function(data){
      $scope.DeviceList=data;
    });

    $diyhttp.post("common!queryServiceStatus.action",{},function(data){
      $scope.serviceStatus=data.list;
    });

    $scope.selectItem=function(){
      $scope.queryPartList($scope.searchOpt.deviceType,function(data){
        $scope.ItemList=data;
      },function(){
        $scope.ItemList=[];
      });
    };

    $scope.loadList=function(){
      $diyhttp.post("reserve!queryMyServiceList.action",$scope.searchOpt,function(data){
        $scope.listService=data.list;
        $scope.searchOpt.spu=data.spu;
      });
    };

    $scope.publishService=function(obj){
      if(obj){
        $vsDialogs.showConfirmDialog("是否确定要发布该服务？",function(){
          $diyhttp.post("reserve!publishService.action",{service:obj,isPass:false},function(data){
            $msgDialog.showMessage("发布成功");
            $scope.loadList();
          });
        });
      }
    };

    $scope.deleteService=function(id){
      $vsDialogs.showConfirmDialog("是否确定要删除该服务？",function(){
        $diyhttp.post("reserve!deleteService.action",{id:id},function(data){
          $msgDialog.showMessage("删除成功");
          $scope.loadList();
        });
      });
    };

    $scope.loadList();
  })

.controller("serviceDetailCtrl",function($scope,$queryService,$diyhttp,$uibModal,$vsDialogs,$msgDialog,$stateParams){
    $scope.service={};
    $scope.DeviceList=[];
    $scope.ItemList=[];
    $scope.FeeCalcType=[];
    $scope.ItemFeeCalcType=[];
    $scope.items=[];

    $queryService.queryDeviceType(function(data){
      $scope.DeviceList=data;
    });

    var id=$stateParams.id;

    $scope.loadDetail=function(){
      if(id){
        $diyhttp.post("reserve!queryServiceById.action",{id:id},function(data) {
          $scope.service=data.service;
        });
      }else{
        $scope.service={};
      }
    };

    var initData=function(){
      $scope.isFirst=true;
      $diyhttp.post("common!queryFeeCalcType.action",{},function(data){
        $scope.FeeCalcType=data.list;
      });

      $diyhttp.post("common!queryItemFeeCalcType.action",{},function(data){
        $scope.ItemFeeCalcType=data.list;
      });

      $scope.loadDetail();
    };

    $scope.selectAllItem=function(){
      $scope.isSelectAll=!$scope.isSelectAll;
      if($scope.isSelectAll){
        angular.forEach($scope.ItemList,function(v){
          v.item_selected=true;
          $scope.items.push({item_id:v.id});
        });
      }else{
        $scope.items=[];
        angular.forEach($scope.ItemList,function(v){
          v.item_selected=false;
          v.item_count=null;
          v.item_required=0;
        });
      }
    };

    $scope.save=function(){
      getServiceItem();
      $diyhttp.post("reserve!saveService.action",{service:$scope.service},function(data){
        $scope.service=data.service;
        $msgDialog.showMessage("保存成功");
      });
    };

    $scope.publish=function(){
      getServiceItem();
      $diyhttp.post("reserve!publishService.action",{service:$scope.service,isPass:true},function(data){
        $msgDialog.showMessage("发布成功");
        $scope.goback();
      });
    };

    var getServiceItem=function(){
      $scope.service.listItem=$scope.items;
      // if($scope.obj.items){
      //   angular.forEach($scope.obj.items,function(v){
      //     var oo={item_id: v};
      //     $scope.service.listItem.push(oo);
      //   });
      // }
    };

    $scope.addOtherFee=function(fee){
      var modalInstance = $uibModal.open({
        templateUrl: 'otherFeeDetail.html',
        controller: 'otherFeeCtrl',
        scope: $scope,
        resolve:{fee:fee}
      });

      modalInstance.result.then(function (result) {
        if(result){
          if(!$scope.service.listFee)$scope.service.listFee=[];
          if(!fee)$scope.service.listFee.push(result);
        }
      });
    };

    $scope.removeFee=function(idx){
      $vsDialogs.showConfirmDialog("您是否确定要移除此项费用？",function(){
        $scope.service.listFee.splice(idx);
      });
    };

    $scope.slctItem=function(obj){
      if(obj.item_selected){
        $scope.items=$scope.items.filter(function(data){
          if (data.item_id!== obj.id) return true;
          else return false;
        });
        $scope.items.push({item_id:obj.id,is_required:obj.item_required,check_count:obj.item_count});
      }else{
        obj.item_required=0;
        obj.item_count=0;
        $scope.items=$scope.items.filter(function(data){
          if (data.item_id!== obj.id) return true;
          else return false;
        });
      }
    };

    $scope.$watch("service.device_type",function(a){
      if(a){
        if($scope.isFirst){
          $scope.isFirst=false;
        }else{
          $scope.isSelectAll=false;
          $scope.items=[];
        }

        $scope.queryPartList(a,function(data){
          $scope.ItemList=data;
          if($scope.service.listItem && $scope.service.device_type===a){
            $scope.items=$scope.service.listItem;
            angular.forEach($scope.service.listItem,function(item){
              $scope.ItemList=$scope.ItemList.filter(function(data){
                if(data.id===item.item_id){
                  data.item_selected=true;
                  data.item_count=item.check_count;
                  data.item_required=item.is_required;
                }
                return data;
              });
            });
          }

          if($scope.ItemList.length===$scope.items.length) $scope.isSelectAll=true;
        },function(){
          $scope.ItemList=[];
        });
      }else $scope.ItemList=[];
    });

    initData();
  })

.controller("otherFeeCtrl",function($scope,$uibModalInstance,fee){
    if(fee)$scope.fee=fee;

    $scope.ok=function(){
      $scope.fee.fee_calc_type=$scope.fee.itemCalcType.code;
      $uibModalInstance.close($scope.fee);
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
});