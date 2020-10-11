
/**
 * Helper object that manages UI-related multi call handling.
 */
var multiCallContainer = function(callPrefix, parentId) {
    var calls = {};
    var activeCall = null;
    var selectedCallId = null;

    var methods = {

        /**
         * Add a call to the collection and to the list on-screen.
         *
         * @return true if the call didn't already exist, false otherwise
         */
        addCall: function(conversation, container) {
            var callIsNew = false;
            if(!(calls[conversation.callId])) {
                // no call or old call is not connected or not (Ringout || OffHook && canEndCall) and new one is something more active
                // entirely too complicated of a condition, but that's asynchronous multiple calls for you
                if(!selectedCallId || !calls[selectedCallId] || (calls[selectedCallId].callState !== "Ringout" && calls[selectedCallId].callState !== 'Connected' && (calls[selectedCallId].callState !== 'OffHook' || !calls[selectedCallId].capabilities.canEndCall) && (conversation.callState === 'Ringin' || conversation.callState === 'Ringout' || conversation.callState === 'OffHook'))) {
                    selectedCallId = conversation.callId;
                    $('ul#calllist li').removeClass('selected');
                 }
                var $calls = $('#' + parentId);
                $calls.append($('<li' +(selectedCallId === conversation.callId ? ' class="selected"' : '') + ' id="' + callPrefix + '_' + conversation.callId + '"><span class="controls"><button type="button" class="answerbtn">Answer</button> <button type="button" class="divertbtn">iDivert</button></span><span class="name"></span><span class="state"></span>'));//<b>' + conversation.participant.name + ':</b> ' + conversation.participant.recipient + '</li>'));
                //$calls[0].scrollTop = $calls[0].scrollHeight;
                $newCall = $('#'+callPrefix+'_'+conversation.callId);
                $newCall.find('.answerbtn').click(incomingAnswerClick);
                $newCall.find('.divertbtn').click(incomingDivertClick);
                $newCall.bind('conversationUpdate.cwic',handleConversationUpdate)
                        .bind('conversationEnd.cwic', handleConversationEnd);
                callIsNew = true;
                $('#switchmodebtn').attr('disabled',true);
            }
            calls[conversation.callId] = conversation;
            methods.updateCall(conversation, container);
            return callIsNew;
        },

        getCall: function(callid) {
            return calls[callid];
        },
        getCalls: function() {
            return calls;
        },
        removeCall: function(callid) {
            if(calls[callid]) {
                delete calls[callid];
                var $remove = $('#' + callPrefix + '_' + callid);
                $remove.find('.answerbtn').unbind();
                $remove.find('.divertbtn').unbind();
                $remove.unbind().remove();
            }
            selectedCallId = null;
            for(var call in calls) {
                if(calls.hasOwnProperty(call)) {
                    selectedCallId = calls[call].id;
                }
            }
            methods.setSelectedCall(selectedCallId);
            if(!selectedCallId) {
                $('#switchmodebtn').removeAttr('disabled');
                $('#callcontainer').hide();
                if(popupwindow) {
                    popupwindow.close();
                }
            }
        },

        removeAll: function() {
            for(var call in calls) {
                if(calls.hasOwnProperty(call)) {
                    methods.removeCall(calls[call].id);
                }
            }
            selectedCallId = null;
            $('#switchmodebtn').attr('disabled',true);
            $('#callcontainer').hide();
        },

        updateCall: function(conversation,container) {
            var $update = $('#' + callPrefix + '_' + conversation.callId);
            var name = '';
            var title = '';
            var state = '';
            if(container) {
                var $container = $(container);
                var classes = getCwicClasses($container);
                var oldclasses = getCwicClasses($update);
                $container.removeClass(classes);
                $update.data('cwic',$container.data('cwic')).removeClass(oldclasses).addClass(classes);
            }
            if(conversation.callState === 'Reorder') {
                name = conversation.calledPartyNumber;
                state = 'Call Failed';
            } else {
                state = conversation.callState;
                if (conversation.isConference) {
                    name = 'Conference';
                    for(var i=0;i<conversation.participants.length;i++) {
                        title += (i>0 ? ', ' : '') + conversation.participants.name;
                    }
                }
                else if (conversation.callType === "Outgoing") {
                    name = conversation.calledPartyName;
                    title = conversation.calledPartyDirectoryNumber;
                }
                else if (conversation.callType === "Incoming") {
                    name = conversation.callingPartyName;
                    title = conversation.callingPartyDirectoryNumber;
                }
            }
            if(conversation.capabilities.canImmediateDivert || conversation.capabilities.canAnswerCall) {
                $update.find('.divertbtn').attr('disabled', !conversation.capabilities.canImmediateDivert);
                $update.find('.answerbtn').attr('disabled', !conversation.capabilities.canAnswerCall);
                $update.find('.controls').show();
            } else {
                $update.find('.controls').hide();
            }
            $update.find('.name').text(name).attr('title',title);
            //console.log("new state for call list: " + state);
            $update.find('.state').text(state).attr('class', 'state ' + conversation.callState);
            if(!selectedCallId) {
                methods.setSelectedCall(conversation.callId);
            }
        },

        getSelectedCall: function() {
            return calls[selectedCallId];
        },

        getCallDiv: function(callId) {
            if(!callId) {
                callId = selectedCallId;
            }
            return $('#'+callPrefix+'_'+callId);
        },
        setSelectedCall: function(callid) {
            selectedCallId = callid;
            $('#calllist li').removeClass('selected');
            $('#'+callPrefix+'_'+callid).addClass('selected');
            updateConversationInfo(calls[selectedCallId], '#callcontainer');
        },

		isCallListEmpty:function(){
			if ($('#calllist').children().size()) {
                return false;
			}
			return true;
		},

		setCallMap:function(conversation){
			calls[conversation.callId] = conversation;
		},

		getCallMap: function(conversationId) {
            return calls[conversationId];
        },

        callListClick: function(e) {
            if((e.target.id !== "calllist")) {
                var el = $(e.target);
                while(el.length && el[0].id.indexOf(callPrefix)) {
                    el = el.parent();
                }
                if(el.length) {
                    var selectedCallid = el[0].id.replace(callPrefix+"_","");
                    methods.setSelectedCall(selectedCallid);
                    settings.log('selected call id:' + selectedCallid);
                    var call = methods.getSelectedCall();
                    settings.log('selected call:',call);
                    methods.updateCall(call);
                }
            }
        }
    };

    return methods;
};

