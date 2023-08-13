package com.klbstore.security.registration;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String t) {
        // TODO: Regex to validate email
        final Pattern p = Pattern.compile(
            "/^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})*$/");
        Matcher m = p.matcher(t);
        Boolean b = m.matches();
        return b;
    }

}
