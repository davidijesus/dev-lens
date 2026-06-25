package com.devlens.domain.models

enum class MisalignmentCategory(val label: String) {
    Aligned("Alinhado"),
    InvisibleHighValue("Alto valor invisivel"),
    OverestimatedLowValue("Baixo valor superestimado"),
    NeedsAttention("Atencao necessaria")
}
