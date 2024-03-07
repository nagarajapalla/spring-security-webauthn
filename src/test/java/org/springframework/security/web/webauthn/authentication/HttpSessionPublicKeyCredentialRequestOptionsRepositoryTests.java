/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.web.webauthn.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.webauthn.api.TestPublicKeyCredentialRequestOptions;
import org.springframework.security.webauthn.api.authentication.PublicKeyCredentialRequestOptions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HttpSessionPublicKeyCredentialRequestOptionsRepository}.
 *
 * @author Rob Winch
 * @since 6.3
 */
class HttpSessionPublicKeyCredentialRequestOptionsRepositoryTests {

	private HttpSessionPublicKeyCredentialRequestOptionsRepository repository = new HttpSessionPublicKeyCredentialRequestOptionsRepository();

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();


	@Test
	void integrationTests() {
		PublicKeyCredentialRequestOptions expected = TestPublicKeyCredentialRequestOptions
			.create()
			.build();
		this.repository.save(this.request, this.response, expected);
		Object attrValue = this.request.getSession().getAttribute(HttpSessionPublicKeyCredentialRequestOptionsRepository.DEFAULT_ATTR_NAME);
		assertThat(attrValue).isEqualTo(expected);
		PublicKeyCredentialRequestOptions loadOptions = this.repository.load(this.request);
		assertThat(loadOptions).isEqualTo(expected);

		this.repository.save(this.request, this.response, null);

		Object attrValueAfterRemoval = this.request.getSession().getAttribute(HttpSessionPublicKeyCredentialRequestOptionsRepository.DEFAULT_ATTR_NAME);
		assertThat(attrValueAfterRemoval).isNull();
		PublicKeyCredentialRequestOptions loadOptionsAfterRemoval = this.repository.load(this.request);
		assertThat(loadOptionsAfterRemoval).isNull();
	}

}