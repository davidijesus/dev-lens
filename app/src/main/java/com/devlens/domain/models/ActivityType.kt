package com.devlens.domain.models

enum class ActivityType(val label: String) {
    Feature("Feature"),
    Bugfix("Bugfix"),
    Refactoring("Refatoracao"),
    Support("Suporte"),
    TechnicalMeeting("Reuniao tecnica"),
    CodeReview("Revisao de codigo"),
    Performance("Performance"),
    TechnicalDebt("Divida tecnica"),
    Documentation("Documentacao"),
    Other("Outro")
}
