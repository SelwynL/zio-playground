# zio-playground
Playground for working with ZIO

For more information about ZIO see the following:
- GitHub Repo: https://github.com/zio/zio
- Overview: https://zio.dev/docs/overview/overview_index
- Data Types: https://zio.dev/docs/datatypes/datatypes_index
- Module Pattern: https://zio.dev/docs/howto/howto_use_module_pattern
- Testing: https://zio.dev/docs/howto/howto_test_effects

# Run
To run this application
```
sbt clean run
```

# Build Artifact
To build a fat JAR which includes all dependencies
```
sbt clean assembly
```

# Plugins
- scalafmt: https://scalameta.org/scalafmt/
- wartremover: https://www.wartremover.org/