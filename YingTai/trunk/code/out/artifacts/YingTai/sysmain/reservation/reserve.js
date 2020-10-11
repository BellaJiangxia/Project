/**
 * Created by vastsoft on 2016/9/12.
 */

angular.module("reservation", ["ngAnimate","ui.bootstrap","diy.dialog", "services", "diy.selector","ui.bootstrap.datetimepicker"])
  .config(function($stateProvider, $urlRouterProvider) {

    $stateProvider.state("reserve", {
      url : "/reserve",
      template : "<div ui-view></div>",
      abstract : true
    });

    $stateProvider.state("reserve.request", {
      url : "/request",
      templateUrl : "reservation/reserve.request.html",
      controller : "requestListCtrl"
    });

    $stateProvider.state("reserve.receive", {
      url : "/receive",
      templateUrl : "reservation/reserve.receive.html",
      controller : "receiveListCtrl"
    });

    $stateProvider.state("reserve.detail", {
      url : "/detail/:id",
      templateUrl : "reservation/reserve.detail.html",
      controller : "reserveDetailCtrl"
    });

    $stateProvider.state("reserve.info", {
      url : "/info/:id",
      templateUrl : "reservation/reserve.info.html",
      controller : "reserveDetailCtrl"
    });

  })

  .filter("item2string",function(){
    return function(arr){
      if(arr&&angular.isArray(arr)){
        var ss=[];
        angular.forEach(arr, function(v) {
          ss.push(v.v_item_name);
        });
        return ss.join(",");
      }else return "";
    };
  })

  .filter("fee2string",function($filter){
    return function(arr){
      if(arr&&angular.isArray(arr)){
        var ss=[];
        angular.forEach(arr, function(v) {
          ss.push("【"+ v.fee_name+"】"+v.itemCalcType.name+" : "+$filter('number')(v.price,2));
        });
        return ss.join(",");
      }else return "";
    };
  })

  .filter("status4recive",function(){
    return function(arr){
      if(arr&&angular.isArray(arr)){
        return res=arr.filter(function(o){
          if(o.code>2&& o.code!==99) return true;
          else return false;
        });
      }else return [];
    };
  })

  .factory("calcPrice",function(){
    return function(listService){
      var total_price=0;
      if(angular.isArray(listService)){
        angular.forEach(listService,function(s){
          switch(s.calc_type){
            case 1:total_price+= s.service_price;break;
            case 2:total_price+=s.listItem.length* s.service_price; break;
            case 3:
              var tms= s.listItem;
              angular.forEach(tms,function(tm){
                total_price+=tm.item_count* s.service_price;
              });
              break;
          }

          var fees= s.listFee;
          angular.forEach(fees,function(f){
            switch (f.fee_calc_type){
              case 11:total_price+= f.price; break;
              case 12:total_price+=s.listItem.length* f.price; break;
            }
          });

        });
      }
      return total_price;
    };
  })

.controller("requestListCtrl",function($scope,$diyhttp,$msgDialog,$vsDialogs){
    $scope.searchOpt={spu:{}};

    var initData=function(){
      $diyhttp.post("common!queryReservationStatus.action",{},function(data){
        $scope.statusList=data.list;
      });

      $scope.loadList();
    };

    $scope.loadList=function(){
      $diyhttp.post("reserve!queryRequestReserveList.action",$scope.searchOpt,function(data){
        $scope.listReserve=data.list;
        $scope.searchOpt.spu=data.spu;
      });
    };

    $scope.sendReserve=function(id){
      $vsDialogs.showConfirmDialog("是否确定提交该预约申请？",function(){
        $diyhttp.post("reserve!submitReservation.action",{id:id},function(){
          $msgDialog.showMessage("已发送预约申请，请耐心等待");
          $scope.loadList();
        });
      });
    };

    $scope.deleteReserve=function(id){
      $vsDialogs.showConfirmDialog("是否确定要删除该预约？",function(){
        $diyhttp.post("reserve!cancelReservation.action",{id:id},function(){
          $msgDialog.showMessage("删除成功");
          $scope.loadList();
        });
      });
    };

    initData();
  })

