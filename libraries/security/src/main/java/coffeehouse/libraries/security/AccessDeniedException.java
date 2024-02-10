package coffeehouse.libraries.security;

/**
 * @author springrunner.kr@gmail.com
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("Access denied: insufficient permissions to access the resources");
    }
}
