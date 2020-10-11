angular.module("main", [ "ngAnimate","ui.router","ui.bootstrap","vastsoft.directives","myDirectives","yingtai.common","services","dicom",
                         "message","dic","log","adminuser","user","org","template","stat",
                         "share","qualityControl"])
.run(function($rootScope,$window,$imagesDialog,$templateCache){
	$templateCache.put("templates/vs-accordion-heading.tamplate.html",'<div class="panel panel-default">'+
			'<div class="panel-heading" class="accordion-toggle" ng-click="toggleOpen()" style="cursor: pointer;">'+
			'<span class="pull-right">&nbsp;<i ng-class="isOpen?\'glyphicon glyphicon-chevron-up\':\'glyphicon glyphicon-chevron-down\'"></i></span>'+
			'<h4 class="panel-title" style="color:white;" uib-accordion-transclude="heading">'+
			'<span uib-accordion-header ng-class="{\'text-muted\': isDisabled}">'+
				'{{heading}}'+
			'</span></h4></div>'+
			'<div class="panel-collapse collapse" uib-collapse="!isOpen">'+
			'<div class="panel-body" ng-transclude></div>'+
			'</div>'+
			'</div>');
	$rootScope.goback=function(){
		$window.history.back();
	};
	$rootScope.showBrowse=function(fileMgr){
		$imagesDialog.show(fileMgr);
	};
}).config(function(){
	
}).controller("mainCtrl", function($scope,$dicom,$http,$window,$interval,$medicalDetailDialog) {
	$scope.arrayPart = [];
	$scope.arrayOrg=[];
	// 获取指定设备类型下的部位类型
	$scope.queryPartList = function(device_type_id,callBack) {
		if (device_type_id == 0) {
			$scope.arrayPart = [];
			return;
		}
		$http.post("sys!queryPartList.action", {
			parentId : device_type_id
		}).success(function(data, status, headers, config) {
			if (data.code == 0) {
				$scope.arrayPart = data.data.listDicValue;
				if (callBack)callBack();
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.arrayDoctors=[];
	// 查询本机构下的所有医生
	$scope.queryDoctorsInOrg = function(orgId) {
		$http.post("diagnosis!queryDoctprsInOrgId.action",{orgId:orgId})
		.success(function(data) {
			if (data.code == 0) {
				$scope.arrayDoctors = data.data.listDoctor;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	//查询所有有效的机构
	$scope.queryAllValidOrg=function(){
		$http.post("org!queryAllValidOrg.action")
		.success(function(data){
			if (data.code==0) {
				$scope.arrayOrg=data.data.listOrg;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.showDicomByImgId=function(dicom_img_id){
		$dicom.openDicoms({
            dicom_img_id:dicom_img_id
		});
	};
	$scope.curr_user = {};
	$scope.showMedicalHisDetail=function(medicalHis){
		$medicalDetailDialog.showDialog(medicalHis.id);
	};
	$scope.getCurrLogin = function() {
		$http.post("user!currentUser.action")
		.success(function(result) {
			if(result.code===0){
				$scope.curr_user=result.data.curr_user;
			}else{
				$window.location.href="../login";
			}
		});
	};
	
	$scope.logout=function(){
		$http.post("user!logout.action")
		.success(function(result) {
			if(result.code===0){
				$window.location.href="../login";
			}
		});
	};
	
	$scope.waitCount={a_user_count:0,a_org_count:0,change_user_count:0,change_org_count:0};
	
	var timer=$interval(function(){
		$http.post("user!approvingUserCount.action")
		.success(function(result) {
			$scope.waitCount.a_user_count=result.data.approving_user_count;
			$scope.waitCount.change_user_count=result.data.change_user_count;
		});
		
		$http.post("org!approvingOrgCount.action")
		.success(function(result) {
			$scope.waitCount.a_org_count=result.data.approving_org_count;
			$scope.waitCount.change_org_count=result.data.change_org_count;
		});
		
	},15000);
	
	$scope.getCurrLogin();
	
	$scope.$on('$destroy', function() {
		if (angular.isDefined(timer)) {
		    $interval.cancel(timer);
		    timer = undefined;
		 }
	});
}).controller("dateSeletor",function($scope, $http) {
	 $scope.today = function(){ // 创建一个方法， 
         $scope.dt = new Date(); // 定义一个属性来接收当天日期
     };
     
     $scope.today(); // 运行today方法
     $scope.clear = function(){  //当运行clear的时候将dt置为空
         $scope.dt = null;
     }
     
     $scope.open = function($event){  // 创建open方法 。 下面默认行为并将opened 设为true
         $event.preventDefault();
         $event.stopPropagation();
         $scope.opened = true;
     }
     $scope.disabled = function(date , mode){ 
         return false;
     }
     $scope.toggleMin = function(){
         $scope.minDate = $scope.minDate ? null : new Date(); //3元表达式，没啥好说的
     }
     
     $scope.toggleMin();
     
     $scope.dateOptions = {
         format : 'yyyy-MM-dd',
         weekStart:1,
         startingDay : 1
     }
//     $scope.formats = ['dd-MMMM-yyyy','yyyy/MM/dd','dd.MM.yyyy','shortDate']; //日期显示格式 
     $scope.dateFormat = 'yyyy-MM-dd';// 将formats的第0项设为默认显示格式 
});
