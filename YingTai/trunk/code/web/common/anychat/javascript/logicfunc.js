// AnyChat for Web SDK

/*******************************************************************************
 * ҵ���߼����� *
 ******************************************************************************/

var mDefaultServerAddr = "demo.anychat.cn"; // Ĭ�Ϸ�������ַ
var mDefaultServerPort = 8906; // Ĭ�Ϸ������˿ں�
var mSelfUserId = -1; // �����û�ID
var mTargetUserId = -1; // Ŀ���û�ID�������˶Է�������Ƶ��
var mRefreshVolumeTimer = -1; // ʵʱ������С��ʱ��
var mRefreshPluginTimer = -1; // ������Ƿ�װ��ɶ�ʱ��

// ��־��¼���ͣ�����־��Ϣ������ʾ��ͬ����ɫ
var LOG_TYPE_NORMAL = 0;
var LOG_TYPE_API = 1;
var LOG_TYPE_EVENT = 2;
var LOG_TYPE_ERROR = 3;

// ֪ͨ���ͣ���������Ϣ������ʾ��ͬ����ɫ
var NOTIFY_TYPE_NORMAL = 0;
var NOTIFY_TYPE_SYSTEM = 1;

function getParameter(param) {
	var query = window.location.search;// ��ȡURL��ַ�У���������ַ�
	var iLen = param.length;// ��ȡ��Ĳ�����Ƴ���
	var iStart = query.indexOf(param);// ��ȡ��ò�����Ƶ���ʵ����
	if (iStart == -1)// -1Ϊû�иò���
		return "";
	iStart += iLen + 1;
	var iEnd = query.indexOf("&", iStart);// ��ȡ�ڶ����������ʵ����
	if (iEnd == -1)// ֻ��һ������
		return query.substring(iStart);// ��ȡ��������Ĳ���ֵ
	return query.substring(iStart, iEnd);// ��ȡ�ڶ��������ֵ
}

function LogicInit() {
	setTimeout(function() {
		if (navigator.plugins && navigator.plugins.length) {
			window.navigator.plugins.refresh(false);
		}

		var user = getParameter("user");
		var room = getParameter("room");

		// ����Ƿ�װ�˲��
		var NEED_ANYCHAT_APILEVEL = "0"; // ����ҵ�����Ҫ��AnyChat API Level
		var errorcode = BRAC_InitSDK(NEED_ANYCHAT_APILEVEL); // ��ʼ�����
		AddLog("BRAC_InitSDK(" + NEED_ANYCHAT_APILEVEL + ")=" + errorcode, LOG_TYPE_API);
		if (errorcode == GV_ERR_SUCCESS) {
			if (mRefreshPluginTimer != -1)
				clearInterval(mRefreshPluginTimer); // �������װ��ⶨʱ��
			ShowLoginDiv(true);
			AddLog("AnyChat Plugin Version:" + BRAC_GetVersion(0), LOG_TYPE_NORMAL);
			AddLog("AnyChat SDK Version:" + BRAC_GetVersion(1), LOG_TYPE_NORMAL);
			AddLog("Build Time:" + BRAC_GetSDKOptionString(BRAC_SO_CORESDK_BUILDTIME), LOG_TYPE_NORMAL);

			GetID("prompt_div").style.display = "none"; // ���ز����װ��ʾ����
			// ��ʼ������Ԫ��
			InitInterfaceUI();

			DisplayLoadingDiv(true);
			login("demo.anychat.cn", 8906, user, "", /*parseInt(room)*/44434);
		} else { // û�а�װ��������ǲ���汾̫�ɣ���ʾ������ؽ���
			GetID("prompt_div").style.display = "block";
			SetDivTop("prompt_div", 300);
			if (errorcode == GV_ERR_PLUGINNOINSTALL)
				GetID("prompt_div_line1").innerHTML = "�״ν�����Ҫ��װ������������ذ�ť���а�װ��";
			else if (errorcode == GV_ERR_PLUGINOLDVERSION)
				GetID("prompt_div_line1").innerHTML = "��⵽��ǰ����İ汾��ͣ������ذ�װ���°汾��";

			if (mRefreshPluginTimer == -1) {
				mRefreshPluginTimer = setInterval(function() {
					LogicInit();
				}, 1000);
			}
		}
	}, 500);
}

