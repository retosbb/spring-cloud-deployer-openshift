package org.springframework.cloud.deployer.spi.openshift.resources.buildConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.cloud.deployer.spi.core.AppDefinition;
import org.springframework.cloud.deployer.spi.core.AppDeploymentRequest;
import org.springframework.core.io.Resource;

import com.google.common.collect.ImmutableMap;

import io.fabric8.openshift.api.model.BuildConfig;
import io.fabric8.openshift.api.model.BuildRequest;
import io.fabric8.openshift.api.model.BuildTriggerPolicyBuilder;
import io.fabric8.openshift.client.mock.OpenShiftServer;

public class BuildConfigFactoryTest {

	@Rule
	public OpenShiftServer server = new OpenShiftServer();

	private BuildConfigFactory buildConfigFactory;

	@Test
	public void buildBuildConfig() {
		buildConfigFactory = new BuildConfigFactory() {

			@Override
			protected BuildRequest buildBuildRequest(final AppDeploymentRequest request, final String appId) {
				return null;
			}
		};

		BuildConfig buildConfig = buildConfigFactory.buildBuildConfig(
				new AppDeploymentRequest(new AppDefinition("testapp", null), mock(Resource.class)), "testapp-source",
				ImmutableMap.of("spring-app-id", "testapp-source"));

		assertThat(buildConfig.getMetadata().getName()).isEqualTo("testapp-source");
		assertThat(buildConfig.getMetadata().getLabels()).isEqualTo(ImmutableMap.of("spring-app-id", "testapp-source"));
		assertThat(buildConfig.getSpec().getTriggers())
				.containsOnly(new BuildTriggerPolicyBuilder().withNewImageChange().endImageChange().build());
	}
}
