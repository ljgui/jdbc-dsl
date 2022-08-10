package org.jdbc.dsl.render;

import java.util.LinkedList;

public class SqlAppender extends LinkedList<String> {

    public SqlAppender add(Object... str) {
        for (Object s : str) {
            this.add(String.valueOf(s));
        }
        return this;
    }

    public SqlAppender addEdSpc(Object... str) {
        for (Object s : str) {
            this.add(String.valueOf(s));
        }
        this.add(" ");
        return this;
    }

    /**
     * 接入sql语句，并自动加入空格
     *
     * @param str
     * @return
     */
    public SqlAppender addSpc(Object... str) {
        for (Object s : str) {
            this.add(s);
            this.add(" ");
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.forEach(builder::append);
        return builder.toString();
    }
}