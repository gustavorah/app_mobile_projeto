package com.gustavo.projeto.model

import java.io.Serializable

data class Crud1Model (
    var id: String? = null,
    var nome: String? = null,
    var email: String? = null,
    var idade: String? = null
) : Serializable