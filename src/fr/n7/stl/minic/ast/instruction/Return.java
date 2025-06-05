/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.FunctionCall;
import fr.n7.stl.minic.ast.expression.accessible.BinaryExpression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a return instruction.
 * 
 * @author Marc Pantel
 *
 */
public class Return implements Instruction {

	protected Expression value;

	protected FunctionDeclaration function;

	protected MethodDeclaration method;

	public Return(Expression _value) {
		this.value = _value;
		this.function = null;
		this.method = null;
	}
	public void setMethod(MethodDeclaration _method) {
        this.method = _method;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ((this.function != null) ? ("// Return in function : " + this.function.getName() + "\n") : "")
				+ "return " + this.value + ";\n";
	}

	public Expression getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope
	 * .Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return this.value.collectAndPartialResolve(_scope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope
	 * .Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.value.completeResolve(_scope);
	}

@Override
public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
    this.function = _container;

    // Direct function call case
    if (this.value instanceof FunctionCall) {
        return ((FunctionCall) this.value).collectAndPartialResolve(_scope, _container);
    }
    
    // Si c'est une BinaryExpression (ex : n * fact(n - 1))
    if (this.value instanceof BinaryExpression) {
        return ((BinaryExpression) this.value).collectAndPartialResolve(_scope, _container);
    }

    // Default case for other expressions
    return this.value.collectAndPartialResolve(_scope);
}
public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, MethodDeclaration _method) {
    this.method = _method;
    return this.value.collectAndPartialResolve(_scope);
}


	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	// @Override
	// public boolean checkType() {
	// 	// Verify that the function is associated
	// 	if (this.function == null) {
	// 		Logger.error("Return statement is not associated with a function.");
	// 		return false;
	// 	}

	// 	// Check if the return type matches the function's declared return type
	// 	if (!this.value.getType().equalsTo(this.function.getType())) {
	// 		Logger.error("The return type does not match the declared return type of the function "
	// 				+ this.function.getName());
	// 		return false;
	// 	}

	// 	return true;
	// }
	@Override
    public boolean checkType() {
        // Vérifier qu'il y a au moins un conteneur (fonction ou méthode)
        if (this.function == null && this.method == null) {
            Logger.error("Return statement is not associated with a function or method.");
            return false;
        }

        // Obtenir le type attendu selon le conteneur
        Type expectedType = (this.function != null) 
            ? this.function.getType() 
            : this.method.getType();

        if (!this.value.getType().equalsTo(expectedType)) {
            String container = (this.function != null) 
                ? "function " + this.function.getName() 
                : "method " + this.method.getName();
            Logger.error("Return type mismatch in " + container);
            return false;
        }

        return true;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register,
	 * int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		// Le return ne déclare pas de mémoire locale supplémentaire
		return _offset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();

    // Génère le code pour évaluer l'expression retournée
    fragment.append(this.value.getCode(_factory));

    // Vérification défensive
    if (this.function == null && this.method == null) {
        throw new RuntimeException("Return statement has no associated function or method");
    }

    if (this.function != null) {
        // Cas d'une fonction
        fragment.add(_factory.createReturn(
            this.function.getType().length(), 
            this.function.getParameters().size()
        ));
    } else {
        // Cas d'une méthode
        fragment.add(_factory.createReturn(
            this.method.getType().length(), 
            this.method.getParameters().size()
        ));
    }

    return fragment;
	}

}
