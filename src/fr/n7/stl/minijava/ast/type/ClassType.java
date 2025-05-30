package fr.n7.stl.minijava.ast.type;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;

public class ClassType implements Type {
	
	protected String name;

	public ClassType(String _name) {
		this.name = _name;
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
			return this.equalsTo(_other);
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
		if (_scope.knows(this.name)) {
			Declaration declaration = _scope.get(this.name);
			if (declaration instanceof fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration) {
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
		return " " + this.name + " ";
	}

}
