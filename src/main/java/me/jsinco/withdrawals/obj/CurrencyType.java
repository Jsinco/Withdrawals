package me.jsinco.withdrawals.obj;

public enum CurrencyType {

    EXPERIENCE("experience"),
    MONEY("dollars"),
    POINTS("Solcoins");

    final String name;

    CurrencyType(String name) {
        this.name = name;
    }

    public String getCurrencyName() {
        return name;
    }
}
