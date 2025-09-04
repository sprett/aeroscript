package no.uio.aeroscript.runtime;

import no.uio.aeroscript.antlr.AeroScriptLexer;
import no.uio.aeroscript.antlr.AeroScriptParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    private AeroScriptParser.ExpressionContext parseExpression(String expression) {
        AeroScriptLexer lexer = new AeroScriptLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AeroScriptParser parser = new AeroScriptParser(tokens);
        return parser.expression();
    }

    @Test
    void visitExpression() {
        Interpreter interpreter = new Interpreter();
        assertEquals(5.0f, Float.parseFloat(interpreter.visitExpression(parseExpression("2 + 3")).evaluate().toString()));
        assertEquals(-1.0f, Float.parseFloat(interpreter.visitExpression(parseExpression("2 - 3")).evaluate().toString()));
        assertEquals(6.0f, Float.parseFloat(interpreter.visitExpression(parseExpression("2 * 3")).evaluate().toString()));
        assertEquals(-1, Float.parseFloat(interpreter.visitExpression(parseExpression("-- 1")).evaluate().toString()));
    }
}
