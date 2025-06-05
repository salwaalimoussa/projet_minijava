/**
 * 
 */
package fr.n7.stl.minic.ast.expression.assignable;

import fr.n7.stl.minic.ast.expression.Expression;

/**
 * Expression whose value can be modified : Arrays, Records, Pointers, Variable,
 * ...
 * 
 * @author Marc Pantel
 *
 */
public interface AssignableExpression extends Expression {
    // on a ajoute cela pour que la classe assignable fonctionne pour le tp2 et on a
    // modifie le fichier variableassignable aussi
    String getName();
}
