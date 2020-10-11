package com.vastsoft.util.common;

import java.util.*;

public final class TimeLimit extends SingleClassConstant {
    public static final TimeLimit TODAY = new TimeLimit(1, "今天");
    public static final TimeLimit YESTERDAY = new TimeLimit(11, "昨天");
    public static final TimeLimit THREE_DAY = new TimeLimit(2, "三天内");
    public static final TimeLimit WEEK = new TimeLimit(3, "一周内");
    public static final TimeLimit HALF_MONTH = new TimeLimit(31, "半月内");
    public static final TimeLimit MONTH = new TimeLimit(4, "一月内");
    public static final TimeLimit THREE_MONTH = new TimeLimit(5, "三月内");
    public static final TimeLimit HALF_YEAR = new TimeLimit(6, "半年内");
    public static final TimeLimit YEAR = new TimeLimit(7, "一年内");
    public static final TimeLimit THREE_YEAR = new TimeLimit(8, "三年内");
    private static Map<Integer, TimeLimit> mapTimeLimit = new LinkedHashMap<Integer, TimeLimit>();

    static {
        mapTimeLimit.put(TODAY.getCode(), TODAY);
        mapTimeLimit.put(YESTERDAY.getCode(), YESTERDAY);
        mapTimeLimit.put(THREE_DAY.getCode(), THREE_DAY);
        mapTimeLimit.put(WEEK.getCode(), WEEK);
        mapTimeLimit.put(HALF_MONTH.getCode(), HALF_MONTH);
        mapTimeLimit.put(MONTH.getCode(), MONTH);
        mapTimeLimit.put(THREE_MONTH.getCode(), THREE_MONTH);
        mapTimeLimit.put(HALF_YEAR.getCode(), HALF_YEAR);
        mapTimeLimit.put(YEAR.getCode(), YEAR);
        mapTimeLimit.put(THREE_YEAR.getCode(), THREE_YEAR);
    }

    protected TimeLimit(int iCode, String strName) {
        super(iCode, strName);
    }

    public static TimeLimit parseCode(int iCode) {
        return mapTimeLimit.get(iCode);
    }

    public static List<TimeLimit> getAll() {
        return new ArrayList<TimeLimit>(mapTimeLimit.values());
    }

    public static void main(String[] args) {
        System.out.println(DateTools.dateToString(TimeLimit.YESTERDAY.getStartTime()) + "-----" + DateTools.dateToString(TimeLimit.YESTERDAY.getEndTime()));
    }

    public Date getEndTime() {
        switch (this.getCode()) {
            case 11:
                return DateTools.getEndTimeByDay(DateTools.getAfterDay(new Date(), -1));
            default:
                return new Date();
        }
    }

    public Date getStartTime() {
        Date result = null;
        Calendar cal = Calendar.getInstance();
        switch (this.getCode()) {
            case 1:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            case 11:
                result = DateTools.getStartTimeByDay(DateTools.getAfterDay(new Date(), -1));
                break;
            case 2:
                int currdayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                if (currdayOfYear <= 3) {
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                    int maxDayOfYear = cal.getMaximum(Calendar.DAY_OF_YEAR);
                    cal.set(Calendar.DAY_OF_YEAR, maxDayOfYear - 3 + currdayOfYear);
                } else
                    cal.set(Calendar.DAY_OF_YEAR, currdayOfYear - 3);
                break;
            case 3:
                int currweekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
                if (currweekOfYear <= 1) {
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                    int maxweekOfYear = cal.getMaximum(Calendar.WEEK_OF_YEAR);
                    cal.set(Calendar.WEEK_OF_YEAR, maxweekOfYear - 1 + currweekOfYear);
                } else
                    cal.set(Calendar.WEEK_OF_YEAR, currweekOfYear - 1);
                break;
            case 31:
                cal.add(Calendar.DAY_OF_YEAR, -15);
                break;
            case 4:
                int currmonthOfYear = cal.get(Calendar.MONTH) + 1;
                if (currmonthOfYear <= 1) {
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                    int maxmonthOfYear = cal.getMaximum(Calendar.MONTH);
                    cal.set(Calendar.MONTH, maxmonthOfYear - 1 + currmonthOfYear);
                } else
                    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
                break;
            case 5:
                currmonthOfYear = cal.get(Calendar.MONTH) + 1;
                if (currmonthOfYear <= 3) {
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                    int maxmonthOfYear = cal.getMaximum(Calendar.MONTH);
                    cal.set(Calendar.MONTH, maxmonthOfYear - 3 + currmonthOfYear);
                } else
                    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 3);
                break;
            case 6:
                currmonthOfYear = cal.get(Calendar.MONTH) + 1;
                if (currmonthOfYear <= 6) {
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                    int maxmonthOfYear = cal.getMaximum(Calendar.MONTH);
                    cal.set(Calendar.MONTH, maxmonthOfYear - 6 + currmonthOfYear);
                } else
                    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 6);
                break;
            case 7:
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                break;
            case 8:
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 3);
                break;
            default:
                return null;
        }
        return result == null ? cal.getTime() : result;
    }

    @Override
    public String toString() {
        return "开始时间：" + DateTools.dateToString(getStartTime()) + " 结束时间：" + DateTools.dateToString(getEndTime());
    }
}
