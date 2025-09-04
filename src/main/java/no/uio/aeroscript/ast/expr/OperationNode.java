package no.uio.aeroscript.ast.expr;

public class OperationNode extends Node {
    private final String operation;
    private final Node left;
    private final Node right;

    public OperationNode(String operation, Node left, Node right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    @Override
    public Object evaluate() {
        // Implement method to evaluate the various expressions
    }
}
