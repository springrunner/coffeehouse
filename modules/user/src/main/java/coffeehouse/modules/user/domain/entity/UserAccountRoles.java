package coffeehouse.modules.user.domain.entity;

import coffeehouse.libraries.base.lang.IterableItem;

import java.util.*;

/**
 * @author springrunner.kr@gmail.com
 */
public class UserAccountRoles implements IterableItem<UserAccountRole> {

    private final Set<UserAccountRole> roles;

    UserAccountRoles(Set<UserAccountRole> roles) {
        this.roles = Objects.requireNonNull(roles, "roles must not be null");
    }

    @Override
    public int size() {
        return roles.size();
    }

    @Override
    public boolean isEmpty() {
        return roles.isEmpty();
    }

    @Override
    public Iterator<UserAccountRole> iterator() {
        return roles.iterator();
    }

    public static UserAccountRoles wrap(UserAccountRole...roles) {
        return wrap(new HashSet<>(Arrays.asList(roles)));
    }

    public static UserAccountRoles wrap(Set<UserAccountRole> roles) {
        return new UserAccountRoles(Objects.requireNonNullElse(roles, Collections.emptySet()));
    }    
}
