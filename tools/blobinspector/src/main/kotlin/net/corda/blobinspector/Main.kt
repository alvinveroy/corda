package net.corda.blobinspector

import picocli.CommandLine
import picocli.CommandLine.*
import java.net.URL
import java.nio.file.InvalidPathException
import java.nio.file.Paths

@Command(
        name = "Blob Inspector",
        version = ["Print version here ..."],
        mixinStandardHelpOptions = true, // add --help and --version options
        description = ["Inspect AMQP serialised binary blobs"]
)
class MyApp : Runnable {
    @Parameters(index = "0", paramLabel = "SOURCE", description = ["URL or file path to the blob"], converter = [SourceConverter::class])
    private var source: URL? = null

    @Option(names = ["--verbose"], description = ["Enable debug output"])
    private var verbose: Boolean = false

    @Option(names = ["--schema"], description = ["Print the blob's schema"])
    private var schema: Boolean = false

    @Option(names = ["--transforms"], description = ["Print the blob's transforms schema"])
    private var transforms: Boolean = false

    override fun run() {
        val config = Config(verbose, schema, transforms, true)
        inspectBlob(config, source!!.readBytes())
    }
}

private class SourceConverter : ITypeConverter<URL> {
    override fun convert(value: String): URL {
        return try {
            Paths.get(value).toUri().toURL()
        } catch (e: InvalidPathException) {
            URL(value)
        }
    }
}

fun main(args: Array<String>) = CommandLine.run(MyApp(), *args)
