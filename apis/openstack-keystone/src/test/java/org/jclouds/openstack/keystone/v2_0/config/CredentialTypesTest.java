/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.openstack.keystone.v2_0.config;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jclouds.openstack.keystone.v2_0.domain.PasswordCredentials;
import org.jclouds.openstack.keystone.v2_0.domain.TokenCredentials;
import org.jclouds.openstack.keystone.v2_0.functions.AuthenticatePasswordCredentials;
import org.jclouds.openstack.keystone.v2_0.functions.AuthenticateTokenCredentials;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

@Test(groups = "unit", testName = "CredentialTypesTest")
public class CredentialTypesTest {

   public void testCredentialTypeOfWhenValid() {
      assertEquals(CredentialTypes.credentialTypeOf(PasswordCredentials.createWithUsernameAndPassword("username",
               "password")), CredentialTypes.PASSWORD_CREDENTIALS);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testCredentialTypeOfWithoutAnnotation() {
      CredentialTypes.credentialTypeOf("");
   }

   public void testIndexByCredentialTypeWhenValid() {
      assertEquals(CredentialTypes.indexByCredentialType(
               ImmutableSet.of(PasswordCredentials.createWithUsernameAndPassword("username", "password"))).keySet(),
               ImmutableSet.of(CredentialTypes.PASSWORD_CREDENTIALS));
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testIndexByCredentialTypeWithoutAnnotation() {
      CredentialTypes.indexByCredentialType(ImmutableSet.of(""));
   }

   public void testTokenCredentials() {
      assertEquals(CredentialTypes.credentialTypeOf(
               TokenCredentials.builder().id("d419ee71f234012a9b4525727995964").build()),
               CredentialTypes.TOKEN_CREDENTIALS);
   }

   public void testNonRetryableCredentialType() {
      final AuthenticateTokenCredentials authentication = new AuthenticateTokenCredentials(null);
      assertFalse(CredentialTypes.credentialTypeObjectOf(authentication).retryable());
   }

   public void testDefaultRetryableCredentialType() {
      final AuthenticatePasswordCredentials authentication = new AuthenticatePasswordCredentials(null);
      assertTrue(CredentialTypes.credentialTypeObjectOf(authentication).retryable());
   }
}
