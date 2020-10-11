/**
 * Created by vastsoft on 2017/1/7.
 */
var wpacsWnd={
  /**
   *
   * @param serverAddr PACS影像服务地址，以“/”结束
   * @param studyUID
   */
  open:function(serverAddr,studyUID){
    var url="../wpacs/wpacs.html?wpacs="+serverAddr+"&stuuid="+studyUID;

    // var wd="width="+screen.width+",height="+screen.height;

    var w=window.open(url,"_blank","titlebar=no,menubar=no,location=no,toolbar=no,resizable=no,scrollbars=no,status=no,fullscreen=yes");
    if (w.outerWidth < screen.availWidth || w.outerHeight < screen.availHeight)
    {
      w.moveTo(0,0);
      w.resizeTo(screen.availWidth, screen.availHeight);
    }
  }
};
