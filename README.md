# CopySign

Copy signing blocks from other apk to your app.

You need CorePatch to install it.

## usage

0. Publish to Maven Local

1. Apply this plugin to your project

```kt
plugins {
    id("io.github.a13e300.tools.copy_sign") version "1.0"
}
```

2. Configure the signature source apk

```kt
copySign {
    setSrcPath(File("path/to/apk.apk"))
}
```

3. run `assemble` or `install` task

