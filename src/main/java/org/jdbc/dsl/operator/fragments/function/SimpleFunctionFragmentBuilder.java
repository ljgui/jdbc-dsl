package org.jdbc.dsl.operator.fragments.function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jdbc.dsl.operator.fragments.EmptySqlFragments;
import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.operator.fragments.SqlFragments;

import java.util.Map;

@Getter
@AllArgsConstructor
public class SimpleFunctionFragmentBuilder implements FunctionFragmentBuilder {

    private final String function;

    private final String name;


    @Override
    public SqlFragments create(String columnFullName, Map<String, Object> opts) {

        if (opts != null) {
            String arg = String.valueOf(opts.get("arg"));
            if ("1".equals(arg)) {
                columnFullName = arg;
            }
        }
        if (columnFullName == null) {
            return EmptySqlFragments.INSTANCE;
        }
        return PrepareSqlFragments
                .of()
                .addSql(function.concat("(").concat(columnFullName).concat(")"));
    }


}
