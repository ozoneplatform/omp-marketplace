package ozone.marketplace.enums

public enum OzoneSize {
    XSMALL(350, 480, "XSMALL", "Extra Small - 350 x 480"),
    SMALL(350, 768, "SMALL", "Small - 350 x 768"),
    MEDIUM(650, 1050, "MEDIUM", "Medium - 650 x 1050"),
    LARGE(1050, 1050, "LARGE", "Large - 1050 x 1050")

    private final Long height
    private final Long width
    private final String name
    private final String description
    OzoneSize(height, width, name, description) {
        this.height = height
        this.width = width
        this.name = name
        this.description = description
    }

    public static getSizeDescriptions() {
        def sizeDescriptions = []
        values().each {
            sizeDescriptions << [size: it.name, description: valueOf(it.name).description]
        }
        return sizeDescriptions
    }
}