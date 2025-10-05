/*
 */
package org.apache.lucene.analysis.custom;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter},
 * {@link LowerCaseFilter} and {@link StopFilter}, using a list of English stop
 * words.
 * 
 * @version $Id: CustomAnalyzer.java
 */
public class CustomAnalyzer extends Analyzer {

    private Set stopSet;

    /** Default maximum allowed token length */
    public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

    private int maxTokenLength = CustomAnalyzer.DEFAULT_MAX_TOKEN_LENGTH;

    /**
     * An array containing some common English words that are usually not
     * useful for searching.
     */
    public static final String[] STOP_WORDS = {};

    /** Builds an analyzer with the default stop words ({@link #STOP_WORDS}). */
    public CustomAnalyzer() {
        this(CustomAnalyzer.STOP_WORDS);
    }

    /** Builds an analyzer with the given stop words. */
    public CustomAnalyzer(final Set stopWords) {
        this.stopSet = stopWords;
    }

    /** Builds an analyzer with the given stop words. */
    public CustomAnalyzer(final String[] stopWords) {
        this.stopSet = StopFilter.makeStopSet(stopWords);
    }

    /**
     * Builds an analyzer with the stop words from the given file.
     * 
     * @see WordlistLoader#getWordSet(File)
     */
    public CustomAnalyzer(final File stopwords) throws IOException {
        this.stopSet = WordlistLoader.getWordSet(stopwords);
    }

    /**
     * Builds an analyzer with the stop words from the given reader.
     * 
     * @see WordlistLoader#getWordSet(Reader)
     */
    public CustomAnalyzer(final Reader stopwords) throws IOException {
        this.stopSet = WordlistLoader.getWordSet(stopwords);
    }

    /**
     * Constructs a {@link StandardTokenizer} filtered by a
     * {@link StandardFilter}, a {@link LowerCaseFilter} and a
     * {@link StopFilter}.
     */
    public TokenStream tokenStream(final String fieldName, final Reader reader) {
        CustomTokenizer tokenStream = new CustomTokenizer(reader);
        tokenStream.setMaxTokenLength(this.maxTokenLength);
        TokenStream result = new StandardFilter(tokenStream);
        result = new LowerCaseFilter(result);
        // result = new StopFilter(result, this.stopSet);
        return result;
    }

    private static final class SavedStreams {
        CustomTokenizer tokenStream;
        TokenStream filteredTokenStream;
    }

    public TokenStream reusableTokenStream(final String fieldName, final Reader reader) throws IOException {
        SavedStreams streams = (SavedStreams) getPreviousTokenStream();
        if (streams == null) {
            streams = new SavedStreams();
            setPreviousTokenStream(streams);
            streams.tokenStream = new CustomTokenizer(reader);
            streams.filteredTokenStream = new StandardFilter(streams.tokenStream);
            streams.filteredTokenStream = new LowerCaseFilter(streams.filteredTokenStream);
            // streams.filteredTokenStream = new
            // StopFilter(streams.filteredTokenStream, this.stopSet);
        } else {
            streams.tokenStream.reset(reader);
        }
        streams.tokenStream.setMaxTokenLength(this.maxTokenLength);
        return streams.filteredTokenStream;
    }

    /**
     * Set maximum allowed token length. If a token is seen that exceeds this
     * length then it is discarded. This setting only takes effect the next
     * time tokenStream or reusableTokenStream is called.
     */
    public void setMaxTokenLength(final int length) {
        this.maxTokenLength = length;
    }

    /**
     * @see #setMaxTokenLength
     */
    public int getMaxTokenLength() {
        return this.maxTokenLength;
    }

}