package fr.n7.stl.minijava.ast.type.declaration;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;

public abstract class ClassElement  implements Declaration {
	
	protected ElementKind elementKind;
	
	protected AccessRight accessRight;
	
	protected String name;
	
	public ClassElement(ElementKind _elementKind, AccessRight _accessRight, String _name) {
		this.elementKind = _elementKind;
		this.accessRight = _accessRight;
		this.name = _name;
	}
	
	public ClassElement(String _name) {
		this( ElementKind.OBJECT, AccessRight.PACKAGE, _name);
	}
	
	public ElementKind getElementKind() {
		return this.elementKind;
	}
	
	public void setElementKind(ElementKind _elementKind) {
		this.elementKind = _elementKind;
	}
	
	public AccessRight getAccessRight() {
		return this.accessRight;
	}
	
	public void setAccessRight(AccessRight _accessRight) {
		this.accessRight = _accessRight;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (!_scope.accepts(this)) {
			return false;
		}
		_scope.register(this);
		return true;
	}

	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return true;
	}

}
