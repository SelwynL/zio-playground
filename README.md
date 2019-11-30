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
This provides the option to run on eof the below programs

### CustomRuntimeEntry
Uses module pattern to compose a `Manager`  which performs file lookup and console logging leveraging `Files` and `Console` programs

### FileReaderEntry
Reads the given file `.gitignore` every 4 seconds and outputs the file contents

### ForkEntry
Leverages `forkAll` to fork `500` green threads that each sleep for `100ms` then return their assigned number to be joined into a `List[Int]`

### KafkaEntry
Kafka client that listens to topic `test` on `localhost:9092` and output the received events

### ScheduleEntry
Leverages `Schedule` to output the time every 2 seconds

# Plugins
- scalafmt: https://scalameta.org/scalafmt/
- wartremover: https://www.wartremover.org/