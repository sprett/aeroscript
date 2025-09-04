package no.uio.aeroscript.ast.expr;

public class NumberNode extends Node {
    private final float value;

    public NumberNode(float value) {
        this.value = value;
    }

    @Override
    public Object evaluate() {
        return value;
    }
}
