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

import com.github.born2snipe.ProjectInfo;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public abstract class AbstractCheckStyleTestCase {

    protected abstract String getConfigFile();

    @Test
    public void shouldNotSeeAnythingWrongWithAClassWithNoAnnotations() {
        assertThereAreNoViolationsIn(ProjectInfo.getJavaFileFor(CheckStyleExecutor.class));
    }

    protected void assertViolationExistsFor(File javaFile, String... expectedMessages) {
        assertEquals(Arrays.asList(expectedMessages), CheckStyleExecutor.executeWithOnlyMessages(getConfigFile(), javaFile));
    }

    protected void assertThereAreNoViolationsIn(File javaFile) {
        assertEquals(Collections.emptyList(), CheckStyleExecutor.executeWithOnlyMessages(getConfigFile(), javaFile));
    }
}
