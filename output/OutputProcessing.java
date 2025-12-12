package com.skeletoncoder.project.output;


import com.skeletoncoder.project.input.Attribute;
import com.skeletoncoder.project.input.Method;
import com.skeletoncoder.project.input.ParsedResult;

public class OutputProcessing {

    public static String generateCode(ParsedResult parsedResult) {
        StringBuilder sb = new StringBuilder();

        if (parsedResult.typeKeyword.equals("interface")) {
            sb.append("public interface ").append(parsedResult.className).append(" {\n\n");
        } else {
            sb.append("public ");
            if (parsedResult.classType.equals("abstract")) {
                sb.append("abstract ");
            }
            sb.append("class ").append(parsedResult.className).append(" {\n\n");
        }

        boolean hasAttributes = !parsedResult.attributes.isEmpty();

        if (!parsedResult.typeKeyword.equals("interface") && hasAttributes) {

            for (Attribute attr : parsedResult.attributes) {
                sb.append("    private ").append(attr.type).append(" ").append(attr.name).append(";\n");
            }
            sb.append("\n");

            sb.append("    public ").append(parsedResult.className).append("(");
            for (int i = 0; i < parsedResult.attributes.size(); i++) {
                Attribute attr = parsedResult.attributes.get(i);
                sb.append(attr.type).append(" ").append(attr.name);
                if (i != parsedResult.attributes.size() - 1) sb.append(", ");
            }
            sb.append(") {\n");
            for (Attribute attr : parsedResult.attributes) {
                sb.append("        this.").append(attr.name).append(" = ").append(attr.name).append(";\n");
            }
            sb.append("    }\n\n");

            for (Attribute attr : parsedResult.attributes) {
                sb.append("    public ").append(attr.type).append(" get")
                        .append(capitalize(attr.name)).append("() {\n")
                        .append("        return ").append(attr.name).append(";\n")
                        .append("    }\n\n");

                sb.append("    public void set").append(capitalize(attr.name))
                        .append("(").append(attr.type).append(" ").append(attr.name).append(") {\n")
                        .append("        this.").append(attr.name).append(" = ").append(attr.name).append(";\n")
                        .append("    }\n\n");
            }
        }

        for (Method m : parsedResult.methods) {
            String params = (m.params == null ? "" : m.params);

            if (parsedResult.typeKeyword.equals("interface")) {
                sb.append("    ").append(m.returnType)
                        .append(" ").append(m.name)
                        .append("(").append(params).append(");\n\n");
            } else if (parsedResult.classType.equals("abstract")) {
                sb.append("    public abstract ")
                        .append(m.returnType).append(" ").append(m.name)
                        .append("(").append(params).append(");\n\n");
            } else {
                sb.append("    public ").append(m.returnType).append(" ").append(m.name)
                        .append("(").append(params).append(") {\n")
                        .append("    }\n\n");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}

