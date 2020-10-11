angular.module("myDirectives",[])
.directive("vsTreeMenu",function($compile){
	return {
		scope:{
			treeMenu:"=rootMenu"
		},
		replace: true,
		restrict: 'AE',
		transclude: true,
		template : '<div style="width:100%;"></div>',
//		require: ["^?uibAccordion","^?uibAccordionGroup","^?uibAccordionHeading"],
		compile : function(tElement, tAttrs, transclude){
			return {
				pre:function($scope,element, attrs,controllers){
					var rootMenu=$scope.treeMenu;
		        	if (!rootMenu || !rootMenu.subMenu || rootMenu.subMenu.length<=0)
		    			return;
		        	var buildMenuTemplate = function(currMenu){
		        		if (!currMenu || !currMenu.subMenu || currMenu.subMenu<=0)
		        			return '';
		        		var menuTemplate = '<uib-accordion close-others="true">';
		        		menuTemplate += '<uib-accordion-group panel-class="panel-primary" is-open="opened'+currMenu.id+'.isCustomHeaderOpen">';
		        		menuTemplate += '<uib-accordion-heading>'+currMenu.name;
		        		menuTemplate += ('<i class="pull-right glyphicon" '+
								'ng-class="opened'+currMenu.id+'.isCustomHeaderOpen?\'glyphicon-chevron-down\':\'glyphicon-chevron-right\'"></i>'+
								'</uib-accordion-heading>');
		            	for (var int = 0; int < currMenu.subMenu.length; int++) {
		            		var subItem = currMenu.subMenu[int];
		            		if (!subItem)continue;
		    				if (subItem.type=='menu') {
		    					menuTemplate += buildMenuTemplate(subItem);
		    				}else if(subItem.type=='item'){
		    					menuTemplate += '<button class="btn btn-primary btn-block" ui-sref="'+subItem.sref+'">'+subItem.name+'</button>';
		    				}
		    			}
		            	menuTemplate += '</uib-accordion>';
		            	return menuTemplate;
		        	}
		        	var template = buildMenuTemplate(rootMenu);
		        	element[0].innerHTML=template;
		        	$compile(element)($scope.$parent);
				},
				post: function($scope,element, attrs,controllers){
				}
			};
		},
	};
}).directive("optionPers",function($parse,$state){
	return {
		scope:{
			searchOpt:"=ngModel"
		},
		restrict: 'E',
		require: ["?ngModel"],
        link: function($scope,element, attrs,controllers) {
        	var ngModelCtrl= controllers[0];
        	var searchOpt= null;
        	var currState=$state.$current;
        	if (currState&&currState.data&&currState.data.searchOpt) {
        		searchOpt=currState.data.searchOpt;
			}
        	if (searchOpt) {
        		$scope.searchOpt=searchOpt;
        		ngModelCtrl.$setViewValue(searchOpt);
			}
        	$scope.$parent.$eval(attrs.onSearch);
            $scope.$on('$destroy', function($event) {
            	if (currState) {
					if (!currState.data)
						currState.data={};
					currState.data.searchOpt=$scope.searchOpt;
				}
            });
        },
	};
}).directive("myecharts",function(){
	return {
		scope:{
			dataSource:"=datasource"
		},
		restrict:"E",
		template:'<div></div>',
		replace:true,
		transclude:false,
		link : function(scope, element, attrs) {
			var dom=element[0];
			dom.style.height=attrs.height;
			dom.style.width=attrs.width;
			scope.$watch(attrs.datasource,function(newVal,oldValue,sscope){
				// 路径配置
		        require.config({
		            paths: {
		            	echarts: '/YingTai/js'
		            }
		        });
		        require(['echarts','echarts/chart/bar'],// 使用柱状图就加载bar模块，按需加载
				    function (ec) {
			            var myChart = ec.init(dom);
			            var option = scope.dataSource;
			            myChart.setOption(option); 
				    }
		        );
			},true);
			
        }
	};
}).directive('switch', function(){
  return {
	    restrict: 'AE'
	  , replace: true
	  , transclude: true
	  , template: function(element, attrs) {
	      var html = '';
	      html += '<span';
	      html +=   ' class="switch' + (attrs.class ? ' ' + attrs.class : '') + '"';
	      html +=   attrs.ngModel ? ' ng-click="' + attrs.disabled + ' ? ' + attrs.ngModel + ' : ' + attrs.ngModel + '=!' + attrs.ngModel + (attrs.ngChange ? '; ' + attrs.ngChange + '"' : '"') : '';
	      html +=   ' ng-class="{ checked:' + attrs.ngModel + ', disabled:' + attrs.disabled + ' }"';
	      html +=   '>';
	      html +=   '<small></small>';
	      html +=   '<input type="checkbox"';
	      html +=     attrs.id ? ' id="' + attrs.id + '"' : '';
	      html +=     attrs.name ? ' name="' + attrs.name + '"' : '';
	      html +=     attrs.ngModel ? ' ng-model="' + attrs.ngModel + '"' : '';
	      html +=     ' style="display:none" />';
	      html +=     '<span class="switch-text">'; /*adding new container for switch text*/
	      html +=     attrs.on ? '<span class="on">'+attrs.on+'</span>' : ''; /*switch text on value set by user in directive html markup*/
	      html +=     attrs.off ? '<span class="off">'+attrs.off + '</span>' : ' ';  /*switch text off value set by user in directive html markup*/
	      html += '</span>';
	      return html;
	    }
	  }
});