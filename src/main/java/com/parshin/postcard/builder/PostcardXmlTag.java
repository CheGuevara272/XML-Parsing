package com.parshin.postcard.builder;

public enum PostcardXmlTag {
    OLDCARDS("OldCards"),
    POSTCARD_ID("postcard_id"),
    AUTHOR("author"),
    AUTHOR_BY_DEFAULT("Author is unknown"),
    THEME("theme"),
    YEAR("year"),
    TYPE("type"),
    COUNTRY("country"),
    VALUABLE("valuable"),
    POSTCARD_IN_THE_MUSEUM("postcard_in_the_museum"),
    MUSEUM_NAME("museum_name"),
    MUSEUM_LOCATION("museum_location"),
    POSTCARD_IN_A_PRIVATE_COLLECTION("postcard_in_a_private_collection"),
    COLLECTORS_NAME("collectors_name"),
    COLLECTION_LOCATION("collection_location");

    private String value;

    PostcardXmlTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
