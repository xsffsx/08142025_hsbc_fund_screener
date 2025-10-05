/**
 * COPYRIGHT. hhhh HOLDINGS PLC 2013. ALL RIGHTS RESERVED.
 * 
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
 
package org.apache.lucene.analysis.custom;


import org.apache.lucene.analysis.Token;

%%

%class CustomTokenizerImpl
%unicode
%integer
%function getNextToken
%pack
%char

%{

public static final int COMMON          = CustomTokenizer.COMMON;

public static final String [] TOKEN_TYPES = CustomTokenizer.TOKEN_TYPES;

public final int yychar()
{
    return yychar;
}

/**
 * Fills Lucene token with the current token text.
 */
final void getText(Token t) {
  t.setTermBuffer(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead);
}
%}

SPEC       = [\u0021-\u002f\u003a-\u0040\u005b-\u0060\u007B-\u007E\uff00-\uffff\u0100-\u02af\u0370-\u4DFF]

// Chinese
CJK         = [\u4e00-\u9fbf]

/** Don't ignore the withe space */
WHITESPACE = \r\n | [ \r\n\t\f]

// basic word: a sequence of digits & letters (includes Thai to enable ThaiAnalyzer to function)
COMMON   = ([:letter:]|{SPEC}|[:digit:]|{CJK}|{WHITESPACE})+

%%

{COMMON}                                                     { return COMMON; }

/** Ignore the rest */
.                                               { /* ignore */ }
