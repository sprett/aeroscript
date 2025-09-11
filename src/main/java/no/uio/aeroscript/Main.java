package no.uio.aeroscript;

import no.uio.aeroscript.antlr.AeroScriptLexer;
import no.uio.aeroscript.antlr.AeroScriptParser;
import no.uio.aeroscript.ast.expr.Node;
import no.uio.aeroscript.runtime.Interpreter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java Main <expression_file>");
            System.exit(1);
        }

        String filename = args[0];
        try {
            String content = Files.readString(Paths.get(filename));
            String[] lines = content.split("\n");

            Interpreter interpreter = new Interpreter();

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                try {
                    AeroScriptLexer lexer = new AeroScriptLexer(CharStreams.fromString(line));
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    AeroScriptParser parser = new AeroScriptParser(tokens);

                    Node result = (Node) interpreter.visit(parser.statement());
                        System.out.println(line + " = " + result.evaluate());
                        result.evaluate(); // PrintNode handles its own output
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }
}