package danilfedorov.domain_core.model.firebase

enum class ProductField(val firebaseKey: String) {
    NAME("name"),
    AMOUNT("amount"),
    UNIT("unit"),
    SELECTED("selected");
}