function LogicCleanup() {
	leaveRoom();
	logout();
}

// ����AnyChat������Ҫ���յ���¼�ɹ��ص�֮�����
function ConfigAnyChatParameter() {

}

function login(srvaddr, port, username, password, room) {

	var errorcode = BRAC_Connect(srvaddr, port); // ���ӷ�����
	AddLog("BRAC_Connect(" + srvaddr + "," + port + ")=" + errorcode, LOG_TYPE_API);

	setTimeout(function() {
		errorcode = BRAC_Login(username, password, 0);
		AddLog("BRAC_Login(" + username + ")=" + errorcode, LOG_TYPE_API);

		setTimeout(function() {
			enterRoom(room);
		}, 1000);
	}, 1000);

	// �������ý���
	// GetID("setting_div").style.display = "none";
}

function logout() {
	var errorcode = BRAC_Logout();
	AddLog("BRAC_Logout()=" + errorcode, LOG_TYPE_API);
}

function enterRoom(roomId) {
	EnterRoomRequest(roomId);
}

function leaveRoom() {
	var errorcode = BRAC_LeaveRoom(-1);
	AddLog("BRAC_LeaveRoom(" + -1 + ")=" + errorcode, LOG_TYPE_API);
}

// ��ʼ������Ԫ��
function InitInterfaceUI() {
	// ���ð�ť
	// GetID("setting").onclick = function() {
	// if (GetID("setting_div").style.display == "block")
	// GetID("setting_div").style.display = "none";
	// else
	// GetID("setting_div").style.display = "block";
	// }

	// ��¼��ť
	// GetID("loginbtn").onclick = function() {
	// if (GetID("password").value == "�����Ϊ��")
	// GetID("password").value = "";
	// if (GetID("username").value != "") {
	// DisplayLoadingDiv(true);
	//
	// login(GetID("ServerAddr").value, parseInt(GetID("ServerPort").value),
	// GetID("username").value, GetID("password").value);
	//
	// // �������ý���
	// GetID("setting_div").style.display = "none";
	// } else {
	// GetID("a_error_user").style.color = "red";
	// AddLog("The user name can not be empty!", LOG_TYPE_ERROR);
	// GetID("username").focus();
	// }
	// }

	// �˳�ϵͳ
	// GetID("ExitSystemBtn").onclick = function() {
	// logout();
	//
	// ShowHallDiv(false);
	// ShowLoginDiv(true);
	// }

	// �˳�����
	// GetID("leaveroom").onclick = function() {
	// var errorcode = BRAC_LeaveRoom(-1);
	// AddLog("BRAC_LeaveRoom(" + -1 + ")=" + errorcode, LOG_TYPE_API);
	// if (mRefreshVolumeTimer != -1)
	// clearInterval(mRefreshVolumeTimer); // ���ʵʱ������ʾ��ʱ��
	// ShowRoomDiv(false); // ���ط������
	// ShowHallDiv(true); // ��ʾ�������
	// mTargetUserId = -1;
	// }

	// �����Զ��巿��
	// GetID("EnterRoomBtn").onclick = function() {
	// if (GetID("customroomid").value != "") {
	// var re = /^[1-9]+[0-9]*]*$/; // �ж��Ƿ�����
	// if (re.test(GetID("customroomid").value)) {// ������
	// EnterRoomRequest(parseInt(GetID("customroomid").value));
	// } else {
	// AddLog("Room ID must be number!", LOG_TYPE_ERROR);
	// GetID("customroomid").value = "";
	// GetID("customroomid").focus();
	// }
	// }
	// }

	// ������Ϣ��ť
	// GetID("SendMsg").onclick = function() {
	// SendMessage();
	// }

	// �س�������Ϣ
	// GetID("MessageInput").onkeydown = function(e) {
	// e = e ? e : window.event; // �����¼�
	// if (e.keyCode == 13 && GetID("MessageInput").value != "") {//
	// �س������ҷ�����Ϣ��Ϊ��
	// SendMessage();
	// }
	// }

	// ���ز����ť��껮�뻮��ʱ��
	GetID("prompt_div_btn_load").onmouseover = function() {
		GetID("prompt_div_btn_load").style.backgroundColor = "#ffc200";
	}
	GetID("prompt_div_btn_load").onmouseout = function() {
		GetID("prompt_div_btn_load").style.backgroundColor = "#ff8100";
	}
	// ���ز������رհ�ť
	GetID("prompt_div_headline2").onclick = function() {
		document.URL = location.href;
	}
	// ����Ƶ���־������
	GetID("LOG_DIV_BODY").onmousemove = function() {
		GetID("LOG_DIV_BODY").style.zIndex = 100;
		GetID("LOG_DIV_CONTENT").style.backgroundColor = "#FAFADD";
		GetID("LOG_DIV_CONTENT").style.border = "1px solid black";
	}
	// ������־�������ƿ�
	GetID("LOG_DIV_BODY").onmouseout = function() {
		GetID("LOG_DIV_BODY").style.zIndex = -1;
		GetID("LOG_DIV_CONTENT").style.backgroundColor = "#C4CEDD";
		GetID("LOG_DIV_CONTENT").style.border = "";
	}
	// �߼����ý���رհ�ť
	GetID("advanceset_div_close").onclick = function() {
		GetID("advanceset_div").style.display = "none";
	}
	// �߼�����
	GetID("advancedsetting").onclick = function() {
		if (GetID("advanceset_div").style.display == "block")
			GetID("advanceset_div").style.display = "none";
		else {
			GetID("advanceset_div").style.display = "block"; // ��ʾ�߼����ý���
			// ��ʼ���߼����ý���
			InitAdvanced();
		}
	}
}