.controller("receiveListCtrl",function($scope,$diyhttp,$vsDialogs,$msgDialog,$uibModal){
    $scope.searchOpt={spu:{}};

    var initData=function(){
      $diyhttp.post("common!queryReservationStatus.action",{},function(data){
        $scope.statusList=data.list;
      });

      $scope.loadList();
    };

    $scope.loadList=function(){
      $diyhttp.post("reserve!queryReceiveReserveList.action",$scope.searchOpt,function(data){
        $scope.listReserve=data.list;
        $scope.searchOpt.spu=data.spu;
      });
    };

    $scope.openConfirmDlg=function(obj,opt){
      if(obj){
        var modalInstance = $uibModal.open({
          templateUrl: 'reservation/dlgConfirm.html',
          controller: 'confirmReserveCtrl',
          resolve:{dlgOptions:{obj:obj,opt:opt}}
        });

        modalInstance.result.then(function (result) {
          if(result) $scope.loadList();
        });
      }else{
        $msgDialog.showMessage("未知的预约");
      }
    };

    initData();
  })

.controller("reserveDetailCtrl",function($scope,$patientInfoShareSelectOrgModal,$diyhttp,$uibModal,$msgDialog,$vsDialogs,$stateParams,calcPrice,$state,$timeout){
    $scope.dateOpened = false;
    $scope.dateOptions={
      minDate:new Date(),
      showWeeks: false
    };

    var id=$stateParams.id;

    $scope.loadDetail=function(){
      if(id){
        $diyhttp.post("reserve!queryReservationById.action",{id:id},function(data) {
          $scope.reserve=data.reserve;
          $scope.reserve.reservation_time=Json2date($scope.reserve.reservation_time);
        });
      }else{
        $scope.reserve={};
      }
    };

    $scope.checkPatient= function () {
      if($scope.reserve.patient_idCard){
        $diyhttp.post("patient!queryPatientByIdentityId.action",{identity_id:$scope.reserve.patient_idCard},function(data){
          if(data&&data.patient){
            $scope.reserve.patient_id=data.patient.id;
            $scope.reserve.patient_name=data.patient.name;
            $scope.reserve.patient_mobile=data.patient.phone_num;
            $scope.reserve.patient_idCard=data.patient.identity_id;
            $scope.reserve.patient_gender=data.patient.gender;
          }else{
            $scope.reserve.patient_idCard=$scope.reserve.patient_idCard;
            $scope.reserve.patient_id=null;
            $scope.reserve.patient_name=null;
            $scope.reserve.patient_mobile=null;
            $scope.reserve.patient_gender=null;
          }
        });
      }

    };

    $scope.selectRequestOrg=function(){
      var modalInstance = $uibModal.open({
        templateUrl: 'reservation/dlgSelectOrg.html',
        controller: 'slctOrgCtrl'
      });

      modalInstance.result.then(function (result) {
        if(result){
          $scope.reserve.receive_org_id=result.id;
          $scope.reserve.v_receive_org=result.org_name;
        }else{
          $scope.reserve.receive_org_id=null;
          $scope.reserve.v_receive_org=null;
        }
      });
    };

    $scope.addReserveService=function(obj){
      if($scope.reserve&&$scope.reserve.receive_org_id){
        var modalInstance = $uibModal.open({
          templateUrl: 'reservation/dlgSelectService.html',
          controller: 'selectServiceCtrl',
          size:"lg",
          scope: $scope,
          resolve:{slctSVC:obj}
        });

        modalInstance.result.then(function (result) {
          if(result){
            if(!$scope.reserve.listService)$scope.reserve.listService=[];
            if(!obj)$scope.reserve.listService.push(result);
            $scope.reserve.total_price=calcPrice($scope.reserve.listService);
          }
        });
      }else{
        $msgDialog.showMessage("请选择预约医院");
      }
    };

    $scope.removeService=function(idx){
      $vsDialogs.showConfirmDialog("您是否确定要移除此项服务？",function(){
        $scope.reserve.listService.splice(idx);
        $scope.reserve.total_price=calcPrice($scope.reserve.listService);
      });
    };

    $scope.save=function(){
      if($scope.reserve.reservation_time==null){
        $msgDialog.showMessage("请选择预约时间");
        return;
      }

      $diyhttp.post("reserve!saveReservation.action",{reserve:$scope.reserve},function(data){
        $msgDialog.showMessage("保存成功");
        $scope.reserve.reservation_id=data.id;
      });
    };

    $scope.send=function(){
      if($scope.reserve.reservation_time==null){
        $msgDialog.showMessage("请选择预约时间");
        return;
      }

      $vsDialogs.showConfirmDialog("是否确定提交该预约申请？",function(){
        $diyhttp.post("reserve!submitReservation.action",{reserve:$scope.reserve},function(){
          $msgDialog.showMessage("已发送预约申请，请耐心等待");
          $timeout(function(){
            $scope.goback();
          });
        });
      });

    };

    $scope.openConfirmDlg=function(obj,opt){
      if(obj){
        var modalInstance = $uibModal.open({
          templateUrl: 'reservation/dlgConfirm.html',
          controller: 'confirmReserveCtrl',
          scope:$scope,
          resolve:{dlgOptions:{obj:obj,opt:opt}}
        });

        modalInstance.result.then(function (result) {
          if(result){
            $state.reload();
          }
        });
      }else{
        $msgDialog.showMessage("未知的预约");
      }
    };

    $scope.loadDetail();

  })

