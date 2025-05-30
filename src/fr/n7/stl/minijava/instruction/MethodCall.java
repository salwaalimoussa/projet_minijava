package fr.n7.stl.minijava.instruction;

import java.util.List;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class MethodCall implements Instruction {
    protected Expression object;
    protected String methodName;
    protected List<Expression> arguments;

    public MethodCall(Expression _object, String _methodName, List<Expression> _arguments) {
        this.object = _object;
        this.methodName = _methodName;
        this.arguments = _arguments;
    }

    public MethodCall(String _methodName, List<Expression> _arguments) {
        this.methodName = _methodName;
        this.arguments = _arguments;
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
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _function) {
        return this.collectAndPartialResolve(_scope);
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
    public boolean checkType() {
        // TODO: Implement type checking for method call
        return true;
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        return _offset;
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
        sb.append(");");
        return sb.toString();
    }
} 