package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minijava.expression.accessible.AccessibleExpression;

public class AttributeAccess implements AccessibleExpression, AssignableExpression {
    protected Expression object;
    protected String attributeName;

    public AttributeAccess(Expression _object, String _attributeName) {
        this.object = _object;
        this.attributeName = _attributeName;
    }

    @Override
    public String getName() {
        return this.attributeName;
    }

    @Override
    public Type getType() {
        // TODO: Implement type resolution for attribute access
        return null;
    }

    @Override
    public String toString() {
        return this.object + "." + this.attributeName;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return this.object.collectAndPartialResolve(_scope);
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.object.completeResolve(_scope);
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        // TODO: Implement code generation for attribute access
        return _factory.createFragment();
    }
} 