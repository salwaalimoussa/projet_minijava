package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import java.util.List;

public class MethodCallAccess implements AccessibleExpression, AssignableExpression {
    protected Expression object;
    protected String methodName;
    protected List<Expression> arguments;

    public MethodCallAccess(Expression _object, String _methodName, List<Expression> _arguments) {
        this.object = _object;
        this.methodName = _methodName;
        this.arguments = _arguments;
    }

    public MethodCallAccess(String _methodName, List<Expression> _arguments) {
        this.methodName = _methodName;
        this.arguments = _arguments;
    }

    @Override
    public String getName() {
        return this.methodName;
    }

    @Override
    public Type getType() {
        // TODO: Implement type resolution for method call
        return null;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = true;
        if (this.object != null) {
            result = this.object.collectAndPartialResolve(_scope);
        }
        for (Expression arg : this.arguments) {
            result = result && arg.collectAndPartialResolve(_scope);
        }
        return result;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = true;
        if (this.object != null) {
            result = this.object.completeResolve(_scope);
        }
        for (Expression arg : this.arguments) {
            result = result && arg.completeResolve(_scope);
        }
        return result;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        // TODO: Implement code generation for method call
        return _factory.createFragment();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.object != null) {
            sb.append(this.object).append(".");
        }
        sb.append(this.methodName).append("(");
        if (!this.arguments.isEmpty()) {
            sb.append(this.arguments.get(0));
            for (int i = 1; i < this.arguments.size(); i++) {
                sb.append(", ").append(this.arguments.get(i));
            }
        }
        sb.append(")");
        return sb.toString();
    }
}