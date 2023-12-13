package eda.teamred.service.model

import jakarta.persistence.Embeddable

@Embeddable
data class Address(val street: String,val number: String, val postCode: String)
