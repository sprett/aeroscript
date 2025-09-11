package no.uio.aeroscript.runtime;

import no.uio.aeroscript.antlr.AeroScriptBaseVisitor;
import no.uio.aeroscript.antlr.AeroScriptParser;
import no.uio.aeroscript.ast.expr.Node;
import no.uio.aeroscript.ast.expr.NumberNode;
import no.uio.aeroscript.ast.expr.OperationNode;
import no.uio.aeroscript.type.Point;
import no.uio.aeroscript.type.Range;

public class Interpreter extends AeroScriptBaseVisitor<Object> {
    public Node visitStatement(AeroScriptParser.StatementContext ctx) {
        return (Node) visit(ctx);
    }

    public Node visitExpression(AeroScriptParser.ExpressionContext ctx) {
        return (Node) visit(ctx);
    }

    @Override
    public Node visitExprStmt(AeroScriptParser.ExprStmtContext ctx) {
        return (Node) visit(ctx.expression());
    }

    @Override
    public Node visitNumberExpr(AeroScriptParser.NumberExprContext ctx) {
        try {
            float value = Float.parseFloat(ctx.NUMBER().getText());
            return new NumberNode(value);
        } catch (NumberFormatException e) {
            throw new AeroScriptRuntimeException("Invalid number format: " + ctx.NUMBER().getText(), e);
        }
    }

    @Override
    public Node visitParenExpr(AeroScriptParser.ParenExprContext ctx) {
        return (Node) visit(ctx.expression());
    }

    @Override
    public Node visitPlusExpr(AeroScriptParser.PlusExprContext ctx) {
        Node left = (Node) visit(ctx.expression(0));
        Node right = (Node) visit(ctx.expression(1));
        return new OperationNode("PLUS", left, right);
    }

    @Override
    public Node visitMinusExpr(AeroScriptParser.MinusExprContext ctx) {
        Node left = (Node) visit(ctx.expression(0));
        Node right = (Node) visit(ctx.expression(1));
        return new OperationNode("MINUS", left, right);
    }

    @Override
    public Node visitTimesExpr(AeroScriptParser.TimesExprContext ctx) {
        Node left = (Node) visit(ctx.expression(0));
        Node right = (Node) visit(ctx.expression(1));
        return new OperationNode("TIMES", left, right);
    }

    @Override
    public Node visitNegExpr(AeroScriptParser.NegExprContext ctx) {
        Node expr = (Node) visit(ctx.expression());
        return new OperationNode("NEG", expr, null);
    }

    @Override
    public Node visitRandomExpr(AeroScriptParser.RandomExprContext ctx) {
        Range range = (Range) visitRange(ctx.range());
        // Create a dummy node to hold the range
        Node rangeNode = new Node() {
            @Override
            public Object evaluate() {
                return range;
            }
        };
        return new OperationNode("RANDOM", null, rangeNode);
    }

    @Override
    public Node visitPointExpr(AeroScriptParser.PointExprContext ctx) {
        Node x = (Node) visit(ctx.expression(0));
        Node y = (Node) visit(ctx.expression(1));
        return new OperationNode("POINT", x, y);
    }

    @Override
    public Range visitRange(AeroScriptParser.RangeContext ctx) {
        try {
            Node minNode = (Node) visit(ctx.expression(0));
            Node maxNode = (Node) visit(ctx.expression(1));

            if (minNode == null) {
                throw new AeroScriptRuntimeException("Min value is null in range");
            }
            if (maxNode == null) {
                throw new AeroScriptRuntimeException("Max value is null in range");
            }

            Object minValue = minNode.evaluate();
            Object maxValue = maxNode.evaluate();

            if (!(minValue instanceof Float)) {
                throw new AeroScriptRuntimeException(
                        "Min value must be a number, got: " + minValue.getClass().getSimpleName());
            }
            if (!(maxValue instanceof Float)) {
                throw new AeroScriptRuntimeException(
                        "Max value must be a number, got: " + maxValue.getClass().getSimpleName());
            }

            float min = (Float) minValue;
            float max = (Float) maxValue;

            if (min > max) {
                throw new AeroScriptRuntimeException("Range invalid: min (" + min + ") > max (" + max + ")");
            }

            return new Range(min, max);
        } catch (AeroScriptRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AeroScriptRuntimeException("Error creating range: " + e.getMessage(), e);
        }
    }
}
