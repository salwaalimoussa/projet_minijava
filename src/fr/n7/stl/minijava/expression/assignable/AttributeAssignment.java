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
    private fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration classDeclaration;

    public AttributeAssignment(AssignableExpression _object, String _attributeName) {
        this.object = _object;
        this.attributeName = _attributeName;
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
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = this.object.collectAndPartialResolve(_scope);
        if (!result) return false;

        Type objectType = this.object.getType();
        if (objectType instanceof fr.n7.stl.minijava.ast.type.ClassType) {
            String className = ((fr.n7.stl.minijava.ast.type.ClassType) objectType).getName();
            
            // Find the class declaration in the current scope or any parent scope
            HierarchicalScope<Declaration> currentScope = _scope;
            while (currentScope != null) {
                Declaration decl = currentScope.get(className);
                if (decl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
                    fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration currentClass = 
                        (fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) decl;
                    
                    // Check current class and all ancestor classes for the attribute
                    while (currentClass != null) {
                        // Check if the attribute exists in this class
                        for (fr.n7.stl.minijava.ast.type.declaration.ClassElement element : currentClass.getElements()) {
                            if (element instanceof fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration 
                                && element.getName().equals(this.attributeName)) {
                                this.classDeclaration = currentClass;
                                return true;
                            }
                        }
                        
                        // If not found, check the ancestor class
                        String ancestorName = currentClass.getAncestor();
                        if (ancestorName != null) {
                            Declaration ancestorDecl = currentScope.get(ancestorName);
                            if (ancestorDecl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
                                currentClass = (fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) ancestorDecl;
                            } else {
                                currentClass = null;
                            }
                        } else {
                            currentClass = null;
                        }
                    }
                    break;
                }
                currentScope = currentScope.getParent();
            }
        }
        return false;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = this.object.completeResolve(_scope);
        if (!result) return false;

        if (this.classDeclaration != null) {
            for (fr.n7.stl.minijava.ast.type.declaration.ClassElement element : this.classDeclaration.getElements()) {
                if (element instanceof fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration 
                    && element.getName().equals(this.attributeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return this.attributeName;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        return _factory.createFragment();
    }

    @Override
    public String toString() {
        return this.object + "." + this.attributeName;
    }
} 