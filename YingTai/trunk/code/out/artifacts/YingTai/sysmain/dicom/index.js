angular.module("main",[ "ui.router","ngAnimate","ui.bootstrap","vs.services","vastsoft.directives","vastsoft.filters",
                        "myDirectives","filters","dicom"])
.constant('HTML5_VIEWER_URL',"http://localhost:8080/YingTaiDSS/wado/index.html")
.run(function($rootScope,$window,$location,$state,$dicom,$dialogs,$templateCache) {
}).controller("mainCtrl",function($scope,$state,$window,$interval,$vsHttp,HTML5_VIEWER_URL) {
	$scope.study_uid = '1.2.156.110001.1.5215.1653.20160806';
	
	$scope.initData=function(){
		
	};
	$scope.requestViewImage=function(){
		if (!$scope.study_uid)
			return;
		$vsHttp.post("legitimate!requestViewImage.action",{
			study_uid:$scope.study_uid
		},function(data){
			window.open(HTML5_VIEWER_URL+"?studyUID="+$scope.study_uid+"&token="+data.token); 
		});
	};
});