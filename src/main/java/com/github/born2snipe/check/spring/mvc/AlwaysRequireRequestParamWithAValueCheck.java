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

import java.util.Collection;

public class AlwaysRequireRequestParamWithAValueCheck extends AbstractCheck {
    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.ANNOTATION};
    }

    @Override
    public void visitToken(DetailAST ast) {
        if (isRequestParam(ast) && isValueAndNameMissing(ast)) {
            log(ast.getLineNo(), ast.getColumnNo(), "annotation.value.is.always.required", "RequestParam", additionalMessageOnViolation);
        }
    }

    private boolean isValueAndNameMissing(DetailAST ast) {
        DetailAST expression = AstNodeLocator.findExpression(ast);
        if (expression != null && expression.getChildCount() > 0) {
            return AstNodeLocator.findStringLiteralText(expression).replace("\"", "").trim().length() == 0;
        }

        Collection<DetailAST> allAnnotationMemberValuePairs = AstNodeLocator.findAllAnnotationMemberValuePair(ast);
        for (DetailAST memberValuePair : allAnnotationMemberValuePairs) {
            String identifierText = AstNodeLocator.findIdentifierText(memberValuePair);
            if (isValueOrName(identifierText)) {
                return isValueAndNameMissing(memberValuePair);
            }
        }

        return true;
    }

    private boolean isValueOrName(String identifierText) {
        return "value".equals(identifierText) || "name".equals(identifierText);
    }

    private boolean isRequestParam(DetailAST ast) {
        return "RequestParam".equals(AstNodeLocator.findIdentifierText(ast));
    }
}
