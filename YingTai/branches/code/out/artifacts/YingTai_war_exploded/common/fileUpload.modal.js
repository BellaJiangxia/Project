angular.module('diy.uploadfile', [ 'ui.bootstrap',"angularFileUpload","diy.dialog"])
.service("$uploadfile", function($uibModal) {
	this.selectFile = function(callbackOk, callbackCancel) {
		$uibModal.open({
			animation : true,
			templateUrl : '../common/uploadfile.html',
			controller : 'uploadfileCtrl',
			size : "",
			resolve : {
				items : function() {
					return "abd";
				}
			}
		}).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
})
.controller("uploadfileCtrl",function($scope,$msgDialog,$uibModalInstance,FileUploader){
	$scope.arrayFileMgr=[];
	var uploader = $scope.uploader = new FileUploader({
        url: 'common!uploadFiles.action?fileType=18',
    });
    // FILTERS
    uploader.filters.push({
        name: 'customFilter',
        fn: function(item, options) {
            return this.queue.length <= 20;
        }
    });
    // CALLBACKS
//    uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
//        console.info('onWhenAddingFileFailed', item, filter, options);
//    };
//    uploader.onAfterAddingFile = function(fileItem) {
//        console.info('onAfterAddingFile', fileItem);
//    };
    uploader.onAfterAddingAll = function(addedFileItems) {
    	uploader.uploadAll();
    };
//    uploader.onBeforeUploadItem = function(item) {
//        console.info('onBeforeUploadItem', item);
//    };
//    uploader.onProgressItem = function(fileItem, progress) {
//        console.info('onProgressItem', fileItem, progress);
//    };
//    uploader.onProgressAll = function(progress) {
//        console.info('onProgressAll', progress);
//    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        if (response.code==0) {
        	if (response.data.tfms&&response.data.tfms.length>0) {
        		for (var int = 0; int < response.data.tfms.length; int++) {
        			$scope.arrayFileMgr.push(response.data.tfms[int]);
				}
			}
		}else {
			$msgDialog.showMessage(response.name);
		}
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
    	$msgDialog.showMessage(response.name)
    };
//    uploader.onCancelItem = function(fileItem, response, status, headers) {
//        console.info('onCancelItem', fileItem, response, status, headers);
//    };
//    uploader.onCompleteItem = function(fileItem, response, status, headers) {
//        console.info('onCompleteItem', fileItem, response, status, headers);
//    };
    uploader.onCompleteAll = function() {
//        console.info('onCompleteAll');
        $scope.ok();
    };
//    console.info('uploader', uploader);
	$scope.ok = function() {
		$uibModalInstance.close($scope.arrayFileMgr);
	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
});