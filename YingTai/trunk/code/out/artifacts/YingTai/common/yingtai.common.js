angular.module("yingtai.common", [])
.factory('reLoginInterceptor', function ($q,$window) {
    return {
        response: function (response) {
        	if(response.data){
        		if(response.data.code===999999999)
        			$window.location.href="../login#?msg=r";
        	}
            return response;
        },
        responseError: function (response) {
            return $q.reject(response);
        }
    };
}).config(function($httpProvider) {
	var defaultsResponce=$httpProvider.defaults.transformResponse;
	$httpProvider.defaults.transformResponse=[function(data,headersGetter){
		try {
			var beginchar=headersGetter("Content-Type");
			if (beginchar.indexOf("application/json")==0) {
				var jsonData=JSON.parse(data)
				if (jsonData.code!=0) {
					if (jsonData.code==22221) {
						window.location.href="../login";
					}else{
						return jsonData;//defaultsResponce[0](data,headersGetter);
						//.concat($http.defaults.transformResponse)
					}
				}else{
					return jsonData;//defaultsResponce[0](data,headersGetter);
				}
			}else{
				return defaultsResponce[0](data,headersGetter);
			}
		} catch (e) {
			return defaultsResponce[0](data,headersGetter);
		}
	}];
}).config(function($httpProvider) {
	$httpProvider.interceptors.push('reLoginInterceptor');
});