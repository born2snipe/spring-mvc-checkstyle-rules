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
package com.github.born2snipe.check.spring.mvc;

import com.github.born2snipe.check.AbstractCheckStyleTestCase;
import com.github.born2snipe.controllers.pathvar.HasPathVariableValueProvidedController;
import com.github.born2snipe.controllers.pathvar.HasPathVariableWithAnEmptyValueProvidedController;
import com.github.born2snipe.controllers.pathvar.HasPathVariableWithNoValueProvidedController;
import com.github.born2snipe.controllers.pathvar.HasPathVariableWithValueTypedOutAndValueProvidedController;
import org.junit.Test;

import static com.github.born2snipe.ProjectInfo.getJavaFileFor;

public class AlwaysRequirePathVariablesWithAValueCheckTest extends AbstractCheckStyleTestCase {
    private static final String MESSAGE = "Please provide a value for the @PathVariable";

    @Override
    protected String getConfigFile() {
        return "config/spring/mvc/always-require-path-variables-with-a-value.xml";
    }

    @Test
    public void shouldRequireProvidingAValueInTheValueProperty() {
        assertViolationExistsFor(getJavaFileFor(HasPathVariableWithNoValueProvidedController.class), MESSAGE);
    }

    @Test
    public void shouldRequirePuttingAValueInTheValueProperty() {
        assertViolationExistsFor(getJavaFileFor(HasPathVariableWithAnEmptyValueProvidedController.class), MESSAGE);
    }

    @Test
    public void shouldFindNoViolationsWhenTheValueIsProvided() {
        assertThereAreNoViolationsIn(getJavaFileFor(HasPathVariableValueProvidedController.class));
    }

    @Test
    public void shouldNotCareIfTheValuePropertyIsTypedOut() {
        assertThereAreNoViolationsIn(getJavaFileFor(HasPathVariableWithValueTypedOutAndValueProvidedController.class));
    }

}