.controller("selectServiceCtrl",function($scope,$diyhttp,$uibModalInstance,slctSVC){
    $scope.showList=true;
    $scope.showDetail=false;
    $scope.items=[];
    $scope.fees=[];
    $scope.service={};

    $scope.selectNext=function(){
      $scope.serviceInfo=null;
      $scope.showList=false;
      $scope.showDetail=true;
      $scope.items=[];
      $scope.fees=[];

      $diyhttp.post("reserve!queryServiceById.action",{id:$scope.slctService.service_id},function(data){
        if(data&&data.service){
          $scope.serviceInfo=data.service;

          if(slctSVC){
            $scope.items=angular.copy(slctSVC.listItem);
            $scope.fees=angular.copy(slctSVC.listFee);

            if(slctSVC.listItem){
              for(var i= 0,len=$scope.serviceInfo.listItem.length;i<len;i++){
                for(var k= 0,feeLength=slctSVC.listItem.length;k<feeLength;k++){
                  if($scope.serviceInfo.listItem[i].item_id===slctSVC.listItem[k].item_id){
                    if(slctSVC.listItem[k].item_count) $scope.serviceInfo.listItem[i].item_count=slctSVC.listItem[k].item_count;
                    $scope.serviceInfo.listItem[i].isSelected=true;
                    break;
                  }
                }
              }
            }

            if(slctSVC.listFee){
              for(var i= 0,len=$scope.serviceInfo.listFee.length;i<len;i++){
                for(var k= 0,feeLength=slctSVC.listFee.length;k<feeLength;k++){
                  if($scope.serviceInfo.listFee[i].fee_id===slctSVC.listFee[k].fee_id){
                    $scope.serviceInfo.listFee[i].isSelected=true;
                    break;
                  }
                }
              }
            }
          }
        }
      });
    };

    $scope.selectPrev=function(){
      $scope.showList=true;
      $scope.showDetail=false;
    };

    if(slctSVC){
      $scope.slctService=slctSVC;
      $scope.selectNext();
    }
    else $scope.slctService=null;

    var initData=function(){
      $diyhttp.post("reserve!queryValidateService.action",{id:$scope.reserve.receive_org_id},function(data){
        if(data&&data.list){
          $scope.listOrgService=data.list || [];
          if(slctSVC){
            var obj=$scope.listOrgService.filter(function(o){return slctSVC.service_id===o.service_id});
            if(obj) obj[0].isSelected=true;
          }
        }
      });
    };

    $scope.chkChange=function(isSlcted,serv){
      if($scope.slctService){
        $scope.slctService.isSelected=false;
        $scope.slctService=null;
        $scope.slctService=serv;
        $scope.slctService.isSelected=isSlcted;
      }else{
        $scope.slctService=serv;
      }
    };

    $scope.selectFee=function(isSlcted,fee){
      if(isSlcted){
        $scope.fees.push(fee);
      }else{
        $scope.fees=$scope.fees.filter(function(data){
          if (data.fee_id!== fee.fee_id) return true;
          else return false;
        });
      }
    };

    $scope.selectItem=function(isSlcted,item){
      if(isSlcted){
        $scope.items.push(item);
      }else{
        $scope.items=$scope.items.filter(function(data){
          if (data.item_id!== item.item_id) return true;
          else return false;
        });
      }
    };

    $scope.changeCount=function(item){
      for(var i= 0,len=$scope.items.length;i<len;i++){
        if($scope.items[i]&&$scope.items[i].item_id===item.item_id){
          $scope.items[i].item_count=item.item_count;
          break;
        }
      }
    };

    $scope.ok=function(){
      if($scope.slctService){
        $scope.service=$scope.slctService;
        $scope.service.listItem=$scope.items;
        $scope.service.listFee=$scope.fees;
      }
      $uibModalInstance.close($scope.service);
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    initData();
  })

.controller("confirmReserveCtrl",function($scope,dlgOptions,$uibModalInstance,$diyhttp,$msgDialog){
    $scope.options=dlgOptions;
    $scope.parm={};
    $scope.dateOpened = false;
    $scope.dateOptions={
      minDate:new Date(),
      showWeeks: false
    };

    if(!$scope.reserve)$scope.reserve=dlgOptions.obj;
    if(!angular.isDate($scope.reserve.reservation_time)) $scope.reserve.reservation_time=Json2date(dlgOptions.obj.reservation_time);

    if($scope.options.opt===11) $scope.options.title="是否确定接受该预约?";
    else if($scope.options.opt===22) $scope.options.title="是否确定结束该预约?";

    $scope.ok=function(){
      if($scope.options&&$scope.options.opt){
        if(!$scope.parm.isPass) $scope.parm.reason=null;

        if($scope.options.opt===11){
          $diyhttp.post("reserve!acceptReservation.action", {
            id: $scope.reserve.reservation_id,
            isPass: $scope.parm.isPass,
            reason: $scope.parm.reason,
            reserveTime: $scope.reserve.reservation_time,
            reserveNo:$scope.reserve.reservation_no
          }, function () {
            $msgDialog.showMessage("操作成功");
            $uibModalInstance.close(true);
          });
        }else if($scope.options.opt===22){
          $diyhttp.post("reserve!dealReservation.action", {
            id: $scope.reserve.reservation_id,
            isPass: $scope.parm.isPass,
            reason: $scope.parm.reason,
            reserveNo:$scope.reserve.reservation_no
          }, function () {
            $msgDialog.showMessage("操作成功");
            $uibModalInstance.close(true);
          });
        }
      }else{
        $msgDialog.showMessage("未知操作");
        $uibModalInstance.close(null);
      }
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

  })

.controller("slctOrgCtrl",function($scope,$uibModalInstance,$diyhttp,$msgDialog){
    $scope.total=0;
    $scope.searchOpt={pageIdx:1,pageSize:10};

    $scope.loadOrgList=function(){
      $diyhttp.post("org!queryOrgListByNameOrCode.action",$scope.searchOpt,function(data){
        $scope.listOrg=data.list;
        $scope.total=data.total;
      });
    };

    $scope.chkChange=function(isSlcted,org){
      if($scope.slctOrg){
        $scope.slctOrg.isSelected=false;
        $scope.slctOrg=null;
        $scope.slctOrg=org;
        $scope.slctOrg.isSelected=isSlcted;
      }else{
        $scope.slctOrg=org;
      }
    };

    $scope.ok=function(){
        $uibModalInstance.close($scope.slctOrg);
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

  });