package com.dummy.wmd.wpc.graphql.model;

/**
 * Theme code list:
 * 	EsgEnhanced,
 * 	Impact,
 * 	Thematic,
 * 	NotAssessed (for products has no ISIN code matched),
 * 	NotETI (for products has ISIN code matched, but classified as NOTETI)
 *
 * Below is the flow how it works:
 *
 * - Check if ISIN (Product) is in Wealth SI Product List, if yes then return the classification ESG Enhanced/Thematic/Imapact.
 * - If no then check if ISIN (Product ) is in Global Wealth Master list from Investment Navigator/Six Telekurs then return the classification as NOTETI.
 * - If the ISIN(Product) is not in Wealth SI Product List and not in Global Wealth Master list then return the classification as Not Assessed.
 */
@SuppressWarnings("java:S115")
public enum EsgClassification {
    EsgEnhanced,
    Impact,
    Thematic,
    NotETI,          // (for products has ISIN code matched, but classified as NOTETI)
    NotAssessed      // (for products has no ISIN code matched),
}
