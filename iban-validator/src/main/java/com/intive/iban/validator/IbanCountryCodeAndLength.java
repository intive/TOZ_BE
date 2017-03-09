package com.intive.iban.validator;

import java.util.HashMap;

public enum IbanCountryCodeAndLength {

    //source: https://www.swift.com/sites/default/files/resources/swift_standards_ibanregistry.pdf
    AL("AL", 28),
    AD("AD", 24),
    AT("AT", 20),
    AZ("AZ", 28),
    BH("BH", 22),
    BE("BE", 16),
    BA("BA", 20),
    BR("BR", 29),
    BG("BG", 22),
    CR("CR", 21),
    HR("HR", 21),
    CY("CY", 28),
    CZ("CZ", 24),
    DK("DK", 18),
    DO("DO", 28),
    EE("EE", 20),
    FI("FI", 18),
    FR("FR", 27),
    GE("GE", 22),
    DE("DE", 22),
    GI("GI", 23),
    GR("GR", 27),
    GT("GT", 28),
    HU("HU", 28),
    IS("IS", 26),
    IE("IE", 22),
    IL("IL", 23),
    IT("IT", 27),
    JO("JO", 30),
    KZ("KZ", 20),
    XK("XK", 20),
    KW("KW", 30),
    LV("LV", 21),
    LB("LB", 28),
    LI("LI", 21),
    LT("LT", 20),
    LU("LU", 20),
    MK("MK", 19),
    MT("MT", 31),
    MR("MR", 27),
    MU("MU", 30),
    MD("MD", 24),
    MC("MC", 27),
    ME("ME", 22),
    NL("NL", 18),
    NO("NO", 15),
    PK("PK", 24),
    PS("PS", 29),
    PL("PL", 28),
    PT("PT", 25),
    QA("QA", 29),
    RO("RO", 24),
    LC("LC", 32),
    SM("SM", 27),
    ST("ST", 25),
    SA("SA", 24),
    RS("RS", 22),
    SC("SC", 31),
    SK("SK", 24),
    SI("SI", 19),
    ES("ES", 24),
    SE("SE", 24),
    CH("CH", 21),
    TL("TL", 23),
    TN("TN", 24),
    TR("TR", 26),
    UA("UA", 29),
    AE("AE", 23),
    GB("GB", 22),
    VG("VG", 24);

    private final String code;
    private final int codeLength;
    private static final HashMap<String, Integer> codes = new HashMap<String, Integer>();

    public static boolean isCountryCodeValid(String code) {
        return codes.containsKey(code);
    }

    public static int getLengthByCountryCodeOrNull(String code) {
        return codes.get(code);
    }

    IbanCountryCodeAndLength(final String code, final int codeLength) {
        this.code = code;
        this.codeLength = codeLength;
    }

    static {
        for (final IbanCountryCodeAndLength ibanCountryCodeAndLength : values()) {
            codes.put(ibanCountryCodeAndLength.code, ibanCountryCodeAndLength.codeLength);
        }
    }
}
