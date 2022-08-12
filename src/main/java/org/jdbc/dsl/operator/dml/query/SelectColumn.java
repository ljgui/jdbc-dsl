package org.jdbc.dsl.operator.dml.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdbc.dsl.core.RDBFeatures;
import org.jdbc.dsl.operator.FunctionColumn;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SelectColumn extends FunctionColumn {

    private String alias;

    public static SelectColumn of(String name,String alias) {
        SelectColumn column = new SelectColumn();
        column.setColumn(name);
        column.setAlias(alias);
        return column;
    }
    public static SelectColumn of(String name) {
        SelectColumn column = new SelectColumn();
        column.setColumn(name);
        return column;
    }

    public SelectColumn max(){
        this.setFunction(RDBFeatures.max.getFunction());
        return this;
    }
    public SelectColumn min(){
        this.setFunction(RDBFeatures.min.getFunction());
        return this;
    }
    public SelectColumn avg(){
        this.setFunction(RDBFeatures.avg.getFunction());
        return this;
    }
    public SelectColumn count(){
        this.setFunction(RDBFeatures.count.getFunction());
        return this;
    }
    public SelectColumn sum(){
        this.setFunction(RDBFeatures.sum.getFunction());
        return this;
    }

}
