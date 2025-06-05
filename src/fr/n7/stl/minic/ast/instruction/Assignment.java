/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for an array type.
 * 
 * @author Marc Pantel
 *
 */
public class Assignment implements Instruction, Expression {

	protected Expression value;
	protected AssignableExpression assignable;
	// ona ajoute cela pour travailler avec logger

	/**
	 * Create an assignment instruction implementation from the assignable
	 * expression
	 * and the assigned value.
	 * 
	 * @param _assignable Expression that can be assigned a value.
	 * @param _value      Value assigned to the expression.
	 */
	public Assignment(AssignableExpression _assignable, Expression _value) {
		this.assignable = _assignable;
		this.value = _value;
		/*
		 * This attribute will be assigned to the appropriate value by the resolve
		 * action
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.assignable + " = " + this.value.toString() + ";\n";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope
	 * .HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean assignableResolved = this.assignable.collectAndPartialResolve(_scope);
		if (!assignableResolved) {
			Logger.error("Failed to resolve assignable expression: " + this.assignable);
		}
		
		boolean valueResolved = this.value.collectAndPartialResolve(_scope);
		if (!valueResolved) {
			Logger.error("Failed to resolve value expression: " + this.value);
		}
		
		return assignableResolved && valueResolved;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		return this.collectAndPartialResolve(_scope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope
	 * .HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean assignableResolved = this.assignable.completeResolve(_scope);
		if (!assignableResolved) {
			Logger.error("Failed to completely resolve assignable expression: " + this.assignable);
		}
		
		boolean valueResolved = this.value.completeResolve(_scope);
		if (!valueResolved) {
			Logger.error("Failed to completely resolve value expression: " + this.value);
		}
		
		return assignableResolved && valueResolved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.expression.Expression#getType()
	 */
	@Override
	public Type getType() {
		return this.assignable.getType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		Type assignableType = this.assignable.getType();
		Type valueType = this.value.getType();
		
		if (assignableType == null) {
			Logger.error("Type of assignable expression is null: " + this.assignable);
			return false;
		}
		
		if (valueType == null) {
			Logger.error("Type of value expression is null: " + this.value);
			return false;
		}
		
		if (assignableType.compatibleWith(valueType)) {
			return true;
		} else {
			Logger.error("Type mismatch in assignment: expected " + assignableType + ", but found " + valueType);
			return false;
		}
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
		return _offset; // Aucun espace mémoire utilisé ici
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();

		// Génère le code pour l'évaluation de la valeur (ex: 5)
		fragment.append(this.value.getCode(_factory));

		// Génère le code pour obtenir l'adresse de la variable (ex: x)
		fragment.append(this.assignable.getCode(_factory));

		// Génère le store : stocke la valeur à l'adresse
		fragment.add(_factory.createStoreI(this.assignable.getType().length()));
		// fragment.add(_factory.createStore(Register.SB, 0, value.getType().length()));
		return fragment;
	}

}
