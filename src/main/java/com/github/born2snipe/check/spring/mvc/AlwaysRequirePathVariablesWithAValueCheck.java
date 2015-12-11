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

import com.github.born2snipe.check.AbstractCheck;
import com.github.born2snipe.check.AstNodeLocator;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class AlwaysRequirePathVariablesWithAValueCheck extends AbstractCheck {
    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.ANNOTATION};
    }

    @Override
    public void visitToken(DetailAST ast) {
        if (isPathVariable(ast) && isValueNotProvided(ast)) {
            log(ast.getLineNo(), "annotation.value.is.always.required", "PathVariable", additionalMessageOnViolation);
        }
    }

    private boolean isValueNotProvided(DetailAST ast) {
        DetailAST expression = AstNodeLocator.findExpression(ast);
        if (expression != null && expression.getChildCount() > 0) {
            return AstNodeLocator.findStringLiteralText(expression).replace("\"", "").trim().length() == 0;
        }

        DetailAST memberValuePair = AstNodeLocator.findAnnotationMemberValuePair(ast);
        if (memberValuePair != null) {
            return isValueNotProvided(memberValuePair);
        }

        return true;
    }

    private boolean isPathVariable(DetailAST ast) {
        return "PathVariable".equals(AstNodeLocator.findIdentifierText(ast));
    }
}
