angular.module("diy.selector", [ "ui.bootstrap","services","diy.dialog"])
.service("$modals",function($uibModal){
	this.openSelectTemplateModal = function(option, callbackOk, callbackCancel) {
		var dlg = $uibModal.open({
			animation : true,
			templateUrl : '../common/modals/select_template.modal.html',
			controller : function($scope, $uibModalInstance,option,$diyhttp,$msgDialog,$queryService) {
				$scope.arrayTemplate = [];
				$scope.arrayPartType=[];
				$scope.templateType=[{
					type:1,
					name:"公共模板",
					isShow:false
				},{
					type:2,
					name:"私有模板",
					isShow:false
				}];
				$scope.initData=function(){
					
				};
				$scope.resetType=function(){
					for (var int = 0; int < $scope.templateType.length; int++) {
						var ttp=$scope.templateType[int];
						ttp.isShow=false;
					}
				};
				$scope.resetDevice=function(){
					if (! $scope.arrayDevice||$scope.arrayDevice.length<=0)
						return;
					for (var int = 0; int < $scope.arrayDevice.length; int++) {
						var ttp=$scope.arrayDevice[int];
						ttp.isShow=false;
					}
				};
				$scope.searchOpt = {
					deviceTypeId:0,
					partTypeId:0,
					type:0,
				};
				$scope.selected ={
					template:null
				};
				$scope.commonClick=function(){
					$scope.selected.template=null;
					$diyhttp.post("sys!queryCommonTemplateList.action",{},function(data){
						$scope.arrayTemplate=data.listTemplate;
					});
				};
				$scope.pptypeClick=function(ptemplate){
					if (ptemplate.isShow){
						ptemplate.isShow=false;
						return;
					}
					$scope.resetType();
					ptemplate.isShow=true;
					$queryService.queryDeviceType();
					$scope.searchOpt.type=ptemplate.type;
					$scope.arrayTemplate = [];
					$scope.selected.template=null;
				};
				$scope.deviceTypeClick=function(device){
					if (device.isShow) {
						device.isShow=false;
						return;
					}
					$scope.resetDevice();
					device.isShow=true;
					$scope.queryPartArray(device.id);
					$scope.searchOpt.deviceTypeId=device.id;
					$scope.arrayTemplate = [];
					$scope.selected.template=null;
				};
				$scope.partTypeClick=function(partType){
					$scope.searchOpt.partTypeId=partType.id;
					$scope.selected.template=null;
					$scope.searchTemplate();
				};
				$scope.ok = function(type) {
					if (! $scope.selected.template)
						return;
					$diyhttp.post("sys!useReportTemplate.action",{
						templateId:$scope.selected.template.id
					},function(data){
						if (! type)
							return;
						$uibModalInstance.close({
							template:$scope.selected.template,
							type:type
						});
					});
				};
				$scope.cancel = function() {
					$uibModalInstance.dismiss('cancel');
				};
				//获取部位类型
				$scope.queryPartArray=function(parentId){
					$scope.searchOpt.partTypeId="0";
					if (parentId=='0') {
						return;
					}
					$diyhttp.post("sys!queryPartList.action",{
						parentId:parentId
					},function(data) {
						$scope.arrayPartType = data.listDicValue;
					});
				};
				//搜索模板
				$scope.searchTemplate=function(){
					$diyhttp.post("sys!searchMyAndPublicFTemplate.action",$scope.searchOpt,function(data) {
						$scope.arrayTemplate = data.listTemplate;
					});
				};
			},
			size : "lg",
			resolve : {option : option}
		});
		dlg.result.then(function(obj) {
			if (callbackOk)callbackOk(obj);
		}, function() {
			if (callbackCancel)callbackCancel();
		});
	};
	this.openTranferRequestModal=function(modalParams,callbackOk,callbackCancel){
		$uibModal.open({
			animation : true,
	    	templateUrl: "../common/modals/tranfer.request.modal.html",
	    	size : "lg",
			resolve : {
				modalParams : modalParams
			},
			controller: function($scope,$diyhttp,$uibModalInstance,modalParams,$msgDialog,$selectDialog){
				if (!modalParams.diagnosis_id || modalParams.diagnosis_id<=0) {
					$msgDialog.showMessage("请指定有效的申请ID！");
					$uibModalInstance.dismiss('cancel');
					return;
				}
				$scope.diagnosis = null;
				$scope.listOrgRelation = [];
				$scope.selected = {
					friendOrgRelation : null,
					product:null,
					total_price:0.0
				};
				$scope.selectOrgClick=function(friendOrgRelation){
					$scope.selected.total_price = 0.0;
					$scope.selected.friendOrgRelation=friendOrgRelation;
					$scope.queryOrgProducts(friendOrgRelation);
				};
				$scope.initData=function(){
					$diyhttp.post("diagnosis!queryFDiagnosisDetailById.action",{
						diagnosisId:modalParams.diagnosis_id
					},function(data){
						$scope.diagnosis = data.diagnosis;
						$scope.queryFriendOrgCanTranfer();
					});
				};
				//查询好友机构
				$scope.queryFriendOrgCanTranfer=function(){
					$diyhttp.post("requestTranfer!queryFriendOrgCanTranferByDiagnosisId.action",{
						diagnosisId:modalParams.diagnosis_id
					},function(data) {
						$scope.listOrgRelation = data.listOrgRelation;
						if (!$scope.listOrgRelation || $scope.listOrgRelation.length<=0) {
							$msgDialog.showMessage("此申请当前没有机构可供转交！");
							$uibModalInstance.dismiss('cancel');
							return;
						}
						if ($scope.listOrgRelation && $scope.listOrgRelation.length==1) {
							$scope.selected.friendOrgRelation=$scope.listOrgRelation[0];
							$scope.queryOrgProducts($scope.selected.friendOrgRelation);
						}
					});
				};
				$scope.selectedProductClick =function (product){
					$scope.selected.product=product;
					if (product.charge_type == 10) {
						$scope.selected.total_price = $scope.img.listSeries.length*product.price;
					}else if (product.charge_type == 20) {
						var total_expose_times = 0;
						for (var int = 0; int < $scope.img.listSeries.length; int++) {
							var series = $scope.img.listSeries[int];
							total_expose_times += series.expose_times;
						}
						$scope.selected.total_price = total_expose_times*product.price;
					}else if (product.charge_type == 30) {
						$scope.selected.total_price = product.price;
					}else {
						$msgDialog.showMessage("不支持的服务计费类型，请联系系统管理员！");
						$scope.cancel();
					}
				};
				//查询机构产品列表
				$scope.queryOrgProducts=function(friendOrgRelation){
					$diyhttp.post("org!queryAllProductOfOrgIdAndDPType.action", {
						deviceTypeId:$scope.diagnosis.dicom_img_device_type_id,
						partTypeId:$scope.diagnosis.dicom_img_part_type_id,
						orgId : friendOrgRelation.relation_org_id
					},function(data) {
						$scope.arrayProduct = data.listOrgProduct;
						if ($scope.arrayProduct&&$scope.arrayProduct.length==1)
							$scope.selectedProductClick($scope.arrayProduct[0]);
					});
				};
				//提交申请
				var commitRequest = function(params,callbackYes,callbackNo) {
					if (!params) {
						if (callbackNo)callbackNo("没有参数！");
						return;
					}
					//检查重复提交诊断
					var checkRepeatCommitDiagnosis=function(params,callBack){
						$diyhttp.post("requestTranfer!checkRepeatTranferRequest.action",params,function(data){
							if (callBack)callBack(data.repeat);
						});
					};
					//执行诊断申请提交或病例保存
					var executeCommitRequest=function(params,callbackYes,callbackNo){
						var url = "requestTranfer!acceptAndTranferRequest.action";
						if (modalParams.againTranfer)
							url = "requestTranfer!againTranferRequest.action";
						$diyhttp.post(url,params,function(data) {
							$msgDialog.showMessage("操作成功");
							if (callbackYes)callbackYes();
						},function(){
							if (callbackNo)callbackNo();
						});
					};
					checkRepeatCommitDiagnosis(params,function(repeat){
						if (repeat) {
							$selectDialog.showMessage("此病例已经提交过此申请了，是否要重复提交？",function(){
								executeCommitRequest(params,callbackYes,callbackNo);
							});
						}else {
							executeCommitRequest(params,callbackYes,callbackNo);
						}
					});
				};
				
				$scope.ok = function() {
					commitRequest({
						diagnosis_id:$scope.diagnosis.id,
						diagnosis_org_id:$scope.selected.friendOrgRelation.relation_org_id,
						diagnosis_product_id:$scope.selected.product.id
					},function(){
						$uibModalInstance.close($scope.selected);
					},function(){
					});
				};
				$scope.cancel = function() {
					$uibModalInstance.dismiss('cancel');
				};
			}
	    }).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).service("$modifyPublishReportOrg",function($uibModal){
	this.open=function(params,callbackOk, callbackCancel){
		$uibModal.open({
			animation : true,
	    	templateUrl: "../common/modals/request.create.orgRelation.modal.html",
	    	controller: function($scope,$diyhttp,$uibModalInstance,params,$msgDialog){
    			$scope.rts=[];
    			$scope.canEdit = false;
    			$scope.title="修改合作关系设置";
    			$scope.opt={
    				org_relation_id:params.org_relation_id,
    				sharePatientInfo:(params.sharePatientInfo?true:false),
    				reportType:params.reportType,
    				customerType:params.customerType
    			};
    			$scope.loadReportType=function(){
    				$diyhttp.post("common!queryReportType.action",{
    				},function(result) {
    					$scope.rts=result.report_type;
    				});
    			};
    			$scope.ok=function(){
    				$diyhttp.post("org!modifyPublishReportOrg.action",{
    					relation_id:$scope.opt.org_relation_id,
    					customerType:$scope.opt.customerType,
    					sharePatientInfo:($scope.opt.sharePatientInfo?1:0)
    				},function(result) {
    					$msgDialog.showMessage("操作成功！");
    					$uibModalInstance.close(true);
    				});
    			};
    			$scope.cancel = function () {
    				$uibModalInstance.dismiss('cancel');
    			};
    			$scope.loadReportType();
	    	},
	    	size : "",
			resolve : {
				params : params
			}
	    }).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).service("$confirmOrgRelationModal",function($uibModal){
	this.open=function(params,callbackOk, callbackCancel){
		$uibModal.open({
			animation : true,
	    	templateUrl: "../common/modals/request.create.orgRelation.modal.html",
	    	controller: function($scope,$diyhttp,$uibModalInstance,params,$msgDialog){
    			$scope.rts=[];
    			$scope.canEdit = false;
    			$scope.title="进行一些必要的设置";
    			$scope.opt={
    				org_relation_id:params.org_relation_id,
    				sharePatientInfo:(params.sharePatientInfo?true:false),
    				reportType:params.reportType,
    				customerType:params.customerType
    			};
    			$scope.loadReportType=function(){
    				$diyhttp.post("common!queryReportType.action",{
    				},function(result) {
    					$scope.rts=result.report_type;
    				});
    			};
    			$scope.ok=function(){
    				$diyhttp.post("org!confirmFriend.action",{
    					relation_id:$scope.opt.org_relation_id,
    					isPass:true,
    					reportType:$scope.opt.reportType,
    					customerType:$scope.opt.customerType,
    					sharePatientInfo:($scope.opt.sharePatientInfo?1:0)
    				},function(result) {
    					$msgDialog.showMessage("操作成功！");
    					$uibModalInstance.close(true);
    				});
    			};
    			$scope.cancel = function () {
    				$uibModalInstance.dismiss('cancel');
    			};
    			$scope.loadReportType();
	    	},
	    	size : "",
			resolve : {
				params : params
			}
	    }).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).service("$requestCreateOrgRelationModal",function($uibModal){
	this.open=function(params,callbackOk, callbackCancel){
		$uibModal.open({
			animation : true,
	    	templateUrl: "../common/modals/request.create.orgRelation.modal.html",
	    	controller: function($scope,$diyhttp,$uibModalInstance,params,$msgDialog){
    			$scope.rts=[];
    			$scope.canEdit = true;
    			$scope.title="提交建立合作关系申请";
    			$scope.opt={
    				sharePatientInfo:false,
    				reportType:0,
    				customerType:0
    			};
    			$scope.loadReportType=function(){
    				$diyhttp.post("common!queryReportType.action",{
    				},function(result) {
    					$scope.rts=result.report_type;
    				});
    			};
    			$scope.ok=function(){
    				if ($scope.opt.reportType==3) {
    					if (!$scope.opt.customerType || ($scope.opt.customerType!=1 && $scope.opt.customerType!=2)) {
    						$msgDialog.showMessage("请选择有效的机构发布身份！");
    						return;
    					}
					}
    				if(params && params.id && params.id>0){
    					$diyhttp.post("org!addOrgFriend.action",{
    						orgId:params.id,
    						reportType:$scope.opt.reportType,
    						sharePatientInfo:($scope.opt.sharePatientInfo?1:0),
    						customerType:$scope.opt.customerType
    					},function(result) {
    						$msgDialog.showMessage("已发送请求，请耐心等候对方答复...");
    						$uibModalInstance.close(true);
    					});
    				}else{
    					$scope.msg="无效的机构";
    				}
    			};
    			$scope.cancel = function () {
    				$uibModalInstance.dismiss('cancel');
    			};
    			$scope.loadReportType();
	    	},
	    	size : "",
			resolve : {
				params : params
			}
	    }).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).service("$patientInfoShareSelectOrgModal",function($uibModal){
	this.open = function(params,callbackOk, callbackCancel) {
		$uibModal.open({
			animation : true,
			templateUrl : '../common/modals/patientInfoShare.select.org.modal.html',
			controller : function($scope,$diyhttp,$uibModalInstance){
				$scope.selectedOrg=null;
				$scope.listRelateOrg = [];
				$scope.searchOpt={
					spu:{}
				};
				$scope.initData=function(){
					$scope.queryRelationOrg();
				};
				$scope.queryRelationOrg=function(){
					$diyhttp.post("org!queryRelationOrgWithPatientInfoShared.action",$scope.searchOpt,function(data){
						$scope.listRelateOrg = data.listRelationOrg;
						$scope.searchOpt.spu=data.spu;
					});
				};
				$scope.selectOrgRelationClick=function(o){
					$scope.selectedOrg = o;
				};
				$scope.ok = function() {
					$uibModalInstance.close($scope.selectedOrg);
				};
				$scope.cancel = function() {
					$uibModalInstance.dismiss('cancel');
				};
			},
			size : "",
			resolve : {
				params : params
			}
		}).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).service("$reportMsgChatModal", function($uibModal) {
	this.open = function(params,callbackOk, callbackCancel) {
		$uibModal.open({
			animation : true,
			templateUrl : '../common/modals/report.msg.chat.html',
			controller : 'reportMsgChatModalCtrl',
			size : "",
			resolve : {
				params : params
			}
		}).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).controller("reportMsgChatModalCtrl",function($scope,$http,$interval,$msgDialog,$uibModalInstance,params){
	$scope.report=params.report;
	$scope.currUser=params.currUser;
	$scope.listReportMsg=[];
	$scope.pageContent={
		msgContent:""
	};
	$scope.initData=function(){
		$scope.queryReportMsg();
	};
	
	$scope.queryReportMsg=function(){
		$http.post("report!queryReportMsgByReportId.action",{reportId:$scope.report.id})
		.success(function(data){
			if (data.code==0) {
				$scope.listReportMsg=data.data.listReportMsg;
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	var timer=$interval(function(){
		$scope.queryReportMsg();
	},3000);
	$scope.sendReportMsg=function(){
		if (!$scope.pageContent.msgContent) {
			return;
		}
		$http.post("report!sendReportMsg.action",{
			reportId:$scope.report.id,
			content:$scope.pageContent.msgContent
		}).success(function(data){
			if (data.code==0) {
				var msgTmp=data.data.reportMsg;
//				msgTmp.
				$scope.listReportMsg.push(msgTmp);
				$scope.pageContent.msgContent="";
			}else {
				$msgDialog.showMessage(data.name);
			}
		});
	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	$scope.$on('$destroy', function() {
		if (angular.isDefined(timer)) {
		    $interval.cancel(timer);
		    timer = undefined;
		 }
	});
}).service("$patientSelector",function($uibModal){
	this.open = function(option, callbackOk, callbackCancel) {
		$uibModal.open({
			animation : true,
			templateUrl : '../common/modals/patient.selector.modal.html',
			controller : 'patientSelectorModalCtrl',
			size : "",
			resolve : {
				option : option
			}
		}).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).controller("patientSelectorModalCtrl",function($scope,$diyhttp,$uibModalInstance,option){
	$scope.listPatientOrgMapper=[];
	$scope.selectedPoMapper=null;
	$scope.searchOpt={
		patient_name:"",
		identity_id:"",
		gender:0,
		spu:{}
	};
	$scope.clickPoMapper=function(p){
		$scope.selectedPoMapper = p;
	};
	$scope.initData=function(){
		$scope.queryThisOrgPatientOrgMapper();
	};
	$scope.queryThisOrgPatientOrgMapper=function(){
		$diyhttp.post("patient!queryThisOrgPatientOrgMapper.action",$scope.searchOpt,function(data){
			$scope.listPatientOrgMapper = data.listPatientOrgMapper;
			$scope.searchOpt.spu=data.spu;
		});
	};
	
	$scope.ok = function() {
		$uibModalInstance.close($scope.selectedPoMapper);
	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}).service("$commitDiagnosisRequestModal", function($uibModal,$msgDialog) {
	this.open = function(option, callbackOk, callbackCancel) {
		if (!option.img||!option.img.listSeries||option.img.listSeries.length<=0) {
			$msgDialog.showMessage("指定的影像中没有序列！");
			return;
		}
		if (!option.request_class || option.request_class<=0) {
			$msgDialog.showMessage("必须指定申请类别！");
			return;
		}
		$uibModal.open({
			animation : true,
			templateUrl : '../common/modals/commit.request.modal.html',
			controller : 'commitDiagnosisRequestModalCtrl',
			size : "sl",
			resolve : {
				option : option
			}
		}).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt.org.id,opt.product.id,opt.aboutids);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).controller('commitDiagnosisRequestModalCtrl', function($scope,option,$msgDialog,$selectDialog,
		$uibModalInstance,$diyhttp,$medicalDetailDialog) {
	$scope.modelTitle = (option.request_class==10?"提交诊断申请":"提交咨询申请");
	$scope.request_class = option.request_class;
	$scope.img=option.img;
	$scope.arrayProduct=[];
	$scope.arrayOrg=[];
	$scope.currStep=1;
	$scope.selected = {
		org : null,
		product:null,
		aboutids:"",
		checkAll:false,
		allow_reporter_publish_report:false,
		total_price:0.0,
		urgent_level:false,
		charge_amount:0
	};
	$scope.selectOrgClick=function(org){
		$scope.selected.total_price = 0.0;
		$scope.selected.org=org;
		$scope.queryOrgProducts();
	};
	$scope.initData=function(){
//		$scope.queryMyFriendsOrg();
		$scope.searchOrgOfCanRequestTo();
	};
	
	$scope.searchOrgOpt={
		org_name:"",
		request_class:option.request_class,
		spu:{}
	};
	
	/**查询可以提交申请的机构列表*/
	$scope.searchOrgOfCanRequestTo = function(){
		$diyhttp.post("request!searchOrgOfCanRequestTo.action",$scope.searchOrgOpt,function(data) {
			$scope.arrayOrg = data.listOrg;
			$scope.searchOrgOpt.spu = data.spu;
			if ($scope.arrayOrg&&$scope.arrayOrg.length==1) {
				$scope.selected.org=$scope.arrayOrg[0];
				$scope.queryOrgProducts();
			}
		});
	};
//	//查询好友机构
//	$scope.queryMyFriendsOrg=function(){
//		$diyhttp.post("org!queryMyOrgValidFriends.action",searchOrgOpt,function(data) {
//			$scope.arrayOrg = data.listOrg;
//			if ($scope.arrayOrg&&$scope.arrayOrg.length==1) {
//				$scope.selected.org=$scope.arrayOrg[0];
//				$scope.queryOrgProducts();
//			}
//		});
//	};
	$scope.onChargeAmountChanged=function(charge_amount){
		if (!$scope.selected.product){
			$scope.selected.total_price = 0;
			$scope.selected.charge_amount = 0;
			return;
		}
		if (product.charge_type == 10) {
			$scope.selected.total_price = charge_amount*product.price;
		}else if (product.charge_type == 20) {
			$scope.selected.total_price = charge_amount*product.price;
		}else if (product.charge_type == 30) {
			$scope.selected.total_price = product.price;
			$scope.selected.charge_amount = 1;
		}else {
			$msgDialog.showMessage("不支持的服务计费类型，请联系系统管理员！");
			$scope.cancel();
		}
	};
	$scope.selectedProductClick =function (product){
		$scope.selected.product=product;
		if (product.charge_type == 10) {
			$scope.selected.total_price = $scope.img.body_part_ids_arr.length*product.price;
			$scope.selected.charge_amount = $scope.img.body_part_ids_arr.length;
		}else if (product.charge_type == 20) {
			$scope.selected.total_price = $scope.img.piece_amount*product.price;
			$scope.selected.charge_amount = $scope.img.piece_amount;
		}else if (product.charge_type == 30) {
			$scope.selected.total_price = product.price;
			$scope.selected.charge_amount = 1;
		}else {
			$msgDialog.showMessage("不支持的服务计费类型，请联系系统管理员！");
			$scope.cancel();
		}
	};
	//查询机构产品列表
	$scope.queryOrgProducts=function(){
		$diyhttp.post("org!queryAllProductOfOrgIdAndDPType.action", {
			deviceTypeId:$scope.img.device_type_id,
			deviceTypeName:$scope.img.device_type_name,
			partTypeId:$scope.img.part_type_id,
			partTypeName:$scope.img.part_type_name,
			orgId : $scope.selected.org.id
		},function(data) {
			$scope.arrayProduct = data.listOrgProduct;
			if ($scope.arrayProduct&&$scope.arrayProduct.length==1)
				$scope.selectedProductClick($scope.arrayProduct[0]);
		});
	};
	$scope.innerRelateMedicalHis=function(flag){
		if (!$scope.currStep)
			$scope.currStep=1;
		else {
			$scope.currStep=flag;
		}
	};
	$scope.listCaseHistory=[];
	$scope.searchOpt={
		case_his_id:option.caseHistory.id,
		patient_name:option.caseHistory.patient_name,
		patient_identity_id:'',
		patient_gender:0,
		spu:{}
	};
	$scope.checkAll=function(isCheck){
		if ($scope.listCaseHistory) {
			for (var int = 0; int < $scope.listCaseHistory.length; int++) {
				var caseHistory=$scope.listCaseHistory[int];
				caseHistory.check=$scope.selected.checkAll;
			}
		}
	};
	//提交申请
	var commitRequest = function(caseHistory,selectedDicomImg) {
		if (!caseHistory || !selectedDicomImg) {
			$msgDialog.showMessage("请选择一个图像！");
			return;
		}
		//检查重复提交诊断
		var checkRepeatCommitDiagnosis=function(caseHistory,selectedDicomImg,org_id,product_id,callBack){
			if (! org_id || !product_id)return false;
			$diyhttp.post("diagnosis!checkRepeatCommitDiagnosis.action",{
				medicalHisId:caseHistory.id,
				dicomImgId:selectedDicomImg.id,
				diagnosisOrgId:org_id,
				productId:product_id
			},function(data){
				if (callBack)callBack(data.repeat);
			});
		};
		//执行诊断申请提交或病例保存
		var executeCommitRequest=function(caseHistory,selectedDicomImg,diagnosisOrgId,diagnosisProductId,aboutCaseIds){
			$diyhttp.post("diagnosis!commitDiagnosis.action",{
				caseHistory : caseHistory,
				selectedDicomImgId : selectedDicomImg.id,
				diagnosisOrgId : diagnosisOrgId,
				diagnosisProductId : diagnosisProductId,
				aboutCaseIds : aboutCaseIds,
				allow_reporter_publish_report:$scope.selected.allow_reporter_publish_report,
				request_class : option.request_class,
				request_urgent_level:($scope.selected.urgent_level?20:10),
				charge_amount:$scope.selected.charge_amount
			},function(data) {
				$msgDialog.showMessage("操作成功");
			});
		};
//		$commitDiagnosisRequestModal.open({
//			caseHistory : caseHistory,
//			img:selectedDicomImg
//		}, function(org_id,product_id,aboutids) {
		checkRepeatCommitDiagnosis(caseHistory,selectedDicomImg,$scope.selected.org.id,$scope.selected.product.id,function(repeat){
			if (repeat) {
				$selectDialog.showMessage("此病例已经提交过此申请了，是否要重复提交？",function(){
					executeCommitRequest(caseHistory,selectedDicomImg,$scope.selected.org.id,$scope.selected.product.id,$scope.selected.aboutids);
				});
			}else {
				executeCommitRequest(caseHistory,selectedDicomImg,$scope.selected.org.id,$scope.selected.product.id,$scope.selected.aboutids);
			}
		});
//		});
	};
	
	$scope.ok = function() {
		$scope.selected.aboutids='';
		if ($scope.listCaseHistory) {
			for (var int = 0; int < $scope.listCaseHistory.length; int++) {
				var caseHistory=$scope.listCaseHistory[int];
				if (!caseHistory.check)continue;
				if ($scope.selected.aboutids=='') {
					$scope.selected.aboutids=caseHistory.id+'';
				}else {
					$scope.selected.aboutids=$scope.selected.aboutids+','+caseHistory.id;
				}
			}
		}
		commitRequest(option.caseHistory,option.img);
		$uibModalInstance.close($scope.selected);
	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	$scope.showMedicalHisDetail=function(caseHistory){
		$medicalDetailDialog.showDialog(caseHistory.id);
	};
	$scope.searchMedicalHis=function(){
		if (!$scope.searchOpt.patient_name) {
			if (!$scope.searchOpt.patient_identity_id) {
				$msgDialog.showMessage("身份证号和病人姓名至少填写一个用于查询！");
				return;
			}
		}
        $diyhttp.post("case!searchOtherSelfOrgMedicalHis.action", $scope.searchOpt,function(data) {
			$scope.listCaseHistory = data.listCaseHistory;
		});
	};
}).service("$selectTranferUserModal", function($uibModal) {
	this.open = function(option, callbackOk, callbackCancel) {
		var dlg = $uibModal.open({
			animation : true,
			templateUrl : '../common/modals/select.tranfer.users.modal.html',
			controller : function($scope, $uibModalInstance,option,$http,$msgDialog) {
				$scope.users = [];
				$scope.showWarning=false;
				$scope.unspecified=false;
				$scope.selected ={
					user:null
				};
				$scope.unspeci=function(){
					$scope.selected.user=null;
				};
				$scope.ok = function() {
					if ($scope.selected.user) {
						if ($scope.unspecified) {
							$uibModalInstance.close(0);
						}else
							$uibModalInstance.close($scope.selected.user.id);
					}else
						$uibModalInstance.close();
				};

				$scope.cancel = function() {
					$uibModalInstance.dismiss('cancel');
				};
				
				//查询所有可以移交的医生列表
				$scope.queryOrgDoctors=function(){
					var data={
						diagnosisId:option.diagnosisId
					};
					$http.post("diagnosis!queryCanTranferDoctors.action",data)
					.success(function(data) {
						if (data.code == 0) {
							$scope.users = data.data.listDoctor;
							if (!$scope.users||$scope.users.length<=0) {
								$scope.showWarning=true;
							}
						} else {
							$msgDialog.showMessage(data.name);
						}
					});
				};
			},
			size : "",
			resolve : {option : option}
		});
		dlg.result.then(function(user_id) {
			if (callbackOk)callbackOk(user_id);
		}, function() {
			if (callbackCancel)callbackCancel();
		});
	};
}).service("$electList", function($uibModal) {
	this.open = function(option,callbackOk, callbackCancel) {
		$uibModal.open({
			animation : true,
			templateUrl : 'common/elect.medical.list.html',
			controller : 'electListCtrl',
			size : "",
			resolve : {
				option : option
			}
		}).result.then(function(opt) {
			if (callbackOk)
				callbackOk(opt);
		}, function() {
			if (callbackCancel)
				callbackCancel();
		});
	};
}).controller("electListCtrl",function($scope,$msgDialog,$uibModalInstance,option){
	$scope.listElectMedical=[{
		name:"生化检验",
		code:"1001"
	},{
		name:"病理结果",
		code:"1002"
	},{
		name:"CT",
		code:"1003"
	},{
		name:"ECT",
		code:"1004"
	},{
		name:"超声",
		code:"1005"
	},{
		name:"内镜",
		code:"1006"
	},{
		name:"核磁",
		code:"1007"
	},{
		name:"影像",
		code:"1008"
	},{
		name:"入院记录",
		code:"3001"
	},{
		name:"用药记录",
		code:"3003"
	},{
		name:"出院记录",
		code:"3002"
	},{
		name:"手术记录",
		code:"3004"
	},{
		name:"同意书",
		code:"3005"
	},{
		name:"医嘱信息",
		code:"3006"
	},{
		name:"病程记录",
		code:"3007"
	}];
	
	$scope.itemClick=function(item){
		var url="http://182.140.132.172/HealthRecordCenter/PatientEMRItem.aspx"
			+"?strSendEnterpriceId=5113002014120486221009&strSystemVersion=2005&strPatientNo="+option.medicalHisNum+"&strEMRItemId="+item.code;
		var newWindow = window.open(url, "_blank");
	};
	
	$scope.initData=function(){
		
	};
	$scope.ok = function() {
		$uibModalInstance.close();
	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
});