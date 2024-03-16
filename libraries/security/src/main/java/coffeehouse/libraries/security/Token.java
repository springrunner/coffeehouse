package coffeehouse.libraries.security;

import coffeehouse.libraries.security.authentication.jwt.Auth0JSONWebToken;

/**
 * @author springrunner.kr@gmail.com
 */
public sealed class Token permits PlainToken, Auth0JSONWebToken {
}
