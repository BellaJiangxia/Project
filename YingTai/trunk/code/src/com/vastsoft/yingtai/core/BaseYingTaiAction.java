package com.vastsoft.yingtai.core;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.http.BaseAction;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

import java.util.Date;

public abstract class BaseYingTaiAction extends BaseAction {
    public static final String PASSPORT = "PASSPORT";
    private Date start, end;

    /**
     * Action过滤参数
     */
    public static String filterParam(String str) {
        if (str == null)
            return null;
        if (str.trim().isEmpty()) {
            return null;
        }
        return str.trim();
    }

    public static Integer filterParam(Integer in) {
        if (in == null)
            return null;
        if (in > 0) {
            return in;
        }
        return null;
    }

    public static Long filterParam(Long lo) {
        if (lo == null)
            return null;
        if (lo > 0) {
            return lo;
        }
        return null;
    }

    public static Double filterParam(Double dou) {
        if (dou == null)
            return null;
        if (dou > 0) {
            return dou;
        }
        return null;
    }

    public Passport takePassport() {
        Passport p = (Passport) takeBySession(PASSPORT);
        return p != null && p.getValid() ? p : null;
    }

    protected Passport isOnline() {
        Passport passport = (Passport) this.takeBySession(PASSPORT);
        return passport;
    }

    public void setStart(Date start) {
        if (start != null)
            this.start = DateTools.getStartTimeByDay(start);
        else
            this.start = start;
    }

    public void setEnd(Date end) {
        if (end != null)
            this.end = DateTools.getEndTimeByDay(end);
        else
            this.end = end;
    }

    protected Date getStart() {
        return start;
    }

    protected Date getEnd() {
        return end;
    }
}
