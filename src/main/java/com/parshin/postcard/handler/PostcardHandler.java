package com.parshin.postcard.handler;

import com.parshin.postcard.builder.PostcardXmlTag;
import com.parshin.postcard.entity.AbstractPostcard;
import com.parshin.postcard.entity.PostcardInMuseum;
import com.parshin.postcard.entity.PostcardInPrivateCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.logging.log4j.Level;

import java.time.Year;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static com.parshin.postcard.builder.PostcardXmlTag.*;

public class PostcardHandler extends DefaultHandler {
    private static final Logger log = LogManager.getLogger();
    private EnumSet<PostcardXmlTag> postcardXmlTagEnumSet;
    private Set<AbstractPostcard> postcards;
    private AbstractPostcard postcard;
    private PostcardXmlTag postcardXmlTag;


    public PostcardHandler() {
        postcards = new HashSet<>();
        postcardXmlTagEnumSet = EnumSet.range(THEME, COLLECTION_LOCATION);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        String museumPostcardTag = POSTCARD_IN_THE_MUSEUM.getValue();
        String inCollectionPostcardTag = POSTCARD_IN_A_PRIVATE_COLLECTION.getValue();
        if (museumPostcardTag.equals(qName) || inCollectionPostcardTag.equals(qName)) {
            postcard = museumPostcardTag.equals(qName) ? new PostcardInMuseum() : new PostcardInPrivateCollection();
            postcard.setPostcardId(attributes.getValue(POSTCARD_ID.getValue()));
            String optionalAuthor = attributes.getValue(AUTHOR.getValue());
            postcard.setAuthor(optionalAuthor == null ? AUTHOR_BY_DEFAULT.getValue() : optionalAuthor);
        } else {
            PostcardXmlTag temp = PostcardXmlTag.valueOf(qName.toUpperCase());
            if (postcardXmlTagEnumSet.contains(temp)) {
                postcardXmlTag = temp;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String data = new String(ch, start, length).trim();
        if (postcardXmlTag != null) {
            switch (postcardXmlTag) {
                case THEME -> postcard.setTheme(data);
                case YEAR -> postcard.setYear(Year.parse(data));
                case TYPE -> postcard.setType(data);
                case COUNTRY -> postcard.setCountry(data);
                case VALUABLE -> postcard.setValuable(data);
                case MUSEUM_NAME -> {
                    PostcardInMuseum postcardInMuseum = (PostcardInMuseum) postcard;
                    postcardInMuseum.setMuseumName(data);
                }
                case MUSEUM_LOCATION -> {
                    PostcardInMuseum postcardInMuseum = (PostcardInMuseum) postcard;
                    postcardInMuseum.setMuseumLocation(data);
                }
                case COLLECTORS_NAME -> {
                    PostcardInPrivateCollection postcardInCollection = (PostcardInPrivateCollection) postcard;
                    postcardInCollection.setCollectorsName(data);
                }
                case COLLECTION_LOCATION -> {
                    PostcardInPrivateCollection postcardInCollection = (PostcardInPrivateCollection) postcard;
                    postcardInCollection.setCollectionLocation(data);
                }
            }
            postcardXmlTag = null;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        String unlimitedTariffTag = POSTCARD_IN_THE_MUSEUM.getValue();
        String limitedTariffTag = POSTCARD_IN_A_PRIVATE_COLLECTION.getValue();
        if (unlimitedTariffTag.equals(qName) || limitedTariffTag.equals(qName)) {
            log.log(Level.INFO, postcard.toString());
            postcards.add(postcard);
        }
    }

    public Set<AbstractPostcard> getPostcards() {
        return postcards;
    }

}
