package io.github.a13e300.tools.copy_sign;

import com.android.build.api.variant.ApplicationVariant;

import io.github.a13e300.tools.copy_sign.utils.StringUtils;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Sync;
import org.gradle.api.tasks.TaskProvider;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

import io.github.a13e300.tools.copy_sign.utils.SignCopy;

public final class CopySignDecorator {
    private final Project project;
    private final CopySignExtension extension;

    public CopySignDecorator(final Project project, final CopySignExtension extension) {
        this.project = project;
        this.extension = extension;
    }

    public void decorateVariant(final ApplicationVariant variant) {
        final TaskProvider<Task> pack = project.getTasks().named("package" + StringUtils.capitalize(variant.getName()));

        final TaskProvider<Task> copySign = project.getTasks().register(
                "copySign" + StringUtils.capitalize(variant.getName()),
                Task.class,
                task -> {
                    final Provider<File> apkFile = pack.map(p ->
                            p.getOutputs().getFiles().getFiles().stream()
                                    .flatMap(f -> {
                                        if (f.isDirectory()) {
                                            return Stream.of(Objects.requireNonNull(f.listFiles()));
                                        } else {
                                            return Stream.of(f);
                                        }
                                    })
                                    .filter(f -> f.getName().endsWith(".apk"))
                                    .findAny()
                                    .orElseThrow()
                    );

                    task.doLast((task1) -> {
                        if (extension.srcPath == null) return;

                        File src = Objects.requireNonNull(extension.srcPath);
                        File dst = apkFile.get();

                        System.out.println("copy signature from " + src + " to " + dst);

                        try {
                            SignCopy.copySign(src, dst);
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
        );

        pack.get().finalizedBy(copySign);
    }
}
