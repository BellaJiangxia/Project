package com.vastsoft.util.db;

import com.vastsoft.util.base.BaseEnum;
import com.vastsoft.util.common.*;
import com.vastsoft.util.log.LoggerUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public abstract class AbstractSearchParam<E> {
    private SplitPageUtil spu;
    private String order_by;
    private Date start;
    private Date end;

    public static AbstractSearchParam getCheapSearchParam() {
        return new AbstractSearchParam() {
        };
    }

    public static AbstractSearchParam getCheapSearchParam(SplitPageUtil spu) {
        AbstractSearchParam result = getCheapSearchParam();
        result.setSpu(spu);
        return result;
    }

    public static AbstractSearchParam getCheapSearchParam(TimeLimit timelimit) {
        AbstractSearchParam result = getCheapSearchParam();
        result.setTimeLimit(timelimit);
        return result;
    }

    public static AbstractSearchParam getCheapSearchParam(SplitPageUtil spu, TimeLimit timelimit) {
        AbstractSearchParam result = getCheapSearchParam();
        result.setTimeLimit(timelimit);
        result.setSpu(spu);
        return result;
    }

    public static AbstractSearchParam getCheapSearchParam(Date start, Date end) {
        AbstractSearchParam result = getCheapSearchParam();
        result.setStartAndEnd(start, end);
        return result;
    }

    public static AbstractSearchParam getCheapSearchParam(SplitPageUtil spu, Date start, Date end) {
        AbstractSearchParam result = getCheapSearchParam();
        result.setStartAndEnd(start, end);
        result.setSpu(spu);
        return result;
    }


//    public static void main(String[] args) throws IllegalAccessException {
//        AbstractSearchParam asp = AbstractSearchParam.getCheapSearchParam();
//        asp.setTimeLimit(TimeLimit.YESTERDAY);
//        System.out.println(asp.buildMap());
//    }

    /**
     * Action过滤参数
     */
    public static String filterParam(String str) {
        if (str == null)
            return null;
        if (str.trim().isEmpty()) {
            return null;
        }
        return str;
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

    public Map<String, Object> buildMap() throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> result = this.buildSubObjMap(this.getClass(), this);
        if (order_by != null && !order_by.trim().isEmpty())
            result.put("order_by", order_by);
        if (start != null && end != null) {
            result.put("start", DateTools.dateToString(start));
            result.put("end", DateTools.dateToString(end));
        }
        return result;
    }

    private Map<String, Object> buildSubObjMap(Class cla, Object obj)
            throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> result = new HashMap<String, Object>();
        this.objectToMap(cla, obj, result);
        Class<?> subClass = cla.getSuperclass();
        if (subClass.equals(AbstractSearchParam.class))
            return result;
        result.putAll(buildSubObjMap(subClass, obj));
        return result;
    }

    private void objectToMap(Class<?> cla, Object obj, Map<String, Object> mapArg)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] flds = cla.getDeclaredFields();
        ff:
        for (Field field : flds) {
            field.setAccessible(true);
            Object result = field.get(obj);
            if (result == null)
                continue;
            if (NumberTools.isFloat(result)) {
                mapArg.put(field.getName(), result);
                continue;
            } else if (result instanceof Collection) {
                throw new RuntimeException("不支持的属性类型，请使用数组！");
            } else if (ReflectTools.isChildClassOf(BaseEnum.class, result.getClass())) {
                result = ((BaseEnum) result).getCode();
            } else if (result.getClass().equals(Boolean.class) || result.getClass().equals(boolean.class)) {
                result = result.equals(Boolean.TRUE) ? 1 : 0;
            } else if (result instanceof SingleClassConstant) {
                result = ((SingleClassConstant) result).getCode();
            } else if (result instanceof Date) {
                result = DateTools.dateToString((Date) result);
            } else if (result.getClass().isArray()) {
                if (Array.getLength(result) <= 0)
                    continue;
                Class<?> ccctmp = result.getClass().getComponentType();
                if (ReflectTools.isChildClassOf(SingleClassConstant.class, ccctmp)) {
                    int length = Array.getLength(result);
                    SplitStringBuilder<Integer> sbb = new SplitStringBuilder<Integer>();
                    fa:
                    for (int i = 0; i < length; i++) {
                        SingleClassConstant scc = (SingleClassConstant) Array.get(result, i);
                        if (scc == null)
                            continue fa;
                        sbb.add(scc.getCode());
                    }
                    result = sbb.toString();
                } else if (ReflectTools.isChildClassOf(BaseEnum.class, ccctmp)) {
                    int length = Array.getLength(result);
                    SplitStringBuilder<Integer> sbb = new SplitStringBuilder<Integer>();
                    fb:
                    for (int i = 0; i < length; i++) {
                        BaseEnum scc = (BaseEnum) Array.get(result, i);
                        if (scc == null)
                            continue fb;
                        sbb.add(scc.getCode());
                    }
                    result = sbb.toString();
                } else if (ccctmp.isPrimitive() && ccctmp != char.class) {
                    result = StringTools.arrayToString(result, ',');
                    if (result == null)
                        continue ff;
                } else if (Number.class.isAssignableFrom(ccctmp)) {
                    result = StringTools.arrayToString(result, ',');
                    if (result == null)
                        continue ff;
                } else {
                    int length = Array.getLength(result);
                    SplitStringBuilder<String> sbb = new SplitStringBuilder<String>("','");
                    for (int i = 0; i < length; i++) {
                        Object ele = Array.get(result, i);
                        if (ele == null)
                            continue;
                        String strtmp = ele.toString();
                        if (StringTools.isEmpty(strtmp))
                            continue;
                        sbb.add(strtmp.trim());
                    }
                    if (sbb.isEmpty())
                        continue ff;
                    result = "'" + sbb.toString() + "'";
                }
            }
            mapArg.put(field.getName(), String.valueOf(result));
        }
    }

    /**
     * 执行搜索
     *
     * @param searcher 搜索器接口的实现
     * @return 返回数据
     * @throws IllegalAccessException 如果生成map时失败抛出
     */
    public List<E> onSearch(Searcher<E> searcher) throws IllegalAccessException {
        Map<String, Object> mapArg = this.buildMap();
        if (this.spu != null) {
            long count = searcher.searchCount(mapArg);
            this.spu.setTotalRow(count);
            if (count <= 0)
                return null;
            mapArg.put("minRow", this.spu.getCurrMinRowNum());
            mapArg.put("maxRow", this.spu.getCurrMaxRowNum());
            mapArg.put("rowCount", this.spu.getPerPageCount());
        }
        return searcher.search(mapArg);
    }

    public void setTimeLimit(TimeLimit timelimit) {
        if (timelimit == null)
            return;
        LoggerUtils.logger.info("向搜索参数中设置TimeLimit：" + timelimit.getCode() + timelimit.getName());
        System.out.println("向搜索参数中设置TimeLimit：" + timelimit.getCode() + timelimit.getName());
        this.start = timelimit.getStartTime();
        this.end = timelimit.getEndTime();
        LoggerUtils.logger.info("开始时间：" + DateTools.dateToString(this.start) + "--结束时间：" + DateTools.dateToString(this.end));
        System.out.println("开始时间：" + DateTools.dateToString(this.start) + "--结束时间：" + DateTools.dateToString(this.end));
    }

    public SplitPageUtil getSpu() {
        return spu;
    }

    public void setSpu(SplitPageUtil spu) {
        this.spu = spu;
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setStartAndEnd(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public static interface Searcher<E> {
        long searchCount(Map<String, Object> mapArg);

        List<E> search(Map<String, Object> mapArg);
    }
}
