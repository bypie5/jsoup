package org.jsoup.parser;

import org.jsoup.MultiLocaleRule;
import org.jsoup.MultiLocaleRule.MultiLocaleTest;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 Tag tests.
 @author Jonathan Hedley, jonathan@hedley.net */
public class TagTest {
    @Rule public MultiLocaleRule rule = new MultiLocaleRule();

    @Test public void isCaseSensitive() {
        Tag p1 = Tag.valueOf("P");
        Tag p2 = Tag.valueOf("p");
        assertNotEquals(p1, p2);
    }

    @Test @MultiLocaleTest public void canBeInsensitive() {
        Tag script1 = Tag.valueOf("script", ParseSettings.htmlDefault);
        Tag script2 = Tag.valueOf("SCRIPT", ParseSettings.htmlDefault);
        assertSame(script1, script2);
    }

    @Test public void trims() {
        Tag p1 = Tag.valueOf("p");
        Tag p2 = Tag.valueOf(" p ");
        assertEquals(p1, p2);
    }

    @Test public void equality() {
        Tag p1 = Tag.valueOf("p");
        Tag p2 = Tag.valueOf("p");
        assertEquals(p1, p2);
        assertSame(p1, p2);
    }

    @Test public void divSemantics() {
        Tag div = Tag.valueOf("div");

        assertTrue(div.isBlock());
        assertTrue(div.formatAsBlock());
    }

    @Test public void pSemantics() {
        Tag p = Tag.valueOf("p");

        assertTrue(p.isBlock());
        assertFalse(p.formatAsBlock());
    }

    @Test public void imgSemantics() {
        Tag img = Tag.valueOf("img");
        assertTrue(img.isInline());
        assertTrue(img.isSelfClosing());
        assertFalse(img.isBlock());
    }

    @Test public void defaultSemantics() {
        Tag foo = Tag.valueOf("FOO"); // not defined
        Tag foo2 = Tag.valueOf("FOO");

        assertEquals(foo, foo2);
        assertTrue(foo.isInline());
        assertTrue(foo.formatAsBlock());
    }

    @Test(expected = IllegalArgumentException.class) public void valueOfChecksNotNull() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class) public void valueOfChecksNotEmpty() {
        Tag.valueOf(" ");
    }

    @Test public void knownTags() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("explain"));
    }

    @Test public void tagsOfSameTypeHashCodeTest() {
        Tag div = Tag.valueOf("div");
        Tag div2 = Tag.valueOf("div");

        assertEquals(1428650543, div.hashCode());
        assertEquals(1428650543, div2.hashCode());
    }

    @Test public void emptyTagHashTest() {
        Tag area = Tag.valueOf("area");

        assertEquals(247201364, area.hashCode());
    }

    @Test public void formSubmitHashTest() {
        Tag area = Tag.valueOf("input");

        assertEquals(-677361513, area.hashCode());
    }

    @Test public void formListedHashTest() {
        Tag output = Tag.valueOf("output");

        assertEquals(470938622, output.hashCode());
    }

    @Test public void keepWhiteSpaceHashTest() {
        Tag pre = Tag.valueOf("pre");

        assertEquals(391634687, pre.hashCode());
    }

    @Test public void selfClosingHashTest() {
        Tag br = Tag.valueOf("<foo />");
        br.setSelfClosing();

        assertEquals(-916668775, br.hashCode());
    }

    @Test public void differentCaseSameHashCodeTest() {
        Tag bigP = Tag.valueOf("P", ParseSettings.htmlDefault);
        Tag littleP = Tag.valueOf("p", ParseSettings.htmlDefault);

        assertEquals(-1486234415, bigP.hashCode());
        assertEquals(-1486234415, littleP.hashCode());
        assertTrue(littleP.hashCode() == bigP.hashCode());
    }
}
