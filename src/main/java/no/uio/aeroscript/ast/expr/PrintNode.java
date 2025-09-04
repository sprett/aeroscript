package no.uio.aeroscript.ast.expr;

public class PrintNode extends Node {
    private final Node expression;

    public PrintNode(Node expression) {
        this.expression = expression;
    }

    @Override
    public Object evaluate() {
        Object result = expression.evaluate();
        System.out.println(result);
        return result;
    }

    public Node getExpression() {
        return expression;
    }
}
