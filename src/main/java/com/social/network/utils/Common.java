package com.social.network.utils;

import com.social.network.exception.UserNotAuthorizedToExecuteThisActionException;
import com.social.network.utils.constants.ErrorMessages;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class Common {

    private Common() {
    }
    public static <T> boolean isNullOrEmpty(T object) {
        if (object == null) {
            return true;
        } else if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        } else if (object instanceof Object[]) {
            return ((Object[]) object).length == 0;
        } else if (object instanceof Optional) {
            return !((Optional<?>) object).isPresent();
        } else {
            // For custom objects, check if it implements isEmpty() method
            try {
                return (boolean) object.getClass().getMethod("isEmpty").invoke(object);
            } catch (Exception e) {
                // If isEmpty() method is not implemented or any exception occurs, just check for null
                return false;
            }
        }
    }

    public static void isUserAuthorizedToExecuteThisAction(Long userId, Long userFromObjectId) {
        if (!userId.equals(userFromObjectId)) {
            throw new UserNotAuthorizedToExecuteThisActionException(ErrorMessages.USER_NOT_AUTHORIZED);
        }
    }

}
