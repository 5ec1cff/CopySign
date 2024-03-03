package io.github.a13e300.tools.copy_sign;

import com.android.build.api.variant.ApplicationAndroidComponentsExtension;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import javax.annotation.Nonnull;

@SuppressWarnings("UnstableApiUsage")
public class CopySignPlugin implements Plugin<Project> {

    @Override
    public void apply(@Nonnull final Project target) {
        if (!target.getPlugins().hasPlugin("com.android.application")) {
            throw new GradleException("com.android.application not applied");
        }

        final CopySignExtension extension = target.getExtensions()
                .create("copySign", CopySignExtension.class);

        target.getExtensions().configure(ApplicationAndroidComponentsExtension.class, components -> {
            final CopySignDecorator decorator = new CopySignDecorator(target, extension);
            components.onVariants(components.selector().all(), variant -> {
                target.afterEvaluate(prj -> decorator.decorateVariant(
                        variant
                ));
            });
        });
    }
}
