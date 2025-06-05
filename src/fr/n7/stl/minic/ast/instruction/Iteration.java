/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional
 * instruction.
 * 
 * @author Marc Pantel
 *
 */
public class Iteration implements Instruction {

	protected Expression condition;
	protected Block body;

	public Iteration(Expression _condition, Block _body) {
		this.condition = _condition;
		this.body = _body;
	}
	public Block getBody() {
		return this.body;
	} 
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "while (" + this.condition + " )" + this.body;
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
		boolean conditionResolved = this.condition.collectAndPartialResolve(_scope);
		boolean bodyResolved = this.body.collectAndPartialResolve(_scope);
		return conditionResolved && bodyResolved;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		// Résout d'abord la condition
		boolean conditionResolved = this.condition.collectAndPartialResolve(_scope);

		// Puis résout le corps en passant le contexte de fonction
		boolean bodyResolved = this.body.collectAndPartialResolve(_scope, _container);

		return conditionResolved && bodyResolved;
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
		boolean conditionResolved = this.condition.completeResolve(_scope);
		boolean bodyResolved = this.body.completeResolve(_scope);
		return conditionResolved && bodyResolved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		if (!this.condition.getType().equalsTo(AtomicType.BooleanType)) {
			Logger.error("The condition of the while loop must be of type boolean.");
			return false;
		}
		return this.body.checkType();
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
		int newOffset = this.body.allocateMemory(_register, _offset);
    	return newOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();
		String startLabel = "while_start_" + _factory.createLabelNumber();
		String endLabel = "while_end_" + _factory.createLabelNumber();

		// Initial jump to condition
		fragment.add(_factory.createJump(startLabel));

		// While loop start
		fragment.addSuffix(startLabel);

		// Condition evaluation
		fragment.append(condition.getCode(_factory));
		fragment.add(_factory.createJumpIf(endLabel, 0));

		// Loop body - Fix variable address loading
		fragment.append(body.getCode(_factory));

		// Jump back to start
		fragment.add(_factory.createJump(startLabel));

		// Loop exit
		fragment.addSuffix(endLabel);

		return fragment;

	}

}