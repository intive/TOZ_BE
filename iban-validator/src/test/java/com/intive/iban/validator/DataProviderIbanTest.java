package com.intive.iban.validator;

import com.tngtech.java.junit.dataprovider.DataProvider;

public class DataProviderIbanTest {

    @DataProvider
    public static String[] dataProviderValidIban() {
        return new String[]{
                "60102010260000042270201111",
                " 61109010140000071219812874",
                "27114020040000300201355387 "};
    }

    @DataProvider
    public static String[] dataProviderInvalidIbanBadCountryCodeOrLength() {
        return new String[]{
                "6010201026000004227021111",
                "611090101400000712198128741",
                "271140200400003002013553"};
    }

    @DataProvider
    public static String[] dataProviderInvalidIbanBadFormat() {
        return new String[]{
                "60 1020 1026 0000 0422 7020 1111",
                "6110901014000007121981287 4",
                "2 7114020040000300201355387",
                "",
                " ",
                null};
    }

    @DataProvider
    public static String[] dataProviderInvalidIbanUnallowedCharacters() {
        return new String[]{
                "60-1020-1026-0000-0422-7020-1111",
                "61109010140000071219811\\n",
                "-7114020040000300201355387"};
    }
}
