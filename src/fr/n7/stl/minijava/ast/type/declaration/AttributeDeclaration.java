package fr.n7.stl.minijava.ast.type.declaration;

import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.util.Logger;
import fr.n7.stl.tam.ast.Register;

public class AttributeDeclaration extends ClassElement {
	
	protected Type type;
	protected Register register;
	protected int offset;

	public AttributeDeclaration( String _name, Type _type) {
		super(_name);
		this.type = _type;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.accessRight + " " + type + " " + this.name + ";\n"; 
	}

	public boolean checkType() {
		// Check that the attribute has a type
		if (this.type == null) {
			Logger.error("Attribute " + this.name + " has no type");
			return false;
		}
		
		return true;
	}

	public int allocateMemory(Register _register, int _offset) {
		this.register = _register;
		this.offset = _offset;
		return _offset + this.type.length();
	}

	public Register getRegister() {
		return this.register;
	}

	public int getOffset() {
		return this.offset;
	}
}
