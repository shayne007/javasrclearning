package com.feng.annotation.genextfile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class TableCreator {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("arguments: annotated classes");
            System.exit(0);
        }
        for (String className : args) {
            Class<?> cl = Class.forName(className);
            DBTable dbTable = cl.getAnnotation(DBTable.class);
            if (dbTable == null) {
                System.out.println("No DBTable annotations in class " + className);
                continue;
            }
            String tableName = dbTable.name();
            // If the name is empty, use the Class name:
            if (tableName.length() < 1) {
                tableName = cl.getName().toUpperCase();
            }
            List<String> columnDefs = new ArrayList<String>();
            for (Field field : cl.getDeclaredFields()) {
                final String[] columnName = {null};
                Annotation[] anns = field.getDeclaredAnnotations();
                if (anns.length < 1) {
                    continue; // Not a db table column
                }
                Arrays.asList(anns).stream().filter(t -> t instanceof SQLInteger).forEach(annotation -> {
                    SQLInteger sInt = (SQLInteger)annotation;
                    // Use field name if name not specified
                    if (sInt.name().length() < 1) {
                        columnName[0] = field.getName().toUpperCase();
                    } else {
                        columnName[0] = sInt.name();
                    }
                    columnDefs.add(columnName[0] + " INT" + getConstraints(sInt.constraints()));
                });
                Arrays.asList(anns).stream().filter(t -> t instanceof SQLString).forEach(annotation -> {
                    SQLString sString = (SQLString)annotation;
                    // Use field name if name not specified
                    if (sString.name().length() < 1) {
                        columnName[0] = field.getName().toUpperCase();
                    } else {
                        columnName[0] = sString.name();
                    }
                    columnDefs.add(
                        columnName[0] + " VARCHAR(" + sString.value() + ")" + getConstraints(sString.constraints()));
                });
            }
            StringBuilder createCommand = new StringBuilder("CREATE TABLE " + tableName + "(");
            for (String columnDef : columnDefs) {
                createCommand.append("\n    " + columnDef + ",");
            }
            // Remove trailing comma
            String tableCreate = createCommand.substring(0, createCommand.length() - 1) + ");";
            System.out.println("Table Creation SQL for " + className + " is :\n" + tableCreate);
        }
    }

    private static String getConstraints(Constraints con) {
        String constraints = "";
        if (!con.allowNull()) {
            constraints += " NOT NULL";
        }
        if (con.primaryKey()) {
            constraints += " PRIMARY KEY";
        }
        if (con.unique()) {
            constraints += " UNIQUE";
        }
        return constraints;
    }
}
