package dao;

import generated.tables.Tags;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
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

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }

    public void toggle(String tags, int id){

        ReceiptsRecord receiptsRecord = dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.eq(id)).fetchOne();
        if(receiptsRecord != null){
            if(dsl.selectFrom(TAGS).where(TAGS.ID.eq(id)).and(TAGS.TAG.eq(tags)).fetchOne() == null){
                dsl.insertInto(TAGS).values(id, tags).execute();

            } else {
                dsl.deleteFrom(TAGS).where(TAGS.TAG.eq(tags)).and(TAGS.ID.eq(id)).execute();

            }
        }

    }

    public List<ReceiptsRecord> getmethod(String tags){

        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.in(dsl.select(TAGS.ID).from(TAGS).where(TAGS.TAG.eq(tags)).fetch())).fetch();

    }
}
