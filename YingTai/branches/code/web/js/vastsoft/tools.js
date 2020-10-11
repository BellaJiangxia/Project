function Json2date(a) {
	return new Date(a.replace("-", "/").replace("-", "/").replace("T", " "))
}
function isEmptyObject(e) {  
    var t;  
    for (t in e){
    	if (t)
    		return false;
    }
    return true; 
}
Date.prototype.toISOString=function(){
	return this.format('yyyy-MM-ddThh:mm:ss');
};
Date.prototype.formatUTC = function(b) {
	var c = {
		"M+" : this.getUTCMonth() + 1,
		"d+" : this.getUTCDate(),
		"h+" : this.getUTCHours(),
		"m+" : this.getUTCMinutes(),
		"s+" : this.getUTCSeconds(),
		"q+" : Math.floor((this.getUTCMonth() + 3) / 3),
		"S" : this.getUTCMilliseconds()
	};
	if (/(y+)/.test(b)) {
		b = b.replace(RegExp.$1, (this.getUTCFullYear() + "").substr(4 - RegExp.$1.length))
	}
	for ( var a in c) {
		if (new RegExp("(" + a + ")").test(b)) {
			b = b.replace(RegExp.$1, RegExp.$1.length == 1 ? c[a] : ("00" + c[a]).substr(("" + c[a]).length))
		}
	}
	return b
};
Date.prototype.format = function(b) {
	var c = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(b)) {
		b = b.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length))
	}
	for ( var a in c) {
		if (new RegExp("(" + a + ")").test(b)) {
			b = b.replace(RegExp.$1, RegExp.$1.length == 1 ? c[a] : ("00" + c[a]).substr(("" + c[a]).length))
		}
	}
	return b
};

function gridPagination(data,pageNum,pageSize){
	var arr=[];
	if($.isArray(data)){
		var start=(pageNum-1)*parseInt(pageSize);
		var end=start+parseInt(pageSize);
		arr=$.extend(true,[],data.slice(start,end));
	}
	return arr;
}
String.prototype.startWith=function(str){    
  var reg=new RegExp("^"+str);    
  return reg.test(this);       
} 

String.prototype.endWith=function(str){    
  var reg=new RegExp(str+"$");    
  return reg.test(this);       
}
