package org.jdbc.dsl.param;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;
import org.jdbc.dsl.operator.dml.query.SelectColumn;

/**
 * SQL参数对象
 *
 */
@SuppressWarnings("all")
@Getter
@Setter
public class Param implements Cloneable {

    /**
     * 条件
     */
    @NonNull
    protected List<Term> terms = new LinkedList<>();

    /**
     * 指定要处理的字段
     */
    @NonNull
    protected List<SelectColumn> includes = new LinkedList<>();

    /**
     * 指定不处理的字段
     */
    @NonNull
    protected Set<String> excludes = new LinkedHashSet<>();

    public <T extends Param> T or(String column, String termType, Object value) {
        Term term = new Term();
        term.setTermType(termType);
        term.setColumn(column);
        term.setValue(value);
        term.setType(Term.Type.or);
        terms.add(term);
        return (T) this;
    }

    public <T extends Param> T and(String column, String termType, Object value) {
        Term term = new Term();
        term.setTermType(termType);
        term.setColumn(column);
        term.setValue(value);
        term.setType(Term.Type.and);
        terms.add(term);
        return (T) this;
    }

    public Term nest() {
        return nest(null, null);
    }

    public Term orNest() {
        return orNest(null, null);
    }

    public Term nest(String termString, Object value) {
        Term term = new Term();
        term.setColumn(termString);
        term.setValue(value);
        term.setType(Term.Type.and);
        terms.add(term);
        return term;
    }

    public Term orNest(String termString, Object value) {
        Term term = new Term();
        term.setColumn(termString);
        term.setValue(value);
        term.setType(Term.Type.or);
        terms.add(term);
        return term;
    }

    public <T extends Param> T includes(String... fields) {
        List<SelectColumn> columns = Arrays.stream(fields).map(SelectColumn::of).collect(Collectors.toList());
        includes.addAll(columns);
        return (T) this;
    }

    public <T extends Param> T excludes(String... fields) {
        excludes.addAll(Arrays.asList(fields));
        return (T) this;
    }

    public <T extends Param> T addTerm(Term term) {
        terms.add(term);
        return (T) this;
    }

    @Override
    @SneakyThrows
    public Param clone() {
        Param param = ((Param) super.clone());
        setExcludes(new LinkedHashSet<>(excludes));
        setIncludes(new LinkedList<>(includes));
        setTerms(this.terms.stream().map(Term::clone).collect(Collectors.toList()));
        return param;
    }
}