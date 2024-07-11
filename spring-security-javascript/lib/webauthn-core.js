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

"use strict";

import base64url from "./base64url.js";
import http from "./http.js";

async function isConditionalMediationAvailable() {
  return !!(
    document.PublicKeyCredential &&
    document.PublicKeyCredential.isConditionalMediationAvailable &&
    (await document.PublicKeyCredential.isConditionalMediationAvailable())
  );
}

async function authenticate(headers, contextPath, useConditionalMediation) {
  const abortController = new AbortController();
  // FIXME: add contextRoot
  const options = await http.post(`${contextPath}/webauthn/authenticate/options`, headers).then((r) => r.json());
  // FIXME: Use https://www.w3.org/TR/webauthn-3/#sctn-parseRequestOptionsFromJSON
  const decodedOptions = { ...options, challenge: base64url.decode(options.challenge) };

  // Invoke the WebAuthn get() method.
  const credentialOptions = {
    publicKey: decodedOptions,
    signal: abortController.signal,
  };
  if (useConditionalMediation) {
    // Request a conditional UI
    credentialOptions.mediation = "conditional";
  }
  const cred = await navigator.credentials.get(credentialOptions);
  const { response, type: credType } = cred;
  let userHandle;
  if (response.userHandle) {
    userHandle = base64url.encode(response.userHandle);
  }
  const body = {
    id: cred.id,
    rawId: base64url.encode(cred.rawId),
    response: {
      authenticatorData: base64url.encode(response.authenticatorData),
      clientDataJSON: base64url.encode(response.clientDataJSON),
      signature: base64url.encode(response.signature),
      userHandle,
    },
    credType,
    clientExtensionResults: cred.getClientExtensionResults(),
    authenticatorAttachment: cred.authenticatorAttachment,
  };

  // FIXME: add contextRoot
  // POST the response to the endpoint that calls
  const authenticationResponse = await http.post(`${contextPath}/login/webauthn`, headers, body).then((response) => {
    if (response.ok) {
      return response.json();
    } else {
      return { errorUrl: "/login?error" };
    }
  });

  // Show UI appropriate for the `verified` status
  if (authenticationResponse && authenticationResponse.authenticated) {
    window.location.href = authenticationResponse.redirectUrl;
  } else {
    window.location.href = authenticationResponse.errorUrl;
  }
}

export default {
  authenticate,
  isConditionalMediationAvailable,
};
