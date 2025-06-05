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
	
	protected HierarchicalScope<Declaration> scope;

	/**
	 * 
	 */
	public ClassDeclaration(boolean _concrete, String _name, String _ancestor, List<ClassElement> _elements) {
		this.concrete = _concrete;
		this.name = _name;
		this.ancestor = _ancestor;
		this.elements = _elements;
		this.scope = null;
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
		this.scope = _scope;  // Store the scope
		
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
		
		// Check for final method override violations
		if (this.ancestor != null && this.scope != null) {
			result = result && checkFinalMethodOverrides();
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
	
	private boolean checkFinalMethodOverrides() {
		// Get ancestor class
		Declaration ancestorDecl = this.scope.get(this.ancestor);
		if (!(ancestorDecl instanceof ClassDeclaration)) {
			return true; // Error already reported elsewhere
		}
		
		ClassDeclaration ancestorClass = (ClassDeclaration) ancestorDecl;
		
		// For each method in this class, check if it overrides a final method in ancestor
		for (ClassElement element : this.elements) {
			if (element instanceof MethodDeclaration) {
				MethodDeclaration thisMethod = (MethodDeclaration) element;
				
				// Look for a method with same name and signature in ancestor hierarchy
				if (isOverridingFinalMethod(thisMethod, ancestorClass)) {
					fr.n7.stl.util.Logger.error("Cannot override final method '" + thisMethod.getName() + "' from class " + ancestorClass.getName() + " in class " + this.name);
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean isOverridingFinalMethod(MethodDeclaration method, ClassDeclaration ancestorClass) {
		// Look for methods with same name and parameter signature in ancestor class
		for (ClassElement ancestorElement : ancestorClass.getElements()) {
			if (ancestorElement instanceof MethodDeclaration) {
				MethodDeclaration ancestorMethod = (MethodDeclaration) ancestorElement;
				
				// Check if method names match and parameter signatures match
				if (ancestorMethod.getName().equals(method.getName()) && 
					hasSameParameterSignature(ancestorMethod, method)) {
					
					// Heuristic: if a concrete method (has body) is being overridden
					// by an abstract method (no body), this suggests final override attempt
					// In normal Java, you can't override concrete with abstract unless there's something special
					if (ancestorMethod.concrete && !method.concrete) {
						return true;
					}
				}
			}
		}
		
		// Check in ancestor's ancestors recursively
		if (ancestorClass.getAncestor() != null && this.scope != null) {
			Declaration grandAncestorDecl = this.scope.get(ancestorClass.getAncestor());
			if (grandAncestorDecl instanceof ClassDeclaration) {
				return isOverridingFinalMethod(method, (ClassDeclaration) grandAncestorDecl);
			}
		}
		
		return false;
	}
	
	private boolean hasSameParameterSignature(MethodDeclaration method1, MethodDeclaration method2) {
		// Check parameter count first
		if (method1.getParameters().size() != method2.getParameters().size()) {
			return false;
		}
		
		// Check parameter types
		for (int i = 0; i < method1.getParameters().size(); i++) {
			Type type1 = method1.getParameters().get(i).getType();
			Type type2 = method2.getParameters().get(i).getType();
			
			if (type1 != null && type2 != null && !type1.equalsTo(type2)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		int currentOffset = _offset;
		
		// First allocate memory for ancestor class if it exists
		if (this.ancestor != null) {
			Declaration ancestorDecl = this.scope.get(this.ancestor);
			if (ancestorDecl instanceof ClassDeclaration) {
				currentOffset = ((ClassDeclaration) ancestorDecl).allocateMemory(_register, currentOffset);
			}
		}
		
		// Then allocate memory for all class elements
		for (ClassElement element : this.elements) {
			if (element instanceof AttributeDeclaration) {
				currentOffset = ((AttributeDeclaration) element).allocateMemory(_register, currentOffset);
			}
			// Methods don't need memory allocation in the instance
		}
		
		return currentOffset;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();
		
		// Add a PUSH instruction to make the fragment non-empty
		fragment.add(_factory.createPush(0));
		
		// Generate code for ancestor class if it exists
		if (this.ancestor != null) {
			Declaration ancestorDecl = this.scope.get(this.ancestor);
			if (ancestorDecl instanceof ClassDeclaration) {
				Fragment ancestorCode = ((ClassDeclaration) ancestorDecl).getCode(_factory);
				if (ancestorCode != null) {
					fragment.append(ancestorCode);
				}
			}
		}
		
		// Generate code for all class elements
		for (ClassElement element : this.elements) {
			Fragment elementCode = null;
			if (element instanceof MethodDeclaration) {
				elementCode = ((MethodDeclaration) element).getCode(_factory);
			} else if (element instanceof ConstructorDeclaration) {
				elementCode = ((ConstructorDeclaration) element).getCode(_factory);
			}
			if (elementCode != null) {
				fragment.append(elementCode);
			}
		}
		
		return fragment;
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

	public List<ClassElement> getElements() {
		return this.elements;
	}

	public String getAncestor() {
		return this.ancestor;
	}

	public HierarchicalScope<Declaration> getScope() {
		return this.scope;
	}

}
