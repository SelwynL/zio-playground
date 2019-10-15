logLevel :=  Level.Info

lazy val root = project.in(file("."))
  .settings(
    organization  :=  "org.selwyn",
    name          :=  "zio-playground",
    version       :=  "0.1.0",
    scalaVersion  :=  "2.12.8",
    description   :=  "Playground for working with ZIO",
    scalacOptions :=  BuildSettings.compilerOptions,
    scalacOptions in  (Compile, console) ~= { _.filterNot(Set("-Ywarn-unused-import")) },
    scalacOptions in  (Test, console)    := (scalacOptions in (Compile, console)).value,
    javacOptions  :=  BuildSettings.javaCompilerOptions,
    resolvers     ++= Dependencies.resolutionRepos,
    shellPrompt   :=  { _ => "zio-playground> "}
  )
  .settings(BuildSettings.wartremoverSettings)
  .settings(BuildSettings.scalaFmtSettings)
  .settings(BuildSettings.scalifySettings)
  .settings(BuildSettings.sbtAssemblySettings)
  .settings(
    libraryDependencies ++= Dependencies.Libraries
  )
