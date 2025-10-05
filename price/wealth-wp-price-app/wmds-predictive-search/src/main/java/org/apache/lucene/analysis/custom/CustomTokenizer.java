/*
 */
package org.apache.lucene.analysis.custom;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * <p>
 * <b> CustomTokenizer. </b>
 * </p>
 */
public class CustomTokenizer extends Tokenizer {
    /** A private instance of the JFlex-constructed scanner */
    private final CustomTokenizerImpl scanner;

    public static final int COMMON = 0;

    /** String token types that correspond to token type int constants */
    public static final String[] TOKEN_TYPES = new String[] {"<COMMON>"};

    void setInput(final Reader reader) {
        this.input = reader;
    }

    private int maxTokenLength = CustomAnalyzer.DEFAULT_MAX_TOKEN_LENGTH;

    /**
     * Set the max allowed token length. Any token longer than this is skipped.
     */
    public void setMaxTokenLength(final int length) {
        this.maxTokenLength = length;
    }

    /** @see #setMaxTokenLength */
    public int getMaxTokenLength() {
        return this.maxTokenLength;
    }

    /**
     * Creates a new instance of the {@link StandardTokenizer}. Attaches the
     * <code>input</code> to a newly created JFlex scanner.
     */
    public CustomTokenizer(final Reader input) {
        this.input = input;
        this.scanner = new CustomTokenizerImpl(input);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.lucene.analysis.TokenStream#next()
     */
    public Token next(final Token reusableToken) throws IOException {
        assert reusableToken != null;
        int posIncr = 1;

        while (true) {
            int tokenType = this.scanner.getNextToken();

            if (tokenType == CustomTokenizerImpl.YYEOF) {
                return null;
            }

            if (this.scanner.yylength() <= this.maxTokenLength) {
                reusableToken.clear();
                reusableToken.setPositionIncrement(posIncr);
                this.scanner.getText(reusableToken);
                final int start = this.scanner.yychar();
                reusableToken.setStartOffset(start);
                reusableToken.setEndOffset(start + reusableToken.termLength());
                // This 'if' should be removed in the next release. For now, it
                // converts
                // invalid acronyms to HOST. When removed, only the 'else' part
                // should
                // remain.
                return reusableToken;
            } else {
                // When we skip a too-long term, we still increment the
                // position increment
                posIncr++;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.lucene.analysis.TokenStream#reset()
     */
    public void reset() throws IOException {
        super.reset();
        this.scanner.yyreset(this.input);
    }

    public void reset(final Reader reader) throws IOException {
        this.input = reader;
        reset();
    }

}
