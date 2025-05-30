package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

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
        Type objectType = this.object.getType();
        if (objectType instanceof fr.n7.stl.minijava.ast.type.ClassType) {
            fr.n7.stl.minijava.ast.type.ClassType classType = (fr.n7.stl.minijava.ast.type.ClassType) objectType;
            HierarchicalScope<Declaration> scope = classType.getScope();
            
            if (scope != null) {
                // Find the class declaration
                Declaration decl = scope.get(classType.getName());
                if (decl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
                    fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration currentClass = 
                        (fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) decl;
                    
                    // Check current class and all ancestor classes for the attribute
                    while (currentClass != null) {
                        // Check if the attribute exists in this class
                        for (fr.n7.stl.minijava.ast.type.declaration.ClassElement element : currentClass.getElements()) {
                            if (element instanceof fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration 
                                && element.getName().equals(this.attributeName)) {
                                return element.getType();
                            }
                        }
                        
                        // If not found, check the ancestor class
                        String ancestorName = currentClass.getAncestor();
                        if (ancestorName != null) {
                            Declaration ancestorDecl = scope.get(ancestorName);
                            if (ancestorDecl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
                                currentClass = (fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) ancestorDecl;
                            } else {
                                currentClass = null;
                            }
                        } else {
                            currentClass = null;
                        }
                    }
                }
            }
        }
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