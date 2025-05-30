/**
 * 
 */
package fr.n7.stl.minijava.ast.type.declaration;

import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * 
 */
public class ClassDeclaration implements Instruction, Declaration {
	
	protected List<ClassElement> elements;
	
	protected boolean concrete;
	
	protected String name;
	
	protected String ancestor;

	/**
	 * 
	 */
	public ClassDeclaration(boolean _concrete, String _name, String _ancestor, List<ClassElement> _elements) {
		this.concrete = _concrete;
		this.name = _name;
		this.ancestor = _ancestor;
		this.elements = _elements;
	}
	
	/**
	 * 
	 */
	public ClassDeclaration(boolean _concrete, String _name, List<ClassElement> _elements) {
		this( _concrete, _name, null, _elements);
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean result = true;
		// Register the class itself in the scope
		if (!_scope.accepts(this)) {
			return false;
		}
		_scope.register(this);
		
		// Create a new scope for class elements
		HierarchicalScope<Declaration> classScope = new fr.n7.stl.minic.ast.scope.SymbolTable(_scope);
		
		// Collect all elements
		for (ClassElement element : this.elements) {
			result = result && element.collectAndPartialResolve(classScope);
		}
		
		return result;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		throw new SemanticsUndefinedException( "Semantics resolve is undefined in ClassDeclaration.");
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean result = true;
		
		// Resolve ancestor class if it exists
		if (this.ancestor != null) {
			Declaration ancestorDecl = _scope.get(this.ancestor);
			if (ancestorDecl == null || !(ancestorDecl instanceof ClassDeclaration)) {
				return false;
			}
		}
		
		// Create a new scope for class elements
		HierarchicalScope<Declaration> classScope = new fr.n7.stl.minic.ast.scope.SymbolTable(_scope);
		
		// Complete resolve for all elements
		for (ClassElement element : this.elements) {
			result = result && element.completeResolve(classScope);
		}
		
		return result;
	}

	@Override
	public boolean checkType() {
		boolean result = true;
		
		// Check ancestor class if it exists
		if (this.ancestor != null) {
			// Type checking for inheritance will be done in completeResolve
			// Here we just verify that the ancestor class exists and is concrete
			// This is already done in completeResolve
		}
		
		// Check types for all class elements
		for (ClassElement element : this.elements) {
			if (element instanceof MethodDeclaration) {
				result = result && ((MethodDeclaration)element).checkType();
			} else if (element instanceof AttributeDeclaration) {
				result = result && ((AttributeDeclaration)element).checkType();
			} else if (element instanceof ConstructorDeclaration) {
				result = result && ((ConstructorDeclaration)element).checkType();
			}
		}
		
		return result;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		throw new SemanticsUndefinedException( "Semantics allocation memory is undefined in ClassDeclaration.");
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		throw new SemanticsUndefinedException( "Semantics get code is undefined in ClassDeclaration.");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		String image = "";
		if (! this.concrete) {
			image += "abstract ";
		}
		image += "class " + this.name + " ";
		if (this.ancestor != null) {
			image += "extends " + this.ancestor + " ";
		}
		image += "{\n";
		for (ClassElement e : this.elements) {
			image += e;
		}
		image += "}\n";
		return image;
	}

}
