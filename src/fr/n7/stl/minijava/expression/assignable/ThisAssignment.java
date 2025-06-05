package fr.n7.stl.minijava.expression.assignable;

import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class ThisAssignment implements AssignableExpression {
    private String currentClassName;
    private HierarchicalScope<Declaration> scope;

    public ThisAssignment() {
        this.currentClassName = null;
    }

    @Override
    public String getName() {
        return "this";
    }

    @Override
    public Type getType() {
        if (this.currentClassName != null && this.scope != null) {
            Declaration decl = this.scope.get(this.currentClassName);
            if (decl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
                fr.n7.stl.minijava.ast.type.ClassType type = new fr.n7.stl.minijava.ast.type.ClassType(this.currentClassName);
                type.completeResolve(this.scope);  // This will set the scope in the ClassType
                return type;
            }
        }
        return null;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        // Find the enclosing class declaration
        HierarchicalScope<Declaration> currentScope = _scope;
        while (currentScope != null) {
            for (Declaration decl : currentScope.getAll()) {
                if (decl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
                    this.currentClassName = decl.getName();
                    this.scope = currentScope;
                    return true;
                }
            }
            currentScope = currentScope.getParent();
        }
        return false;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.currentClassName != null;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        // TODO: Implement code generation for this assignment
        return _factory.createFragment();
    }

    @Override
    public String toString() {
        return "this";
    }
} 