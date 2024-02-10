package coffeehouse.libraries.security;

/**
 * @author springrunner.kr@gmail.com
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Unauthorized access: You must be authenticated to access this resources");
    }
}
