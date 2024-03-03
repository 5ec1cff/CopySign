package io.github.a13e300.tools.copy_sign;

import org.gradle.api.Action;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CopySignExtension {
    File srcPath = null;

    public void setSrcPath(@Nullable File path) {
        srcPath = path;
    }
}