function PasswordFocus(obj, color) {
	// �ж��ı����е������Ƿ���Ĭ������
	if (obj.value == "�����Ϊ��")
		obj.value = "";
	obj.type = "password";
	// �����ı����ȡ����ʱ�򱳾���ɫ�任
	obj.style.backgroundColor = color;
}
// ������뿪ʱ��ı��ı��򱳾���ɫ
function myblur(obj, color) {
	obj.style.background = color;
}

// ����߶Ȳ����ý���λ��
function SetDivTop(id, TheHeight) {
	var BodyHeight = document.documentElement.clientHeight; // ���������ɼ�����߶�
	if (TheHeight < BodyHeight) {// div�߶�С�ڿɼ�����߶�
		// GetID("margintop").style.height = (BodyHeight - TheHeight) / 4 +
		// "px";
		GetID(id).style.marginTop = "0px";
	}
}

// ϵͳ��Ϣ�����������
function DisplayScroll(id) {
	var offset = GetID(id); // ��Ҫ����div
	if (offset.offsetHeight < offset.scrollHeight) {// div�ɼ�߶�С��div�������߶�
		GetID(id).style.overflowY = "scroll";// ��ʾ������
		GetID(id).scrollTop = GetID(id).scrollHeight;// �������Զ��������ײ�
	} else
		GetID(id).style.overflowY = "hidden";// ���ع�����
}
// ������Ϣ
function SendMessage() {
	if (GetID("MessageInput").value != "") {// ������Ϣ��Ϊ��
		var Msg = GetID("MessageInput").value;
		BRAC_SendTextMessage(0, 0, Msg); // ���÷�����Ϣ����
		DisplayTextMessage(mSelfUserId, Msg);
		GetID("MessageInput").value = "";
		GetID("MessageInput").focus();
	}
}

