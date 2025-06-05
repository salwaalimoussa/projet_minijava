package fr.n7.stl.minijava.ast.type.declaration;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.scope.Declaration;

public class ConstructorDeclaration extends ClassElement {
	
	protected List<ParameterDeclaration> parameters;
	
	protected Block body;

	public ConstructorDeclaration(String _name, List<ParameterDeclaration> _parameters, Block _body) {
		super( _name);
		this.parameters = _parameters;
		this.body = _body;
	}

	@Override
	public String toString() {
		String image = "";
		image += this.accessRight + " " + this.name + "( ";
		Iterator<ParameterDeclaration> iterator = this.parameters.iterator();
		if (iterator.hasNext()) {
			ParameterDeclaration parameter = iterator.next();
			image += parameter;
			while (iterator.hasNext()) {
				 parameter = iterator.next();
				 image += " ," + parameter;
			}
		}
		image += ")";
		image += this.body; 
		return image;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ParameterDeclaration> getParameters() {
		return this.parameters;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// First register the constructor itself
		if (!_scope.accepts(this)) {
			return false;
		}
		_scope.register(this);
		
		// Create a new scope for constructor parameters and body
		HierarchicalScope<Declaration> constructorScope = new fr.n7.stl.minic.ast.scope.SymbolTable(_scope);
		
		// Collect parameters
		boolean result = true;
		for (ParameterDeclaration param : this.parameters) {
			result = result && param.collectAndPartialResolve(constructorScope);
		}
		
		// Collect body
		if (this.body != null) {
			result = result && this.body.collectAndPartialResolve(constructorScope);
		}
		
		return result;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// Create a new scope for constructor parameters and body
		HierarchicalScope<Declaration> constructorScope = new fr.n7.stl.minic.ast.scope.SymbolTable(_scope);
		
		// First register all parameters in the scope
		boolean result = true;
		for (ParameterDeclaration param : this.parameters) {
			result = result && param.collectAndPartialResolve(constructorScope);
			if (!result) {
				return false;
			}
		}
		
		// Then resolve parameters
		for (ParameterDeclaration param : this.parameters) {
			result = result && param.completeResolve(constructorScope);
			if (!result) {
				return false;
			}
		}
		
		// Finally resolve body with parameters in scope
		if (this.body != null) {
			result = result && this.body.completeResolve(constructorScope);
		}
		
		return result;
	}

	public boolean checkType() {
		boolean result = true;
		
		// Check types in the constructor body
		if (this.body != null) {
			result = result && this.body.checkType();
		}
		
		return result;
	}

	public fr.n7.stl.tam.ast.Fragment getCode(fr.n7.stl.tam.ast.TAMFactory _factory) {
		fr.n7.stl.tam.ast.Fragment fragment = _factory.createFragment();
		
		// Add a PUSH instruction to make the fragment non-empty
		fragment.add(_factory.createPush(0));
		
		// Create a label for the constructor
		String constructorLabel = "constructor_" + this.name;
		fragment.addPrefix(constructorLabel);
		
		// Generate code for constructor body
		if (this.body != null) {
			fragment.append(this.body.getCode(_factory));
		}
		
		// Add return instruction (constructors don't return values)
		fragment.add(_factory.createReturn(0, this.parameters.size() * 1)); // Assuming each parameter takes 1 word
		
		return fragment;
	}
}
