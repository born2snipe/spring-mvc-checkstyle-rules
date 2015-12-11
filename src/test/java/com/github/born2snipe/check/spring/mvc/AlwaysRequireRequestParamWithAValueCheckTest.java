/**
 * Copyright to the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.github.born2snipe.check.spring.mvc;

import com.github.born2snipe.check.AbstractCheckStyleTestCase;
import com.github.born2snipe.controllers.requestparam.HasARequiredRequestParamWithNoValueAndNoNameController;
import com.github.born2snipe.controllers.requestparam.HasRequestParamWithANameController;
import com.github.born2snipe.controllers.requestparam.HasRequestParamWithAValueController;
import com.github.born2snipe.controllers.requestparam.HasRequestParamWithAValueExplicityController;
import com.github.born2snipe.controllers.requestparam.HasRequestParamWithAnEmptyNameController;
import com.github.born2snipe.controllers.requestparam.HasRequestParamWithAnEmptyValueController;
import com.github.born2snipe.controllers.requestparam.HasRequestParamWithNoValueAndNoNameController;
import com.github.born2snipe.controllers.requestparam.HasRequiredRequestParamWithAValueController;
import com.github.born2snipe.controllers.requestparam.HasRequiredRequestParamWithAnEmptyValueController;
import org.junit.Test;

import static com.github.born2snipe.ProjectInfo.getJavaFileFor;

public class AlwaysRequireRequestParamWithAValueCheckTest extends AbstractCheckStyleTestCase {
    private static final String MESSAGE = "Please provide a value for the @RequestParam";

    @Override
    protected String getConfigFile() {
        return "config/spring/mvc/always-require-request-param-with-a-value.xml";
    }

    @Test
    public void shouldNotFailIfTheValueProvidedIsNotBlankAndTheParameterIsMarkedAsRequired() {
        assertThereAreNoViolationsIn(getJavaFileFor(HasRequiredRequestParamWithAValueController.class));
    }

    @Test
    public void shouldFailIfTheValueProvidedIsBlankAndTheParameterIsMarkedAsRequired() {
        assertViolationExistsFor(getJavaFileFor(HasRequiredRequestParamWithAnEmptyValueController.class), MESSAGE);
    }

    @Test
    public void shouldFailIfTheValueProvidedIsBlank() {
        assertViolationExistsFor(getJavaFileFor(HasRequestParamWithAnEmptyValueController.class), MESSAGE);
    }

    @Test
    public void shouldFailIfTheNameProvidedIsBlank() {
        assertViolationExistsFor(getJavaFileFor(HasRequestParamWithAnEmptyNameController.class), MESSAGE);
    }

    @Test
    public void shouldFailIfTheOnlyProvidedIsRequired() {
        assertViolationExistsFor(getJavaFileFor(HasARequiredRequestParamWithNoValueAndNoNameController.class), MESSAGE);
    }

    @Test
    public void shouldRequireANameOrValue() {
        assertViolationExistsFor(getJavaFileFor(HasRequestParamWithNoValueAndNoNameController.class), MESSAGE);
    }

    @Test
    public void shouldNotFailIfANameIsProvided() {
        assertThereAreNoViolationsIn(getJavaFileFor(HasRequestParamWithANameController.class));
    }

    @Test
    public void shouldNotFailIfAValueIsProvided() {
        assertThereAreNoViolationsIn(getJavaFileFor(HasRequestParamWithAValueController.class));
    }

    @Test
    public void shouldNotFailIfAValueIsProvidedExplicitly() {
        assertThereAreNoViolationsIn(getJavaFileFor(HasRequestParamWithAValueExplicityController.class));
    }
}