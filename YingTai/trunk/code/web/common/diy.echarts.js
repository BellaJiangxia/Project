var echarts=angular.module("diy.echarts",[])
echarts.directive("myecharts",function(){
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
		            	echarts: '../js'
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
});