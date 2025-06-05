package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class SuperAccess implements AccessibleExpression, AssignableExpression {

    public SuperAccess() {
    }

    @Override
    public String getName() {
        return "super";
    }

    @Override
    public Type getType() {
        // TODO: Implement type resolution for super access
        return null;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return true;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return true;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        // TODO: Implement code generation for super access
        return _factory.createFragment();
    }

    @Override
    public String toString() {
        return "super";
    }
}