package fr.n7.stl.minijava.ast.type;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;

public class ClassType implements Type {
	
	protected String name;
	protected HierarchicalScope<Declaration> scope;

	public ClassType(String _name) {
		this.name = _name;
		this.scope = null;
	}

	public String getName() {
		return this.name;
	}

	public HierarchicalScope<Declaration> getScope() {
		return this.scope;
	}

	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof ClassType) {
			return this.name.equals(((ClassType) _other).name);
		}
		return false;
	}

	@Override
	public boolean compatibleWith(Type _other) {
		if (_other instanceof ClassType) {
			ClassType otherClass = (ClassType) _other;
			
			// If types are equal, they are compatible
			if (this.equalsTo(_other)) {
				return true;
			}
			
			// If either scope is not set, we can't check inheritance
			if (this.scope == null || otherClass.scope == null) {
				return false;
			}
			
			// Check if this is a superclass of _other
			String currentClassName = otherClass.name;
			
			// Check the inheritance chain
			while (currentClassName != null) {
				Declaration decl = otherClass.scope.get(currentClassName);
				if (decl instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
					fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration currentClass = 
						(fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) decl;
					
					// If we find this class in the inheritance chain of _other, types are compatible
					if (currentClass.getName().equals(this.name)) {
						return true;
					}
					
					// Move up to the ancestor
					currentClassName = currentClass.getAncestor();
				} else {
					currentClassName = null;
				}
			}
		}
		return false;
	}

	@Override
	public Type merge(Type _other) {
		if (this.compatibleWith(_other)) {
			return this;
		}
		return null;
	}

	@Override
	public int length() {
		return 1; // Class reference takes one word
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		this.scope = _scope;  // Store the scope for later use
		if (_scope.knows(this.name)) {
			Declaration declaration = _scope.get(this.name);
			if (declaration instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
				declaration = (fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) declaration;
				return true;
			} else {
				fr.n7.stl.util.Logger.error("The type " + this.name + " is not a class.");
				return false;
			}
		} else {
			fr.n7.stl.util.Logger.error("The class " + this.name + " has not been found.");
			return false;
		}
	}
	
	public String toString() {
		return "" + this.name + "";
	}


	

}
