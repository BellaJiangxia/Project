angular.module("dic",["ui.bootstrap","diy.dialog"])
.config(function($stateProvider, $urlRouterProvider){
	$stateProvider.state("dic",{
		url : "/dic",
		templateUrl : "dic/dic.html",
		controller : "dicCtrl"
	}).state("dic.sysconfig",{
		url : "/sysconfig",
		templateUrl : "dic/dic.sys.html",
		controller : "dicSysconfigCtrl"
	}).state("dic.checkpro",{
		url : "/checkpro",
		templateUrl : "dic/dic.checkpro.html",
		controller : "dicCheckproCtrl"
	});
	$urlRouterProvider.when("/dic","/dic/sysconfig");
}).controller("dicCtrl",function($scope,$http,$location,$msgDialog,$selectDialog){
	$scope.statType=1;
	$scope.statTypeMap = {
		sysConfig : true,
		checkpro : false,
		reset : function() {
			this.sysConfig = false;
			this.checkpro = false;
		}
	};
	$scope.selectStatType = function(currPage) {
		$scope.statTypeMap.reset();
		switch (currPage) {
		case "sysConfig":
			$scope.statTypeMap.sysConfig = true;
			$scope.statType = 1;
			$location.path("dic/sysconfig");
			break;
		case "checkpro":
			$scope.statTypeMap.checkpro = true;
			$scope.statType = 2;
			$location.path("dic/checkpro");
			break;
		default:
			break;
		}
	};
}).controller("dicSysconfigCtrl",function($scope,$http,$msgDialog,$selectDialog){
	$scope.listDicType=[{
		isShow:false,
		listValue:[]
	}];
	$scope.newValue=false;
	$scope.currDicValue=null;
	$scope.currDicType=null;
	$scope.parentValue=null;
	$scope.newDicValue={};
	$scope.addDicValue=function(){
		$http.post("sys!addDicValue.action",{
			type:$scope.newDicValue.dic_type,
			flag:$scope.newDicValue.flag,
			parentId:$scope.newDicValue.father_dic_id,
			value:$scope.newDicValue.value,
			unit:$scope.newDicValue.unit
		}).success(function(data){
			$msgDialog.showMessage(data.name);
			if (data.code==0) {
				$scope.currDicValue=null;
				$scope.initData();
				$scope.newDicValue={};
			}
		});
	};
	$scope.innerNewValue=function(dicType,dicValue){
		if (dicType) {
			$scope.parentValue=null;
			$scope.currDicValue=null;
			$scope.newValue=true;
			$scope.newDicValue.dic_type=dicType.code;
			$scope.newDicValue.description=dicType.name;
			$scope.newDicValue.father_dic_id=0;
			if (dicType.code==1) {
				$scope.newDicValue.flag=1;
			}else {
				$scope.newDicValue.flag=3;
			}
			return;
		}
		if (dicValue) {
			$scope.parentValue=dicValue
			$scope.currDicValue=null;
			$scope.newValue=true;
			if (dicValue.dic_type==1) {
				if ($scope.currDicType.code==20) {
					$scope.newDicValue.dic_type=20;
					$scope.newDicValue.description=$scope.currDicType.desc;
					$scope.newDicValue.father_dic_id=dicValue.id;
					$scope.newDicValue.flag=1;
				}else{
					$scope.newDicValue.dic_type=2;
					$scope.newDicValue.description="部位类型";
					$scope.newDicValue.father_dic_id=dicValue.id;
					$scope.newDicValue.flag=1;
				}
			}
		}
	};
	$scope.resetDicType=function(){
		if (!$scope.listDicType)return;
		for (var int = 0; int < $scope.listDicType.length; int++) {
			var dicType=$scope.listDicType[int];
			dicType.isShow=false;
			if (dicType.code==1&&dicType.listValue&&dicType.listValue.length>0) {
				for (var int2 = 0; int2 < dicType.listValue.length; int2++) {
					var dicValue=dicType.listValue[int2];
					dicValue.isShow=false;
				}
			}
		}
	};
	$scope.initData=function(){
		$scope.selectStatType('sysConfig');//
		$scope.newValue=false;
		$scope.currDicValue=null;
		$scope.currDicType=null;
		$scope.queryAllDicType();
	};
	$scope.queryAllDicType=function(){
		$http.post("sys!querySysConfig.action").success(function(data){
			if (data.code==0) {
				$scope.listDicType=data.data.listDicType;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.valueClick=function(dicValue){
		$scope.newValue=false;
		$scope.currDicValue=null;
		if (dicValue.dic_type!=1) {
			$scope.currDicValue=dicValue;
			return;
		}
		if (dicValue.isShow) {
			dicValue.isShow=false;
			return;
		}
		if ($scope.currDicType.listValue&&$scope.currDicType.listValue.length>0) {
			for (var int = 0; int < $scope.currDicType.listValue.length; int++) {
				var dv=$scope.currDicType.listValue[int];
				dv.isShow=false;
			}
		}
		var ddata={parentId:dicValue.id};
		if ($scope.currDicType.code==20) {
			ddata.type=20;
		}
		$http.post("sys!queryDicValueListByParentId.action",ddata)
		.success(function(data){
			if (data.code==0) {
				dicValue.subListValue=data.data.listDicValue;
				dicValue.isShow=true;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.saveDicValue=function(){
		$http.post("sys!midifyDicValue.action",{
			dicValueId:$scope.currDicValue.id,
			value:$scope.currDicValue.value
		}).success(function(data){
			$msgDialog.showMessage(data.name);
		});
	};
	$scope.deleteDicValue=function(){
		$selectDialog.showMessage("你确定要删除此字段吗？",function(){
			$http.post("sys!removeDicValue.action",{dicValueId:$scope.currDicValue.id})
			.success(function(data){
				$msgDialog.showMessage(data.name);
				if (data.code==0) {
					$scope.currDicValue=null;
					$scope.initData();
				}
			});
		});
	};
	$scope.typeClick=function(dicType){
		$scope.currDicType=dicType;
		$scope.newValue=false;
		if (dicType.isShow) {
			$scope.resetDicType();
			$scope.currDicValue=null;
			return;
		}
		$scope.resetDicType();
		$scope.currDicValue=null;
		$http.post("sys!queryDicValueListByType.action",{type:dicType.code})
		.success(function(data){
			if (data.code==0) {
				if (dicType.maxNum==1) {
					if (data.data.listDicValue&&data.data.listDicValue.length>0) {
						$scope.currDicValue=data.data.listDicValue[0];
					}
				}else{
					dicType.listValue=data.data.listDicValue;
					dicType.isShow=true;
				}
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
}).controller("dicCheckproCtrl",function($scope,$http,$msgDialog,$selectDialog){
	$scope.newDicValue={
		description:"检查项目",
		value:"",
		unit:"",
		parentId:0,
		type:20,
		flag:1,
		part:null,
		reset:function(){
			this.description="检查项目";
			this.value="";
			this.unit="";
			this.father_dic_id=0;
			this.dic_type=20;
			this.flag=1;
			this.part=null;
		}
	};
	$scope.treeRoot={
		name:"检查项目配置",
		isShow:true,
		listDicValue:[],
		newValue:false,
		innerNewModel:function(){
			this.newValue=true;
		},
		outerNewModel:function(){
			this.newValue=false;
		},
		reset:function(){
			if (this.listDicValue&&this.listDicValue.length>0) {
				for (var int = 0; int < this.listDicValue.length; int++) {
					var devices=this.listDicValue[int];
					devices.isShow=false;
					if (devices.listDicValue&&devices.listDicValue.length>0) {
						for (var int2 = 0; int2 < devices.listDicValue.length; int2++) {
							var parts=devices.listDicValue[int2];
							parts.isShow=false;
						}
					}
				}
			}
			this.outerNewModel();
		}
	};
	$scope.queryOpt={
		currDevice:null,
		currPart:null
	};
	$scope.initData=function(){
		$scope.selectStatType('checkpro');
		$scope.queryCheckProListByDeviceId(null,null);
	};
	$scope.newCheckPro=function(device,part){
		$scope.treeRoot.innerNewModel();
		$scope.newDicValue.reset();
		$scope.newDicValue.parentId=part.id;
		$scope.newDicValue.part=part;
	};
	$scope.removeCheckPro=function(listCheckPro,index){
		$selectDialog.showMessage("你确定要删除此项？",function(){
			var checkPro=listCheckPro[index];
			$http.post("sys!removeDicValue.action",{dicValueId:checkPro.id})
			.success(function(data){
				if (data.code==0) {
					$msgDialog.showMessage("删除成功！");
					listCheckPro.splice(index,1);
				}else {
					$msgDialog.showMessage(data.name);
				}
			});
		});
	};
	$scope.addDicValue=function(){
		$http.post("sys!addDicValue.action",$scope.newDicValue)
		.success(function(data){
			$msgDialog.showMessage(data.name);
			if (data.code==0) {
				if (!$scope.newDicValue.part.listDicValue)
					$scope.newDicValue.part.listDicValue=[];
				$scope.newDicValue.part.listDicValue.push(data.data.dicValue);
				$scope.newDicValue.reset();
				$scope.treeRoot.outerNewModel();
			}
		});
	};
	$scope.partClick=function(deviceType,partType){
		$scope.newDicValue.reset();
		$scope.treeRoot.outerNewModel();
		if (partType.isShow){
			partType.isShow=false;
			return;
		}
		$scope.treeRoot.reset();
		partType.isShow=true;
		deviceType.isShow=true;
		$scope.queryCheckProListByDeviceId(deviceType,partType);
	};
	$scope.deviceClick=function(deviceType){
		$scope.newDicValue.reset();
		$scope.treeRoot.outerNewModel();
		if (deviceType.isShow){
			deviceType.isShow=false;
			return;
		}
		$scope.treeRoot.reset();
		deviceType.isShow=true;
		$scope.queryCheckProListByDeviceId(deviceType,null);
	};
	$scope.queryCheckProListByDeviceId=function(deviceType,partType){
		var data={};
		var souzhe=null;
		if (deviceType) {
			data.deviceTypeId=deviceType.id;
			if (partType) {
				data.partTypeId=partType.id;
				souzhe=partType;
			}else {
				souzhe=deviceType;
			}
		}else {
			souzhe=$scope.treeRoot;
		}
		$http.post("sys!queryCheckProListByDeviceId.action",data)
		.success(function(data){
			if (data.code==0) {
				souzhe.listDicValue=data.data.listDicValue;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
});