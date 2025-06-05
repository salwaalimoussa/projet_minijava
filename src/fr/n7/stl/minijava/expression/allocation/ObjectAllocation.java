package fr.n7.stl.minijava.expression.allocation;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
import java.util.List;

public class ObjectAllocation implements AccessibleExpression {
    protected String className;
    protected List<Expression> arguments;
    protected HierarchicalScope<Declaration> scope;

    public ObjectAllocation(String _className, List<Expression> _arguments) {
        this.className = _className;
        this.arguments = _arguments;
    }

    @Override
    public Type getType() {
        if (this.scope != null) {
            fr.n7.stl.minijava.ast.type.ClassType type = new fr.n7.stl.minijava.ast.type.ClassType(this.className);
            if (type.completeResolve(this.scope)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        this.scope = _scope;
        
        // Check if the class exists
        Declaration decl = _scope.get(this.className);
        if (!(decl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration)) {
            Logger.error("Class " + this.className + " not found");
            return false;
        }
        
        // Resolve all arguments
        boolean result = true;
        for (Expression arg : this.arguments) {
            result = result && arg.collectAndPartialResolve(_scope);
        }
        return result;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        this.scope = _scope;
        
        // Check if the class exists and get its declaration
        Declaration decl = _scope.get(this.className);
        if (!(decl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration)) {
            Logger.error("Class " + this.className + " not found");
            return false;
        }
        
        fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration classDecl = 
            (fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) decl;
        
        // Check constructor arguments
        boolean result = true;
        for (Expression arg : this.arguments) {
            result = result && arg.completeResolve(_scope);
        }
        
        // TODO: Check if constructor exists with matching argument types
        
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