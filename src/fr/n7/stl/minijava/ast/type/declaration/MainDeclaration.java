package fr.n7.stl.minijava.ast.type.declaration;

import java.util.List;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.scope.SymbolTable;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class MainDeclaration implements Instruction {
	
	protected String name;
	
	protected List<Declaration> declarations;
	
	protected Block main;

	public MainDeclaration(String _name, List<Declaration> _declarations, Block _main) {
		this.name = _name;
		this.declarations = _declarations;
		this.main = _main;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean result = true;
		
		// Create a new scope for main declarations
		HierarchicalScope<Declaration> mainScope = new SymbolTable(_scope);
		
		// Collect all declarations
		for (Declaration declaration : this.declarations) {
			if (declaration instanceof Instruction) {
				result = result && ((Instruction)declaration).collectAndPartialResolve(mainScope);
				if (!result) {
					Logger.error("Failed to collect declaration: " + declaration);
				}
			}
		}
		
		// Collect main block with the same scope that has the declarations
		result = result && this.main.collectAndPartialResolve(mainScope);
		if (!result) {
			Logger.error("Failed to collect main block");
		}
		
		return result;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		return this.collectAndPartialResolve(_scope);
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// Create a new scope for main declarations
		HierarchicalScope<Declaration> mainScope = new SymbolTable(_scope);
		
		// First collect and resolve all declarations
		boolean result = true;
		for (Declaration declaration : this.declarations) {
			if (declaration instanceof Instruction) {
				// First collect and resolve the declaration
				result = result && ((Instruction)declaration).collectAndPartialResolve(mainScope);
				if (!result) {
					Logger.error("Failed to collect declaration: " + declaration);
					return false;
				}
				
				// Then complete resolve the declaration
				result = result && ((Instruction)declaration).completeResolve(mainScope);
				if (!result) {
					Logger.error("Failed to resolve declaration: " + declaration);
					return false;
				}
			}
		}
		
		// Then resolve the main block using the same scope
		result = result && this.main.completeResolve(mainScope);
		if (!result) {
			Logger.error("Failed to resolve main block");
			return false;
		}
		
		return result;
	}

	@Override
	public boolean checkType() {
		boolean result = true;
		
		// Check types for all declarations
		for (Declaration declaration : this.declarations) {
			if (declaration instanceof Instruction) {
				result = result && ((Instruction)declaration).checkType();
				if (!result) {
					Logger.error("Type check failed for declaration: " + declaration);
				}
			}
		}
		
		// Check types in the main block
		result = result && this.main.checkType();
		if (!result) {
			Logger.error("Type check failed in main block");
		}
		
		return result;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();
		
		// Add a PUSH instruction to make the fragment non-empty
		fragment.add(_factory.createPush(0));
		
		// Generate code for declarations
		for (Declaration declaration : this.declarations) {
			if (declaration instanceof Instruction) {
				Fragment declCode = ((Instruction)declaration).getCode(_factory);
				if (declCode != null) {
					fragment.append(declCode);
				}
			}
		}
		
		// Generate code for main block
		if (this.main != null) {
			Fragment mainCode = this.main.getCode(_factory);
			if (mainCode != null) {
				fragment.append(mainCode);
			}
		}
		
		return fragment;
	}
	
	@Override
	public int allocateMemory(Register _register, int _offset) {
		int currentOffset = _offset;
		
		// Allocate memory for declarations
		for (Declaration declaration : this.declarations) {
			if (declaration instanceof Instruction) {
				currentOffset = ((Instruction)declaration).allocateMemory(_register, currentOffset);
			}
		}
		
		// Allocate memory for main block
		if (this.main != null) {
			currentOffset = this.main.allocateMemory(_register, currentOffset);
		}
		
		return currentOffset;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		String image = "";
		image += "public class " + this.name + " ";
		image += "{\n";
		image += "\n";
		for (Declaration uneDeclaration : this.declarations) {
			image += uneDeclaration;
			image += "\n";
		}
		image += "\tpublic static void Main( String[] args) ";
		image += this.main;
		image += "\n";
		image += "}\n";
		return image;
	}

}
