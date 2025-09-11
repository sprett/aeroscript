package no.uio.aeroscript.ast.expr;

import no.uio.aeroscript.runtime.AeroScriptRuntimeException;
import no.uio.aeroscript.type.Point;
import no.uio.aeroscript.type.Range;

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
        try {
            switch (operation) {
                case "PLUS":
                    return evaluateArithmeticOperation(left, right, (a, b) -> a + b, "+");
                case "MINUS":
                    return evaluateArithmeticOperation(left, right, (a, b) -> a - b, "-");
                case "TIMES":
                    return evaluateArithmeticOperation(left, right, (a, b) -> a * b, "*");
                case "NEG":
                    return evaluateNegation(left);
                case "RANDOM":
                    return evaluateRandom(left, right);
                case "POINT":
                    return evaluatePoint(left, right);
                default:
                    throw new AeroScriptRuntimeException("Undefined operation: '" + operation + "'");
            }
        } catch (AeroScriptRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AeroScriptRuntimeException("Error evaluating operation '" + operation + "': " + e.getMessage(),
                    e);
        }
    }

    private Float evaluateArithmeticOperation(Node left, Node right, ArithmeticOperation op, String operator) {
        if (left == null) {
            throw new AeroScriptRuntimeException("Left operand is null for operation '" + operator + "'");
        }
        if (right == null) {
            throw new AeroScriptRuntimeException("Right operand is null for operation '" + operator + "'");
        }

        Object leftValue = left.evaluate();
        Object rightValue = right.evaluate();

        if (!(leftValue instanceof Float)) {
            throw new AeroScriptRuntimeException(
                    "Left operand must be a number, got: " + leftValue.getClass().getSimpleName());
        }
        if (!(rightValue instanceof Float)) {
            throw new AeroScriptRuntimeException(
                    "Right operand must be a number, got: " + rightValue.getClass().getSimpleName());
        }

        return op.apply((Float) leftValue, (Float) rightValue);
    }

    private Float evaluateNegation(Node expr) {
        if (expr == null) {
            throw new AeroScriptRuntimeException("Expression is null for negation operation");
        }

        Object value = expr.evaluate();
        if (!(value instanceof Float)) {
            throw new AeroScriptRuntimeException(
                    "Negation operand must be a number, got: " + value.getClass().getSimpleName());
        }

        return (Float) value * -1;
    }

    private Float evaluateRandom(Node left, Node right) {
        if (right != null) {
            Object rightValue = right.evaluate();
            if (rightValue instanceof Range) {
                Range range = (Range) rightValue;
                return range.getRandomValue();
            } else {
                if (left == null) {
                    throw new AeroScriptRuntimeException("Left operand is null for RANDOM operation");
                }
                Object leftValue = left.evaluate();
                if (!(leftValue instanceof Float)) {
                    throw new AeroScriptRuntimeException("Left operand must be a number for RANDOM operation, got: "
                            + leftValue.getClass().getSimpleName());
                }
                if (!(rightValue instanceof Float)) {
                    throw new AeroScriptRuntimeException("Right operand must be a number for RANDOM operation, got: "
                            + rightValue.getClass().getSimpleName());
                }
                float min = (Float) leftValue;
                float max = (Float) rightValue;
                if (min > max) {
                    throw new AeroScriptRuntimeException("RANDOM range invalid: min (" + min + ") > max (" + max + ")");
                }
                return min + (float) Math.random() * (max - min);
            }
        }
        throw new AeroScriptRuntimeException("RANDOM operation requires a range");
    }

    private Point evaluatePoint(Node x, Node y) {
        if (x == null) {
            throw new AeroScriptRuntimeException("X coordinate is null for POINT operation");
        }
        if (y == null) {
            throw new AeroScriptRuntimeException("Y coordinate is null for POINT operation");
        }

        Object xValue = x.evaluate();
        Object yValue = y.evaluate();

        if (!(xValue instanceof Float)) {
            throw new AeroScriptRuntimeException(
                    "X coordinate must be a number, got: " + xValue.getClass().getSimpleName());
        }
        if (!(yValue instanceof Float)) {
            throw new AeroScriptRuntimeException(
                    "Y coordinate must be a number, got: " + yValue.getClass().getSimpleName());
        }

        return new Point((Float) xValue, (Float) yValue);
    }

    @FunctionalInterface
    private interface ArithmeticOperation {
        Float apply(Float a, Float b);
    }
}
