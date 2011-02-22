package com.magi.magento;

public enum Carrier {

    DHL("dhl", "DHL"),
    FedEx("fedex", "Fed Ex"),
    UPS("ups", "UPS"),
    USPS("usps", "US Postal Service");

    private final String code;

    private final String name;

    private Carrier(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Carrier fromName(String name) {
        for( Carrier c : Carrier.values() ) {
            if( c.name.equals(name)) {
                return c;
            }
        }
        throw new RuntimeException("No carrier found for "+name);
    }

    public String toString() {
        return code;
    }
}
