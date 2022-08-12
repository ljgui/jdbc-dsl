package org.jdbc.dsl.operator.fragments.query;

import lombok.Getter;
import lombok.Setter;
import org.jdbc.dsl.operator.dml.query.SelectColumn;
import org.jdbc.dsl.operator.dml.query.SortOrder;
import org.jdbc.dsl.param.Term;

import java.util.*;

@Getter
@Setter
public class QueryOperatorParameter {

    private List<SelectColumn> select = new ArrayList<>();

    private Set<String> selectExcludes = new HashSet<>();

    private String from;

    private String fromAlias;

    private List<Term> where = new ArrayList<>();

    private List<SortOrder> orderBy = new ArrayList<>();

    private List<SelectColumn> groupBy = new ArrayList<>();

    private List<Term> having = new ArrayList<>();

    private Integer pageIndex;

    private Integer pageSize;

    private Boolean forUpdate;

    private Map<String,Object> context;

    public String getFromAlias() {
        if (fromAlias == null) {
            return from;
        }
        return fromAlias;
    }

   /* public QueryOperatorParameter(List<SelectColumn> select,Set<String> selectExcludes){
        this.select = select;
        this.selectExcludes = selectExcludes;
    }

    public static QueryOperatorParameter of(List<SelectColumn> select,Set<String> selectExcludes){
        return new QueryOperatorParameter(select,selectExcludes);
    }*/

}
