package fr.n7.stl.minijava.expression.assignable;

import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class SuperAssignment implements AssignableExpression {

    public SuperAssignment() {
    }

    @Override
    public String getName() {
        return "super";
    }

    @Override
    public Type getType() {
        // TODO: Implement type resolution for super assignment
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
        // TODO: Implement code generation for super assignment
        return _factory.createFragment();
    }

    @Override
    public String toString() {
        return "super";
    }
} 