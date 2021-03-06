package de.zalando.zally.rule.zalando

import com.typesafe.config.Config
import de.zalando.zally.rule.api.Check
import de.zalando.zally.rule.api.Severity
import de.zalando.zally.rule.api.Violation
import de.zalando.zally.rule.api.Rule
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired

@Rule(
        ruleSet = ZalandoRuleSet::class,
        id = "166",
        severity = Severity.MUST,
        title = "Avoid Link in Header Rule"
)
class AvoidLinkHeadersRule(@Autowired rulesConfig: Config) : HttpHeadersRule(rulesConfig) {
    private val description = "Do Not Use Link Headers with JSON entities"

    @Check(severity = Severity.MUST)
    override fun validate(swagger: Swagger): Violation? {
        return super.validate(swagger)
    }

    override fun isViolation(header: String) = header == "Link"

    override fun createViolation(paths: List<String>): Violation {
        return Violation(description, paths)
    }
}
