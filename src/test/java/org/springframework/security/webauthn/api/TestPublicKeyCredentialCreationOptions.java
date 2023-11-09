/*
 * Copyright 2002-2023 the original author or authors.
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

package org.springframework.security.webauthn.api;

import org.springframework.security.webauthn.api.core.BufferSource;
import org.springframework.security.webauthn.api.registration.*;

import java.time.Duration;

public class TestPublicKeyCredentialCreationOptions {
	public static PublicKeyCredentialCreationOptions.PublicKeyCredentialCreationOptionsBuilder createPublicKeyCredentialCreationOptions() {

		AuthenticatorSelectionCriteria authenticatorSelection = AuthenticatorSelectionCriteria.builder()
				.userVerification(UserVerificationRequirement.PREFERRED)
				.residentKey(ResidentKeyRequirement.DISCOURAGED)
				.build();
		BufferSource challenge = BufferSource.fromBase64("IBQnuY1Z0K1HqBoFWCp2xlJl8-oq_aFIXzyT_F0-0GU");
		PublicKeyCredentialRpEntity rp = PublicKeyCredentialRpEntity.builder()
				.id("localhost")
				.name("SimpleWebAuthn Example")
				.build();
		BufferSource userId = BufferSource.fromBase64("oWJtkJ6vJ_m5b84LB4_K7QKTCTEwLIjCh4tFMCGHO4w");
		PublicKeyCredentialUserEntity userEntity = PublicKeyCredentialUserEntity.builder()
				.displayName("user@localhost")
				.id(userId)
				.name("user@localhost")
				.build();
		DefaultAuthenticationExtensionsClientInputs clientInputs = new DefaultAuthenticationExtensionsClientInputs();
		clientInputs.add(ImmutableAuthenticationExtensionsClientInput.credProps);
		return  PublicKeyCredentialCreationOptions.builder()
				.attestation(AttestationConveyancePreference.NONE)
				.user(userEntity)
				.pubKeyCredParams(PublicKeyCredentialParameters.RS256, PublicKeyCredentialParameters.ES256)
				.authenticatorSelection(authenticatorSelection)
				.challenge(challenge)
				.rp(rp)
				.extensions(clientInputs)
				.timeout(Duration.ofMillis(60000));
	}
}