// ��ʾ������Ϣ
function DisplayTextMessage(fromuserid, message) {
	var namestr = BRAC_GetUserName(fromuserid) + "&nbsp" + GetTheTime();
	if (fromuserid == mSelfUserId)
		namestr = namestr.fontcolor("#008000");
	else
		namestr = namestr.fontcolor("#000080");
	message = message.fontcolor("#333333");

	var msgdiv = document.createElement("div");
	msgdiv.setAttribute("class", "TheMsgStyle");
	msgdiv.innerHTML = namestr + "��&nbsp&nbsp" + message;
	GetID("ReceiveMsgDiv").appendChild(msgdiv);
	DisplayScroll("ReceiveMsgDiv");
}

// ��������Ϣ������ʾ֪ͨ��Ϣ
function ShowNotifyMessage(message, type) {
	// if (type == NOTIFY_TYPE_SYSTEM) {
	// message = message.fontcolor("#FF0000");
	// } else {
	// message = message.fontcolor("#333333");
	// }
	// var msgdiv = document.createElement("div");
	// msgdiv.setAttribute("class", "TheMsgStyle");
	// msgdiv.innerHTML = message + "&nbsp(" + GetTheTime().fontcolor("#999999")
	// + ")";
	// GetID("ReceiveMsgDiv").appendChild(msgdiv);
	// DisplayScroll("ReceiveMsgDiv");
}

// ��ʾ��¼����
function ShowLoginDiv(bShow) {
	if (bShow) {
		// GetID("login_div").style.display = "block"; // ��ʾ��¼����
		// GetID("username").focus();
		// SetDivTop("login_div", 195); // ��¼���洹ֱ����
		// GetID("LOG_DIV_BODY").style.display = "block"; // ��ʾϵͳ��Ϣ��
		// GetID("ServerAddr").value = mDefaultServerAddr;
		// GetID("ServerPort").value = mDefaultServerPort;
	} else {

	}
}

// ��ʾ�������
function ShowHallDiv(bShow) {
	if (bShow) {
		GetID("room_div_userlist").innerHTML = ""; // ��շ����������б�
		GetID("login_div").style.display = "none"; // ���ص�¼����
		GetID("hall_div").style.display = "block"; // ��ʾ�������
		// GetID("customroomid").value = "";
		SetDivTop("hall_div", 400); // ������洹ֱ����
		// GetID("customroomid").focus();
		// GetID("a_error_user").style.color = "#FAFADD";

		// GetID("hall_div_td_name").innerHTML = BRAC_GetUserName(mSelfUserId);
		// GetID("hall_div_td_id").innerHTML = mSelfUserId;
		// GetID("hall_div_td_level").innerHTML =
		// BRAC_GetUserLevel(mSelfUserId);
		// GetID("hall_div_td_ip").innerHTML =
		// BRAC_QueryUserStateString(mSelfUserId, BRAC_USERSTATE_LOCALIP);
	} else {
		GetID("hall_div").style.display = "none";
	}
}

// ��ʾ�������
function ShowRoomDiv(bShow) {
	if (bShow) {
		GetID("hall_div").style.display = "none"; // ���ش������
		GetID("room_div").style.display = "block"; // ��ʾ�������
		SetDivTop("room_div", 610); // ������洹ֱ����
		// GetID("MessageInput").focus();
	} else {
		GetID("advanceset_div").style.display = "none"; // ���ظ߼����ý���
		// GetID("ReceiveMsgDiv").innerHTML = ""; // ��շ��������Ϣ���տ�
		GetID("room_div").style.display = "none"; // ���ط������
	}
}

// �������ָ���ķ���
function EnterRoomRequest(roomid) {
	var errorcode = BRAC_EnterRoom(roomid, "", 0); // ���뷿��
	AddLog("BRAC_EnterRoom(" + roomid + ")=" + errorcode, LOG_TYPE_API);
	if (errorcode == 0)
		DisplayLoadingDiv(true);
}

