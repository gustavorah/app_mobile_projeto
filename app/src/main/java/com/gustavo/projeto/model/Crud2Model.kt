package com.gustavo.projeto.model

import java.io.Serializable

data class Crud1Model (
    var id: String? = null,
    var nome: String? = null,
    var descricao: String? = null,
    var localizacao: String? = null,
    var category: String? = null,
    var impacto: Double? = null,
    var userId: String? = null
) : Serializable