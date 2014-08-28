package marketplace

enum RecommendedLayout {

    ACCORDION("Accordion"),
    DESKTOP("Desktop"),
    PORTAL("Portal"),
    TABBED("Tabbed")

    RecommendedLayout(String description) {
        this.description = description;
    }

    private final String description

    public String description() { return description }

    @Override
    public String toString() {
        description
    }
}