function GetID(id) {
	if (document.getElementById) {
		return document.getElementById(id);
	} else if (window[id]) {
		return window[id];
	}
	return null;
}
// ��ָ���û�������Ƶ
function RequestOtherUserVideo(userid) {
	var userlist = GetID("room_div_userlist");
	// ����û��б�������<a>��ǩ
	var userdivobj = userlist.getElementsByTagName("div");
	for (var i = 0; i < userdivobj.length; i++) {
		userdivobj[i].style.backgroundColor = "White";
	}
	// ��ȡ�û��б�������<img>��ǩ
	var userimgobj = userlist.getElementsByTagName("img");
	for (var j = 0; j < userimgobj.length; j++) {
		if (userimgobj[j].getAttribute("class") == "MicrophoneTag") { // ��ͼƬΪ
			// ��Ͳ ͼƬ
			userimgobj[j].src = "./images/advanceset/microphone_false.png";
			userimgobj[j].onclick = ""; // ɾ��Ͳ��ť����¼�
			userimgobj[j].style.cursor = "";
		}
	}
	// �ж��Ƿ���Ҫ�ر�֮ǰ��������û�����Ƶ���
	if (mTargetUserId != -1) {
		reVideoDivSize();
		BRAC_UserCameraControl(mTargetUserId, 0);
		BRAC_UserSpeakControl(mTargetUserId, 0);
	}
	GetID(userid + "_MicrophoneTag").src = "./images/advanceset/microphone_true.png"; // ������ͲͼƬ
	GetID(userid + "_UserDiv").style.backgroundColor = "#E6E6E6"; // ���ñ����<a>Ԫ�ص�������ɫ

	mTargetUserId = userid; // ���ñ����û�IDΪȫ�ֱ���
	BRAC_UserCameraControl(userid, 1); // ����Է���Ƶ
	BRAC_UserSpeakControl(userid, 1); // ����Է�����
	// ����Զ����Ƶ��ʾλ��
	BRAC_SetVideoPos(userid, GetID("AnyChatRemoteVideoDiv"), "ANYCHAT_VIDEO_REMOTE");
	MicrophoneOnclick(userid); // Ϊ��ǰ��Ƶ�Ự�û���Ͳ��ť��ӵ���¼�
}

// ���б��е��û�������ӡ�ɾ�����
function RoomUserListControl(userid, bInsert) {
	var userlist = GetID("room_div_userlist");
	if (bInsert) {
		var itemdiv = document.createElement("div");
		itemdiv.setAttribute("class", "UserListStyle");
		itemdiv.id = userid + "_UserDiv";

		// �ж��û�����ͷ״̬
		if (BRAC_GetCameraState(userid) == 0)
			AddImage(itemdiv, userid + "_CameraTag", "CameraTag", "", userid); // �������ͷͼƬ<img>��ǩ
		if (BRAC_GetCameraState(userid) == 1)
			AddImage(itemdiv, userid + "_CameraTag", "CameraTag", "./images/advanceset/camera_false.png", userid); // �������ͷͼƬ<img>��ǩ
		if (BRAC_GetCameraState(userid) == 2)
			AddImage(itemdiv, userid + "_CameraTag", "CameraTag", "./images/advanceset/camera_true.png", userid); // �������ͷͼƬ<img>��ǩ
		// �жϵ�ǰID�Ƿ�Ϊ�Լ�
		if (userid == mSelfUserId) {
			AddImage(itemdiv, mSelfUserId + "_MicrophoneTag", "mSelfMicrophoneTag", "./images/advanceset/microphone_true.png", userid); // ��ӻ�ͲͼƬ<img>��ǩ
			itemdiv.innerHTML += "&nbsp" + BRAC_GetUserName(mSelfUserId) + "(�Լ�)";
		} else {
			AddImage(itemdiv, userid + "_MicrophoneTag", "MicrophoneTag", "./images/advanceset/microphone_false.png", userid); // ��ӻ�ͲͼƬ<img>��ǩ
			// ����û�����<a>��ǩ
			var a = document.createElement("a");
			a.id = userid + "_UserTag";
			a.title = BRAC_GetUserName(userid);
			a.innerHTML = BRAC_GetUserName(userid);
			a.href = "javascript:RequestOtherUserVideo(" + userid + ")";
			itemdiv.appendChild(a);
		}
		GetID("room_div_userlist").appendChild(itemdiv);
		MicrophoneOnclick(mSelfUserId);
	} else {
		var my = GetID(userid + "_UserDiv");
		userlist.removeChild(my);
	}
	DisplayScroll("room_div_userlist");
}
// div��ť��껮�뻮��Ч��
function Mouseover(id) {
	GetID(id).style.backgroundColor = "#FFFFCC";
}
// div��ť��껮�뻮��Ч��
function Mouseout(id) {
	GetID(id).style.backgroundColor = "#E6E6E6";
}
// ��ȡ��ǰʱ�� (00:00:00)
function GetTheTime() {
	var TheTime = new Date();
	return TheTime.toLocaleTimeString();
}

