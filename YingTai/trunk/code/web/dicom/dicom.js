angular.module('dicom', ['diy.dialog'])
.service("$dicom",function($msgDialog,$http,$timeout,$window) {
	this.openDicoms = function(option){
		if (!option || isEmptyObject(option)) {
			$msgDialog.showMessage("参数不合法，参数为空，或者为空对象！");
			return;
		}
		if ((!option.dicom_img_id || option.dicom_img_id <=0) &&
			(!option.markChar || option.markChar =='' || !option.orgAffixId || option.orgAffixId<=0)) {
			$msgDialog.showMessage("参数不合法，markChar或者dicom_img_id&orgAffixId 不能为空或null！");
			return;
		}

		try {
			var thisDoOnLoad = this.DoOnLoad;
			$http.post("org!selectOrgAffixByOption.action",option).success(function(data){
				if (data.code==0) {
					var url=data.data.orgAffix.dicomweb_url;
					if (data.data.orgAffix.wasLan&&data.data.orgAffix.wasLan!="")
						url=data.data.orgAffix.intranet_url;

					if(data.data.orgAffix.view_type===10){
						try {
							if (!ViewerMgr) {
								$msgDialog.showMessage('WebWiewer 控件未安装！option:'+angular.toJson(option));
								return ;
							}
							thisDoOnLoad();

							ViewerMgr.AkamiCloseViewer(-1);
							var ins=ViewerMgr.Login(url,'DicomWeb.dll',data.data.orgAffix.user_name,data.data.orgAffix.password);
							if(ins<=0){
								$msgDialog.showMessage('WebViewer 控件登录失败,option:'+angular.toJson(option)+" url:"+url+" 用户名："+data.data.orgAffix.user_name+
									" 密码："+data.data.orgAffix.password);
								return ;
							}
							var handle=ViewerMgr.AkamiNewViewer(0,'',5,0,0,10000,10000);//
							if (handle!=0) {
								ViewerMgr.ViewerSetting('DisableDicomPrint','0');
								console.log("打开图像：option"+angular.toJson(option));
								var ra=ViewerMgr.AkamiOpenStudy(handle,data.data.img.mark_char,'','');
								if(ra=0)alert('打开失败,option'+angular.toJson(option));
							}else {
								$msgDialog.showMessage("打开图像失败！at'"+data.data.orgAffix.dicomweb_url+"',请先确保安装了控件且地址正确！option："+angular.toJson(option));
								return ;
							}
						} catch (e) {
							console.log(e);
							$msgDialog.showMessage("打开图像失败！at'"+data.data.orgAffix.dicomweb_url+"',请先确保安装了控件且地址正确！option："+angular.toJson(option));
						}
					}else if(data.data.orgAffix.view_type===20){
						// option.markChar="2.16.840.1.113669.632.20.121711.10000155501";
						url="../wpacs/wpacs.html?wpacs="+url+"&stuuid="+data.data.img.mark_char;
						var w=$window.open(url,"影泰DICOM影像浏览器","menubar=no,location=no,toolbar=no,resizable=yes,scrollbars=no,status=no,left="+screen.width+",top=0,width=99999,height=99999");
						$timeout(function(){
							//w.resizeTo(screen.width, screen.height);
						});
					}
				}else {
					$msgDialog.showMessage(data.name);
				}
			});
		} catch (e) {
			console.log(e);
			$msgDialog.showMessage("你的浏览器暂不支持查看图像！请使用IE10以上浏览器查看本网站，且暂不支持Windows10操作系统！option："+angular.toJson(option));
		}
	};
	this.openDicom = function(markChar,orgAffixId) {
		if (!markChar || markChar=="") {
			$msgDialog.showMessage("必须指定markChar！");
			return;
		}
		this.openDicoms({
			markChar:markChar,
			orgAffixId:orgAffixId
		});
	};
	this.DoOnLoad = function() {
		try {
			ViewerMgr.Setting('PostImageURL', '');
			ViewerMgr.Setting('PostImageSessionID', 'c20i2n45uoccqy55uhacljuc');
			ViewerMgr.Setting('LANGUAGE', '0');
			ViewerMgr.Setting('PARAM1', '1');
			ViewerMgr.Setting('UsePrintLayout', '1');
			ViewerMgr.Setting('EnableProxy', '0');
			ViewerMgr.Setting('ScreenResolutionX', '0');
			ViewerMgr.Setting('ScreenResolutionY', '0');
			ViewerMgr.Setting('KeepAlive', '1');
			ViewerMgr.Setting('AutoStackModality', 'CT,MR,PT');
			ViewerMgr.Setting('ApplyPRKOMode', '1');
			ViewerMgr.Setting('MagnifyMode', '4');
			ViewerMgr.Setting('GrayMonSize', '3.0');
			ViewerMgr.Setting('CutLineMode', '0');
			ViewerMgr.Setting('AutoSplitLocalizer', '1');
			ViewerMgr.Setting('ECGDefHighPassFreq', '0.5');
			ViewerMgr.Setting('ECGDefLowPassFreq', '40');
			ViewerMgr.Setting('AutoSaveUserProfile', '1');
			ViewerMgr.Setting('OST', '1');
			ViewerMgr.Setting('VirtualDesktop', '1');
			ViewerMgr.Setting('VirtualDesktopMenuItem', '0');
			ViewerMgr.Setting('SyncLoopLayoutMax', '1');
			ViewerMgr.Setting('FrameScrollBar', '1');
			ViewerMgr.Setting('CDRSize', '600');
			ViewerMgr.Setting('DVDRSize', '4300');
			ViewerMgr.Setting('ViewerOnDisk', '1');
			ViewerMgr.Setting('CheckDigitalSignature', '0');
			ViewerMgr.Setting('DragOpenImageMode', '0');
			ViewerMgr.Setting('OSTThumbSize', '1.0');
			ViewerMgr.Setting('SmartLayoutMaxCol', '10');
			ViewerMgr.Setting('SmartLayoutMaxRow', '10');
			ViewerMgr.Setting('SamePatientMode', '0');
			ViewerMgr.Setting('AllowCloneMeasurement', '1');
			ViewerMgr.Setting('ShowOSTFrameAfterLoadingImage', '0');
			ViewerMgr.Setting('DefPixelPaddingValue', '0');
			ViewerMgr.Setting('DefPixelPaddingValueCount', '0');
			ViewerMgr.Setting('ShowNextStudyAfterComplete', '0');
			ViewerMgr.Setting('SaveImageHidePatientIDName', '0');
			ViewerMgr.Setting('LargeICONThreshold', '3');
			ViewerMgr.Setting('AutoTileModality', '');
			ViewerMgr.Setting('DefTileColumn', '3');
			ViewerMgr.Setting('DefTileRow', '3');
			ViewerMgr.Setting('VideoRAMThreshold', '0');
			ViewerMgr.Setting('MammoHPApplyPR', '0');
			ViewerMgr.Setting('ApplyPR', '1');
			ViewerMgr.Setting('DICOMDiskUnCompress', '0');
			ViewerMgr.Setting('SeriesImageCountThreshold', '0');
			ViewerMgr.Setting('VerticalAlignOverlayFactor', '75');
			ViewerMgr.Setting('SyncZoomForDifferentSliceDirection', '1');
			ViewerMgr.Setting('RamDiskPath', 'm:');
			ViewerMgr.Setting('AllowSwapPixelDataToDisk', '1');
			ViewerMgr.Setting('VolumeDllVersion', '');
			ViewerMgr.Setting('MPRDllVersion', '1,1,10705,0');
			ViewerMgr.Setting('SwapToDiskThreshold', '0');
			ViewerMgr.Setting('ShowAnnROI', '0');
		} catch (e) {
			console.log("看图控件初始化失败，请使用IE10及其以上浏览器查看本网页，且暂不支持Windows10操作系统:"+e);
		}
	};
	this.closeDicom = function(){
		if (!ViewerMgr)return ;
		ViewerMgr.AkamiCloseViewer(-1);
	};
})