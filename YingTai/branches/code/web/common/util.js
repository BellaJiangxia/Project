angular.module('yingtai.utils', [ 'ui.bootstrap' ])
//.filter('cutstr', function () {
//    return function (value, wordwise, max, tail) {
//        if (!value) return '';
//
//        max = parseInt(max, 10);
//        if (!max) return value;
//        if (value.length <= max) return value;
//
//        value = value.substr(0, max);
//        if (wordwise) {
//            var lastspace = value.lastIndexOf(' ');
//            if (lastspace != -1) {
//              //Also remove . and , so its gives a cleaner result.
//              if (value.charAt(lastspace-1) == '.' || value.charAt(lastspace-1) == ',') {
//                lastspace = lastspace - 1;
//              }
//              value = value.substr(0, lastspace);
//            }
//        }
//
//        return value + (tail || ' …');
//    };
//})
.service("$writingNote", function($uibModal) {
	this.selectOrg = function(option, callbackOk, callbackCancel) {
		var dlg = $uibModal.open({
			animation : true,
			templateUrl : '../common/select_org.html',
			controller : 'selectOrgCtrl',
			size : null,
			resolve : {
				items : function() {
					return "abd";
				}
			}
		});

		dlg.result.then(function(org_id) {
			if (callbackOk)
				callbackOk(org_id);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});

	};
})

.controller('selectOrgCtrl', function($scope, $uibModalInstance) {

	$scope.items = [ "asdfasdf", "asdf", "xxx", "yyy" ];

	$scope.selected = {
		item : $scope.items[0]
	};

	$scope.ok = function() {
		$uibModalInstance.close($scope.selected.item);
	};

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
});