/**
 * 
 */
package fr.n7.stl.minic.ast.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of a hierarchical scope using maps.
 * @author Marc Pantel
 *
 */
public class SymbolTable implements HierarchicalScope<Declaration> {
	
	private Map<String, Object> declarations;
	private HierarchicalScope<Declaration> parent;

	public SymbolTable() {
		this.declarations = new HashMap<String, Object>();
		this.parent = null;
	}
	
	public SymbolTable(Scope<Declaration> _parent) {
		this();
		if (_parent instanceof HierarchicalScope) {
			this.parent = (HierarchicalScope<Declaration>) _parent;
		} else {
			this.parent = null;
		}
	}
	
	public SymbolTable(HierarchicalScope<Declaration> _parent) {
		this();
		this.parent = _parent;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#get(java.lang.String)
	 */
	@Override
	public Declaration get(String _name) {
		if (this.declarations.containsKey(_name)) {
			Object value = this.declarations.get(_name);
			if (value instanceof Declaration) {
				return (Declaration) value;
			} else if (value instanceof Map) {
				// For overloaded methods/constructors, return the first one
				@SuppressWarnings("unchecked")
				Map<String, Declaration> methods = (Map<String, Declaration>) value;
				return methods.values().iterator().next();
			}
		}
		if (this.parent != null) {
			return this.parent.get(_name);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String _name) {
		return this.declarations.containsKey(_name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#accepts(fr.n7.stl.block.ast.scope.Declaration)
	 */
	@Override
	public boolean accepts(Declaration _declaration) {
		if (!this.contains(_declaration.getName())) {
			return true;
		}
		// If it's a method declaration, check if we can overload
		if (_declaration instanceof fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration) {
			Object existing = this.declarations.get(_declaration.getName());
			if (existing instanceof Map) {
				// Already have overloaded methods, check signatures
				@SuppressWarnings("unchecked")
				Map<String, Declaration> methods = (Map<String, Declaration>) existing;
				String signature = getMethodSignature((fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration) _declaration);
				return !methods.containsKey(signature);
			} else if (existing instanceof fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration) {
				// First overload attempt
				return true;
			}
		}
		// If it's a constructor declaration, check if we can overload
		else if (_declaration instanceof fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration) {
			Object existing = this.declarations.get(_declaration.getName());
			if (existing instanceof Map) {
				// Already have overloaded constructors, check signatures
				@SuppressWarnings("unchecked")
				Map<String, Declaration> constructors = (Map<String, Declaration>) existing;
				String signature = getConstructorSignature((fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration) _declaration);
				return !constructors.containsKey(signature);
			} else if (existing instanceof fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration) {
				// First overload attempt
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#register(fr.n7.stl.block.ast.scope.Declaration)
	 */
	@Override
	public void register(Declaration _declaration) {
		if (_declaration instanceof fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration) {
			registerMethod((fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration) _declaration);
		} else if (_declaration instanceof fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration) {
			registerConstructor((fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration) _declaration);
		} else if (this.accepts(_declaration)) {
			this.declarations.put(_declaration.getName(), _declaration);
		} else {
			throw new IllegalArgumentException();
		}
	}

	private void registerMethod(fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration method) {
		String name = method.getName();
		String signature = getMethodSignature(method);
		
		if (!this.declarations.containsKey(name)) {
			// First method with this name
			this.declarations.put(name, method);
		} else {
			Object existing = this.declarations.get(name);
			if (existing instanceof fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration) {
				// Convert to overloaded methods map
				Map<String, Declaration> methods = new HashMap<>();
				fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration existingMethod = 
					(fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration) existing;
				methods.put(getMethodSignature(existingMethod), existingMethod);
				methods.put(signature, method);
				this.declarations.put(name, methods);
			} else if (existing instanceof Map) {
				// Add to existing overloaded methods
				@SuppressWarnings("unchecked")
				Map<String, Declaration> methods = (Map<String, Declaration>) existing;
				if (!methods.containsKey(signature)) {
					methods.put(signature, method);
				} else {
					throw new IllegalArgumentException("Method with same signature already exists");
				}
			}
		}
	}

	private String getMethodSignature(fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration method) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getType().toString()).append("#");
		for (fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration param : method.getParameters()) {
			sb.append(param.getType().toString()).append(",");
		}
		return sb.toString();
	}

	private void registerConstructor(fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration constructor) {
		String name = constructor.getName();
		String signature = getConstructorSignature(constructor);
		
		if (!this.declarations.containsKey(name)) {
			// First constructor with this name
			this.declarations.put(name, constructor);
		} else {
			Object existing = this.declarations.get(name);
			if (existing instanceof fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration) {
				// Convert to overloaded constructors map
				Map<String, Declaration> constructors = new HashMap<>();
				fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration existingConstructor = 
					(fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration) existing;
				constructors.put(getConstructorSignature(existingConstructor), existingConstructor);
				constructors.put(signature, constructor);
				this.declarations.put(name, constructors);
			} else if (existing instanceof Map) {
				// Add to existing overloaded constructors
				@SuppressWarnings("unchecked")
				Map<String, Declaration> constructors = (Map<String, Declaration>) existing;
				if (!constructors.containsKey(signature)) {
					constructors.put(signature, constructor);
				} else {
					throw new IllegalArgumentException("Constructor with same signature already exists");
				}
			}
		}
	}

	private String getConstructorSignature(fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration constructor) {
		StringBuilder sb = new StringBuilder();
		for (fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration param : constructor.getParameters()) {
			sb.append(param.getType().toString()).append(",");
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.HierarchicalScope#knows(java.lang.String)
	 */
	@Override
	public boolean knows(String _name) {
		if (this.contains(_name)) {
			return true;
		} else {
			if (this.parent != null) {
				return this.parent.knows(_name);
			} else {
				return false;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "Local definitions : ";
		for (Map.Entry<String, Object> entry : this.declarations.entrySet()) {
			result += entry.getKey() + " -> " + entry.getValue() + "\n";
		}
		if (this.parent != null) {
			result += this.parent;
		}
		return result;
	}

	@Override
	public Iterable<Declaration> getAll() {
		// Collect all declarations, including those in overloaded methods/constructors
		java.util.List<Declaration> allDeclarations = new java.util.ArrayList<>();
		for (Object value : this.declarations.values()) {
			if (value instanceof Declaration) {
				allDeclarations.add((Declaration) value);
			} else if (value instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Declaration> methods = (Map<String, Declaration>) value;
				allDeclarations.addAll(methods.values());
			}
		}
		return allDeclarations;
	}

	@Override
	public HierarchicalScope<Declaration> getParent() {
		return this.parent;
	}

}
