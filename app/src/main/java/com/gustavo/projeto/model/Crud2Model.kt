package com.gustavo.projeto.model

import java.io.Serializable

data class Crud2Model (
    var id: String? = null,
    var quantidade: String? = null,
    var category: String? = null,
    var userId: String? = null
) : Serializable