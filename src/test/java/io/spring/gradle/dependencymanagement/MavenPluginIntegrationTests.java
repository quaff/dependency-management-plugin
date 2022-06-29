/*
 * Copyright 2014-2022 the original author or authors.
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

package io.spring.gradle.dependencymanagement;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;

/**
 * Integration tests for interacting with the {@link org.gradle.api.plugins.MavenPlugin}.
 *
 * @author Andy Wilkinson
 */
public class MavenPluginIntegrationTests {

    @Rule
    public final GradleBuild gradleBuild = new GradleBuild();

    @Test
    public void generatedPomsAreCustomized() {
        this.gradleBuild.runner().withArguments("install").build();
        assertThatGeneratedPom().nodeAtPath("//dependencyManagement").isNotNull();
    }

    @Test
    public void customizationOfGeneratedPomsCanBeDisabled() {
        this.gradleBuild.runner().withArguments("install").build();
        assertThatGeneratedPom().nodeAtPath("//dependencyManagement").isNull();
    }

    @Test
    public void pomCustomizationDoesNotStopCustomConfToScopeMappingsFromWorking() {
        this.gradleBuild.runner().withArguments("install").build();
        assertThatGeneratedPom().textAtPath("/project/dependencies/dependency[groupId/text()='commons-logging']/scope").isEqualTo("compile");
    }

    private NodeAssert assertThatGeneratedPom() {
        return new NodeAssert(new File(this.gradleBuild.runner().getProjectDir(), "build/poms/pom-default.xml"));
    }

}
