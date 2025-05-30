package fr.n7.stl.minijava.expression.allocation;

import java.util.List;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class ObjectAllocation implements Expression {
    protected String className;
    protected List<Expression> arguments;

    public ObjectAllocation(String _className, List<Expression> _arguments) {
        this.className = _className;
        this.arguments = _arguments;
    }

    @Override
    public Type getType() {
        // TODO: Implement type resolution for object allocation
        return null;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = true;
        for (Expression arg : this.arguments) {
            result = result && arg.collectAndPartialResolve(_scope);
        }
        return result;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = true;
        for (Expression arg : this.arguments) {
            result = result && arg.completeResolve(_scope);
        }
        return result;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        // TODO: Implement code generation for object allocation
        return _factory.createFragment();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new ").append(this.className).append("(");
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