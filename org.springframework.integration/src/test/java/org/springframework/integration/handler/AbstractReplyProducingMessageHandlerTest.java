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
package org.springframework.integration.handler;

import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.integration.core.Message;
import org.springframework.integration.core.MessageChannel;
import org.springframework.integration.core.MessagingException;
import org.springframework.integration.message.MessageBuilder;

/**
 * @author Iwein Fuld
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class AbstractReplyProducingMessageHandlerTest {

	private AbstractReplyProducingMessageHandler handler = new AbstractReplyProducingMessageHandler() {
		@Override
		protected void handleRequestMessage(Message<?> requestMessage, ReplyMessageHolder replyMessageHolder) {
			replyMessageHolder.add(requestMessage);
		}
	};
	private Message<?> message = MessageBuilder.withPayload("test").build();
	@Mock
	private MessageChannel channel = null;

	@Test
	public void errorMessageShouldContainChannelName() {
		handler.setOutputChannel(channel);
		when(channel.send(message)).thenReturn(false);
		when(channel.toString()).thenReturn("testChannel");
		try {
			handler.handleMessage(message);
			fail("Expected a MessagingException");
		} catch (MessagingException e) {
			assertThat(e.getMessage(), JUnitMatchers.containsString("'testChannel'"));
		}
	}
}
