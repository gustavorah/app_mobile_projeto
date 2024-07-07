package com.gustavo.projeto.model

import java.io.Serializable

data class Crud1ImpactoModel (
    var id: String? = null,
    var impacto: Double? = null,
    var userId: String? = null
) : Serializable