angular.module("stat", ["diy.dialog","stat.diagnosis","stat.request" ])
.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state("stat", {
		url : "/stat",
		templateUrl : "stat/stat.html",
		controller : "statCtrl"
	});
}).controller("statCtrl",function($http, $scope, $location, $msgDialog) {
	$scope.statTypeMap = {
		year : true,
		month : false,
		day : false,
		reset : function() {
			this.year = false;
			this.month = false;
			this.day = false;
		}
	};
	$scope.selectStatType = function(currPage) {
		$scope.statTypeMap.reset();
		switch (currPage) {
		case "year":
			$scope.statTypeMap.year = true;
			$scope.statType = 1;
			$scope.orgMedicalHisStatByType();
			$scope.dataSource.title.text="按年统计";
			$scope.dataSource.title.subtext="最近5年的数据";
			break;
		case "month":
			$scope.statTypeMap.month = true;
			$scope.statType = 2;
			$scope.orgMedicalHisStatByType();
			$scope.dataSource.title.text="按月统计";
			$scope.dataSource.title.subtext="最近12月的数据";
			break;
		case "day":
			$scope.statTypeMap.day = true;
			$scope.statType = 3;
			$scope.orgMedicalHisStatByType();
			$scope.dataSource.title.text="按天统计";
			$scope.dataSource.title.subtext="最近10天的数据";
			break;
		default:
			break;
		}
	};
	$scope.statType = 0;
	$scope.dataSource = {
		title:{
			text:"按年统计",
			subtext:"最近5年的数据"
		},
         tooltip: {show: true},
         legend: {
        	 data:["新增病例","新增申请","新增人员","新增诊断"]
         },
         xAxis : [{
                 type : 'category',
                 data : ["2011","2012","2013","2014","2015"]
             }],
         yAxis : [{type : 'value'}],
         series : [{
                "name":"新增病例",
                "type":"bar",
                "data":[0,0,0,0,0]
            },{
                "name":"新增申请",
                "type":"bar",
                "data":[0,0,0,0,0]
            },{
                "name":"新增人员",
                "type":"bar",
                "data":[0,0,0,0,0]
            },{
                "name":"新增诊断",
                "type":"bar",
                "data":[0,0,0,0,0]
            }]
	};
	$scope.medicalHisTotal = 0;
	$scope.initData = function() {
		$scope.orgMedicalHisStatByType();
	};
	$scope.orgMedicalHisStatByType = function() {
		$http.post("stat!orgStatByType.action", {
			type : $scope.statType
		}).success(function(data) {
			if (data.code == 0) {
				$scope.medicalHisTotal = data.data.medicalHisTotal;
				$scope.dataSource.xAxis[0].data=data.data.listStatmh.label;
				$scope.dataSource.series[0]["data"]	= data.data.listStatmh.value;
				$scope.dataSource.series[1]["data"]	= data.data.listRequest.value;
				$scope.dataSource.series[2]["data"]	= data.data.listOrgUser.value;
				$scope.dataSource.series[3]["data"]	= data.data.listDiagnosis.value;
			} else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
});