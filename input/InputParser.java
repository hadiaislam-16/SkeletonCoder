package com.skeletoncoder.project.input;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    public static ParsedResult validate(String input) {
        ParsedResult result = new ParsedResult();

        Pattern classPattern = Pattern.compile(
                "Create\\s+(?:a|an)?\\s*(?:(abstract|concrete)\\s+)?(class|interface)\\s+named\\s+([A-Z][A-Za-z0-9_]*)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher classMatcher = classPattern.matcher(input);
        if (!classMatcher.find()) {
            throw new IllegalArgumentException("Class type or name missing");
        }
        String classType = classMatcher.group(1);
        result.classType = (classType != null) ? classType.toLowerCase() : "concrete";
        result.typeKeyword = classMatcher.group(2).toLowerCase();
        result.className = classMatcher.group(3);


        Pattern methodSplitPattern = Pattern.compile("\\s+and\\s+has\\s+(?:a\\s+)?methods?|method\\s+", Pattern.CASE_INSENSITIVE);
        String[] parts = methodSplitPattern.split(input, 2);
        String attrSection = parts[0].trim();
        String methodSection = (parts.length > 1) ? parts[1].trim() : null;


        result.attributes = new ArrayList<>();
        Pattern attrPattern = Pattern.compile("has\\s+(.+)$", Pattern.CASE_INSENSITIVE);
        Matcher attrMatcher = attrPattern.matcher(attrSection);
        if (attrMatcher.find()) {
            String attrText = attrMatcher.group(1).trim();
            String[] attrParts = attrText.split("\\s*(?:and|,)\\s*");
            for (String attr : attrParts) {
                Matcher mAttr = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*)\\s*[:;]\\s*([A-Za-z0-9_<>]+)").matcher(attr.trim());
                if (mAttr.find()) {
                    result.attributes.add(new Attribute(mAttr.group(1), mAttr.group(2)));
                } else {
                    throw new IllegalArgumentException("Invalid attribute format: " + attr);
                }
            }
        }


//        if (!result.typeKeyword.equals("interface") && result.attributes.isEmpty()) {
//            throw new IllegalArgumentException("At least one attribute is required for concrete or abstract class.");
//        }


        result.methods = new ArrayList<>();
        if (methodSection != null && !methodSection.isEmpty()) {

            String[] methodParts = methodSection.split("\\s+and\\s+");

            for (String m : methodParts) {
                Matcher mMethod = Pattern.compile(
                        "([a-zA-Z_][a-zA-Z0-9_]*)\\((.*?)\\)\\s*[:;]\\s*([A-Za-z0-9_<>]+)"
                ).matcher(m.trim());
                if (mMethod.find()) {
                    result.methods.add(new Method(
                            mMethod.group(1),
                            mMethod.group(3),
                            mMethod.group(2)
                    ));

                } else {
                    throw new IllegalArgumentException("Method format invalid: " + m);
                }
            }
        }


        if ((result.classType.equals("abstract")) && result.methods.isEmpty()) {
            throw new IllegalArgumentException("Abstract class or interface must have at least one method.");
        }

        return result;
    }
}
