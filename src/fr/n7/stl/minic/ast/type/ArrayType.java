/**
 * 
 */
package fr.n7.stl.minic.ast.type;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.util.Logger;

/**
 * @author Marc Pantel
 *
 */
public class ArrayType implements Type {

	protected Type element;

	public ArrayType(Type _element) {
		this.element = _element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof ArrayType) {
			return this.element.equalsTo(((ArrayType) _other).element);
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		if (_other == null) {
			return false;
		}
		
		if (_other instanceof ArrayType) {
			return this.element.compatibleWith(((ArrayType) _other).element);
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (_other == null) {
			return AtomicType.ErrorType;
		}
		
		if (_other instanceof ArrayType) {
			Type mergedElement = this.element.merge(((ArrayType) _other).element);
			if (mergedElement == AtomicType.ErrorType) {
				return AtomicType.ErrorType;
			}
			return new ArrayType(mergedElement);
		} else {
			return AtomicType.ErrorType;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		// Return the size of a pointer to the array
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.element + " [])";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean resolved = this.element.completeResolve(_scope);
		if (!resolved) {
			Logger.error("Failed to resolve array element type: " + this.element);
		}
		return resolved;
	}

	/**
	 * @return Type of the elements in the array.
	 */
	public Type getType() {
		return this.element;
	}

}
