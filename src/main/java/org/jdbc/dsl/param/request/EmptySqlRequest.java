package org.jdbc.dsl.param.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptySqlRequest implements SqlRequest {

    public static final EmptySqlRequest INSTANCE=new EmptySqlRequest();

    @Override
    public String getSql() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Object[] getParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "empty sql";
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
