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
public class Conditional implements Instruction {

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}
	public Block getThenBlock() {
		return this.thenBranch;
	}
	public Block getElseBlock() {
		return this.elseBranch;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch
				+ ((this.elseBranch != null) ? (" else " + this.elseBranch) : "");
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
		boolean thenResolved = this.thenBranch.collectAndPartialResolve(_scope);
		boolean elseResolved = (this.elseBranch != null) ? this.elseBranch.collectAndPartialResolve(_scope) : true;
		return conditionResolved && thenResolved && elseResolved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope
	 * .Scope)
	 */
	@Override
public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _function) {
    boolean conditionResolved = this.condition.collectAndPartialResolve(_scope);
    boolean thenResolved = this.thenBranch.collectAndPartialResolve(_scope, _function);
    boolean elseResolved = (this.elseBranch != null) ? this.elseBranch.collectAndPartialResolve(_scope, _function) : true;
    return conditionResolved && thenResolved && elseResolved;
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
    boolean thenResolved = this.thenBranch.completeResolve(_scope);
    boolean elseResolved = (this.elseBranch != null) ? this.elseBranch.completeResolve(_scope) : true;
    return conditionResolved && thenResolved && elseResolved;
}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		boolean ok1 = this.thenBranch.checkType();
		boolean ok2 = (this.elseBranch != null) ? this.elseBranch.checkType() : true;
		boolean ok3 = this.condition.getType().equals(AtomicType.BooleanType);
		if (!ok3) {
			Logger.error("Condition of the conditional is not a boolean type.");
		}
		return ok1 && ok2 && ok3;
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
		int currentOffset = _offset;
		 this.thenBranch.allocateMemory(_register, currentOffset);
		if (this.elseBranch != null) {
			this.elseBranch.allocateMemory(_register, currentOffset);
		}
		return currentOffset;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 * @Override
public Fragment getCode(TAMFactory _factory) {
    Fragment fragment = _factory.createFragment();
    
    // Generate condition code
    fragment.append(condition.getCode(_factory));
    
    // Create labels
    String elseLabel = "else_" + _factory.createLabelNumber();
    String endLabel = "endif_" + _factory.createLabelNumber();
    
    // Jump to else if condition false
    fragment.add(_factory.createJumpIf(elseLabel, 0));
    
    // Then branch
    fragment.append(thenBranch.getCode(_factory));
    fragment.add(_factory.createJump(endLabel));
    
    // Else branch
    fragment.addSuffix(elseLabel);
    if (elseBranch != null) {
        fragment.append(elseBranch.getCode(_factory));
    }
    
    fragment.addSuffix(endLabel);
    
    return fragment;
}

	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();
	
		// Code condition n == 0
		fragment.append(condition.getCode(_factory)); // Génère code pour charger n == 0
	
		String elseLabel = "else_" + _factory.createLabelNumber();
		String endLabel = "endif_" + _factory.createLabelNumber();
	
		fragment.add(_factory.createJumpIf(elseLabel, 0)); // si faux (n != 0) sauter else
	
		// Then branch (return 1)
		fragment.append(thenBranch.getCode(_factory)); // return 1
		fragment.add(_factory.createJump(endLabel));
	
		// Else branch
		fragment.addSuffix(elseLabel);
		if (elseBranch != null) {
			fragment.append(elseBranch.getCode(_factory));
		}
	
		fragment.addSuffix(endLabel);
	
		return fragment;
	}
	public Block getThenBranch() {
		return this.thenBranch;
	}
	
	public Block getElseBranch() {
		return this.elseBranch;
	}
	
}
