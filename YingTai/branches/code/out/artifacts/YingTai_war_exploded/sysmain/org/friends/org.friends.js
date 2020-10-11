angular.module("org.friends", ['ui.bootstrap',"services","diy.selector",'ui.router','checklist-model','angular-confirm',"diy.dialog"])
.config(function($stateProvider,$urlRouterProvider){
	$stateProvider.state("org.friends",{
		url:"/friends",
		templateUrl: "org/friends/org.friends.html",
		abstract: true,
		controller: "orgFriendCtrl"
	}).state("org.friends.valid",{
		url:"/valid",
		templateUrl: "org/friends/org.friends.valid.html",
		controller: "orgFriendValidCtrl"
	}).state("org.friends.auditing",{
		url:"/auditing",
		templateUrl: "org/friends/org.friends.auditing.html",
		controller: "orgFriendAuditingCtrl"
	});
	$urlRouterProvider.when("/org/friends","/org/friends/valid");
}).controller("orgFriendAuditingCtrl",function($scope,$diyhttp,$msgDialog,$confirmOrgRelationModal){
	$scope.listOrgRelation=[];
	$scope.searchOpt={
		spu:{},
		orgName:"",
		friendStatus:11
	};
	$scope.initData=function(){
		$scope.loadOrgRelation();
	};
	$scope.loadOrgRelation=function(){
		$diyhttp.post("org!queryMyFriends.action",$scope.searchOpt,function(result) {
			$scope.listOrgRelation=result.list;
			$scope.searchOpt.spu = result.spu;
		});
	};
	$scope.confirmFriend=function(relation,isPass){
		if(!relation){
			$msgDialog.showMessage("无效的合作关系！");
			return;
		}
		if (isPass) {
			$confirmOrgRelationModal.open({
				org_relation_id:relation.org_relation.id,
				sharePatientInfo:0,
				reportType:relation.org_relation.publish_report_type,
				customerType:0
			},function(){
				$scope.loadOrgRelation();
			});
		}else {
			$diyhttp.post("org!confirmFriend.action",{
				relation_id:relation.org_relation.id,
				isPass:false
			},function(result) {
				$scope.loadOrgRelation();
			});
		}
	};
}).controller("orgFriendValidCtrl",function($scope,$diyhttp,$selectDialog,$msgDialog,$modifyPublishReportOrg){
	$scope.listOrgRelation=[];
	$scope.searchOpt={
		spu:{},
		orgName:"",
		friendStatus:1
	};
	$scope.reportOrg="";
	$scope.initData=function(){
		$scope.loadOrg();
	};
	$scope.loadOrg=function(){
		$diyhttp.post("org!queryMyFriends.action",$scope.searchOpt,function(result) {
			$scope.listOrgRelation=result.list;
			$scope.searchOpt.spu = result.spu;
		});
	};
	$scope.modifyPublishReportOrg=function(relation){
		$modifyPublishReportOrg.open({
			org_relation_id:relation.org_relation.id,
			sharePatientInfo:relation.org_relation_config.share_patient_info,
			reportType:relation.org_relation.publish_report_type,
			customerType:(relation.org_relation_config.publish_report_org_id==$scope.currOrg.id?1:2)
		},function(){
			$scope.loadOrg();
		});
	};
	$scope.openOrClosePiShare=function(orgRelation){
		if (!orgRelation)
			return;
		$diyhttp.post("org!openOrClosePiShare.action",{
			orgId:orgRelation.relation_org_id,
			share_type:orgRelation.org_relation_config.share_patient_info
		},function(data){
			$scope.loadOrg();
		});
	};
	$scope.cancelFriend=function(relation_id){
		if(!relation_id || relation_id<=0){
			$msgDialog.showMessage("无效的合作关系！");
			return;
		}
		$selectDialog.showMessage('是否确定撤销合作关系?',function() {
        	$diyhttp.post("org!cancelFriend.action",{
        		relation_id:relation_id
        	},function(result) {
				$scope.loadOrg();
			});
        });
	};
	
	$scope.setReportOrg=function(relation){
		var modalInstance = $uibModal.open({
		      templateUrl: 'selectReportOrg.html',
		      controller: 'friendReportCtrl',
		      resolve:{relation:relation}
		    });
			
		modalInstance.result.then(function (result) {
			if(result){
				$scope.msg=result.name;
				$timeout(function(){
					$scope.msg=""
				},1500);
			}
	    });
	};
	
//	$scope.tab={isActive:false};
//	$scope.$watch("diagnosisCount.joiningOrgCount", function() {
//		//$scope.tab.isActive=true;
//		$scope.loadOrg();
//	});
}).controller("orgFriendCtrl",function($scope,$diyhttp,$uibModal,$timeout,$confirm,$msgDialog){
	$scope.tabItems=[{
		index:0,
		title:"合作机构",
		active:false,
		stateName:"org.friends.valid"
	},{
		index:1,
		title:"待审核",
		active:false,
		stateName:"org.friends.auditing"
	}];
});