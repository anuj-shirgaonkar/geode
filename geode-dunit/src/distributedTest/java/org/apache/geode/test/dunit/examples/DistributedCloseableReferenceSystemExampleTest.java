/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geode.test.dunit.examples;

import static java.util.Arrays.asList;
import static org.apache.geode.distributed.ConfigurationProperties.LOCATORS;
import static org.apache.geode.test.dunit.VM.getController;
import static org.apache.geode.test.dunit.VM.getVM;
import static org.apache.geode.test.dunit.rules.DistributedRule.getLocators;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.Properties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.geode.distributed.DistributedSystem;
import org.apache.geode.test.dunit.VM;
import org.apache.geode.test.dunit.rules.DistributedCloseableReference;

@SuppressWarnings("serial")
public class DistributedCloseableReferenceSystemExampleTest implements Serializable {

  @Rule
  public DistributedCloseableReference<DistributedSystem> system =
      new DistributedCloseableReference<>();

  @Before
  public void setUp() {
    Properties configProperties = new Properties();
    configProperties.setProperty(LOCATORS, getLocators());

    for (VM vm : asList(getVM(0), getVM(1), getVM(2), getVM(3), getController())) {
      vm.invoke(() -> {
        system.set(DistributedSystem.connect(configProperties));
      });
    }
  }

  @Test
  public void eachVmHasItsOwnSystemConnection() {
    for (VM vm : asList(getVM(0), getVM(1), getVM(2), getVM(3), getController())) {
      vm.invoke(() -> {
        assertThat(system.get()).isNotNull();
      });
    }
  }
}