/**
 *
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.github.born2snipe.check;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CheckStyleExecutor {
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^(.+):([0-9]+): ([a-zA-Z]+): (.+)$");
    private static final Pattern MESSAGE_PATTERN_WITH_COLUMN = Pattern.compile("^(.+):([0-9]+):([0-9]+): ([a-zA-Z]+): (.+)$");

    public static List<String> executeWithOnlyMessages(String configFile, File... javaFiles) {
        return execute(configFile, javaFiles).stream()
                .map((v) -> v.message)
                .collect(Collectors.toList());
    }

    public static List<Violation> execute(String configFile, File... javaFiles) {
        return execute(configFile, Arrays.asList(javaFiles));
    }

    public static List<Violation> execute(String configFile, List<File> javaFiles) {
        Checker checker = new Checker();
        try {
            ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

            Configuration config = ConfigurationLoader.loadConfiguration(
                    new InputSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile)),
                    null,
                    false
            );

            Locale locale = Locale.ROOT;
            checker.setLocaleCountry(locale.getCountry());
            checker.setLocaleLanguage(locale.getLanguage());
            checker.setModuleClassLoader(Thread.currentThread().getContextClassLoader());
            checker.configure(config);
            checker.addListener(new BriefLogger(capturedOutput));
            checker.process(javaFiles);

            return parseErrorsFrom(new String(capturedOutput.toByteArray(), StandardCharsets.UTF_8));
        } catch (CheckstyleException e) {
            throw new RuntimeException(e);
        } finally {
            checker.destroy();
        }
    }

    private static List<Violation> parseErrorsFrom(String content) {
        ArrayList<Violation> violations = new ArrayList<>();

        for (String line : content.split("\n")) {
            Optional<Violation> violation = parseViolationFrom(line);
            if (violation.isPresent()) {
                violations.add(violation.get());
            }
        }

        return violations;
    }

    private static Optional<Violation> parseViolationFrom(String line) {
        Violation violation = null;
        Matcher matcher = MESSAGE_PATTERN_WITH_COLUMN.matcher(line);
        if (matcher.find()) {
            violation = new Violation(matcher.group(1), Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(3)), matcher.group(5), parseSeverity(matcher.group(4)));
        } else {
            matcher = MESSAGE_PATTERN.matcher(line);
            if (matcher.find()) {
                violation = new Violation(matcher.group(1), Integer.valueOf(matcher.group(2)), 0, matcher.group(4), parseSeverity(matcher.group(3)));
            }
        }
        return Optional.ofNullable(violation);
    }

    private static SeverityLevel parseSeverity(String value) {
        return SeverityLevel.valueOf(value.toUpperCase());
    }

    private static class BriefLogger extends DefaultLogger {
        public BriefLogger(OutputStream out) {
            super(out, true, out, true, true);
        }
    }

    public static class Violation {
        public final String filename;
        public final int lineNumber;
        public final int columnNumber;
        public final String message;
        public final SeverityLevel severityLevel;


        public Violation(String filename, int lineNumber, int columnNumber, String message, SeverityLevel severityLevel) {
            this.filename = filename;
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
            this.message = message;
            this.severityLevel = severityLevel;
        }
    }
}
