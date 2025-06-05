/**
 * 
 */
package fr.n7.stl.minic.ast;

import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.Return;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
import java.util.List;

/**
 * Represents a Block node in the Abstract Syntax Tree node for the Bloc
 * language.
 * Declares the various semantics attributes for the node.
 * 
 * A block contains declarations. It is thus a Scope even if a separate
 * SymbolTable is used in
 * the attributed semantics in order to manage declarations.
 * 
 * @author Marc Pantel
 *
 */
public class Block {

	/**
	 * Sequence of instructions contained in a block.
	 */
	protected List<Instruction> instructions;

	/**
	 * Constructor for a block.
	 */
	public Block(List<Instruction> _instructions) {
		this.instructions = _instructions;
	}

	public List<Instruction> getInstructions() {
		return this.instructions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _local = "";
		for (Instruction _instruction : this.instructions) {
			_local += _instruction;
		}
		return "{\n" + _local + "}\n";
	}

	/**
	 * Inherited Semantics attribute to collect all the identifiers declaration and
	 * check
	 * that the declaration are allowed.
	 * 
	 * @param _scope Inherited Scope attribute that contains the identifiers defined
	 *               previously
	 *               in the context.
	 * @return Synthesized Semantics attribute that indicates if the identifier
	 *         declaration are
	 *         allowed.
	 */

	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean result = true;
		for (Instruction instruction : this.instructions) {
			boolean resolved = instruction.collectAndPartialResolve(_scope);
			if (!resolved) {
				Logger.error("Failed to collect instruction: " + instruction);
			}
			result = result && resolved;
		}
		return result;
	}

	/**
	 * Inherited Semantics attribute to collect all the identifiers declaration and
	 * check
	 * that the declaration are allowed.
	 * 
	 * @param _scope     Inherited Scope attribute that contains the identifiers
	 *                   defined previously
	 *                   in the context.
	 * @param _container Inherited Container attribute that allows to link the
	 *                   return instructions
	 *                   with the function declaration.
	 * @return Synthesized Semantics attribute that indicates if the identifier
	 *         declaration are
	 *         allowed.
	 */
	
	 public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _function) {
		 boolean result = true;
		 for (Instruction instruction : this.instructions) {
			 boolean resolved = instruction.collectAndPartialResolve(_scope, _function);
			 if (!resolved) {
				 Logger.error("Failed to collect instruction with function context: " + instruction);
			 }
			 result = result && resolved;
		 }
		 return result;
	 }

	/**
	 * Inherited Semantics attribute to check that all identifiers have been defined
	 * and
	 * associate all identifiers uses with their definitions.
	 * 
	 * @param _scope Inherited Scope attribute that contains the defined
	 *               identifiers.
	 * @return Synthesized Semantics attribute that indicates if the identifier used
	 *         in the
	 *         block have been previously defined.
	 */
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean result = true;

		// First pass: collect and resolve all declarations
		for (Instruction instruction : this.instructions) {
			// Collect and resolve all instructions first
			boolean resolved = instruction.collectAndPartialResolve(_scope);
			if (!resolved) {
				Logger.error("Failed to collect instruction: " + instruction);
				result = false;
			}
		}

		// Second pass: complete resolution of all instructions
		for (Instruction instruction : this.instructions) {
			boolean resolved = instruction.completeResolve(_scope);
			if (!resolved) {
				Logger.error("Failed to resolve instruction: " + instruction);
				result = false;
			}
		}

		return result;
	}
	public void associateReturnsWithMethod(MethodDeclaration method) {
    for (Instruction instruction : this.instructions) {
        if (instruction instanceof Return) {
            ((Return) instruction).setMethod(method);
        }
        // Récursif pour les blocks imbriqués
        if (instruction instanceof Block) {
            ((Block) instruction).associateReturnsWithMethod(method);
        }
        // AJOUTER : Gestion des conditionnels (if/else)
        if (instruction instanceof fr.n7.stl.minic.ast.instruction.Conditional) {
            fr.n7.stl.minic.ast.instruction.Conditional conditional = 
                (fr.n7.stl.minic.ast.instruction.Conditional) instruction;
            if (conditional.getThenBlock() != null) {
                conditional.getThenBlock().associateReturnsWithMethod(method);
            }
            if (conditional.getElseBlock() != null) {
                conditional.getElseBlock().associateReturnsWithMethod(method);
            }
        }
        // AJOUTER : Gestion des boucles (while)
        if (instruction instanceof fr.n7.stl.minic.ast.instruction.Iteration) {
            fr.n7.stl.minic.ast.instruction.Iteration iteration = 
                (fr.n7.stl.minic.ast.instruction.Iteration) instruction;
            if (iteration.getBody() != null) {
                iteration.getBody().associateReturnsWithMethod(method);
            }
        }
    }
}
	/**
	 * Synthesized Semantics attribute to check that an instruction if well typed.
	 * 
	 * @return Synthesized True if the instruction is well typed, False if not.
	 */
	public boolean checkType() {

		boolean result = true;
		for (Instruction instruction : this.instructions) {
			result = instruction.checkType() && result;
		}
		return result;
	}

	/**
	 * Inherited Semantics attribute to allocate memory for the variables declared
	 * in the instruction.
	 * Synthesized Semantics attribute that compute the size of the allocated
	 * memory.
	 * 
	 * @param _register Inherited Register associated to the address of the
	 *                  variables.
	 * @param _offset   Inherited Current offset for the address of the variables.
	 * @return The new offset after allocating memory for all instructions.
	 */
	public int allocateMemory(Register _register, int _offset) {
		int currentOffset = _offset;
		for (Instruction instruction : this.instructions) {
			currentOffset = instruction.allocateMemory(_register, currentOffset);
		}
		return currentOffset;
	}

	/**
	 * Inherited Semantics attribute to build the nodes of the abstract syntax tree
	 * for the generated TAM code.
	 * Synthesized Semantics attribute that provide the generated TAM code.
	 * 
	 * @param _factory Inherited Factory to build AST nodes for TAM code.
	 * @return Synthesized AST for the generated TAM code.
	 */

	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();
		for (Instruction instruction : this.instructions) {
			fragment.append(instruction.getCode(_factory));
		}
		return fragment;
	}

}
