package fr.n7.stl.minijava.instruction;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.ClassElement;
import fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
import java.util.List;

public class MethodCall implements Instruction {
    protected Expression object;
    protected String methodName;
    protected List<Expression> arguments;
    protected MethodDeclaration resolvedMethod;

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
        
        if (this.object != null) {
            Type objectType = this.object.getType();
            if (objectType != null) {
                String className = objectType.toString();
                
                
                Declaration classDecl = _scope.get(className);
                
                // Si pas trouvé localement, chercher dans le scope global
                if (classDecl == null) {
                    HierarchicalScope<Declaration> globalScope = _scope;
                    while (globalScope.getParent() != null) {
                        globalScope = globalScope.getParent();
                    }
                    
                    classDecl = globalScope.get(className);
                }
                
                if (classDecl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
                    fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration clazz = 
                        (fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) classDecl;
                    
                    boolean methodFound = false;
                    for (ClassElement element : clazz.getElements()) {
                        if (element instanceof MethodDeclaration) {
                            MethodDeclaration method = (MethodDeclaration) element;
                            if (method.getName().equals(this.methodName)) {
                                this.resolvedMethod = method;
                                methodFound = true;
                                break;
                            }
                        }
                    }
                    
                    if (!methodFound) {
                        Logger.error("Method '" + this.methodName + "' does not exist in class '" + className + "'");
                        return false;
                    }
                } else {
                    Logger.error("Cannot find class declaration for type: " + className);
                    return false;
                }
            }
        } else {
            Declaration decl = _scope.get(this.methodName);
            if (decl instanceof MethodDeclaration) {
                this.resolvedMethod = (MethodDeclaration) decl;
            } else {
                Logger.error("Method '" + this.methodName + "' not found in current scope");
                return false;
            }
        }
        
        return result;
    }

    @Override
    public boolean checkType() {
        boolean result = true;
        
        // Vérifier que la méthode a été résolue
        if (this.resolvedMethod == null) {
            Logger.error("Method '" + this.methodName + "' was not resolved");
            return false;
        }
        
        // Vérifier les types des arguments
        List<fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration> parameters = this.resolvedMethod.getParameters();
        
        // Vérifier le nombre d'arguments
        if (this.arguments.size() != parameters.size()) {
            Logger.error("Method '" + this.methodName + "' expects " + parameters.size() + 
                        " arguments but " + this.arguments.size() + " were provided");
            return false;
        }
        
        // Vérifier le type de chaque argument
        for (int i = 0; i < this.arguments.size(); i++) {
            Expression arg = this.arguments.get(i);
            fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration param = parameters.get(i);
            
            Type argType = arg.getType();
            Type paramType = param.getType();
            
            if (argType == null) {
                Logger.error("Argument " + (i + 1) + " of method '" + this.methodName + "' has no type");
                result = false;
            } else if (paramType == null) {
                Logger.error("Parameter " + (i + 1) + " of method '" + this.methodName + "' has no type");
                result = false;
            } else if (!argType.equalsTo(paramType)) {
                Logger.error("Type mismatch for argument " + (i + 1) + " of method '" + this.methodName + 
                            "': expected " + paramType + ", but got " + argType);
                result = false;
            }
        }
        
        return result;
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        return _offset;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
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