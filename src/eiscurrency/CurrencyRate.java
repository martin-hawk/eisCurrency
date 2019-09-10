package eiscurrency;

public class CurrencyRate {

    private String type;
    private String dt;
    private String ccyFrom;
    private float amtFrom;
    private String ccyTo;
    private float amtTo;

    public CurrencyRate(String type, String dt, String ccyFrom, float amtFrom, String ccyTo, float amtTo) {
        this.type = type;
        this.dt = dt;
        this.ccyFrom = ccyFrom;
        this.amtFrom = amtFrom;
        this.ccyTo = ccyTo;
        this.amtTo = amtTo;
    }

    public String getType() {
        return type;
    }

    public String getDt() {
        return dt;
    }

    public String getCcyFrom() {
        return ccyFrom;
    }

    public float getAmtFrom() {
        return amtFrom;
    }

    public String getCcyTo() {
        return ccyTo;
    }

    public float getAmtTo() {
        return amtTo;
    }

    @Override
    public String toString() {
        return "Valiutos " + this.ccyTo + " kursas " + this.dt + " datai yra " + this.amtTo;
//        return this.type + " " + this.ccyFrom + " " + this.amtFrom + " " + this.dt + " " + this.ccyTo + " " + this.amtTo;
    }

}
