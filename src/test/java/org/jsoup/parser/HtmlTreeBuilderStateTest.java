package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.HtmlTreeBuilderState.Constants;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HtmlTreeBuilderStateTest {
    static List<Object[]> findArrays() {
        ArrayList<Object[]> array = new ArrayList<>();
        Field[] fields = Constants.class.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType().isArray()) {
                try {
                    array.add((Object[]) field.get(null));
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        return array;
    }

    static void ensureSorted(List<Object[]> constants) {
        for (Object[] array : constants) {
            Object[] copy = Arrays.copyOf(array, array.length);
            Arrays.sort(array);
            assertArrayEquals(array, copy);
        }
    }

    @Test
    public void ensureArraysAreSorted() {
        List<Object[]> constants = findArrays();
        ensureSorted(constants);
        assertEquals(38, constants.size());
    }

    /*
        These tests reach previously untested (or lightly) states in HTMLTreeBuilderState.
        These tests are important because they check if errors are being logged in appropriate
        situations.
    */
    // InSelectInTable state was never reached by previous tests
    @Test public void unexpectedStartTagInSelectInTableTest() {
        String html = "<table><select><td></td></select></table>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [StartTag] when in state [InSelectInTable]";
        assertEquals(expectedError, parser.getErrors().get(1).getErrorMessage());
    }

    @Test public void unexpectedEndTagInSelectInTableTest() {
        String html = "<table><select></td></select></table>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [EndTag] when in state [InSelectInTable]";
        assertEquals(expectedError, parser.getErrors().get(1).getErrorMessage());
    }

    @Test public void errorWhenDocTypeInHeadTest() {
        String html = "<head><!DOCTYPE html><title>hi</title></head>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [Doctype] when in state [InHead]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }

    @Test public void headInHeadTest() {
        String html = "<head><head></head>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [StartTag] when in state [InHead]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }

    @Test public void unexpectedTitleAfterHeadTest() {
        String html = "<head></head><title>hello world</title>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [StartTag] when in state [AfterHead]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }

    @Test public void unexpectedScriptAfterHeadTest() {
        String html = "<head></head><script>alert('hello world');</script>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [StartTag] when in state [AfterHead]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }

    // Frameset error states
    @Test public void ignoreFramesetInBodyTest() {
        String html = "<body>a<frameset></frameset></body>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        parser.getErrors();
        String expectedError = "Unexpected token [StartTag] when in state [InBody]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }

    @Test public void DoctypeInFramesetTest() {
        String html = "<frameset><!DOCTYPE html></frameset>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [Doctype] when in state [InFrameset]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }

    @Test public void EOFInFramesetTest() {
        String html = "<frameset>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [EOF] when in state [InFrameset]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }

    @Test public void UnexpectedStartTagInFramesetTest() {
        String html = "<frameset><a></frameset>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [StartTag] when in state [InFrameset]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }

    @Test public void DoctypeAfterFrameSetTest() {
        String html = "<frameset></frameset><!DOCTYPE html>";
        Parser parser = Parser.htmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        String expectedError = "Unexpected token [Doctype] when in state [AfterFrameset]";
        assertEquals(expectedError, parser.getErrors().get(0).getErrorMessage());
    }
}
