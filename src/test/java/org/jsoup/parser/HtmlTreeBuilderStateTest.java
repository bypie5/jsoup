package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.HtmlTreeBuilderState.Constants;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HtmlTreeBuilderStateTest {
    @Test
    public void ensureArraysAreSorted() {
        String[][] arrays = {
            Constants.InBodyStartToHead,
            Constants.InBodyStartPClosers,
            Constants.Headings,
            Constants.InBodyStartPreListing,
            Constants.InBodyStartLiBreakers,
            Constants.DdDt,
            Constants.Formatters,
            Constants.InBodyStartApplets,
            Constants.InBodyStartEmptyFormatters,
            Constants.InBodyStartMedia,
            Constants.InBodyStartInputAttribs,
            Constants.InBodyStartOptions,
            Constants.InBodyStartRuby,
            Constants.InBodyStartDrop,
            Constants.InBodyEndClosers,
            Constants.InBodyEndAdoptionFormatters,
            Constants.InBodyEndTableFosters,
            Constants.InCellNames,
            Constants.InCellBody,
            Constants.InCellTable,
            Constants.InCellCol,
        };

        for (String[] array : arrays) {
            String[] copy = Arrays.copyOf(array, array.length);
            Arrays.sort(array);
            assertArrayEquals(array, copy);
        }
    }

    // Test which activates the process() function in HtmlTreeBuilder
    @Test public void parsesRoughAttributeString() {
        String html = "<head></head><h1>hello world</h1>";

        Element el = Jsoup.parse(html);//.getElementsByTag("a").get(0);
        /*Attributes attr = el.attributes();
        assertEquals(7, attr.size());
        assertEquals("123", attr.get("id"));
        assertEquals("baz = 'bar'", attr.get("class"));
        assertEquals("border: 2px", attr.get("style"));
        assertEquals("", attr.get("qux"));
        assertEquals("", attr.get("zim"));
        assertEquals("12", attr.get("foo"));
        assertEquals("18", attr.get("mux"));*/
    }

}
