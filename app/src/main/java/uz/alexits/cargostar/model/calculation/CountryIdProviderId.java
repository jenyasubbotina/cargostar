package uz.alexits.cargostar.model.calculation;

public class CountryIdProviderId {
    private Long countryId;
    private Long providerId;

    public CountryIdProviderId() {
        this(null, null);
    }

    public CountryIdProviderId(final Long countryId, final Long providerId) {
        this.countryId = countryId;
        this.providerId = providerId;
    }

    public void setProviderId(final Long providerId) {
        this.providerId = providerId;
    }

    public void setCountryId(final Long countryId) {
        this.countryId = countryId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public Long getCountryId() {
        return countryId;
    }
}
