package no.uio.aeroscript.runtime;

import no.uio.aeroscript.antlr.AeroScriptBaseVisitor;
import no.uio.aeroscript.antlr.AeroScriptParser;
import no.uio.aeroscript.ast.expr.Node;

public class Interpreter extends AeroScriptBaseVisitor<Object> {
    @Override
    public Node visitExpression(AeroScriptParser.ExpressionContext ctx) {
        // Add implementation here
    }
}
