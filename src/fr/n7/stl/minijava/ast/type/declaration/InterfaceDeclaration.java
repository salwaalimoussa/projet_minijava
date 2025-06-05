package fr.n7.stl.minijava.ast.type.declaration;

import java.util.List;

import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class InterfaceDeclaration implements Instruction, Declaration {
    
    protected String name;
    protected List<MethodDeclaration> methods;

    public InterfaceDeclaration(String _name, List<MethodDeclaration> _methods) {
        this.name = _name;
        this.methods = _methods;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = true;
        // Register the interface itself in the scope
        if (!_scope.accepts(this)) {
            return false;
        }
        _scope.register(this);
        
        // Create a new scope for interface methods
        HierarchicalScope<Declaration> interfaceScope = new fr.n7.stl.minic.ast.scope.SymbolTable(_scope);
        
        // Collect all methods
        for (MethodDeclaration method : this.methods) {
            result = result && method.collectAndPartialResolve(interfaceScope);
        }
        
        return result;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
        return this.collectAndPartialResolve(_scope);
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = true;
        // Create a scope for resolving interface methods
        HierarchicalScope<Declaration> interfaceScope = new fr.n7.stl.minic.ast.scope.SymbolTable(_scope);
        
        // Complete resolve for all methods
        for (MethodDeclaration method : this.methods) {
            result = result && method.completeResolve(interfaceScope);
        }
        
        return result;
    }

    @Override
    public boolean checkType() {
        return true;  // Interfaces don't need type checking at this level
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        return _offset;  // Interfaces don't need memory allocation
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        return _factory.createFragment();  // Interfaces don't generate code
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Type getType() {
        return new fr.n7.stl.minijava.ast.type.InterfaceType(this);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("interface ").append(this.name).append(" {\n");
        for (MethodDeclaration method : this.methods) {
            sb.append(method).append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
} 