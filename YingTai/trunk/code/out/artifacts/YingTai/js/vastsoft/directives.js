angular.module('vastsoft.directives', [])
.run(function($rootScope,$templateCache){
	$templateCache.put("treeItem2.html",'<a href="javascript:void(0);" ng-click="itemClick(item)">'+
			'<span ng-class="item.id==selectedItem.id?\'btn btn-success btn-xs\':\'\'">'+
			'<span ng-class="item.isShow?\'glyphicon glyphicon-minus\':\'glyphicon glyphicon-plus\'"></span>'+
			'{{item.name}}<span></a><div ng-if="item.isShow">'+
			'<ul style="list-style-type: none;padding-left: 20px;">'+
			'<li ng-repeat="item in item.listChild" ng-include="\'treeItem2.html\'"></li>'+
			'</ul></div>');
	$rootScope.dateOptions = {
        formatMonth:'MM',
        formatYear:'yyyy',
        formatDay:'dd',
        startingDay : 1,
        maxDate: new Date()
    };
}).directive("vsAccordionMenu",function(){
	
}).directive("dateSelector",function($compile){
	var jsonStrToDate=function(a) {
		try {
			if (a&&(typeof a=="string"))
				return new Date(a.replace("-", "/").replace("-", "/").replace("T", " "));
			return a;
		} catch (e) {
			return a;
		}
	};
	var randomString=function(len) {
		len = len || 32;
		var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
		var maxPos = $chars.length;
		var pwd = '';
		for (i = 0; i < len; i++) {
			pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
		}
		return pwd;
	};
	return {
		replace: true,
		restrict: 'AE',
		transclude: true,
		require: ["?ngModel"],
		template : function(element, attrs) {
			var opened = 'selectDateOpened'+randomString(6);
		    var html = '<div class="input-group">';
		    html += '<input type="text" class="form-control" uib-datepicker-popup ng-model="'+attrs.ngModel+'"';
		    if (attrs.ngChange)
		    	html += ' ng-change="'+attrs.ngChange+'"';
		    if (attrs.placeholder)
		    	html += ' placeholder="'+attrs.placeholder+'" ';
		    html += ' datepicker-options="dateOptions" ng-required="true" close-text="关闭" current-text="今天" clear-text="清除"';
		    html += ' ng-readOnly="true" is-open="'+opened+'"/>';
		    html += '<span class="input-group-btn">';
		    html += '<button type="button" class="btn btn-info" ng-click="'+opened+'=!'+opened+'"';
		    if (attrs.ngDisabled)
		    	html += ' ng-disabled="'+attrs.ngDisabled+'"';
		    html += '>';
		    html += '<i class="glyphicon glyphicon-calendar"></i>';
		    html += '</button>';
		    html += '</span>';
		    html += '</div>';
		    return html;
		},
		link: function($scope,element, attrs,controllers) {
			var ngModelCtrl= controllers[0];
			ngModelCtrl.$render = function() {
				ngModelCtrl.$setViewValue(jsonStrToDate(ngModelCtrl.$viewValue));
            };
//            $scope.$eval(opened+' = false;');
//			$compile(element)($scope);
        },
	};
});