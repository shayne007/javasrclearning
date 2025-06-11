package com.feng.jdk.annotation.testcase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Programmers annotate each method or set of methods which fulfill the requirements of a particular
 * use case.
 * </p>
 * <p>A project manager can get an idea of project progress by counting the implemented use cases,
 * and developers maintaining the project can easily find use cases if they need to update or debug
 * business rules within the system.
 * </p>
 *
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class UseCaseTracker {
    public static void trackUseCases(List<Integer> useCases, Class<?> cl) {
        for (Method method : cl.getMethods()) {
            UseCase useCase = method.getDeclaredAnnotation(UseCase.class);
            if (useCase != null) {
                System.out.println("Found Use Case1: " + useCase.id() + " " + useCase.description());
            }
        }
        for (Method m : cl.getDeclaredMethods()) {
            UseCase uc = m.getAnnotation(UseCase.class);
            if (uc != null) {
                System.out.println("Found Use Case:" + uc.id() +
                        " " + uc.description());
                useCases.remove(new Integer(uc.id()));
            }
        }
        for (int i : useCases) {
            System.out.println("Warning: Missing use case-" + i);
        }
    }

    public static void main(String[] args) {
        List<Integer> useCases = new ArrayList<Integer>();
        Collections.addAll(useCases, 47, 48, 49, 50);
        trackUseCases(useCases, PasswordUtils.class);
    }
}