// �����־����ʾ����ݲ�ͬ��������ʾ��ͬ����ɫ
function AddLog(message, type) {
	if (type == LOG_TYPE_API) { // API������־����ɫ
		message = message.fontcolor("Green");
	} else if (type == LOG_TYPE_EVENT) { // �ص��¼���־����ɫ
		message = message.fontcolor("#CC6600");
	} else if (type == LOG_TYPE_ERROR) { // ������־����ɫ
		message = message.fontcolor("#FF0000");
	} else { // ��ͨ��־����ɫ
		message = message.fontcolor("#333333");
	}
	// GetID("LOG_DIV_CONTENT").innerHTML += message + "&nbsp" +
	// GetTheTime().fontcolor("#333333") + "<br />";
	// DisplayScroll("LOG_DIV_CONTENT");
}

// ��ʾ�ȴ���������ʾ�û��������ڽ�����
function DisplayLoadingDiv(bShow) {
	if (bShow) {
		GetID("LOADING_DIV").style.display = "block";
		GetID("LOADING_GREY_DIV").style.display = "block";
		var TheHeight = document.documentElement.clientHeight;
		var TheWidth = document.body.offsetWidth;
		GetID("LOADING_DIV").style.marginTop = (TheHeight - 50) / 2 + "px";
		GetID("LOADING_DIV").style.marginLeft = (TheWidth - 130) / 2 + "px";
	} else {
		GetID("LOADING_DIV").style.display = "none";
		GetID("LOADING_GREY_DIV").style.display = "none";
	}
}

// ���� ����ͷ ��Ͳ ͼ��
function AddImage(parent_id, img_id, img_class, fir_img, userid) {
	var imgs = document.createElement("img");
	imgs.id = img_id;
	imgs.className = img_class;
	imgs.src = fir_img;
	imgs.style.width = "15px";
	imgs.style.height = "15px";
	parent_id.appendChild(imgs);
}
// Ϊ������û���Ͳ��ť��ӵ���¼�
function MicrophoneOnclick(userid) {
	GetID(userid + "_MicrophoneTag").style.cursor = "pointer"; // �����״
	GetID(userid + "_MicrophoneTag").onclick = function() { // ��Ͳ����¼�
		var ImgPath = GetID(userid + "_MicrophoneTag").src.split('/');
		if (ImgPath[ImgPath.length - 1] == "microphone_true.png") {
			GetID(userid + "_MicrophoneTag").src = "./images/advanceset/microphone_false.png";
			BRAC_UserSpeakControl(userid, 0); // �ر�����
		} else {
			GetID(userid + "_MicrophoneTag").src = "./images/advanceset/microphone_true.png";
			BRAC_UserSpeakControl(userid, 1); // ��������
		}
	}
}
// �ָ���ʾ��Ƶdiv��С
function reVideoDivSize() {
	var divWidth = GetID("AnyChatRemoteVideoDiv").offsetWidth;
	var divHeight = GetID("AnyChatRemoteVideoDiv").offsetHeight;
	if (divWidth < divHeight) {
		GetID("AnyChatRemoteVideoDiv").style.width = (4.0 / 3 * divHeight) + "px";
		GetID("AnyChatRemoteVideoDiv").style.height = divHeight + "px";
	}
}