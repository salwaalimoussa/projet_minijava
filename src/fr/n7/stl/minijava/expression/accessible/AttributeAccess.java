package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AttributeAccess implements AccessibleExpression, AssignableExpression {
    protected Expression object;
    protected String attributeName;
    protected fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration resolvedAttribute;

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
        if (this.resolvedAttribute != null) {
            return this.resolvedAttribute.getType();
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
        boolean result = this.object.completeResolve(_scope);
        
        if (result) {
            Type objectType = this.object.getType();
            if (objectType != null) {
                String className = objectType.toString().trim();
                
                HierarchicalScope<Declaration> globalScope = _scope;
                while (globalScope.getParent() != null) {
                    globalScope = globalScope.getParent();
                }
                
                Declaration classDecl = globalScope.get(className);
                if (classDecl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
                    fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration currentClass = 
                        (fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) classDecl;
                    
                    boolean attributeFound = false;
                    while (currentClass != null && !attributeFound) {
                        for (fr.n7.stl.minijava.ast.type.declaration.ClassElement element : currentClass.getElements()) {
                            if (element instanceof fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration 
                                && element.getName().equals(this.attributeName)) {
                                this.resolvedAttribute = (fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration) element;
                                attributeFound = true;
                                break;
                            }
                        }
                        
                        if (!attributeFound) {
                            String ancestorName = currentClass.getAncestor();
                            if (ancestorName != null) {
                                Declaration ancestorDecl = globalScope.get(ancestorName);
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
                    
                    if (!attributeFound) {
                        Logger.error("Attribute '" + this.attributeName + "' does not exist in class '" + className + "'");
                        return false;
                    }
                } else {
                    Logger.error("Cannot find class declaration for type: " + className);
                    return false;
                }
            } else {
                Logger.error("Cannot determine type of object for attribute access: " + this.attributeName);
                return false;
            }
        }
        
        return result;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        // TODO: Implement code generation for attribute access
        return _factory.createFragment();
    }
}