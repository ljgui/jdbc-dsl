package org.jdbc.dsl.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * 查询参数
 */
@Getter
@Setter
@SuppressWarnings("all")
public class QueryParam extends Param implements Serializable, Cloneable {
    private static final long serialVersionUID = 7941767360194797891L;

    /**是否进行分页，默认为true */
    private boolean paging = false;

    /** 第一页索引 **/
    private int firstPageIndex = 0;

    /** 第几页 */
    private int pageIndex = 1;

    /** 每页显示记录条数 */
    private int pageSize = 25;

    /** 排序字段 */
    private List<Sort> sorts = new LinkedList<>();

    /** 分组字段 */
    private List<String> groupBy = new LinkedList<>();

    /** 上下文信息 */
    private Map<String, Object> context;

    public Optional<Object> getContext(String key) {
        if (context == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(context.get(key));
    }

    public void context(String key, Object value) {
        if (context == null) {
            context = new HashMap<>();
        }
        context.put(key, value);
    }

    public Sort orderBy(String column) {
        Sort sort = new Sort(column);
        sorts.add(sort);
        return sort;
    }

    public void groupBy(String column) {
        groupBy.add(column);
    }
    public void groupBy(String... column) {
        groupBy.addAll(Arrays.asList(column));
    }

    public <Q extends QueryParam> Q noPaging() {
        setPaging(false);
        return (Q) this;
    }

    public <Q extends QueryParam> Q doPaging(int pageIndex) {
        setPageIndex(pageIndex);
        setPaging(true);
        return (Q) this;
    }

    public <Q extends QueryParam> Q doPaging(int pageIndex, int pageSize) {
        setPageIndex(pageIndex);
        setPageSize(pageSize);
        setPaging(true);
        return (Q) this;
    }

    public <Q extends QueryParam> Q rePaging(int total) {
        paging = true;
        // 当前页没有数据后跳转到最后一页
        if (pageIndex != 0 && (pageIndex * pageSize) >= total) {
            int tmp = total / this.getPageSize();
            pageIndex = total % this.getPageSize() == 0 ? tmp - 1 : tmp;
        }
        return (Q) this;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = Math.max(pageIndex - firstPageIndex, 0);
    }


    @Override
    public QueryParam clone() {
        QueryParam queryParam = ((QueryParam) super.clone());
        if (queryParam.context != null) {
            queryParam.context = new HashMap<>(queryParam.context);
        }
        return queryParam;
    }
}
