package dao;

import api.ReceiptResponse;
import generated.tables.Total;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TotalRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<TotalRecord> getAllReceipts() {
        //return dsl.selectFrom().fetch();
        Result<Record4<String, BigDecimal, Integer, String>> a = dsl.select(TAGS.TAG, RECEIPTS.AMOUNT, RECEIPTS.ID, RECEIPTS.MERCHANT).from(RECEIPTS.leftJoin(TAGS).on(TAGS.ID.eq(RECEIPTS.ID))).fetch();

        List<TotalRecord> temp = new ArrayList<TotalRecord>();

        for (Record b: a) {
            TotalRecord tmprcd = new TotalRecord();
            tmprcd.setAmount(b.get(RECEIPTS.AMOUNT));
            tmprcd.setId(b.get(RECEIPTS.ID));
            tmprcd.setMerchant(b.get(RECEIPTS.MERCHANT));
            tmprcd.setTag(b.get(TAGS.TAG));
            temp.add(tmprcd);

        }
        return temp;
    }
}
