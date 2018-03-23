package test.custom.id

class Toy {
    UUID id
    String name
    String color

    static searchable = true

    static mapping = {
        id(generator: "uuid2", type: "uuid-char", length: 36)
    }

    static constraints = {
        name(nullable: true)
    }
}
