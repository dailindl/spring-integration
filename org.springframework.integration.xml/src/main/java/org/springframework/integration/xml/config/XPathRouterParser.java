/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.xml.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.ConfigurationException;
import org.springframework.integration.xml.router.XPathMultiChannelNameResolver;
import org.springframework.integration.xml.router.XPathSingleChannelNameResolver;
import org.springframework.util.StringUtils;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;

/**
 * @author Jonas Partner
 */
public class XPathRouterParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected boolean shouldGenerateId() {
		return false;
	}

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		boolean multiChannel = Boolean.parseBoolean(element.getAttribute("multi-channel"));
		String xPathExpression = element.getAttribute("xpath-expression");
		String xPathExpressionRef = element.getAttribute("xpath-expression-ref");
		if ((StringUtils.hasText(xPathExpression) && StringUtils.hasText(xPathExpressionRef))
				|| (!StringUtils.hasText(xPathExpression) && !StringUtils.hasText(xPathExpressionRef))) {
			throw new ConfigurationException("Exactly one of 'xpath-expression' or 'xpath-expression-ref' is required.");
		}
		if (multiChannel) {
			builder.getBeanDefinition().setBeanClass(XPathMultiChannelNameResolver.class);
		}
		else {
			builder.getBeanDefinition().setBeanClass(XPathSingleChannelNameResolver.class);
		}
		if (StringUtils.hasText(xPathExpression)) {
			XPathExpression expression = XPathExpressionFactory.createXPathExpression(xPathExpression);
			builder.addConstructorArgValue(expression);
		}
		else {
			builder.addConstructorArgReference(xPathExpressionRef);
		}
	}

}
