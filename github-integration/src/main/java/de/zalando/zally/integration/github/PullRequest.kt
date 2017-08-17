package de.zalando.zally.integration.github

import com.fasterxml.jackson.databind.ObjectMapper
import de.zalando.zally.integration.zally.Configuration
import org.apache.commons.io.IOUtils
import org.kohsuke.github.GHCommitState
import org.kohsuke.github.GHRepository
import java.nio.charset.StandardCharsets
import java.util.Optional

class PullRequest(private val yamlMapper: ObjectMapper,
                  private val repository: GHRepository,
                  private val commitHash: String) {

    fun updateCommitState(state: GHCommitState, url: String, description: String) {
        repository.createCommitStatus(commitHash, state, url, description, "Zally")
    }

    private fun getFileContents(path: String): Optional<String> {
        return Optional.ofNullable(repository.getTreeRecursive(commitHash, 1).getEntry(path))
                .map { IOUtils.toString(it.readAsBlob(), StandardCharsets.UTF_8) }
    }

    fun getConfiguration(): Optional<Configuration> {
        return getFileContents(ZALLY_CONFIGURATION_PATH).map {
            yamlMapper.readValue(it, Configuration::class.java)
        }
    }

    fun getSwaggerFile(): Optional<String> {
        return getConfiguration().flatMap {
            getFileContents(it.swaggerPath)
        }
    }

    companion object {
        private val ZALLY_CONFIGURATION_PATH = ".zally.yaml"
    }
}
