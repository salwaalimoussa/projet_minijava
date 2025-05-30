package fr.n7.stl.minijava.expression.assignable;

import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAssignment implements AssignableExpression {
    protected AssignableExpression object;
    protected String attributeName;

    public AttributeAssignment(AssignableExpression _object, String _attributeName) {
        this.object = _object;
        this.attributeName = _attributeName;
    }

    @Override
    public String getName() {
        return this.attributeName;
    }

    @Override
    public Type getType() {
        // TODO: Implement type resolution for attribute assignment
        return null;
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
        // TODO: Implement code generation for attribute assignment
        return _factory.createFragment();
    }

    @Override
    public String toString() {
        return this.object + "." + this.attributeName;
    }
} 