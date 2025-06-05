package fr.n7.stl.minijava.ast.type.declaration;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.util.Logger;
import java.util.Iterator;
import java.util.List;

public class MethodDeclaration  extends ClassElement {
	
	protected boolean concrete;
	
	protected List<ParameterDeclaration> parameters;
	
	protected Block body;
	
	protected Type type;
	
	public MethodDeclaration(String _name, Type _type, List<ParameterDeclaration> _parameters, Block _body) {
		super( _name );
		this.parameters = _parameters;
		this.body = _body;
		this.concrete = (_body != null);
		this.type = _type;
	}
	
	public MethodDeclaration(String _name, Type _type, List<ParameterDeclaration> _parameters) {
		this( _name, _type, _parameters, null);
	}

	

	@Override
	public String toString() {
		String image = "";
		if (! this.concrete) {
			image += "abstract ";
		}
		image += this.accessRight + " " + this.type + " " + this.name + "( ";
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
		if (this.concrete) {
			image += this.body; 
		} else {
			image += ";";
		}
		return image;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// Let SymbolTable handle method overloading
		if (!_scope.accepts(this)) {
			return false;
		}
		_scope.register(this);
		
		// Create a new scope for method parameters and body
		HierarchicalScope<Declaration> methodScope = new fr.n7.stl.minic.ast.scope.SymbolTable(_scope);
		
		// Collect parameters
		boolean result = true;
		for (ParameterDeclaration param : this.parameters) {
			result = result && param.collectAndPartialResolve(methodScope);
		}
		
		// If concrete method (has body), collect body
		if (this.concrete && this.body != null) {
			result = result && this.body.collectAndPartialResolve(methodScope);
		}
		// Dans MethodDeclaration, après collectAndPartialResolve du body
if (this.concrete && this.body != null) {
    result = result && this.body.collectAndPartialResolve(methodScope);
    // AJOUT : Associer les returns à cette méthode
    this.body.associateReturnsWithMethod(this);
}
		
		return result;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// Create a new scope for method parameters and body
		HierarchicalScope<Declaration> methodScope = new fr.n7.stl.minic.ast.scope.SymbolTable(_scope);
		boolean result = true;
		for (ParameterDeclaration param : this.parameters) {
			result = result && param.collectAndPartialResolve(methodScope);
			if (!result) {
				return false;
			}
		}
		// Resolve parameters
		for (ParameterDeclaration param : this.parameters) {
			result = result && param.completeResolve(methodScope);
		}
		
		// If concrete method (has body), resolve body
		if (this.concrete && this.body != null) {
			result = result && this.body.completeResolve(methodScope);
		}
		
		return result;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	public List<ParameterDeclaration> getParameters() {
		return this.parameters;
	}

	public boolean checkType() {
		boolean result = true;
		
		// Check return type
		if (this.type == null) {
			Logger.error("Method " + this.name + " has no return type");
			return false;
		}
		
		// Check parameter types
		for (ParameterDeclaration param : this.parameters) {
			if (param.getType() == null) {
				Logger.error("Parameter " + param.getName() + " in method " + this.name + " has no type");
				return false;
			}
		}
		
		// Check types in method body if it's a concrete method
		if (this.concrete && this.body != null) {
			result = result && this.body.checkType();
		}
		
		return result;
	}

	public fr.n7.stl.tam.ast.Fragment getCode(fr.n7.stl.tam.ast.TAMFactory _factory) {
		fr.n7.stl.tam.ast.Fragment fragment = _factory.createFragment();
		
		if (this.concrete && this.body != null) {
			int localVariablesSize = this.body.allocateMemory(fr.n7.stl.tam.ast.Register.LB, 0);
			// Add a PUSH instruction to make the fragment non-empty
			fragment.add(_factory.createPush(localVariablesSize));
			
			// Create a label for the method
			String methodLabel = "method_" + this.name;
			fragment.addPrefix(methodLabel);
			
			// Generate code for method body
			fragment.append(this.body.getCode(_factory));
			
			// Add return instruction if needed
			if (!this.type.equals(fr.n7.stl.minic.ast.type.AtomicType.VoidType)) {
				fragment.add(_factory.createReturn(this.type.length(), this.parameters.size() * 1)); // Assuming each parameter takes 1 word
			} else {
				fragment.add(_factory.createReturn(0, this.parameters.size() * 1));
			}
		}
		
		return fragment;
	}

}
