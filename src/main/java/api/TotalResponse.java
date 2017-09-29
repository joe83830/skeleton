package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TotalRecord;

import java.math.BigDecimal;
import java.sql.Time;

public class TotalResponse {

    @JsonProperty
    Integer id;

    @JsonProperty
    String merchantName;

    @JsonProperty
    BigDecimal value;

    @JsonProperty
    String tag;


    public TotalResponse(TotalRecord dbRecord) {
        this.merchantName = dbRecord.getMerchant();
        this.value = dbRecord.getAmount();
        this.id = dbRecord.getId();
        this.tag = dbRecord.getTag();

    }


}
