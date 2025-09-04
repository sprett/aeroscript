package no.uio.aeroscript.runtime;

public class AeroScriptRuntimeException extends RuntimeException {
    public AeroScriptRuntimeException(String message) {
        super(message);
    }

    public AeroScriptRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
