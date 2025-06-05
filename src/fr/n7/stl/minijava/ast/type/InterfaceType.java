package fr.n7.stl.minijava.ast.type;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.InterfaceDeclaration;

public class InterfaceType implements Type {
    
    protected InterfaceDeclaration declaration;
    
    public InterfaceType(InterfaceDeclaration _declaration) {
        this.declaration = _declaration;
    }
    
    @Override
    public boolean equalsTo(Type _other) {
        if (_other instanceof InterfaceType) {
            return this.declaration.getName().equals(((InterfaceType) _other).declaration.getName());
        }
        return false;
    }
    
    @Override
    public boolean compatibleWith(Type _other) {
        if (_other instanceof InterfaceType) {
            return this.equalsTo(_other);
        }
        return false;
    }
    
    @Override
    public Type merge(Type _other) {
        if (this.compatibleWith(_other)) {
            return this;
        } else {
            return null;
        }
    }
    
    @Override
    public int length() {
        return 1;  // Interface reference takes one word
    }
    
    @Override
    public String toString() {
        return this.declaration.getName();
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return true;  // Interface type is already resolved through its declaration
    }
} 